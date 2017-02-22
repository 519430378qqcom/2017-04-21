package com.umiwi.ui.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by txy on 15/10/30.
 */
public class MineMessageModel {

    @SerializedName("avatar")
    private String avatar;
    @SerializedName("username")
    private String username;
    @SerializedName("ctime")
    private String ctime;
    @SerializedName("content")
    private String content;

    public String getAvatar() {
        return avatar;
    }

    public String getUsername() {
        return username;
    }

    public String getCtime() {
        return ctime;
    }

    public String getContent() {
        return content;
    }
}
