package cn.youmi.pay.manager;

import cn.youmi.framework.manager.ModelManager;
import cn.youmi.framework.util.SingletonFactory;
import cn.youmi.pay.event.PayResultEvent;

/**
 * 
 * @author tangxiyong
 * @version 2014-11-19 上午12:50:36
 */
public class PayResultManager extends ModelManager<PayResultEvent, String> {

	public static PayResultManager getInstance() {
		return SingletonFactory.getInstance(PayResultManager.class);
	}

	public void setPayResult(PayResultEvent key) {
		for (ModelManager.ModelStatusListener<PayResultEvent, String> l : listeners) {
			l.onModelUpdate(key, " ");
		}
	}

	public void setPayResult(PayResultEvent key, String str) {
		for (ModelManager.ModelStatusListener<PayResultEvent, String> l : listeners) {
			l.onModelUpdate(key, str);
		}
	}

}
