package com.umiwi.video.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.umiwi.ui.IVoiceService;

import java.io.IOException;


/**
 * Created by Administrator on 2017/3/7.
 */

public class VoiceService extends Service {


    public static String url;
    /**
     * 打开完成的信息
     */
    public static final String OPEN_COMPLETION = "com.atguigu.mediaplay.OPEN_COMPLETION";
    /**
     * 播放音乐，
     */
    private MediaPlayer mediaPlayer;

    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer mp) {
            play();
            notifyChange(OPEN_COMPLETION);

        }
    };

    private void notifyChange(String action) {
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            next();
            return true;
        }
    };
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG", "voiceService + onCreate()");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        url =  intent.getStringExtra("url");
//        Log.e("TAG", "url123=" + url);
        Log.e("TAG", "onBind()");
        return myBinder;
    }

    /**
     * 代理VoiceService
     */
    private IVoiceService.Stub myBinder = new IVoiceService.Stub() {
        VoiceService service = VoiceService.this;


        @Override
        public void play() throws RemoteException {
            service.play();

        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public void openAudio(String url) throws RemoteException {
            service.openAudio(url);
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public String getAudioName() throws RemoteException {
            return null;
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return service.isPlaying();
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return service.getAudioPath();
        }

        @Override
        public void next() throws RemoteException {

        }

        @Override
        public void pre() throws RemoteException {

        }

        @Override
        public void seekTo(int position) throws RemoteException {
            service.seekTo(position);
        }


    };

    /**
     * 音频播放
     */
    private void play() {
        mediaPlayer.start();
    }

    /**
     * 音频暂定
     */
    private void pause() {
        mediaPlayer.pause();

    }


    /**
     * 根据URL 播放音乐
     *
     * @param url
     */
    private void openAudio(String url) {
        this.url = url;
        Log.e("TAG", "服务里传递的URL=" + url);
        if (mediaPlayer != null) {
            //把上一个音频资源释放
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
        //设置准备，错误，播放完成的监听
        mediaPlayer.setOnPreparedListener(mOnPreparedListener);
        mediaPlayer.setOnErrorListener(mOnErrorListener);
        mediaPlayer.setOnCompletionListener(mOnCompletionListener);


        //设置播放地址
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 得到音频的总时长
     *
     * @return
     */
    private int getDuration() {
        if(mediaPlayer != null) {
            return  mediaPlayer.getDuration();
        }
        return 0;
    }

    /**
     * 得到音频播放的当前进度
     *
     * @return
     */
    private int getCurrentPosition() {
        if(mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    /**
     * 得到音频名称
     *
     * @return
     */
    private String getAudioName() {
        return "";
    }

    /**
     * 是否在播放
     *
     * @return
     */
    private boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    /**
     * 得到播放地址
     *
     * @return
     */
    private String getAudioPath() {
        if(url != null) {
            return url;
        }
        return "";
    }

    /**
     * 播放下一个
     */
    private void next() {

    }

    /**
     * 播放上一个
     */
    private void pre() {

    }

    /**
     * 根据具体的位置拖动音乐继续播放
     */
    private void seekTo(int position){
        if(mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("TAG", "onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        if(mediaPlayer !=null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
        Log.e("TAG", "voiceService + onDestory()");
    }
    //  public static MediaPlayer mediaPlayer;
//    private String url;
//
//
//    public class VoiceBinder extends Binder {
//
//        public void playVoice(String path) {
//            if (mediaPlayer == null) {
//                mediaPlayer = new MediaPlayer();
//            }
//            mediaPlayer.reset();
//            try {
//                mediaPlayer.setDataSource(path);
//                mediaPlayer.prepare();
//                mediaPlayer.start();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        public void pauseVocie() {
//            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                mediaPlayer.pause();
//            }
//        }
//
//        public void stopVoice() {
//            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                mediaPlayer.stop();
//                mediaPlayer.release();
//                mediaPlayer = null;
//            }
//        }
//
//
//        public void playOrPause() {
//            if (mediaPlayer.isPlaying()) {
//                mediaPlayer.pause();
//            } else {
//                mediaPlayer.start();
//            }
//        }
//
//
//        public int getDuration() {
//            return mediaPlayer.getDuration();
//        }
//
//        public int getCurrentDuration() {
//            return mediaPlayer.getCurrentPosition();
//        }
//
//        public MediaPlayer getMediaplayer(){
//            return mediaPlayer;
//        }
//
//        public void searchInfo(String url){
//            OkHttpUtils.get().url(url).build().execute(new CustomStringCallBack() {
//                @Override
//                public void onFaild() {
//
//                }
//
//                @Override
//                public void onSucess(String data) {
//                    if (!TextUtils.isEmpty(data)){
//                        SouceBean voiceBean = JsonUtil.json2Bean(data, SouceBean.class);
//                        if (voiceBean!= null){
//                            UmiwiApplication.getInstance().getBinder().playVoice(voiceBean.getSource());
//                        }
//                    }
//
//                }
//            });
//        }
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        url = intent.getStringExtra("url");
//        return new VoiceBinder();
//    }

}
