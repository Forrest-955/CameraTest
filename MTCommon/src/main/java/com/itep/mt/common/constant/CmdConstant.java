package com.itep.mt.common.constant;

/**
 * 指令常量类
 */
public class CmdConstant {

    public static final byte[] CMD_OK = {0x00, 0x00};   //成功返回值

    public static final byte[] CMD_3023 = {0x30, 0x23};//获取柜员图片

    public static final byte[] CMD_3050 = {0x30, 0x50};//删除文件

    public static final byte[] CMD_3054 = {0x30, 0x54};//设置音量

    public static final byte[] CMD_3055 = {0x30, 0x55};//设置广告播放时间

    public static final byte[] CMD_3056 = {0x30, 0x56};//查看文件

    public static final byte[] CMD_3059 = {0x30, 0x59}; //资源下载

    public static final byte[] CMD_305A = {0x30, 0x5A};//下载资源后返回

    public static final byte[] CMD_3060 = {0x30, 0x60}; //设置工厂序列号

    public static final byte[] CMD_3061 = {0x30, 0x61};//获取工厂序列号

    public static final byte[] CMD_3062 = {0x30, 0x62};//获取设备信息

    public static final byte[] CMD_3067 = {0x30, 0x67};  //设置编码格式

    public static final byte[] CMD_3068 = {0x30, 0x68};  //设置hidInterval

    public static final byte[] CMD_3069 = {0x30, 0x69};// 获取hid属性

    public static final byte[] CMD_3070 = {0x30, 0x70};//设置Pid

    public static final byte[] CMD_3071 = {0x30, 0x71};//设置Pid

    public static final byte[] CMD_3072 = {0x30, 0x72};//设置下载速率

    public static final byte[] CMD_3073 = {0x30, 0x73};//设置上传速率

    public static final byte[] CMD_3074 = {0x30, 0x74};//设置波特率

    //public static final byte[] CMD_3077 = {0x30, 0x77};//获取hidInterval

    public static final byte[] CMD_3078 = {0x30, 0x78};//获取下载速率

    public static final byte[] CMD_3079 = {0x30, 0x79};//获取上传速率

    public static final byte[] CMD_3080 = {0x30, (byte) 0x80};//获取波特率

    public static final byte[] CMD_3081 = {0x30, (byte) 0x81};//获取编码

    public static final byte[] CMD_3082 = {0x30, (byte) 0x82};//设置设备信息

    public static final byte[] CMD_3098 = {0x30, (byte) 0x98};//设置序列号

    public static final byte[] CMD_3099 = {0x30, (byte) 0x99};//重启

    public static final byte[] CMD_5002 = {0x50, 0x02}; // 信息交互数据分析

    public static final byte[] CMD_5005 = {0x50, 0x05};//电子工牌

    public static final byte[] CMD_5006 = {0x50, 0x06};//评价

    public static final byte[] CMD_5009 = {0x50, 0x09}; // 设置自定义设备序列号

    public static final byte[] CMD_500A = {0x50, 0x0a};//获取自定义设备序列号

    public static final byte[] CMD_5010 = {0x50, 0x10}; // 推送PDF签字

    public static final byte[] CMD_501A = {0x50, 0x1A}; // 读取指纹

    public static final byte[] CMD_5013 = {0x50, 0x13}; // 显示PDF

    public static final byte[] CMD_5014 = {0x50, 0x14}; // 问卷调查界面按钮

    public static final byte[] CMD_5015 = {0x50, 0x15}; // 问卷调查

    public static final byte[] CMD_5018 = {0x50, 0x18}; // 列表选择

    public static final byte[] CMD_5019 = {0x50, 0x19}; // 问卷调查 每题

    public static final byte[] CMD_500B = {0x50, 0x0b};//复位

    public static final byte[] CMD_500C = {0x50, 0x0c};//初始化密钥失败

    public static final byte[] CMD_500D = {0x50, 0x0d}; // 下载主密钥

    public static final byte[] CMD_500E = {0x50, 0x0e}; // 下载工作密钥

    public static final byte[] CMD_500F = {0x50, 0x0f}; // 获取密码

    public static final byte[] CMD_5007 = {0x50, 0x07}; // 获取数字

    public static final byte[] CMD_5008 = {0x50, 0x08}; // 获取明文

    public static final byte[] CMD_501D = {0x50, 0x1d}; // 显示二维码

    public static final byte[] CMD_5021 = {0x50, 0x21}; // 显示交互信息并输入密码

    public static final byte[] CMD_5023 = {0x50, 0x23}; // 激活密钥号

    public static final byte[] CMD_5024 = {0x50, 0x24}; // 获取当前密钥号和长度

    public static final byte[] CMD_502B = {0x50, 0x2B};//PDF 信息确认

    public static final byte[] CMD_5040 = {0x50, 0x40}; // 获取密钥校验值

    public static final byte[] CMD_5041 = {0x50, 0x41}; // 生成公私钥

    public static final byte[] CMD_5042 = {0x50, 0x42}; // 下载公钥

    public static final byte[] CMD_5049 = {0x50, 0x49}; // 获取密码

    public static final byte[] CMD_5050 = {0x50, 0x50}; // 信息交互扩展

    public static final byte[] CMD_5051 = {0x50, 0x51}; // 推送PDF显示信息或确认

    public static final byte[] CMD_5052 = {0x50, 0x52}; // 推送PDF签字

    public static final byte[] CMD_5053 = {0x50, 0x53}; // 评价，包含不满原因选择

    public static final byte[] CMD_5054 = {0x50, 0x54}; // 设置柜员信息

    public static final byte[] CMD_501E = {0x50, 0x1e}; // 下载公钥

    public static final byte[] CMD_6007 = {0x60, 0x07};//获取指纹特征信息

    public static final byte[] CMD_6008 = {0x60, 0x08};//获取指纹模板信息

    public static final byte[] CMD_6014 = {0x60, 0x14};//开示摄像

    public static final byte[] CMD_6015 = {0x60, 0x15};//关闭摄像

    public static final byte[] CMD_600D = {0x60, 0x0D};//设置网点信息

    public static final byte[] CMD_600E = {0x60, 0x0E};//获取头像MD5

    public static final byte[] CMD_6011 = {0x60, 0x11};//交互信息带协议

    public static final byte[] CMD_602A = {0x60, 0x2A};//密钥检验

    public static final byte[] CMD_6036 = {0x60, 0x36};//获取文件块数

    public static final byte[] CMD_6037 = {0x60, 0x37};//获取文件

    public static final byte[] CMD_6043 = {0x60, 0x43};//获取MD5值

    public static final byte[] CMD_5444 = {0x54, 0x44};//获取文件

    public static final byte[] CMD_6044 = {0x60, 0x44};//打开双目摄像头

    public static final byte[] CMD_6050 = {0x60, 0x50};//获取文件信息

    public static final byte[] CMD_6051 = {0x60, 0x51};//获取文件内容

    public static final byte[] CMD_6060 = {0x60, 0x60};//曲靖商业银行设置是否国密加密

    public static final byte[] CMD_FFFF={ (byte) 0xff, (byte) 0xff};//错误
}
