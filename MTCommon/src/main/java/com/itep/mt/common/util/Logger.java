package com.itep.mt.common.util;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 打日志
 */
public class Logger {

    private static String tag = "ITEP_MT";
    private static final String ADB_FILE_PATH = "/mnt/internal_sd/mt/system/adb_debug";     //adb文件路径
//    private static final String ADB_FILE_PATH = "/storage/emulated/0/mt/system/adb_debug";     //adb文件路径
    private static final String LOG_FILE = "/mnt/internal_sd/mt/system/";     //adb文件路径
//    private static final String LOG_FILE = "/storage/emulated/0/mt/system/";     //adb文件路径
    private static boolean isOpenLogFlag = false;//是否开启LOG标志
    static {
        isOpenLogFlag = isOpenLog();
    }


    /**
     * 设置程序标签，每个应用在启动时设置
     *
     * @param t 标签
     */
    public static void setTag(String t) {
        tag = t;
    }

    /**
     * 输出错误日志
     *
     * @param msg 日志消息
     */
    public static void e(String msg) {
        if (isOpenLogFlag) {
            Log.e(tag, msg);
        }
        //write("ERROR", getInstance().getPrefixName(), msg);
    }

    /**
     * 输出错误日志
     * @param msg 日志信息
     * @param throwable 异常
     */
    public static void e(String msg,Throwable throwable){
        if (isOpenLogFlag) {
            Log.e(tag,msg,throwable);
        }
    }

    /**
     * 输出调试信息日志
     *
     * @param msg 日志消息
     */
    public static void i(String msg) {
        if (isOpenLogFlag) {
            Log.i(tag, msg);
        }
        //write("INFO", getInstance().getPrefixName(), msg);
    }

    /**
     * 判断是否写LOG
     *
     * @return 是否打印log
     */
    private static boolean isOpenLog() {
        File mFile = new File(ADB_FILE_PATH);
        if (mFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 写到文件中的log的前缀，如果因为混淆之类的原因而取不到，就返回"[ minify ]"
     *
     * @return prefix
     */
    private String getPrefixName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null || sts.length == 0) {
            return "[ minify ]";
        }
        try {
            for (StackTraceElement st : sts) {
                if (st.isNativeMethod()) {
                    continue;
                }
                if (st.getClassName().equals(Thread.class.getName())) {
                    continue;
                }
                if (st.getClassName().equals(this.getClass().getName())) {
                    continue;
                }
                if (st.getFileName() != null) {
                    return "[ " + Thread.currentThread().getName() +
                            ": " + st.getFileName() + ":" + st.getLineNumber() +
                            " " + st.getMethodName() + " ]";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "[ minify ]";
    }

    /**
     * 追加文件：使用FileWriter
     *
     * @param level   等级
     * @param prefix  前缀
     * @param content 内容
     */
    private static void write(String level, String prefix, String content) {
        if (!isOpenLogFlag)
            return;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String time = sdf.format(new Date());
            sdf=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String date=sdf.format(new Date());
            FileWriter writer = new FileWriter(LOG_FILE+date+".log", true);
            writer.write(time + ": " + level + "/" + prefix + ": " + content + "\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
