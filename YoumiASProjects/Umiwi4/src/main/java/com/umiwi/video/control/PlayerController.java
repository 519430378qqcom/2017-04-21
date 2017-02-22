package com.umiwi.video.control;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.umiwi.media.Marker;
import com.umiwi.ui.R;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.VideoManager;
import com.umiwi.ui.model.VideoModel;
import com.umiwi.ui.util.EncryptTools;
import com.umiwi.video.application.Settings;
import com.umiwi.video.widget.YoumiMediaController;
import com.umiwi.video.widget.YoumiVideoView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cn.youmi.account.manager.UserManager;
import cn.youmi.framework.util.NetworkManager;
import cn.youmi.framework.util.SharePreferenceUtil;
import cn.youmi.framework.util.ToastU;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayerController {

    private String TAG = "PlayerController";

    public class PlayerItem {
        public PlayerItem() {
            codecStatus = CodecStatus.INITIALIZED;
        }

        private Marker mMarker = new Marker();
        VideoModel dataSource;
        private long startTime;
//		private long endTime;

        public VideoModel getDataSource() {
            return dataSource;
        }

        CodecStatus codecStatus;

        boolean isLocalResource() {
            return true;// TODO
        }

        private long lastPosition = 0;
    }

    private enum CodecStatus {
        INITIALIZED(-10), DECODING(10), DECODED(20), ENCODING(30), ENCODED(40);

        CodecStatus(int value) {
            _value = value;
        }

        private int _value;

        @SuppressWarnings("unused")
        public int getIntValue() {
            return _value;
        }
    }


    /**
     * 开始打点纪录
     */
    public void mark() {
        if (mCurrentPlayerItem != null) {
            int position = getCurrentPosition();
            if (position != 0) {
                mCurrentPlayerItem.mMarker.markPercent((int) (getCurrentPosition() * 1.0 / mVideoView.getDuration() * 100));
            }
            if (position != 0 && position > mCurrentPlayerItem.lastPosition) {
                mCurrentPlayerItem.lastPosition = position;
            }
        }
//        Log.e(TAG, "mark() returned: " + mCurrentPlayerItem.lastPosition);
    }

    private static PlayerController _instance;

    public static PlayerController getInstance() {
        if (_instance == null) {
            _instance = new PlayerController();
        }
        return _instance;
    }


    public PlayerItem getCurrentPlayerItem() {
        return mCurrentPlayerItem;
    }

    public PlayerItem getItemToPlay() {
        return mItemToPlay;
    }

    // -------------------
    private PlayerItem mCurrentPlayerItem;
    private PlayerItem mLastPlayerItem;
    private PlayerItem mItemToPlay;
    private ArrayList<VideoModel> playList;

    public ArrayList<VideoModel> getPlayList() {
        return playList;
    }

    private YoumiVideoView mVideoView;
    private YoumiMediaController mMediaController;

    private Context mContext;

    private OkHttpClient okHttpClien;

    private PlayerController() {
//        createPlayer();
    }

    boolean isOnlyFullScree = false;

    public void setPlayerFuller() {
        isOnlyFullScree = true;
    }

    public void goSmallScreen() {
        if (mVideoView == null) {
            return;
        }
        mVideoView.goSmallScreen();
    }

    public void setDisableOrientation() {
        mVideoView.setDisableOrientation();
    }

    public void setEnableOrientation() {
        mVideoView.setEnableOrientation();
    }

    public void initPlayerController(Context context) {
        this.mContext = context;
        okHttpClien = new OkHttpClient();

        uploadRecord();

        initPlayerView();

        mSpUtil = UmiwiApplication.getInstance().getSpUtil();
    }

    public void addPlayerViewToWindows(@Nullable ViewGroup window) {
        if (mVideoView == null) {
            return;
        }
        ViewGroup parent = (ViewGroup) mVideoView.getParent();
        if (parent != null) {
            parent.removeView(mVideoView);
        }
        window.addView(mVideoView, 0);
    }


    public void removePlayerViewToWindows(ViewGroup window) {
        isOnlyFullScree = false;
//        ViewGroup parent = (ViewGroup) mVideoView.getParent();
//        if (parent != null) {
//            parent.removeView(mVideoView);
//        }
//        window.removeView(mVideoView);
    }

    private void initPlayerView() {
        mMediaController = new YoumiMediaController(mContext);
        mVideoView = (YoumiVideoView) LayoutInflater.from(mContext).inflate(R.layout.player_view, null);

        if (isOnlyFullScree) {
            mMediaController.setOnlyFullScreen(true);
        }

        mVideoView.setSmallScreenLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        mVideoView.setFullScreenLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        mVideoView.setMediaController(mMediaController);
        if (isOnlyFullScree) {
            mVideoView.setDisableOrientation();
            mVideoView.goFullScreen();
        }
        mVideoView.setOnPauseListener(videoPauseListener);
        mVideoView.setOnBufferingUpdateListener(mBufferingUpdateListener);
        mVideoView.setOnPreparedListener(mPreparedListener);
        mVideoView.setOnReStartListener(videoReStartListener);
        mVideoView.setOnInfoListener(mInfoListener);

    }

    private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer mp, int percent) {
            mark();
        }
    };

    private IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            seekToLastPostion();
        }
    };

    private IMediaPlayerObserver.onPauseListener videoPauseListener = new IMediaPlayerObserver.onPauseListener() {
        @Override
        public void onPause() {
            if (mVideoView == null) {
                return;
            }
            if (mLastPlayerItem == null && mLastPlayerItem.dataSource == null) {
                return;
            }
            mLastPlayerItem.dataSource.setLastwatchposition(mVideoView.getCurrentPosition());
//            uploadRecord();//上传纪录

        }
    };

    private IMediaPlayerObserver.onReStartListener videoReStartListener = new IMediaPlayerObserver.onReStartListener() {
        @Override
        public void onReStart() {
            try {
                mLastPlayerItem.startTime = System.currentTimeMillis();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    };

    long bufferTimeStart;
    long bufferTimeEnd;
    long bufferTime;

    private IMediaPlayer.OnInfoListener mInfoListener =
            new IMediaPlayer.OnInfoListener() {
                public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
                    switch (arg1) {
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                            bufferTimeStart = System.currentTimeMillis();
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                            bufferTimeEnd = System.currentTimeMillis();
                            bufferTime += (bufferTimeEnd - bufferTimeStart);
                            break;
                    }
                    return true;
                }
            };

    private void seekToPausePostion() {
        if (mVideoView == null) {
            return;
        }
        if (mCurrentPlayerItem == null && mCurrentPlayerItem.dataSource == null) {
            return;
        }
        mVideoView.seekTo(mCurrentPlayerItem.dataSource.getLastwatchposition());
    }

    private void seekToLastPostion() {
        if (mVideoView == null) {
            return;
        }
        if (mCurrentPlayerItem == null) {
            return;
        }
        if (mCurrentPlayerItem.dataSource == null) {
            return;
        }
        int lastPostion = mCurrentPlayerItem.dataSource.getLastwatchposition();
        if (lastPostion > 0 && mVideoView.getDuration() != 0) {
            if (mVideoView.getDuration() - lastPostion < 30 * 1000) {
                lastPostion = 0;
            }
            lastPostion = lastPostion - 30 * 1000;
        }
        if (lastPostion > 0) {
            mVideoView.seekTo(lastPostion);
        }
//        mCurrentPlayerItem.startTime = System.currentTimeMillis();
    }

    /**
     * 单独初始化
     */
    public void initNativeMediaPlayer() {
        // init player
        Settings settings = new Settings(this.mContext);
        switch (settings.getPlayer()) {
            case Settings.PV_PLAYER__IjkMediaPlayer:
                IjkMediaPlayer.loadLibrariesOnce(null);
                IjkMediaPlayer.native_profileBegin("libijkplayer.so");
                break;
//            case Settings.PV_PLAYER__IjkExoMediaPlayer:
//                break;
            case Settings.PV_PLAYER__AndroidMediaPlayer:
            default:
                break;
        }
    }

    public void initConfigrationChange(Configuration configuration) {
        if (null == mVideoView) {
            return;
        }
        if (null == configuration) {
            mVideoView.onConfigrationChange();
            return;
        }
        if (isOnlyFullScree) {
            mVideoView.changeConfiguration(true);
        } else {
            mVideoView.changeConfiguration(configuration);
        }

    }

    public void releaseAndStop() {
        if (mVideoView == null) {
            return;
        }
        if (!mVideoView.isBackgroundPlayEnabled()) {
            mVideoView.stopPlayback();
            mVideoView.release(true);
            mVideoView.stopBackgroundPlay();
        } else {
            mVideoView.enterBackground();
        }
    }

    public void registerAllListener() {
        registerHomeWatcherListener();
        registerPhoneListener();
        registerLockScreenListener();

    }

    public void unregisterAllListener() {
        unregisterHomeWatcherListener();
        unregisterPhoneListener();
        unregisterLockScreenListener();
    }

    public void releaseNativeMediaPlayer() {
        Settings settings = new Settings(mContext);
        if (settings.getPlayer() == Settings.PV_PLAYER__IjkMediaPlayer) {
            IjkMediaPlayer.native_profileEnd();
        }
        mLastPlayerItem = null;
        mCurrentPlayerItem = null;
        mItemToPlay = null;
        mVideoView = null;
        isOnlyFullScree = false;
    }

    public void setRightPanelLayout(View rightPanelLayout) {
        mMediaController.setRightPanelLayout(rightPanelLayout);
    }

    public void setCustomPanelContainer(View customPanelContainer) {
        mMediaController.setCustomPanelContainer(customPanelContainer);
    }

    public void hideCustomPanel() {
        if (mMediaController.isShowCustomPanel()) {
            mMediaController.hideCustomPanel();
        }
    }

    public void hideEndPanel() {
        if (mMediaController.isShowEndPanel()) {
            mMediaController.hideEndPanel();
        }
    }

    public void showPrepareLoading() {
        mMediaController.showLoading();
    }

    public void showCustomPanel() {
        mMediaController.showCustomPanel();
    }

    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener l) {
        if (mVideoView == null) {
            return;
        }
        mVideoView.setOnCompletionListener(l);
    }

    public void setScreenAndControllerHide(IMediaPlayerController.screenAndControllerHide listener) {
        mMediaController.setScreenAndControllerHideListener(listener);
    }

    public void pause() {
        if (mVideoView != null && mVideoView.isPlaying())
            mVideoView.onPause();
    }

    public void resume() {
        if (mVideoView != null && !mVideoView.isPlaying())
            mVideoView.onStart();
    }

    public void setPlayList(ArrayList<VideoModel> playList) {
        this.playList = playList;
    }


    public void setDataSource(VideoModel video) {
        mLastPlayerItem = mCurrentPlayerItem;

        mItemToPlay = new PlayerItem();
        mItemToPlay.dataSource = video;
        mItemToPlay.codecStatus = CodecStatus.INITIALIZED;
//        mItemToPlay.startTime = System.currentTimeMillis();
        // setCurrentPlayStatus(PlayStatus.INITIALIZED);
    }


    public void releasePlayer() {
        mVideoView.suspend();
    }

    public void onComplete() {
        if (mVideoView == null) {
            return;
        }
        mVideoView.onComplete();
//        releaseAndStop();
    }

    private void uploadRecord() {
        String uid = UserManager.getInstance().getUid();
        if (uid != null && UserManager.getInstance().isLogin() && mLastPlayerItem != null && !mLastPlayerItem.getDataSource().isTry()) {
            mLastPlayerItem.mMarker.setVideo(mLastPlayerItem.getDataSource());

//            Log.e(TAG, "uploadRecord() returned: " + mLastPlayerItem.mMarker.isMarked());
            long pos = mLastPlayerItem.lastPosition;
            if (mCurrentPlayerItem != null && mCurrentPlayerItem.lastPosition > pos) {
                pos = mCurrentPlayerItem.lastPosition;
            }
            if (mLastPlayerItem.mMarker.isMarked()) {
                mLastPlayerItem.mMarker.saveAndUpload(uid, mLastPlayerItem.startTime, System.currentTimeMillis(), bufferTime, pos / 1000);
                bufferTime = 0;
                mLastPlayerItem.startTime = 0;
                mLastPlayerItem.mMarker.setMarked(false);
            }
        }
    }

    /**
     * 解密视频
     */
    private void videoEncrypt() {
        if (mVideoView == null) {
            return;
        }
        if (mCurrentPlayerItem != null) {
            mCurrentPlayerItem.codecStatus = CodecStatus.ENCODING;
            EncryptTools.encriptVideoFile(mCurrentPlayerItem.dataSource);
            mCurrentPlayerItem.codecStatus = CodecStatus.ENCODED;
            VideoManager.getInstance().saveVideo(mCurrentPlayerItem.dataSource);
        }
    }


    public static boolean supportMultipleScreen() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }


    public void cancelOkHttpClien() {
        okHttpClien.cancel(null);
    }

    public void prepareAndStart() {
        if (mItemToPlay == null) {
            return;// TODO
        }
        mVideoView.stopPlayback();
        mVideoView.release(true);
        mVideoView.enterBackground();

        mVideoView.requestLayout();
        mVideoView.invalidate();

//        mVideoView//TODO videoplay need replace init
        mCurrentPlayerItem = mItemToPlay;
        mLastPlayerItem = mCurrentPlayerItem;

        mMediaController.setVideoTitle(mCurrentPlayerItem.dataSource.getTitle());
        mMediaController.setReturnIcon(R.drawable.ic_action_bar_return);
        try {
            String url = null;
            if (mCurrentPlayerItem.dataSource.isLocalFileValid()) {
                mCurrentPlayerItem.codecStatus = CodecStatus.DECODING;
                EncryptTools.decodeVideoFile(mCurrentPlayerItem.dataSource);
                mCurrentPlayerItem.codecStatus = CodecStatus.DECODED;
                url = mCurrentPlayerItem.dataSource.getFilePath();//本地
                mVideoView.setVideoPath(url);
                mVideoView.onStart();
                return;
            } else {
                url = mCurrentPlayerItem.dataSource.getVideoUrl();//在线
            }
            if (url != null) {
                startResolveRealUrl(url);
            } else {
                Toast.makeText(UmiwiApplication.getContext(), "视频参数错误，请重试", Toast.LENGTH_SHORT).show();
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    private void startResolveRealUrl(final String url) {
        okHttpClien.setConnectTimeout(10, TimeUnit.SECONDS);
        okHttpClien.setReadTimeout(10, TimeUnit.SECONDS);
        okHttpClien.setWriteTimeout(10, TimeUnit.SECONDS);
        okHttpClien.setFollowRedirects(true);
        okHttpClien.setFollowSslRedirects(true);
        okHttpClien.setRetryOnConnectionFailure(true);

        Request.Builder okHttpRequestBuilder = new Request.Builder();
        okHttpRequestBuilder.url(url);
        okHttpRequestBuilder.addHeader("User-Agent", System.getProperty("http.agent"));
        Request okHttpRequest = okHttpRequestBuilder.build();

        okHttpClien.newCall(okHttpRequest).enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(final Request request, IOException e) {
//                e.printStackTrace();
                handler.post(new Runnable() {
                    @Override
                    public void run() {//TODO
//                        Toast.makeText(mContext, "获取播放链接失败,请刷新重试", Toast.LENGTH_LONG).show();
                        startResolveRealUrl(url);
                    }
                });
//                Log.e("vedeo_url",request.httpUrl().url().toString());
//                request.newBuilder().get();
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String realUrl = response.request().httpUrl().toString();
                    ((Activity) mContext).runOnUiThread(new Runnable() {//TODO getActivity() is null
                        @Override
                        public void run() {
                            if (mVideoView != null) {
                                mVideoView.setVideoURI(Uri.parse(realUrl));
                                mVideoView.onStart();
                            }
                        }
                    });

                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
//                            if (502 == response.code()) {
//                                Log.e("course", "502");
//                            }
                            startResolveRealUrl(url);
//                            Log.e("course", response.code()+"");
//                            Toast.makeText(mContext, "获取播放链接失败,请刷新重试", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    Handler handler = new Handler();


    public void stop() {
        if (mLastPlayerItem != null) {
            if (mLastPlayerItem.startTime != 0) {
                mLastPlayerItem = mCurrentPlayerItem;
                uploadRecord();
            }
        }
    }


    public int getCurrentPosition() {
        if (mVideoView == null) {
            return 0;
        }
        return mVideoView.getCurrentPosition();
    }

    public boolean isPlayNext() {
        if (playList == null) {
            return false;
        }
        if (mCurrentPlayerItem == null) {
            return false;
        }
        if (mCurrentPlayerItem.dataSource == null) {
            return false;
        }
        int index = playList.indexOf(mCurrentPlayerItem.dataSource);

        if (nextIndex(index)) {
            return true;
        }

//        if (index >= 0 && index <= playList.size() - 2) {
//            VideoModel nextVideo = playList.get(index + 1);
//            if (!NetworkManager.getInstance().isWifi() && !nextVideo.isLocalFileValid()) {
//                return true;
//            }
//            setDataSource(nextVideo);
//            return true;
//        }

        return false;
    }

    private SharePreferenceUtil mSpUtil;

    private boolean nextIndex(int index) {
        if (index >= 0 && index <= playList.size() - 2) {
            VideoModel nextVideo = playList.get(index + 1);

            if (!nextVideo.isLocalFileValid()) {
                if (!NetworkManager.getInstance().isWifi()) {
                    if (!NetworkManager.getInstance().isWapNetwork()) {
                        index = index + 1;
                        ToastU.showShort(UmiwiApplication.getContext(), "无网络，尝试搜索本地视频");
                        return nextIndex(index);
                    } else {
                        if (!mSpUtil.getPlayWith3G()) {//不允许3g
                            index = index + 1;
                            ToastU.showShort(UmiwiApplication.getContext(), "正在使用3G网络，尝试搜索本地视频");
                            return nextIndex(index);
                        }
                    }
                }
            }
            VideoModel playVideo = playList.get(index + 1);
            setDataSource(playVideo);
            return true;
        }
        return false;
    }


    public void playNext() {
        if (playList != null) {
            int index = playList.indexOf(mCurrentPlayerItem.dataSource);
            if (index >= 0 && index <= playList.size() - 2) {
                VideoModel nextVideo = playList.get(index + 1);
                setDataSource(nextVideo);
            }
        }
    }

    public void registerPhoneListener() {
        try {
            IntentFilter filter = new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
            mContext.registerReceiver(mPhoneCallReceiver, filter);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void unregisterPhoneListener() {
        try {
            mContext.unregisterReceiver(mPhoneCallReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听来电
     */
    private BroadcastReceiver mPhoneCallReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mVideoView == null || !mVideoView.isPlaying()) {
                return;
            }
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        }

        final PhoneStateListener listener = new PhoneStateListener() {

            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                if (mVideoView == null) {
                    return;
                }
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING://铃响
                        if (mVideoView.isPlaying()) {
                            mVideoView.onPause();
                            // 存储纪录上传纪录
                            //videoPauseListener
                        }
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK://接听
                        if (mVideoView.isPlaying()) {
                            mVideoView.onPause();
                            // 存储纪录上传纪录
                            //videoPauseListener
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE://挂机
                        if (!mVideoView.isPlaying()) {
                            mVideoView.onStart();
                            // 从暂停处开始播放
                            seekToPausePostion();
                        }
                        break;
                }
            }
        };
    };

    public void registerLockScreenListener() {
        try {
            PowerManager manager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
//        Log.d(TAG, "registerLockScreenListener() returned: " + manager.isWakeLockLevelSupported());
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            mContext.registerReceiver(mLockScreenReceiver, filter);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void unregisterLockScreenListener() {
        try {
            mContext.unregisterReceiver(mLockScreenReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 锁屏监听
     */
    private BroadcastReceiver mLockScreenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Intent.ACTION_SCREEN_ON://开屏 可以不处理 开屏-->解屏
                    break;
                case Intent.ACTION_SCREEN_OFF://锁屏
                    if (mVideoView != null && mVideoView.isPlaying()) {
                        mVideoView.onPause();
                        // 存储纪录上传纪录
                        //videoPauseListener
                    }
                    mMediaController.show();
                    mMediaController.showMiddlePlay();
                    break;
                case Intent.ACTION_USER_PRESENT://解屏
//                    if (!mVideoView.isPlaying()) {
//                        mVideoView.onStart();
//                        // 从暂停处开始播放
//                        seekToPausePostion();
//                        mVideoView.onPause();
//                    }
                    seekToPausePostion();

                    if (mVideoView != null) {
                        mVideoView.onPause();
                    }
                    mMediaController.show();
//                    mMediaController.hideMiddlePlay();
                    break;
            }

        }
    };

    public void registerHomeWatcherListener() {
        try {
            IntentFilter filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            mContext.registerReceiver(mHomeWatcherReceiver, filter);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void unregisterHomeWatcherListener() {
        try {
            mContext.unregisterReceiver(mHomeWatcherReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver mHomeWatcherReceiver = new BroadcastReceiver() {
        private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private static final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
        private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";

        @Override
        public void onReceive(Context context, Intent intent) {//android.intent.action.CLOSE_SYSTEM_DIALOGS  电源
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            switch (intent.getAction()) {
                case Intent.ACTION_CLOSE_SYSTEM_DIALOGS:
                    try {
                        switch (intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)) {
                            case SYSTEM_DIALOG_REASON_HOME_KEY:// 短按Home键
                            case SYSTEM_DIALOG_REASON_RECENT_APPS:// 长按Home键 或者 activity切换键//meizu的怎么监听呀
                            case SYSTEM_DIALOG_REASON_ASSIST:// samsung 长按Home键
                                if (mVideoView.isPlaying()) {
                                    mVideoView.onPause();
                                    // 存储纪录上传纪录
                                    //videoPauseListener

                                }
                                mMediaController.show();
                                mMediaController.showMiddlePlay();
                                break;
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}