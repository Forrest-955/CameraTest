package com.itep.mt.common.sys;

import com.itep.mt.common.util.Logger;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * 与硬件服务程序vendord的通信
 */

public class SysAccessor {
    private static final String HOST_NAME = null;   //null表示本地回环
    private static final int HOST_PORT = 8090;      //主机通信端口

    private Socket sock = null;                     //连接
    private byte[] buffer = new byte[4096];         //缓存

    /**
     * 与vendord建立连接
     */
    public boolean open() {
        if (sock != null) {
            Logger.e("只允许与硬件服务器建立单次连接。请先关闭原始连接。");
            return false;
        }
        try {
            sock = new Socket(HOST_NAME, HOST_PORT);
            sock.setTcpNoDelay(true);
            sock.setSendBufferSize(4096);
            sock.setKeepAlive(true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 与vendord关闭连接
     */
    public void close() {
        if (sock != null) {
            try {
                sock.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            sock = null;
        }
    }

    /**
     * 判断buffer中数据是否是一个json对象
     *
     * @return
     */
    private boolean isJSON(int len) {
        String str = new String(buffer, 0, len);
        try {
            JSONObject js = new JSONObject(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 发送命令，并等待得到响应结果。当获取到完整的一个数据包后，它就会立即返回
     *
     * @param type         命令类型
     * @param params       命令参数
     * @param milliTimeout 超时时间，单位为毫秒。如果为0，则表示永不超时(实际上是1小时)。
     * @return 响应结果对象。如果发生错误，则返回null。
     */
    public CmdResonse sendCmd(String type, JSONObject params, long milliTimeout) {
        if (type == null || params == null || sock == null) {
            return null;
        }
        try {
            DataInputStream is = new DataInputStream(sock.getInputStream());
            DataOutputStream os = new DataOutputStream(sock.getOutputStream());

            //发命令
            JSONObject js = new JSONObject();
            js.put("type", type);
            js.put("params", params);
            os.write(js.toString().getBytes());

            //读结果
            int offset = 0, len = 0;
            long start_time = System.currentTimeMillis();
            long end_time = milliTimeout == 0 ? start_time + 3600000 : start_time + milliTimeout;
            do {
                int rbs = is.read(buffer, offset, buffer.length - len);
                if (rbs >= 0) {
                    offset += rbs;
                    len += rbs;

                    if (len >= buffer.length || isJSON(len)) {//包完整了就返回
                        break;
                    }//else 继续读
                }//else 继续读

                //避免占用过高的CPU
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ;
            } while (System.currentTimeMillis() < end_time);

            return CmdResonse.parseInstance(new String(buffer, 0, len));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 建立连接，而后发送命令，然后接收结果，关闭连接，并返回结果
     *
     * @param type         命令类型
     * @param params       命令参数
     * @param milliTimeout 超时，单位为毫秒。如果为0，则表示永不超时(实际上是1小时)。
     * @return 响应结果对象。如果发生错误，则返回null。
     */
    public static CmdResonse sendOneCmd(String type, JSONObject params, long milliTimeout) {
        CmdThread ct = new CmdThread(type, params, milliTimeout);
        ct.start();
        try {
            ct.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.i(ct.getCmdResonse().getErr_msgs());
        return ct.getCmdResonse();
    }

    //实现与硬件服务程序通过网络进行通信
    static class CmdThread extends Thread {
        private String type;
        private JSONObject params;
        private long milliTimeout;
        private CmdResonse cr = null;      //响应结果对象

        public CmdThread(String type, JSONObject params, long milliTimeout) {
            this.type = type;
            this.params = params;
            this.milliTimeout = milliTimeout;
        }

        public void run() {
            SysAccessor sa = new SysAccessor();
            if (sa.open()) {
                cr = sa.sendCmd(type, params, milliTimeout);
                sa.close();
            }
        }

        public CmdResonse getCmdResonse() {
            return cr;
        }
    }

}
