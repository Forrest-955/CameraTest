package com.itep.mt.common.util;

import android.text.TextUtils;

import java.io.File;

/**
 * 扩展名格式判断
 * Created by cy on 2019/06/10.
 */

public class FileTypeUtil {

    public static final String PIC_EXT_BMP  =".bmp";
    public static final String PIC_EXT_JPG  =".jpg";
    public static final String PIC_EXT_JPEG =".jpeg";
    public static final String PIC_EXT_GIF  =".gif";
    public static final String PIC_EXT_PNG  =".png";
    private static String[] PICTURE_FOMAT_SUPPORT_LIST =
            {PIC_EXT_BMP, PIC_EXT_JPG, PIC_EXT_JPEG, PIC_EXT_GIF, PIC_EXT_PNG};//图片格式支持列表
    private static String[] VIDEO_FOMAT_SUPPORT_LIST =
            {".avi", ".mp4", ".mpg", ".mpeg", ".wmv", ".rm", ".rmvb", ".flv", ".vob"};  //视频图片格式支持列表

    /**
     * 根据文件名判断文件是否是图片文件
     * @param name 图片文件名称
     * @return true表示是图片
     */
    public static boolean isPictureFile(String name) {
        int n = name.lastIndexOf('.');
        if (n != -1 && n < name.length()) {
            String extName = name.substring(n).toLowerCase();
            for (int i = 0; i < PICTURE_FOMAT_SUPPORT_LIST.length; i++) {
                if (extName.compareTo(PICTURE_FOMAT_SUPPORT_LIST[i]) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 根据文件名判断文件是否是视频文件
     * @param name 视频文件名称
     * @return true表示是视频
     */
    public static boolean isVideoFile(String name) {
        int n = name.lastIndexOf('.');
        if (n != -1 && n < name.length()) {
            String extName = name.substring(n).toLowerCase();
            for (int i = 0; i < VIDEO_FOMAT_SUPPORT_LIST.length; i++) {
                if (extName.compareTo(VIDEO_FOMAT_SUPPORT_LIST[i]) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 根据文件名和后缀判断是否是某种文件
     * @param name 文件名
     * @param ext 扩展名
     * @return
     */
    public static boolean isFileType(String name,String ext){
        int n = name.lastIndexOf('.');
        if (n != -1 && n < name.length()) {
            String extName = name.substring(n).toLowerCase();
                if (extName.compareTo(ext) == 0) {
                    return true;
                }
        }
        return false;
    }

    /**
     * 获取后缀名(含.)
     *
     * @param path 全路径
     * @return
     */
    public static String getFileExtName(String path) {
        String ext = "";
        if ((path != null) && (path.length() > 0)) {
            int dot = path.lastIndexOf('.');
            if ((dot > -1) && (dot < (path.length() - 1))) {
                ext = path.substring(dot);
            }
        }
        return ext;
    }

    /**
     * 获取文件名
     *
     * @param path 全路径
     * @return
     */
    public static String getFileName(String path) {
        String fileName="";
        if (!TextUtils.isEmpty(path)) {
            fileName = path.substring(path.lastIndexOf(File.separator) + 1);
        }
        int dot = path.lastIndexOf('.');
        if ((dot > -1) && (dot < (path.length() - 1))) {
            fileName = path.substring(0,dot);
        }
        return fileName;
    }

    /**
     * 获取完整文件名
     *
     * @param path 全路径
     * @return
     */
    public static String getFullFileName(String path) {
        if (!TextUtils.isEmpty(path)) {
            return path.substring(path.lastIndexOf(File.separator) + 1);
        }
        return "";
    }
}
