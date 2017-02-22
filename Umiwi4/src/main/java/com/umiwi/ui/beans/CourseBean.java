package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 讲师课程列表
 * 
 * @author tjie00
 * 
 */
public class CourseBean extends BaseGsonBeans {

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	@SerializedName("grade")
	private String grade;

	@SerializedName("classes")
	private String classes;

	@SerializedName("image")
	private String image;

	@SerializedName("authorname")
	private String authorname;
	
	@SerializedName("detailurl")
	private String detailurl;

	@SerializedName("watchnum")
	private String watchnum;
	
	public static CourseBean fromJson(String json) {
		return new Gson().fromJson(json, CourseBean.class);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public String getAuthorname() {
		return authorname;
	}

	public void setAuthorname(String authorname) {
		this.authorname = authorname;
	}

	public String getWatchnum() {
		return watchnum;
	}

	public void setWatchnum(String watchnum) {
		this.watchnum = watchnum;
	}

	public String getDetailurl() {
		return detailurl;
	}

	public void setDetailurl(String detailurl) {
		this.detailurl = detailurl;
	}

	public static class CourseBeanRequestData {

		@SerializedName("errormsg")
		private Boolean errormsg;

		@SerializedName("totals")
		private int totals;

		@SerializedName("curr_page")
		private int curr_page;

		@SerializedName("pages")
		private int pages;
		
		@SerializedName("description")
		private String description;

		@SerializedName("image")
		private String image;
		
		@SerializedName("title")
		private String title;
		
		@SerializedName("name")
		private String name;
		
		@SerializedName("uid")
		private String uid;
		
		@SerializedName("tutoruid")
		private String tutoruid;
		
		public String getTutoruid() {
			return tutoruid;
		}

		@SerializedName("isconsult")
		private boolean isconsult;
		
		@SerializedName("isbuyconsult")
		private boolean isbuyconsult;
		
		public boolean isIsbuyconsult() {
			return isbuyconsult;
		}

		@SerializedName("price")
		private String price;
		
		@SerializedName("follownum")
		private Integer follownum;
		
		@SerializedName("resolved")
		private String resolved;
		
		@SerializedName("score")
		private String score;
		
		@SerializedName("evaluatelist")
		private ArrayList<EvaluateBean> evaluatelist;
		
		public boolean isIsconsult() {
			return isconsult;
		}

		public String getPrice() {
			return price;
		}

		public Integer getFollownum() {
			return follownum;
		}

		public String getResolved() {
			return resolved;
		}

		public String getScore() {
			return score;
		}

		public ArrayList<EvaluateBean> getEvaluatelist() {
			return evaluatelist;
		}

		public String getImage() {
			return image;
		}

		public String getTitle() {
			return title;
		}

		public String getName() {
			return name;
		}

		public String getUid() {
			return uid;
		}

		@SerializedName("record")
		private ArrayList<CourseBean> record;

		public String getDescription() {
			return description;
		}

		public boolean getErrormsg() {
			return errormsg;
		}

		public int getTotals() {
			return totals;
		}

		public int getCurr_page() {
			return curr_page;
		}

		public int getPages() {
			return pages;
		}

		public ArrayList<CourseBean> getRecord() {
			return record;
		}

	}
	
	public static class GameCourseBeanRequestData {
		@SerializedName("totals")
		private int totals;
		
		@SerializedName("total")
		private int total;
		
		@SerializedName("record")
		private ArrayList<CourseBean> record;

		public int getTotals() {
			return totals;
		}

		public int getTotal() {
			return total;
		}

		public ArrayList<CourseBean> getRecord() {
			return record;
		}
	}
	
	
	// 讲师评价bean类
	public static class EvaluateBean {
		@SerializedName("score")
		private String score;
		
		@SerializedName("content")
		private String content;
		
		@SerializedName("order_id")
		private String order_id;
		
		@SerializedName("username")
		private String username;
		
		@SerializedName("avatar")
		private String avatar;
		
		@SerializedName("time")
		private String time;

		public String getScore() {
			return score;
		}

		public String getContent() {
			return content;
		}

		public String getOrder_id() {
			return order_id;
		}

		public String getUsername() {
			return username;
		}

		public String getAvatar() {
			return avatar;
		}

		public String getTime() {
			return time;
		}
	}
}
