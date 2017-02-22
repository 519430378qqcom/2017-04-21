package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 评论
 * 
 * @author tangxiong
 * @version 2014年7月28日 上午11:15:38
 */
public class CommentTagBeans extends BaseGsonBeans {

	@SerializedName("tagid")
	private String tagid;

	@SerializedName("name")
	private String name;

	@SerializedName("num")
	private String num;

	public String getTagid() {
		return tagid;
	}

	public void setTagid(String tagid) {
		this.tagid = tagid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public static CommentTagBeans fromJson(String json) {
		return new Gson().fromJson(json, CommentTagBeans.class);
	}

	public static class CommentRequestData {

		@SerializedName("record")
		private ArrayList<CommentTagBeans> record;

		public ArrayList<CommentTagBeans> getRecord() {
			return record;
		}

	}
}
