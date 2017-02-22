package com.umiwi.ui.model;

import com.google.gson.annotations.SerializedName;

import cn.youmi.framework.model.BaseModel;

/**
 * @author tangxiyong
 * @version 2015-6-10 上午10:52:22
 */
public class HomeRecommendCourseListModel extends BaseModel {

	@SerializedName("coursetitle")
	public String courseTitle;
	
	@SerializedName("subtitle")
	public String subtitle;
	
	@SerializedName("description")
	public String description;
	
	@SerializedName("image")
	public String image;
	
	@SerializedName("detailurl")
	public String detailUrl;

	@SerializedName("courseurl")
	public String courseUrl;
	
	@SerializedName("name")
	public String name;

	@SerializedName("tutoruid")
	public int tutorUid;
	
	@SerializedName("isconsult")
	public boolean isConsult;
	
	@SerializedName("uid")
	public int uid;
}
