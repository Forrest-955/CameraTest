package com.itep.mt.common.finger;


/**
 * 指纹回调类
 * Created by hx on 2019/7/18.
 */
public abstract class FingerCallback{

    /**
     * 成功
     * @param fingerResult 指纹结果集合
     */
    public abstract void onSuccess(FingerResult fingerResult);

    /**
     * 失败
     * @param code 错误编码
     */
    public abstract void onError(int code);

    /**
     * 调用进程
     * @param code  状态码
     * @param result 结果集
     */
    public void onProcess(int code, FingerResult result) {

    }

}