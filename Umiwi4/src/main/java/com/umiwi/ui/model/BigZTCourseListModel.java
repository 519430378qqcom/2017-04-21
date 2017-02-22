package com.umiwi.ui.model;

import cn.youmi.framework.model.BaseModel;

import com.google.gson.annotations.SerializedName;

/**
 * @author tangxiyong
 * @version 2015-6-11 下午4:03:24
 */
public class BigZTCourseListModel extends BaseModel {

	@SerializedName("id")
	public String id;

	@SerializedName("title")
	public String title;

	@SerializedName("grade")
	public String grade;

	@SerializedName("classes")
	public String classes;

	@SerializedName("image")
	public String image;

	@SerializedName("authorname")
	public String authorname;

	@SerializedName("detailurl")
	public String detailurl;

	@SerializedName("price")
	public String price;
}
