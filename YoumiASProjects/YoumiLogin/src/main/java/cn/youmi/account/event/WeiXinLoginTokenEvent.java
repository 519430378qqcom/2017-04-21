package cn.youmi.account.event;

public enum WeiXinLoginTokenEvent {
	
	TOKEN_SUCC(1),
	
	TOKEN_FAIL(2),
	/** 用于传微信的token*/
	TOKEN_KEY(3),

	/** onError */
	ERROR(503),
	/***/
	DEFAULT_VALUE(99999);

	private int value;

	public int getValue() {
		return value;
	}

	WeiXinLoginTokenEvent(final int value) {
		this.value = value;
	}
}
