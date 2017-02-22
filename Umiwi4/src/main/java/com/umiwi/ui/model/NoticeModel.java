package com.umiwi.ui.model;

import com.google.gson.annotations.SerializedName;

import cn.youmi.framework.model.BaseModel;

/**
 * Created by txy on 15/11/4.
 */
public class NoticeModel extends BaseModel{
    @SerializedName("coupon")
    private String coupon;
    @SerializedName("message")
    private String message;
    @SerializedName("coin_goods")
    private String coin_goods;
    @SerializedName("activity")
    private String activity;
    @SerializedName("shake")
    private String shake;

    public String getCoupon() {
        return coupon;
    }

    public String getMessage() {
        return message;
    }

    public String getCoin_goods() {
        return coin_goods;
    }

    public String getActivity() {
        return activity;
    }

    public String getShake() {
        return shake;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCoin_goods(String coin_goods) {
        this.coin_goods = coin_goods;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setShake(String shake) {
        this.shake = shake;
    }
}
