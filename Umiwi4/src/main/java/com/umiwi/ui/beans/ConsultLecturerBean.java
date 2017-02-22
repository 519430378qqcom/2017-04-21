package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class ConsultLecturerBean extends BaseGsonBeans{
	
	@SerializedName("isonline")
	private boolean isonline;
	public boolean isIsonline() {
		return isonline;
	}

	public void setIsonline(boolean isonline) {
		this.isonline = isonline;
	}

	@SerializedName("name")
	private String name;
	
	@SerializedName("title")
	private String title;
	
	@SerializedName("image")
	private String image;
	
	@SerializedName("courseurl")
	private String courseurl;
	
	@SerializedName("tutoruid")
	private String tutoruid;
	
	@SerializedName("nickname")
	private String nickname;
	
	@SerializedName("specailty")
	private String specailty;
	
	@SerializedName("price")
	private String price;
	
	@SerializedName("isconsult")
	private boolean isconsult;
	
	@SerializedName("uid")
	private String uid;
	
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public boolean isIsconsult() {
		return isconsult;
	}

	public void setIsconsult(boolean isconsult) {
		this.isconsult = isconsult;
	}

	public static ConsultLecturerBean fromJson(String json) {
		return new Gson().fromJson(json, ConsultLecturerBean.class);
	}

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

	public String getTutoruid() {
		return tutoruid;
	}

	public void setTutoruid(String tutoruid) {
		this.tutoruid = tutoruid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSpecailty() {
		return specailty;
	}

	public void setSpecailty(String specailty) {
		this.specailty = specailty;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public static class ConsultLecturerRequestData {

		@SerializedName("totals")
		private int totals;

		@SerializedName("pages")
		private int pages;

		@SerializedName("curr_page")
		private int curr_page;
		

		@SerializedName("total")
		private int total;
		
		
		@SerializedName("record")
		private ArrayList<ConsultLecturerBean> record;
		
		public int getTotals() {
			return totals;
		}

		public int getPages() {
			return pages;
		}


		public int getCurr_page() {
			return curr_page;
		}


		public int getTotal() {
			return total;
		}


		public ArrayList<ConsultLecturerBean> getRecord() {
			return record;
		}
		
		
	}
	
}
