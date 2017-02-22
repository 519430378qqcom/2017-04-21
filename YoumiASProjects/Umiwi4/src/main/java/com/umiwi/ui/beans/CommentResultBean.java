package com.umiwi.ui.beans;

import cn.youmi.framework.model.BaseModel;

import com.google.gson.annotations.SerializedName;

public class CommentResultBean extends BaseModel {
	@SerializedName("e")
	private String e;

	@SerializedName("coin")
	private Result m;

	@SerializedName("record")
	private CommentListBeans r;

	@SerializedName("threadid")
	private String threadid;
	
	public Boolean isSucc() {
		return "9999".equals(e);
	}

	public String getE() {
		return e;
	}

	public Result getM() {
		return m;
	}

	public CommentListBeans getR() {
		return r;
	}

	public String getThreadid() {
		return threadid;
	}

	public static class Result {
		@SerializedName("coinnum")
		public String coinnum;
		
		@SerializedName("cointype")
		public String cointype;
		
		@SerializedName("e")
		public String e;
		
		@SerializedName("m")
		public String m;

		public String getCoinnum() {
			return coinnum;
		}

		public String getCointype() {
			return cointype;
		}

		public String getE() {
			return e;
		}

		public String getM() {
			return m;
		}
	}
}
