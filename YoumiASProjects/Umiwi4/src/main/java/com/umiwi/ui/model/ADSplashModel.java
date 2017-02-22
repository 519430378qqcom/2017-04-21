package com.umiwi.ui.model;

import com.google.gson.annotations.SerializedName;

import cn.youmi.framework.model.BaseModel;

/**
 * Created by txy on 15/11/3.
 */
public class ADSplashModel extends BaseModel {

    @SerializedName("img")
    private String imageUrl;
    @SerializedName("url")
    private String detailUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDetailUrl() {
        return detailUrl;
    }
}
