package com.itep.mt.common.util;

import com.itep.mt.common.constant.AppConstant;
import com.itep.mt.common.sys.SysCommand;
import com.itep.mt.common.sys.SysCtrl;

import java.io.File;
import java.util.ArrayList;

/**
 * 查找文件的类
 * Created by JimGw on 2017/3/27.
 */

public class SearchFileUtils {
    private static ArrayList<File> filelist = new ArrayList<File>();

    /**
     * 从TF卡检测是否存在升级文件
     *
     * @param keywords 关键词
     * @return 文件
     */
    public static File getFileFromTF(String keywords) {
        filelist.clear();
        if (!SysCtrl.remountSdcard()){//重新挂载SD卡
            return null;
        }
        //SysCommand.runCmd("chmod 777 " + SysCtrl.PATH_SDCARD_ROOT);
        getFilelists(SysCtrl.PATH_SDCARD_ROOT, keywords);

        //如果存在多个升级包，判断版本取最高版本进行升级
        if (filelist.size() >= 1) {
            File mFile = new File(filelist.get(0).getPath());
            Logger.i("mFileName:" + mFile.getName());
            for (int i = 0; i < filelist.size(); i++) {
                String fileName = filelist.get(i).getName();
                Logger.i("fileName:" + fileName);
                int result = fileName.compareTo(mFile.getName());
                if (result >= 0) {
                    mFile = filelist.get(i);
                }
                Logger.i("mFile:" + mFile.getName());
            }
            return mFile;
        }
        return null;
    }

    /**
     * 从内部存储中检测是否存在升级文件
     *
     * @param keywords 关键词
     * @return 文件
     */
    public static File getFileFromSys(String keywords) {
        filelist.clear();
        getFilelists(AppConstant.PATH_CONF_ROOT + "system/", keywords);
        if (filelist.size() >= 1)
            return filelist.get(0);
        return null;
    }

    /**
     * 查找关键词文件
     *
     * @param strPath
     * @param keywords
     */
    public static void getFilelists(String strPath, String keywords) {
        File dir = new File(strPath);
        try {
            File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    String fileName = files[i].getName();
                    if (files[i].isDirectory()) { // 判断是文件还是文件夹
                        getFilelists(files[i].getAbsolutePath(), keywords);
                    } else {
                        if (fileName.endsWith(keywords)) {
                            filelist.add(files[i]);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.e("sdcard err");
        }
    }

    /**
     * 删除所有关键词后缀文件
     *
     * @param strPath
     * @param keywords
     */
    public static void DeleteFileFromSys(String strPath, String keywords) {
        File dir = new File(strPath);
        try {
            File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    String fileName = files[i].getName();
                    if (files[i].isDirectory()) { // 判断是文件还是文件夹
                        getFilelists(files[i].getAbsolutePath(), keywords);
                    } else {
                        if (fileName.endsWith(keywords)) {
                            files[i].delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.e("sdcard err");
        }
    }
}
