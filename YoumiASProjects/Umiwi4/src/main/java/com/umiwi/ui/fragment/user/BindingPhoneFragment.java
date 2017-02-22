package com.umiwi.ui.fragment.user;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.youmi.account.event.UserEvent;
import cn.youmi.framework.dialog.MsgDialog;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.CookieDao;
import cn.youmi.framework.http.CookieModel;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.ResultParser;
import cn.youmi.framework.model.ResultModel;
import cn.youmi.framework.util.KeyboardUtils;
import cn.youmi.framework.util.ToastU;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.ImageCookieUtils;

/**
 * @author tangxiyong
 * @version 2014年9月3日 下午4:14:40
 */
public class BindingPhoneFragment extends BaseConstantFragment {
	private EditText phoneNum;
	private ImageView codeImage;
	private EditText imageCodeNum;
	private EditText phoneCodeNumEt;
	private TextView phoneCodeNumStr;
	private boolean isCodeNum;
	// private boolean isPhoneSuccCodeNum;
	private String strPhoneNum;
	private String strImageCodeNum;
	private TextView commitNext;

	private boolean isImageCodeNull;
	private boolean isImageCodeFail;
	private RelativeLayout imageCodeLayout;
	private LinearLayout phoneCodeLayout;
	private TextView countdown;

	@SuppressLint("InflateParams") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_binding_mobile, null);
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, "绑定手机号");
		
		this.setHasOptionsMenu(true);

		phoneNum = (EditText) view.findViewById(R.id.phone_number_et);//手机号码
		codeImage = (ImageView) view.findViewById(R.id.code_imageview);//图片验证码
		imageCodeNum = (EditText) view.findViewById(R.id.code_number_et);//输入图片验证码
		phoneCodeNumEt = (EditText) view
				.findViewById(R.id.phone_code_number_et);
		phoneCodeNumStr = (TextView) view
				.findViewById(R.id.phone_code_number_textview);//获取手机验证码的textview
		imageCodeLayout = (RelativeLayout) view
				.findViewById(R.id.imagecode_layout);
		phoneCodeLayout = (LinearLayout) view
				.findViewById(R.id.phonecode_layout);
		succ_context = (TextView) view.findViewById(R.id.succ_context);
		countdown = (TextView) view.findViewById(R.id.countdown);
		commitNext = (TextView) view.findViewById(R.id.commit_textview);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);

//		setActionBarRightButton("跳过");
		
		phoneCodeNumStr.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				strPhoneNum = phoneNum.getText().toString().trim();
				if (TextUtils.isEmpty(strPhoneNum)) {
					showMsg("手机号不能为空");
					return;
				}

				if (isCodeNum) {// 请求手机验证码
					strImageCodeNum = imageCodeNum.getText().toString().trim();
					if (TextUtils.isEmpty(strImageCodeNum)) {
						showMsg("验证码不能为空");
						return;
					}
					showBar();
					KeyboardUtils.hideKeyboard(getActivity());
					getPhoneAndImagesCode(strPhoneNum, strImageCodeNum);
				} else {
					showBar();
					KeyboardUtils.hideKeyboard(getActivity());
					getPhoneAndImagesCode(strPhoneNum, "");
				}
			}
		});

		commitNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String strPhoneCodeNum = phoneCodeNumEt.getText().toString()
						.trim();
				if (TextUtils.isEmpty(strPhoneCodeNum)) {
					showMsg("手机验证码不能为空");
					return;
				}
				KeyboardUtils.hideKeyboard(getActivity());
				showBar();
				getPhoneAndPhonesCode(strPhoneNum, strPhoneCodeNum);
			}
		});

		codeImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isImageCodeFail) {
					getImagecode();
				}
			}
		});
		return view;
	}

	protected void rightRuttonClickListener() {
		final MsgDialog dialog = new MsgDialog();
		dialog.setTitle("提示");
		dialog.setMessage("绑定手机号，账号更安全哦~");
		dialog.setPositiveButtonText("去绑定");
		dialog.setPositiveButtonListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismissAllowingStateLoss();
			}
		});
		dialog.setNegativeButtonText("暂不绑定");
		dialog.setNegativeButtonListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismissAllowingStateLoss();
				UmiwiContainerActivity csa = (UmiwiContainerActivity) getActivity();
				csa.slideToFinishActivity();
			}
		});
		dialog.show(getActivity().getSupportFragmentManager(),
				"phonedialog");
	}

	private int countNum = 60;
	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			countNum--;
			countdown.setText(Html.fromHtml("<font color='#7eb706'>"
					+ String.valueOf(countNum) + "</font>" + " 秒后重新获取"));
			if (countNum >= 1) {
				handler.postDelayed(this, 1000);
			} else {
				countdown.setVisibility(View.GONE);
				phoneCodeNumStr.setVisibility(View.VISIBLE);
				countNum = 60;
				handler.removeCallbacks(runnable);
			}

		}
	};

	/**
	 * 获取手机验证码
	 * 
	 * @param phoneNum
	 * @param imageCodeNum
	 *            图片验证码
	 */
	private void getPhoneAndImagesCode(String phoneNum, String imageCodeNum) {
		String loginURL;
		if (TextUtils.isEmpty(imageCodeNum)) {
			loginURL = String.format(UmiwiAPI.USER_MOBILE_CAPTCHA, phoneNum);
			isImageCodeNull = true;
		} else {
			loginURL = String.format(UmiwiAPI.USER_MOBILE_CAPTCHA_IMAGE, phoneNum, imageCodeNum);
			isImageCodeNull = false;
		}

		final String imageCodeNumTemp = imageCodeNum;

		GetRequest<ResultModel> get = new GetRequest<ResultModel>(loginURL,
				ResultParser.class, phoneListener);

		if (!TextUtils.isEmpty(imageCodeNumTemp)) {
			CookieModel model = CookieDao.getInstance(
					UmiwiApplication.getInstance()).getByName("passport");
			if (model != null && !TextUtils.isEmpty(model.getValue())) {
				get.setHeader("COOKIE", "passport=" + model.getValue()+";");
			}
		}

		HttpDispatcher.getInstance().go(get);

	}

	private Listener<ResultModel> phoneListener = new Listener<ResultModel>() {

		@Override
		public void onResult(AbstractRequest<ResultModel> request, ResultModel t) {

			if (!t.isSucc()) {
				codeImage.setVisibility(View.VISIBLE);
				getImagecode();
				if (!isImageCodeNull) {
					showMsg(t.showMsg());
				}
				imageCodeLayout.setVisibility(View.VISIBLE);
				isCodeNum = true;
				succ_context.setVisibility(View.GONE);
			} else {
				//点击获取手机验证码后的结果
				imageCodeLayout.setVisibility(View.GONE);
				phoneCodeLayout.setVisibility(View.VISIBLE);
				countdown.setVisibility(View.VISIBLE);
				phoneCodeNumEt.setVisibility(View.VISIBLE);
				phoneCodeNumStr.setVisibility(View.GONE);
				succ_context.setText("已经发送验证码到:"
						+ phoneNum.getText().toString().trim());
				succ_context.setVisibility(View.VISIBLE);
				handler.postDelayed(runnable, 1000);
				dismissBar();
			}
		}

		@Override
		public void onError(AbstractRequest<ResultModel> requet,
				int statusCode, String body) {
			ToastU.showShort(getActivity(), body);
			dismissBar();
		}

	};

	public Bitmap BytesToBimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	private void getImagecode() {

		ImageCookieUtils.getInstance().getImagecode(
				String.format(UmiwiAPI.USER_GET_IMAGE_CODE,
						System.currentTimeMillis() + ""),
				codeImage, responseListener, errorListener);
	

	}

	Response.Listener<Bitmap> responseListener = new Response.Listener<Bitmap>() {
		@Override
		public void onResponse(Bitmap bitmap) {
			codeImage.setImageBitmap(bitmap);
			dismissBar();
		}
	};
	
	ErrorListener errorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError arg0) {
			codeImage.setImageDrawable(UmiwiApplication.getInstance().getResources().getDrawable(R.drawable.image_loader_min));
			showMsg("获取图片验证码失败,点击重试");
			isImageCodeFail = true;
			dismissBar();
		}
	};
	
	
	/**
	 * 绑定手机号
	 * 
	 * @param phoneNum
	 * @param phoneCodeNum
	 *            发送到手机的验证码
	 */
	private void getPhoneAndPhonesCode(String phoneNum, String phoneCodeNum) {
		String loginURL = String.format(UmiwiAPI.USER_BIND_MOBILE, phoneNum, phoneCodeNum);
		GetRequest<ResultModel> get = new GetRequest<ResultModel>(loginURL,
				ResultParser.class, codelistener);
		HttpDispatcher.getInstance().go(get);
	}

	private Listener<ResultModel> codelistener = new Listener<ResultModel>() {

		@Override
		public void onResult(AbstractRequest<ResultModel> request, ResultModel t) {

			if (t.isSucc()) {

				YoumiRoomUserManager.getInstance().getUserInfoSave(
						UserEvent.USER_BINDING_PHONE);// 更新用户信息但不需要刷新页面
				
				UmiwiContainerActivity csa = (UmiwiContainerActivity) getActivity();
						csa.slideToFinishActivity();
						ToastU.show(getActivity(), "绑定成功", 1);
				

			} else {
				showMsg(t.showMsg());
				phoneCodeNumStr.setText("重新获取验证码");
			}
			dismissBar();
		}

		@Override
		public void onError(AbstractRequest<ResultModel> requet,
				int statusCode, String body) {
			showMsg(body);
			dismissBar();
		}

	};
	private ProgressBar mProgressBar;
	private TextView succ_context;

	private void showBar() {
		mProgressBar.setVisibility(View.VISIBLE);
	}

	private void dismissBar() {
		mProgressBar.setVisibility(View.GONE);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(fragmentName);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(fragmentName);
	}

	@Override
	public void onStop() {
		super.onStop();
		handler.removeCallbacks(runnable);
		KeyboardUtils.hideKeyboard(getActivity());
	}
}
