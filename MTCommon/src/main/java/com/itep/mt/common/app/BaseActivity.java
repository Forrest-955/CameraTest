package com.itep.mt.common.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.itep.mt.common.sys.SysConf;
import com.itep.mt.common.util.JSYHCommon;
import com.itep.mt.common.util.Logger;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * 所有应用程序的Activity应该都要继承的基类
 */
public class BaseActivity extends Activity {
    private boolean autoFinishOnPause = true;        //当onPause暂停时，是否自动关闭当前界面

    /**
     * 子类调用用于修改自动关闭界面的选项
     */
    protected void noFinishOnPause() {
        this.autoFinishOnPause = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppController.registerActivity(this);//向控制中心报到
        RemoteCmdService.noticeStatusAtRun();
        super.onCreate(savedInstanceState);
        if ("建设银行".equals(SysConf.getAppVersionName())) {
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath(SysConf.get_JSYH_SET_FONT_PATH())
                    .setFontAttrId(com.itep.mt.common.R.attr.fontPath)
                    .build()

            );
            Logger.i("字体路径:" + SysConf.get_JSYH_SET_FONT_PATH());
            JSYHCommon.switchLanguage();
        }
    }

    @Override
    protected void onPause() {
        if ("建设银行".equals(SysConf.getAppVersionName())) {
        }
        if (!isFinishing() && autoFinishOnPause) {
            this.finish();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        RemoteCmdService.noticeStatusAtStop();//暂停
        AppController.unregisterActivity(this);//退出控制中心
        super.onDestroy();
    }

    //消息类型定义
    public static final int MSG_CLOSE = 0;        //关闭的消息，无参数
    public static final int MSG_DATA = 1;        //发送数据


    //处理来自外部，一般是通过AppController的消息。成员不需要声明为static，由于短暂持有，不会造成内存泄露
    private Handler ctrlHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CLOSE:
                    BaseActivity.this.finish();
                    break;
                case MSG_DATA:
                    BaseActivity.this.handleDataMessage(msg);
                    break;
            }
            super.handleMessage(msg);
        }

    };

    /**
     * 处理来自外部的发送数据的消息。所有显示后，仍然需要接收来自外部的数据参数的子类都应该实现该函数。
     *
     * @param msg 消息。注意：不应该长时间持有该对象，否会会造成内存泄露
     */
    protected void handleDataMessage(Message msg) {

    }

    /**
     * 向内部handler挂载通知消息
     *
     * @param msg_type 消息类型
     */
    public void postNoticeMessage(int msg_type) {
        ctrlHandler.sendEmptyMessage(msg_type);
    }

    /**
     * 向内部handler挂载通知消息
     *
     * @param msg_type   消息类型
     * @param param_type 参数的类型
     * @param param      消息的参数
     */
    public void postNoticeMessage(int msg_type, int param_type, Object param) {
        Message msg = ctrlHandler.obtainMessage(msg_type, param_type, 0, param);
        ctrlHandler.sendMessage(msg);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
