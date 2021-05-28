package com.itep.mt.common.constant.version;

/**
 * 命令常量，被用于以下场景：
 * （1）每个指令集配置文件，作为每个指令的消息类型参数，即json对象的msg属性。
 * （2）调度程序com.itep.mt.dispatch.work.version包及以下、实现ICmdExecutable接口的类，
 * 作为这些命令执行单元Handler的命令标识(Cmd id)。这此命令标识与指令的消息类型参数一致。
 * （3）调度程序发送的ActionRequest请求对象中，作为请求动作类型标识
 * （4）各个业务应用程序回送的ActionResponse响应对象，作为响应结果的类型标识
 * 常量通常定义为"客户版本_模块_动作"， 通用版常量前不带标识，特殊版本才带
 */
public class GenericConstant {

    //下载参数
    public static final String DOWN_SIGN = "down_sign";
    public static final String DOWN_FILE_TYPE = "down_file_type";               //下载文件类型
    public static final String DOWN_DATA_TYPE = "down_data_type";               //下载文件类型
    public static final String DOWN_ZIP_TYPE = "down_zip_type";               //下载文件类型
    public static final String DOWN_FILE_NAME = "down_file_name";   //文件名
    public static final String DOWN_FILE_SIZE = "down_file_size";   //文件大小
    public static final String DOWN_FILE_MD5 = "down_file_md5";     //文件MD5值

    //广告播放
    public static final String GENERIC_MEDIAPLAYER = "generic_mediaplayer";    //广告播放
    public static final String GENERIC_MEDIAPLAYER_APPOINT = "generic_mediaplayer_appoint";    //广告播放指定播放
    public static final String GENERIC_MEDIAPLAYER_UPDATEPLAY="updatePlay";//更新播放
    public static final String GENERIC_MEDIAPLAYER_REFRESH_PLAYLIST="refreshPlayList";//刷新广告列表
    public static final String GENERIC_MEDIAPLAYER_SYNC_VOLUME="syncVolume";//同步音量
    //信息交互
    public static final String GENERIC_INFOVIEWER = "generic_infoviewer";                      //通用信息交互
    public static final String GENERIC_INFOVIEWER_PDF_SIGN = "generic_infoviewer_pdf_sign";  //推送PDF签字需等待返回值
    public static final String GENERIC_INFOVIEWER_PDF_SIGN_ASYNC = "generic_infoviewer_pdf_sign_async";//推送PDF签字异步
    public static final String GENERIC_INFOVIEWER_PDF_GET_SIGN_STATE = "generic_infoviewer_pdf_get_sign_state";//查询PDF签字状态
    public static final String GENERIC_INFOVIEWER_PDF_SHOW = "generic_infoviewer_pdf_show";//推送PDF显示功能
    public static final String GENERIC_GET_SIGNDATA = "generic_get_signdata";//获取签名坐标数据
    public static final String GENERIC_GET_SIGNDATA_BLOCKS = "generic_get_signdata_blocks";//获取签名坐标数据块数
    public static final String GENERIC_GET_FINGER_FEATURE = "generic_get_finger_feature";//获取指纹特征值
    public static final String GENERIC_GET_FINGER_FEATURE_BLOCKS = "generic_get_finger_feature_blocks";//获取指纹特征值块数
    public static final String GENERIC_GET_SIGNPDF_BLOCKS = "generic_get_signpdf_blocks";//获取签名pdf块数
    public static final String GENERIC_GET_SIGNPDF = "generic_get_signpdf";//获取签名pdf
    public static final String GENERIC_LIST_SELECT = "generic_list_select";//列表选择
    public static final String GENERIC_QUESTIONNAIRE_HASBUTTON = "generic_questionnaire_hasbutton";//问卷调查带按钮
    public static final String GENERIC_QUESTIONNAIRE_EVERY_ANSWER = "generic_questionnaire_every_answer";//问卷调查每题返回
    public static final String GENERIC_QUESTIONNAIRE = "generic_questionnaire";//问卷调查
    public static final String GENERIC_READ_FINGER = "generic_read_finger";//读取指纹
    public static final String GENERIC_READ_FINGER_RESULT_SHOW = "generic_read_finger_result_show";//读取指纹结果显示
    public static final String GENERIC_QRCODE_PICTURE_SHOW = "generic_qrcode_picture_show";//二维码图片展示
    //参数存储
    public static final String PDF_SIGN_STATE = "pdf_sign_state";//PDF签名状态 0未签字，1正在签字，2签字完成，3超时，4用户取消
    public static final String PDF_SIGN_DATA = "pdf_sign_data";//PDF签名坐标数据
    public static final String PDF_SIGN_PIC_BLOCKS = "pdf_sign_pic_blocks";//PDF签名图片块数
    public static final String PDF_SIGN_DATA_BLOCKS = "pdf_sign_data_blocks";//PDF签名坐标块数
    public static final String PDF_FINGER_FEATURE_DATA = "pdf_finger_feature_data";//PDF指纹特征值数据

    //评价
    public static final String GENERIC_OPEN_WORK_CARD = "generic_open_work_card";         //启动电子工牌
    public static final String GENERIC_EVALUATE = "generic_evaluate";                      //评价

    //通用密码键盘部分指令集
    public static final String GENERIC_SET_MAIN_KEY = "generic_set_main_key";            //下载主密钥
    public static final String GENERIC_SET_WORK_KEY = "generic_set_work_key";        //下载工作密钥（用户密钥）
    public static final String GENERIC_GET_INPUT_PWD = "generic_get_input_pwd";        //获取密码

    public static final String GENERIC_GET_INPUT_PWD_CLEAR = "generic_get_input_pwd_clear"; //获取密码明文2

    public static final String GENERIC_INIT_KEY = "generic_init_key";              //密钥初始化
    public static final String GENERIC_GET_NUM = "generic_get_num";                //获取数字
    public static final String GENERIC_SET_PUBLIC_KEY = "generic_set_public_key";//下载公钥
    public static final String GENERIC_INFO_GET_PASSWORD = "generic_info_get_password";//显示交互信息并输入密码
    public static final String GENERIC_SET_KEY_NO = "generic_set_key_no";       //激活密钥号
    public static final String GENERIC_GET_KEY_NO_AND_LENGTH = "generic_get_keyno_and_length";//获取当前密钥号和长度

    //通用密码键盘相关参数存储
    public static final String GENERIC_MAIN_KEY = "generic_main_key_";           //主密钥+序号为对应的存储键值
    public static final String GENERIC_WORK_KEY = "generic_work_key_";            //工作密+钥序号为对应的存储键值
    public static final String GENERIC_MAIN_KEY_NO = "generic_main_key_no";       //激活的主密钥号的存储键值
    public static final String GENERIC_WORK_KEY_NO = "generic_work_key_no";       //激活的工作密钥号的存储键值

    public static final String GENERIC_SN = "generic_sn";       //序列号

    public static final String GENERIC_CUSTOM_SN="generic_custom_sn";//自定义序列号

    //通用返回值标志
    public static final String GENERIC_REPLY = "generic_reply";                       //不带参数返回
    public static final String GENERIC_REPLY_PARAM = "generic_reply_param";         //带参数返回
    public static final String GENERIC_REPLY_OTHER = "generic_reply_other";         //特殊返回
    public static final String GENERIC_REPLY_DIRECT = "generic_reply_direct";         //不加密返回
    public static final String GENERIC_REPLY_TKEY = "generic_reply_tkey";         //旧传输密钥返回

    public static final String GENERIC_CANCEL_APP = "closeApp";         //取消应用

    public static final int GENERIC_SIGH_DATA_LENGTH = 1024;                        //签名数据下载块长度
//    public static final int GENERIC_SIGH_DATA_LENGTH = 512;                        //江西农信签名数据下载块长度



    public static final String INPUT_LENGTH="input_length";//输入密码长度

    public static final String ENCRYPTION="encryption";//设置加密模式

    public static final String GENERIC_WORK_INFO="generic_work_info";//设置柜员信息

    //国密认证
    public static final String GM_INTERNAL_AUTH = "gm_internal_auth";           //内部认证结果
    public static final String GM_RANDOMNUM ="gm_randomnum";                    //随机数用于验证外部认证的随机数
    public static final String GM_EXTERNAL_AUTH = "gm_external_auth";            //外部认证结果
    public static final String GM_EXTERNAL_COUNT = "gm_external_count";            //外部认证次数
    public static final int    GM_EXTERNAL_DEFAULT_COUNT = 5;            //外部认证次数默认值
    public static final String GM_MACKEY = "gm_mackey";       //macKEY   SM4 进行加密使用的
    public static final String GM_INTERNAL_AUTH_KEY = "gm_internal_auth_key";       //内部认证密钥
    public static final String GM_EXTERNAL_AUTH_KEY = "gm_external_auth_key";            //外部认证密钥
    public static final String GM_SERVER_NUM ="gm_server_num";//服务端序号
    public static final String GM_DEVICE_NUM ="gm_device_num";//客户端序号

    public static final String GM_SM2PUBLICKKEY ="gm_sm2publickkey";//SM2公钥
    public static final String GM_SM2PRIVATEKEY = "gm_sm2privatekey";//SM2私钥
    public static final String GM_MAINKEY ="gm_mainkey";//主密钥明文
    public static final String GM_WORKKEY = "gm_workkey";//工作密钥明文
    public static final String GM_TPK = "gm_workkey";//工作密钥明文
    public static final String GM_AIK = "gm_workkey";//身份证密钥
    public static final String GM_TRAMSKEY = "gm_tramskey";//认证密钥

    public static final String GM_TRAMSKEYDEFAULT = "30303030303030303030303030303030";       //macKEY   SM4 进行加密使用的
    public static final String GM_MACKEYDEFAULT = "30303030303030303030303030303030";       //macKEY   SM4 进行加密使用的
    public static final String GM_MAINKEYDEFAULT= "38383838383838383838383838383838";       //macKEY   SM4 进行加密使用的
    public static final String GM_INTERNAL_AUTH_KEYDEFAULT= "36363636363636363636363636363636";       //macKEY   SM4 进行加密使用的
    public static final String GM_EXTERNAL_AUTH_KEYDEFAULT= "37373737373737373737373737373737";       //macKEY   SM4 进行加密使用的

    public static final String GM_CLEA_ZERO = "0000000000000000000000000000000";
    public static final String GENERIC_OPEN_CHEKC="generic_open_chekc";

}
