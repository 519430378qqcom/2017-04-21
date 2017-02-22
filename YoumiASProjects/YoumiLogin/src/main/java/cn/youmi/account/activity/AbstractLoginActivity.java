package cn.youmi.account.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.auth.QQAuth;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cn.youmi.account.beans.UserLoginBeans;
import cn.youmi.account.event.ClassesEvent;
import cn.youmi.account.event.LoginModeEvent;
import cn.youmi.account.event.UserEvent;
import cn.youmi.account.event.WeiXinLoginTokenEvent;
import cn.youmi.account.manager.OAuthLoginResultManager;
import cn.youmi.account.manager.UserManager;
import cn.youmi.account.manager.WeiXinLoginTokenManager;
import cn.youmi.account.manager.WeiXinTokenKeyManager;
import cn.youmi.account.manager.YoumiLoginResultManager;
import cn.youmi.account.model.OAuthResultModel;
import cn.youmi.account.model.UserModel;
import cn.youmi.account.model.WXTokenModel;
import cn.youmi.framework.activity.BaseSwipeBackActivity;
import cn.youmi.framework.main.BaseApplication;
import cn.youmi.framework.manager.ModelManager.ModelStatusListener;
import cn.youmi.framework.util.PreferenceUtils;

/**
 * 登录界面
 *
 * @author tangxiyong 2013-11-15上午10:15:37
 *
 */
public abstract class AbstractLoginActivity extends BaseSwipeBackActivity {

	public static final String FIRST_OUTH_LOGIN = "isFirstOuthLogin";

	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

//	public int WXOAuthCode = 0;
//	public String WXToken = "";

	private AuthInfo mWeiboAuth;
	public SsoHandler mSsoHandler;
	private Oauth2AccessToken mAccessToken;

	/** QQ登录 */
	public static QQAuth mQQAuth;
	private Tencent mTencent;
	/** 登录方式*/
	public LoginModeEvent loginMode;

	/** 微信 登录AppID */
	protected abstract String weixinAppId();
	/** 微信 登录AppKey*/
	protected abstract String weixinAppKey();
	/** QZoneAPP_ID*/
	protected abstract String qqAppId();
	/** 新浪APP_KEY*/
	protected abstract String sinaWeiboAppKey();
	/** 新浪授权回调URL*/
	protected abstract String sinaWeiboRediteUrl();
	/** 新浪授权作用域*/
	protected abstract String sinaWeiboScope();
	/** 从哪个页面启动登录的*/
	protected abstract ClassesEvent getClassesFrom();
	/** 渠道号 */
	protected abstract String getChannel();


	/**
	 * 新浪微博第三方授权成功与服务器确认登录(如果参数顺序 不 统一 或者链接不一致)
	 * http://passport.youmi.cn/mobile/oauthlogin?type=%s&connectid=%s&token=%s&secret=%s&refresh_token=%s&ref=%s
	 * @return Method = get
	 */
	protected abstract String getOAuthSinaWeiboLoginUrl();
	/**
	 * 新浪微博第三方授权成功与服务器确认登录(如果参数顺序 不 统一 或者链接不一致)
	 * http://passport.youmi.cn/mobile/oauthlogin?type=%s&connectid=%s&token=%s&secret=%s&refresh_token=%s&ref=%s
	 * @return Method = post
	 */
	protected abstract String postOAuthSinaWeiboLoginUrl();
	/**
	 * 腾讯QQ第三方授权成功与服务器确认登录(如果参数顺序 不 统一 或者链接不一致)
	 * http://passport.youmi.cn/mobile/oauthlogin?type=%s&connectid=%s&token=%s&secret=%s&refresh_token=%s&ref=%s
	 * @return Method = get
	 */
	protected abstract String getOAuthQQLoginUrl();
	/**
	 * 腾讯QQ第三方授权成功与服务器确认登录(如果参数顺序 不 统一 或者链接不一致)
	 * http://passport.youmi.cn/mobile/oauthlogin?type=%s&connectid=%s&token=%s&secret=%s&refresh_token=%s&ref=%s
	 * @return Method = post
	 */
	protected abstract String postOAuthQQLoginUrl();
	/**
	 * 微信第三方授权成功与服务器确认登录(如果参数顺序 不 统一 或者链接不一致)
	 * http://passport.youmi.cn/mobile/oauthlogin?type=%s&connectid=%s&token=%s&secret=%s&refresh_token=%s&ref=%s
	 * @return Method = get
	 */
	protected abstract String getOAuthWeiXinLoginUrl();
	/**
	 * 微信第三方授权成功与服务器确认登录(如果参数顺序 不 统一 或者链接不一致)
	 * http://passport.youmi.cn/mobile/oauthlogin?type=%s&connectid=%s&token=%s&secret=%s&refresh_token=%s&ref=%s
	 * @return Method = post
	 */
	protected abstract String postOAuthWeiXinLoginUrl();

	/**
	 * http://passport.youmi.cn/login/login/?%s&expire=315360000
	 * @return Method = get
	 */
	protected abstract String getYoumiLoginUrl();

	/**
	 * https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code
	 * @return
	 */
	protected abstract String getWeiXinTokenUrl();

	protected abstract String getOAuthType();
	/**
	 * method= GET && POST
	 * @return
	 */
	private  String getMethod = "POST";

	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		progressShow();

		api = WXAPIFactory.createWXAPI(this, weixinAppId(), false);
		api.registerApp(weixinAppId());

		mQQAuth = QQAuth.createInstance(qqAppId(), BaseApplication.getApplication());
		mTencent = Tencent.createInstance(qqAppId(), BaseApplication.getApplication());

		mWeiboAuth = new AuthInfo(this, sinaWeiboAppKey(), sinaWeiboRediteUrl(), sinaWeiboScope());
		getMethod = UserManager.getInstance().getMethod();
	}

	@Override
	protected void onStart() {
		super.onStart();
		WeiXinLoginTokenManager.getInstance().registerListener(weiXinTokenListener);
		YoumiLoginResultManager.getInstance().registerListener(youmiLoginListener);
		WeiXinTokenKeyManager.getInstance().registerListener(weiXinTokenKeyListener);
		OAuthLoginResultManager.getInstance().registerListener(oAuthLoginListener);
		UserManager.getInstance().registerListener(userInfoListener);
	}

	@Override
	protected void onStop() {
		super.onStop();
		WeiXinLoginTokenManager.getInstance().unregisterListener(weiXinTokenListener);
		YoumiLoginResultManager.getInstance().unregisterListener(youmiLoginListener);
		WeiXinTokenKeyManager.getInstance().unregisterListener(weiXinTokenKeyListener);
		OAuthLoginResultManager.getInstance().unregisterListener(oAuthLoginListener);
		UserManager.getInstance().unregisterListener(userInfoListener);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			progressFinish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (mTencent != null) {
				mTencent.onActivityResult(requestCode, resultCode, data);
			}
		}
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	public void progressDissmiss() {
		if(mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	public void progressShow() {
		mProgressDialog = new ProgressDialog(AbstractLoginActivity.this);
		mProgressDialog.setTitle("正在登录");
		mProgressDialog.setMessage("请稍候... ...");
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.show();
	}



	/**
	 * umiwi帐户登录
	 * method = get
	 */
	protected void getYoumiLogin() {
		YoumiLoginResultManager.getInstance().getUserLoginPassport(getYoumiLoginUrl(), getClassesFrom());
		loginMode = LoginModeEvent.LOGIN_YOUMI;
	}

	/**
	 * umiwi帐户登录
	 * method = post
	 */
	protected void postYoumiLogin(HashMap<String, String> param) {
		YoumiLoginResultManager.getInstance().postUserLoginPassport(getYoumiLoginUrl(), getClassesFrom(), param);
		loginMode = LoginModeEvent.LOGIN_YOUMI;
	}

	/***/
	protected void QQLogin() {
		if (!mQQAuth.isSessionValid()) {
			IUiListener listener = new QQLoginUiListener();
			// mQQAuth.login(this, "all", listener);
			// mTencent.loginWithOEM(this, "all",
			// listener,"10000144","10000144","xxxx");
			mTencent.login(this, "all", listener);
		} else {
			mQQAuth.logout(this);
		}
		loginMode = LoginModeEvent.LOGIN_QQ;
	}

	/**
	 *
	 * @param scope:snsapi_userinfo
	 * @param state:umiwi
	 */
	protected void WeiXinLogin(String scope, String state) {
		if (api.isWXAppInstalled()) {
			// 微信应用授权
			SendAuth.Req authReq = new SendAuth.Req();
			authReq.scope = scope;
			authReq.state = state;
			api.sendReq(authReq);
		} else {
			// 提示用户安装微信
			Toast.makeText(this, "授权失败,请检查微信版本", Toast.LENGTH_SHORT).show();
			progressFinish();
		}
		loginMode = LoginModeEvent.LOGIN_WEIXIN;
	}

	protected void WeiXinLogin() {
		if (api.isWXAppInstalled()) {
			// 微信应用授权
			SendAuth.Req authReq = new SendAuth.Req();
			authReq.scope = "snsapi_userinfo";
			authReq.state = "youmi";
			api.sendReq(authReq);
		} else {
			// 提示用户安装微信
			Toast.makeText(this, "授权失败,请检查微信版本", Toast.LENGTH_SHORT).show();
			progressFinish();
		}
		loginMode = LoginModeEvent.LOGIN_WEIXIN;
	}


	/***/
	protected void SinaWeiboLogin() {
		mSsoHandler = new SsoHandler(this, mWeiboAuth);
		mSsoHandler.authorize(new WeiboListener());
		loginMode = LoginModeEvent.LOGIN_SINA_WEIBO;
	}



	/**
	 * qq登录回调
	 *
	 * @author tangxiong
	 * @version 2014年6月5日 下午5:37:29
	 */
	private class QQLoginUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			doComplete((JSONObject) response);
		}

		protected void doComplete(JSONObject values) {
			try {
				if (getMethod.equals("GET")) {
					OAuthLoginResultManager.getInstance().getOAuthCookie(
							String.format(getOAuthQQLoginUrl(), getOAuthType(),
									values.getString("openid"),
									values.getString("access_token"), "", "",
									getChannel()), getClassesFrom());
				} else if (getMethod.equals("POST")) {//type=%s&connectid=%s&token=%s&secret=%s&refresh_token=%s&ref=%s
					HashMap<String, String> param = new HashMap<String, String>();
					param.put("type", getOAuthType());// 4
					param.put("connectid", values.getString("openid"));
					param.put("token", values.getString("access_token"));
					param.put("ref", getChannel());
					OAuthLoginResultManager.getInstance().postOAuthCookie(
							postOAuthQQLoginUrl(), getClassesFrom(), param);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onError(UiError e) {
			Toast.makeText(AbstractLoginActivity.this, e.errorMessage, Toast.LENGTH_SHORT).show();
			progressFinish();
		}

		@Override
		public void onCancel() {
			Toast.makeText(AbstractLoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
			progressFinish();
		}
	}

	/**
	 * 微博回调
	 *
	 * @author tangxiong
	 * @version 2014年6月5日 下午5:35:31
	 */
	private class WeiboListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			// 从 Bundle 中解析 Token
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				if (getMethod.equals("GET")) {// type=2
					OAuthLoginResultManager.getInstance().getOAuthCookie(
							String.format(getOAuthSinaWeiboLoginUrl(),
									getOAuthType(), mAccessToken.getUid(),
									mAccessToken.getToken(), "","",
									getChannel()), getClassesFrom());
				} else if (getMethod.equals("POST")) {//type=%s&connectid=%s&token=%s&secret=%s&refresh_token=%s&ref=%s
					HashMap<String, String> param = new HashMap<String, String>();
					param.put("type", getOAuthType());// 2
					param.put("connectid", mAccessToken.getUid());
					param.put("token", mAccessToken.getToken());
					param.put("ref", getChannel());
					OAuthLoginResultManager.getInstance().postOAuthCookie(
							postOAuthSinaWeiboLoginUrl(), getClassesFrom(),
							param);
				}
			} else {
				// 以下几种情况，您会收到 Code：
				// 1. 当您未在平台上注册的应用程序的包名与签名时；
				// 2. 当您注册的应用程序包名与签名不正确时；
				// 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
//				 String code = values.getString("code");
//				 String message = "授权失败";
//				 if (!TextUtils.isEmpty(code)) {
//				 message = message + "\nObtained the code: " + code;
//				 }
				// Toast.makeText(LoginActivity.this, message,
				// Toast.LENGTH_SHORT).show();

				Toast.makeText(AbstractLoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
				progressFinish();
			}
		}

		@Override
		public void onCancel() {
			Toast.makeText(AbstractLoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
			progressFinish();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(AbstractLoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
			progressFinish();
		}
	}


	/**
	 * 接收微信授权返回的token
	 */
	private ModelStatusListener<WeiXinLoginTokenEvent, String> weiXinTokenKeyListener = new ModelStatusListener<WeiXinLoginTokenEvent, String>() {

		@Override
		public void onModelGet(WeiXinLoginTokenEvent key, String models) {}

		@Override
		public void onModelUpdate(WeiXinLoginTokenEvent key, String model) {
			switch (key) {
				case TOKEN_KEY:
					WeiXinLoginTokenManager.getInstance().getWXLoginToken(
							String.format(getWeiXinTokenUrl(), weixinAppId(),
									weixinAppKey(), model));
					break;

				default:
					break;
			}
		}

		@Override
		public void onModelsGet(WeiXinLoginTokenEvent key, List<String> models) {}
	};

	/**
	 * 通过微信的授权token进行登录 
	 * @param wxtoken
	 */
	private ModelStatusListener<WeiXinLoginTokenEvent, WXTokenModel> weiXinTokenListener = new ModelStatusListener<WeiXinLoginTokenEvent, WXTokenModel>() {

		@Override
		public void onModelGet(WeiXinLoginTokenEvent key, WXTokenModel models) {}

		@Override
		public void onModelUpdate(WeiXinLoginTokenEvent key, WXTokenModel model) {
			switch (key) {
				case TOKEN_SUCC:
					if (getMethod.equals("GET")) {// type=5
						OAuthLoginResultManager.getInstance().getOAuthCookie(
								String.format(String.format(
										getOAuthWeiXinLoginUrl(), getOAuthType(),
										model.getOpenid(), model.getAccesstoken(),weixinAppKey(),model.getRefreshtoken(),
										getChannel()), getClassesFrom()) +"&unionid="+model.getUnionid(),
								getClassesFrom());
					} else if (getMethod.equals("POST")) {//type=%s&connectid=%s&token=%s&secret=%s&refresh_token=%s&ref=%s
						HashMap<String, String> param = new HashMap<String, String>();
						param.put("type", getOAuthType());// 5
						param.put("connectid", model.getOpenid());
						param.put("token", model.getAccesstoken());
						param.put("secret", weixinAppKey());
						param.put("refresh_token", model.getRefreshtoken());
						param.put("unionid", model.getUnionid());
						param.put("ref", getChannel());
						OAuthLoginResultManager.getInstance().postOAuthCookie(
								postOAuthWeiXinLoginUrl(), getClassesFrom(), param);
					}
					break;
				case TOKEN_FAIL:
					Toast.makeText(AbstractLoginActivity.this, model.getErrmsg(), Toast.LENGTH_SHORT).show();
					progressFinish();
					break;
				case ERROR:
					Toast.makeText(AbstractLoginActivity.this, model.getErrmsg(), Toast.LENGTH_SHORT).show();
					progressFinish();
					break;

				default:
					break;
			}

		}

		@Override
		public void onModelsGet(WeiXinLoginTokenEvent key, List<WXTokenModel> models) {}
	};

	private ModelStatusListener<ClassesEvent, OAuthResultModel> oAuthLoginListener = new ModelStatusListener<ClassesEvent, OAuthResultModel>() {

		@Override
		public void onModelGet(ClassesEvent key, OAuthResultModel models) {}

		@Override
		public void onModelUpdate(ClassesEvent key, OAuthResultModel model) {

			switch (key) {
				case LOGIN:
					if (!"login".equals(model.getoAuthMessage().getMethod())) {// 首次注册
						PreferenceUtils.setPrefBoolean(AbstractLoginActivity.this, FIRST_OUTH_LOGIN, true);
					}
					UserManager.getInstance().getUserInfoSave(UserEvent.HOME_LOGIN, model.getoAuthMessage().getUid());
					break;
				case ERROR:
					Toast.makeText(AbstractLoginActivity.this, model.showMsg(), Toast.LENGTH_SHORT).show();
					progressFinish();
					break;
				case FAIL:
					Toast.makeText(AbstractLoginActivity.this, model.showMsg(), Toast.LENGTH_SHORT).show();
					progressFinish();
					break;

				default:
					break;
			}
		}

		@Override
		public void onModelsGet(ClassesEvent key, List<OAuthResultModel> models) {}
	};

	/** 优米账号登录*/
	private ModelStatusListener<ClassesEvent, UserLoginBeans> youmiLoginListener = new ModelStatusListener<ClassesEvent, UserLoginBeans>() {

		@Override
		public void onModelGet(ClassesEvent key, UserLoginBeans models) {}

		@Override
		public void onModelUpdate(ClassesEvent key, UserLoginBeans model) {
			switch (key) {
				case LOGIN:
					UserManager.getInstance().getUserInfoSave(UserEvent.HOME_LOGIN);
					break;
				case ERROR:
					Toast.makeText(AbstractLoginActivity.this, model.showMsg(), Toast.LENGTH_SHORT).show();
					progressFinish();
					break;
				case FAIL:
					Toast.makeText(AbstractLoginActivity.this, model.showMsg(), Toast.LENGTH_SHORT).show();
					progressFinish();
					break;

				default:
					break;
			}

		}

		@Override
		public void onModelsGet(ClassesEvent key, List<UserLoginBeans> models) {}
	};


	private ModelStatusListener<UserEvent, UserModel> userInfoListener = new ModelStatusListener<UserEvent, UserModel>() {

		@Override
		public void onModelGet(UserEvent key, UserModel models) {}

		@Override
		public void onModelUpdate(UserEvent key, UserModel model) {
			switch (key) {
				case HOME_LOGIN:
					AbstractLoginActivity.this.finish();
					progressDissmiss();
					break;

				default:
					break;
			}
		}

		@Override
		public void onModelsGet(UserEvent key, List<UserModel> models) {}
	};

	private void progressFinish() {
		AbstractLoginActivity.this.finish();
		progressDissmiss();
	}

}