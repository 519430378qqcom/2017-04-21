package com.umiwi.ui.beans;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 摇一摇门票授权bean
 * 
 * @author tjie00
 * 
 */
public class ShakeTicketBean extends BaseGsonBeans {

	@SerializedName("id")
	private String id;

	@SerializedName("status")
	private Integer status;

	@SerializedName("name")
	private String name;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("address")
	private String address;

	public static ShakeTicketBean fromJson(String json) {
		return new Gson().fromJson(json, ShakeTicketBean.class);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
