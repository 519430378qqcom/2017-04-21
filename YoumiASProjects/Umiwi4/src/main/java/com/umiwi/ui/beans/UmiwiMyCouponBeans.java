package com.umiwi.ui.beans;

import com.google.gson.annotations.SerializedName;

/**
 * 优惠券
 *
 * @author tangxiyong 2013-12-6上午11:31:09
 */
public class UmiwiMyCouponBeans extends BaseGsonBeans {

    @SerializedName("code")
    private String code;

    @SerializedName("ctime")
    private String ctime;

    @SerializedName("expiretime")
    private String expiretime;

    @SerializedName("type")
    private String type;

    @SerializedName("value")
    private String value;
    @SerializedName("value_type")
    private String valueType;

    @SerializedName("value_name")
    private String valueName;

    /**
     * 判断优惠券是否可以关闭
     */
    @SerializedName("statusnum")
    private int statusnum;
    /**
     * 优惠券状态
     */
    @SerializedName("status")
    private String status;

    @SerializedName("orderid")
    private String orderid;

    @SerializedName("closeurl")
    private String closeurl;

    @SerializedName("detailurl")
    private String detailUrl;

    @SerializedName("iswill")
    private boolean isWill;

    @SerializedName("type_number")
    private int typeNumber;

    public String getCode() {
        return code;
    }

    public String getCtime() {
        return ctime;
    }

    public String getExpiretime() {
        return expiretime;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getValueType() {
        return valueType;
    }

    public String getValueName() {
        return valueName;
    }

    public int getStatusnum() {
        return statusnum;
    }

    public String getStatus() {
        return status;
    }

    public String getOrderid() {
        return orderid;
    }

    public String getCloseurl() {
        return closeurl;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public boolean isWill() {
        return isWill;
    }

    public int getTypeNumber() {
        return typeNumber;
    }
}
