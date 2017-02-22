package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 笑话
 * 
 * @author tjie00
 * 
 */
public class JokeBean extends BaseGsonBeans {

	@SerializedName("id")
	private int id;

	@SerializedName("content")
	private String content;

	@SerializedName("review")
	private String review;

	public static JokeBean fromJson(String json) {
		return new Gson().fromJson(json, JokeBean.class);
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public static class JokeBeanRequestData {

		@SerializedName("e")
		private String errorcode;

		@SerializedName("m")
		private String message;


		@SerializedName("record")
		private ArrayList<JokeBean> record;

		public String getErrorcode() {
			return errorcode;
		}

		public String getMessage() {
			return message;
		}

		public ArrayList<JokeBean> getRecord() {
			return record;
		}

	}
}
