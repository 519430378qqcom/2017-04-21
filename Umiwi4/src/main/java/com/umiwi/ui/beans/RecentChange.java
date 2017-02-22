package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 最近更新
 */
public class RecentChange extends BaseGsonBeans {
	
	public static RecentChange fromJson(String json) {
		return new Gson().fromJson(json, RecentChange.class);
	}
	
	@SerializedName("id")
	private String id;
	
	@SerializedName("title")
	private String title;
	
	@SerializedName("image")
	private String image;

	@SerializedName("authorname")
	private String authorname;

	@SerializedName("detailurl")
	private String detailurl;
	
	
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

	public static class RecentChangeWrapper {
		@SerializedName("day")
		private ArrayList<RecentChange> day;
		
		@SerializedName("week")
		private ArrayList<RecentChange> week;
		
		@SerializedName("month")
		private ArrayList<RecentChange> month;
		
		@SerializedName("more")
		private ArrayList<RecentChange> more;

		public ArrayList<RecentChange> getDay() {
			return day;
		}

		public ArrayList<RecentChange> getWeek() {
			return week;
		}

		public ArrayList<RecentChange> getMonth() {
			return month;
		}

		public ArrayList<RecentChange> getMore() {
			return more;
		}

		//更新数据
		public void setWrapper(RecentChangeWrapper newRecentChangeWrapper) {
			ArrayList<RecentChange> temps = newRecentChangeWrapper.getDay(); 
			if(temps != null && temps.size() > 0) {
				ArrayList<RecentChange> dayTemp = this.getDay();
				if(dayTemp != null) {
					dayTemp.addAll(temps);
				} else {
					dayTemp = temps;
				}
				
				temps = null;
			}
			
			temps = newRecentChangeWrapper.getWeek(); 
			if(temps != null && temps.size() > 0) {
				ArrayList<RecentChange> weekTemp = this.getWeek();
				if(weekTemp != null) {
					weekTemp.addAll(temps);
				} else {
					weekTemp = temps;
				}
				
				temps = null;
			}
			
			temps = newRecentChangeWrapper.getMonth(); 
			if(temps != null && temps.size() > 0) {
				ArrayList<RecentChange> monthTemp = this.getMonth();
				if(monthTemp != null) {
					monthTemp.addAll(temps);
				} else {
					monthTemp = temps;
				}
				
				temps = null;
			}
			
			temps = newRecentChangeWrapper.getMore(); 
			if(temps != null && temps.size() > 0) {
				ArrayList<RecentChange> moreTemp = this.getMore();
				if(moreTemp != null) {
					moreTemp.addAll(temps);
				} else {
					moreTemp = temps;
				}
				
				temps = null;
			}
		}
		
	}
	
	public static class RecentChangeRequestData {
		
		@SerializedName("totals")
		private Integer totals;
		
		@SerializedName("total")
		private Integer total;
		
		@SerializedName("curr_page")
		private Integer curr_page;
		
		@SerializedName("pages")
		private Integer pages;
		
		@SerializedName("record")
		private RecentChangeWrapper record;

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

		public RecentChangeWrapper getRecord() {
			return record;
		}
		
	}
	
}
