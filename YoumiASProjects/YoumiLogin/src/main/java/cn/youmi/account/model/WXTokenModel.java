package cn.youmi.account.model;

import cn.youmi.framework.model.BaseModel;

import com.google.gson.annotations.SerializedName;

public class WXTokenModel extends BaseModel {
	@SerializedName("errcode")
	private int errcode;

	@SerializedName("errmsg")
	private String errmsg;

	@SerializedName("access_token")
	private String accesstoken;

	@SerializedName("expires_in")
	private int expiresin;

	@SerializedName("refresh_token")
	private String refreshtoken;

	@SerializedName("openid")
	private String openid;

	@SerializedName("scope")
	private String scope;

	@SerializedName("unionid")
	private String unionid;

	public String getAccesstoken() {
		return accesstoken;
	}

	public int getExpiresin() {
		return expiresin;
	}

	public String getRefreshtoken() {
		return refreshtoken;
	}

	public String getOpenid() {
		return openid;
	}

	public String getScope() {
		return scope;
	}

	public int getErrcode() {
		return errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public String getUnionid() {
		return unionid;
	}

}
