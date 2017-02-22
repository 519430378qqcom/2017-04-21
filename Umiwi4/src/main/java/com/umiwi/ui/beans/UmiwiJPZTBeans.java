package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 精品专题 一级列表
 * 
 * @author tangxiyong 2013-12-9下午2:02:12
 * 
 */
public class UmiwiJPZTBeans extends BaseGsonBeans {

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	@SerializedName("introduce")
	private String introduce;

	@SerializedName("image")
	private String image;

	@SerializedName("image2")
	private String image2;

	@SerializedName("detailurl")
	private String detailurl;

	public static UmiwiJPZTBeans fromJson(String json) {
		return new Gson().fromJson(json, UmiwiJPZTBeans.class);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public String getDetailurl() {
		return detailurl;
	}

	public void setDetailurl(String detailurl) {
		this.detailurl = detailurl;
	}

	public static class UmiwiJPZTListRequestData {

		@SerializedName("total")
		private int total;

		@SerializedName("totals")
		private int totals;

		@SerializedName("curr_page")
		private int curr_page;

		@SerializedName("pages")
		private int pages;

		@SerializedName("record")
		private ArrayList<UmiwiJPZTBeans> record;

		public int getTotal() {
			return total;
		}

		public int getTotals() {
			return totals;
		}

		public int getCurr_page() {
			return curr_page;
		}

		public int getPages() {
			return pages;
		}

		public ArrayList<UmiwiJPZTBeans> getRecord() {
			return record;
		}

	}
}
