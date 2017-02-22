package com.umiwi.ui.activity;

import java.util.List;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;
import cn.youmi.account.event.UserEvent;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.manager.ModelManager.ModelStatusListener;
import cn.youmi.framework.util.ToastU;
import cn.youmi.pay.event.PayFromClassesEvent;
import cn.youmi.pay.event.PaySuccTypeEvent;
import cn.youmi.pay.manager.PaySuccTypeManager;
import cn.youmi.pay.ui.AbstractPayActivity;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.YoumiRoomUserManager;

/**
 * @author tangxiyong
 * @version 2015-4-17 下午2:01:52 
 */
public class YMPayActivity extends AbstractPayActivity {

	private ProgressDialog progressDialog;

	public static final String PAY_TYPE = "pay.type";
	public static final String PYA_URL = "pay.url";
	/**
	 * 支付宝
	 */
	public static final int PAY_ALIPAY = 1;
	/**
	 * 微信
	 */
	public static final int PAY_WEIXIN = 2;
	/**
	 * 银联支付
	 */
	public static final int PAY_UNIONPAY = 3;
	/**
	 * 优米余额支付
	 */
	public static final int PAY_YOUMI = 4;
	/**
	 * 网页支付
	 */
	public static final int PAY_WEB = 5;

	@Override
	protected String weixinPayAppId() {
		return UmiwiAPI.WEIXIN_APP_ID_PAY;
	}

	@Override
	protected PayFromClassesEvent getPayType() {
		return PayFromClassesEvent.DEFAULT_VALUE;
	}

	@Override
	protected PaySuccTypeEvent getPaySuccType() {
		return PaySuccTypeEvent.DEFAULT_VALUE;
	}

	private String payUrl;

	private ModelStatusListener<PaySuccTypeEvent, String> payResultListener = new ModelStatusListener<PaySuccTypeEvent, String>() {

		@Override
		public void onModelGet(PaySuccTypeEvent key, String models) {
		}

		@Override
		public void onModelUpdate(PaySuccTypeEvent key, String model) {
			switch (key) {
			case DEFAULT_VALUE:
				YoumiRoomUserManager.getInstance().getUserInfoSave(UserEvent.PAY_SUCC);
//				YoumiRoomUserManager.getInstance().getUserInfoSave(UserEvent.PAY_SUCC, YoumiRoomUserManager.getInstance().getUser().getUid());
				break;
			case CANCEL:
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						ToastU.showShort(YMPayActivity.this, "取消支付");
					}
				});
				dismissActivity();
				break;

			default:
				break;
			}
		}

		@Override
		public void onModelsGet(PaySuccTypeEvent key, List<String> models) {
		}
	};

	private ModelStatusListener<UserEvent, UserModel> userListener = new ModelStatusListener<UserEvent, UserModel>() {

		@Override
		public void onModelGet(UserEvent key, UserModel models) {
		}

		@Override
		public void onModelUpdate(UserEvent key, UserModel model) {
			switch (key) {
			case PAY_SUCC:
				ToastU.showShort(YMPayActivity.this, "支付成功");
				dismissActivity();
				break;

			default:
				break;
			}
		}

		@Override
		public void onModelsGet(UserEvent key, List<UserModel> models) {
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setTheme(R.style.Theme_Umiwi_Translucent);
		super.onCreate(savedInstanceState);

		progressDialog = new ProgressDialog(this);
		progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(false);
		progressDialog.setOnKeyListener(keyBackListener);
		progressDialog.show();
		payUrl = getIntent().getStringExtra(PYA_URL);

		switch (getIntent().getIntExtra(PAY_TYPE, 0)) {
		case PAY_ALIPAY:
			alipayPay(payUrl);
			break;

		case PAY_UNIONPAY:
			unionPay(payUrl);
			break;

		case PAY_WEIXIN:
			weixinPay(payUrl);
			break;

		case PAY_YOUMI:
			youmiPay(payUrl);
			break;

		case PAY_WEB:
			webPay(payUrl, this);
			break;

		default:
			ToastU.showShort(this, "支付信息错误，请选择其他方式");
			this.finish();
			break;
		}

		PaySuccTypeManager.getInstance().registerListener(payResultListener);
		YoumiRoomUserManager.getInstance().registerListener(userListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dismissProgerss();
		PaySuccTypeManager.getInstance().unregisterListener(payResultListener);
		YoumiRoomUserManager.getInstance().unregisterListener(userListener);
	}

	OnKeyListener keyBackListener = new OnKeyListener() {

		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				YMPayActivity.this.finish();
				return true;
			}
			return false;
		}
	};

	private void showProgress() {
		if (null != progressDialog) {
			progressDialog.show();
		} else {
			progressDialog = new ProgressDialog(YMPayActivity.this);
			progressDialog.show();
		}

	}

	private void dismissProgerss() {
		progressDialog.dismiss();
	}

	private void dismissActivity() {
		dismissProgerss();
		YMPayActivity.this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			YMPayActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
