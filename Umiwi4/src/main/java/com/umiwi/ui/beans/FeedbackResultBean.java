package com.umiwi.ui.beans;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 意见反馈结果
 * 
 * @author tjie00 2014-06-16
 * 
 */
public class FeedbackResultBean extends BaseGsonBeans {
	public static FeedbackResultBean fromJson(String json) {
		return new Gson().fromJson(json, FeedbackResultBean.class);
	}

	public static class FeedbackResultRequestData {

		@SerializedName("code")
		private String code;

		@SerializedName("msg")
		private String msg;
		
		@SerializedName("idkey")
		private String idkey;

		public String getCode() {
			return code;
		}

		public String getMsg() {
			return msg;
		}

		public String getIdkey() {
			return idkey;
		}
		
	}
}
