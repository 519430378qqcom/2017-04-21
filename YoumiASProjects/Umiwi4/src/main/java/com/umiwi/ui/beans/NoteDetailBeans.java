package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 我的笔记详情
 * 
 * @author tangxiong
 * @version 2014年6月16日 下午6:15:04
 */
public class NoteDetailBeans extends BaseGsonBeans {

	@SerializedName("content")
	private String content;

	@SerializedName("image")
	private String image;

	@SerializedName("ctime")
	private String ctime;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public static NoteDetailBeans fromJson(String json) {
		return new Gson().fromJson(json, NoteDetailBeans.class);
	}

	public static class NoteDetailRequestData {

		@SerializedName("record")
		private ArrayList<NoteDetailBeans> record;

		public ArrayList<NoteDetailBeans> getRecord() {
			return record;
		}

	}
}
