package com.umiwi.ui.beans;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 添加收藏
 * 
 * @author tangxiyong 2014-2-15下午7:37:03
 * 
 */
public class AddFavBeans extends BaseGsonBeans {
	public static AddFavBeans fromJson(String json) {
		return new Gson().fromJson(json, AddFavBeans.class);
	}

	public static class AddFavBeansRequestData {

		@SerializedName("status")
		private String status;

		@SerializedName("msg")
		private String msg;

		public String getStatus() {
			return status;
		}

		public String getMsg() {
			return msg;
		}

	}
}
