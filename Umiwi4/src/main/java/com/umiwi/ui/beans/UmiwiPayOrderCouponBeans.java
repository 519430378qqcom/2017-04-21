package com.umiwi.ui.beans;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * 订单-用于获取种类优惠类型的字段：优惠券、限时、打卡...
 * 
 * @author tangxiyong 2014-3-14下午6:22:11
 * 
 */
public class UmiwiPayOrderCouponBeans implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7022145997677694593L;
	
	@SerializedName("id")
	private String id;
	@SerializedName("name")
	private String name;
	@SerializedName("desc")
	private String desc;
	@SerializedName("money")
	private String money;
	@SerializedName("offset")
	private String offset;

	@SerializedName("type")
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
