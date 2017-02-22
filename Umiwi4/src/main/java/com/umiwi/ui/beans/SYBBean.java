package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 首页的创业类别bean
 */
public class SYBBean {


	public static StageSectionBean fromJson(String json) {
		return new Gson().fromJson(json, StageSectionBean.class);
	}

	@SerializedName("title")
	private String coursetitle;

	@SerializedName("detailurl")
	private String detailurl;
	@SerializedName("courseurl")
	public String courseurl;
	@SerializedName("image")
	private String image;

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

	public static class SYBeanWapper {

		@SerializedName("type")
		private String type;
		
		@SerializedName("name")
		private String maintitle;

		
		@SerializedName("list")
		private ArrayList<SYBBean> content;

		public String getType() {
			return type;
		}
		
		public String getMaintitle() {
			return maintitle;
		}

		

		public ArrayList<SYBBean> getContent() {
			return content;
		}

	}

	public static class SYBeanRequestData {

		@SerializedName("record")
		private ArrayList<SYBeanWapper> record;

		public ArrayList<SYBeanWapper> getRecord() {
			return record;
		}

	}
}
