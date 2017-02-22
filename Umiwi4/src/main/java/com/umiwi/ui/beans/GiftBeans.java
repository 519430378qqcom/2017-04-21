package com.umiwi.ui.beans;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 优惠大礼包
 * 
 * @author tangxiong
 * @version 2014年9月16日 下午3:07:51
 */
public class GiftBeans extends BaseGsonBeans {

	public static GiftBeans fromJson(String json) {
		return new Gson().fromJson(json, GiftBeans.class);
	}

	public static class GiftRequestData {
		@SerializedName("imagebackground")
		private String imagebackground;

		@SerializedName("imagebox")
		private String imagebox;

		@SerializedName("imagegift")
		private String imagegift;

		@SerializedName("imagecommit")
		private String imagecommit;

		@SerializedName("status")
		private boolean status;

		@SerializedName("url")
		private String giftUrl;

		public String getImagebackground() {
			return imagebackground;
		}

		public String getImagebox() {
			return imagebox;
		}

		public String getImagegift() {
			return imagegift;
		}

		public String getImagecommit() {
			return imagecommit;
		}

		public boolean isStatus() {
			return status;
		}

		public String getGiftUrl() {
			return giftUrl;
		}

	}
}
