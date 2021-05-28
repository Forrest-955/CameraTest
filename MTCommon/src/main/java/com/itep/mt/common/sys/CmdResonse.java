package com.itep.mt.common.sys;

import org.json.JSONObject;

/**
 * 命令的执行响应对象
 */

public class CmdResonse {
    private String type;        //命令码
    private Boolean result;     //执行结果
    private String err_msgs;    //错误信息
    private JSONObject jsdata;  //结果数据

    public CmdResonse(String type, boolean result, String err_msgs, JSONObject data) {
        this.type = type;
        this.result = result;
        this.err_msgs = err_msgs;
        this.jsdata = data;
    }

    public String getType() {
        return type;
    }

    public Boolean getResult() {
        return result;
    }

    public JSONObject getJsdata() {
        return jsdata;
    }

    public String getErr_msgs() {
        return err_msgs;
    }

    /**
     * 解析命令响应，得出响应对象
     *
     * @param jsStr json对象字符串
     * @return 命令响应对象
     */
    public static CmdResonse parseInstance(String jsStr) {
        try {
            JSONObject js = new JSONObject(jsStr);
            if (js.has("type") && js.has("success") && js.has("error_message")
                    && js.has("data")) {
                CmdResonse cr = new CmdResonse(js.optString("type"), js.optBoolean("success"),
                        js.optString("error_message"), js.optJSONObject("data"));
                return cr;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
