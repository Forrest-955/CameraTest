package com.itep.mt.common.ipc;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * 动作响应
 */
public class ActionResponse implements Parcelable {
    private String id;                //类型ID

    private String resultString = "";                        //执行结果, 字符串类型
    private byte[] resultByteArray = new byte[0];        //执行结果，数组类型
    private int resultInt = 0;                            //执行结果，整数类型
    private JSONObject resultOther = new JSONObject();    //执行结果, 其他类型

    public ActionResponse(String id) {
        this.id = id;
    }

    public ActionResponse(String id, String resultString) {
        this.id = id;
        this.resultString = resultString;
    }

    public ActionResponse(String id, byte[] resultByteArray) {
        this.id = id;
        this.resultByteArray = resultByteArray;
    }

    public ActionResponse(String id, int resultInt) {
        this.id = id;
        this.resultInt = resultInt;
    }

    public ActionResponse(String id, JSONObject resultOther) {
        this.id = id;
        this.resultOther = resultOther;
    }

    public ActionResponse(String id, String resultString,
                          byte[] resultByteArray, int resultInt, JSONObject resultOther) {
        super();
        this.id = id;
        this.resultString = resultString;
        this.resultByteArray = resultByteArray;
        this.resultInt = resultInt;
        this.resultOther = resultOther;
    }

    public String getId() {
        return id;
    }

    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }

    public byte[] getResultByteArray() {
        return resultByteArray;
    }

    public void setResultByteArray(byte[] resultByteArray) {
        this.resultByteArray = resultByteArray;
    }

    public int getResultInt() {
        return resultInt;
    }

    public void setResultInt(int resultInt) {
        this.resultInt = resultInt;
    }

    public JSONObject getResultOther() {
        return resultOther;
    }

    public void setResultOther(JSONObject resultOther) {
        this.resultOther = resultOther;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(resultString);
        dest.writeByteArray(resultByteArray);
        dest.writeInt(resultInt);
        dest.writeString(resultOther.toString());
    }

    public static final Creator<ActionResponse> CREATOR = new Creator<ActionResponse>() {
        @Override
        public ActionResponse createFromParcel(Parcel in) {
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

            return new ActionResponse(type, paramstr, paramba, paramint, jo);
        }

        @Override
        public ActionResponse[] newArray(int size) {
            return new ActionResponse[size];
        }
    };
}
