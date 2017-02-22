package cn.youmi.account.event;

public enum LoginModeEvent {
	
	LOGIN_YOUMI(1),
	
	LOGIN_SINA_WEIBO(2),
	
	LOGIN_WEIXIN(3),
	
	LOGIN_QQ(4),

	/** onError */
	ERROR(503),
	/***/
	DEFAULT_VALUE(99999);

	private int value;

	public int getValue() {
		return value;
	}

	LoginModeEvent(final int value) {
		this.value = value;
	}
}
