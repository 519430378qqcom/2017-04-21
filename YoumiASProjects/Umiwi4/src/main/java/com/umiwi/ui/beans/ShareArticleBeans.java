package com.umiwi.ui.beans;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 轮播活动分享
 * 
 * @author tangxiong
 * @version 2014年8月19日 下午5:18:54
 */
public class ShareArticleBeans extends BaseGsonBeans {

	public static ShareArticleBeans fromJson(String json) {
		return new Gson().fromJson(json, ShareArticleBeans.class);
	}

	public static class ShareArticleBeansRequestData {

		@SerializedName("sharetype")
		private String sharetype;

		@SerializedName("sharecontent")
		private String sharecontent;

		@SerializedName("shareimage")
		private String shareimage;

		@SerializedName("shareurl")
		private String shareurl;

		@SerializedName("sharebutton")
		private String sharebutton;

		public String getSharetype() {
			return sharetype;
		}

		public String getSharecontent() {
			return sharecontent;
		}

		public String getShareimage() {
			return shareimage;
		}

		public String getShareurl() {
			return shareurl;
		}

		public String getSharebutton() {
			return sharebutton;
		}

	}

}
