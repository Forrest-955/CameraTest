package com.itep.mt.common.util;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by Administrator on 2017/3/9.
 */

public class ZipUtil {

    public static int upZipFile(File zipFile, String folderPath) throws UnsupportedEncodingException {
        //public static void upZipFile() throws Exception{
        ZipFile zfile;
        try {
            zfile = new ZipFile(zipFile);
            Enumeration zList = zfile.entries();
            ZipEntry ze = null;
            byte[] buf = new byte[1024];
            while (zList.hasMoreElements()) {
                ze = (ZipEntry) zList.nextElement();
                if (ze.isDirectory()) {
                    Logger.i("ze.getName() = " + ze.getName());
                    String dirstr = folderPath + ze.getName();
                    //dirstr.trim();
                    dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
                    Logger.i("dirstr = " + dirstr);
                    File f = new File(dirstr);
                    f.mkdir();
                    continue;
                }
                Logger.i("ze.getName() = " + ze.getName());

                OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
                InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
                int readLen = 0;
                while ((readLen = is.read(buf, 0, 1024)) != -1) {
                    os.write(buf, 0, readLen);
                }
                is.close();
                os.close();
            }
            zfile.close();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    /**
     * 文件解压
     * @param zipFile       压缩文件
     * @param folderPath    解压路径
     * @param maxSize       允许单个文件大小的最大值，超出报错。
     * @return
     * @throws UnsupportedEncodingException
     */
    public static int upZipFile(File zipFile, String folderPath, long maxSize) throws UnsupportedEncodingException {
        //public static void upZipFile() throws Exception{
        ZipFile zfile;
        try {
            zfile = new ZipFile(zipFile);
            Enumeration zList = zfile.entries();
            ZipEntry ze = null;
            byte[] buf = new byte[1024];
            while (zList.hasMoreElements()) {
                ze = (ZipEntry) zList.nextElement();
                if (ze.isDirectory()) {
                    Logger.i("ze.getName() = " + ze.getName());
                    String dirstr = folderPath + ze.getName();
                    //dirstr.trim();
                    dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
                    Logger.i("dirstr = " + dirstr);
                    File f = new File(dirstr);
                    f.mkdir();
                    continue;
                }
                Logger.i("ze.getName() = " + ze.getName());

                if (ze.getSize() > maxSize)
                    return -1;

                OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
                InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
                int readLen = 0;
                while ((readLen = is.read(buf, 0, 1024)) != -1) {
                    os.write(buf, 0, readLen);
                }
                is.close();
                os.close();
            }
            zfile.close();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     *
     * @param baseDir     指定根目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    public static File getRealFileName(String baseDir, String absFileName) {

        String[] dirs = absFileName.split("/");
        String lastDir = baseDir;
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                lastDir += (dirs[i] + "/");
                File dir = new File(lastDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                    Logger.i("create dir = " + (lastDir + "/" + dirs[i]));
                }
            }
            File ret = new File(lastDir, dirs[dirs.length - 1]);
            Logger.i("2ret = " + ret);
            return ret;
        } else {
            return new File(baseDir, absFileName);
        }
    }

    /**
     * 解压zip到指定的路径
     *
     * @param zipFileString ZIP的名称
     * @param outPathString 要解压缩路径
     * @throws Exception
     */
    public static void UnZipFolder(String zipFileString, String outPathString) throws Exception {
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                //获取部件的文件夹名
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {
                Logger.i(outPathString + File.separator + szName);
                File file = new File(outPathString + File.separator + szName);
                if (!file.exists()) {
                    Logger.i("Create the file:" + outPathString + File.separator + szName);
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                // 获取文件的输出流
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // 读取（字节）字节到缓冲区
                while ((len = inZip.read(buffer)) != -1) {
                    // 从缓冲区（0）位置写入（字节）字节
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
    }
}
