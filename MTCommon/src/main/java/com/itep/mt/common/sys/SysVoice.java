package com.itep.mt.common.sys;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;

import com.itep.mt.common.constant.AppConstant;
import com.itep.mt.common.util.ContextUtil;

/**
 * 系统语音总类
 */
public class SysVoice {

    /**
     * 初始化音频
     */
    public static void initVoice(){
        SysTTS.getInstance();
        SysBtnSound.getInstance();
    }

    /**
     * 播放TTS语音
     * @param txt
     */
    public static void playTTS(String txt){
        stopVoice();
        SysTTS.getInstance().setCallBack(null);
        SysTTS.getInstance().playText(txt);
    }


//    /**
//     * 播放TTS语音
//     * @param txt
//     */
//    public static void playTTS(String txt, SysTTS.TTSEndCallBack ttsEndCallBack){
//        stopVoice();
//        SysTTS.getInstance().playText(txt);
//        SysTTS.getInstance().setCallBack(ttsEndCallBack);
//    }


    /**
     * 播放按键声
     */
    public static void playBtnSound(){
        //播放按键声 //停止所有语音
        stopVoice();
        SysBtnSound.getInstance().play();
    }

    /**
     * 播放媒体音乐
     * @param assetPath  路径
     */
    public static void playAssetMedia(String assetPath){
        stopVoice();
    }

    /**
     * 播放媒体音乐
     * @param filePath 路径
     */
    public static void playMedia(String filePath){
        stopVoice();
    }


    /**
     * 播放媒体音乐
     * @param fileDescriptor  路径
     */
    public static void playMedia(AssetFileDescriptor fileDescriptor){
        stopVoice();
    }

    /**
     * 停止所有音频
     */
    public static void stopVoice(){
        //停止语音  先排除按钮声
        SysTTS.getInstance().stopTTS();
        //SysBtnSound.getInstance().stop();
    }

    /**
     *释放资源
     */
    public static void releaseAll(){
        //SysMedia.getInstance().releaseMedia();
    }

    /**
     * 释放音频资源
     */
    public static void releaseMedia(){
        //SysMedia.getInstance().releaseMedia();
    }


    /**
     * 恢复媒体音量
     */
    public static void resetSysVolume(){
        Context context = ContextUtil.getInstance();
        AudioManager mAudioManager = (AudioManager) context. getSystemService(Context.AUDIO_SERVICE);
        int audioMax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioMax, 0);
    }


    /**
     * 播放音频
     * @param voiceType 音频类型
     * @param voiceStr  音频内容，包括语音或路径地址
     * @param defVoiceStr 默认播放文本
     */
    public static void playVoice(int voiceType,String voiceStr,String defVoiceStr){
        switch (voiceType) {
            case 0:
                break;
            case 1:
                SysVoice.playTTS(defVoiceStr);
                break;
            case 2:
                SysVoice.playTTS(voiceStr);
                break;
            case 3:
                SysVoice.playMedia(AppConstant.PATH_SOUND_ROOT+voiceStr);
                break;
        }
    }


}
