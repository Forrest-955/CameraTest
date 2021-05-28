package com.itep.mt.common.sys;

import com.itep.mt.common.constant.AppConstant;

/**
 * 系统控制命令类
 * Created by JimGw on 2017/3/27.
 */

public class SysCtrl {

    public static final String PATH_SDCARD_ROOT = AppConstant.PATH_CONF_ROOT + "sdcard_root/";
    public static final String PATH_EXTERNAL_SD_ROOT = "/mnt/external_sd";

    /**
     * 重新挂载sd卡
     *
     * @return
     */
    public static boolean remountSdcard() {
//        if (!SysCommand.runCmd("chmod 777 " + PATH_EXTERNAL_SD_ROOT)){
//            return false;
//        }
        if (!SysCommand.runCmd("umount " + PATH_EXTERNAL_SD_ROOT)){
            return false;
        }
        //挂载sd卡到指定的目录
        boolean result = SysCommand.runCmd("mount -t vfat -o rw,umask=000 /dev/block/mmcblk1 " + PATH_SDCARD_ROOT);
        if (result) {
            //return SysCommand.runCmd("chmod 777 -R " + PATH_SDCARD_ROOT);
            return true;
        }
        return false;
    }

    /**
     * 卸载sd卡
     */
    public static void umountSdcard() {
        SysCommand.runCmd("umount " + PATH_SDCARD_ROOT);
    }
}
