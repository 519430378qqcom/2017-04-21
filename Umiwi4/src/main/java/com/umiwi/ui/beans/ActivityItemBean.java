package com.umiwi.ui.beans;

import com.google.gson.annotations.SerializedName;

import cn.youmi.framework.model.BaseModel;

public class ActivityItemBean extends BaseModel {
    @SerializedName("title")
    private String title;

    @SerializedName("address")
    private String address;

    @SerializedName("city")
    private String city;

    @SerializedName("type")
    private String type;

    @SerializedName("joiner")
    private String joiner;

    @SerializedName("image")
    private String imageURL;

    @SerializedName("detailurl")
    private String detailURL;

    @SerializedName("authorname")
    private String authorName;

    @SerializedName("authortitle")
    private String authorTitle;

    @SerializedName("starttime")
    private String startTime;

    @SerializedName("maxperson")
    private int maxPersons;

    @SerializedName("checkeds")
    private int checkedNum;//TOOD?

    @SerializedName("total")
    private int total;

    @SerializedName("endtime")
    private String endTime;

    @SerializedName("isend")
    private boolean isEnd;

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getType() {
        return type;
    }

    public String getJoiner() {
        return joiner;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getDetailURL() {
        return detailURL;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorTitle() {
        return authorTitle;
    }

    public String getStartTime() {
        return startTime;
    }

    public int getMaxPersons() {
        return maxPersons;
    }

    public int getCheckedNum() {
        return checkedNum;
    }

    public int getTotal() {
        return total;
    }

    public String getEndTime() {
        return endTime;
    }

    public boolean isEnd() {
        return isEnd;
    }
}

