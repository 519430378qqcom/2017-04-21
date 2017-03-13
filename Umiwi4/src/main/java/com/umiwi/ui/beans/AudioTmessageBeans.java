package com.umiwi.ui.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/3/13.
 */

public class AudioTmessageBeans extends BaseGsonBeans {
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }
}
