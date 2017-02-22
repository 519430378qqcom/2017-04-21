package cn.youmi.pay.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import cn.youmi.account.event.UserEvent;
import cn.youmi.account.manager.UserManager;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.activity.BaseSwipeBackActivity;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.manager.ModelManager.ModelStatusListener;
import cn.youmi.framework.model.ResultModel;
import cn.youmi.framework.util.ToastU;
import cn.youmi.pay.event.PayFromClassesEvent;
import cn.youmi.pay.event.PayResultEvent;
import cn.youmi.pay.event.PaySuccTypeEvent;
import cn.youmi.pay.manager.PayResultManager;
import cn.youmi.pay.manager.PaySuccTypeManager;
import cn.youmi.pay.model.PayResult;
import cn.youmi.pay.model.WeiXinPayResultModel;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;

/**
 * 付款界面
 * 
 * @author tangxiyong 2014-3-24上午10:58:02
 * 
 */
public abstract class AbstractPayActivity extends BaseSwipeBackActivity {
	protected IWXAPI api;

	private static final int PLUGIN_NOT_INSTALLED = -1;
	private static final int PLUGIN_NEED_UPGRADE = 2;

	/**
	 * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
	 */
	private String mMode = "00";

	public static final String KEY_FROM_CLASSES = "key.fromclasses";
	public static final String KEY_PAY_TYPE = "key.paytype";

	/** startActivityForResult 打开web支付 */
	public static final int PAYDOING_REQUEST_CODE_WEB_PAY = 1;
	/** onActivityResult web结果 成功 */
	public static final int PAYDOING_RESULT_CODE_WEB_PAY_ISSUCC = 115;
	/** onActivityResult web结果 不知道成功，刷新 */
	public static final int PAYDOING_RESULT_CODE_WEB_PAY_REFRESH = 116;

	/** startActivityForResult 充值 */
	public static final int PAYDOING_REQUEST_CODE_RECHARGE_PAY = 1000;
	/** onActivityResult 充值结果 刷新 */
	public static final int PAYDOING_RESULT_CODE_RECHARGE_PAY_REFRESH = 999;

	/** startActivityForResult 充值打开支付界面 */
	public static final int RECHARGE_REQUEST_CODE = 2;
	/** onActivityResult 充值结果 */
	public static final int RECHARGE_RESULT_CODE = 118;

	/** 微信 支付AppID wx6eb28a78527d2f69 */
	protected abstract String weixinPayAppId();

	protected abstract PayFromClassesEvent getPayType();

	/** 支付的渠道来源，用于刷新指定页面 */
	protected abstract PaySuccTypeEvent getPaySuccType();
	
	public Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		api = WXAPIFactory.createWXAPI(AbstractPayActivity.this, weixinPayAppId());
		api.registerApp(weixinPayAppId());

		UserManager.getInstance().registerListener(userListener);
		PayResultManager.getInstance().registerListener(payResultListener);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		UserManager.getInstance().unregisterListener(userListener);
		PayResultManager.getInstance().unregisterListener(payResultListener);
	}

	/**
	 * 网页支付
	 * 
	 * @param payUrl
	 */
	protected void webPay(String payUrl, Activity activity) {
		Intent intent = new Intent(AbstractPayActivity.this, PayWebActivity.class);
		intent.putExtra(PayWebActivity.WEB_URL, payUrl);
		activity.startActivityForResult(intent, 1);
	}

	/**
	 * 帐户余额直接支付
	 * 
	 * @param payUrl
	 */
	protected void youmiPay(String payUrl) {

		GetRequest<ResultModel> request = new GetRequest<ResultModel>(payUrl, GsonParser.class, ResultModel.class, ConfirmurlListener);
		request.go();
	}

	private Listener<ResultModel> ConfirmurlListener = new Listener<ResultModel>() {

		@Override
		public void onResult(AbstractRequest<ResultModel> request, ResultModel t) {
			if (null != t) {
				if (t.isSucc()) {// 支付成功
					PaySuccTypeManager.getInstance().setPayResult(getPaySuccType());
				} else {
					ToastU.showLong(AbstractPayActivity.this, t.showMsg());
				}
			}
		}

		@Override
		public void onError(AbstractRequest<ResultModel> requet,
				int statusCode, String body) {
			ToastU.showLong(AbstractPayActivity.this, body);
		}
	};

	/**
	 * 支付宝 客户端支付
	 * 
	 * @param payUrl
	 */
	protected void alipayPay(String payUrl) {

		GetRequest<ResultModel> request = new GetRequest<ResultModel>(payUrl, GsonParser.class, ResultModel.class, alipayListener);
		request.go();
	}

	private Listener<ResultModel> alipayListener = new Listener<ResultModel>() {

		@Override
		public void onResult(AbstractRequest<ResultModel> request, ResultModel t) {
			if (null != t) {
				if (t.isSucc()) {
					// 生成支付信息成功
					// start the pay.
					// Result.sResult = null;
					final String orderInfo = t.getR();
					new Thread() {
						public void run() {
							String result = new PayTask(AbstractPayActivity.this).pay(orderInfo);
//							String result = new AliPay(AbstractPayActivity.this, mHandler).pay(orderInfo);
							PayResultManager.getInstance().setPayResult(
									PayResultEvent.RQF_ALIPAY, result);
						}
					}.start();

				} else {
					ToastU.showLong(AbstractPayActivity.this, t.getM());
				}
			}
		}

		@Override
		public void onError(AbstractRequest<ResultModel> requet,
				int statusCode, String body) {
			ToastU.showLong(AbstractPayActivity.this, body);
		}
	};

	/**
	 * 银联 客户端支付
	 * 
	 * @param payUrl
	 */
	protected void unionPay(String payUrl) {

		GetRequest<ResultModel> request = new GetRequest<ResultModel>(payUrl, GsonParser.class, ResultModel.class, unionPayListener);
		request.go();
	}

	private Listener<ResultModel> unionPayListener = new Listener<ResultModel>() {

		@Override
		public void onResult(AbstractRequest<ResultModel> request, ResultModel t) {
			if (null != t) {
				if (t.isSucc()) {
					// 步骤1：从网络开始,获取交易流水号即TN
					PayResultManager.getInstance().setPayResult(PayResultEvent.RQF_UNPAY, t.getR());
				} else {
					ToastU.showLong(AbstractPayActivity.this, t.getM());
				}
			}
		}

		@Override
		public void onError(AbstractRequest<ResultModel> requet,
				int statusCode, String body) {
			ToastU.showLong(AbstractPayActivity.this, body);
		}
	};

	/**
	 * 微信 客户端支付
	 * 
	 * @param payUrl
	 */
	protected void weixinPay(String payUrl) {

		if (api.isWXAppInstalled()) {
			GetRequest<WeiXinPayResultModel> request = new GetRequest<WeiXinPayResultModel>(
					payUrl, GsonParser.class, WeiXinPayResultModel.class, weixinPayListener);
			request.go();
		} else {
			ToastU.showLong(AbstractPayActivity.this, "未安装微信或微信版本号过低");
			PaySuccTypeManager.getInstance().setPayResult(PaySuccTypeEvent.CANCEL);
		}

	}

	private Listener<WeiXinPayResultModel> weixinPayListener = new Listener<WeiXinPayResultModel>() {

		@Override
		public void onResult(AbstractRequest<WeiXinPayResultModel> request,
				WeiXinPayResultModel t) {
			if (null != t) {
				if (t.isSucc()) {
					String wxAppID = t.getAppid();
					PayReq req = new PayReq();
					req.appId = wxAppID;
					req.partnerId = t.getPartnerid();
					req.prepayId = t.getPrepayid();
					req.nonceStr = t.getNoncestr();
					req.timeStamp = t.getTimestamp();
					req.packageValue = t.getPackagename();
					req.sign = t.getSign();
					api.sendReq(req);

				} else {
					ToastU.showLong(AbstractPayActivity.this, t.getM());
				}
			}
		}

		@Override
		public void onError(AbstractRequest<WeiXinPayResultModel> requet,
				int statusCode, String body) {
			ToastU.showLong(AbstractPayActivity.this, body);
		}
	};

	public boolean copyApkFromAssets(Context context, String fileName,
			String path) {
		boolean copyIsFinish = false;
		try {
			InputStream is = context.getAssets().open(fileName);
			File file = new File(path);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();
			copyIsFinish = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return copyIsFinish;
	}
	
	
	private ModelStatusListener<PayResultEvent, String> payResultListener = new ModelStatusListener<PayResultEvent, String>() {
		private String c_s;
		@Override
		public void onModelGet(PayResultEvent key, String models) {
		}

		@Override
		public void onModelUpdate(PayResultEvent key, String model) {

			switch (key) {
			case RQF_WEIXIN:
				String errCode = model;
				if ("0".equals(errCode)) {// 支付成功
					PaySuccTypeManager.getInstance().setPayResult(getPaySuccType());
				} else if ("-2".equals(errCode)) {
					PaySuccTypeManager.getInstance().setPayResult(PaySuccTypeEvent.CANCEL);
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							ToastU.showShort(AbstractPayActivity.this, "用户取消支付");
						}
					});
				} else {
					PaySuccTypeManager.getInstance().setPayResult(PaySuccTypeEvent.CANCEL);
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							ToastU.showShort(AbstractPayActivity.this, "未知错误请重试");
						}
					});
				}
				break;
			case RQF_UNPAY:
				String tn = "";
				if (model == null || model.length() == 0) {
					AlertDialog.Builder builder = new AlertDialog.Builder(AbstractPayActivity.this);
					builder.setTitle("错误提示");
					builder.setMessage("网络连接失败,请重试!");
					builder.setNegativeButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					builder.create().show();
				} else {
					tn = model;
					// 步骤2：通过银联工具类启动支付插件
					// mMode参数解释：
					// 0 - 启动银联正式环境
					// 1 - 连接银联测试环境
					int ret = UPPayAssistEx.startPay(AbstractPayActivity.this, null, null, tn, mMode);
					if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
						// 需要重新安装控件
						AlertDialog.Builder builder = new AlertDialog.Builder(AbstractPayActivity.this);
						builder.setTitle("提示");
						builder.setMessage("完成购买需要安装银联支付控件，是否安装？");

						builder.setNegativeButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										if (copyApkFromAssets(
												AbstractPayActivity.this, "UPPayPluginEx.apk",
												Environment.getExternalStorageDirectory().getAbsolutePath() + "/UPPayPluginEx.apk")) {
											Intent intent = new Intent(Intent.ACTION_VIEW);
											intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											intent.setDataAndType(
													Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/UPPayPluginEx.apk"),
													"application/vnd.android.package-archive");
											AbstractPayActivity.this.startActivity(intent);
										}
									}
								});

						builder.setPositiveButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						builder.create().show();

					}
				}
				break;
			case RQF_ALIPAY:
				PayResult payResult = new PayResult(model);
//				PayResult payResult = new PayResult((String) msg.obj);
				
				
				String resultStatus = payResult.getResultStatus();
				final String resultMsg = payResult.getMemo();

//				String resultStatus = null;
//				Pattern alipay_r = Pattern.compile("resultStatus=\\{(\\d+)\\};");
//				Matcher result = alipay_r.matcher(model);
//				while (result.find()) {
//					resultStatus = result.group(1);
//				}
				if (TextUtils.equals(resultStatus, "9000")) {// 支付成功
					PaySuccTypeManager.getInstance().setPayResult(getPaySuccType());
				} else {
					if (TextUtils.equals(resultStatus, "8000")) {

						mHandler.post(new Runnable() {
							@Override
							public void run() {
								ToastU.showShort(AbstractPayActivity.this, "支付结果确认中");
							}
						});
					} else {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								ToastU.showShort(AbstractPayActivity.this, resultMsg);
							}
						});
						PaySuccTypeManager.getInstance().setPayResult(PaySuccTypeEvent.CANCEL);
					}
//					PaySuccTypeManager.getInstance().setPayResult(PaySuccTypeEvent.CANCEL);
//					Pattern alipay_ = Pattern.compile("memo=\\{(.*)\\};");
//					Matcher result_m = alipay_.matcher(model);
//					while (result_m.find()) {
//						c_s = result_m.group(1);
//					}
//					if (!TextUtils.isEmpty(c_s)) {
//						mHandler.post(new Runnable() {
//							@Override
//							public void run() {
//								ToastU.showShort(AbstractPayActivity.this, c_s);
//							}
//						});
//
//					} else {
//						mHandler.post(new Runnable() {
//							@Override
//							public void run() {
//								ToastU.showShort(AbstractPayActivity.this,"操作错误");
//							}
//						});
//
//					}
				}
				break;
				
			case WEB_SUCC:
				PaySuccTypeManager.getInstance().setPayResult(getPaySuccType());
				break;
			case WEB_REFRESH:
				PaySuccTypeManager.getInstance().setPayResult(PaySuccTypeEvent.CANCEL);
				break;

			default:
				break;
			}
		}

		@Override
		public void onModelsGet(PayResultEvent key, List<String> models) {
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
				AbstractPayActivity.this.finish();
				break;
//			case PAY_HOME_COURSE:
//				AbstractPayActivity.this.finish();
//				break;
//			case PAY_HOME_VIP:
//				AbstractPayActivity.this.finish();
//				break;
//			case PAY_ZHUANTI:
//				AbstractPayActivity.this.finish();
//				break;
//			case PAY_LECTURER:
//				AbstractPayActivity.this.finish();
//				break;
//			case PAY_HOME_ORDER:
//				AbstractPayActivity.this.finish();
//				break;
			case PAY_MINE_RECHARGE:
				Intent intent = getIntent();
				intent.putExtra("coupon_type", "充值成功");
				AbstractPayActivity.this.setResult(118, intent);
				AbstractPayActivity.this.finish();
				break;
			case PAY_MINEANDORDER_RECHARGE:
				Intent i = getIntent();
				i.putExtra("coupon_type", "充值成功");
				AbstractPayActivity.this.setResult(118, i);
				AbstractPayActivity.this.finish();
				break;
			default:
				break;
			}

		}

		@Override
		public void onModelsGet(UserEvent key, List<UserModel> models) {
		}
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			switch (resultCode) {// wap
			case 115:// 网页支付成功
				PayResultManager.getInstance().setPayResult(PayResultEvent.WEB_SUCC);
//				PaySuccTypeManager.getInstance().setPayResult(getPaySuccType());
				return;
			case 116:// 网页“返回键” 判断是否支付成功
				PayResultManager.getInstance().setPayResult(
						PayResultEvent.WEB_REFRESH);
				return;

			default:
				break;
			}
			break;
		default:
			break;

		}

		// 步骤3：处理银联手机支付控件返回的支付结果
		if (data == null) {
			return;
		}
		String msg = "";
		/*
		 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
		String str = data.getExtras().getString("pay_result");
		if (null != str && !"".equals(str)) {
			if (str.equalsIgnoreCase("success")) {
				// msg = "支付成功！";
				// paySuccRefresh();
				PaySuccTypeManager.getInstance().setPayResult(getPaySuccType());
			} else if (str.equalsIgnoreCase("fail")) {
				msg = "支付失败！";
				PaySuccTypeManager.getInstance().setPayResult(PaySuccTypeEvent.CANCEL);
			} else if (str.equalsIgnoreCase("cancel")) {
				msg = "用户取消了支付";
				PaySuccTypeManager.getInstance().setPayResult(PaySuccTypeEvent.CANCEL);
			}

			if (!str.equalsIgnoreCase("success")) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("支付结果通知");
				builder.setMessage(msg);
				builder.setInverseBackgroundForced(true);
				// builder.setCustomTitle();
				builder.setNegativeButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();
			}
		}

	
	}

}
