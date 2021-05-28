package com.itep.mt.common.util;

/**
 * Created by Administrator on 2017/2/9.
 */

public class CommunicationUtil {

    //实现为单一实例
    private static CommunicationUtil _instance = null;

    //返回单一实例
    public static CommunicationUtil getInstance() {
        if (_instance == null) {
            _instance = new CommunicationUtil();
        }
        return _instance;
    }

    public native int uartopen(String uartfile, int baud);  //调用jni打开串口

    public native int uartread(int fd, byte[] buffer, int millTimeout);  //读串口数据

    public native int uartwrite(int fd, byte[] data); //向串口写入数据

    public native int uartclose(int fd);//关闭串口

    public native int hidopen(String file); //打开HID

    public native int hidread(int fd, byte[] buffer, int millTimeout); //读U口数据

    public native int hidwrite(int fd, byte[] data); //向U口写入数据

    public native void hidclose(int fd); //关闭U口

    static {
        System.loadLibrary("Communication");//加载jni的SO
    }
}