package com.itep.mt.common.util;

/**
 * 字符串处理
 */

public class StringUtil {

    /**
     * byte数组转为HEX字符串
     *
     * @param data 数组
     * @return HEX格式的字符串
     */
    public static String bytesToHex(byte[] data) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexs = new char[data.length * 2];

        int v;
        for (int j = 0; j < data.length; j++) {
            v = data[j] & 0xFF;
            hexs[j * 2] = hexArray[v >>> 4];
            hexs[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexs);
    }

    /**
     * HEX字符串转化byte数组
     *
     * @param hex 字符串
     * @return byte数组
     */
    public static byte[] hexToBytes(String hex) {
        int size = hex.length() / 2;
        String hexString = hex.toUpperCase();
        byte[] data = new byte[size];
        for (int i = 0; i < size; i++) {
            int high = hexString.charAt(i * 2) & 0xFF;
            int low = hexString.charAt(i * 2 + 1) & 0xFF;
            if (high >= '0' && high <= '9') {
                high -= '0';
            } else {//A-F
                high -= 'A';
                high += 10;
            }
            if (low >= '0' && low <= '9') {
                low -= '0';
            } else {//A-F
                low -= 'A';
                low += 10;
            }
            data[i] = (byte) ((high << 4) | low);
        }
        return data;
    }

    /**
     * 将0x30拆分的字符串转为byte数组，例如0x3A 0x31转为 0xA1
     *
     * @param asc    0x30拆分的字符数组
     * @param offset 偏移量
     * @param len    需要转化的数据长度
     * @return byte格式数组
     */
    public static byte[] ascToBytes(byte[] asc, int offset, int len) {
        if (asc == null || offset < 0 || len < 0 || offset + len > asc.length) {
            Logger.e("Invalid parameters of bytesToAsc");
            return null;
        }
        int n = len / 2;
        byte[] data = new byte[n];

        for (int i = 0; i < n; i++) {
            data[i] = (byte) ((((asc[offset + i * 2] - (byte) 0x30) & ((byte) 0x0f)) << 4) | ((asc[offset + i * 2 + 1] - (byte) 0x30) & ((byte) 0x0f)));
        }
        return data;
    }

    /**
     * 将0x30拆分的字符串转为byte数组，例如0x3A 0x31转为 0xA1
     *
     * @param asc 0x30拆分的字符数组
     * @return byte格式数组
     */
    public static byte[] ascToBytes(byte[] asc) {
        return ascToBytes(asc, 0, asc.length);
    }


    /**
     * 将byte数组转为0x30拆分的字符串，例如0xA1转为0x3A 0x31
     *
     * @param data   byte格式数组
     * @param offset 偏移量
     * @param len    需要转化的数据长度
     * @return 0x30拆分的字符数组
     */
    public static byte[] bytesToAsc(byte[] data, int offset, int len) {
        if (data == null || offset < 0 || len < 0 || offset >= data.length || offset + len > data.length) {
            Logger.e("Invalid parameters of bytesToAsc");
            return null;
        }

        byte[] asc = new byte[len * 2];

        for (int i = 0; i < len; i++) {
            byte c = data[offset + i];
            asc[i * 2] = (byte) (((c & 0xF0) >> 4) + 0x30);
            asc[i * 2 + 1] = (byte) ((c & 0x0F) + 0x30);
        }
        return asc;
    }

    /**
     * 将byte数组转为0x30拆分的字符串，例如0xA1转为0x3A 0x31
     *
     * @param data byte格式数组
     * @return 0x30拆分的字符数组
     */
    public static byte[] bytesToAsc(byte[] data) {
        return bytesToAsc(data, 0, data.length);
    }

    /**
     * 用于byte数组转成int类型
     *
     * @param data byte型数组
     * @return 整型数据
     */
    public static int bytesToInt(byte[] data) {
        int value = 0;
        for (int i = 0; i < data.length; i++) {
            int shift = (data.length - 1 - i) * 8;
            value += (data[i] & 0xFF) << shift;
        }
        return value;
    }

    /**
     * 用于byte数组转成int类型
     *
     * @param data byte型数组
     * @return 整型数据
     */
    public static int bytesToInt(byte data) {
        int value = 0;
        for (int i = 0; i < 1; i++)
            value = (int) ((data & 0xff) << i * 8);
        return value;
    }

    /**
     * 用于byte数组转成int类型
     *
     * @param data byte型数组 数据低位是高位
     * @return 整型数据
     */
    public static int bytesToIntBig(byte[] data) {
        int value = 0;
        for (int i = data.length - 1; i >= 0; i--) {
            int shift = (i) * 8;
            value += (data[i] & 0xFF) << shift;
        }
        return value;
    }

    /**
     * 将ascii码存放的的数据转换成bcd码
     *
     * @param asc ascii码数组
     * @return bcd码
     */
    public static byte[] ascToBcd(byte[] asc) {

        for (int i = 0; i < asc.length; i++) {
            if (asc[i] > 0x39)
                asc[i] = (byte) (asc[i] - 0x41 + 0x0A);
        }
        byte[] data = new byte[asc.length / 2];
        for (int i = 0; i < asc.length / 2; i++) {
            data[i] = (byte) (((asc[2 * i] - 0x30) << 4) + ((asc[2 * i + 1] - 0x30) & 0xf));
        }
        return data;
    }

    /**
     * 判断是否纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否为空
     * @param cs
     * @return
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if(cs != null && (strLen = cs.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if(!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static int hexToInt(int a){
        String s = Integer.toHexString(a);
        return Integer.valueOf(s);
    }

    /**
     * int转byte数组
     * @param n int值
     * @return byte[4]
     */
    public static byte[] intToBytes2(int n){
        byte[] b = new byte[4];

        for(int i = 0;i < 4;i++)
        {
            b[i]=(byte)(n>>(24-i*8));

        }
        return b;
    }


    /**
     * 去除左右空格并空
     * @param str
     * @return
     */
    public static String trimToEmpty(String str){
        if(str==null){
            return "";
        }else{
            return str.trim();
        }
    }

}
