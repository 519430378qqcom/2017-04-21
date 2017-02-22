package cn.youmi.framework.manager;

public enum ResultEvent {
	/** 用户是否登录成功 */
	USER_LOGIN_SUCC(1),
	
	/** 判断修改密码是否成功 */
	USER_CHANGE_PASSWORD_SUCC(42),
	
	/** 判断#用户修改用户名和密码是否成功 */
	USER_CHANGE_NAME_AND_PASSWORD_SUCC(43),
	/** 判断手机注册是否成功 */
	USER_MOBILE_REGISTE_SUCC(48),
	/** 修改密码是否成功 */
	USER_PASSWORD_RESET_SUCC(50),
	
	/** 监听推送的新信息*/
	PUSH_MESSAGE(99),
	/** 清除信息红点*/
	PUSH_MESSAGE_REMOVE(98),

	QR_CODE(1001),

	/***/
	DEFAULT_VALUE(99999);

	private int value;

	public int getValue() {
		return value;
	}

	ResultEvent(final int value) {
		this.value = value;
	}
}
