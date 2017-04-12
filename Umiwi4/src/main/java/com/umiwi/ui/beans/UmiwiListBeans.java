package com.umiwi.ui.beans;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 一级列表解析方式 适用于排行榜
 * 
 * @author tangxiyong 2013-11-19下午5:49:44
 * 
 */
public class UmiwiListBeans extends BaseGsonBeans {

	@SerializedName("title")
	private String title;

	@SerializedName("id")
	private long id;
	@SerializedName("albumid")
	private String albumid;

	public String getAlbumid() {
		return albumid;
	}

	public void setAlbumid(String albumid) {
		this.albumid = albumid;
	}

	@SerializedName("uid")
	private long uid;
	@SerializedName("image")
	private String image;
	private String tutoruid;

	public String getTutoruid() {
		return tutoruid;
	}

	public void setTutoruid(String tutoruid) {
		this.tutoruid = tutoruid;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	@SerializedName("grade")
	private String grade;

	@SerializedName("authorname")
	private String authorname;

	@SerializedName("classes")
	private String classes;

	@SerializedName("detailurl")
	private String detailurl;

	@SerializedName("spmurl")
	private String spmurl;

	@SerializedName("notelist")
	private String notelist;

	/**
	 * 收藏列表的收藏id，用于删除
	 */
	@SerializedName("favid")
	private String favid;

	/**
	 * 轮播判断类型
	 */
	@SerializedName("types")
	private String types;

	@SerializedName("url")
	private String url;

	/** 免费体验7天 */
	@SerializedName("classname")
	private String classname;

	@SerializedName("gridtype")
	private String gridtype;

	@SerializedName("gridmore")
	private String gridmore;

	@SerializedName("category")
	private String category;

	@SerializedName("watchnum")
	private String watchnum;

	@SerializedName("notesnum")
	private String notesnum;

	@SerializedName("icontype")
	private String icontype;

	@SerializedName("icon")
	private String icon;

	@Override
	public String toString() {
		return "UmiwiListBeans{" +
				"title='" + title + '\'' +
				", id=" + id +
				", albumid='" + albumid + '\'' +
				", uid=" + uid +
				", image='" + image + '\'' +
				", tutoruid='" + tutoruid + '\'' +
				", grade='" + grade + '\'' +
				", authorname='" + authorname + '\'' +
				", classes='" + classes + '\'' +
				", detailurl='" + detailurl + '\'' +
				", spmurl='" + spmurl + '\'' +
				", notelist='" + notelist + '\'' +
				", favid='" + favid + '\'' +
				", types='" + types + '\'' +
				", url='" + url + '\'' +
				", classname='" + classname + '\'' +
				", gridtype='" + gridtype + '\'' +
				", gridmore='" + gridmore + '\'' +
				", category='" + category + '\'' +
				", watchnum='" + watchnum + '\'' +
				", notesnum='" + notesnum + '\'' +
				", icontype='" + icontype + '\'' +
				", icon='" + icon + '\'' +
				'}';
	}

	public static UmiwiListBeans fromJson(String json) {
		return new Gson().fromJson(json, UmiwiListBeans.class);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public String getSpmurl() {
		return spmurl;
	}

	public void setSpmurl(String spmurl) {
		this.spmurl = spmurl;
	}

	public String getNotelist() {
		return notelist;
	}

	public void setNotelist(String notelist) {
		this.notelist = notelist;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public String getFavid() {
		return favid;
	}

	public void setFavid(String favid) {
		this.favid = favid;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getGridtype() {
		return gridtype;
	}

	public void setGridtype(String gridtype) {
		this.gridtype = gridtype;
	}

	public String getGridmore() {
		return gridmore;
	}

	public void setGridmore(String gridmore) {
		this.gridmore = gridmore;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getWatchnum() {
		return watchnum;
	}

	public void setWatchnum(String watchnum) {
		this.watchnum = watchnum;
	}

	public String getNotesnum() {
		return notesnum;
	}

	public void setNotesnum(String notesnum) {
		this.notesnum = notesnum;
	}

	public String getIcontype() {
		return icontype;
	}

	public void setIcontype(String icontype) {
		this.icontype = icontype;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public static class ChartsListRequestData {

		@SerializedName("total")
		private int total;

		@SerializedName("totals")
		private int totals;

		/** 当前页 */
		@SerializedName("curr_page")
		private int curr_page;

		/** 总页数 */
		@SerializedName("pages")
		private int pages;

		@SerializedName("show")
		private boolean isShow;

		@SerializedName("record")
		private ArrayList<UmiwiListBeans> record;

		public void setTotal(int total) {
			this.total = total;
		}

		public void setTotals(int totals) {
			this.totals = totals;
		}

		public void setCurr_page(int curr_page) {
			this.curr_page = curr_page;
		}

		public void setPages(int pages) {
			this.pages = pages;
		}

		public void setShow(boolean show) {
			isShow = show;
		}

		public void setRecord(ArrayList<UmiwiListBeans> record) {
			this.record = record;
		}

		public int getTotal() {
			return total;
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

		public boolean isShow() {
			return isShow;
		}

		public ArrayList<UmiwiListBeans> getRecord() {
			return record;
		}

		@Override
		public String toString() {
			return "ChartsListRequestData{" +
					"total=" + total +
					", totals=" + totals +
					", curr_page=" + curr_page +
					", pages=" + pages +
					", isShow=" + isShow +
					", record=" + record +
					'}';
		}
	}
}
