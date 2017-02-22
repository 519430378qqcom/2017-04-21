package com.umiwi.ui.beans;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 首页活动弹窗图
 * 
 * @author tangxiyong 2014-2-17下午3:10:54
 * 
 */
public class HomeADBeans extends BaseGsonBeans {
	public static HomeADBeans fromJson(String json) {
		return new Gson().fromJson(json, HomeADBeans.class);
	}

	public static class HomeADBeansRequestData {

		@SerializedName("id")
		private String id;

		@SerializedName("img")
		private String img;

		@SerializedName("width")
		private String width;

		@SerializedName("height")
		private String height;

		@SerializedName("url")
		private String url;

		@SerializedName("status")
		private String status;

		@SerializedName("type")
		private String type;

		public String getId() {
			return id;
		}

		public String getImg() {
			return img;
		}

		public String getWidth() {
			return width;
		}

		public String getHeight() {
			return height;
		}

		public String getUrl() {
			return url;
		}

		public String getStatus() {
			return status;
		}

		public String getType() {
			return type;
		}

	}
}
