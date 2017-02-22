package cn.youmi.account.model;

import android.text.TextUtils;
import cn.youmi.framework.model.BaseModel;

import com.google.gson.annotations.SerializedName;

public class UserModel extends BaseModel {
	@SerializedName("uid")
	private String uid;
	@SerializedName("username")
	private String username;
	@SerializedName("moblie")
	private String mobile;
	@SerializedName("avatar")
	private String avatar;
	@SerializedName("cookie")
	private String cookie;
	@SerializedName("balance")
	private String balance;
	@SerializedName("mycoin")
	private String mycoin;//积分
	@SerializedName("identity")
	private String identity;// 身份级别
	@SerializedName("grade")
	private String grade;// 观看权限级别
	@SerializedName("identity_expire")
	private String identity_expire;// 用户身份过期时间

	@SerializedName("usertest")
	private String usertest;// 判断用户是否做过测试题
	@SerializedName("dobindmobile")
	private boolean dobindmobile;

	/** 讲师端 */
	@SerializedName("tutoruid")
	private String tutoruid;// 用于获取讲师课程

	@SerializedName("price")
	private int price;// 咨询定价

	@SerializedName("account")
	private int account;// 账户余额

	public boolean isLoggedIn() {
		return isLogin();
	}
	
	public UserModel() {
		
	}
	
	public UserModel(String msg) {
		this.username = msg;
	}

	public enum LoginStatus {

		LOGGED_IN(1), LOGGED_OUT(2), BANNED(3);

		private final int Value;

		LoginStatus(int value) {
			this.Value = value;
		}

		public static LoginStatus ValueOf(int value) {
			switch (value) {
			case 1:
				return LOGGED_IN;
			case 2:
				return LOGGED_OUT;
			case 3:
				return BANNED;
			default:
				return null;
			}
		}

		public int getValue() {
			return this.Value;

		}
	}

	private LoginStatus mLoginStatus;

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid
	 *            the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the avatar
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar
	 *            the avatar to set
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/**
	 * @return the cookie
	 */
	public String getCookie() {
		return cookie;
	}

	/**
	 * @param cookie
	 *            the cookie to set
	 */
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	/**
	 * @return the balance
	 */
	public String getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getMycoin() {
		return mycoin;
	}

	public void setMycoin(String mycoin) {
		this.mycoin = mycoin;
	}

	/**
	 * @return the identity
	 */
	public String getIdentity() {
		return identity;
	}

	/**
	 * @param identity
	 *            the identity to set
	 */
	public void setIdentity(String identity) {
		this.identity = identity;
	}

	/**
	 * @return the grade
	 */
	public String getGrade() {
		return grade;
	}

	/**
	 * @param grade
	 *            the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}

	/**
	 * @return the identityExpire
	 */
	public String getIdentity_expire() {
		return identity_expire;
	}

	/**
	 * @param identityExpire
	 *            the identityExpire to set
	 */
	public void setIdentity_expire(String identity_expire) {
		this.identity_expire = identity_expire;
	}

	public LoginStatus getLoginStatus() {
		return mLoginStatus;
	}

	public void setLoginStatus(LoginStatus mLoginStatus) {
		this.mLoginStatus = mLoginStatus;
	}

	public Boolean isLogin() {
		return mLoginStatus == LoginStatus.LOGGED_IN && !TextUtils.isEmpty(uid);
	}

	public Boolean isLogout() {
		return !isLogin();
	}

	public String getUsertest() {
		return usertest;
	}

	public void setUsertest(String usertest) {
		this.usertest = usertest;
	}

	public boolean isDobindmobile() {
		return dobindmobile;
	}

	public String getTutoruid() {
		return tutoruid;
	}

	public void setTutoruid(String tutoruid) {
		this.tutoruid = tutoruid;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}

}
