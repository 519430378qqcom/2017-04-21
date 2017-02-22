package cn.youmi.account.manager;

import cn.youmi.account.event.WeiXinLoginTokenEvent;
import cn.youmi.account.model.WXTokenModel;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.manager.ModelManager;
import cn.youmi.framework.util.SingletonFactory;

/**
 * @author tangxiyong
 * @version 2014-11-14 上午11:25:10
 */
public class WeiXinLoginTokenManager extends
		ModelManager<WeiXinLoginTokenEvent, WXTokenModel> {

	public static WeiXinLoginTokenManager getInstance() {
		return SingletonFactory.getInstance(WeiXinLoginTokenManager.class);
	}

	public void getWXLoginToken(final String wxTokenUrl) {
		GetRequest<WXTokenModel> requestx = new GetRequest<WXTokenModel>(
				wxTokenUrl, GsonParser.class, WXTokenModel.class, wxLoginTokelistener);
		requestx.go();
	}
	
	private Listener<WXTokenModel> wxLoginTokelistener = new Listener<WXTokenModel>() {

		@Override
		public void onResult(AbstractRequest<WXTokenModel> request,
				WXTokenModel t) {
			if (null != t) {
				if (0 == t.getErrcode()) {
					for (ModelManager.ModelStatusListener<WeiXinLoginTokenEvent, WXTokenModel> l : listeners) {
						l.onModelUpdate(WeiXinLoginTokenEvent.TOKEN_SUCC, t);
					}
				} else {
					for (ModelManager.ModelStatusListener<WeiXinLoginTokenEvent, WXTokenModel> l : listeners) {
						l.onModelUpdate(WeiXinLoginTokenEvent.TOKEN_FAIL, t);
					}
				}
			} else {
				for (ModelManager.ModelStatusListener<WeiXinLoginTokenEvent, WXTokenModel> l : listeners) {
					l.onModelUpdate(WeiXinLoginTokenEvent.ERROR, t);
				}
			}
		}

		@Override
		public void onError(AbstractRequest<WXTokenModel> requet,
				int statusCode, String body) {
			for (ModelManager.ModelStatusListener<WeiXinLoginTokenEvent, WXTokenModel> l : listeners) {
				l.onModelUpdate(WeiXinLoginTokenEvent.ERROR, new WXTokenModel());
			}
		}
	};
}
