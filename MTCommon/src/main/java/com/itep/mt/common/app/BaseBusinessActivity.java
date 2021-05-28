package com.itep.mt.common.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itep.mt.common.R;
import com.itep.mt.common.constant.CmdConstant;
import com.itep.mt.common.constant.version.GenericConstant;
import com.itep.mt.common.dto.BaseDTO;
import com.itep.mt.common.ipc.ActionResponse;
import com.itep.mt.common.sys.SysConf;
import com.itep.mt.common.sys.SysVoice;
import com.itep.mt.common.util.DataStore;
import com.itep.mt.common.util.KeyCodeUtil;
import com.itep.mt.common.util.ReflectionUtils;

import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 所有应用程序的Activity应该都要继承的基类
 * <p>
 * 将倒计时模块，头部logo，title的加载和语音初始化模块超抽取
 */
public abstract class BaseBusinessActivity<T> extends BaseActivity implements View.OnClickListener, View.OnKeyListener {

    private final int DEFAULT_TIME_OUT = 30;//默认超时时间

    private ImageView iv_logo;//logo

    protected TextView tv_time;//顶部倒计时

    protected ImageView iv_time_icon; //倒计时图片

    protected TextView tv_top_title;//顶部信息

    protected TimeoutTimer timeoutTimer;

    protected boolean isMainTimeOut = false; // 主屏是否超时

    protected String msgType; //消息类型

    protected int timeOut;//倒计时

    protected boolean destroyStopVoice = true;//销毁时是否停止语音 默认TURE

    /**
     * 请求对象
     */
    protected T reqDto;

    /**
     * 数据存储对象
     */
    protected DataStore dataStore = new DataStore();
    /**
     * 是否打开键盘灯
     */
    private boolean isOpenKeyLight;
    private long currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createContentView();
        initBusValue();
        initBusView();
        initValue();
        initView();
        playVoice();
        createCountdown();
    }

    /**
     * 创建上下文视图
     */
    protected void createContentView() {
        setContentView(getContentView());
    }


    /**
     * 初始化业务视图
     */
    protected void initBusView() {
        View timeView = findViewById(R.id.tv_time_out);
        View timePicView = findViewById(R.id.tv_time_icon);
        if (timeView != null) {
            tv_time = (TextView) timeView;
            iv_time_icon = (ImageView) timePicView;
            // 从配置文件中获取是否需要显示顶部时间，如果为0就是显示。否则隐藏
            if (SysConf.getTitle_time() > 0) {
                tv_time.setVisibility(View.INVISIBLE);
                iv_time_icon.setVisibility(View.INVISIBLE);
            }
        }

        View logoView = findViewById(R.id.iv_logo);
        if (logoView != null) {
            iv_logo = (ImageView) logoView;
        }
        View titleView = findViewById(R.id.tv_top_title);
        if (titleView != null) {
            tv_top_title = (TextView) titleView;
        }
        //获取logo目录中logo图片，如果logoBm不为null则加载logo图片
        Bitmap logoBm = BitmapFactory.decodeFile(com.itep.mt.common.constant.AppConstant.PATH_LOGO);
        if (logoBm != null && iv_logo != null) {
            iv_logo.setImageBitmap(logoBm);
            //iv_logo.setBackground(null);
        }
    }

    /**
     * 初始化业务值
     */
    protected void initBusValue() {
        Class clazz = ReflectionUtils.getSuperClassGenricType(this.getClass());
        reqDto = (T) getIntent().getExtras().getSerializable(clazz.getSimpleName());
        if (reqDto instanceof BaseDTO) {
            BaseDTO baseDTO = (BaseDTO) reqDto;
            msgType = baseDTO.getMsgType();
        }
    }

    /**
     * 获取layout
     *
     * @return
     */
    protected abstract int getContentView();


    /**
     * 初始化值
     */
    protected abstract void initValue();

    /**
     * 初始化视图
     */
    protected abstract void initView();

    /**
     * 获取操作超时返回值
     *
     * @return
     */
    protected abstract byte[] getTimeOutRet();

    /**
     * 播放语音
     */
    protected abstract void playVoice();

    /**
     * 超时回调
     */
    protected void onTimeOut() {
        byte[] timeOutRet = getTimeOutRet();
        if (timeOutRet != null) {
            replyByParam(timeOutRet);
        }
        finish();
    }

    /**
     * 播放超时语音
     */
    protected void playTimeOutVoice() {
        SysVoice.playTTS(getString(R.string.time_out));
    }


    /**
     * 创建计时器
     */
    protected void createCountdown() {
        if (tv_time != null) {
            if (timeOut <= 0) {
                timeOut = DEFAULT_TIME_OUT;
            }
            startCountdown(timeOut + 1);
        }
    }


    /**
     * 开始计时
     *
     * @param time
     */
    protected void startCountdown(int time) {
        stopCountdown();
        timeoutTimer = new TimeoutTimer(time);
        timeoutTimer.start();
    }

    /**
     * 实现倒计时内部类
     */
    public class TimeoutTimer extends CountDownTimer {

        public TimeoutTimer(int millisInFuture) {
            super((long) (millisInFuture * 1000), 1000);
        }

        public void onFinish() {
            isMainTimeOut = true;
            tv_time.setText("0");
            playTimeOutVoice();
            onTimeOut();
        }

        public void onTick(long millisUntilFinished) {
            long time = millisUntilFinished / 1000;
            currentTime = time;
            tv_time.setText(String.valueOf(time));
        }
    }

    /**
     * 获取倒计时当前剩余时间
     *
     * @return
     */
    public long getCountTime() {
        return currentTime;
    }

    /**
     * 停止倒计时
     */
    public void stopCountdown() {
        if (timeoutTimer != null) {
            timeoutTimer.cancel();
        }
    }

    /**
     * 最小点击延迟时间/毫秒
     */
    private final int MIN_CLICK_DELAY_TIME = 150;

    /**
     * 上次点击时间
     */
    private long lastClickTime = 0;

    /**
     * 点击事件
     *
     * @param v 控件
     */
    @Override
    public void onClick(View v) {
        if (isMainTimeOut)
            return;
        //避免点击太快
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            SysVoice.playBtnSound();
            onBtnClick(v);
        }
    }

    private Map<Integer,Boolean> keyRecord=new HashMap<Integer,Boolean>();//键盘记录

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (isMainTimeOut) {
            return true;
        }
        if (event.getRepeatCount()>0) {//按钮连发直接返回
            return true;
        }
        keyCode = KeyCodeUtil.correctKeyCode(keyCode);
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            if (keyCode != -1) {
                keyRecord.put(keyCode,true);
            }
            if(keyRecord.size()==1){
                onKeyClick(keyCode);
            }
        }else if(event.getAction() ==KeyEvent.ACTION_UP){
            if(keyRecord.containsKey(keyCode)){//up事件删除key
                keyRecord.remove(keyCode);
            }
        }
        return true;
    }


    /**
     * 键盘点击事件
     *
     * @param keyCode 键码
     */
    protected abstract void onKeyClick(int keyCode);


    /**
     * 按钮点击事件
     *
     * @param v 控件
     */
    protected abstract void onBtnClick(View v);


    /**
     * 销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        //修改到finish执行关闭资源操作
        destroyResource();
        super.finish();
    }

    /**
     * 销毁资源
     */
    protected void destroyResource() {
        stopCountdown();
        //释放语音
        if (!isMainTimeOut && destroyStopVoice) {//判断不为超时停止语音
            SysVoice.stopVoice();
            SysVoice.releaseMedia();
        }
        colseKeyboardLight();
    }


    /**
     * 打开键盘灯
     */
    protected void openKeyboardLight() {
        isOpenKeyLight = SysConf.keyboardLight(1);
    }

    /**
     * 关闭键盘灯
     */
    protected void colseKeyboardLight() {
        if (isOpenKeyLight) {
            SysConf.keyboardLight(0);
        }
    }


    /**
     * 响应
     *
     * @param ret
     */
    protected void response(String paramType, byte[] ret) {
        response(msgType, paramType, ret);
    }

    /**
     * 响应
     *
     * @param msgType
     * @param paramType
     * @param ret
     */
    protected void response(String msgType, String paramType, byte[] ret) {
        ActionResponse res = new ActionResponse(paramType, ret);
        ActionResponseManager.response(msgType, res);
    }


    /**
     * 响应
     *
     * @param ret
     */
    protected void response(String paramType, byte[] ret, boolean isDel) {
        ActionResponse res = new ActionResponse(paramType, ret);
        ActionResponseManager.response(msgType, res, isDel);
    }


    /**
     * 有参数响应
     *
     * @param ret
     */
    protected void replyByParam(byte[] ret) {
        response(GenericConstant.GENERIC_REPLY_PARAM, ret);
    }

    /**
     * 无参数响应
     *
     * @param ret
     */
    protected void replyNoParam(byte[] ret) {
        response(GenericConstant.GENERIC_REPLY, ret);
    }


    /**
     * 有参数返回
     *
     * @param cmd    指令
     * @param params 参数
     * @throws RemoteException
     */
    protected void replyByParam(byte[] cmd, byte[] params) {
        replyByParam(ArrayUtils.addAll(cmd, params));
    }


    /**
     * 返回成功有参数
     *
     * @param params
     * @throws RemoteException
     */
    protected void replyOk(byte[] params) {
        replyByParam(CmdConstant.CMD_OK, params);
    }


}

