package com.umiwi.ui.fragment.user;

import android.graphics.Bitmap;
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

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.UserBeans;
import com.umiwi.ui.beans.UserBeans.ChartsListRequestData;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.util.CommonHelper;
import com.umiwi.ui.util.ImageCookieUtils;
import com.umiwi.ui.util.LoginUtil;

import cn.youmi.framework.dialog.MsgDialog;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.CookieDao;
import cn.youmi.framework.http.CookieModel;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.model.ResultModel;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.util.KeyboardUtils;
import cn.youmi.framework.util.ToastU;

/**
 * 找回密码
 * 
 * @author tangxiong
 * @version 2014年7月21日 下午2:20:41
 */
public class ForgetPasswordFragment extends BaseConstantFragment {

	private TextView phoneReset;
	private EditText phoneNumber;
	
	private ProgressBar mProgressBar;
	private LinearLayout forgetPassword;
	private LinearLayout phoneResetPassword;
	private LinearLayout commitResetPassword;

	private TextView phoneNumberReset;
	private EditText resetCode;
	private TextView codeResend;
	private TextView resetCommit;
	private TextView codeTime;
	
	private String phoneNumberStr;
	private String strToken;
	
	private TextView passwordResetCommit;
	private EditText etPassword;
	private EditText etPasswordConfirm;

	private RelativeLayout imageCodeLayout;
	private ImageView codeImage;
	private EditText codeNum;
	private boolean isImageCodeFail;
	private boolean isImageCodeNull;
	private boolean isCodeNum;
	private String strImageCodeNum;
	
	private ImageLoader mImageLoader;
	
	private int countNum = 60;
	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			countNum--;
			codeTime.setText(Html.fromHtml("<font color='#7eb706'>" + String.valueOf(countNum) + "</font>" + " 秒后重新获取"));
			if (countNum >= 1) {
				handler.postDelayed(this, 1000);
			} else {
				codeTime.setVisibility(View.GONE);
				codeResend.setText(Html.fromHtml("收不到验证码?  "+"<font color='#006ed3'>" + "重新发送" + "</font>"));
				codeResend.setVisibility(View.VISIBLE);
				countNum = 60;
				handler.removeCallbacks(runnable);
			}

		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_forget_password, null);
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, "找回密码");
		//setActionBarRightButton("意见反馈");
		this.setHasOptionsMenu(true);
		
		mImageLoader = new ImageLoader(getActivity());
		
		mProgressBar = (ProgressBar) view.findViewById(R.id.loading);
		forgetPassword = (LinearLayout) view.findViewById(R.id.forget_password_relativelayout);
		forgetPassword.setVisibility(View.VISIBLE);
		phoneResetPassword = (LinearLayout) view.findViewById(R.id.phone_reset_password_relativelayout);
		commitResetPassword = (LinearLayout) view.findViewById(R.id.commit_reset_password_relativelayout);
		
		//------输入手机号-----
		phoneReset = (TextView) view.findViewById(R.id.phone_reset_tv);
		phoneNumber = (EditText) view.findViewById(R.id.phone_number_et);

		phoneReset.setOnClickListener(new PnoneResetClickListener());
		//-------图片验证码----------
		imageCodeLayout = (RelativeLayout) view.findViewById(R.id.imagecode_layout);
		codeImage = (ImageView) view.findViewById(R.id.code_imageview);
		codeNum = (EditText) view.findViewById(R.id.code_number_et);
		
		codeImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isImageCodeFail) {
					ImageCookieUtils.getInstance().getImagecode(
							String.format(UmiwiAPI.USER_GET_IMAGE_CODE,
									System.currentTimeMillis() + ""),
							codeImage, responseListener, errorListener);
				}
				
				//mImageLoader.loadImage(String.format(UmiwiAPI.USER_GET_IMAGE_CODE, System.currentTimeMillis() + ""), codeImage, R.drawable.ic_launcher);
				
			}
		});
		
		//------验证-----
		phoneNumberReset = (TextView) view.findViewById(R.id.phone_number_tv);
		resetCode = (EditText) view.findViewById(R.id.reset_code_et);
		
		codeResend = (TextView) view.findViewById(R.id.code_resend_tv);
		resetCommit = (TextView) view.findViewById(R.id.reset_commit);
		codeTime = (TextView) view.findViewById(R.id.code_time_tv);
		
		resetCommit.setOnClickListener(new CodeCommitClickListener());
		codeResend.setOnClickListener(new CodeResendClickListener());
		//------重置密码------
		passwordResetCommit = (TextView) view.findViewById(R.id.tv_commit);
		etPassword = (EditText) view.findViewById(R.id.et_password);
		etPasswordConfirm = (EditText) view.findViewById(R.id.et_password_confirm);
		
		passwordResetCommit.setOnClickListener(new PasswordResetCommitClickListener());

		return view;

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
	public void onDestroyView() {
		super.onDestroyView();
	}

	/**
	 * 获取手机验证码
	 * @author tangxiong
	 * @version 2014年7月21日 下午5:34:53
	 */
	private class PnoneResetClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			phoneNumberStr = phoneNumber.getText().toString().trim();
			if (TextUtils.isEmpty(phoneNumberStr)) {
				showMsg("请输入手机号");
				return;
			}
			if (isCodeNum) {
				strImageCodeNum = codeNum.getText().toString().trim();
				if (TextUtils.isEmpty(strImageCodeNum)) {
					showMsg("验证码不能为空");
					return;
				}
				showBar();
				KeyboardUtils.hideKeyboard(getActivity());
				phoneResetRequest(phoneNumberStr, strImageCodeNum);
			} else {
				showBar();
				KeyboardUtils.hideKeyboard(getActivity());
				phoneResetRequest(phoneNumberStr, "");
			}
			
		}
	}


	/**
	 * 提交验证码，验证手机号
	 * @author tangxiong
	 * @version 2014年7月21日 下午5:35:50
	 */
	private class CodeCommitClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			strToken = resetCode.getText().toString().trim();
			if (TextUtils.isEmpty(strToken)) {
				showMsg("请输入手机验证码");
				return;
			}
			KeyboardUtils.hideKeyboard(getActivity());
			showBar();
			setCodeAndGetToken(strToken);
		}
	}

	/**
	 * 重新获取验证码
	 * @author tangxiong
	 * @version 2014年7月21日 下午5:35:46
	 */
	private class CodeResendClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			
			codeTime.setVisibility(View.VISIBLE);
			codeResend.setVisibility(View.GONE);
			
			phoneResetRequest(phoneNumberStr, "");
			
		}
	}

	private Listener<ResultModel> phoneResetListener = new Listener<ResultModel>() {

		@Override
		public void onResult(AbstractRequest<ResultModel> request, ResultModel t) {

			dismiss();
			if (t.isSucc()) {
				mActionBarToolbar.setTitle("验证手机号");
				forgetPassword.setVisibility(View.GONE);
				phoneResetPassword.setVisibility(View.VISIBLE);
				phoneNumberReset.setText("已经发送验证码到: "+phoneNumberStr);
				handler.postDelayed(runnable, 1000);
			} else {
				ImageCookieUtils.getInstance().getImagecode(
						String.format(UmiwiAPI.USER_GET_IMAGE_CODE,
								System.currentTimeMillis() + ""), codeImage,
						responseListener, errorListener);
				
				if (!isImageCodeNull) {
					ToastU.showShort(getActivity(), t.showMsg());
				}
				imageCodeLayout.setVisibility(View.VISIBLE);
				isCodeNum = true;
			}
		}

		@Override
		public void onError(AbstractRequest<ResultModel> requet,
				int statusCode, String body) {
			dismiss();
			showMsg("未知错误,请重试");
		}

	};
	
	Response.Listener<Bitmap> responseListener = new Response.Listener<Bitmap>() {
		@Override
		public void onResponse(Bitmap bitmap) {
			codeImage.setImageBitmap(bitmap);
			dismiss();
		}
	};
	ErrorListener errorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError arg0) {
			codeImage.setImageDrawable(UmiwiApplication.getInstance().getResources().getDrawable(R.drawable.image_loader_min));
			showMsg("获取图片验证码失败,点击重试");
			isImageCodeFail = true;
			dismiss();
		}
	};

	
	 
	/**
	 * 发送手机号获取验证码
	 * @param strMobileNum
	 */
	protected void phoneResetRequest(String strMobileNum, String imageCodeNum) {
		
		String strConfirm;
		if (TextUtils.isEmpty(imageCodeNum)) {
			strConfirm = String.format(UmiwiAPI.USER_IFORGOT_SEND_PHONES_CODE, strMobileNum, System.currentTimeMillis() + "");
			isImageCodeNull = true;
		} else {
			strConfirm = String.format(UmiwiAPI.USER_IFORGOT_SEND_PHONES_CODE_WITH_CAPTCODE, imageCodeNum, strMobileNum, System.currentTimeMillis() + "");
			isImageCodeNull = false;
		}
		
		final String imageCodeNumTemp = imageCodeNum;
		
		GetRequest<ResultModel> request = new GetRequest<ResultModel>(strConfirm, GsonParser.class, ResultModel.class, phoneResetListener);
		
		if (!TextUtils.isEmpty(imageCodeNumTemp)) {
			CookieModel model = CookieDao.getInstance(
					UmiwiApplication.getInstance()).getByName("passport");
			if (model != null && !TextUtils.isEmpty(model.getValue())) {
				request.setHeader("COOKIE", "passport=" + model.getValue()+";");
			}
		}
		
		request.go();
	}

	/***
	 * 验证手机号获取重置密码的权限 token
	 * @param token
	 */
	public void setCodeAndGetToken(final String token) {
		final String strCheckConfirm = String.format(UmiwiAPI.CHECK_CONFIRM__URL, token, phoneNumberStr);
		GetRequest<ResultModel> request = new GetRequest<ResultModel>(strCheckConfirm, GsonParser.class, ResultModel.class, listener);
		request.go();
	}

	private Listener<ResultModel> listener = new Listener<ResultModel>() {
		@Override
		public void onResult(AbstractRequest<ResultModel> request, ResultModel t) {
			dismiss();
			if (t.isSucc()) {
				mActionBarToolbar.setTitle("设置新密码");
				forgetPassword.setVisibility(View.GONE);
				phoneResetPassword.setVisibility(View.GONE);
				commitResetPassword.setVisibility(View.VISIBLE);
			} else {
				ToastU.showShort(getActivity(), t.showMsg());
			}
		}

		@Override
		public void onError(AbstractRequest<ResultModel> requet,
				int statusCode, String body) {

		}
	};
	
	private class PasswordResetCommitClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			String strPassword = etPassword.getText().toString().trim();
			String strPasswordConfirm = etPasswordConfirm.getText().toString().trim();
			if (TextUtils.isEmpty(strPassword)) {
				showMsg("请输入密码");
				return;
			}
			if (strPassword.length() < 6 || strPassword.length() > 16) {
				showMsg("密码长度必须是6-16位");
				return;
			}
			if (!strPassword.equals(strPasswordConfirm)) {
				showMsg("两次密码不一致，请重新设置");
				return;
			}
			KeyboardUtils.hideKeyboard(getActivity());
			showBar();
			passwordResetResult(strPassword);
		}
	}

	/**
	 * 重置为新密码
	 * @param strPassword
	 */
	protected void passwordResetResult(String strPassword) {
		String strPasswordMD5 = CommonHelper.encodeMD5(strPassword);
		final String strSetPassword = String.format(UmiwiAPI.SET_PASSWORD_URL, strPasswordMD5, strToken, phoneNumberStr);
		
		GetRequest<UserBeans.ChartsListRequestData> get = new GetRequest<UserBeans.ChartsListRequestData>(strSetPassword, GsonParser.class, UserBeans.ChartsListRequestData.class, mssssListener);
		get.go();
	}
	
	/** 修改密码是否成功 */
	private  Listener<UserBeans.ChartsListRequestData> mssssListener = new Listener<UserBeans.ChartsListRequestData>() {

		@Override
		public void onResult(AbstractRequest<ChartsListRequestData> request,
				ChartsListRequestData t) {
			if (t.getE() == 9999) {

				final MsgDialog dialog = new MsgDialog();
				dialog.setTitle("提示");
				dialog.setMessage("重置密码成功,马上登录");
				dialog.setPositiveButtonText(R.string.logiin);
				dialog.setPositiveButtonListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismissAllowingStateLoss();
						LoginUtil.getInstance().showLoginView(getActivity());
						getActivity().finish();
					}
				});
				dialog.setNegativeButtonListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismissAllowingStateLoss();
						getActivity().finish();
					}
				});
				dialog.show(getChildFragmentManager(), "dialog");
			
			} else {
				ToastU.showShort(getActivity(), t.getM());
			}
		}

		@Override
		public void onError(AbstractRequest<ChartsListRequestData> requet,
				int statusCode, String body) {
			// TODO Auto-generated method stub
			
		}};
	//private ActionBar mActionBar;
	
	private void showBar() {
		if (mProgressBar != null && !mProgressBar.isShown()) {
			mProgressBar.setVisibility(View.VISIBLE);
		}
	}
	private void dismiss() {
		if (mProgressBar != null && mProgressBar.isShown()) {
			mProgressBar.setVisibility(View.GONE);
		}
	}
}
