package com.umiwi.ui.beans;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 我摇到的
 *
 * @author tjie00
 *         2014/01/15 13:30
 */
public class MineShakeCouponBean extends BaseGsonBeans {

    @SerializedName("image")
    private String image;

    @SerializedName("type")
    private String type;

    @SerializedName("status")
    private String status;

    @SerializedName("albumid")
    private String albumid;

    @SerializedName("detailurl")
    private String detailurl;

    @SerializedName("menpiaourl")
    private String menpiaourl;

    @SerializedName("total")
    private int total;

    @SerializedName("totals")
    private int totals;

    /**
     * 当前页
     */
    @SerializedName("curr_page")
    private int curr_page;

    /**
     * 总页数
     */
    @SerializedName("pages")
    private int pages;

    @SerializedName("record")
    private ArrayList<MineShakeCouponBean> record;

    public ArrayList<MineShakeCouponBean> getRecord() {
        return record;
    }

    public void setRecord(ArrayList<MineShakeCouponBean> record) {
        this.record = record;
    }

    @SerializedName("loginstatus")
    private int loginstatus;

    public String getMenpiaourl() {
        return menpiaourl;
    }

    public void setMenpiaourl(String menpiaourl) {
        this.menpiaourl = menpiaourl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }

    public String getDetailurl() {
        return detailurl;
    }

    public void setDetailurl(String detailurl) {
        this.detailurl = detailurl;
    }


    public int getLoginstatus() {
        return loginstatus;
    }

    public void setLoginstatus(int loginstatus) {
        this.loginstatus = loginstatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MineShakeCouponBean fromJson(String json) {
        return new Gson().fromJson(json, MineShakeCouponBean.class);
    }

    // 优惠券
    public static final String KEY_TYPE_COUPON = "1";
    // 门票
    public static final String KEY_TYPE_HUODONG = "2";
    // 推荐好课
    public static final String KEY_TYPE_COURSE = "3";
    // 神秘大礼包
    public static final String KEY_TYPE_GIFT = "4";
    // 赠书
    public static final String KEY_TYPE_BOOK = "5";
    // 内幕爆料
    public static final String KEY_TYPE_LEAKS = "6";
    // 免费课程
    public static final String KEY_TYPE_COURSEFREE = "7";

    // 优惠券
    public static final int TYPE_COUPON = 1;
    // 门票
    public static final int TYPE_HUODONG = 2;
    // 推荐好课
    public static final int TYPE_COURSE = 3;
    // 神秘大礼包
    public static final int TYPE_GIFT = 4;
    // 赠书
    public static final int TYPE_BOOK = 5;
    // 内幕爆料
    public static final int TYPE_LEAKS = 6;
    // 免费课程
    public static final int TYPE_COURSEFREE = 7;

}
