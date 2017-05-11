package com.umiwi.ui.fragment.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.util.ImageCookieUtils;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.CookieDao;
import cn.youmi.framework.http.CookieModel;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.ResultParser;
import cn.youmi.framework.model.ResultModel;
import cn.youmi.framework.util.KeyboardUtils;

/***
 * 注册选择页面（将手机号或邮箱传给下个页面处理）
 * 
 * @author tangxiong
 * @version 2014年9月10日 下午2:51:54 
 */
public class RegisterShowFragment extends BaseConstantFragment {

	private TextView registerCommit;
	private EditText registerPhoneNum;
	private ProgressBar mProgressBar;
	private RelativeLayout imageCodeLayout;
	private ImageView codeImage;
	private EditText codeNum;
	private String strMobileNum_;
	private String strImageCodeNum;
	private boolean isImageCodeNull;
	private boolean isImageCodeFail;
	
	private boolean isCodeNum;
	
	private SharedPreferences mSharedPreferences;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_register_show_layout, null);
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, "手机快速注册");


		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		registerCommit = (TextView) view.findViewById(R.id.register_commit_textview);
		
		registerPhoneNum = (EditText) view.findViewById(R.id.register_phone_number_edittext);
		
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
			}
		});

		registerCommit.setOnClickListener(new RegisterCommitListener());
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
		mSharedPreferences.edit().putBoolean("isCanShowGift", true).commit();
		MobclickAgent.onPageEnd(fragmentName);
	}
	
	private class RegisterCommitListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			strMobileNum_ = registerPhoneNum.getText().toString().trim();
			if (TextUtils.isEmpty(strMobileNum_)) {
				showMsg("手机号不能为空");
				return;
			}
			if (isCodeNum) {// 请求手机验证码
				strImageCodeNum = codeNum.getText().toString().trim();
				if (TextUtils.isEmpty(strImageCodeNum)) {
					showMsg("验证码不能为空");
					return;
				}
				showBar();
				getPhoneAndImagesCode(strMobileNum_, strImageCodeNum);
			} else {
				showBar();
				KeyboardUtils.hideKeyboard(getActivity());
				getPhoneAndImagesCode(strMobileNum_, "");
			}
		}
	}

	/**
	 * 获取手机验证码
	 * 
	 * @param phoneNum
	 * @param imageCodeNum 图片验证码
	 */
	private void getPhoneAndImagesCode(String phoneNum, String imageCodeNum) {
		String loginURL;
		if (TextUtils.isEmpty(imageCodeNum)) {
			loginURL = String.format(UmiwiAPI.USER_SEND_PHONES_CODE, phoneNum);
			isImageCodeNull = true;
		} else {
			loginURL = String.format(UmiwiAPI.USER_MOBILE_CAPTCHA_IMAGE, phoneNum, imageCodeNum);
			isImageCodeNull = false;
		}

		final String imageCodeNumTemp = imageCodeNum;
		
		GetRequest<ResultModel> get = new GetRequest<ResultModel>(loginURL, ResultParser.class, phoneListener);
		Log.e("loginURL", "loginURL=" + loginURL);
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
			if (!t.isSucc()) {// 需要输入图片验证码
				ImageCookieUtils.getInstance().getImagecode(
						String.format(UmiwiAPI.USER_GET_IMAGE_CODE,
								System.currentTimeMillis() + ""),
						codeImage, responseListener, errorListener);

				if (!isImageCodeNull) {
					showMsg("请检查手机号或验证码是否正确");
//					showMsg(t.showMsg());
				}
				dissmiss();
				imageCodeLayout.setVisibility(View.VISIBLE);
				isCodeNum = true;
			} else {// 发送手机验证码成功，下一页
				Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
				i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, RegisterPhoneCodeFragment.class);
				i.putExtra(RegisterPhoneCodeFragment.PHONE_NUMBER, strMobileNum_);
				startActivity(i);
				dissmiss();
				getActivity().finish();
			}
		}

		@Override
		public void onError(AbstractRequest<ResultModel> requet,
				int statusCode, String body) {
//			showAlertMsg(body);
			dissmiss();
		}

	};
	
	Response.Listener<Bitmap> responseListener = new Response.Listener<Bitmap>() {
		@Override
		public void onResponse(Bitmap bitmap) {
			codeImage.setImageBitmap(bitmap);
			dissmiss();
		}
	};
	ErrorListener errorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError arg0) {
			codeImage.setImageDrawable(UmiwiApplication.getInstance().getResources().getDrawable(R.drawable.image_loader_min));
			showMsg("获取图片验证码失败,点击重试");
			isImageCodeFail = true;
			dissmiss();
		}
	};

	@Override
	public void onStop() {
		super.onStop();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	public void dissmiss() {
		if (mProgressBar.isShown()) {
			mProgressBar.setVisibility(View.GONE);
		}
	}
	private void showBar() {
		mProgressBar.setVisibility(View.VISIBLE);
	}
}
