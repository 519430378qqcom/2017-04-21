package com.umiwi.video.recorder;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Administrator on 2017/3/9.
 */

public class AudioManager {

    private MediaRecorder mediaRecorder;
    private String mDir;
    private String mCurrentFilePath;

    private static AudioManager mInstance;
    private boolean isPrePared;
    private  boolean isRelease;
    private AudioManager(String dir) {
       mDir = dir;
    }

    public String  getCurrentFilePath() {
        return mCurrentFilePath;
    }
    public boolean  getIsRelease() {
        return isRelease;
    }
    public interface AudioStateListener {
        void wellPrepared();
    }

    public AudioStateListener mListener;

    public void setOnAudioStateListener(AudioStateListener listener) {
        mListener = listener;
    }

    public static AudioManager getmInstance(String dir) {
        if (mInstance == null) {
            synchronized (AudioManager.class) {
                if (mInstance == null) {
                    mInstance = new AudioManager(dir);
                }
            }
        }
        return mInstance;

    }

    public void prepareAudio() {

        try {
            Log.e("audio","开始准备");
            isPrePared = false;
            File dir = new File(mDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = getVoiceFileName();
            File file = new File(dir, fileName);
            mCurrentFilePath = file.getAbsolutePath();
            if (mediaRecorder == null){
                mediaRecorder = new MediaRecorder();
            }
            //设置输出文件的路径
            mediaRecorder.setOutputFile(file.getAbsolutePath());
            Log.e("getAbsolutePath",file.getAbsolutePath());
            //设置midiarecodrder 音频为麦克风
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //这是音频的格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            //设置音频的编码为amr
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();
            //准备结束
            isPrePared = true;
            isRelease = false;

            Log.e("audio","准备结束");
            if (mListener != null) {
                Log.e("audio","jianting");

                mListener.wellPrepared();
            }
        } catch (IOException e) {
            Log.e("error","e :" +e);
            e.printStackTrace();
        }
    }

    /**
     * 随机生成音频文件路径
     *
     * @return
     */
    private String getVoiceFileName() {
        return UUID.randomUUID().toString() + ".mp3";
    }

    public int getVoiceLevel(int maxLevel) {
        if (isPrePared) {
            try {
                return maxLevel * mediaRecorder.getMaxAmplitude() / 32768 + 1;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        return 1;
    }

    public void release() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        isRelease = true;
    }

    public void cancle() {
        release();
        if (mCurrentFilePath != null) {
            File file = new File(mCurrentFilePath);
            file.delete();
            mCurrentFilePath = null;
        }

    }

    public void deleteFile(){
        if (mCurrentFilePath != null) {
            File file = new File(mCurrentFilePath);
            file.delete();
            mCurrentFilePath = null;
        }
    }
}
