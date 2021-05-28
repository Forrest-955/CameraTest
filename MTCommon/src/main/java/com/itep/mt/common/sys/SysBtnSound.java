package com.itep.mt.common.sys;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.itep.mt.common.R;
import com.itep.mt.common.util.ContextUtil;
import com.itep.mt.common.util.Logger;

/**
 * 按钮声音类
 */
public class SysBtnSound {

    private static SysBtnSound sysBtnSound = null;       //单一实例
    /**
     * 提示声
     */
    private AudioManager mAudioManager; //声音管理适配

    private int currentVolume;    //当前音量

    private SoundPool soundPool;

    private int btnId;//按键音id

    private boolean isInited = false;       //标记是否初始化完成

    private SysBtnSound(){
       init();
    }

    /**
     * 获得单一实例
     */
    public static SysBtnSound getInstance(){
        if( sysBtnSound == null ){
            sysBtnSound = new SysBtnSound();
        }
        return sysBtnSound;
    }

    /**
     * 初始化
     */
    private void init(){
        mAudioManager = (AudioManager) ContextUtil.getInstance().getSystemService(Context.AUDIO_SERVICE);
        soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
        btnId = soundPool.load(ContextUtil.getInstance(), R.raw.b, 1);
        isInited=true;
    }

    /**
     * 播放
     */
    public void play() {
        if(isInited){
            try {
                currentVolume = SysConf.getBtnVolume();
                mAudioManager.setStreamVolume(AudioManager.STREAM_RING, 15, 0);
                float streamVolumeMax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                float volume = currentVolume / streamVolumeMax;
                soundPool.play(btnId, volume, volume, 1, 0, 1f);
            } catch (Exception e) {
                Logger.i("播放提示音异常!");
            }
        }
    }

    /**
     * 停止语音
     */
    public void stop(){
        if(soundPool!=null){
            soundPool.stop(btnId);
        }
    }


}
