package cn.youmi.account.manager;

import android.text.TextUtils;

import java.util.HashMap;

import cn.youmi.account.dao.UserDao;
import cn.youmi.account.event.UserEvent;
import cn.youmi.account.model.UserModel;
import cn.youmi.account.model.UserModel.LoginStatus;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.CookieDao;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.PostRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.http.parsers.StringParser;
import cn.youmi.framework.main.BaseApplication;
import cn.youmi.framework.main.ContextProvider;
import cn.youmi.framework.manager.ModelManager;
import cn.youmi.framework.util.ForeTask;

public abstract class UserManager extends ModelManager<UserEvent, UserModel> {
	private static UserManager sInstance;
	public static UserManager getInstance(){
		return sInstance;
	}
	
	public static void setInstance(UserManager um){
		sInstance = um;
	}
	
	public abstract boolean isConsult();
	public abstract String getUserInfoUrl();
	public abstract String getLogoutUrl();
	/**
	 * method= GET && POST
	 * @return
	 */
	public abstract String getMethod();
	
	/** 获取用户信息*/
	private Listener<UserModel> userListener = new Listener<UserModel>(){
		@Override
		public void onResult(AbstractRequest<UserModel> request,UserModel user) {
			UserEvent key = (UserEvent) request.getTag();
			save(user, key);
		}

		@Override
		public void onError(AbstractRequest<UserModel> request, int statusCode,
				String body) {
		}
	};
	
	
	/**
	 * 成功“登录”后获取用户信息并保存到数据库 仅GET
	 * @param tag  请求标识
	 */
	public void getUserInfoSave(UserEvent tag) {
		
//		GetRequest<UserModel> get = new GetRequest<UserModel>(getUserInfoUrl(), GsonParser.class,UserModel.class, userListener);
//		get.setTag(tag);
//		get.go();
		String uid = CookieDao.getInstance(BaseApplication.getApplication()).getUid();
		if (TextUtils.isEmpty(uid))
			uid = "";
		getUserInfoSave(tag, uid);
	}
	/**
	 * 成功“登录”后获取用户信息并保存到数据库 支持POST，GET
	 * @param tag  请求标识
	 */
	public void getUserInfoSave(UserEvent tag, String uid) {
		if (getMethod().equals("GET")) {
			GetRequest<UserModel> get = new GetRequest<UserModel>(String.format(getUserInfoUrl(), uid), GsonParser.class,UserModel.class, userListener);
			get.setTag(tag);
			get.go();
		} else if (getMethod().equals("POST")) {
			PostRequest<UserModel> request = new PostRequest<UserModel>(getUserInfoUrl(),  GsonParser.class,UserModel.class, userListener);
			request.setTag(tag);
			request.addParam("uid", uid);
			request.go();
		} else {
			PostRequest<UserModel> request = new PostRequest<UserModel>(getUserInfoUrl(),  GsonParser.class,UserModel.class, userListener);
			request.setTag(tag);
			request.addParam("uid", uid);
			request.go();
		}
		
	}
	/**
	 * 成功“登录”后获取用户信息并保存到数据库 仅POST
	 * @param tag  请求标识
	 */
	public void getUserInfoSave(UserEvent tag, HashMap<String , String > param) {
		PostRequest<UserModel> get = new PostRequest<UserModel>(getUserInfoUrl(), GsonParser.class,UserModel.class, userListener);
		get.setTag(tag);
		get.addParams(param);
		get.go();
	}
	
	/** 从本地获取用户对象*/
	public UserModel getUser() {
//		String uid = CookieModelManager.getInstance().getUid();
		String uid = CookieDao.getInstance(BaseApplication.getApplication()).getUid();
  		if(uid == null)
		{
  			//游客对象
			UserModel user = new UserModel();
			user.setAvatar("http://i2.umivi.net/avatar/0/0b.jpg");
			user.setUid("");
			user.setBalance("0");
			user.setCookie("");
			user.setGrade("0");
			user.setIdentity("0");
			user.setIdentity_expire("");
			user.setMobile("0");
			user.setUsername("");
			user.setUsertest("");
			user.setMycoin("");
			user.setLoginStatus(LoginStatus.LOGGED_OUT);
			return user;
		}
  		
		UserModel user =  getByUid(uid);
		
		if(user == null) {
			getUserInfoSave(UserEvent.LOGIN_TOURISTS);
 			//登录用户临时对象
			user = new UserModel();
			user.setAvatar("http://i2.umivi.net/avatar/0/0b.jpg");
			user.setUid("");
			user.setBalance("0");
			user.setCookie("");
			user.setGrade("0");
			user.setIdentity("0");
			user.setIdentity_expire("");
			user.setMobile("0");
			user.setUsername("");
			user.setUsertest("");
			user.setMycoin("");
			user.setLoginStatus(LoginStatus.LOGGED_IN);
		} 
		
		user.setLoginStatus(LoginStatus.LOGGED_IN);
		return user;
	}
	
	/** 获取当前登录的 uid*/
	public String getUid() {
//		return CookieModelManager.getInstance().getUid();
		return CookieDao.getInstance(BaseApplication.getApplication()).getUid();
	}
	
	public UserModel getByUid(String uid) {
		if(uid == null) {
			return null;
		}
		return UserDao.getInstance().getByUid(uid);
 	}
	
 	public synchronized Boolean isLogin() {
 		return getUser().isLogin();
 	}
	
	/** 将用户信息 保存数据库  */
	public void save(UserModel user, UserEvent key) {
		if(user != null) {
			UserDao.getInstance().save(user);
			
			for(ModelStatusListener<UserEvent, UserModel> l : listeners){
				l.onModelUpdate(key, user);
			}
		}
	}
	
	private Listener<String> listener = new Listener<String>() {

		@Override
		public void onResult(AbstractRequest<String> request, String t) {
			
		}

		@Override
		public void onError(AbstractRequest<String> requet, int statusCode,
				String body) {
			
		}
	};
	
	public void logout() {
		logout("");
		
		GetRequest<String> request = new GetRequest<String>(getLogoutUrl(), StringParser.class, listener);
		request.go();
	}
	public void logout(final String msg) {
		final UserModel user  = getUser();
		String uid = null;
		if(user!= null)
		{
			uid = user.getUid();
		}
//		CookieModelManager.getInstance().logout();
		CookieDao.getInstance(BaseApplication.getApplication()).clear();
		CookieDao.getInstance(BaseApplication.getApplication()).deleteUser();
		if(uid != null) {
			UserDao.getInstance().delete(uid);
		}

		new ForeTask() {
			@Override
			protected void runInForeground() {
		 		for(ModelStatusListener<UserEvent, UserModel> l:listeners) {
		 			if(user == null) {
		 				continue;
		 			}
		 			user.setLoginStatus(LoginStatus.LOGGED_OUT);
					l.onModelUpdate(UserEvent.LOGGED_OUT, user);
				}
			}
		};
	}
	 
	public UserModel findUserByUID(String uid){
		return null;
	}
}
