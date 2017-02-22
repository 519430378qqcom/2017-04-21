package cn.youmi.account.event;

public enum LoginEvent {
	/** 用户是否登录成功 */
	YOUMI_LOGIN_SUCC(1),

	/** onError*/
	LOGIN_ERROR(503),
	/***/
	DEFAULT_VALUE(99999);

	private int value;

	public int getValue() {
		return value;
	}

	LoginEvent(final int value) {
		this.value = value;
	}
}
