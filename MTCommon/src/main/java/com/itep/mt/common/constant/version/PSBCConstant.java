package com.itep.mt.common.constant.version;

/**
 * 邮储常量
 */
public class PSBCConstant {

    /*
    //密码键盘
    public static final String PINPAD_INPUT = "pinpad_input";        //请输入密码，非加密

    //邮储信息交互
    public static final String INFOVIEWER_PSBC = "infoviewer_psbc";               //信息交互
    public static final String PSBC_INFOVIEWER_SIGN = "psbc_infoviewer_sign";    //信息交互签名
    public static final String PSBC_INFOVIEWER_SIGN_DEFINE = "psbc_infoviewer_sign_define";//信息交互签名邮储定义指令
    public static final String PSBC_PDF_SHOW = "psbc_pdf_show";                    //PDF显示

    public static final String GENERIC_INFOVIEWER = "generic_infoviewer";        //通用信息交互
    public static final String ZJYC_INFOVIEWER = "zjyc_infoviewer";        //浙江邮政信息交互
    public static final String ZJYC_LAST_INFO = "zjyc_last_info";        //浙江邮政信息交互上次内容存储标志

    //邮储评价界面
    public static final String PSBC_EVALUATE = "evaluate_psbc";                //服务评价
    public static final String PSBC_SIGN_IN = "sign_in_psbc";                  //柜员签到
    public static final String PSBC_SIGN_OUT = "sign_out_psbc";                //柜员签退

    //邮储通用指令
    public static final String CANCEL_APP_PSBC = "cancel_app_psbc";           //取消应用

    //邮储密码键盘部分指令集
    public static final String PSBC_GET_PUBLIC_KEY = "psbc_get_public_key";       //生成公私钥对
    public static final String PSBC_SET_WORK_KEY = "psbc_set_work_key";             //下载工作密钥
    public static final String PSBC_SET_DISCRETE = "psbc_set_discrete";            //生成离散因子
    public static final String PSBC_GET_DISCRETE = "psbc_get_discrete";            //获取离散因子
    public static final String PSBC_GET_INPUTPASSWORD = "psbc_get_inputpassword";//获取密码
    public static final String PSBC_GET_HASH = "psbc_get_hash";                    //计算杂凑值  sha_1  sm3
    public static final String PSBC_INPUT_RSA_PUBKEY = "psbc_input_rsa_pubkey";  //导入RSA 公钥
    public static final String PSBC_INPUT_RSA_PRIKEY = "psbc_input_rsa_prikey";  //导入RSA 私钥
    public static final String PSBC_INPUT_RSA_ENC = "psbc_input_rsa_enc";          //RSA 加密
    public static final String PSBC_SET_SIGN_DISCRETE = "psbc_set_sign_discrete"; //生成签名轨迹离散因子
    public static final String PSBC_GET_SIGN_DISCRETE = "psbc_get_sign_discrete"; //获取签名轨迹离散因子
    public static final String PSBC_SET_SIGN_KEY = "psbc_set_sign_key";            //下载轨迹加密密钥
    public static final String PSBC_SET_SIGN_ENCRYPTION = "psbc_set_sign_encryption";//设置轨迹加密算法
    public static final String PSBC_GET_SIGN_ENCRYPTION = "psbc_get_sign_encryption";//获取轨迹加密算法
    */
    public static final String PSBC_GET_AUTHORIZATION =  "psbc_get_authorization" ;//获取授权码
    public static final String PSBC_GET_PUBLIC_KEY =     "psbc_get_public_key"    ;//产生非对称算法密钥对
    public static final String PSBC_SET_DISCRETE =       "psbc_set_discrete"      ;//产生离散因子
    public static final String PSBC_SET_WORK_KEY =       "psbc_set_work_key"      ;//下载工作密钥
    public static final String PSBC_GET_INPUT_PASSWORD = "psbc_get_input_password";//获取密码
    public static final String PSBC_SET_ENCRYPTION =     "psbc_set_encryption"    ;//设置加密算法
    public static final String PSBC_SET_AMENDS =         "psbc_set_amends"        ;//设置后补字符
    public static final String PSBC_SET_PIN_LEN =        "psbc_set_pin_len"       ;//设置密码长度
    public static final String PSBC_SET_PIN_RULE =       "psbc_set_pin_rule"      ;//设置密码输入规则
    public static final String PSBC_SET_VOICE =          "psbc_set_voice"         ;//选择语音
    public static final String PSBC_GET_HASH =           "psbc_get_hash"          ;//计算杂凑值
    public static final String PSBC_SET_CARD_NO =        "psbc_set_card_no"       ;//设置账号或卡号信息
    public static final String PSBC_CANCEL_APP =         "psbc_cancel_app"        ;//取消应用
    public static final String PSBC_GET_DEVICE_ID =      "psbc_get_device_id"     ;//获取设备序列号
    public static final String PSBC_GET_DISCRETE =       "psbc_get_discrete"      ;//获取离散因子
    public static final String PSBC_SIGN_IN =            "psbc_sign_in"           ;//柜员签到
    public static final String PSBC_SIGN_OUT =           "psbc_sign_out"          ;//柜员签退
    public static final String PSBC_EVALUATE =           "psbc_evaluate"          ;//评价
    public static final String PSBC_GET_PHOTO =          "psbc_get_photo"         ;//查询柜员照片
    public static final String PSBC_DOWNLOAD =           "psbc_download"          ;//图片下载
    public static final String PSBC_INFOVIEWER =         "psbc_infoviewer"        ;//信息交互
    public static final String PSBC_DEVICE_TYPE =        "psbc_device_type"       ;//获取设备类型
    public static final String PSBC_PIN_RULE =           "psbc_pin_rule"          ;//设置密码键盘运算模式
    public static final String PSBC_GET_ENCRYPTION =     "psbc_get_encryption"    ;//获取加密算法
    public static final String PSBC_GET_AMENDS =         "psbc_get_amends"        ;//获取后补字符
    public static final String PSBC_GET_PIN_LEN =        "psbc_get_pin_len"       ;//获取密码长度
    public static final String PSBC_GET_PIN_RULE =       "psbc_get_pin_type"      ;//获取密码输入规则

    public static final String PSBC_SET_CLEAR_MAIN_KEY ="psbc_set_clear_main_key";//明文下载主密钥
    public static final String PSBC_SET_MAIN_KEY ="psbc_set_main_key";//密文下载主密钥
    public static final String PSBC_SET_WORK_KEY_OLD ="psbc_set_work_key_old";//明文下载主密钥(旧方法，可能不启用)
    public static final String PSBC_CHECK_KEY     = "psbc_check_key"    ;//检查密钥
    public static final String PSBC_SET_PIN_KEY   = "psbc_set_pin_key"  ;//设置pin加密使用的工作密钥
    public static final String PSBC_RESET_FACTORY = "psbc_reset_factory";//恢复出厂设置
    public static final String PSBC_SET_SN        = "psbc_set_sn"       ;//设置序列号/SN
    public static final String PSBC_GET_SN        = "psbc_get_sn"       ;//读取序列号/SN
    public static final String PSBC_SET_MODE      = "psbc_set_mode"     ;//设置设备系统模式
    public static final String PSBC_GET_MODE      = "psbc_get_mode"     ;//获取设备系统模式
    public static final String PSBC_SET_HINT      = "psbc_set_hint"     ;//设置信息交互提示语
    public static final String PSBC_SET_DESTORY   = "psbc_set_destory"  ;//设置开盖自毁
    public static final String PSBC_SET_DEVICE_ID = "psbc_set_device_id";//设置设备序列号

    //public static final String ZJYC_LAST_INFO = "zjyc_last_info";        //浙江邮政信息交互上次内容存储标志

    //邮储相关参数存储
    public static final String PSBC_DEVICEID = "deviceid";                 //设备序列号
    public static final String PSBC_SN_ID = "psbc_sn_id";                  //卡号，账号
    public static final String PSBC_DISCTETE = "psbc_discrete";              //离散因子
    public static final String PSBC_PUBLIC_KEY = "psbc_public_key";       //公私
    public static final String PSBC_ENCRYPTION = "psbc_encryption";       //加密算法类型
    public static final String PSBC_AMENDS = "psbc_amends";                //Pin后补规则
    public static final String PSBC_KEY_LEN = "psbc_key_len";              //密码长度
    public static final String PSBC_INPUT_RULE = "psbc_input_rule";       //密码输入上传规则
    public static final String PSBC_CARD_NO = "psbc_cade_no";              //卡号，账号
    //public static final String PSBC_PIN_RULE = "psbc_pin_rule";            //pin运算规则
    public static final String PSBC_SOUND = "psbc_sound";                   //语音播放信息
    public static final String PSBC_SOUND_ID = "psbc_sound_id";                   //语音播放信息
    public static final String PSBC_WORK_KEY_SM4 = "psbs_work_key_sm4";             //工作密钥
    public static final String PSBC_WORK_KEY_DES = "psbs_work_key_des";             //工作密钥
    public static final String PSBC_WORK_KEY_3DES = "psbs_work_key_3des";             //工作密钥
    public static final String PSBC_SIGN_DISCTETE = "psbc_sign_discrete";           //轨迹加密密钥离散因子
    public static final String PSBC_SIGN_ENCRYPTION = "psbc_sign_encryption";       //轨迹加密算法
    public static final String PSBC_SIGN_WORK_KEY_SM4 = "psbs_sign_work_key_sm4";   //轨迹加密工作密钥

    //加密方式
    public static final byte PSBC_ENCRYPTION_DES =0x01;
    public static final byte PSBC_ENCRYPTION_3DES =0x02;
    public static final byte PSBC_ENCRYPTION_3DES_24 =0x03;
    public static final byte PSBC_ENCRYPTION_SM4 =0x04;

    //输入方式 自动返回/点击确认返回
    public static final byte PSBC_INPUT_CONFIRM =0x01;
    public static final byte PSBC_INPUT_AUTO =0x02;

}
