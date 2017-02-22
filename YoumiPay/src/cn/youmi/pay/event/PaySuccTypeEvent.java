package cn.youmi.pay.event;

/**
 * 购买的类型
 * 
 * @author tangxiyong
 * @version 2014-11-20 下午4:45:56
 */
public enum PaySuccTypeEvent {
	/** 优米课堂 专辑购买 */
	COURSE(2),

	/** 优米课堂 会员购买 */
	VIP(3),

	/** 优米课堂 充值 */
	RECHARGE(8),

	/** 优米课堂 专题购买 */
	ZHUANTI(12),

	/** 优米课堂 讲师咨询购买 */
	LECTURER(13),

	/** 订单界面 完成支付 */
	ORDER_LIST(99),
	
	/** 取消，未知错误*/
	CANCEL(555),

	/** onError */
	ERROR(503),
	/***/
	DEFAULT_VALUE(99999);

	private int value;

	public int getValue() {
		return value;
	}

	PaySuccTypeEvent(final int value) {
		this.value = value;
	}

	public static PaySuccTypeEvent getName(int index) {
		switch (index) {
		case 2:
			return COURSE;
		case 3:
			return VIP;
		case 8:
			return RECHARGE;
		case 12:
			return ZHUANTI;
		case 13:
			return LECTURER;
		case 99:
			return ORDER_LIST;
		case 503:
			return ERROR;

		default:
			return DEFAULT_VALUE;
		}

	}

}
