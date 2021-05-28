package com.itep.mt.common.util;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.itep.mt.common.sys.SysConf;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Pattern;

import taobe.tec.jcc.JChineseConvertor;

/**
 * Created by linjinlong on 2018/8/10.
 */

public class JSYHCommon {

    public static String sToc(String str) {
        JChineseConvertor jChineseConvertor;
        String complexText = "";
        try {
            jChineseConvertor = JChineseConvertor.getInstance();
            complexText = jChineseConvertor.s2t(str);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return complexText;
    }


    /**
     * @param str
     */
    public static boolean isInteger(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }


    /**
     * android 语言设置
     */
    public static void switchLanguage() {
        //设置应用语言类型
        Resources resources = ContextUtil.getInstance().getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (SysConf.get_JSYH_SET_LAN() == 1) {
            config.locale = Locale.ENGLISH;
        } else if (SysConf.get_JSYH_SET_LAN() == 2) {
            config.locale = Locale.TRADITIONAL_CHINESE;
        } else {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        }
        resources.updateConfiguration(config, dm);
    }

    public static String getBranchBankName() {
        String branchBankName = "";
        if (SysConf.get_JSYH_SET_LAN() == 1) {
            String name = SysConf.get_JSYH_BANK_NAME();
            try {
                if (name.contains("支行")) {
                    branchBankName = PingYinUtil.getPingYin(name.substring(0, name.lastIndexOf("支行"))) + " SUB-BR";

                } else {
                    branchBankName = PingYinUtil.getPingYin(name) + " SUB-BR";
                }

                if (branchBankName.length() > 15) {
                    if (name.contains("支行")) {
                        branchBankName = PingYinUtil.getEachFirstSpell(name.substring(0, name.lastIndexOf("支行"))) + " SUB-BR";

                    } else {
                        branchBankName = PingYinUtil.getEachFirstSpell(name) + " SUB-BR";
                    }
                }


            } catch (Exception e) {
                Logger.i(e + "");
                branchBankName = name;
            }

        } else if (SysConf.get_JSYH_SET_LAN() == 2) {
            branchBankName = sToc(SysConf.get_JSYH_BANK_NAME());
        } else {
            branchBankName = SysConf.get_JSYH_BANK_NAME();
        }

        return branchBankName;
    }

    ;
}

