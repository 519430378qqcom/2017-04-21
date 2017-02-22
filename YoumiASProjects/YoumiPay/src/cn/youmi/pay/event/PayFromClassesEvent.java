package cn.youmi.pay.event;

/**
 * 购买来源的页面
 * 
 * @author tangxiyong
 * @version 2014-11-20 下午4:45:34
 */
public enum PayFromClassesEvent {

	/** 优米课堂 从详情页购买专辑 */
	DETAIL_COURSE(1),

	/** 优米课堂 从详情页购买vip */
	DETAIL_VIP(2),

	/** 优米课堂 在vip页面购买vip */
	LIST_VIP(3),

	/** 优米课堂 专题页面 */
	ZHUANTI(4),

	/** 优米课堂 讲师咨询讲师页面 */
	LECTURER(5),

	/** 订单页面 */
	ORDER_LIST(6),

	/** 从‘我的’界面充值 */
	RECHARGE(7),

	/** 优优说－账目充值 */
	YYS_RECHARGE(21),
	/** 优优说－产品购买充值 */
	YYS_PRODUCTBUY(22),

	/** onError */
	ERROR(503),
	/***/
	DEFAULT_VALUE(99999);

	private int value;

	public int getValue() {
		return value;
	}

	PayFromClassesEvent(final int value) {
		this.value = value;
	}

	public static PayFromClassesEvent getName(int index) {
		switch (index) {
		case 1:
			return DETAIL_COURSE;
		case 2:
			return DETAIL_VIP;
		case 3:
			return LIST_VIP;
		case 4:
			return ZHUANTI;
		case 5:
			return LECTURER;
		case 6:
			return ORDER_LIST;
		case 7:
			return RECHARGE;
		case 503:
			return ERROR;

		default:
			return DEFAULT_VALUE;
		}

	}

}
