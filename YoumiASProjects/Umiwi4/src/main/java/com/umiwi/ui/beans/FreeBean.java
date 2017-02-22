package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 限免课程
 */
public class FreeBean extends BaseGsonBeans {
	
	public static FreeBean fromJson(String json) {
		return new Gson().fromJson(json, FreeBean.class);
	}
	
	@SerializedName("id")
	private String id;
	
	@SerializedName("title")
	private String title;
	
	@SerializedName("classname")
	private String classname;
	
	@SerializedName("httitle")
	private String httitle;
	
	@SerializedName("htdetail")
	private String htdetail;

	@SerializedName("image")
	private String image;

	@SerializedName("classes")
	private String classes;
	
	@SerializedName("grade")
	private String grade;

	@SerializedName("authorname")
	private String authorname;

	@SerializedName("detailurl")
	private String detailurl;
	
	@SerializedName("progress")
	private String progress;
	
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

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getHttitle() {
		return httitle;
	}

	public void setHttitle(String httitle) {
		this.httitle = httitle;
	}

	public String getHtdetail() {
		return htdetail;
	}

	public void setHtdetail(String htdetail) {
		this.htdetail = htdetail;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getAuthorname() {
		return authorname;
	}

	public void setAuthorname(String authorname) {
		this.authorname = authorname;
	}

	public String getDetailurl() {
		return detailurl;
	}

	public void setDetailurl(String detailurl) {
		this.detailurl = detailurl;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}
	
	public static class FreeBeanRequestData {
		
		@SerializedName("errormsg")
		private String errormsg;
		
		@SerializedName("totals")
		private Integer totals;
		
		@SerializedName("total")
		private Integer total;
		
		@SerializedName("curr_page")
		private Integer curr_page;
		
		@SerializedName("pages")
		private Integer pages;
		
		@SerializedName("returnmsg")
		private String returnmsg;
		
		@SerializedName("record")
		private ArrayList<FreeBean> record;

		@SerializedName("show")
		private Boolean show;
		
		@SerializedName("login")
		private Boolean login;
		
		@SerializedName("userctime")
		private Boolean userctime;
		
		@SerializedName("usercexprise")
		private Boolean usercexprise;
		
		public String getMsg() {
			return returnmsg;
		}

		public String getErrormsg() {
			return errormsg;
		}

		public Integer getTotals() {
			return totals;
		}

		public Integer getTotal() {
			return total;
		}

		public Integer getCurr_page() {
			return curr_page;
		}

		public Integer getPages() {
			return pages;
		}

		public String getReturnmsg() {
			return returnmsg;
		}

		public Boolean getShow() {
			return show;
		}

		public Boolean getLogin() {
			return login;
		}

		public Boolean getUserctime() {
			return userctime;
		}

		public Boolean getUsercexprise() {
			return usercexprise;
		}

		public ArrayList<FreeBean> getRecord() {
			return record;
		}
		
	}
	
}
