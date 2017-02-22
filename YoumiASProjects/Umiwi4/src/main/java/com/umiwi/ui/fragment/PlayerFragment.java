package com.umiwi.ui.fragment;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.managers.VideoManager;
import com.umiwi.ui.model.VideoModel;
import com.umiwi.video.control.PlayerController;

import java.util.ArrayList;

import cn.youmi.framework.fragment.AbstractFragment;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class PlayerFragment extends AbstractFragment {

    public static final String KEY_VIDEO = "key.video";
    FrameLayout player_container;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();//此方法不存在状态样高度问题
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getActivity().getWindow().setAttributes(lp);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        PlayerController.getInstance().setPlayerFuller();
        PlayerController.getInstance().initPlayerController(getActivity());
        PlayerController.getInstance().initNativeMediaPlayer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_player, null);

        player_container = (FrameLayout) view.findViewById(R.id.player_container);

        VideoModel videoModel = (VideoModel) getActivity().getIntent().getSerializableExtra(KEY_VIDEO);
        ArrayList<VideoModel> videos = VideoManager.getInstance().getVideosByAlbumId(videoModel.getAlbumId());

        PlayerController.getInstance().setOnCompletionListener(mCompletionListener);
        PlayerController.getInstance().addPlayerViewToWindows(player_container);
        PlayerController.getInstance().setPlayList(videos);
        PlayerController.getInstance().setDataSource(videoModel);
        PlayerController.getInstance().prepareAndStart();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        return view;
    }

    IMediaPlayer.OnCompletionListener mCompletionListener = new IMediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(IMediaPlayer mp) {
            if (PlayerController.getInstance().isPlayNext()) {
                PlayerController.getInstance().stop();
                PlayerController.getInstance().onComplete();
                PlayerController.getInstance().showPrepareLoading();
                PlayerController.getInstance().prepareAndStart();
            } else {
                getActivity().finish();
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }

    @Override
    public void onResume() {
        super.onResume();
        PlayerController.getInstance().registerAllListener();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        MobclickAgent.onPageStart(fragmentName);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        MobclickAgent.onPageEnd(fragmentName);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PlayerController.getInstance().unregisterAllListener();
        PlayerController.getInstance().cancelOkHttpClien();
        PlayerController.getInstance().stop();
        PlayerController.getInstance().releaseAndStop();
        PlayerController.getInstance().releaseNativeMediaPlayer();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        PlayerController.getInstance().initConfigrationChange(newConfig);
    }

    @Override
    protected void onLazyLoad() {

    }
}
