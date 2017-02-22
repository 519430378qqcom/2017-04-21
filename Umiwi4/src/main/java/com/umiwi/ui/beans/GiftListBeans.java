package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 优惠大礼包
 * 
 * @author tangxiong
 * @version 2014年9月16日 下午3:07:51
 */
public class GiftListBeans extends BaseGsonBeans {

	@SerializedName("url")
	private String imageUrl;

	@SerializedName("type")
	private String type;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static GiftListBeans fromJson(String json) {
		return new Gson().fromJson(json, GiftListBeans.class);
	}

	public static class GiftListRequestData {
		@SerializedName("imagelistbackground")
		private String imagelistbackground;

		@SerializedName("imageheader")
		private String imageheader;

		@SerializedName("status")
		private boolean status;

		@SerializedName("msg")
		private String msg;

		@SerializedName("giftlist")
		private ArrayList<GiftListBeans> giftlist;

		public String getImagelistbackground() {
			return imagelistbackground;
		}

		public String getImageheader() {
			return imageheader;
		}

		public boolean isStatus() {
			return status;
		}

		public String getMsg() {
			return msg;
		}

		public ArrayList<GiftListBeans> getGiftlist() {
			return giftlist;
		}

	}
}
