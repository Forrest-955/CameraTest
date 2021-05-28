package com.itep.mt.common.dto;
import java.io.Serializable;

public abstract class BaseDTO implements Serializable {

    private String msgType;

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
