package com.umiwi.ui.beans;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 搜索结果
 * 
 * @author tjie00 
 * 
 */
public class SearchBean extends BaseGsonBeans {

	@SerializedName("title")
	private String title;

	@SerializedName("id")
	private long id;

	@SerializedName("image")
	private String image;

	@SerializedName("grade")
	private String grade;

	@SerializedName("authorname")
	private String authorname;

	@SerializedName("classes")
	private String classes;

	@SerializedName("detailurl")
	private String detailurl;


	@SerializedName("watchnum")
	private String watchnum;
	
	public SearchBean() {
		
	}
	
	public SearchBean(String title, String url) {
		this.title = title;
		this.detailurl = url;
	}

	public static SearchBean fromJson(String json) {
		return new Gson().fromJson(json, SearchBean.class);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getAuthorname() {
		return authorname;
	}

	public void setAuthorname(String authorname) {
		this.authorname = authorname;
	}

	public String getDetailurl() {
		return detailurl;
	}

	public void setDetailurl(String detailurl) {
		this.detailurl = detailurl;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}


	public String getWatchnum() {
		return watchnum;
	}

	public void setWatchnum(String watchnum) {
		this.watchnum = watchnum;
	}


	public static class SearchBeanRequestData {

		@SerializedName("total")
		private int total;

		@SerializedName("totals")
		private int totals;

		/** 当前页 */
		@SerializedName("curr_page")
		private int curr_page;

		/** 总页数 */
		@SerializedName("pages")
		private int pages;

		@SerializedName("issearch")
		private int issearch;

		@SerializedName("record")
		private ArrayList<SearchBean> record;

		@SerializedName("tags")
		private List<String> tags;

		
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


		public ArrayList<SearchBean> getRecord() {
			return record;
		}

		public int getIssearch() {
			return issearch;
		}

		public List<String> getTags() {
			return tags;
		}

	}
}
