package cn.youmi.pay.event;

public enum PayResultEvent {
	SUCC(1),

	REFRESH(3),
	
	WEB_SUCC(4),
	
	WEB_REFRESH(5),
	
	RQF_WEIXIN(10),
	
	RQF_UNPAY(11),
	
	RQF_ALIPAY(12),

	/** onError */
	ERROR(503),
	/***/
	DEFAULT_VALUE(99999);

	private int value;

	public int getValue() {
		return value;
	}

	PayResultEvent(final int value) {
		this.value = value;
	}

}
