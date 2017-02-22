package com.umiwi.video.utils;

import android.media.MediaPlayer;


/**
 * Created by txy on 15/9/8.
 */
public abstract class MediaPlayerDelegate extends MediaPlayer {

    public MediaPlayerDelegate() {}

    public boolean isFullScreen;

    public boolean isComplete = false;

    public Orientation currentOriention;

    public boolean onChangeOrient = true;

    public abstract int getVideoOrientation();

    public abstract void changeVideoSize(int var1, int var2);

    public abstract void goFullScreen();

}
