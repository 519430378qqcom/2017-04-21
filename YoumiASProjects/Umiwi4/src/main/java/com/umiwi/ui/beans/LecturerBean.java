package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 讲师
 */
public class LecturerBean extends BaseGsonBeans {

	public static LecturerBean fromJson(String json) {
		return new Gson().fromJson(json, LecturerBean.class);
	}

	@SerializedName("name")
	private String name;

	@SerializedName("title")
	private String title;

	@SerializedName("tutoruid")
	private String tutoruid;

	@SerializedName("image")
	private String image;

	@SerializedName("courseurl")
	private String courseurl;

	@SerializedName("isconsult")
	private boolean isconsult;

	@SerializedName("uid")
	private String uid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTutoruid() {
		return tutoruid;
	}

	public void setTutoruid(String tutoruid) {
		this.tutoruid = tutoruid;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCourseurl() {
		return courseurl;
	}

	public void setCourseurl(String courseurl) {
		this.courseurl = courseurl;
	}

	public boolean isIsconsult() {
		return isconsult;
	}

	public void setIsconsult(boolean isconsult) {
		this.isconsult = isconsult;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public static class LecturerBeanWapper {

		@SerializedName("pinyinname")
		private String lastName;

		@SerializedName("pinyin")
		private String nameOrder;

		@SerializedName("content")
		private ArrayList<LecturerBean> lecturers;

		public String getLastName() {
			return lastName;
		}

		public String getNameOrder() {
			return nameOrder;
		}

		public ArrayList<LecturerBean> getLecturers() {
			return lecturers;
		}

	}

	// 讲师列表
	public static class LecturerBeanRequestData {

		@SerializedName("returnmsg")
		private String msg;

		@SerializedName("record")
		private ArrayList<LecturerBeanWapper> record;

		public String getMsg() {
			return msg;
		}

		public ArrayList<LecturerBeanWapper> getRecord() {
			return record;
		}

	}

	// 推荐讲师
	public static class LecturerBeanRecommendRequestData {

		@SerializedName("record")
		private ArrayList<LecturerBean> record;

		public ArrayList<LecturerBean> getRecord() {
			return record;
		}

	}
}
