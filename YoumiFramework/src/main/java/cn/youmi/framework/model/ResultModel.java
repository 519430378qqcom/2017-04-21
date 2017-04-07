package cn.youmi.framework.model;

import com.google.gson.annotations.SerializedName;

public class ResultModel extends BaseModel {
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

	public Boolean isSucc() {
		return "9999".equals(e) || "succ".equals(code);
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

	public String getR() {
		return r;
	}

	public void setR(String r) {
		this.r = r;
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

	@Override
	public String toString() {
		return "ResultModel{" +
				"e='" + e + '\'' +
				", m='" + m + '\'' +
				", code='" + code + '\'' +
				", msg='" + msg + '\'' +
				", r='" + r + '\'' +
				", status='" + status + '\'' +
				", succnum='" + succnum + '\'' +
				'}';
	}
}
