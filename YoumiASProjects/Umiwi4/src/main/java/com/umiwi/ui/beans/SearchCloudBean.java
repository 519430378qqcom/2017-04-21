package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 搜索云标签
 * @author tjie00
 */

public class SearchCloudBean extends BaseGsonBeans {

	@SerializedName("title")
	private String title;


	@SerializedName("type")
	private String type;

	@SerializedName("detailurl")
	private String detailurl;
	
	@SerializedName("frm")
	private String frm;
	
	@SerializedName("info")
	private LecturerBean info;
	
	public LecturerBean getInfo() {
		return info;
	}

	public void setInfo(LecturerBean info) {
		this.info = info;
	}

	@SerializedName("color")
	private String color;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public static SearchCloudBean fromJson(String json) {
		return new Gson().fromJson(json, SearchCloudBean.class);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDetailurl() {
		return detailurl;
	}

	public void setDetailurl(String detailurl) {
		this.detailurl = detailurl;
	}

	public String getFrm() {
		return frm;
	}

	public void setFrm(String frm) {
		this.frm = frm;
	}

	public static class SearchCloudBeanRequestData {

		@SerializedName("total")
		private int total;

		/** 当前页 */
		@SerializedName("curr_page")
		private int curr_page;

		/** 总页数 */
		@SerializedName("pages")
		private int pages;

		@SerializedName("record")
		private ArrayList<ArrayList<SearchCloudBean>> record;

		public int getTotal() {
			return total;
		}

		public int getCurr_page() {
			return curr_page;
		}

		public int getPages() {
			return pages;
		}

		public ArrayList<ArrayList<SearchCloudBean>> getRecord() {
			return record;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public void setCurr_page(int curr_page) {
			this.curr_page = curr_page;
		}

		public void setPages(int pages) {
			this.pages = pages;
		}

		public void setRecord(ArrayList<ArrayList<SearchCloudBean>> record) {
			this.record = record;
		}

	}
}
