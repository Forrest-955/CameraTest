package com.itep.mt.common.util;

import java.text.DecimalFormat;

public class DeviceUtils {

    /**
     * 生成SN
     * @param sn  原SN
     * @param size 需要生成的长度
     * @return  返回新SN
     */
    public static String generateSn(String sn,int size){
        String newSn=null;
        if(!StringUtil.isBlank(sn)){
            if(sn.length()>=size){
                newSn=sn.substring(0, size);
            }else{
                int fill=size-sn.length();
                int num=(int) Math.pow(10,fill);
                String formatStr=String.valueOf(num).replace("1","");
                newSn=sn+new DecimalFormat(formatStr).format((int) (Math.random() * num));
            }
        }
        return newSn;
    }

}
