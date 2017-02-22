package com.umiwi.ui.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * @author tangxiyong
 * @version 2015-5-7 下午6:27:28
 */
public class CourseListModel implements Serializable {

	@SerializedName("title")
	public String title;

	@SerializedName("id")
	public long id;

	@SerializedName("image")
	public String image;

	@SerializedName("grade")
	public int grade;

	@SerializedName("authorname")
	public String authorname;

	@SerializedName("classes")
	public String classes;

	@SerializedName("detailurl")
	public String detailurl;

	@SerializedName("notesnum")
	public String notesnum;

	@SerializedName("notelist")
	public String notelist;

	@SerializedName("favid")
	public String favid;

}
