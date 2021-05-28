package com.itep.mt.common.finger;

import android.graphics.Bitmap;

/**
 * 指纹结果集类
 * Created by hx on 2019/7/18.
 */
public class FingerResult {
    /**
     * 状态
     */
    private int status;

    /**
     * 消息
     */
    private String msg;

    /**
     * 图片
     */
    private Bitmap img;

    /**
     * 特征模板
     */
    private byte[] template;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public byte[] getTemplate() {
        return template;
    }

    public void setTemplate(byte[] template) {
        this.template = template;
    }
}
