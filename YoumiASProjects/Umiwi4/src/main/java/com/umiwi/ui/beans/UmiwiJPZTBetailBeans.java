package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 精品专题 详情页
 * 
 * @author tangxiyong 2013-12-19下午4:19:31
 * 
 */
public class UmiwiJPZTBetailBeans extends BaseGsonBeans {

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

	public static UmiwiJPZTBetailBeans fromJson(String json) {
		return new Gson().fromJson(json, UmiwiJPZTBetailBeans.class);
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

	public static class UmiwiJPZTDetailRequestData {

		@SerializedName("id")
		private int id;

		@SerializedName("title")
		private String title;

		@SerializedName("sectionid")
		private String sectionid;

		@SerializedName("price")
		private int price;

		@SerializedName("raw_price")
		private int raw_price;

		@SerializedName("discount_price")
		private int discount_price;

		@SerializedName("isbuy")
		private boolean isbuy;

		@SerializedName("introduce")
		private String introduce;

		@SerializedName("image")
		private String imageMin;

		@SerializedName("image2")
		private String imageBig;

		@SerializedName("record")
		private ArrayList<UmiwiJPZTBetailBeans> record;

		public int getId() {
			return id;
		}

		public String getSectionid() {
			return sectionid;
		}

		public int getPrice() {
			return price;
		}

		public boolean isIsbuy() {
			return isbuy;
		}

		public String getTitle() {
			return title;
		}

		public String getIntroduce() {
			return introduce;
		}

		public String getImageMin() {
			return imageMin;
		}

		public String getImageBig() {
			return imageBig;
		}

		public ArrayList<UmiwiJPZTBetailBeans> getRecord() {
			return record;
		}

		public int getRaw_price() {
			return raw_price;
		}

		public int getDiscount_price() {
			return discount_price;
		}

	}
}
