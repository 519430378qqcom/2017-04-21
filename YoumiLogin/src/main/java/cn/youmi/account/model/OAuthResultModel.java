package cn.youmi.account.model;

import cn.youmi.framework.model.BaseModel;

import com.google.gson.annotations.SerializedName;

public class OAuthResultModel extends BaseModel {
	@SerializedName("e")
	private String e;

	@SerializedName("m")
	private String m;

	@SerializedName("code")
	private String code;

	@SerializedName("msg")
	private String msg;

	@SerializedName("r")
	private OAuthResultMessage oAuthMessage;

	public Boolean isSucc() {
		return "9999".equals(e) || "succ".equals(code);
	}
	
	public OAuthResultModel(String msg) {
		this.msg = msg;
		this.m = msg;
	}

	public String showMsg() {
		String msg_str = "未知错误";
		if (null != m && !"".equals(m)) {
			msg_str = m;
		} else {
			msg_str = msg;
		}
		return msg_str;
	}

	public String getE() {
		return e;
	}

	public void setE(String e) {
		this.e = e;
	}

	public String getM() {
		return m;
	}

	public void setM(String m) {
		this.m = m;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public OAuthResultMessage getoAuthMessage() {
		return oAuthMessage;
	}

	public void setoAuthMessage(OAuthResultMessage oAuthMessage) {
		this.oAuthMessage = oAuthMessage;
	}

	public class OAuthResultMessage extends BaseModel {
		@SerializedName("uid")
		private String uid;

		@SerializedName("method")
		private String method;

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
		}

	}

}
