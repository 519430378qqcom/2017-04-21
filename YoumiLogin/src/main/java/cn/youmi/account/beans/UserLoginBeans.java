package cn.youmi.account.beans;

import cn.youmi.framework.model.BaseModel;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author tangxiyong 2014-5-7下午1:59:41
 * 
 */
public class UserLoginBeans extends BaseModel {

	@SerializedName("uid")
	private String uid;// 用户id

	@SerializedName("username")
	private String username;// 用户名

	@SerializedName("email")
	private String email;// 用户邮箱

	@SerializedName("token")
	private String token;//

	@SerializedName("code")
	private String code;

	@SerializedName("msg")
	private String msg;// 登陆失败返回信息

	public Boolean isSucc() {
		return "succ".equals(code);
	}

	public String showMsg() {
		String msg_str = "未知错误";
		if (null != msg && !"".equals(msg)) {
			msg_str = msg;
		}
		return msg_str;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

}
