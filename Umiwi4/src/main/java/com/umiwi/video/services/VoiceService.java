package com.umiwi.video.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.SeekBar;

import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;
import com.umiwi.video.control.IMusicPlayerService;

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

    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        url = intent.getStringExtra("url");
        Log.e("service", "service被绑定...");
        return new VoiceBinder();
    }

}
