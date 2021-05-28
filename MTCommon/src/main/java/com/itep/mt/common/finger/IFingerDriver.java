package com.itep.mt.common.finger;

import android.content.Context;

/**
 * 指纹设备接口类
 * Created by hx on 2019/7/18.
 */
public interface IFingerDriver {

    /**
     * 成功
     */
    public int CODE_SUCCEE=0;

    /**
     * 连接异常
     */
    public int CODE_CONN_ERR=-2;

    /**
     * 超时异常
     */
    public int TIME_OUT_ERR=-3;

    /**
     * 用户取消
     */
    public int CANCLE_ERR=-4;  // 针对天诚指纹切换时错误返回。

    /**
     * 异常
     */
    public int CODE_ERR=-1;

    /**
     * 初始化
     * @param context
     */
     void init(Context context);

    /**
     * 打开设备
     */
    boolean openDevice();

    /**
     * 关闭设备
     */
    void colseDevice();

    /**
     * 获取指纹信息
     * @param timeOut  超时时间
     * @param fingerCallback 指纹回调
     */
    void getFeature(int timeOut,FingerCallback fingerCallback);

    /**
     * 关闭业务操作
     */
    void cancel();

    /**
     * 登记指纹
     * @param timeOut 超时时间
     * @param fingerCallback 指纹回调
     */
    void enroll(int timeOut,FingerCallback fingerCallback);

    /**
     * 获取指纹模板  （不是所有指纹都支持此接口）
     * @param tz1
     * @param tz2
     * @param tz3
     * @return
     */
     String getTemplateByTz(String tz1,String tz2,String tz3);

}
