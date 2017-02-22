package com.umiwi.ui.beans;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 一级列表解析方式 适用于排行榜
 * 
 * @author tangxiyong 2013-11-19下午5:49:44
 * 
 */
public class UserBeans extends BaseGsonBeans {
	

	public static UserBeans fromJson(String json) {
		return new Gson().fromJson(json, UserBeans.class);
	}

	public static class ChartsListRequestData {

		@SerializedName("e")
		private int e;

		@SerializedName("m")
		private String m;

		@SerializedName("r")
		private UserBeans record;

		public int getE() {
			return e;
		}

		public String getM() {
			return m;
		}

		public UserBeans getRecord() {
			return record;
		}

	}
}
