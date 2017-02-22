package cn.youmi.pay.model;

import cn.youmi.framework.model.BaseModel;

import com.google.gson.annotations.SerializedName;

/**
 * 微信支付 获取信息
 * 
 * @author tangxiyong 2014-4-9下午3:47:06
 * 
 */
public class WeiXinPayResultModel extends BaseModel {

	@SerializedName("e")
	private String e;

	@SerializedName("m")
	private String m;

	@SerializedName("appid")
	private String appid;

	@SerializedName("noncestr")
	private String noncestr;

	@SerializedName("package")
	private String packagename;

	@SerializedName("partnerid")
	private String partnerid;

	@SerializedName("prepayid")
	private String prepayid;

	@SerializedName("sign")
	private String sign;

	@SerializedName("timestamp")
	private String timestamp;

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

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getNoncestr() {
		return noncestr;
	}

	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}

	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}

	public String getPartnerid() {
		return partnerid;
	}

	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}

	public String getPrepayid() {
		return prepayid;
	}

	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public Boolean isSucc() {
		return "9999".equals(e);
	}

}
