package com.umiwi.ui.beans;

import com.google.gson.annotations.SerializedName;

import cn.youmi.framework.model.BaseModel;

public class QRCodeBeans extends BaseModel {


    @SerializedName("type")
    private String type;

    @SerializedName("url")
    private String url;

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

}
