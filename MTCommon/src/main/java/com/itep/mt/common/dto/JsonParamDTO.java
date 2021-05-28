package com.itep.mt.common.dto;

import com.itep.mt.common.annotation.ParamField;

/**
 * JSON数据参数
 */
public class JsonParamDTO extends BaseDTO{

    /**
     * JSON格式数据
     */
    @ParamField(sort = 0, len = 2, lenType = ParamField.LenType.DYNAMIC,encode = ParamField.Encode.ASC)
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "JsonParamDTO{" +
                "data='" + data + '\'' +
                '}';
    }
}
