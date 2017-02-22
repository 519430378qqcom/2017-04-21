package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 播放记录
 * 
 * @author tangxiyong 2013-12-9上午10:54:49
 * 
 */
public class UmiwiMyRecordBeans extends BaseGsonBeans {

	@SerializedName("id")
	private String id;

	@SerializedName("vid")
	private String vid;

	@SerializedName("title")
	private String title;

	@SerializedName("time")
	private String time;

	@SerializedName("url")
	private String url;

	@SerializedName("canbuy")
	private String canbuy;
	
	@SerializedName("detailurl")
	private String detailurl;
	
	@SerializedName("groupname")
	private String groupname;
	
	@SerializedName("terminal")
	private String terminal;
	
	@SerializedName("progresstext")
	private String progressText;
	
	public String getProgressText(){
		return progressText;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public static UmiwiMyRecordBeans fromJson(String json) {
		return new Gson().fromJson(json, UmiwiMyRecordBeans.class);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCanbuy() {
		return canbuy;
	}

	public void setCanbuy(String canbuy) {
		this.canbuy = canbuy;
	}

	public String getDetailurl() {
		return detailurl;
	}

	public void setDetailurl(String detailurl) {
		this.detailurl = detailurl;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	public static class MyCouponListRequestData {

		@SerializedName("total")
		private int total;

		@SerializedName("totals")
		private int totals;

		@SerializedName("curr_page")
		private int curr_page;

		@SerializedName("pages")
		private int pages;

		@SerializedName("record")
		private ArrayList<UmiwiMyRecordBeans> record;

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

		public ArrayList<UmiwiMyRecordBeans> getRecord() {
			return record;
		}

	}
}
