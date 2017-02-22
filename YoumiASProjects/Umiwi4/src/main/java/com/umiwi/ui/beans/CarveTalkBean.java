package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 首页的创业谈谈，淘宝故事bean
 */
public class CarveTalkBean extends BaseGsonBeans {

	@SerializedName("coursetitle")
	private String coursetitle;
	@SerializedName("detailurl")
	private String detailurl;
	@SerializedName("image")
	private String image;
	@SerializedName("subtitle")
	private String subtitle;
	@SerializedName("smalltitle")
	private String smalltitle;

	public String getCoursetitle() {
		return coursetitle;
	}

	public void setCoursetitle(String coursetitle) {
		this.coursetitle = coursetitle;
	}

	public String getDetailurl() {
		return detailurl;
	}

	public void setDetailurl(String detailurl) {
		this.detailurl = detailurl;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getSmalltitle() {
		return smalltitle;
	}

	public void setSmalltitle(String smalltitle) {
		this.smalltitle = smalltitle;
	}

	public static CarveTalkBean fromJson(String json) {
		return new Gson().fromJson(json, CarveTalkBean.class);
	}

	public static class CarveTalkBeanWapper {

		@SerializedName("type")
		private String type;
		@SerializedName("maintitle")
		private String maintitle;
		@SerializedName("mainsubtitle")
		private String mainsubtitle;
		@SerializedName("content")
		private ArrayList<CarveTalkBean> carvetalk;

		public String getType() {
			return type;
		}

		public ArrayList<CarveTalkBean> getCarvetalk() {
			return carvetalk;
		}

		public void setCarvetalk(ArrayList<CarveTalkBean> carvetalk) {
			this.carvetalk = carvetalk;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getMaintitle() {
			return maintitle;
		}

		public void setMaintitle(String maintitle) {
			this.maintitle = maintitle;
		}

		public String getMainsubtitle() {
			return mainsubtitle;
		}

		public void setMainsubtitle(String mainsubtitle) {
			this.mainsubtitle = mainsubtitle;
		}

	}

	public static class CarveTalkBeanRequestData {
		@SerializedName("record")
		private ArrayList<CarveTalkBeanWapper> record;

		public ArrayList<CarveTalkBeanWapper> getRecord() {
			return record;
		}

		public void setRecord(ArrayList<CarveTalkBeanWapper> record) {
			this.record = record;
		}
	}

}
