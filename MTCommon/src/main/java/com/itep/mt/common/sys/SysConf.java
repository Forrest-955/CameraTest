package com.itep.mt.common.sys;

import com.itep.mt.common.constant.AppConstant;
import com.itep.mt.common.constant.version.GMConstant;
import com.itep.mt.common.constant.version.GXNXConstant;
import com.itep.mt.common.constant.version.GenericConstant;
import com.itep.mt.common.constant.version.JSYHConstant;
import com.itep.mt.common.constant.version.PSBCConstant;
import com.itep.mt.common.util.ConfFile;
import com.itep.mt.common.util.Logger;
import com.itep.mt.common.util.StringUtil;

import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;

/**
 * 系统配置
 */
public class SysConf {

    private static long DEFULT_TIMEOUT = 2000;      //2秒超时
    public static int SYS_MODE_USER = 1;           //用户模式
    public static int SYS_MODE_PRODUCTION = 0;       //生产模式

    /**
     * 读取序列号
     *
     * @return 读序列号
     */
    public static String readSerialNumber() {
//         SysCommand.runCmd("chmod 666 /mnt/internal_sd/mt/system/deviceinfo.json");
         String sn=ConfFile.getConfString(AppConstant.PATH_SYSTEM_DEVICE_INFO, GenericConstant.GENERIC_SN, "");
         if(StringUtil.isBlank(sn)){
             sn=ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, GenericConstant.GENERIC_SN, "");
             if(!StringUtil.isBlank(sn))
                 writeSerialNumber(sn);
         }
        return sn;
    }

    /**
     * 写入序列号
     *
     * @param sn 序列号
     * @return 是否成功, true表示成功
     */
    public static boolean writeSerialNumber(String sn) {
        return ConfFile.putConf(AppConstant.PATH_SYSTEM_DEVICE_INFO, GenericConstant.GENERIC_SN, sn);
    }


    /**
     * 写自定义序列号
     * @param sn 序列号
     * @return
     */
    public static boolean writeCustomSerialNumber(String sn){
        return ConfFile.putConf(AppConstant.PATH_SYSTEM_DEVICE_INFO, GenericConstant.GENERIC_CUSTOM_SN, sn);
    }


    /**
     * 读取自定义序列号
     * @param
     * @return
     */
    public static String readCustomSerialNumber(){
//        SysCommand.runCmd("chmod 666 /mnt/internal_sd/mt/system/deviceinfo.json");
        String sn=ConfFile.getConfString(AppConstant.PATH_SYSTEM_DEVICE_INFO, GenericConstant.GENERIC_CUSTOM_SN, "");
        if(StringUtil.isBlank(sn)){
            sn=ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, GenericConstant.GENERIC_CUSTOM_SN, "");
            if(!StringUtil.isBlank(sn))
                writeCustomSerialNumber(sn);
        }
        return sn;
    }



    /**
     * 重置自毁芯片的初始值
     *
     * @return true表示成功，false表示失败
     */
    public static boolean initDestruct() {
        try {
            //构造参数
            JSONObject params = new JSONObject();
            CmdResonse cr = SysAccessor.sendOneCmd("destruct_init", params, DEFULT_TIMEOUT);
            return cr.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断RTC自毁芯片的值是否是安全值
     *
     * @return true表示安全的，false表示已被修改
     */
    public static boolean isDestructSecure() {
        try {
            //构造参数
            JSONObject params = new JSONObject();
            CmdResonse cr = SysAccessor.sendOneCmd("destruct_is_secure", params, DEFULT_TIMEOUT);
            if (cr.getResult()) {
                JSONObject jsdata = cr.getJsdata();
                return jsdata.optBoolean("secure");
            } else {
                Logger.e(cr.getErr_msgs());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 读取系统模式
     *
     * @return 系统模式，返回SYS_MODE_USER 或 SYS_MODE_PRODUCTION
     */
    public static int getSysMode() {

        String modeJson = "/mnt/internal_sd/mt/system/sysmode.json";
//        String modeJson = "/storage/emulated/0/mt/system/sysmode.json";
        JSONObject jsonObject = ConfFile.readJSonFile(modeJson);
        String mode = jsonObject.optString("mode");
        if (mode.equals("user")) {
            return SYS_MODE_USER;
        } else {
            return SYS_MODE_PRODUCTION;
        }
        //        try {
        //            //构造参数
        //            JSONObject params = new JSONObject();
        //            CmdResonse cr = SysAccessor.sendOneCmd("conf_get_sys_mode", params, DEFULT_TIMEOUT);
        //            if (cr.getResult()) {
        //                JSONObject jsdata = cr.getJsdata();
        //                Logger.e("getSysModeString:"+jsdata.optString("mode"));
        //                if (jsdata.optString("mode").compareTo("user") == 0) {
        //                    return SYS_MODE_USER;
        //                } else {
        //                    return SYS_MODE_PRODUCTION;
        //                }
        //            } else {
        //                Logger.e(cr.getErr_msgs());
        //            }
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
        //        return SYS_MODE_PRODUCTION;
    }

    /**
     * 设置系统模式
     *
     * @param mode 系统模式，可取值SYS_MODE_USER或SYS_MODE_PRODUCTION
     * @return 是否成功, true表示成功
     */
    public static boolean setSysMode(int mode) {

        return ConfFile.putConf("/mnt/internal_sd/mt/system/sysmode.json", "mode", mode == 1 ? "user" : "production");
//        return ConfFile.putConf("/storage/emulated/0/mt/system/sysmode.json", "mode", mode == 1 ? "user" : "production");
        //        try {
        //            //构造参数
        //            JSONObject params = new JSONObject();
        //            params.put("mode", mode == SYS_MODE_USER ? "user" : "production");
        //
        //            CmdResonse cr = SysAccessor.sendOneCmd("conf_set_sys_mode", params, DEFULT_TIMEOUT);
        //            return cr.getResult();
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
        //        return false;
    }

    /**
     * 获得应用程序版本号
     *
     * @return 应用程序版本号
     */
    public static String getAppVersionNo() {
        return ConfFile.getConfString(AppConstant.PATH_APP_VERSION, "version", "V1.0");
    }

    /**
     * 设置应用程序版本号
     *
     * @param version 版本号
     * @return 应用程序版本号
     */
    public static boolean setAppVersionNo(String version) {
        return ConfFile.putConf(AppConstant.PATH_APP_VERSION, "version", version);
    }

    /**
     * 获得应用程序版本编译号
     *
     * @return 是否成功
     */
    public static String getAppBuildNo() {
        return ConfFile.getConfString(AppConstant.PATH_APP_VERSION, "build", "2017032301");
    }

    /**
     * 设置应用程序版本编译号
     *
     * @param build 版本编译号
     * @return 是否成功
     */
    public static boolean setAppBuildNo(String build) {
        return ConfFile.putConf(AppConstant.PATH_APP_VERSION, "build", build);
    }

    /**
     * 获得应用程序版本名称
     *
     * @return 应用程序版本名称
     */
    public static String getAppVersionName() {
        return ConfFile.getConfString(AppConstant.PATH_APP_VERSION, "name", "通用版");
    }

    /**
     * 设置应用程序版本名称
     *
     * @param name 版本名称
     * @return 是否成功
     */
    public static boolean setAppVersionName(String name) {
        return ConfFile.putConf(AppConstant.PATH_APP_VERSION, "name", name);
    }

    /**
     * 获得外部存储的大小
     *
     * @return 字节数，当为0时表示发生了错误
     */
    public static long getStorageSize() {
        try {
            FileReader fr = new FileReader(new File("/sys/class/block/mmcblk1/size"));
            char[] buf = new char[16];
            int len = fr.read(buf);
            fr.close();
            if (len > 0) {
                return Long.parseLong(new String(buf, 0, len - 1)) * 512;//最后一个字节是回车，去除
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获得SD卡存储的大小
     *
     * @return 字节数，当为0时表示发生了错误
     */
    public static long getSDStorageSize() {
        try {
            File file=new File("/sys/class/block/mmcblk1/size");
            if(file.exists()){
                FileReader fr = new FileReader(new File("/sys/class/block/mmcblk1/size"));
                char[] buf = new char[16];
                int len = fr.read(buf);
                fr.close();
                if (len > 0) {
                    return Long.parseLong(new String(buf, 0, len - 1)) * 512;//最后一个字节是回车，去除
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获得图片播放间隔
     *
     * @return 间隔秒数
     */
    public static int getPicturePlaybackInterval() {
        return ConfFile.getConfInteger(AppConstant.PATH_MEDIA_PLAYER_CONF, AppConstant.PICTURE_PLAYBACK_INTERVAL, AppConstant.DEF_PICTURE_INTERVAL);
    }

    /**
     * 设置图片播放间隔
     *
     * @param second 间隔秒数
     * @return true表示成功，false表示失败
     */
    public static boolean setPicturePlaybackInterval(int second) {
        return ConfFile.putConf(AppConstant.PATH_MEDIA_PLAYER_CONF, AppConstant.PICTURE_PLAYBACK_INTERVAL, second);
    }

    /**
     * 获取多媒体音量
     *
     * @return 音量值
     */
    public static int getMediaVolume() {
        return ConfFile.getConfInteger(AppConstant.PATH_MEDIA_PLAYER_CONF, AppConstant.MEDIA_VOLUME, AppConstant.DEF_VOLUME_INTERVAL);
    }

    /**
     * 设置多媒体音量
     *
     * @param mVolume 音量值
     * @return true表示成功，false表示失败
     */
    public static boolean setMediaVolume(int mVolume) {
        return ConfFile.putConf(AppConstant.PATH_MEDIA_PLAYER_CONF, AppConstant.MEDIA_VOLUME, mVolume);
    }

    /**
     * 获取业务音量
     *
     * @return 音量值
     */
    public static int getWorkVolume() {
        return ConfFile.getConfInteger(AppConstant.PATH_MEDIA_PLAYER_CONF, AppConstant.WORK_VOLUME, AppConstant.DEF_VOLUME_INTERVAL);
    }

    /**
     * 设置业务音量
     *
     * @param mVolume 音量值
     * @return true表示成功，false表示失败
     */
    public static boolean setWorkVolume(int mVolume) {
        return ConfFile.putConf(AppConstant.PATH_MEDIA_PLAYER_CONF, AppConstant.WORK_VOLUME, mVolume);
    }

    /**
     * 获取按键音量
     *
     * @return 音量值
     */
    public static int getBtnVolume() {
        return ConfFile.getConfInteger(AppConstant.PATH_MEDIA_PLAYER_CONF, AppConstant.BTN_VOLUME, AppConstant.DEF_VOLUME_INTERVAL);
    }

    /**
     * 设置按键音量
     *
     * @param mVolume 音量值
     * @return true表示成功，false表示失败
     */
    public static boolean setBtnVolume(int mVolume) {
        return ConfFile.putConf(AppConstant.PATH_MEDIA_PLAYER_CONF, AppConstant.BTN_VOLUME, mVolume);
    }

    /**
     * 设置串口1波特率
     *
     * @param baud 波特率
     * @return rue表示成功，false表示失败
     */
    public static boolean setUartBaud(int baud) {
        JSONObject jsconf = ConfFile.readJSonFile(AppConstant.PATH_DISPATCH_CONF);
        try {
            JSONObject jsonObject = jsconf.getJSONObject("host_uart_s0");
            jsonObject.put("baud", baud);
            return ConfFile.putConf(AppConstant.PATH_DISPATCH_CONF, "host_uart_s0", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取串口1波特率
     *
     * @return rue表示成功，false表示失败
     */
    public static int getUartBaud() {
        JSONObject jsconf = ConfFile.readJSonFile(AppConstant.PATH_DISPATCH_CONF);
        try {
            JSONObject jsonObject = jsconf.getJSONObject("host_uart_s0");
            return jsonObject.optInt("baud", 9600);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 9600;
    }

    /**
     * 设置编码格式
     *
     * @param coded_format 随机数
     * @return true表示成功，false表示失败
     */
    public static boolean setCodedFormat(String coded_format) {
        return ConfFile.putConf(AppConstant.PATH_COMMON_CONF, "coded_format", coded_format);
    }

    /**
     * 获取编码格式
     *
     * @return true表示成功，false表示失败
     */
    public static String getCodedFormat() {
        return ConfFile.getConfString(AppConstant.PATH_COMMON_CONF, "coded_format", "GBK");
    }

    /**
     * 设置随机数
     *
     * @param random 随机数
     * @return true表示成功，false表示失败
     */
    public static boolean setRandom(String random) {
        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, "random", random);
    }

    /**
     * 获取随机数
     *
     * @return true表示成功，false表示失败
     */
    public static String getRandom() {
        return ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, "random", "");
    }


    //通用密码键盘存储#START##########################################################################
    public static final String INIT_MAIN_KEY = "383838383838383838383838383838383838383838383838";

    public static final String INIT_WORK_KEY = "000000000000000000000000000000000000000000000000";

    /**
     * 密钥初始化
     *
     * @return true表示成功，false表示失败
     */
    public static boolean initGeneric_KEY() {
        //清理配置 ,因之前版本序列号在密码配置文件中，先读取序列号而保存
        readSerialNumber();
        readCustomSerialNumber();
        ConfFile.clearConf(AppConstant.PATH_PINPAD_CONF);
        return true;
    }

    /**
     * 激活主密钥号
     *
     * @param no 激活的主密钥号
     * @return true表示成功，false表示失败
     * @no 主密钥号
     */
    public static boolean set_Main_Key_No(int no) {
        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, GenericConstant.GENERIC_MAIN_KEY_NO, no);
    }

    /**
     * 激活工作密钥号
     *
     * @param no 激活的工作密钥号
     * @return true表示成功，false表示失败
     * @no 主密钥号
     */
    public static boolean set_Work_Key_No(int no) {
        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, GenericConstant.GENERIC_WORK_KEY_NO, no);
    }

    /**
     * 获取主密钥号
     *
     * @return true表示成功，false表示失败
     * @no 主密钥号
     */
    public static int get_Main_Key_No() {
        return Integer.parseInt(ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, GenericConstant.GENERIC_MAIN_KEY_NO, "-1"));
    }

    /**
     * 获取工作密钥号
     *
     * @return true表示成功，false表示失败
     * @no 主密钥号
     */
    public static int get_Work_Key_No() {
        return Integer.parseInt(ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, GenericConstant.GENERIC_WORK_KEY_NO, "-1"));
    }

    /**
     * 设置主密钥
     *
     * @param newMainKey 新主密钥
     * @return true表示成功，false表示失败
     * @no 主密钥号
     */
    public static boolean setGeneric_Main_Key(int no, String newMainKey) {
        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, GenericConstant.GENERIC_MAIN_KEY + no, newMainKey);
    }

    /**
     * 获取主密钥/初始化密钥
     *
     * @return 主密钥
     * @no 主密钥号
     */
    public static String getGeneric_Main_Key(int no) {
      return getGeneric_Main_Key(no, INIT_MAIN_KEY);
    }


    /**
     * 设置密码长度
     *
     * @param len 密码长度
     * @return true表示成功，false表示失败
     */
    public static boolean setPwdLength(int len) {
        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, GenericConstant.INPUT_LENGTH, len);
    }


    /**
     * 设置加密模式
     *
     * @return true表示成功，false表示失败
     */
    public static boolean setEncryption(byte encryption) {
        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, GenericConstant.ENCRYPTION, encryption);
    }

    /**
     * 获取加密模式
     *
     * @return true表示成功，false表示失败
     */
    public static String getEncryption() {
        return ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, GenericConstant.ENCRYPTION, "50");
    }


    /**
     * 获取主密钥
     * @param no 主密钥号
     * @param defaultValue 默认值
     * @return
     */
    public static String getGeneric_Main_Key(int no,String defaultValue) {
        return ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, GenericConstant.GENERIC_MAIN_KEY + no, defaultValue);
    }



//    /**
//     * 设置工作密钥
//     *
//     * @param newWorkKey 新工作密钥
//     * @return true表示成功，false表示失败
//     * @no 工作密钥号
//     */
//    public static boolean setGeneric_Work_Key(int no, String newWorkKey) {
//        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, GenericConstant.GENERIC_WORK_KEY + no, newWorkKey);
//    }

    /**
     * 设置工作密钥
     *
     * @param newWorkKey 新工作密钥
     * @return true表示成功，false表示失败
     * @mKeyNo 主密钥号
     * @wkeyNo 工作密钥号（能存16*16组密钥）
     */
    public static boolean setGeneric_Work_Key(int mKeyNo, int wkeyNo, String newWorkKey) {
        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, GenericConstant.GENERIC_WORK_KEY + mKeyNo + "_" + wkeyNo, newWorkKey);
    }

    /**
     * 获取工作密钥
     *
     * @return 工作密钥
     * @no 工作密钥号
     */
    public static String getGeneric_Work_Key(int no) {
        return ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, GenericConstant.GENERIC_WORK_KEY + no, INIT_WORK_KEY);
    }

    /**
     * 获取工作密钥
     *
     * @return 工作密钥
     * @no 工作密钥号
     */
    public static String getGeneric_Work_Key(int mKeyNo, int wkeyNo,String defaultValue) {
        return ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, GenericConstant.GENERIC_WORK_KEY + mKeyNo + "_" + wkeyNo, defaultValue);
    }



    /**
     * 获取工作密钥
     *
     * @return 工作密钥
     * @mKeyNo 主密钥号
     * @wkeyNo 工作密钥号
     */
    public static String getGeneric_Work_Key(int mKeyNo, int wkeyNo) {
        return getGeneric_Work_Key(mKeyNo,wkeyNo,INIT_WORK_KEY);
    }

    //通用密码键盘存储#END###########################################################################

    /**
     * 获取主密钥
     *
     * @return 主密钥
     */
    public static String getGYYH_Main_Key() {
        return null;
        //return ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, GYYHConstant.GYYH_MAIN_KEY, "");
    }


    /**
     * 获取工作密钥
     *
     * @param no 工作密钥索引号
     * @return 工作密钥
     */
    public static String getGYYH_Work_Key(int no) {
        return null;
        //return ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, GYYHConstant.GYYH_WORK_KEY + no, "");
    }

    /**
     * 获取广西农信SM4工作密钥
     *
     * @return SM4工作密钥
     */
    public static String getGXNX_WORK_KEY_SM4() {
        return ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, GXNXConstant.GXNX_WORK_KEY_SM4, "");
    }

    /**
     * 设置广西农信SM4工作密钥
     *
     * @param workKey 密钥
     * @return true表示成功，false表示失败
     */
    public static boolean setGXNX_WORK_KEY_SM4(String workKey) {
        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, GXNXConstant.GXNX_WORK_KEY_SM4, workKey);
    }

//    /**
//     * 密钥初始化
//     *
//     * @return true表示成功，false表示失败
//     */
//    public static boolean initGYYH_KEY() {
//        for (int i = 0; i < 16; i++) {
//            ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, GYYHConstant.GYYH_WORK_KEY + i, "00000000000000000000000000000000");
//        }
//        return true;
//    }

    /**
     * 获取键盘进程标志，区别于键盘使用程序（0：密码键盘应用；1：其他应用）
     *
     * @return
     */
    public static int getKeyboardProcess() {
        return ConfFile.getConfInteger(AppConstant.PATH_PINPAD_CONF, AppConstant.KEYBOARD_PROCESS, 0);
    }

    /**
     * 设置键盘进程标志，区别于键盘使用程序（0：密码键盘应用；1：其他应用）
     *
     * @param process
     * @return
     */
    public static boolean setKeyboardProcess(int process) {
        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, AppConstant.KEYBOARD_PROCESS, process);
    }


    /**
     * 获取邮储SN码
     *
     * @return true表示成功，false表示失败
     */
    public static byte[] getPSBC_SN_ID() {
        return (ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, PSBCConstant.PSBC_SN_ID, "")).getBytes();
    }


    /**
     * 设置邮储加密算法
     *
     * @param encryption 加密类型
     * @return true表示成功，false表示失败
     */
    public static boolean setPSBC_ENCRYPTION(String encryption) {
        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, PSBCConstant.PSBC_ENCRYPTION, encryption);
    }

    /**
     * 获取邮储加密算法
     *
     * @return true表示成功，false表示失败
     */
    public static byte[] getPSBC_ENCRYPTION() {
        byte[] encryption = StringUtil.hexToBytes(ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, PSBCConstant.PSBC_ENCRYPTION, ""));
        if (encryption.length < 1) {
            //如果未设置参数 默认加密算法为SM4
            encryption = new byte[]{PSBCConstant.PSBC_ENCRYPTION_SM4};
        }
        return encryption;
    }


    /**
     * 设置邮储Pin后补规则
     *
     * @param amends 后补规则
     * @return true表示成功，false表示失败
     */
    public static boolean setPSBC_AMENDS(String amends) {
        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, PSBCConstant.PSBC_AMENDS, amends);
    }

    /**
     * 获取邮储Pin后补规则
     *
     * @return true表示成功，false表示失败
     */
    public static byte[] getPSBC_AMENDS() {
        return StringUtil.hexToBytes(ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, PSBCConstant.PSBC_AMENDS, ""));
    }


    /**
     * 设置邮储密码长度
     *
     * @param len 密码长度
     * @return true表示成功，false表示失败
     */
    public static boolean setPSBC_KEY_LEN(String len) {
        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, PSBCConstant.PSBC_KEY_LEN, len);
    }

    /**
     * 获取邮储密码长度
     *
     * @return true表示成功，false表示失败
     */
    public static byte[] getPSBC_KEY_LEN() {
        return StringUtil.hexToBytes(ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, PSBCConstant.PSBC_KEY_LEN, ""));
    }


    /**
     * 设置邮储密码输入上传规则
     *
     * @param inputrule 密码输入上传规则
     * @return true表示成功，false表示失败
     */
    public static boolean setPSBC_INPUT_RULE(String inputrule) {
        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, PSBCConstant.PSBC_INPUT_RULE, inputrule);
    }

    /**
     * 获取邮储密码输入上传规则
     *
     * @return true表示成功，false表示失败
     */
    public static byte[] getPSBC_INPUT_RULE() {
        return StringUtil.hexToBytes(ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, PSBCConstant.PSBC_INPUT_RULE, ""));
    }


    /**
     * 设置邮储卡号
     *
     * @param cardno 密码输入上传规则
     * @return true表示成功，false表示失败
     */
    public static boolean setPSBC_CARD_NO(String cardno) {
        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, PSBCConstant.PSBC_CARD_NO, cardno);
    }




    /**
     * 设置邮储密码键盘运算模式
     *
     * @param pinrule 密码键盘运算模式
     * @return true表示成功，false表示失败
     */
    public static boolean setPSBC_PIN_RULE(String pinrule) {
        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, PSBCConstant.PSBC_PIN_RULE, pinrule);
    }





    /**
     * 获取邮储轨迹加密密钥离散因子
     *
     * @return true表示成功，false表示失败
     */
    public static byte[] getPSBC_SIGN_DISCTETE() {
        return StringUtil.hexToBytes(ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, PSBCConstant.PSBC_SIGN_DISCTETE, ""));
    }

    /**
     * 设置邮储轨迹加密算法
     *
     * @param encryption 加密类型
     * @return true表示成功，false表示失败
     */
    public static boolean setPSBC_SIGN_ENCRYPTION(String encryption) {
        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, PSBCConstant.PSBC_SIGN_ENCRYPTION, encryption);
    }

    /**
     * 获取邮储轨迹加密算法
     *
     * @return true表示成功，false表示失败
     */
    public static byte[] getPSBC_SIGN_ENCRYPTION() {
        return StringUtil.hexToBytes(ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, PSBCConstant.PSBC_SIGN_ENCRYPTION, ""));
    }

    /**
     * 设置邮储轨迹加密SM4工作密钥
     *
     * @param workKey 密钥
     * @return true表示成功，false表示失败
     */
    public static boolean setPSBC_SIGN_WORK_KEY_SM4(String workKey) {
        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, PSBCConstant.PSBC_SIGN_WORK_KEY_SM4, workKey);
    }

    /**
     * 获取邮储轨迹加密SM4工作密钥
     *
     * @return SM4工作密钥
     */
    public static byte[] getPSBC_SIGN_WORK_KEY_SM4() {
        return StringUtil.hexToBytes(ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, PSBCConstant.PSBC_SIGN_WORK_KEY_SM4, INIT_WORK_KEY));
    }
    //邮储密码键盘存储#END###########################################################################

//    /**
//     * 设置密码长度
//     *
//     * @param len
//     * @return
//     */
//    public static boolean setQHYH_PSW_LEN(int len) {
//        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, QHYHConstant.QHYH_SET_PSW_LEN, len);
//    }


//    /**
//     * @return 返回密码长度
//     */
//    public static int getQHYH_PSW_LEN() {
//        return ConfFile.getConfInteger(AppConstant.PATH_PINPAD_CONF, QHYHConstant.QHYH_SET_PSW_LEN, 6);
//    }


//
//    /**
//     * @return
//     */
//    public static int getQHYH_IS_TIME_SHOW() {
//        return ConfFile.getConfInteger(AppConstant.PATH_COMMON_CONF, QHYHConstant.QHYH_IS_TIME_SHOW, 1);
//    }


    /**
     * 建设银行获取语言
     *
     * @return
     */
    public static int get_JSYH_SET_LAN() {
        return ConfFile.getConfInteger(AppConstant.PATH_COMMON_CONF, JSYHConstant.JSYH_SET_LANGUAGE, 0);
    }

    /**
     * 设置主题目录
     *
     * @param subjName 文件夹名称
     * @return
     */
    public static boolean set_JSYH_SET_DIS_SUBJ(String subjName) {
        return ConfFile.putConf(AppConstant.PATH_COMMON_CONF, JSYHConstant.JSYH_MEDIAPLAYER_APPOINT, subjName);
    }

    /**
     * 设置主题目录
     *
     * @return
     */
    public static String get_JSYH_SET_DIS_SUBJ() {
        return ConfFile.getConfString(AppConstant.PATH_COMMON_CONF, JSYHConstant.JSYH_MEDIAPLAYER_APPOINT, "default");
    }


    /**
     * 建设银行  设置字体路径
     *
     * @param fontPath 字体路径
     * @return
     */
    public static boolean set_JSYH_SET_FONT_PATH(String fontPath) {
        return ConfFile.putConf(AppConstant.PATH_COMMON_CONF, JSYHConstant.JSYH_SET_FONT, fontPath);
    }

    public static String get_JSYH_SET_FONT_PATH() {
//        return ConfFile.getConfString(AppConstant.PATH_COMMON_CONF, JSYHConstant.JSYH_SET_FONT, "/storage/emulated/0/mt/fonts/FZSong-RKXX.ttf");
        return ConfFile.getConfString(AppConstant.PATH_COMMON_CONF, JSYHConstant.JSYH_SET_FONT, "/mnt/internal_sd/mt/fonts/FZSong-RKXX.ttf");
    }


    /**
     * 建设银行  设置语音
     *
     * @param voice 语音类型
     * @return
     */
    public static boolean set_JSYH_SET_VOICE(int voice) {
        return ConfFile.putConf(AppConstant.PATH_COMMON_CONF, JSYHConstant.JSYH_SET_VOICE, voice);
    }

    /**
     * 建设银行 获取语音
     *
     * @return
     */
    public static int get_JSYH_SET_VOICE() {
        return ConfFile.getConfInteger(AppConstant.PATH_COMMON_CONF, JSYHConstant.JSYH_SET_VOICE, 0);
    }

    /**
     * 建设银行 设置银行名称
     */
    public static boolean set_JSYH_BANK_NAME(String name) {
        return ConfFile.putConf(AppConstant.PATH_COMMON_CONF, JSYHConstant.JSYH_BANK_NAME, name);
    }

    /**
     * 建设银行 获取银行名称
     */
    public static String get_JSYH_BANK_NAME() {
        return ConfFile.getConfString(AppConstant.PATH_COMMON_CONF, JSYHConstant.JSYH_BANK_NAME, "中国邮政储蓄银行");
    }

    /**
     * 设置建设银行主密钥
     *
     * @param newMainKey 新主密钥
     * @return true表示成功，false表示失败
     * @no 主密钥号
     */
    public static boolean setJSYH_Main_Key(int no, String newMainKey) {
        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, JSYHConstant.JSYH_MAIN_KEY + no, newMainKey);
    }

    /**
     * 获取建设银行主密钥
     *
     * @return 主密钥
     * @no 主密钥号
     */
    public static String getJSYH_Main_Key(int no) {
        return ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, JSYHConstant.JSYH_MAIN_KEY + no, "");
    }

    /**
     * 设置建设银行密钥号加密类型
     *
     * @param no         主密钥号
     * @param iAlgorithm 加密类型
     * @return true表示成功，false表示失败
     * @no 主密钥号
     */
    public static boolean setJSYH_Key_Type(int no, int iAlgorithm) {
        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, JSYHConstant.JSYH_KEY_TYPE + no, iAlgorithm);
    }

    /**
     * 获取建设银行密钥号加密类型
     *
     * @return 主密钥
     * @no 主密钥号
     */
    public static String getJSYH_Key_Type(int no) {
        return ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, JSYHConstant.JSYH_KEY_TYPE + no, "");
    }

    /**
     * 设置建设银行工作密钥明文
     *
     * @param newWorkKey 新工作密钥
     * @return true表示成功，false表示失败
     * @no 工作密钥号
     */
    public static boolean setJSYH_Work_Key(int no, String newWorkKey) {
        return ConfFile.putConf(AppConstant.PATH_PINPAD_CONF, JSYHConstant.JSYH_WORK_KEY + no, newWorkKey);
    }

    /**
     * 获取建设银行工作密钥明文
     *
     * @return 工作密钥
     * @no 工作密钥号
     */
    public static String getJSYH_Work_Key(int no) {
        return ConfFile.getConfString(AppConstant.PATH_PINPAD_CONF, JSYHConstant.JSYH_WORK_KEY + no, "");
    }

    /**
     * 获取gpio2中/sys/class/gpio/gpio2/value的值
     *
     * @return String 返回value值
     */
    public static String getGPIOValue() {
        try {
            //String valuePath = "/sys/class/gpio/gpio182/value";
            String valuePath = "/sys/bus/i2c/devices/1-0051/tamper";
            byte[] value = SysCommand.readFile(valuePath, 0, 1, true);

            return new String(value);

        } catch (Exception e) {
            Logger.e("getGPIOValue Exception");
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 设置开盖自毁值 0不开启，1开启
     *
     * @param value 设置的值
     * @return true表示成功，false表示失败
     */
    public static boolean setOpenDestoryMonitorValue(int value) {
        return ConfFile.putConf(AppConstant.PATH_COMMON_CONF, "OpenDestoryMonitorValue", value);
    }

    /**
     * 获取开盖自毁值 0不开启，1开启
     *
     * @return true表示成功，false表示失败
     */
    public static int getOpenDestoryMonitorValue() {
        return ConfFile.getConfInteger(AppConstant.PATH_COMMON_CONF, "OpenDestoryMonitorValue", 0);
    }

    /**
     * 读取键盘类型 12键，15键, 0键 -- 无实体键盘
     *
     * @return 返回12 或 15
     */
    public static int getKeyBoardType() {
        return ConfFile.getConfInteger(AppConstant.PATH_SYSTEM_FUNCTION, "keyboard", 12);
    }


    /**
     * 读取键盘类型
     * @param keyboardType 12键，15键
     */
    public static boolean setKeyBoardType(int keyboardType) {
         return ConfFile.putConf(AppConstant.PATH_SYSTEM_FUNCTION, "keyboard", keyboardType);
    }


    /**
     * 读取是否有键盘灯 0表示无 1表示有
     */
    public static int getBackLightExist() {
        return ConfFile.getConfInteger(AppConstant.PATH_SYSTEM_FUNCTION, "backlight", 0);
    }

    /**
     * 读取USB接口类型
     */
    public static int getUSBType() {
        return ConfFile.getConfInteger(AppConstant.PATH_SYSTEM_FUNCTION, "usb_type", 0);
    }

    /**
     * 读取是否有直连外设
     */
    public static boolean getExternalDeviceExist() {
        return ConfFile.getConfInteger(AppConstant.PATH_SYSTEM_FUNCTION, "finger", 0)==1
                || ConfFile.getConfInteger(AppConstant.PATH_SYSTEM_FUNCTION, "camera", 0)==1
                || ConfFile.getConfInteger(AppConstant.PATH_SYSTEM_FUNCTION, "IDCard", 0)==1;
    }

    /**
     * 读取是否有摄像头
     */
    public static int getCameraExist() {
        return ConfFile.getConfInteger(AppConstant.PATH_SYSTEM_FUNCTION, "camera", 0);
    }

    /**
     * 获取指纹仪是否存在状态 0表示无、1表示有（外置），2表示有（内置）
     * @return
     */
    public static int getFingerExist(){
        return ConfFile.getConfInteger(AppConstant.PATH_SYSTEM_FUNCTION, "finger", 0);
    }

    /**
     * 获取二代证功能是否存在
     * @return
     */
    public static int getIDCardExist() {
        return ConfFile.getConfInteger(AppConstant.PATH_SYSTEM_FUNCTION, "IDCard", 0);
    }

    /**
     *获取fun配置中PID值
     * @return
     */
    public static String getSysFunPid(){
        return ConfFile.getConfString(AppConstant.PATH_SYSTEM_FUNCTION, "pid", "");
    }

    /**
     * 读取外设检测时间
     */
    public static int getExternalDeviceTime() {
        return ConfFile.getConfInteger(AppConstant.PATH_SYSTEM_FUNCTION, "testTime", 90);
    }

    /**
     * 读取是否有WIFI模块 1表示有
     */
    public static int getWIFIExist() {
        return ConfFile.getConfInteger(AppConstant.PATH_SYSTEM_FUNCTION, "WIFI", 0);
    }

    /**
     * 读取是否有WIFI模块 1表示有
     */
    public static boolean setFunctionConfig(JSONObject json) {
        return ConfFile.putConf(AppConstant.PATH_SYSTEM_FUNCTION, json);
    }

    /**
     * 设置hid.in.length
     *
     * @param length 波特率
     * @return rue表示成功，false表示失败
     */
    public static boolean setHidLength(int length) {
        JSONObject jsconf = ConfFile.readJSonFile(AppConstant.PATH_DISPATCH_CONF);
        try {
            JSONObject jsonObject = jsconf.getJSONObject("host_hid");
            jsonObject.put("dataLen", length);
            return ConfFile.putConf(AppConstant.PATH_DISPATCH_CONF, "host_hid", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取hid.in.length
     *
     * @return rue表示成功，false表示失败
     */
    public static int getHidLength() {
        JSONObject jsconf = ConfFile.readJSonFile(AppConstant.PATH_DISPATCH_CONF);
        try {
            JSONObject jsonObject = jsconf.getJSONObject("host_hid");
            return jsonObject.optInt("dataLen", 512);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 512;
    }

    /**
     * 键盘灯 0关闭1开启
     *
     * @return
     */
    public static boolean keyboardLight(int status) {
        if (status == 0 || status == 1) {
            String cmd = "echo " + status + " >  /sys/class/leds/matrix-key/brightness";
            boolean ret = SysCommand.runCmd(cmd);
            return ret;
        } else {
            return false;
        }
    }

    public static int getUSBExist() {
        return ConfFile.getConfInteger(AppConstant.PATH_SYSTEM_FUNCTION, "usb", 0);
    }

    /**
     * USB类型 0--PC  1--Android
     *
     * @return
     */
    public static boolean setUSBType() {
        int type = ConfFile.getConfInteger(AppConstant.PATH_SYSTEM_FUNCTION, "usb_type", 0);
        String cmd = "echo " + type + " > /sys/class/gpio/gpio265/value";
        boolean ret = SysCommand.runCmd(cmd);
        return ret;
    }
    /**
     * 获取指纹仪ID
     * @return
     */
    public static String getFingerId(){
        return ConfFile.getConfString(AppConstant.PATH_INFOVIEWER_CONF,"finger_id","");
    }


    /**
     *  设置指纹仪ID
     * @return
     */
    public static boolean setFingerId(String fingerId){
        return ConfFile.putConf(AppConstant.PATH_INFOVIEWER_CONF,"finger_id",fingerId);
    }


    /**
     *  设置pdf屏幕宽
     * @return
     */
    public static boolean setPdfScreenWidth(int screenWidth){
        return ConfFile.putConf(AppConstant.PATH_INFOVIEWER_CONF,"pdf_screen_width",screenWidth);
    }


    /**
     *  设置pdf屏幕高
     * @return
     */
    public static boolean setPdfScreenHeight(int screenHeight){
        return ConfFile.putConf(AppConstant.PATH_INFOVIEWER_CONF,"pdf_screen_height",screenHeight);
    }



    /**
     *  获取pdf屏幕宽
     * @return
     */
    public static int getPdfScreenWidth(){
        return ConfFile.getConfInteger(AppConstant.PATH_INFOVIEWER_CONF,"pdf_screen_width",1280);
    }


    /**
     *  获取pdf屏幕高
     * @return
     */
    public static int getPdfScreenHeight(){
        return ConfFile.getConfInteger(AppConstant.PATH_INFOVIEWER_CONF,"pdf_screen_height",800);
    }


    /**
     * 设置PID
     * @param pid
     * @return
     */
    public static boolean setPid(String pid){
        boolean iret = SysCommand.runCmd("setprop persist.sys.usb.pid " + pid);
        SysCommand.sync();
        return iret;
    }

    /**
     * 获取PID
     * @return
     */
    public static String getPid(){
        String ret = SysCommand.runCmdForResult("getprop persist.sys.usb.pid");
        return ret;
    }

    /**
     * 设置VID
     * @param vid
     * @return
     */
    public static boolean setVid(String vid){
        boolean iret = SysCommand.runCmd("setprop persist.sys.usb.vid " + vid);
        SysCommand.sync();
        return iret;
    }


    /**
     * 获取VID
     * @return
     */
    public static String getVid(){
        String ret = SysCommand.runCmdForResult("getprop persist.sys.usb.vid");
        return ret;
    }


    /**
     * 设置下载速率
     * @param outLength
     * @return
     */
    public static boolean setHidOutLength(int outLength){
        boolean iret = SysCommand.runCmd("setprop persist.sys.usb.hid.out.length " + outLength);
        SysCommand.sync();
        return iret;
    }

    /**
     * 获取下载速率
     * @return
     */
    public static int getHidOutLength(){
        String ret = SysCommand.runCmdForResult("getprop persist.sys.usb.hid.out.length");
        return NumberUtils.toInt(ret);
    }

    /**
     * 设置上传速率
     * @param inLength
     * @return
     */
    public static boolean setHidInLength(int inLength){
        boolean iret = SysCommand.runCmd("setprop persist.sys.usb.hid.in.length " + inLength);
        SysCommand.sync();
        return iret;
    }

    /**
     * 获取上传速率
     * @return
     */
    public static int getHidInLength(){
        String ret = SysCommand.runCmdForResult("getprop persist.sys.usb.hid.in.length");
        return NumberUtils.toInt(ret);
    }


    /**
     * 设置HID带宽等级
     * @param interval
     * @return
     */
    public static boolean setHidInterval(int interval){
        boolean iret = SysCommand.runCmd("setprop persist.sys.usb.hid.interval " + interval);
        SysCommand.sync();
        return iret;
    }


    /**
     * 获取HID带宽等级
     * @return
     */
    public static int getHidInterval(){
        String ret = SysCommand.runCmdForResult("getprop persist.sys.usb.hid.interval");
        return NumberUtils.toInt(ret);
    }

    /**
     *存储保护密钥的加密密钥KEEK
     * @param key 有芯片产生的随机密钥
     * @return
     */
    public static boolean setProtectEncryptKey(String key){
        return ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GMConstant.GM_PROTECT_ENCRYPT_KEY, key);
    }
    /**
     *获取保护密钥的加密密钥KEEK
     * @return
     */
    public static String getProtectEncryptKey(){
        return ConfFile.getConfString(AppConstant.PATH_GMPINPAD_CONF, GMConstant.GM_PROTECT_ENCRYPT_KEY, "");
    }

    /**
     *存储保护密钥密文KEK
     * @param key 有芯片产生的随机密钥
     * @return
     */
    public static boolean setProtectKey(String key){
        return ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GMConstant.GM_PROTECT_KEY, key);
    }
    /**
     *获取保护密钥密文KEK
     * @return
     */
    public static String getProtectKey(){
        return ConfFile.getConfString(AppConstant.PATH_GMPINPAD_CONF, GMConstant.GM_PROTECT_KEY, "");
    }

    /**
     * 获取置主密钥
     * @return
     */
    public static String getGMMainKey(){
        String str= ConfFile.getConfString(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_MAINKEY, null);
        return  SMAction.useProtectKeyDecrypt(str);

    }

    /**
     * 设置主密钥
     * @param key 主密钥的值
     * @return
     */
    public static boolean setGMMainKey(String key){
        return ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_MAINKEY, SMAction.useProtectKeyEncrypt(key));
    }

    /**
     * 设置MAC密钥
     * @return
     */
    public static String getGMMacKey(){
        String str= ConfFile.getConfString(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_MACKEY, null);
        return  SMAction.useProtectKeyDecrypt(str);

    }

    /**
     * 设置MAC密钥
     * @param key 主密钥的值
     * @return
     */
    public static boolean setGMMacKey(String key){
        return ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_MACKEY, SMAction.useProtectKeyEncrypt(key));
    }

    /**
     * 内部认证密钥
     * @return
     */
    public static String getGMInternalKey(){
        String str= ConfFile.getConfString(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_INTERNAL_AUTH_KEY, null);
        return  SMAction.useProtectKeyDecrypt(str);

    }

    /**
     * 设置内部认证密钥
     * @param key 主密钥的值
     * @return
     */
    public static boolean setGMInternalKey(String key){
        return ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_INTERNAL_AUTH_KEY, SMAction.useProtectKeyEncrypt(key));
    }

    /**
     * 外部认证密钥
     * @return
     */
    public static String getGMExternalKey(){
        String str= ConfFile.getConfString(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_EXTERNAL_AUTH_KEY, null);
        return  SMAction.useProtectKeyDecrypt(str);

    }

    /**
     * 设置外部认证密钥
     * @param key 主密钥的值
     * @return
     */
    public static boolean setGMExternalKey(String key){
        return ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_EXTERNAL_AUTH_KEY, SMAction.useProtectKeyEncrypt(key));
    }

    /**
     * 内部认证密钥
     * @return
     */
    public static String getGMTramsKey(){
        String str= ConfFile.getConfString(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_TRAMSKEY, null);
        return  SMAction.useProtectKeyDecrypt(str);

    }

    /**
     * 设置内部认证密钥
     * @param key 主密钥的值
     * @return
     */
    public static boolean setGMTramsKey(String key){
        return ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_TRAMSKEY, SMAction.useProtectKeyEncrypt(key));
    }

    public static String getAppHash(String pkg){
        Logger.e("getAppHash:"+pkg);
        JSONObject jsconf = ConfFile.readJSonFile(AppConstant.PATH_COMMON_CONF);
        try {
            JSONObject jsonObject = jsconf.getJSONObject("hash");
            String hash=jsonObject.optString(pkg,"");
            return hash;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取是否显示倒计时
     * @return
     */
    public static int getTitle_time(){
        return ConfFile.getConfInteger(AppConstant.PATH_SYSTEM_FUNCTION,"title_time",0);
    }

    /**
     * 获取是否显示倒计时
     * @param flag  0-显示，1-不显示
     * @return
     */
    public static boolean setTitle_time(int flag){
        return ConfFile.putConf(AppConstant.PATH_SYSTEM_FUNCTION, "title_time", flag);
    }

    /**
     * 是否使用国密算法
     * @return
     */
    public static int getIsUseGM(){
        return ConfFile.getConfInteger(AppConstant.PATH_SYSTEM_FUNCTION,"is_ues_GM",0);
    }

    /**
     * 获取是否使用国密算法
     * 曲靖商业银行专用
     * @param flag 1。国密  其他3DES
     * @return
     */
    public static boolean setIsUseGM(int flag){
        return ConfFile.putConf(AppConstant.PATH_SYSTEM_FUNCTION, "is_ues_GM", flag);
    }


}
