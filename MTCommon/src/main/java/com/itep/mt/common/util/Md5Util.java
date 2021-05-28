package com.itep.mt.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/3/3.
 */

public class Md5Util {

    public static String md5ForFile(File file) {
        int buffersize = 1024;
        FileInputStream fis = null;
        DigestInputStream dis = null;

        try {
            //创建MD5转换器和文件流
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            dis = new DigestInputStream(fis, messageDigest);

            byte[] buffer = new byte[buffersize];
            //DigestInputStream实际上在流处理文件时就在内部就进行了一定的处理
            while (dis.read(buffer) > 0)
                ;

            //通过DigestInputStream对象得到一个最终的MessageDigest对象。
            messageDigest = dis.getMessageDigest();

            // 通过messageDigest拿到结果，也是字节数组，包含16个元素
            byte[] array = messageDigest.digest();
            // 同样，把字节数组转换成字符串
            StringBuilder hex = new StringBuilder(array.length * 2);
            for (byte b : array) {
                if ((b & 0xFF) < 0x10) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对一个文件求他的md5值
     *
     * @param file 文件
     * @return MD5值
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String FileMD5(File file)
            throws NoSuchAlgorithmException, IOException {
        FileInputStream fis = null;
        String resultString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length = 0;
            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }
            resultString = StringUtil.bytesToHex(md.digest());
            return resultString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }
}
