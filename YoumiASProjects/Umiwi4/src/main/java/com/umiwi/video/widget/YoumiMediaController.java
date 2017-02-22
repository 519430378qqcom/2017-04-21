package com.umiwi.video.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.umiwi.media.VolumeView;
import com.umiwi.ui.R;
import com.umiwi.video.control.IMediaPlayerController;
import com.umiwi.video.control.IMediaPlayerObserver;
import com.umiwi.video.utils.PlayUtils;

import java.util.Formatter;
import java.util.Locale;


/**
 * Created by txy on 15/9/15.
 */
public class YoumiMediaController extends FrameLayout implements IMediaPlayerController {

    private String TAG = "YoumiMediaController";

    private static final int SHOW_SHORT_TIME = 0b101110111000;
    private static final int SHOW_LONG_TIME = 0b1001110001000;

    public YoumiMediaController(Context context) {
        super(context);
        initMediaController(context);
    }

    public YoumiMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMediaController(context);
    }

    public YoumiMediaController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMediaController(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public YoumiMediaController(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initMediaController(context);
    }

    private Context mAppContext;

    private Activity mActivity;

    private IMediaPlayerObserver mPlayer;

    private View view;
    private Toolbar mToolbar;
    private RelativeLayout bottomControlPanel;
    private FrameLayout rightPanelContainerLayout;

    private FrameLayout playerBgPanel;
    private ProgressBar bufferProgress;
    private ImageView mMiddlePlayButton;
    private TextView middleErroContent;
    private ImageView mPlayPauseButton;
    private ImageView mScreenChangeButton;
    private SeekBar playLoadingBar;
    private TextView mCurrentPostionTextView;

    private TextView mBatteryTextView;
    private TextView mTimeTextView;

    private VolumeView mVolumeView;
    private AudioManager mAudioManager;

    private void setActivity(Activity activity) {
        mActivity = activity;
    }

    private void initMediaController(Context context) {
        mAppContext = context.getApplicationContext();
        setActivity((Activity) context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.youmi_player_controller_layout, null);

        LayoutParams frameParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addView(view, frameParams);

        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        initLayout();
        initLoadingPanel();
        initMiddleScrollPanel();

        gestureDetectorCompat = new GestureDetectorCompat(context, mGestureDetectorListener);
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

    }

    private void initLayout() {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        bottomControlPanel = (RelativeLayout) view.findViewById(R.id.player_bottom_control_panel);
        rightPanelContainerLayout = (FrameLayout) view.findViewById(R.id.right_panel_container);
        customPanelContainer = (FrameLayout) view.findViewById(R.id.custom_panel_container);

        playerBgPanel = (FrameLayout) view.findViewById(R.id.player_bg_panel);
        bufferProgress = (ProgressBar) view.findViewById(R.id.buffer_progress);
        mMiddlePlayButton = (ImageView) view.findViewById(R.id.middle_play_button);
        middleErroContent = (TextView) view.findViewById(R.id.middle_erro_text);

        mPlayPauseButton = (ImageView) view.findViewById(R.id.bottom_play_pause_button);
        mScreenChangeButton = (ImageView) view.findViewById(R.id.bottom_screen_change_button);
        mCurrentPostionTextView = (TextView) view.findViewById(R.id.current_postion_text_view);
        playLoadingBar = (SeekBar) view.findViewById(R.id.progress_seekbar);


        mBatteryTextView = (TextView) view.findViewById(R.id.battery_textview);
        mTimeTextView = (TextView) view.findViewById(R.id.time_textview);

        mMiddlePlayButton.setOnClickListener(mMiddlePlayClickListener);
        mPlayPauseButton.setOnClickListener(mPlayPauseClickListener);
        mScreenChangeButton.setOnClickListener(mScreenChangeClickListener);
        playLoadingBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        playLoadingBar.setMax(1000);
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        mToolbar.setNavigationOnClickListener(returnListener);

        mVolumeView = (VolumeView) findViewById(R.id.volume_view);

    }

    private FrameLayout customPanelContainer;

    private FrameLayout loadingLayout;
    private ImageView loadingBg;
    private ProgressBar loadingProgress;
    private TextView loadingContent;

    private void initLoadingPanel() {
        loadingLayout = (FrameLayout) view.findViewById(R.id.loading_panel_container);
        loadingProgress = (ProgressBar) view.findViewById(R.id.loading_progress);
//        loadingBg = (ImageView) view.findViewById(R.id.loading_bg);
        loadingContent = (TextView) view.findViewById(R.id.loading_content);
    }

    private LinearLayout middleScrollPanel;
    private TextView mActionTextView;
    private ImageView mActionImageView;

    private void initMiddleScrollPanel() {
        middleScrollPanel = (LinearLayout) view.findViewById(R.id.player_middle_scroll_control_panel);
        mActionTextView = (TextView) view.findViewById(R.id.action_text_view);
        mActionImageView = (ImageView) view.findViewById(R.id.action_image_view);
    }

    private void showMiddleScroll() {
        middleScrollPanel.setVisibility(View.VISIBLE);
    }

    private void hideMIddleScroll() {
        middleScrollPanel.setVisibility(View.GONE);
    }


    public void setCustomPanelContainer(@NonNull View customView) {
        customPanelContainer.removeAllViews();
        LayoutParams frameParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        customPanelContainer.addView(customView, frameParams);
    }

    /**
     * 左侧面板控制
     */
    public void setRightPanelLayout(View rightPanelLayout) {
        rightPanelContainerLayout.removeAllViews();
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rightPanelContainerLayout.addView(rightPanelLayout, frameParams);
    }


    public void setVideoTitle(String videoTitle) {
        mToolbar.setTitle(videoTitle);
    }

    public void setVideoTitle(CharSequence videoTitle) {
        mToolbar.setTitle(videoTitle);
    }

    public void setReturnIcon(int res) {
        mToolbar.setNavigationIcon(res);
    }

    public void setReturnIcon(Drawable drawable) {
        mToolbar.setNavigationIcon(drawable);
    }

    public void registerBatteryTime() {
        try {
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            mAppContext.registerReceiver(mBatteryLevelReceiver, ifilter);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void unregisterBatteryTime() {
        mBatteryTextView.setText("");
        mTimeTextView.setText("");
        mBatteryTextView.setVisibility(View.GONE);
        mTimeTextView.setVisibility(View.GONE);
    }


    BroadcastReceiver mBatteryLevelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            context.unregisterReceiver(this);
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int batteryPct = level * 100 / scale;
            mBatteryTextView.setText(batteryPct + "%");
            mTimeTextView.setText(PlayUtils.getSystemTime());
            mBatteryTextView.setVisibility(View.VISIBLE);
            mTimeTextView.setVisibility(View.VISIBLE);
        }
    };


    private ConnectivityManager connectivityManager;

    public void registerNetworkStatusListener() {
        connectivityManager = (ConnectivityManager) mAppContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mAppContext.registerReceiver(mNetworkStatusReceiver, intentFilter);
    }

    public void unregisterNetworkStatusListener() {
        mAppContext.unregisterReceiver(mNetworkStatusReceiver);
    }

    public boolean isMoblideNetwork() {
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    public boolean isWIFINetwork() {
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public void setOnlyFullScreen(boolean isOnlyFullScreen) {
        this.isOnlyFullScreen = isOnlyFullScreen;
        if (mScreenChangeButton != null) {
            mScreenChangeButton.setVisibility(View.INVISIBLE);
        }
    }

    boolean isOnlyFullScreen;

    private OnClickListener returnListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (isOnlyFullScreen) {
                isOnlyFullScreen = false;
                mPlayer.goSmallScreen();
                mActivity.finish();
                return;
            }
            if (mPlayer.isFullScreen()) {
                mPlayer.goSmallScreen();
            } else {
                mActivity.finish();
            }
        }
    };

    private BroadcastReceiver mNetworkStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            TelephonyManager.NETWORK_TYPE_TD_SCDMA;
            switch (intent.getAction()) {
                case Intent.ACTION_MANAGE_NETWORK_USAGE:
                    break;
            }
        }
    };

    private View.OnClickListener mMiddlePlayClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            updatePausePlay();

            mPlayer.onStart();
            show(SHOW_SHORT_TIME);
            if (isShowMiddlePlay()) {
                hideMiddlePlay();
            }
            if (isShowEndPanel()) {
                hideEndPanel();
            }
        }
    };

    private final OnClickListener mPlayPauseClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            updatePausePlay();

            if (mPlayer.isPlaying()) {
                mPlayer.onPause();
                showMiddlePlay();
            } else {
                mPlayer.onStart();
                hideMiddlePlay();
                show(SHOW_SHORT_TIME);
            }
        }
    };

    private final OnClickListener mScreenChangeClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (isOnlyFullScreen) {
//                mScreenChangeButton.setEnabled(false);
                return;
            }
            if (!mPlayer.isFullScreen()) {
                mPlayer.goFullScreen();
            } else {
                mPlayer.goSmallScreen();
            }
        }
    };

    private void updatePausePlay() {
        if (mPlayer == null) {
            return;
        }
        if (mPlayPauseButton == null) {
            return;
        }
        int resId;
        if (mPlayer.isPlaying()) {
            resId = R.drawable.video_player_play_selector;
        } else {
            resId = R.drawable.video_player_pause_selector;
        }
        mPlayPauseButton.setImageResource(resId);
    }

    private boolean mShowing = true;
    private boolean mDragging;

    private final SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                return;
            }
            if (null != mPlayer && mPlayer.isPlaying()) {
                long duration = mPlayer.getDuration();
                long newposition = (duration * progress) / 1000L;
                mPlayer.seekTo((int) newposition);
                if (mCurrentPostionTextView != null)
                    mCurrentPostionTextView.setText(stringForTime((int) newposition) + " / " + stringForTime((int) duration));
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            show(3600000);
            mDragging = true;
            mHandler.removeMessages(SHOW_PROGRESS);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mDragging = false;
            setProgress();
            show(sDefaultTimeout);
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    };


    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;

    private static final int sDefaultTimeout = 3000;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int pos;
            switch (msg.what) {
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_PROGRESS:
                    pos = setProgress();
                    if (!mDragging && mShowing && mPlayer != null && mPlayer.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    };

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private int setProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (playLoadingBar != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                playLoadingBar.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
//            playLoadingBar.setSecondaryProgress(percent * 10);
        }

        if (mCurrentPostionTextView != null)
            mCurrentPostionTextView.setText(stringForTime(position) + " / " + stringForTime(duration));

        return position;
    }


    @Override
    public void show() {
        bottomControlPanel.setVisibility(View.VISIBLE);
        mToolbar.setVisibility(View.VISIBLE);
        if (mPlayer != null && mPlayer.isFullScreen()) {
            showRightPanel();
        }
    }

    @Override
    public void hide() {
        mToolbar.setVisibility(View.INVISIBLE);
        bottomControlPanel.setVisibility(View.INVISIBLE);
        hideRightPanel();
        if (mPlayer != null && mPlayer.isFullScreen()) {
            if (onScreenAndControllerHideListener != null) {
                onScreenAndControllerHideListener.hide();
            }
        }
    }


    @Override
    public boolean isShowing() {
        return bottomControlPanel.isShown();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (playLoadingBar != null) {
            playLoadingBar.setEnabled(enabled);
            if (playLoadingBar.isEnabled())
                mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    }

    @Override
    public void setAnchorView(View view) {

    }

    @Override
    public void setMediaPlayer(IMediaPlayerObserver player) {
        mPlayer = player;
    }

    @Override
    public void show(int timeout) {

//        if (!mShowing) {
//            setProgress();
//            if (mPauseButton != null) {
//                mPauseButton.requestFocus();
//            }
//            disableUnsupportedButtons();
//            updateFloatingWindowLayout();
//            mWindowManager.addView(mDecor, mDecorLayoutParams);
//            mShowing = true;
//        }
        show();

        // cause the progress bar to be updated even if mShowing
        // was already true.  This happens, for example, if we're
        // paused with the progress bar showing the user hits play.
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            Message msg = mHandler.obtainMessage(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
    }

    @Override
    public boolean isOnlyFullScreen() {
        return isOnlyFullScreen;
    }

    @Override
    public void showOnce(View view) {
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        loadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean isShowLoading() {
        return loadingLayout.isShown();
    }

    @Override
    public void showCustomPanel() {
        customPanelContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCustomPanel() {
        customPanelContainer.setVisibility(View.GONE);
    }

    @Override
    public boolean isShowCustomPanel() {
        return customPanelContainer.isShown();
    }

    @Override
    public void showRightPanel() {
        rightPanelContainerLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRightPanel() {
        rightPanelContainerLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean isShowRightPanel() {
        return rightPanelContainerLayout.isShown();
    }

    @Override
    public void showMiddlePlay() {
        mMiddlePlayButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideMiddlePlay() {
        mMiddlePlayButton.setVisibility(View.GONE);
    }

    @Override
    public boolean isShowMiddlePlay() {
        return mMiddlePlayButton.isShown();
    }

    @Override
    public void showEndPanel() {
        playerBgPanel.setVisibility(View.VISIBLE);
        showMiddlePlay();
        mMiddlePlayButton.setImageResource(R.drawable.ic_replay);
        middleErroContent.setVisibility(View.GONE);
    }

    @Override
    public void hideEndPanel() {
        playerBgPanel.setVisibility(View.GONE);
        if (isShowMiddlePlay()) {
            hideMiddlePlay();
        }
        mMiddlePlayButton.setImageResource(R.drawable.ic_video_play);
        middleErroContent.setVisibility(View.GONE);
    }

    @Override
    public void showErroPanel() {
        playerBgPanel.setVisibility(View.VISIBLE);
        showMiddlePlay();
        mMiddlePlayButton.setImageResource(R.drawable.ic_video_play);
        middleErroContent.setVisibility(View.VISIBLE);
        middleErroContent.setText("视频加载出错，请重试！");
    }

    @Override
    public boolean isShowEndPanel() {
        return playerBgPanel.isShown();
    }

    @Override
    public void showBufferPanel() {
        bufferProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBufferPanel() {
        bufferProgress.setVisibility(View.GONE);
    }

    @Override
    public void setScreenAndControllerHideListener(screenAndControllerHide listener) {
        this.onScreenAndControllerHideListener = listener;
    }

    @Override
    protected void onDetachedFromWindow() {//clear
        super.onDetachedFromWindow();
    }

    screenAndControllerHide onScreenAndControllerHideListener;


    private int GESTURE_FLAG = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        int action = MotionEventCompat.getActionMasked(event);

        if (mPlayer == null || mPlayer.getDuration() == 0 || mPlayer.getCurrentPosition() == 0) {
            return false;
        }

        if (event.getPointerCount() == 1 && event.getAction() == MotionEvent.ACTION_UP) {
            hideMIddleScroll();
            mActionTextView.setText("");
            if (direction == DIRECTION_HORIZONTAL) {
                int hDesc = (int) (currentPosition + (event.getX() - x) * 400);
                mPlayer.seekTo(hDesc);
            }
            direction = -1;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            GESTURE_FLAG = 0;// 手指离开屏幕后，重置调节音量或进度的标志
        }

        return gestureDetectorCompat.onTouchEvent(event);
    }

    boolean isSlide = false;

    private GestureDetectorCompat gestureDetectorCompat;

    private GestureDetector.SimpleOnGestureListener mGestureDetectorListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {//抬起：手指离开触摸屏的那一刹那 无法与doubleTap/doubleTapEvent 区分
            if (isSlide) {
                isSlide = false;
                show(3000);
            }
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {//单击：与doubleTap/doubleTapEvent 区分

            if (mPlayer == null || mPlayer.isPlayComplete()) {
                return true;
            }
            if (isShowing()) {//单击隐藏
                hide();
                if (isShowMiddlePlay()) {
                    hideMiddlePlay();
                }
            } else {//单击显示
                if (mPlayer.isPlaying()) {
                    if (isShowMiddlePlay()) {
                        hideMiddlePlay();
                    }
                    show(SHOW_LONG_TIME);
                } else {
                    if (null != mHandler) {
                        mHandler.removeCallbacksAndMessages(null);
                    }
                    show();
                    showMiddlePlay();
                }

            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {// 双击：与doubleTapEvent的区别？
            if (mPlayer == null || mPlayer.isPlayComplete()) {
                return true;
            }

            updatePausePlay();

            if (mPlayer.isPlaying()) {//双击暂停
                mPlayer.onPause();
                if (isShowing()) {
                    if (null != mHandler) {
                        mHandler.removeCallbacksAndMessages(null);
                    }
                    showMiddlePlay();
                } else {
                    if (isShowMiddlePlay()) {
                        hideMiddlePlay();
                    }
                }
            } else {//双击播放
                mPlayer.onStart();
                if (isShowMiddlePlay()) {

                }
                hideMiddlePlay();
                if (isShowing()) {
                    show(SHOW_SHORT_TIME);
                }
            }


            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {// 双击：与doubleTap的区别？
            return super.onDoubleTapEvent(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {//长按：手指按在持续一段时间，并且没有松开。
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent event, float distanceX, float distanceY) {//滚动：手指在触摸屏上滑动。
//            return super.onScroll(e1, e2, distanceX, distanceY);
            if (isShowing()) {//TODO 滑动操作时，如果控制面板已经显示了，先取消自动隐藏功能，手指离开后重新启动3秒自动隐藏
                isSlide = true;
                mHandler.removeCallbacksAndMessages(null);
            }
            if (mPlayer == null || mPlayer.isPlayComplete()) {
                return true;
            }

            showMiddleScroll();
            float h = event.getX() - e1.getX();
            int hDesc = (int) (currentPosition + h * 400);

            if (direction == -1) {
                int avtx = (int) Math.abs(distanceX);
                int avty = (int) Math.abs(distanceY);
                if (avtx < 2 && avty < 2) {

                } else {
                    direction = avtx > avty ? DIRECTION_HORIZONTAL : DIRECTION_VERTICAL;
                }
            } else {
                mActionImageView.setVisibility(View.VISIBLE);
                int max = mAudioManager
                        .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                if (direction == DIRECTION_VERTICAL) {// vertical
                    int currentVolume = mAudioManager
                            .getStreamVolume(AudioManager.STREAM_MUSIC);
                    int delta = (int) ((event.getY() - lastY) / 20);
                    int desc = currentVolume - delta;
                    if (delta != 0) {
                        lastY = event.getY();
                    }
                    desc = Math.max(desc, 0);
                    desc = Math.min(desc, max);
                    mVolumeView.setMaxVolume(max);
                    mVolumeView.setVolume(desc);
                    if (desc == 0) {
                        mActionImageView
                                .setImageResource(R.drawable.video_player_bt_sound_mute);
                    } else {
                        mActionImageView
                                .setImageResource(R.drawable.video_player_bt_sound);
                    }

                    int p = (int) (desc * 1.0 / max * 100);
                    String percent = String.format(Locale.ENGLISH, "%3d", p);
                    mActionTextView.setText(percent + "% ");
                } else {
                    int pos = mPlayer.getCurrentPosition();
                    if (hDesc > pos) {
                        mActionImageView
                                .setImageResource(R.drawable.ic_fast_forward);
                    } else {
                        mActionImageView
                                .setImageResource(R.drawable.ic_fast_backward);
                    }
                    if (hDesc < 0) {
                        hDesc = 0;
                    }

                    int duration = mPlayer.getDuration();
                    if (hDesc > duration) {
                        hDesc = duration;
                    }

                    StringBuilder sb = new StringBuilder();
                    sb.append(positionToText(hDesc));
                    sb.append("/");
                    sb.append(positionToText(duration));
                    String text = sb.toString();
                    mActionTextView.setText(text);

//                    if (playLoadingBar != null) {
//                        if (duration > 0) {
//                            playLoadingBar.setProgress((int) (1000L * pos / duration));
//                        }
//                    }
                }
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {//抛掷：手指在触摸屏上迅速移动，并松开的动作。
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onShowPress(MotionEvent e) {//按住：手指按在触摸屏上，它的时间范围在按下起效，在长按之前
            super.onShowPress(e);
        }

        @Override
        public boolean onDown(MotionEvent event) {//按下： 刚刚手指接触到触摸屏的那一刹那，就是触的那一下。
            x = event.getX();
            lastY = event.getY();
            if (mPlayer != null && mPlayer.isPlaying()) {
                currentPosition = mPlayer.getCurrentPosition();
            }

            return true;//处理事件
        }
    };
    private static final int DIRECTION_VERTICAL = 1;
    private static final int DIRECTION_HORIZONTAL = 0;
    private float x;
    private int direction = -1;
    private int currentPosition = 0;
    private float lastY = 0;

    private String positionToText(int i) {
        i /= 1000;
        int minute = i / 60;// 分钟
        int hour = minute / 60;// 小时
        int second = i % 60;// 秒
        minute %= 60;
        String str = String.format(Locale.CHINA, "%02d:%02d:%02d", hour,
                minute, second);
        if (hour >= 100) {
            return "00:00:00";
        }
        return str;
    }
}
