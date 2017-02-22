package cn.youmi.account.event;


public enum UserEvent {
	/** 用户是否登录成功 */
	USER_LOGIN_SUCC(1),
	/** 登录成功后刷新用户界面 */
	HOME_LOGIN(2),
	/** 修改密码后刷新用户界面 */
	HOME_CHANGE_PASSWORD(3),
	/** 游客 */
	LOGIN_TOURISTS(18),
	/** 做完测试题刷新用户界面 */
	HOME_TESTINFO(19),
	/** 重置密码后刷新用户界面 */
	HOME_RESET_PASSWORD(4),
	/** #号用户：更改用户名和密码后刷新用户界面 */
	HOME_CHANGE_NAME_AND_PASSWORD(5),
	/** 退出登录后刷新用户界面 */
	HOME_LOGIN_OUT(6),
	/** 网页内注册或登陆成功 */
	HOME_WEBVIEW(7),
	/** 用户注册成功并补全信息 */
	HOME_USER_REGISTE_SUCC_AND_COMMIT_INFO(12),
	
	
	/** 退出账号 */
	LOGGED_OUT(10),
	/** 头像刷新 */
	AVATAR(11),
	/** 统计 */
	START_COUNT(13),
	/** 账号被踢出 */
	LOGGED_KICK_OUT(14),
	/** splash start*/
	USER_START(16),
	
//	/** 购买会员 */
//	PAY_HOME_VIP(21),
//	/** 购买专辑 */
//	PAY_HOME_COURSE(22),
//	/** 订单页面支付成功刷新 */
//	PAY_HOME_ORDER(23),
	/** 从‘我的’界面充值 成功后只要刷新‘我的’ */
	PAY_MINE_RECHARGE(24),
	/** 在订单支付时充值 成功后只要刷新‘我的’ 同时刷新支付界面 */
	PAY_MINEANDORDER_RECHARGE(25),
//	/** 购买专题*/
//	PAY_ZHUANTI(26),
//	/** 购买专题*/
//	PAY_LECTURER(27),
	/** 讲师咨询价格设置成功，刷新用户信息*/
	CONSULTATION_SETTING(40),
	
	/** 支付成功*/
	PAY_SUCC(666),
	
	/** 手机注册成功未补全信息 */
	USER_MOBILE_REGISTE_SUCC_NO_INFO(49),
	/** 绑定手机号成功 */
	USER_BINDING_PHONE(51),
	LOGIN(250),
	EORR_LOGIN(401),
	/***/
	DEFAULT_VALUE(99999);

	private int value;

	public int getValue() {
		return value;
	}

	UserEvent(final int value) {
		this.value = value;
	}
}
