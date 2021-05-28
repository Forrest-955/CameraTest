package com.itep.mt.common.constant.version;

import com.itep.mt.common.constant.AppConstant;
import com.itep.mt.common.util.ConfFile;

/**
 * 四川农信常量
 * Created by mo on 2019/10/29.
 */

public class SCNXConstant {
    public static boolean mInitStatus = false;  // 是否初始化

    public static final Object mLock = new Object();
    public static final String SCNX_REPLY_PARAM = "scnx_reply_param";         //带参数返回
    public static final String SCNX_SET_MAIN_KEY = "setMainKeyOld";       //下载主密钥(旧系统)
    public static final String SCNX_SET_WORK_KEY = "setWorkKeyOld";       //下载工作密钥(旧系统)
    public static final String SCNX_SET_WORK_KEY_WY = "setWorkKeyWY";       //下载工作密钥网银
    public static final String SCNX_GET_INPUT_PWD = "scnx_get_input_pwd";     //获取密码
    public static final String SCNX_GET_INPUT_PWD_PINBLOK = "getInputPwdPinblock";     //获取密码
    public static final String SCNX_GET_INPUT_PWD_WY = "getInputPwdPinblockWY";     //获取密码网银
    public static final String SCNX_INFOVIEWER = "oldInfoviewer";            //老系统信息交互
    public static final String SCNX_INFOVIEWER_TEST = "oldInfoviewerText";            //老系统信息交互测试环境
    public static final String SCNX_OPEN_WORK_CARD = "oldOpenWorkCard";            //启动电子工牌(老系统)
    public static final String SCNX_REPLY = "cgb_reply";
    public static final String SCNX_PHOTO = "scnx_photo";  // 四川农信下载照片
    public static final String SCNX_PHOTO_PATH = AppConstant.PATH_PHOTO_ROOT + "teller.jpg";  // 四川农信下载照片
    private static final String SCNX_TER_NAME= "scnx_ter_name";  //柜员名字
    private static final String SCNX_TER_NO= "scnx_ter_no";  //柜员工员
    private static final String SCNX_TER_LEVLE= "scnx_ter_levle";  //柜员等级

    /**
     * 四川农信设置图片块数
     * @param no
     * @return
     */
    public static boolean set_packageNo(int no) {
        return ConfFile.putConf(AppConstant.PATH_COMMON_CONF, SCNXConstant.SCNX_PHOTO, no);
    }

    /**
     * 四川农信获取图片块数
     * @return
     */
    public static int get_packageNo() {
        return ConfFile.getConfInteger(AppConstant.PATH_COMMON_CONF, SCNXConstant.SCNX_PHOTO, 0);
    }

    public static boolean set_terName(String name) {
        return ConfFile.putConf(AppConstant.PATH_COMMON_CONF,SCNX_TER_NAME, name);
    }

    public static String get_terName() {
        return ConfFile.getConfString(AppConstant.PATH_COMMON_CONF, SCNX_TER_NAME, "");
    }

    public static boolean set_terNo(String No) {
        return ConfFile.putConf(AppConstant.PATH_COMMON_CONF, SCNX_TER_NO, No);
    }

    public static String get_terNo() {
        return ConfFile.getConfString(AppConstant.PATH_COMMON_CONF, SCNX_TER_NO, "");
    }

    public static boolean set_terLevle(int le) {
        return ConfFile.putConf(AppConstant.PATH_COMMON_CONF, SCNX_TER_LEVLE, le);
    }

    public static int get_terLevle() {
        return ConfFile.getConfInteger(AppConstant.PATH_COMMON_CONF, SCNX_TER_LEVLE, 0);
    }


}
