package com.itep.mt.common.sys;

import com.itep.mt.common.util.FileUtils;
import com.itep.mt.common.util.Logger;
import com.itep.mt.common.util.StringUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * 系统操作
 */

public class SysCommand {

    private static long DEFULT_TIMEOUT = 300000;//300秒超时

    private static String RUN_RES_PATH="/mnt/internal_sd/mt/tmp/runRes.txt";
//    private static String RUN_RES_PATH = "/storage/emulated/0/internal_sd/mt/tmp/runRes.txt";

    public static boolean reboot(){return runCmd("reboot");}
    public static boolean sync(){return runCmd("sync");}

    /**
     * 使用root权限运行一个命令
     *
     * @param cmd 命令字符串
     * @return 是否成功运行
     */
    public static boolean runCmd(String cmd) {
        try {
            JSONObject params = new JSONObject();
            params.put("command", cmd);
            CmdResonse cr = SysAccessor.sendOneCmd("cmd_run", params, DEFULT_TIMEOUT);
            if (!cr.getResult()) {
                Logger.e(cr.getErr_msgs());
            }
            return cr.getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String runCmdForResult(String cmd)  {
        String retStr="";
        boolean ret=runCmd(cmd+" > "+RUN_RES_PATH);
        if(ret){
            boolean res = runCmd("chmod 666 " + RUN_RES_PATH);
            if (res){
                File file=new File(RUN_RES_PATH);
                if(file.exists()){
                    retStr=FileUtils.readFileContent(file);
                    file.delete();
                }
            }
        }
        return retStr;
    }

    /**
     * 从指定文件的指定偏移位置读取数据
     *
     * @param path        文件全路径
     * @param file_offset 偏移量大小
     * @param data_len    数据长度
     * @param from_begin  是否从头开始读，默认是true
     * @return 读到的数据，发生错误时，返回null
     */
    public static byte[] readFile(String path, int file_offset, int data_len, boolean from_begin) {

        try {
            //构造参数
            JSONObject params = new JSONObject();
            params.put("path", path);
            params.put("length", data_len);
            params.put("offset", file_offset);
            params.put("seek", from_begin ? "begin" : "end");


            CmdResonse cr = SysAccessor.sendOneCmd("file_read", params, DEFULT_TIMEOUT);
            if (cr.getResult()) {
                JSONObject jsdata = cr.getJsdata();
                int len = jsdata.optInt("length");
                String data = jsdata.optString("data");
                if (data.length() == len * 2) {
                    return StringUtil.hexToBytes(data);
                } else {
                    Logger.e("收到不完整的数据");
                }
            } else {
                Logger.e(cr.getErr_msgs());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[]{0};
    }

    /**
     * 向指定文件的指定偏移位置写入数据
     *
     * @param path        文件全路径
     * @param file_offset 偏移量大小
     * @param file_data   文件数据
     * @param from_begin  是否从头开始写，默认是true
     * @return 写成功的字节数
     */
    public static int writeFile(String path, int file_offset, byte[] file_data, boolean from_begin) {

        try {
            //构造参数
            JSONObject params = new JSONObject();
            params.put("path", path);
            params.put("length", file_data.length);
            params.put("offset", file_offset);
            params.put("seek", from_begin ? "begin" : "end");
            params.put("data", StringUtil.bytesToHex(file_data));

            CmdResonse cr = SysAccessor.sendOneCmd("file_write", params, DEFULT_TIMEOUT);
            if (cr.getResult()) {
                JSONObject jsdata = cr.getJsdata();
                return jsdata.optInt("length");
            } else {
                Logger.e(cr.getErr_msgs());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
