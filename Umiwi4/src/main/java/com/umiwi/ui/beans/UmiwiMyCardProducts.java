package com.umiwi.ui.beans;

import com.google.gson.annotations.SerializedName;

/**
 * 我的兑换码兑换卡 兑换的商品详情
 *
 * @author xiaobo
 */
public class UmiwiMyCardProducts {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("grade")
    private String grade;

    @SerializedName("classes")
    private String classes;

    @SerializedName("image")
    private String image;

    @SerializedName("authorname")
    private String authorname;

    @SerializedName("detailurl")
    private String detailurl;

    public String getTitle() {
        return title;
    }

    public String getDetailurl() {
        return detailurl;
    }
}
