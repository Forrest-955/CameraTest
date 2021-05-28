package com.itep.mt.common.constant;

import com.itep.mt.common.sys.SysConf;

/**
 * 定义所有应用程序共用的常量
 */

public class AppConstant {
    public final static String CODED_FORMAT = SysConf.getCodedFormat();//编码格式

    public final static String PATH_CONF_ROOT = "/mnt/internal_sd/mt/";
//    public final static String PATH_CONF_ROOT = "/storage/emulated/0/mt/";

    //多媒体
    public final static String PATH_MEDIA_ROOT = PATH_CONF_ROOT + "media/";
    public final static String PATH_PICTURE_ROOT = PATH_MEDIA_ROOT + "picture/";   //图片存放路径
    public final static String PATH_VIDEO_ROOT = PATH_MEDIA_ROOT + "video/";       //视频存放路径
    public final static String PATH_PHOTO_ROOT = PATH_MEDIA_ROOT + "photo/";       //头像存放路径
    public final static String PATH_SOUND_ROOT = PATH_MEDIA_ROOT + "sound/";       //音频存放路径
    public final static String PATH_MEDIA_PLAYER_CONF = PATH_CONF_ROOT + "mediaplayer/mediaplayer.json";   //多媒体参数配置文件
    public final static int DEF_PICTURE_INTERVAL = 5000;                             //图片播放间隔

    //广告存储KEY
    public final static String PICTURE_PLAYBACK_INTERVAL = "picture_playback_interval";      //图片间隔存储值
    public final static String ADPLAYER_MODE_KEY = "adplayer_mode_key";                     //存储播放模式，0随机，1指定两种，
    public final static String ADPLAYER_ORDER_MODE_KEY = "adplayer_order_mode_key";        //存储指定播放模式，分为指定1全部图片，2单个图片，3全部视频，4单个视频，5混合五种
    public final static String ADPLAYER_ORDER_MIX_LIST_KEY = "adplayer_order_mix_list_key"; //存储混合播放列表

    public final static String PATH_TMP = PATH_CONF_ROOT + "tmp/";                                  //临时文件目录
    public final static String PATH_SYSTEM_ROOT = PATH_CONF_ROOT + "system/";                       //系统更新包存放路径
    public final static String PATH_APP_VERSION = PATH_CONF_ROOT + "system/version.json";           //应用程序版本文件
    public final static String PATH_UPDATE_CONTINUE_CONF = PATH_CONF_ROOT + "system/update.json";   //存储继续完成升级的配置文件

    //升级程序调用码
    public final static int REQUEST_CODE_UPDATE = 100;          //立即升级特定文件的请求码
    public final static int REQUEST_CODE_CONTINUE_UPDATE = 101; //尝试继续完成升级

    //密码键盘
    public final static String PATH_GMPINPAD_CONF = PATH_CONF_ROOT + "pinpad/gmpinpad.json";
    public final static String PATH_PINPAD_CONF = PATH_CONF_ROOT + "pinpad/pinpad.json";                //密码键盘参数配置文件
    public final static String KEYBOARD_PROCESS = "Keyboard_process";                //键盘进程标志存储
    //LOGO图片
    public final static String PATH_LOGO_ROOT = PATH_CONF_ROOT + "common/logo/";                     //logo文件夹
    public final static String PATH_LOGO = PATH_CONF_ROOT + "common/logo/logo.png";                     //logo图片
    public final static String PATH_QRCODE = PATH_PHOTO_ROOT + "QRCodeImage.jpg";                     //二维码图片
    //调度程序
    public final static String PATH_DISPATCH_CONF = PATH_CONF_ROOT + "dispatch/conf/conf.json";        //调度程序配置文件
    //信息交互
    public final static String PATH_INFOVIEWER_CONF = PATH_CONF_ROOT + "infoviewer/infoviewer.json"; //信息交互参数配置文件
    public final static String PATH_INFOVIEWER_HTML = PATH_CONF_ROOT + "infoviewer/html/";   // HTML 存放路径
    //通用
    public final static String PATH_COMMON_CONF = PATH_CONF_ROOT + "common/common.json";                //通用配置文件
    //签名图片
    public final static String PATH_SIGN_PHOTO = PATH_CONF_ROOT + "common/sign.png";                //签名图片路径
    //签名图片路径 SVG
    public final static String PATH_SIGN_PHOTO_SVG=PATH_CONF_ROOT + "common/sign.svg";  //签名SVG文件
    //签名坐标数据
    public final static String PATH_SIGN_TXT=PATH_CONF_ROOT+"common/sign.txt"; //签名坐标数据文本
    //指纹图片
    public final static String PATH_FINGER_PIC = PATH_CONF_ROOT + "common/finger.jpg";   //指纹图片
    //指纹特征文件
    public final static String PATH_FINGER_SIGN=PATH_CONF_ROOT+"common/fingersign.txt"; //指纹特征文件
    //zip文件保存路径
    public final static String PATH_ZIP = PATH_CONF_ROOT + "zip/";
    //设备配置文件路径
    public final static String PATH_SYSTEM_FUNCTION=PATH_CONF_ROOT+"system/function.json";
    //设备信息文件路径
    public final static String PATH_SYSTEM_DEVICE_INFO=PATH_CONF_ROOT+"system/deviceinfo.json";
    //设备键盘信息文件路径
    public final static String PATH_SYSTEM_KEYBOARD=PATH_CONF_ROOT+"system/keyboard.json";

    //建设银行轨迹数据
    public final static String PATH_SIGN_TRACE = PATH_CONF_ROOT + "common/trace.data";
    //定义文件类型
    public final static int PHOTO_TYPE = 1;                //头像
    public final static int PICTURE_TYPE = 2;              //广告图片
    public final static int VIDEO_TYPE = 3;                //视频
    public final static int SOUND_TYPE = 4;                //音频
    public final static int MTU_TYPE = 5;                   //mtu升级包
    public final static int PDF_TYPE = 6;                   //pdf文件
    public final static int LOGO_TYPE = 7;                  //logo图片
    public final static int ZIP_TYPE = 8;                  //zip文件
    public final static int HTML_TYPE = 9;                  //html文件
    public final static int PDFSHOW_TYPE = 10;                  //PDF显示文件

    public final static String PATH_PDF_ROOT = PATH_CONF_ROOT + "pdf/";       //pdf存放路径
    public final static String PATH_PDF_SHOW = PATH_PDF_ROOT + "show.pdf";       //pdf显示文件路径
    public final static String PATH_PDF_NULL = PATH_PDF_ROOT + "null.pdf";       //未传pdf时显示空pdf文件路径
    public final static String PATH_PDF_SIGN = PATH_PDF_ROOT + "sign.pdf";       //pdf签名文件路径
    public final static String PATH_PDF_PIC_SHOW = PATH_PDF_ROOT + "show.jpg";   //pdf显示图片文件路径
    public final static String PATH_PDF_SEAL_PIC = PATH_PDF_ROOT + "seal.jpg";   //电子签章图片文件路径
    public final static String PATH_PDF_SIGN_SAVE_PIC = PATH_PDF_ROOT + "signSave.jpg";   //签名合成后图片

    public final static String PATH_CALCULATE_DATA_BEFORE = PATH_PDF_ROOT + "calculateDataData_before";   //加解密的数据前
    public final static String PATH_CALCULATE_DATA_AFTER = PATH_PDF_ROOT + "calculateDataData_after";   //加解密的数据后

    //音量存储值
    public final static String MEDIA_VOLUME = "media_volume";//多媒体音量
    public final static String WORK_VOLUME = "work_volume";  //业务音量
    public final static String BTN_VOLUME = "btnvolume";     //按键音量
    public final static int DEF_VOLUME_INTERVAL = 15;        //默认音量值

    public final static String PATH_LOG = PATH_SYSTEM_ROOT + "log/";       //log日志文件夹路径

    //定义app状态常量
    public static final int APP_STATUS_STOP = 0;        //未显示可见的界面。
    public static final int APP_STATUS_RUN_FG = 1;        //运行在前台，正在展示界面
    public static final int APP_STATUS_PAUSE = 2;        //暂停状态

    //app名称
    public static final String APPID_MEDIAPLAYER    = "com.itep.mt.adplayer";                //广告播放
    public static final String APPID_DISPATCH       = "com.itep.mt.dispatch";                //分发
    public static final String APPID_EVALUATOR      = "com.itep.mt.evaluator";               //评价
    public static final String APPID_FACTORYTEST    = "com.itep.mt.factorytests";            //工厂测试
    public static final String APPID_INFOVIEWER     = "com.itep.mt.infoviewer";              //信息交互
    public static final String APPID_PINPAD         = "com.itep.mt.pinpad";                  //密码键盘
    public static final String APPID_SYSTEMSETTINGS = "com.itep.mt.systemsettings";          //系统设置
    public static final String APPID_SYSTEMUPDATE   = "com.itep.mt.systemupdate";            //系统升级

    public static final int SIGN_PAD_WIDTH = 500;
    public static final int SIGN_PAD_HEIGHT = 300;
    public static final int SCREEN_WIDTH_MAX = 1280;
    public static final int SCREEN_HEIGTH_MAX = 800;

    public static final int STATUS_STOP = 0;        //未显示可见的界面。
    public static final int STATUS_RUN_FG = 1;        //运行在前台，正在展示界面
    public static final int STATUS_PAUSE = 2;        //暂停状态
}
