package com.umiwi.video.control;

import android.view.View;

/**
 * Created by txy on 15/10/20.
 */
public interface IMediaPlayerController  {

    void show();

    void hide();

    boolean isShowing();

    void setAnchorView(View view);

    void setEnabled(boolean enabled);

    void setMediaPlayer(IMediaPlayerObserver player);

    void show(int timeout);

    void showOnce(View view);

    boolean isOnlyFullScreen();

    void showLoading();
    void hideLoading();
    boolean isShowLoading();

    void showCustomPanel();
    void hideCustomPanel();
    boolean isShowCustomPanel();

    void showRightPanel();
    void hideRightPanel();
    boolean isShowRightPanel();

    void showMiddlePlay();
    void hideMiddlePlay();
    boolean isShowMiddlePlay();

    void showEndPanel();
    void hideEndPanel();
    boolean isShowEndPanel();
    void showErroPanel();

    void showBufferPanel();
    void hideBufferPanel();

    interface screenAndControllerHide{
        void hide();
    }

    void setScreenAndControllerHideListener(screenAndControllerHide listener);

}
