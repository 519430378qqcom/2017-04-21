package cn.youmi.account.manager;

import java.util.HashMap;

import cn.youmi.account.beans.UserLoginBeans;
import cn.youmi.account.event.ClassesEvent;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.PostRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.manager.ModelManager;
import cn.youmi.framework.util.SingletonFactory;

/**
 * 优米账号判断是登陆成功
 * 
 * @author tangxiyong
 * @version 2014-11-13 下午5:30:01
 */
public class YoumiLoginResultManager extends
		ModelManager<ClassesEvent, UserLoginBeans> {

	public static YoumiLoginResultManager getInstance() {
		return SingletonFactory.getInstance(YoumiLoginResultManager.class);
	}
	/**
	 * http://passport.youmi.cn/login/login/?%s&expire=315360000
	 * @return
	 */
//	protected abstract String getLoginUrl();
	
	/** 用户登录是否成功 */
	public void getUserLoginPassport(String youmiLoginUrl, ClassesEvent tag) {
		GetRequest<UserLoginBeans> get = new GetRequest<UserLoginBeans>(
				youmiLoginUrl, GsonParser.class, UserLoginBeans.class,userLoginListener);
		get.setTag(tag);
		get.go();
	}
	
	public void postUserLoginPassport(String youmiLoginUrl, ClassesEvent tag, HashMap<String, String> param) {
		PostRequest<UserLoginBeans> get = new PostRequest<UserLoginBeans>(
				youmiLoginUrl, GsonParser.class, UserLoginBeans.class,userLoginListener);
		get.setTag(tag);
		get.addParams(param);
		get.go();
	}

	/** 用户登录操作 */
	private Listener<UserLoginBeans> userLoginListener = new Listener<UserLoginBeans>() {

		@Override
		public void onResult(AbstractRequest<UserLoginBeans> request,
				UserLoginBeans t) {
			ClassesEvent key = (ClassesEvent) request.getTag();
			if (t != null) {
				if (t.isSucc()) {
					for (ModelManager.ModelStatusListener<ClassesEvent, UserLoginBeans> l : listeners) {
						l.onModelUpdate(key, t);
					}
				} else {
					for (ModelManager.ModelStatusListener<ClassesEvent, UserLoginBeans> l : listeners) {
						l.onModelUpdate(ClassesEvent.FAIL, t);
					}
				}
			}
		}

		@Override
		public void onError(AbstractRequest<UserLoginBeans> requet,
				int statusCode, String body) {
			// 关闭activity的对话框

			for (ModelManager.ModelStatusListener<ClassesEvent, UserLoginBeans> l : listeners) {
				l.onModelUpdate(ClassesEvent.ERROR, new UserLoginBeans());
			}
		}
	};
}
