package com.umiwi.ui.beans;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;



/**
 * 优惠活动列表:
 * 		摇一摇;签到; 等活动 
 */
public class LuckyContent extends BaseGsonBeans {

	@SerializedName("name")
	private String name;
	
	@SerializedName("action")
	private String action;
	
	@SerializedName("url")
	private String url;
	
	@SerializedName("image")
	private String image;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public static LuckyContent fromJson(String json) {
		return new Gson().fromJson(json, LuckyContent.class);
	}
	
	public static class LuckyContentRequestData {
		
		
		@SerializedName("0")
		private LuckyContent logout;
		
		@SerializedName("1")
		private LuckyContent exp;
		
		@SerializedName("22")
		private LuckyContent gold;

		@SerializedName("23")
		private LuckyContent diamond;
		
		@SerializedName("20")
		private LuckyContent silver;
		
		public LuckyContent getExp() {
			return exp;
		}

		public LuckyContent getGold() {
			return gold;
		}

		public LuckyContent getDiamond() {
			return diamond;
		}

		public LuckyContent getSilver() {
			return silver;
		}

		public LuckyContent getLogout() {
			return logout;
		}
		
	}
	
}
