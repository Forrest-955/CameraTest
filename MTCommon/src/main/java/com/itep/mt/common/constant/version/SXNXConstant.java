package com.itep.mt.common.constant.version;

import com.itep.mt.common.constant.AppConstant;

/**
 * 山西农信常量（陕西农信在后面）
 * Created by JOY on 2017/10/16.
 */

public class SXNXConstant {

    //密码键盘部分指令集
    public static final String SXNX_SET_MAIN_KEY_NO = "enableKeyNoByELf";                   //激活密钥号
    public static final String SXNX_GET_INPUT_PWD = "getInputPwdByElf";                       //获取密码
    public static final String SXNX_GET_INPUT_PWD_PINBLOCK = "getInputPwdPinblockByElf";     //获取密码PINBLOCK
    public static final String SXNX_SET_MAIN_KEY = "setMainKeyByElf";                         //修改密码键盘主密钥
    public static final String SXNX_SET_INPUT_LENGTH = "setInputLengthByElf";                 //设置密码长度
    public static final String SXNX_DELETE_KEY = "deleteKeyByElf";                             //删除密钥
    public static final String SXNX_SET_WORK_KEY = "setWorkKeyByElf";                         //修改工作密钥（用户密钥）
    public static final String SXNX_SET_ENCRYPTION = "setEncryptionByElf";                     //设置密钥加密模式
    public static final String SXNX_CANCEL_APP = "closeApp";                             //取消应用

    public static final String SXNX_REPLY = "sxnx_reply";                                       //检查密钥
    public static final String SXNX_REPLY_PARAM = "sxnx_reply_param";                           //检查密钥

    public static final String SXNX_MAIN_NO = "sxnx_main_no";                                   //激活的主密钥号
    public static final String SXNX_WORK_NO = "sxnx_work_no";                                   //激活的工作密钥号
    public static final String SXNX_INPUT_LENGTH = "sxnx_input_length";                         //密码长度
    public static final String SXNX_ENCRYPTION = "sxnx_encryption";                             //加密模式
    public static final String SXNX_MAIN_KEY = "sxnx_main_key_";                                //主密钥+序号为对应的存储键值
    public static final String SXNX_WORK_KEY = "sxnx_work_key_";                                //工作密+钥序号为对应的存储键值
    public static final String SXNX_GET_PWD_PROCLAIMED = "getPwdClearByOld";             //获取明文
    public static final String SXNX_GET_PWD_PROCLAIMED_AGAIN = "getPwdClearByOld"; //再次获取明文
    public static final String SXNX_GET_PWD_PROCLAIMED_ALL = "getPwdClearAllByOld";             //获取明文
    public static final String SXNX_GET_PWD_PROCLAIMED_ALL_AGAIN = "getPwdClearAllByOld"; //再次获取明文

    //指纹仪指令集
    public static final String SXNX_FINGER_ENABLE = "sxnx_finger_enable";               //指纹模块确认
    public static final String SXNX_FP_GET_FEATURE = "sxnx_fp_get_feature";              //获取指纹特征值
    public static final String SXNX_FP_GET_TEMPLATE = "sxnx_fp_get_template";             //获取指纹模版
    public static final String SXNX_FP_GET_IMGDATA = "sxnx_fp_get_imgdata";             //获取指纹图像
    public static final String SXNX_FP_GET_IMGDATA_BLOCKS = "sxnx_fp_get_imgdata_blocks";//获取指纹图像块数
    public static final String SXNX_IMGDATA = "sxnx_imgdata";                          //指纹图像存储
    public static final String SXNX_IMGDATA1_PATH = AppConstant.PATH_CONF_ROOT + "common/sxnximage1.bmp"; //指纹图像1存储路径
    public static final String SXNX_IMGDATA2_PATH = AppConstant.PATH_CONF_ROOT + "common/sxnximage2.bmp"; //指纹图像2存储路径
    public static final String SXNX_IMGDATA3_PATH = AppConstant.PATH_CONF_ROOT + "common/sxnximage3.bmp"; //指纹图像3存储路径

    public static final String SXNX_INFOVIEWER_PDF_SIGN = "sxnx_infoviewer_pdf_sign";  //推送PDF签字需等待返回值

    //陕西农信
    public static final String SXNX_SHANXI_SET_WORK_KEY = "sxnx_shanxi_set_work_key"; //修改工作密钥（用户密钥）
    public static final String SXNX_SHANXI_SET_MAIN_KEY = "sxnx_shanxi_set_main_key"; //下载主密钥密文
    public static final String SXNX_SHANXI_CHECKVALUE = "sxnx_shanxi_checkvalue";    //计算校验值

    //辽宁农信
    public static final String SXNX_SET_FIXED_KEY = "setFixedKeyByElf";                     //设置使用固定密钥
}
