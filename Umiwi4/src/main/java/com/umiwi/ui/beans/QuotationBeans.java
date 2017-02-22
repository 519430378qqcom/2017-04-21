package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 语录
 * 
 * @author D_HANDSOME
 * 
 */
public class QuotationBeans extends BaseGsonBeans {

	@SerializedName("sayinglistid")
	private String sayinglistid;

	@SerializedName("author")
	private String author;

	@SerializedName("title")
	private String title;

	@SerializedName("catname")
	private String catname;

	@SerializedName("created")
	private String created;

	public String getSayinglistid() {
		return sayinglistid;
	}

	public void setSayinglistid(String sayinglistid) {
		this.sayinglistid = sayinglistid;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCatname() {
		return catname;
	}

	public void setCatname(String catname) {
		this.catname = catname;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public static QuotationBeans fromJson(String json) {
		return new Gson().fromJson(json, QuotationBeans.class);
	}

	public static class QuotationBeansRequestData {

		@SerializedName("data")
		private ArrayList<QuotationBeans> data;

		public ArrayList<QuotationBeans> getData() {
			return data;
		}

	}
}
