package cn.youmi.pay.manager;

import cn.youmi.framework.manager.ModelManager;
import cn.youmi.framework.util.SingletonFactory;
import cn.youmi.pay.event.PaySuccTypeEvent;

/**
 * 
 * @author tangxiyong
 * @version 2014-11-19 上午12:50:36
 */
public class PaySuccTypeManager extends ModelManager<PaySuccTypeEvent, String> {

	public static PaySuccTypeManager getInstance() {
		return SingletonFactory.getInstance(PaySuccTypeManager.class);
	}

	public void setPayResult(PaySuccTypeEvent key) {
		for (ModelManager.ModelStatusListener<PaySuccTypeEvent, String> l : listeners) {
			l.onModelUpdate(key, " ");
		}
	}

	public void setPayResult(PaySuccTypeEvent key, String str) {
		for (ModelManager.ModelStatusListener<PaySuccTypeEvent, String> l : listeners) {
			l.onModelUpdate(key, str);
		}
	}

}
