package com.umiwi.ui.beans;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 删除收藏视频&状态判断如：订单提交成功判断
 * 
 * @author tangxiyong 2014-2-15下午9:27:18
 * 
 */
public class UmiwiResultBeans extends BaseGsonBeans {
 
	public static UmiwiResultBeans fromJson(String json) {
		return new Gson().fromJson(json, UmiwiResultBeans.class);
	}

	public static class ResultBeansRequestData {

		@SerializedName("e")
		private String e;

		@SerializedName("m")
		private String m;

		@SerializedName("code")
		private String code;

		@SerializedName("msg")
		private String msg;

		@SerializedName("r")
		private String r;
		
		@SerializedName("status")
		private String status;
		
		@SerializedName("succnum")
		private String succnum;
		
 
		public String getE() {
			return e;
		}

		public String getM() {
			return m;
		}

		public String getCode() {
			return code;
		}

		public String getMsg() {
			return msg;
		}

		public String getR() {
			return r;
		}

		public void setE(String e) {
			this.e = e;
		}

		public void setM(String m) {
			this.m = m;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public void setR(String r) {
			this.r = r;
		}

		public Boolean isSucc() {
			return "9999".equals(e) || "succ".equals(code);
		}
		
		public Boolean isDeteleSucc(){
			return "succ".equals(msg);
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getSuccnum() {
			return succnum;
		}

		public void setSuccnum(String succnum) {
			this.succnum = succnum;
		}

	}
}
