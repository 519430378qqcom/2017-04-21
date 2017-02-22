package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 评论列表
 * 
 * @author tangxiong
 * @version 2014年7月30日 下午3:22:22
 */
public class CommentListBeans extends BaseGsonBeans {

	@SerializedName("id")
	private String id;

	@SerializedName("uid")
	private String uid;

	@SerializedName("ctime")
	private String ctime;

	@SerializedName("utime")
	private String utime;

	@SerializedName("albumid")
	private String albumid;

	@SerializedName("question")
	private String question;

	@SerializedName("from")
	private String from;

	@SerializedName("rcount")
	private String rcount;

	@SerializedName("state")
	private String state;

	@SerializedName("type")
	private String type;

	@SerializedName("shortlink")
	private String shortlink;

	@SerializedName("headimg")
	private String headimg;

	@SerializedName("username")
	private String username;

	@SerializedName("identity")
	private String identity;

	@SerializedName("reply")
	private ArrayList<CommentReplyBean> reply;

	public ArrayList<CommentReplyBean> getReply() {
		return reply;
	}

	public String getId() {
		return id;
	}

	public String getUid() {
		return uid;
	}

	public String getCtime() {
		return ctime;
	}

	public String getUtime() {
		return utime;
	}

	public String getAlbumid() {
		return albumid;
	}

	public String getQuestion() {
		return question;
	}

	public String getFrom() {
		return from;
	}

	public String getRcount() {
		return rcount;
	}

	public String getState() {
		return state;
	}

	public String getType() {
		return type;
	}

	public String getShortlink() {
		return shortlink;
	}

	public String getHeadimg() {
		return headimg;
	}

	public String getUsername() {
		return username;
	}

	public String getIdentity() {
		return identity;
	}

	public static CommentListBeans fromJson(String json) {
		return new Gson().fromJson(json, CommentListBeans.class);
	}

	public static class CommentListRequestData {

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
		private ArrayList<CommentListBeans> record;

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

		public ArrayList<CommentListBeans> getRecord() {
			return record;
		}

	}

	public static class CommentReplyBean {
		@SerializedName("id")
		private String id;

		@SerializedName("uid")
		private String uid;

		@SerializedName("ctime")
		private String ctime;

		@SerializedName("threadid")
		private String threadid;

		@SerializedName("content")
		private String content;

		@SerializedName("username")
		private String username;

		@SerializedName("tip_time")
		private String tip_time;

		public String getId() {
			return id;
		}

		public String getUid() {
			return uid;
		}

		public String getCtime() {
			return ctime;
		}

		public String getThreadid() {
			return threadid;
		}

		public String getContent() {
			return content;
		}

		public String getUsername() {
			return username;
		}

		public String getTip_time() {
			return tip_time;
		}

	}
}
