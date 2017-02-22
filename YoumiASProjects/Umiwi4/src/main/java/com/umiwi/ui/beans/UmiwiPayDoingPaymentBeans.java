package com.umiwi.ui.beans;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * 支付界面 第三方支付、银联
 * 
 * @author tangxiyong 2014-3-17上午10:20:47
 * 
 */
public class UmiwiPayDoingPaymentBeans implements Serializable{

	@SerializedName("channels")
	private String channels;
	@SerializedName("method")
	private String method;
	@SerializedName("name")
	private String name;
	@SerializedName("icon")
	private String icon;
	@SerializedName("desc")
	private String desc;
	@SerializedName("url")
	private String url;

	public String getChannels() {
		return channels;
	}

	public void setChannels(String channels) {
		this.channels = channels;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
