package com.itep.mt.common.util;

import com.itep.mt.common.constant.AppConstant;
import com.itep.mt.common.constant.PinConstant;
import com.itep.mt.common.sys.SysConf;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 207C10的12键键盘是15键键盘屏蔽第一排按键来使用，需要重新映射键位
 * 原先的键位对应键值如下：
 * 8        9       10
 * 11       12      13
 * 14       15      16
 * 132      7       131
 * 67       56      66
 * Created by cy on 2019/06/27.
 */

public class KeyCodeUtil {

    private static Map<Integer,Integer> KEY_CODE_MAP=new HashMap<Integer,Integer>();

    private static long time = 0;

    static {
        initConf();
    }

    /**
     * 初始化配置
     */
    public static void initConf(){
        Logger.i("初始化KeyCode");
        //读取配置
        JSONObject keyboard=ConfFile.readJSonFile(AppConstant.PATH_SYSTEM_KEYBOARD);
        if(keyboard!=null){//使用配置文件
            Iterator<String> it=keyboard.keys();
            while (it.hasNext()){
                String key=it.next();
                try {
                    JSONObject  keyCode=keyboard.getJSONObject(key);
                    Integer srcCode=keyCode.getInt("src_code");
                    Integer destCode=keyCode.getInt("dest_code");
                    KEY_CODE_MAP.put(srcCode,destCode);
                } catch (JSONException e) {
                    Logger.e("读取键盘配置异常",e);
                }
            }
        }else if(SysConf.getKeyBoardType()==12){//默认12键 (C20键值）
            KEY_CODE_MAP.put(7, PinConstant.KEY_0);
            KEY_CODE_MAP.put(8, PinConstant.KEY_1);
            KEY_CODE_MAP.put(9,PinConstant.KEY_2);
            KEY_CODE_MAP.put(10,PinConstant.KEY_3);
            KEY_CODE_MAP.put(11,PinConstant.KEY_4);
            KEY_CODE_MAP.put(12,PinConstant.KEY_5);
            KEY_CODE_MAP.put(13,PinConstant.KEY_6);
            KEY_CODE_MAP.put(14,PinConstant.KEY_7);
            KEY_CODE_MAP.put(15,PinConstant.KEY_8);
            KEY_CODE_MAP.put(16,PinConstant.KEY_9);
            KEY_CODE_MAP.put(67,PinConstant.CANCLE_KEY);//取消
            KEY_CODE_MAP.put(66,PinConstant.CONFIRM_KEY);//确定

//            KEY_CODE_MAP.put(56,PinConstant.KEY_0);
//            KEY_CODE_MAP.put(11, PinConstant.KEY_1);
//            KEY_CODE_MAP.put(12,PinConstant.KEY_2);
//            KEY_CODE_MAP.put(13,PinConstant.KEY_3);
//            KEY_CODE_MAP.put(14,PinConstant.KEY_4);
//            KEY_CODE_MAP.put(15,PinConstant.KEY_5);
//            KEY_CODE_MAP.put(16,PinConstant.KEY_6);
//            KEY_CODE_MAP.put(132,PinConstant.KEY_7);
//            KEY_CODE_MAP.put(7,PinConstant.KEY_8);
//            KEY_CODE_MAP.put(131,PinConstant.KEY_9);
//            KEY_CODE_MAP.put(67,PinConstant.CANCLE_KEY);//取消
//            KEY_CODE_MAP.put(66,PinConstant.CONFIRM_KEY);//确定
        }else{//15键
            KEY_CODE_MAP.put(7,PinConstant.KEY_0);
            KEY_CODE_MAP.put(8, PinConstant.KEY_1);
            KEY_CODE_MAP.put(9,PinConstant.KEY_2);
            KEY_CODE_MAP.put(10,PinConstant.KEY_3);
            KEY_CODE_MAP.put(11,PinConstant.KEY_4);
            KEY_CODE_MAP.put(12,PinConstant.KEY_5);
            KEY_CODE_MAP.put(13,PinConstant.KEY_6);
            KEY_CODE_MAP.put(14,PinConstant.KEY_7);
            KEY_CODE_MAP.put(15,PinConstant.KEY_8);
            KEY_CODE_MAP.put(16,PinConstant.KEY_9);
            KEY_CODE_MAP.put(132,PinConstant.BACKSPACE_KEY);//更正
            KEY_CODE_MAP.put(131,PinConstant.KEY_CLEAR);//清空
            KEY_CODE_MAP.put(67,PinConstant.CANCLE_KEY);//取消
            KEY_CODE_MAP.put(66,PinConstant.CONFIRM_KEY);//确定
            KEY_CODE_MAP.put(56,PinConstant.KEY_DOT);//.号
        }
    }

    /**
     * 是否快速点击，当返回true时拦截多余点击，避免同一个按键输入两次
     *
     * @return true是 false否
     */
    public static boolean fastClick() {
        if (System.currentTimeMillis() - time < 35) {
            return true;
        } else {
            time = System.currentTimeMillis();
            return false;
        }

    }

    public static String keyCodeChangeStr(int keyCode) {
        int correctKeyCode = correctKeyCode(keyCode);
        if (correctKeyCode <= 9 && correctKeyCode >= 0) {
            return correctKeyCode + "";
        } else {
            return "";
        }
    }

    /**
     * 返回修正后的键位
     *
     * @param keyCode 原始键值
     * @return 返回按键在键盘中的序号，从左到右从上到下
     */
    public static int correctKeyCode(int keyCode) {
        if(KEY_CODE_MAP.containsKey(keyCode)){
            return KEY_CODE_MAP.get(keyCode);
        }else{
            return -1;
        }
    }
}
