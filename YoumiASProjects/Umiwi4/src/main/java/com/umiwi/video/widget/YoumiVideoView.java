/*
ths IjkVideoView
 */
package com.umiwi.video.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.umiwi.ui.R;
import com.umiwi.video.application.Settings;
import com.umiwi.video.control.IMediaPlayerController;
import com.umiwi.video.control.IMediaPlayerObserver;
import com.umiwi.video.services.MediaPlayerService;
import com.umiwi.video.utils.DeviceOrientationHelper;
import com.umiwi.video.utils.DeviceOrientationHelper.OrientationChangeCallback;
import com.umiwi.video.utils.MediaPlayerDelegate;
import com.umiwi.video.utils.Orientation;
import com.umiwi.video.utils.PlayUtils;
import com.umiwi.video.utils.PreferenceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.youmi.framework.util.AndroidSDK;
import cn.youmi.framework.util.ToastU;
import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.TextureMediaPlayer;


/**
 * Created by txy on 15/9/10.
 */
public class YoumiVideoView extends FrameLayout implements IMediaPlayerObserver, OrientationChangeCallback {
    private String TAG = "YoumiVideoView";

    // settable by the client
    private Uri mUri;
    private Map<String, String> mHeaders;

    // all possible internal states
    /**
     * 错误
     */
    private static final int STATE_ERROR = -1;
    /**
     * 空闲
     */
    private static final int STATE_IDLE = 0;
    /**
     * 准备中
     */
    private static final int STATE_PREPARING = 1;
    /**
     * 准备完成
     */
    private static final int STATE_PREPARED = 2;
    /**
     * 播放ing
     */
    private static final int STATE_PLAYING = 3;
    /**
     * 播放暂停
     */
    private static final int STATE_PAUSED = 4;
    /**
     * 播放中返回或播放完成
     */
    private static final int STATE_PLAYBACK_COMPLETED = 5;

    // mCurrentState is a VideoView object's current state.
    // mTargetState is the state that a method caller intends to reach.
    // For instance, regardless the VideoView object's current state,
    // calling pause() intends to bring the object to a target state
    // of STATE_PAUSED.
    private int mCurrentState = STATE_IDLE;
    private int mTargetState = STATE_IDLE;

    // All the stuff we need for playing and showing a video
    private IRenderView.ISurfaceHolder mSurfaceHolder = null;
    private IMediaPlayer mMediaPlayer = null;
    // private int         mAudioSession;
    private int mVideoWidth;
    private int mVideoHeight;
    private int mSurfaceWidth;
    private int mSurfaceHeight;
    private IMediaPlayerController mMediaController;
    private IMediaPlayer.OnCompletionListener mOnCompletionListener;
    private IMediaPlayer.OnPreparedListener mOnPreparedListener;
    private int mCurrentBufferPercentage;
    private IMediaPlayer.OnErrorListener mOnErrorListener;
    private IMediaPlayer.OnInfoListener mOnInfoListener;
    private int mSeekWhenPrepared;  // recording the seek position while preparing
    private boolean mCanPause = true;
    private boolean mCanSeekBack = true;
    private boolean mCanSeekForward = true;

    /** Subtitle rendering widget overlaid on top of the video. */
    // private RenderingWidget mSubtitleWidget;

    /**
     * Listener for changes to subtitle data, used to redraw when needed.
     */
    // private RenderingWidget.OnChangedListener mSubtitlesChangedListener;

    private MediaPlayerDelegate mMediaPlayerDelegate;

    private DeviceOrientationHelper orientationHelper;

    private LayoutInflater inflater;
    private FrameLayout mVideoFrameLayout;
    private RelativeLayout spaceMiddle;

    private FrameLayout playerMediaControllerPanel;

    /**
     * 用于适应屏幕大小的
     */
    private FitScaleImageView playback;
    private View view;//稍后修改消除用 view. 的影响\\^^^^^//


    int fullWidth, fullHeight;// , smallWidth, smallHeight;

    int landPlayheight;
    int landPlaywidth;
    int playwidth;
    int playheight;
    int lastFullHeight;
    int lastFullWidth;
    int lastpercent;
    int lastOrientation;

    int videoSize = 100;

    private Context mAppContext;
    private Settings mSettings;

    private Activity mActivity;

    public YoumiVideoView(Context context) {
        super(context);
        initVideoView(context);
    }

    public YoumiVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVideoView(context);
    }

    public YoumiVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVideoView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public YoumiVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initVideoView(context);
    }

    private int mVideoSarNum;
    private int mVideoSarDen;

    private int mVideoRotationDegree;

    public static final int RENDER_NONE = 0;
    public static final int RENDER_SURFACE_VIEW = 1;
    public static final int RENDER_TEXTURE_VIEW = 2;

    private List<Integer> mAllRenders = new ArrayList<Integer>();
    private int mCurrentRenderIndex = 0;
    private int mCurrentRender = RENDER_NONE;

    private void initRenders() {
        mAllRenders.clear();

        if (mSettings.getEnableSurfaceView()) {
            mAllRenders.add(RENDER_SURFACE_VIEW);
        }
        if (mSettings.getEnableTextureView() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mAllRenders.add(RENDER_TEXTURE_VIEW);
        }
        if (mSettings.getEnableNoView()) {
            mAllRenders.add(RENDER_NONE);
        }

        if (mAllRenders.isEmpty()) {
            mAllRenders.add(RENDER_SURFACE_VIEW);
        }
        mCurrentRender = mAllRenders.get(mCurrentRenderIndex);
        setRender(mCurrentRender);
    }

    public int toggleRender() {
        mCurrentRenderIndex++;
        mCurrentRenderIndex %= mAllRenders.size();

        mCurrentRender = mAllRenders.get(mCurrentRenderIndex);
        setRender(mCurrentRender);
        return mCurrentRender;
    }

//    @NonNull
//    public static String getRenderText(Context context, int render) {
//        String text;
//        switch (render) {
//            case RENDER_NONE:
//                text = context.getString(R.string.VideoView_render_none);
//                break;
//            case RENDER_SURFACE_VIEW:
//                text = context.getString(R.string.VideoView_render_surface_view);
//                break;
//            case RENDER_TEXTURE_VIEW:
//                text = context.getString(R.string.VideoView_render_texture_view);
//                break;
//            default:
//                text = context.getString(R.string.N_A);
//                break;
//        }
//        return text;
//    }

    private IRenderView mRenderView;

    public void setRenderView(IRenderView renderView) {
        if (mRenderView != null) {
            if (mMediaPlayer != null)
                mMediaPlayer.setDisplay(null);

            View renderUIView = mRenderView.getView();
            mRenderView.removeRenderCallback(mSHCallback);
            mRenderView = null;
            mVideoFrameLayout.removeView(renderUIView);
        }

        if (renderView == null)
            return;

        mRenderView = renderView;
        renderView.setAspectRatio(mCurrentAspectRatio);
        if (mVideoWidth > 0 && mVideoHeight > 0)
            renderView.setVideoSize(mVideoWidth, mVideoHeight);
        if (mVideoSarNum > 0 && mVideoSarDen > 0)
            renderView.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);

        View renderUIView = mRenderView.getView();
        LayoutParams lp = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        renderUIView.setLayoutParams(lp);
        mVideoFrameLayout.addView(renderUIView);

        mRenderView.addRenderCallback(mSHCallback);
        mRenderView.setVideoRotation(mVideoRotationDegree);
    }

    public void setRender(int render) {
        switch (render) {
            case RENDER_NONE:
                setRenderView(null);
                break;
            case RENDER_TEXTURE_VIEW: {
                TextureRenderView renderView = new TextureRenderView(getContext());
                if (mMediaPlayer != null) {
                    renderView.getSurfaceHolder().bindToMediaPlayer(mMediaPlayer);
                    renderView.setVideoSize(mMediaPlayer.getVideoWidth(), mMediaPlayer.getVideoHeight());
                    renderView.setVideoSampleAspectRatio(mMediaPlayer.getVideoSarNum(), mMediaPlayer.getVideoSarDen());
                    renderView.setAspectRatio(mCurrentAspectRatio);
                }
                setRenderView(renderView);
                break;
            }
            case RENDER_SURFACE_VIEW: {
                SurfaceRenderView renderView = new SurfaceRenderView(getContext());
                renderView.getHolder().setFormat(PixelFormat.TRANSPARENT);
                renderView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                setRenderView(renderView);
                break;
            }
            default:
                Log.e(TAG, String.format(Locale.getDefault(), "invalid render %d\n", render));
                break;
        }
    }

    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        setVideoURI(uri, null);
    }

    private void setVideoURI(Uri uri, Map<String, String> headers) {
        mUri = uri;
        mHeaders = headers;
        mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    View yp_player_container;

    private void setActivity(Activity activity) {
        mActivity = activity;
    }

    private void initVideoView(Context context) {
        mAppContext = context.getApplicationContext();
        mSettings = new Settings(mAppContext);

        setActivity((Activity) context);
        inflater = (LayoutInflater) mAppContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.yp_player_view, null);
        yp_player_container = view.findViewById(R.layout.yp_player_container);
        addView(view);

        mMediaPlayerDelegate = new MediaPlayerDelegate() {
            @Override
            public int getVideoOrientation() {
                return 0;
            }

            @Override
            public void changeVideoSize(int var1, int var2) {

            }

            @Override
            public void goFullScreen() {

            }

        };

        initLayout();
        initBackground();
//        initRenders();


        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        mCurrentState = STATE_IDLE;
        mTargetState = STATE_IDLE;
    }


//    @Override
//    protected void onWindowVisibilityChanged(int visibility) {
//        super.onWindowVisibilityChanged(visibility);
//        switch (visibility) {
//            case View.VISIBLE:
//                start();
//                break;
//            case View.INVISIBLE:
//                pause();
//                break;
//            case View.GONE:
//                pause();
//                break;
//
//        }
//        Log.e(TAG, "onWindowVisibilityChanged" + visibility);
//    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (orientationHelper != null)
            orientationHelper.enableListener();//激活自动转屏功能
    }

    @Override
    protected void onDetachedFromWindow() {//当view离开附着的窗口时
        super.onDetachedFromWindow();
        if (orientationHelper != null) {
            orientationHelper.disableListener();
            orientationHelper.setCallback(null);
            orientationHelper = null;
        }
        mVideoFrameLayout.setKeepScreenOn(false);//小米手机等等
        initRenders();
    }

    private void initLayout() {
        spaceMiddle = (RelativeLayout) view.findViewById(R.id.space_middle);
        mVideoFrameLayout = (FrameLayout) view.findViewById(R.id.surface_view);
//        mVideoFrameLayout.setOpaque(false);
//        mVideoFrameLayout.setSurfaceTextureListener(new CanvasListener());
        mVideoFrameLayout.setKeepScreenOn(true);

//        if (null != mMediaPlayer) {
//            mVideoFrameLayout.getSurfaceHolder().bindToMediaPlayer(mMediaPlayer);
//            mVideoFrameLayout.setVideoSize(mMediaPlayer.getVideoWidth(), mMediaPlayer.getVideoHeight());
//            mVideoFrameLayout.setVideoSampleAspectRatio(mMediaPlayer.getVideoSarNum(), mMediaPlayer.getVideoSarDen());
//        }
//        mVideoFrameLayout.setLayerType(TextureRenderView.LAYER_TYPE_HARDWARE, new Paint());//TODO 待定
//        mVideoFrameLayout.setAspectRatio(mCurrentAspectRatio);
//        mVideoFrameLayout.addRenderCallback(mSHCallback);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasVirtualButtonBar(mAppContext)) {
            addLeftAndRight();
        }
        playback = (FitScaleImageView) view.findViewById(R.id.player_back);

        playerMediaControllerPanel = (FrameLayout) view.findViewById(R.id.player_media_controller_panel_container);

        orientationHelper = new DeviceOrientationHelper(mActivity, this);


        initRenders();
    }

    // TODO change this
    public void relayout() {
        super.requestLayout();
    }

    public void setMediaController(@NonNull YoumiMediaController controller) {
        mMediaController = controller;
        if (mMediaController != null) {
            mMediaController.hide();
        }
        playerMediaControllerPanel.removeAllViews();
        if (playerMediaControllerPanel != null) {
            LayoutParams frameParams = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            playerMediaControllerPanel.addView((YoumiMediaController) mMediaController, frameParams);
        } else {
            playerMediaControllerPanel = (FrameLayout) view.findViewById(R.id.player_media_controller_panel_container);
            LayoutParams frameParams = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            playerMediaControllerPanel.addView((YoumiMediaController) mMediaController, frameParams);
        }

        attachMediaController();
    }


    private void attachMediaController() {
        if (mMediaController != null) {
            mMediaController.hide();
        }
        if (mMediaPlayer != null && mMediaController != null) {
            mMediaController.setMediaPlayer(this);
            View anchorView = this.getParent() instanceof View ?
                    (View) this.getParent() : this;
            mMediaController.setAnchorView(anchorView);
            mMediaController.setEnabled(isInPlaybackState());
        }
    }

    /**
     * 带有虚拟键的4.4设备转屏会出现半屏，临时方案是通过给surfaceview左右添加view
     */
    private void addLeftAndRight() {

        View viewLeft = new View(mAppContext);
        viewLeft.setId(R.id.video_left_id);
        viewLeft.setVisibility(View.INVISIBLE);
        RelativeLayout.LayoutParams leftLayoutParams = new RelativeLayout.LayoutParams(
                0, 0);
        leftLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        viewLeft.setLayoutParams(leftLayoutParams);
        spaceMiddle.addView(viewLeft);

        View viewRight = new View(mAppContext);
        viewRight.setId(R.id.video_right_id);
        viewRight.setVisibility(View.INVISIBLE);
        RelativeLayout.LayoutParams rightLayoutParams = new RelativeLayout.LayoutParams(
                0, 0);
        rightLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        viewRight.setLayoutParams(rightLayoutParams);
        spaceMiddle.addView(viewRight);

        RelativeLayout.LayoutParams surfaceLayoutParams = (RelativeLayout.LayoutParams) mVideoFrameLayout
                .getLayoutParams();
        surfaceLayoutParams.addRule(RelativeLayout.RIGHT_OF, viewLeft.getId());
        surfaceLayoutParams.addRule(RelativeLayout.LEFT_OF, viewRight.getId());
    }

    /**
     * 调整播放画面的宽高比
     *
     * @param force 是否强制刷新播放器宽高
     */
    public void resizeMediaPlayer(boolean force) {
        if (mMediaPlayerDelegate != null) {
            if (mMediaPlayerDelegate.isFullScreen) {
//                videoSize = sp.getInt("video_size", 100);
                videoSize = 100;
            } else {
                videoSize = 100;
            }
            resizeVideoView(videoSize, force);
        }
    }

    /**
     * 重新调整视频的画面
     *
     * @param percent 画面百分比
     * @param force   是否强制刷新
     */
    public void resizeVideoView(int percent, boolean force) {

        int showWidth, showHeight;
        fullWidth = playback.getWidth();
        fullHeight = playback.getHeight();

        int orientation = mMediaPlayerDelegate == null ? 0 : mMediaPlayerDelegate.getVideoOrientation();

        if (lastpercent == percent && fullWidth == lastFullWidth
                && fullHeight == lastFullHeight && lastOrientation == orientation && !force) {
            return;
        }

        if (mMediaPlayerDelegate == null) {
            return;
        }
        if (percent == -1) {
            if (mMediaPlayerDelegate.isFullScreen) {
                showHeight = fullHeight;
                showWidth = fullWidth;
            } else if (PlayUtils.isLandscape(mAppContext)) {
                showHeight = landPlayheight;
                showWidth = landPlaywidth;
            } else {
                showHeight = playheight;
                showWidth = playwidth;
            }
        } else {
            int resizeScreenWidth = 0, resizeScreenHeight = 0;
            if (percent == 50) {//50%
                //控件一般高
                resizeScreenWidth = fullWidth / 2;
                resizeScreenHeight = fullHeight / 2;
            } else if (percent == 75) {//75%
                resizeScreenWidth = fullWidth * 3 / 4;
                resizeScreenHeight = fullHeight * 3 / 4;
            } else {//100%
                resizeScreenHeight = fullHeight;
                resizeScreenWidth = fullWidth;
            }
            // 以宽度为基准
            showWidth = resizeScreenWidth;
            // 视频的宽度
            int videoWidth = 0, videoHeight = 0;
            if (orientation == 0 || orientation == 3) {
                videoWidth = mVideoWidth;
                videoHeight = mVideoHeight;
            } else {
                videoHeight = mVideoHeight;
                videoWidth = mVideoWidth;
            }

            if (videoWidth == 0) {
                return;
            }

            // 成比例的高度
            showHeight = showWidth * videoHeight / videoWidth;

            // 展示的高于预留的
            if (showHeight > resizeScreenHeight) {
                showHeight = resizeScreenHeight;
                showWidth = resizeScreenHeight * videoWidth / videoHeight;
            }
        }

        int leftWidth = (fullWidth - showWidth) / 2;
        int topWidth = (fullHeight - showHeight) / 2;
        mRenderView.setVideoSize(showWidth, showHeight);
        RelativeLayout leftSpace = (RelativeLayout) view.findViewById(R.id.space_left);
        RelativeLayout rightSpace = (RelativeLayout) view.findViewById(R.id.space_right);
        RelativeLayout topSpace = (RelativeLayout) view.findViewById(R.id.space_top);
        RelativeLayout bottomSpace = (RelativeLayout) view.findViewById(R.id.space_bottom);
        leftSpace.setVisibility(View.INVISIBLE);
        rightSpace.setVisibility(View.INVISIBLE);
        topSpace.setVisibility(View.INVISIBLE);
        bottomSpace.setVisibility(View.INVISIBLE);

        RelativeLayout.LayoutParams leftPara = (RelativeLayout.LayoutParams) leftSpace
                .getLayoutParams();
        leftPara.height = fullHeight;
        leftPara.width = leftWidth;
        // if (leftWidth > 0) {
        // leftPara.rightMargin = 1;
        // }
        leftSpace.setLayoutParams(leftPara);
        leftSpace.requestLayout();
        RelativeLayout.LayoutParams rightPara = (RelativeLayout.LayoutParams) rightSpace
                .getLayoutParams();
        rightPara.height = fullHeight;
        rightPara.width = leftWidth;
        // if (leftWidth > 0) {
        // rightPara.leftMargin = 1;
        // }
        rightSpace.setLayoutParams(rightPara);
        rightSpace.requestLayout();
        RelativeLayout.LayoutParams topPara = (RelativeLayout.LayoutParams) topSpace
                .getLayoutParams();
        topPara.height = topWidth;
        topPara.width = fullWidth;
//        if (topWidth > 0) {
//            topPara.bottomMargin = 1;
//        }
        topSpace.setLayoutParams(topPara);
        topSpace.requestLayout();
        RelativeLayout.LayoutParams bottomPara = (RelativeLayout.LayoutParams) bottomSpace
                .getLayoutParams();
        bottomPara.height = topWidth;
        bottomPara.width = fullWidth;
//        if (topWidth > 0) {
//            bottomPara.topMargin = 1;
//        }
        bottomSpace.setLayoutParams(bottomPara);
        bottomSpace.requestLayout();
        // 是否需要对视频做特殊大小处理 no
//        ++showWidth;
//        ++showHeight;
        if (mMediaPlayerDelegate != null) {
            mMediaPlayerDelegate.changeVideoSize(showWidth, showHeight);
        }
        lastFullHeight = fullHeight;
        lastFullWidth = fullWidth;
        lastpercent = percent;
        lastOrientation = orientation;

        setSmallScreenLayout();
    }

    private void openVideo() {
        if (mUri == null || mSurfaceHolder == null) {
            // not ready for playback just yet, will try again later
            return;
        }
        // we shouldn't clear the target state, because somebody might have
        // called start() previously
        release(false);
        AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        try {

            mMediaPlayer = createPlayer(mSettings.getPlayer());

            // TODO: create SubtitleController in MediaPlayer, but we need
            // a context for the subtitle renderers
            final Context context = getContext();
            // REMOVED: SubtitleController

            // REMOVED: mAudioSession
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnInfoListener(mInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mCurrentBufferPercentage = 0;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                mMediaPlayer.setDataSource(mAppContext, mUri, mHeaders);
            } else {
                mMediaPlayer.setDataSource(mUri.toString());
            }
            bindSurfaceHolder(mMediaPlayer, mSurfaceHolder);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);//设置播放里常亮
            mMediaPlayer.prepareAsync();

            // REMOVED: mPendingSubtitleTracks

            // we don't set the target state here either, but preserve the
            // target state that was there before.
            mCurrentState = STATE_PREPARING;
            attachMediaController();
        } catch (IOException ex) {
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
            return;
        } catch (IllegalArgumentException ex) {
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
            return;
        }

    }

//    @NonNull
//    public static String getPlayerText(Context context, int player) {
//        String text;
//        switch (player) {
//            case Settings.PV_PLAYER__AndroidMediaPlayer:
//                text = context.getString(R.string.VideoView_player_AndroidMediaPlayer);
//                break;
//            case Settings.PV_PLAYER__IjkMediaPlayer:
//                text = context.getString(R.string.VideoView_player_IjkMediaPlayer);
//                break;
//            case Settings.PV_PLAYER__IjkExoMediaPlayer:
//                text = context.getString(R.string.VideoView_player_IjkExoMediaPlayer);
//                break;
//            default:
//                text = context.getString(R.string.N_A);
//                break;
//        }
//        return text;
//    }

    public IMediaPlayer createPlayer(int playerType) {
        IMediaPlayer mediaPlayer = null;
//        Log.e("createplayer", "====" + playerType);
        switch (playerType) {
//            case Settings.PV_PLAYER__IjkExoMediaPlayer: {
//                IjkExoMediaPlayer IjkExoMediaPlayer = new IjkExoMediaPlayer(mAppContext);
//                mediaPlayer = IjkExoMediaPlayer;
//            }
//            break;
            case Settings.PV_PLAYER__AndroidMediaPlayer: {
                AndroidMediaPlayer androidMediaPlayer = new AndroidMediaPlayer();
                mediaPlayer = androidMediaPlayer;
            }
            break;
            case Settings.PV_PLAYER__IjkMediaPlayer:
            default: {
                IjkMediaPlayer ijkMediaPlayer = null;
                if (mUri != null) {
                    ijkMediaPlayer = new IjkMediaPlayer();
                    IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);

                    if (mSettings.getUsingMediaCodec()) {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
                        if (mSettings.getUsingMediaCodecAutoRotate()) {
                            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
                        } else {
                            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 0);
                        }
                    } else {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);
                    }

                    if (mSettings.getUsingOpenSLES()) {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1);
                    } else {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
                    }

                    String pixelFormat = mSettings.getPixelFormat();
                    if (TextUtils.isEmpty(pixelFormat)) {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
                    } else {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", pixelFormat);
                    }
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);

                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);

                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
                }
                mediaPlayer = ijkMediaPlayer;
            }
            break;
        }

        if (mSettings.getEnableDetachedSurfaceTextureView()) {
            mediaPlayer = new TextureMediaPlayer(mediaPlayer);
        }

        return mediaPlayer;
    }

    public void stopPlayback() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mCurrentState = STATE_IDLE;
            mTargetState = STATE_IDLE;
            AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
            am.abandonAudioFocus(null);
        }
    }


    IMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener =
            new IMediaPlayer.OnVideoSizeChangedListener() {
                public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
                    /** 视频宽高比发生变化*/
//                    if (mVideoHeight == height && mVideoWidth == width) {
//                        return;
//                    }

                    mVideoWidth = mp.getVideoWidth();
                    mVideoHeight = mp.getVideoHeight();
                    mVideoSarNum = mp.getVideoSarNum();
                    mVideoSarDen = mp.getVideoSarDen();

//                    if (mVideoWidth != 0 && mVideoHeight != 0) {
//                        if (mRenderView != null) {
//                            mRenderView.setVideoSize(mVideoWidth, mVideoHeight);
//                            mRenderView.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
//                        }
//                        requestLayout();
//                    }
                    mRenderView.setVideoSize(mVideoWidth, mVideoHeight);
                    mRenderView.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
                    relayout();

//                    mVideoWidth = width;
//                    mVideoHeight = height;
//                    mActivity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
////                            mVideoFrameLayout.setVideoSize(mMediaPlayer.getVideoWidth(), mMediaPlayer.getVideoHeight());
////                            mVideoFrameLayout.setVideoSampleAspectRatio(mMediaPlayer.getVideoSarNum(), mMediaPlayer.getVideoSarDen());
////                            resizeMediaPlayer(false);
//                        }
//                    });
//                    requestLayout();
                }
            };

    IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
        public void onPrepared(IMediaPlayer mp) {
            mCurrentState = STATE_PREPARED;

            // Get the capabilities of the player for this stream
            // REMOVED: Metadata

            if (mOnPreparedListener != null) {
                mOnPreparedListener.onPrepared(mMediaPlayer);
            }
            if (mMediaController != null) {
                mMediaController.setEnabled(true);
            }

            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();

            int seekToPosition = mSeekWhenPrepared;  // mSeekWhenPrepared may be changed after seekTo() call
            if (seekToPosition != 0) {
                seekTo(seekToPosition);
            }
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                // REMOVED: getHolder().setFixedSize(mVideoWidth, mVideoHeight);
                if (mVideoFrameLayout != null) {
//                    mVideoFrameLayout.setVideoSize(mVideoWidth, mVideoHeight);//初始化
                    mRenderView.setVideoSize(mVideoWidth, mVideoHeight);
                    mRenderView.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
//                    resizeMediaPlayer(false);
//                    mVideoFrameLayout.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
                    if (!mRenderView.shouldWaitForResize() || mSurfaceWidth == mVideoWidth && mSurfaceHeight == mVideoHeight) {
                        // We didn't actually change the size (it was already at the size
                        // we need), so we won't get a "surface changed" callback, so
                        // start the video here instead of in the callback.
                        if (mTargetState == STATE_PLAYING) {
                            onStart();
                            if (mMediaController != null) {
                                mMediaController.hide();
                            }
                            if (mMediaController.isShowEndPanel()) {
                                mMediaController.hideEndPanel();
                            }
                            mMediaController.hideLoading();
                            if (mMediaController.isShowCustomPanel()) {
                                mMediaController.hideCustomPanel();
                            }
                        } else if (!isPlaying() &&
                                (seekToPosition != 0 || getCurrentPosition() > 0)) {
                            if (mMediaController != null) {
                                // Show the media controls when we're paused into a video and make 'em stick.
                                mMediaController.show(3000);
                            }
                        }
                    }
                }

            } else {
                // We don't know the video size yet, but should start anyway.
                // The video size might be reported to us later.
                if (mTargetState == STATE_PLAYING) {
                    onStart();
                    if (mMediaController != null) {
                        mMediaController.hide();
                    }
                    if (mMediaController.isShowEndPanel()) {
                        mMediaController.hideEndPanel();
                    }
                    mMediaController.hideLoading();
                    if (mMediaController.isShowCustomPanel()) {
                        mMediaController.hideCustomPanel();
                    }
                }
            }
        }
    };

    private IMediaPlayer.OnCompletionListener mCompletionListener =
            new IMediaPlayer.OnCompletionListener() {
                public void onCompletion(IMediaPlayer mp) {
                    mCurrentState = STATE_PLAYBACK_COMPLETED;
                    mTargetState = STATE_PLAYBACK_COMPLETED;
                    if (mMediaController != null) {
                        mMediaController.hide();
                    }
                    if (mOnCompletionListener != null) {
                        mOnCompletionListener.onCompletion(mMediaPlayer);
                    }
                    /** 播放结束处理*/
                    mMediaController.showEndPanel();

                }
            };

    private IMediaPlayer.OnInfoListener mInfoListener =
            new IMediaPlayer.OnInfoListener() {
                public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
                    if (mOnInfoListener != null) {
                        mOnInfoListener.onInfo(mp, arg1, arg2);
                    }
                    switch (arg1) {
                        case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                            break;
                        case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                            mMediaController.showBufferPanel();
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                            mMediaController.hideBufferPanel();
                            break;
                        case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                            break;
                        case IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                            break;
                        case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                            break;
                        case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                            break;
                        case IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                            break;
                        case IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                            break;
                        case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                            mVideoRotationDegree = arg2;
                            if (mRenderView != null)
                                mRenderView.setVideoRotation(arg2);
                            break;
                        case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                            break;
                    }
                    return true;
                }
            };

    private IMediaPlayer.OnErrorListener mErrorListener =
            new IMediaPlayer.OnErrorListener() {
                public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
                    mCurrentState = STATE_ERROR;
                    mTargetState = STATE_ERROR;
                    if (mMediaController != null) {
                        mMediaController.hide();
                    }

                    /* If an error handler has been supplied, use it and finish. */
                    if (mOnErrorListener != null) {
                        if (mOnErrorListener.onError(mMediaPlayer, framework_err, impl_err)) {
                            return true;
                        }
                    }

                    switch (framework_err) {
                        case IMediaPlayer.MEDIA_ERROR_UNKNOWN:
                            break;
                        case IMediaPlayer.MEDIA_ERROR_SERVER_DIED:
                            break;
                        case IMediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                            break;
                        case IMediaPlayer.MEDIA_ERROR_IO:
                            break;
                        case IMediaPlayer.MEDIA_ERROR_MALFORMED:
                            break;
                        case IMediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                            break;
                        case IMediaPlayer.MEDIA_ERROR_TIMED_OUT:
                            break;
                        case -10000://TODO 网络切换暂时会报这个错误
                            mMediaController.showErroPanel();
                            break;
                    }

                    /* Otherwise, pop up an error dialog so the user knows that
                     * something bad has happened. Only try and pop up the dialog
                     * if we're attached to a window. When we're going away and no
                     * longer have a window, don't bother showing the user an error.
                     */
                    if (getWindowToken() != null) {
                        Resources r = mAppContext.getResources();
                        int messageId;

                        if (framework_err == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK) {
//                            messageId = R.string.VideoView_error_text_invalid_progressive_playback;
                        } else {
//                            messageId = R.string.VideoView_error_text_unknown;
                        }

//                        new AlertDialog.Builder(getContext())
//                                .setMessage(messageId)
//                                .setPositiveButton(R.string.VideoView_error_button,
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int whichButton) {
//                                            /* If we get here, there is no onError listener, so
//                                             * at least inform them that the video is over.
//                                             */
//                                                if (mOnCompletionListener != null) {
//                                                    mOnCompletionListener.onCompletion(mMediaPlayer);
//                                                }
//                                            }
//                                        })
//                                .setCancelable(false)
//                                .show();
                        mMediaController.showErroPanel();
                    }
                    return true;
                }
            };

    int Adaptation_lastPercent = 0;

    private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener =
            new IMediaPlayer.OnBufferingUpdateListener() {
                public void onBufferingUpdate(IMediaPlayer mp, final int percent) {
                    mCurrentBufferPercentage = percent;
                }
            };

    public void setOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener l) {
        mBufferingUpdateListener = l;
    }

    /**
     * Register a callback to be invoked when the media file
     * is loaded and ready to go.
     *
     * @param l The callback that will be run
     */
    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener l) {
        mOnPreparedListener = l;
    }

    /**
     * Register a callback to be invoked when the end of a media file
     * has been reached during playback.
     *
     * @param l The callback that will be run
     */
    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener l) {
        mOnCompletionListener = l;
    }

    /**
     * Register a callback to be invoked when an error occurs
     * during playback or setup.  If no listener is specified,
     * or if the listener returned false, VideoView will inform
     * the user of any errors.
     *
     * @param l The callback that will be run
     */
    public void setOnErrorListener(IMediaPlayer.OnErrorListener l) {
        mOnErrorListener = l;
    }

    /**
     * Register a callback to be invoked when an informational event
     * occurs during playback or setup.
     *
     * @param l The callback that will be run
     */
    public void setOnInfoListener(IMediaPlayer.OnInfoListener l) {
        mOnInfoListener = l;
    }

    // REMOVED: mSHCallback
    private void bindSurfaceHolder(IMediaPlayer mp, IRenderView.ISurfaceHolder holder) {
        if (mp == null)
            return;

        if (holder == null) {
            mp.setDisplay(null);
            return;
        }

        holder.bindToMediaPlayer(mp);
    }

    IRenderView.IRenderCallback mSHCallback = new IRenderView.IRenderCallback() {
        @Override
        public void onSurfaceChanged(@NonNull IRenderView.ISurfaceHolder holder, int format, int w, int h) {
            if (holder == null || holder.getRenderView() != mRenderView) {
                return;
            }

            mSurfaceWidth = w;
            mSurfaceHeight = h;
            boolean isValidState = (mTargetState == STATE_PLAYING);
            boolean hasValidSize = !mRenderView.shouldWaitForResize() || (mVideoWidth == w && mVideoHeight == h);
            if (mMediaPlayer != null && isValidState && hasValidSize) {
                if (mSeekWhenPrepared != 0) {
                    seekTo(mSeekWhenPrepared);
                }
                onStart();
            }
        }


        @Override
        public void onSurfaceCreated(@NonNull IRenderView.ISurfaceHolder holder, int width, int height) {
            if (holder == null || holder.getRenderView() != mRenderView) {
                return;
            }
            mSurfaceHolder = holder;
            if (mMediaPlayer != null)
                bindSurfaceHolder(mMediaPlayer, holder);
            else
                openVideo();
        }

        @Override
        public void onSurfaceDestroyed(@NonNull IRenderView.ISurfaceHolder holder) {
            if (holder == null && holder.getRenderView() != mRenderView) {
                return;
            }

            // after we return from this we can't use the surface any more
            mSurfaceHolder = null;
            // REMOVED: if (mMediaController != null) mMediaController.hide();
            // REMOVED: release(true);
            releaseWithoutStop();
        }
    };

    public void releaseWithoutStop() {
        if (mMediaPlayer != null)
            mMediaPlayer.setDisplay(null);
    }

    /*
     * release the media player in any state
     */
    public void release(boolean cleartargetstate) {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            // REMOVED: mPendingSubtitleTracks.clear();
            mCurrentState = STATE_IDLE;
            if (cleartargetstate) {
                mTargetState = STATE_IDLE;
            }
            AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
            am.abandonAudioFocus(null);
        }

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN:
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_BACK://监听返回键，处理全屏时返回监听
                        break;
                    case KeyEvent.KEYCODE_HOME://监听不到
                        break;
                }
                break;
        }
        return super.dispatchKeyEvent(event);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isKeyCodeSupported = keyCode != KeyEvent.KEYCODE_BACK &&
                keyCode != KeyEvent.KEYCODE_VOLUME_UP &&
                keyCode != KeyEvent.KEYCODE_VOLUME_DOWN &&
                keyCode != KeyEvent.KEYCODE_VOLUME_MUTE &&
                keyCode != KeyEvent.KEYCODE_MENU &&
                keyCode != KeyEvent.KEYCODE_CALL &&
                keyCode != KeyEvent.KEYCODE_ENDCALL;
        if (isInPlaybackState() && isKeyCodeSupported && mMediaController != null) {
            if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK ||
                    keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                if (mMediaPlayer.isPlaying()) {
                    onPause();
                    mMediaController.show();
                } else {
                    onStart();
                    mMediaController.hide();
                }
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
                if (!mMediaPlayer.isPlaying()) {
                    onStart();
                    mMediaController.hide();
                }
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                    || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
                if (mMediaPlayer.isPlaying()) {
                    onPause();
                    mMediaController.show();
                }
                return true;
            } else {
                toggleMediaControlsVisiblity();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private void toggleMediaControlsVisiblity() {
        if (mMediaController.isShowing()) {
            mMediaController.hide();
        } else {
            mMediaController.show();
        }
    }


    public void suspend() {
        release(false);
    }

    public void resume() {
        openVideo();
    }

    @Override
    public boolean isPlaying() {
        return isInPlaybackState() && mMediaPlayer.isPlaying();
    }

    @Override
    public boolean isPlayComplete() {
        return mCurrentState == STATE_PLAYBACK_COMPLETED || mTargetState == STATE_PLAYBACK_COMPLETED;
    }


    @Override
    public void onStart() {
        if (isInPlaybackState()) {
            mMediaPlayer.start();
            mCurrentState = STATE_PLAYING;
        }
        mTargetState = STATE_PLAYING;
        if (onReStartListener != null) {
            onReStartListener.onReStart();
        }
    }

    @Override
    public void onPause() {
        if (isInPlaybackState()) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                mCurrentState = STATE_PAUSED;
            }
        }
        mTargetState = STATE_PAUSED;
        if (onPauseListener != null) {
            onPauseListener.onPause();
        }

    }

    @Override
    public void onRePlay() {

    }

    @Override
    public void onComplete() {
        initRenders();
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void setDisableOrientation() {
        if (orientationHelper != null)
            orientationHelper.disableListener();
    }

    @Override
    public void setEnableOrientation() {
        if (orientationHelper != null)
            orientationHelper.enableListener();
    }

    @Override
    public void setOnPauseListener(IMediaPlayerObserver.onPauseListener listener) {
        this.onPauseListener = listener;
    }

    IMediaPlayerObserver.onPauseListener onPauseListener;

    @Override
    public void setOnReStartListener(onReStartListener listener) {
        this.onReStartListener = listener;
    }

    IMediaPlayerObserver.onReStartListener onReStartListener;

    @Override
    public boolean isFullScreen() {
        return mMediaPlayerDelegate.isFullScreen;
    }

    @Override
    public void goFullScreen() {
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        if (orientationHelper != null) {
            orientationHelper.enableListener();
        }
        fullWidth = mActivity.getWindowManager().getDefaultDisplay().getWidth();
        mActivity.closeOptionsMenu();
        setPlayerFullScreen(false);
        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                onConfigrationChange();
            }
        });
        if (orientationHelper != null) {
            orientationHelper.fromUser = true;
        }
    }

    @Override
    public void goSmallScreen() {
        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);//坚屏
        setPlayerSmallScreen(true);
        if (orientationHelper != null) {
            orientationHelper.fromUser = false;
        }
    }

    @Override
    public int getDuration() {
        if (isInPlaybackState()) {
            return (int) mMediaPlayer.getDuration();
        }

        return -1;
    }

    @Override
    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return (int) mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(int msec) {
        if (isInPlaybackState()) {
            mMediaPlayer.seekTo(msec);
            mSeekWhenPrepared = 0;
        } else {
            mSeekWhenPrepared = msec;
        }
    }

    @Override
    public int getBufferPercentage() {
        if (mMediaPlayer != null) {
            return mCurrentBufferPercentage;
        }
        return 0;
    }

    private boolean isInPlaybackState() {
        return (mMediaPlayer != null &&
                mCurrentState != STATE_ERROR &&
                mCurrentState != STATE_IDLE &&
                mCurrentState != STATE_PREPARING);
    }

    @Override
    public boolean canPause() {
        return mCanPause;
    }

    @Override
    public boolean canSeekBackward() {
        return mCanSeekBack;
    }

    @Override
    public boolean canSeekForward() {
        return mCanSeekForward;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    // REMOVED: getAudioSessionId();
    // REMOVED: onAttachedToWindow();
    // REMOVED: onDetachedFromWindow();
    // REMOVED: onLayout();
    // REMOVED: draw();
    // REMOVED: measureAndLayoutSubtitleWidget();
    // REMOVED: setSubtitleWidget();
    // REMOVED: getSubtitleLooper();

    //-------------------------
    // Extend: Aspect Ratio
    //-------------------------

    private static final int[] s_allAspectRatio = {
            IRenderView.AR_ASPECT_FIT_PARENT,
            IRenderView.AR_ASPECT_FILL_PARENT,
            IRenderView.AR_ASPECT_WRAP_CONTENT,
            // IRenderView.AR_MATCH_PARENT,
            IRenderView.AR_16_9_FIT_PARENT,
            IRenderView.AR_4_3_FIT_PARENT};
    private int mCurrentAspectRatioIndex = 0;
    private int mCurrentAspectRatio = s_allAspectRatio[0];


    public int toggleAspectRatio() {
        mCurrentAspectRatioIndex++;
        mCurrentAspectRatioIndex %= s_allAspectRatio.length;

        mCurrentAspectRatio = s_allAspectRatio[mCurrentAspectRatioIndex];
        if (mRenderView != null)
            mRenderView.setAspectRatio(mCurrentAspectRatio);
        return mCurrentAspectRatio;
    }

    //-------------------------
    // Extend: Player
    //-------------------------
    public int togglePlayer() {
        if (mMediaPlayer != null)
            mMediaPlayer.release();

        mRenderView.getView().invalidate();
        openVideo();
        return mSettings.getPlayer();
    }

    //-------------------------
    // Extend: Background
    //-------------------------

    private boolean mEnableBackgroundPlay = false;

    private void initBackground() {
        mEnableBackgroundPlay = mSettings.getEnableBackgroundPlay();
        if (mEnableBackgroundPlay) {
            MediaPlayerService.intentToStart(getContext());
            mMediaPlayer = MediaPlayerService.getMediaPlayer();
        }
    }

    public boolean isBackgroundPlayEnabled() {
        return mEnableBackgroundPlay;
    }

    public void enterBackground() {
        MediaPlayerService.setMediaPlayer(mMediaPlayer);
    }

    public void stopBackgroundPlay() {
        MediaPlayerService.setMediaPlayer(null);
    }


    /**
     * YoukuBasePlayerManager 横坚屏管理
     */
    @Override
    public void land2Port() {
        if (!PreferenceUtil.getPreferenceBoolean(mActivity, "video_lock", false)) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }

    @Override
    public void port2Land() {
        if (null != orientationHelper) {
            orientationHelper.disableListener();
        }
        if (PreferenceUtil.getPreferenceBoolean(mActivity, "video_lock", false)) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            layoutHandler.removeCallbacksAndMessages(null);
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
    }

    @Override
    public void reverseLand() {
        if (null != orientationHelper) {
            orientationHelper.disableListener();
        }
        if (!PreferenceUtil.getPreferenceBoolean(mActivity, "video_lock", false)) {
            layoutHandler.removeCallbacksAndMessages(null);
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }
    }

    @Override
    public void reversePort() {
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onFullScreenPlayComplete() {
        if (null != orientationHelper) {
            orientationHelper.disableListener();
        }
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private boolean isLand() {
        Display getOrient = mActivity.getWindowManager().getDefaultDisplay();
        return getOrient.getWidth() > getOrient.getHeight();
    }

    /**
     * view-controller-vertical
     */
    public void setSmallScreenLayout() {
        if (smallScreenLayoutParams == null) {
            throw new IllegalArgumentException(
                    "Small screen LayoutParams does not be set. Do you forget call setSmallScreenLayoutParams(ViewGroup.LayoutParams)? ");
        }
        this.setLayoutParams(smallScreenLayoutParams);
        playback.isFullscreen = false;
    }

    /**
     * view-controller-fullscreen
     */
    public void setFullscreenLayout() {
        if (fullScreenLayoutParams == null) {
            throw new IllegalArgumentException(
                    "Full screen LayoutParams does not be set. Do you forget call setFullScreenLayoutParams(ViewGroup.LayoutParams)? ");
        }
        this.setLayoutParams(fullScreenLayoutParams);
        playback.isFullscreen = true;
        if (AndroidSDK.isMarshmallow()) {
            this.setBackgroundColor(getResources().getColor(R.color.black, null));
        } else {
            this.setBackgroundColor(getResources().getColor(R.color.black));
        }
    }

    private ViewTreeObserver vto;

    /**
     * 当播放器尺寸变化时候调用
     */
    public void onConfigrationChange() {
        vto = playback.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(mGlobalLayoutListener);
    }

    ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            resizeMediaPlayer(false);
        }
    };

    private ViewGroup.LayoutParams smallScreenLayoutParams = null;

    /**
     * 当切换到竖屏时，会调用这个params来设置view的布局
     *
     * @param params 全屏时候的布局
     */
    public void setSmallScreenLayoutParams(ViewGroup.LayoutParams params) {
        smallScreenLayoutParams = params;
    }

    private ViewGroup.LayoutParams fullScreenLayoutParams = null;

    /**
     * 当切换到全屏时，会调用这个params来设置view的布局
     *
     * @param params 全屏时候的布局
     */
    public void setFullScreenLayoutParams(ViewGroup.LayoutParams params) {
        fullScreenLayoutParams = params;
    }

    public void setPlayerFullScreen(final boolean lockSensor) {
        if (mMediaController.isShowing() && !mMediaController.isShowRightPanel()) {
            mMediaController.showRightPanel();
        }
        if (mMediaPlayerDelegate != null)
            mMediaPlayerDelegate.isFullScreen = true;//TODO 待调

        if (lockSensor) {
            if (!PreferenceUtil.getPreferenceBoolean(mActivity, "video_lock", false)) {//不用加上sdk小于10的条件
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            } else {
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }

        mMediaPlayerDelegate.onChangeOrient = true;// 重影绘制

        Message message = layoutHandler.obtainMessage();
        message.what = GET_LAND_PARAMS;
        message.obj = true;
        layoutHandler.sendMessage(message);

        setFullscreenLayout();

        ((YoumiMediaController) mMediaController).registerBatteryTime();
    }

    private void setPlayerSmallScreen(boolean setRequsetOreitation) {
        if (mMediaController.isShowRightPanel()) {
            mMediaController.hideRightPanel();
        }
        if (mMediaPlayerDelegate != null)
            mMediaPlayerDelegate.isFullScreen = false;

        mMediaPlayerDelegate.onChangeOrient = false;// 重影绘制
        if (playerMediaControllerPanel != null) {
            playerMediaControllerPanel.setPadding(0, 0, 0, 0);
        }

        if (mMediaPlayerDelegate != null && !mMediaPlayerDelegate.isComplete) {
            orientationHelper.enableListener();
            orientationHelper.isFromUser();
        }
        Message message = layoutHandler.obtainMessage();
        message.what = GET_PORT_PARAMS;
        message.obj = setRequsetOreitation;

        layoutHandler.sendMessage(message);

        setSmallScreenLayout();

        ((YoumiMediaController) mMediaController).unregisterBatteryTime();
    }


    public void changeConfiguration(Configuration newConfig) {
        if (isLand()) {
            fullWidth = mActivity.getWindowManager().getDefaultDisplay().getWidth();
            mActivity.closeOptionsMenu();
            setPlayerFullScreen(false);
            mActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    onConfigrationChange();
                }
            });
            if (orientationHelper != null) {
                orientationHelper.fromUser = true;
            }
        } else {
            setPlayerSmallScreen(true);
            if (orientationHelper != null) {
                orientationHelper.fromUser = false;
            }
        }
    }

    public void changeConfiguration(boolean fullsceen) {
        setPlayerFullScreen(fullsceen);
    }

    private final int GET_LAND_PARAMS = 800;
    private final int GET_PORT_PARAMS = 801;
    private Handler layoutHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 横屏
                case GET_LAND_PARAMS: {
//                    relayout();
//                    invalidate();
//                    yp_player_container.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    hideNavBar();
                    mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    resizeMediaPlayer(false);
                    changeConfiguration(new Configuration());
                    WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();//此方法不存在状态样高度问题
                    lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    mActivity.getWindow().setAttributes(lp);
                    mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    if (null != mMediaPlayerDelegate) {
                        mMediaPlayerDelegate.currentOriention = Orientation.LAND;
                    }
                    break;
                }
                // 竖屏
                case GET_PORT_PARAMS: {
                    showNavBar();
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                    WindowManager.LayoutParams attr = mActivity.getWindow().getAttributes();//此方法不存在状态样高度问题
                    attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    mActivity.getWindow().setAttributes(attr);
                    mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

                    if (null != mMediaPlayerDelegate) {
                        mMediaPlayerDelegate.currentOriention = Orientation.VERTICAL;
                    }
                    break;
                }
            }
            if (mMediaPlayerDelegate.isFullScreen) {
                mVideoFrameLayout.setBackgroundResource(android.R.color.black);
            } else {
                mVideoFrameLayout.setBackgroundResource(android.R.color.white);
            }
        }
    };


    private boolean hasVirtualButtonBar(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && !ViewConfiguration.get(context).hasPermanentMenuKey();
    }

    private void hideNavBar() {
        if (AndroidSDK.isKK()) {
            View decorView = mActivity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void showNavBar() {
        if (AndroidSDK.isKK()) {
            addLeftAndRight();
            View decorView = mActivity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(0);
        }
    }
}
