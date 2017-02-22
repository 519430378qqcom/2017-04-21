package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author tangxiyong 2014-4-28下午3:51:41
 * 
 */
public class LogoUserInfoBeans extends BaseGsonBeans {

	@SerializedName("value")
	private int value;

	@SerializedName("name")
	private String name;

	@SerializedName("image")
	private String image;

	@SerializedName("color")
	private String color;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public static LogoUserInfoBeans fromJson(String json) {
		return new Gson().fromJson(json, LogoUserInfoBeans.class);
	}

	public static class LogoUserInfoRequestData {

		@SerializedName("total")
		private int total;

		@SerializedName("nexturl")
		private String nexturl;

		@SerializedName("strrecord")
		private String strrecord;

		@SerializedName("record")
		private ArrayList<LogoUserInfoBeans> record;

		public int getTotal() {
			return total;
		}

		public String getNexturl() {
			return nexturl;
		}

		public String getStrrecord() {
			return strrecord;
		}

		public ArrayList<LogoUserInfoBeans> getRecord() {
			return record;
		}

	}
}
