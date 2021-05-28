package com.itep.mt.common.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by JOY on 2018/4/8.
 */

public class MemoryUtil {

    /**
     * 获取TF存储可用空间
     */
    public static long getSDStorageAvailSize() {
        try {
            File sdcardDir = new File("/mnt/external_sd");
            StatFs sf = new StatFs(sdcardDir.getPath());
            long blockSize = sf.getBlockSizeLong();
            long blockCount = sf.getBlockCountLong();
            long availCount = sf.getAvailableBlocksLong();
            Logger.i("block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:" + blockSize * blockCount / 1024 + "KB");
            Logger.i("可用的block数目：:" + availCount + ",剩余空间:" + availCount * blockSize / 1024 + "KB");
            return availCount * blockSize;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取FLASH存储可用空间
     */
    public static long getStorageAvailSize() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            Logger.i("sdcardDir.getPath():" + sdcardDir.getPath());
            long blockSize = sf.getBlockSizeLong();
            long blockCount = sf.getBlockCountLong();
            long availCount = sf.getAvailableBlocksLong();
            Logger.i("block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:" + blockSize * blockCount / 1024 + "KB");
            Logger.i("可用的block数目：:" + availCount + ",剩余空间:" + availCount * blockSize / 1024 + "KB");
            return availCount * blockSize ;
        }
        return 0;
    }
}
