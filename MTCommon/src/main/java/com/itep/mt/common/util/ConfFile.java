package com.itep.mt.common.util;

import com.itep.mt.common.sys.SysCommand;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 封装配置文件的读写操作
 */
public class ConfFile {

    /**
     * 读取Json格式的配置文件
     *
     * @param path 文件全路径
     * @return 文件内容对应的json对象
     */
    public static JSONObject readJSonFile(String path) {
        JSONObject jo = null;//json对象
        try {
            //先判断文件有效性
            File file = new File(path);
            if (file.canRead()) {
                //读文件内容
                int size = (int) file.length();
                byte[] content = new byte[size];
                FileInputStream fi = new FileInputStream(file);
                try {
                    int len = fi.read(content, 0, size);
                    if (len != size) {
                        Logger.e("Incomplete file content!");
                    }
                    //生成json对象
                    jo = new JSONObject(new String(content));
                } finally {
                    fi.close();
                }
            } else {
                Logger.e("No file: " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jo;
    }

    /**
     * 更新json文件的配置项
     *
     * @param path  json文件全路径
     * @param key   键名
     * @param value 键值
     * @return 是否成功
     */
    public static boolean putConf(String path, String key, String value) {
        try {
            JSONObject js = null;
            File file = new File(path);
            if (file.exists()) {
                if (file.length() > 0) {
                    js = readJSonFile(path);
                } else {
                    SysCommand.runCmd("rm " + path);
                    js = new JSONObject();
                }
            } else {
                js = new JSONObject();
            }
            js.put(key, value);

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(js.toString().getBytes("UTF-8"));
            fos.close();
            SysCommand.sync();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新json文件的配置项
     *
     * @param path  json文件全路径
     * @param key   键名
     * @param value 键值
     * @return 是否成功
     */
    public static boolean putConf(String path, String key, int value) {
        try {
            JSONObject js = null;
            File file = new File(path);
            if (file.exists()) {
                if (file.length() > 0) {
                    js = readJSonFile(path);
                } else {
                    SysCommand.runCmd("rm " + path);
                    js = new JSONObject();
                }
            } else {
                js = new JSONObject();
            }
            js.put(key, value);

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(js.toString().getBytes("UTF-8"));
            fos.close();
            SysCommand.sync();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新json文件的配置项
     *
     * @param path       json文件全路径
     * @param key        键名
     * @param jsonObject 键值
     * @return 是否成功
     */
    public static boolean putConf(String path, String key, JSONObject jsonObject) {
        try {
            JSONObject js = null;
            File file = new File(path);
            if (file.exists()) {
                if (file.length() > 0) {
                    js = readJSonFile(path);
                } else {
                    SysCommand.runCmd("rm " + path);
                    js = new JSONObject();
                }
            } else {
                js = new JSONObject();
            }
            js.put(key, jsonObject);

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(js.toString().replaceAll("\\\\", "").getBytes("UTF-8"));//去除反斜杠
            fos.close();
            SysCommand.sync();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新json文件全文
     *
     * @param path       json文件全路径
     * @param jsonObject json文件内容
     * @return 是否成功
     */
    public static boolean putConf(String path, JSONObject jsonObject) {
        try {
            File file = new File(path);
            if (file.exists()) {
                    SysCommand.runCmd("rm " + path);
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(jsonObject.toString().replaceAll("\\\\", "").getBytes("UTF-8"));//去除反斜杠
            fos.close();
            SysCommand.sync();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 得到字符串类型的配置值
     *
     * @param path         配置文件路径
     * @param key          键名
     * @param defaultvalue 如果不存在的默认值
     * @return 键值
     */
    public static String getConfString(String path, String key, String defaultvalue) {
        JSONObject js = ConfFile.readJSonFile(path);
        if (js == null) {
            return defaultvalue;
        }
        return js.optString(key, defaultvalue);
    }

    /**
     * 得到整数类型的配置值
     *
     * @param path         配置文件路径
     * @param key          键名
     * @param defaultvalue 如果不存在的默认值
     * @return 键值
     */
    public static int getConfInteger(String path, String key, int defaultvalue) {
        JSONObject js = ConfFile.readJSonFile(path);
        if (js == null) {
            return defaultvalue;
        }
        return js.optInt(key, defaultvalue);
    }


    /**
     * 清理配置
     * @param path
     * @return
     */
    public static boolean clearConf(String path){
        try {
            JSONObject  js = new JSONObject();
            File file = new File(path);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(js.toString().replaceAll("\\\\", "").getBytes("UTF-8"));//去除反斜杠
            fos.close();
            SysCommand.runCmd("sync");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}

