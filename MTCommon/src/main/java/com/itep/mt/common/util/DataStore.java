package com.itep.mt.common.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 应用程序的数据仓库，存放一些非固定的中间值
 */

public class DataStore {

    private Context sfContext;      //上下文
    private SharedPreferences sp;   //SharedPreferences对象

    //SharedPreferences所在的空间，每个应用程序可以设置私有的数据仓库
    private String store_name = "datastore";    //名称

    //构造函数
    public DataStore(String spName) {
        this.store_name = spName;
        sp = ContextUtil.getInstance().getSharedPreferences(spName, 0);
    }

    public DataStore() {
        this("datastore");
    }

    /**
     * 存储字符串类型的参数
     *
     * @param key   标识
     * @param value 值
     */
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        //editor.apply();
        editor.commit();
    }

    /**
     * 获得字符串类型的存储值
     *
     * @param key 标识
     * @return 存储的值，默认为空字符串
     */
    public String getString(String key) {
        return sp.getString(key, "");
    }

    //整数类型
    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        //editor.apply();
        editor.commit();
    }

    public int getInt(String key) {
        return sp.getInt(key, 0);
    }

    //获取默认值自定义情况
    public int getInt(String key, int value) {
        return sp.getInt(key, value);
    }

    //boolean类型
    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        //editor.apply();
        editor.commit();
    }

    public boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }

    //fload类型
    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, value);
        //editor.apply();
        editor.commit();
    }

    public float getFloat(String key) {
        return sp.getFloat(key, (float) 0);
    }

    //byte数组
    public void putByteArray(String key, byte[] value) {
        String hex = StringUtil.bytesToHex(value);
        putString(key, hex);
    }

    public byte[] getByteArray(String key) {
        String hex = getString(key);
        return StringUtil.hexToBytes(hex);
    }

}
