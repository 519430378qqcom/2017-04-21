package com.umiwi.ui.beans;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 广告图
 * 
 * @author tangxiyong 2014-1-16上午10:25:12
 * 
 */
public class UmiwiADBeans extends BaseGsonBeans {

	public static UmiwiADBeans fromJson(String json) {
		return new Gson().fromJson(json, UmiwiADBeans.class);
	}

	public static class UmiwiADRequestData {

		@SerializedName("code")
		private String code;

		@SerializedName("msg")
		private String msg;

		@SerializedName("device")
		private String device;

		@SerializedName("url")
		private String url;

		@SerializedName("img")
		private String img;

		public String getCode() {
			return code;
		}

		public String getMsg() {
			return msg;
		}

		public String getDevice() {
			return device;
		}

		public String getUrl() {
			return url;
		}

		public String getImg() {
			return img;
		}

	}

}
