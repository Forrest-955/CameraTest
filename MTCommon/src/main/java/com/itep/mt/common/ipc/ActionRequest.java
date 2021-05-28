package com.itep.mt.common.ipc;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * 请求的动作
 */
public class ActionRequest implements Parcelable {

    private String type;                        //动作类型标识

    private String paramStr = "";                    //字符串类型的参数
    private byte[] paramByteArray = new byte[0];    //字节数组类型的参数
    private int paramInt = 0;                        //整数类型的参数
    private JSONObject paramOther = new JSONObject();//其他参数

    public ActionRequest(String type) {
        this.type = type;
    }

    public ActionRequest(String type, String param) {
        this.type = type;
        this.paramStr = param;
    }

    public ActionRequest(String type, byte[] param) {
        this.type = type;
        this.paramByteArray = param;
    }

    public ActionRequest(String type, int param) {
        this.type = type;
        this.paramInt = param;
    }

    public ActionRequest(String type, JSONObject param) {
        this.type = type;
        this.paramOther = param;
    }

    public ActionRequest(String type, String paramStr, byte[] paramByteArray, int paramInt, JSONObject paramJs) {
        this.type = type;
        this.paramStr = paramStr;
        this.paramByteArray = paramByteArray;
        this.paramInt = paramInt;
        this.paramOther = paramJs;
    }

    public String getType() {
        return type;
    }

    public String getParamStr() {
        return paramStr;
    }

    public void setParamStr(String paramStr) {
        this.paramStr = paramStr;
    }

    public byte[] getParamByteArray() {
        return paramByteArray;
    }

    public void setParamByteArray(byte[] paramByteArray) {
        this.paramByteArray = paramByteArray;
    }

    public int getParamInt() {
        return paramInt;
    }

    public void setParamInt(int paramInt) {
        this.paramInt = paramInt;
    }

    public JSONObject getParamOther() {
        return paramOther;
    }

    public void setParamOther(JSONObject param) {
        this.paramOther = param;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //序列化
        dest.writeString(type);
        dest.writeString(paramStr);
        dest.writeByteArray(paramByteArray);
        dest.writeInt(paramInt);
        dest.writeString(paramOther.toString());
    }

    public static final Creator<ActionRequest> CREATOR = new Creator<ActionRequest>() {
        @Override
        public ActionRequest createFromParcel(Parcel in) {
            //反序列化
            String type = in.readString();
            String paramstr = in.readString();
            byte[] paramba = in.createByteArray();
            int paramint = in.readInt();
            String param = in.readString();
            JSONObject jo = null;

            try {
                jo = new JSONObject(param);
            } catch (Exception e) {
                e.printStackTrace();
                jo = new JSONObject();
            }

            return new ActionRequest(type, paramstr, paramba, paramint, jo);
        }

        @Override
        public ActionRequest[] newArray(int size) {
            return new ActionRequest[size];
        }
    };
}
