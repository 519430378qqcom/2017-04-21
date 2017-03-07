package com.umiwi.video.control;

/**
 * Created by Administrator on 2017/3/7.
 */

public interface IMusicPlayerService {
    void callPlay(String path);

    void callStop();

    void callPause();

    void callPlayOrPause();

    int callGetCurrentDuration();

    int callGetDuration();
}
