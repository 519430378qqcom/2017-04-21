package com.umiwi.ui.beans;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

/**
 * 优惠卷列表
 * 
 * @author tangxiyong 2014-3-14下午6:25:13
 * 
 */
public class UmiwiPayOrderDiscountlistBeans implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6975648510722909289L;
	@SerializedName("name")
	private String name;
	@SerializedName("type")
	private String type;
	@SerializedName("activity")
	private String activity;
	@SerializedName("desc")
	private String desc;
	@SerializedName("offset")
	private String offset;
	@SerializedName("money")
	private String money;
	@SerializedName("couponid")
	private String couponid;

	@SerializedName("coupon")
	private ArrayList<UmiwiPayOrderCouponBeans> couponList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getCouponid() {
		return couponid;
	}

	public void setCouponid(String couponid) {
		this.couponid = couponid;
	}

	public ArrayList<UmiwiPayOrderCouponBeans> getCouponList() {
		return couponList;
	}

	public void setCouponList(ArrayList<UmiwiPayOrderCouponBeans> couponList) {
		this.couponList = couponList;
	}

}
