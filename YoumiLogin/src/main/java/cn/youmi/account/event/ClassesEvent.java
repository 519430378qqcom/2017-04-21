package cn.youmi.account.event;

public enum ClassesEvent {
	/** 普通登录 */
	LOGIN(1),

	FAIL(404),

	/** onError */
	ERROR(503),
	/***/
	DEFAULT_VALUE(99999);

	private int value;

	public int getValue() {
		return value;
	}

	ClassesEvent(final int value) {
		this.value = value;
	}

}
