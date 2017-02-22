package cn.youmi.account.manager;

import java.util.HashMap;

import cn.youmi.account.event.ClassesEvent;
import cn.youmi.account.model.OAuthResultModel;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.PostRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.manager.ModelManager;
import cn.youmi.framework.util.SingletonFactory;

/**
 * 第三方登录成功
 * 
 * @author tangxiyong
 * @version 2014-11-13 下午7:09:31
 */
public class OAuthLoginResultManager extends
		ModelManager<ClassesEvent, OAuthResultModel> {

	public static OAuthLoginResultManager getInstance() {
		return SingletonFactory.getInstance(OAuthLoginResultManager.class);

	}

	/**
	 * http://passport.youmi.cn/mobile/oauthlogin?type=%s&connectid=%s&token=%s&
	 * secret=%s&refresh_token=%s&ref=%s
	 */
//	protected abstract String getOAuthLoginUrl();

	/**
	 * http://passport.youmi.cn/mobile/oauthlogin?type=%s&connectid=%s&token=%s&
	 * secret=%s&refresh_token=%s&ref=%s
	 * 
	 * @param userId
	 * @param token
	 * @param tokenSecret
	 * @param refreshToken
	 * @param type
	 * 
	 * get 方法
	 */
	public void getOAuthCookie(String OAuthLoginurl, ClassesEvent tag) {
		GetRequest<OAuthResultModel> get = new GetRequest<OAuthResultModel>(
				OAuthLoginurl, GsonParser.class, OAuthResultModel.class,
				OAuthCookieListener);
		get.setTag(tag);
		get.go();
	}

	/**
	 * http://passport.youmi.cn/mobile/oauthlogin?type=%s&connectid=%s&token=%s&
	 * secret=%s&refresh_token=%s&ref=%s
	 * 
	 * @param userId
	 * @param token
	 * @param tokenSecret
	 * @param refreshToken
	 * @param type
	 * 
	 * put 方法
	 */
	public void postOAuthCookie(String OAuthLoginurl, ClassesEvent tag, HashMap<String, String> param) {
		PostRequest<OAuthResultModel> get = new PostRequest<OAuthResultModel>(
				OAuthLoginurl, GsonParser.class, OAuthResultModel.class,
				OAuthCookieListener);
		get.setTag(tag);
		get.addParams(param);
		get.go();
	}
	
	private Listener<OAuthResultModel> OAuthCookieListener = new Listener<OAuthResultModel>() {

		@Override
		public void onResult(AbstractRequest<OAuthResultModel> request,
				OAuthResultModel t) {
			ClassesEvent key = (ClassesEvent) request.getTag();
			if (null!=t && t.isSucc()) {
				// 第三方登录成功,获取cookie与用户信息
//				if (!"login".equals(t.getoAuthMessage().getMethod())) {// 首次注册绑定手机号
//					
//				}
				for (ModelManager.ModelStatusListener<ClassesEvent, OAuthResultModel> l : listeners) {
					l.onModelUpdate(key, t);
				}

			} else {
				// 第三方登录失败,应清除授权信息
				for (ModelManager.ModelStatusListener<ClassesEvent, OAuthResultModel> l : listeners) {
					l.onModelUpdate(ClassesEvent.FAIL, t);
				}
			}
		}

		@Override
		public void onError(AbstractRequest<OAuthResultModel> requet,
				int statusCode, String body) {
			// 关闭activity的对话框
			for (ModelManager.ModelStatusListener<ClassesEvent, OAuthResultModel> l : listeners) {
				l.onModelUpdate(ClassesEvent.ERROR, new OAuthResultModel(body));
			}
		}
	};

}
