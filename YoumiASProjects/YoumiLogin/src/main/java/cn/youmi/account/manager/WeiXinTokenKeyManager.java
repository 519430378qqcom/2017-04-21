package cn.youmi.account.manager;

import cn.youmi.account.event.WeiXinLoginTokenEvent;
import cn.youmi.framework.manager.ModelManager;
import cn.youmi.framework.util.SingletonFactory;

/**
 * 在WXEntryActivity.java的
 * 	onResp(){
 *		switch (resp.errCode) {
 *		case BaseResp.ErrCode.ERR_OK://发送成功
 *			if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
 *		        WeiXinTokenKeyManager.getInstance().setWXTokenKey(authResp.code);
 *			} 
 *			break;
 *			}
 *		}
 * @author tangxiyong
 * @version 2014-11-14 下午4:55:20
 */
public class WeiXinTokenKeyManager extends
		ModelManager<WeiXinLoginTokenEvent, String> {

	public static WeiXinTokenKeyManager getInstance() {
		return SingletonFactory.getInstance(WeiXinTokenKeyManager.class);
	}

	public void setWXTokenKey(String wxTokenKey) {
		for (ModelManager.ModelStatusListener<WeiXinLoginTokenEvent, String> l : listeners) {
			l.onModelUpdate(WeiXinLoginTokenEvent.TOKEN_KEY, wxTokenKey);
		}
	}

}
