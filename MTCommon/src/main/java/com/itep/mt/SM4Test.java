package com.itep.mt;

/**
 * Created by JimGw on 2018/8/27.
 */
public class SM4Test {
    //加载动态库
    static {
        System.loadLibrary("sm4");
    }

    public native static void encryptSM4(byte[] key, byte[] input, int iLen_key, int iLen_input, byte[] output);

    public native static void decryptSM4(byte[] key, byte[] input, int iLen_key, int iLen_input, byte[] output);

    public static native String stringFromJNI();

}
