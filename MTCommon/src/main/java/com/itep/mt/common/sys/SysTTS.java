package com.itep.mt.common.sys;

import android.content.Context;
import android.media.AudioManager;

import com.itep.mt.common.util.ContextUtil;
import com.itep.mt.common.util.Logger;
import com.unisound.client.SpeechConstants;
import com.unisound.client.SpeechSynthesizer;
import com.unisound.client.SpeechSynthesizerListener;

import java.io.File;

/**
 * 基于云知声的Text to speech离线模块实现TTS功能
 */
public class SysTTS {

    private static final String app_key = "d7nfqlvreaxbyjwzhvmr4p6taa6izsc5e6yjliqq";           //注册的应用程序ip
    private static final String app_secret = "b171ed12c990bfa0581ad6703ee1df50";                //注册的应用密钥
    private static final String mFrontendModel = "/mnt/internal_sd/mt/tts/OfflineTTSModels/frontend_model"; //声音模型文件
//    private static final String mFrontendModel = "/storage/emulated/0/mt/tts/OfflineTTSModels/frontend_model"; //声音模型文件
    private static final String mBackendModel = "/mnt/internal_sd/mt/tts/OfflineTTSModels/backend_female"; //声音模型文件
//    private static final String mBackendModel = "/storage/emulated/0/mt/tts/OfflineTTSModels/backend_female"; //声音模型文件
    private static final int DEF_VOLUME_LEVEL = 80;                                             //默认音量等级

    private static SysTTS tts = null;       //单一实例
    private SpeechSynthesizer mTTSPlayer;   //TTS服务
    private boolean isInited = false;       //标记是否初始化完成

    private SysTTS() {
        if (checkEnv()) {
            initTts();
        }
    }

    /**
     * 获得单一实例
     */
    public static SysTTS getInstance() {
        if (tts == null) {
            tts = new SysTTS();
        }
        return tts;
    }

    /**
     * 判断本地环境是否满足
     */
    private boolean checkEnv() {
        boolean ret = true;
        File _FrontendModelFile = new File(mFrontendModel);
        if (!_FrontendModelFile.exists()) {
            Logger.e("TTS声音模型文件：" + mFrontendModel + "不存在！");
            ret = false;
        }

        File _BackendModelFile = new File(mBackendModel);
        if (!_BackendModelFile.exists()) {
            Logger.e("TTS声音模型文件：" + mBackendModel + "不存在！");
            ret = false;
        }
        return ret;
    }


    /**
     * 初始化本地离线TTS
     */
    private void initTts() {
        // 初始化语音合成对象
        mTTSPlayer = new SpeechSynthesizer(ContextUtil.getInstance(), app_key, app_secret);
        mTTSPlayer.setOption(SpeechConstants.TTS_SERVICE_MODE, SpeechConstants.TTS_SERVICE_MODE_LOCAL);// 设置本地合成
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_FRONTEND_MODEL_PATH, mFrontendModel);            //设置前端模型
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_BACKEND_MODEL_PATH, mBackendModel);              // 设置后端模型
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_VOICE_VOLUME, DEF_VOLUME_LEVEL);

        // 设置回调监听
        mTTSPlayer.setTTSListener(new SpeechSynthesizerListener() {

            @Override
            public void onEvent(int type) {
                switch (type) {
                    case SpeechConstants.TTS_EVENT_INIT:
                        // 初始化成功回调
                        Logger.i("TTS Callback onInitFinish");
                        isInited = true;
                        break;
                    case SpeechConstants.TTS_EVENT_SYNTHESIZER_START:
                        // 开始合成回调
                        Logger.i("TTS Callback beginSynthesizer");
                        break;
                    case SpeechConstants.TTS_EVENT_SYNTHESIZER_END:
                        // 合成结束回调
                        Logger.i("TTS Callback endSynthesizer");
                        break;
                    case SpeechConstants.TTS_EVENT_BUFFER_BEGIN:
                        // 开始缓存回调
                        Logger.i("TTS Callback beginBuffer");
                        break;
                    case SpeechConstants.TTS_EVENT_BUFFER_READY:
                        // 缓存完毕回调
                        Logger.i("TTS Callback bufferReady");
                        break;
                    case SpeechConstants.TTS_EVENT_PLAYING_START:
                        // 开始播放回调
                        Logger.i("TTS Callback onPlayBegin");
                        break;
                    case SpeechConstants.TTS_EVENT_PLAYING_END:
                        // 播放完成回调
                        Logger.i("TTS Callback onPlayEnd");
                        //setTTSButtonReady();
                        if (mCallBack != null) {
                                mCallBack.handle();
                        }
                        break;
                    case SpeechConstants.TTS_EVENT_PAUSE:
                        // 暂停回调
                        Logger.i("TTS Callback pause");
                        break;
                    case SpeechConstants.TTS_EVENT_RESUME:
                        // 恢复回调
                        Logger.i("TTS Callback resume");
                        break;
                    case SpeechConstants.TTS_EVENT_STOP:
                        // 停止回调
                        Logger.i("TTS Callback stop");
                        break;
                    case SpeechConstants.TTS_EVENT_RELEASE:
                        // 释放资源回调
                        Logger.i("TTS Callback release");
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onError(int type, String errorMSG) {
                // 语音合成错误回调
                Logger.i("onError");
                Logger.e(errorMSG);
            }
        });

        // 初始化合成引擎
        mTTSPlayer.init("");
    }

    /**
     * 引擎是否已被正确初始化
     *
     * @return true表示已初始化
     */
    public boolean isInited() {
        return (mTTSPlayer != null && isInited);
    }


    /**
     * 设置音量大小，
     *
     * @param level 0-100
     */
    public void setVolumeLevel(int level) {
        if (level >= 0 && level <= 100 && isInited()) {
            mTTSPlayer.setOption(SpeechConstants.TTS_KEY_VOICE_VOLUME, level);
            Context context = ContextUtil.getInstance();
            AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int volume = level * maxVolume / 100;
            if (volume == 0) {//如果TTS音量设置为空，则系统音量设置为1，否则按键音无声音
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 1, 0);
            } else {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
            }
        }//else 不处理
    }

    /**
     * 立即播放文件内容
     *
     * @param txt 文本内容
     */
    public void playText(String txt) {
        if (isInited()) {
            //获取当前音量，并将TTS设置为当前音量
            Context context = ContextUtil.getInstance();
            AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int audio_max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            //  int audio_cur = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            //mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audio_max, 0);
            int audio_cur = SysConf.getWorkVolume();
            Logger.i("audio_max=" + audio_max + ",audio_cur=" + audio_cur);
            int level = audio_cur * 100 / audio_max;
            SysTTS.getInstance().setVolumeLevel(level);
            mTTSPlayer.playText(txt);
        }//else 不执行
    }

    /**
     * TTS播放完的回调
     */
    TTSEndCallBack mCallBack;

    public interface TTSEndCallBack {
        public void handle();
    }

    /**
     * 设置播放结束的回调
     *
     * @param mCallBack 回调
     */
    public void setCallBack(TTSEndCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    /**
     * 停止TTS播放
     */
    public void stopTTS() {
        if (mTTSPlayer.isPlaying()) {
            mTTSPlayer.stop();
        }
    }

    /**
     * 是否播放
     */
    public boolean isPlaying(){
        return mTTSPlayer.isPlaying();
    }

}
