package com.umiwi.video.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.umiwi.ui.beans.VoiceBean;
import com.umiwi.ui.beans.updatebeans.SouceBean;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.util.JsonUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;


/**
 * Created by Administrator on 2017/3/7.
 */

public class VoiceService extends Service {
  public static MediaPlayer mediaPlayer;
    private String url;


    public class VoiceBinder extends Binder {

        public void playVoice(String path) {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void pauseVocie() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        }

        public void stopVoice() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }


        public void playOrPause() {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        }


        public int getDuration() {
            return mediaPlayer.getDuration();
        }

        public int getCurrentDuration() {
            return mediaPlayer.getCurrentPosition();
        }

        public MediaPlayer getMediaplayer(){
            return mediaPlayer;
        }

        public void searchInfo(String url){
            OkHttpUtils.get().url(url).build().execute(new CustomStringCallBack() {
                @Override
                public void onFaild() {

                }

                @Override
                public void onSucess(String data) {
                    if (!TextUtils.isEmpty(data)){
                        SouceBean voiceBean = JsonUtil.json2Bean(data, SouceBean.class);
                        if (voiceBean!= null){
                            UmiwiApplication.getInstance().getBinder().playVoice(voiceBean.getSource());
                        }
                    }

                }
            });
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        url = intent.getStringExtra("url");
        return new VoiceBinder();
    }

}
