package com.umiwi.ui.beans;

import com.google.gson.annotations.SerializedName;

/**
 * 摇一摇优惠券摇奖详情
 *
 * @author tjie00
 *         2014/01/15 13:30
 */
public class UmiwiShakeBean extends BaseGsonBeans {

    @SerializedName("number")
    private String num;

    @SerializedName("lotteryurl")
    private String lotteryurl;

    @SerializedName("status")
    private String status;

    @SerializedName("record")
    private String record;

    @SerializedName("resulttype")
    private String resulttype;

    @SerializedName("resulturl")
    private String resulturl;

    @SerializedName("lotteryuserid")
    private String lotteryUserId;


    public String getNum() {
        return num;
    }

    public String getLotteryurl() {
        return lotteryurl;
    }

    public String getStatus() {
        return status;
    }

    public String getRecord() {
        return record;
    }

    public String getResulttype() {
        return resulttype;
    }

    public String getResulturl() {
        return resulturl;
    }

    public String getLotteryUserId() {
        return lotteryUserId;
    }
}
