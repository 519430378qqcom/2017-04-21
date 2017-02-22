package com.umiwi.ui.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.util.ToastU;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.UmiwiResultBeans;
import com.umiwi.ui.http.parsers.ResponseParser;

public class ActivitySignupFragment extends BaseFragment {
	public static final String KEY_ACTIVITY_ID = "keyActivityID";
	private String activityID = null;

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_signup_activity, null);
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		mActionBarToolbar.setTitle("活动报名");
		
		activityID = getActivity().getIntent().getStringExtra(KEY_ACTIVITY_ID);

		view.findViewById(R.id.commit_button).setOnClickListener(
				mCommitButtonListener);
		initView(view);
		return view;
	}
	
	private void initView(View view) {
		username = (TextView) view.findViewById(R.id.username_edit_text);
		wechatText = (TextView) view.findViewById(R.id.wechat_edit_text);
		phoneText = (TextView) view.findViewById(R.id.phone_edit_text);
		careerText = (TextView) view.findViewById(R.id.career_edit_text);
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
	

	private Listener<UmiwiResultBeans.ResultBeansRequestData> listener = new Listener<UmiwiResultBeans.ResultBeansRequestData>() {
		@Override
		public void onResult(
				AbstractRequest<UmiwiResultBeans.ResultBeansRequestData> request,
				UmiwiResultBeans.ResultBeansRequestData t) {
//			dismissDialog();
			if (t.isSucc()) {
				ToastU.showShort(getActivity(), "提交成功");
				getActivity().finish();
			} else {
				ToastU.showShort(getActivity(), "提交失败");
			}

		}

		@Override
		public void onError(
				AbstractRequest<UmiwiResultBeans.ResultBeansRequestData> requet,
				int statusCode, String body) {
//			dismissDialog();
			ToastU.showShort(getActivity(), "提交失败(错误)");
		}
	};

	OnClickListener mCommitButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String name = username.getText().toString();

			if (TextUtils.isEmpty(name)) {
				ToastU.showShort(getActivity(), "请输入名字！");
				return;
			}

			String wechat = wechatText.getText().toString();
			String phoneNumber = phoneText.getText().toString();
			if (TextUtils.isEmpty(phoneNumber)) {
				ToastU.showShort(getActivity(), "请输联系方式！");
				return;
			}
			String career = careerText.getText().toString();

			String baseURL = null;
			baseURL = "http://v.youmi.cn";

			String url;
			try {
				url = baseURL + "/viphuodong/mobileAdduser/?huodongid="
						+ activityID + "&realname="
						+ URLEncoder.encode(name, "UTF-8") + "&weixin="
						+ URLEncoder.encode(wechat, "UTF-8") + "&phonenumber="
						+ phoneNumber + "&industry="
						+ URLEncoder.encode(career, "UTF-8");
				GetRequest<UmiwiResultBeans.ResultBeansRequestData> request = new GetRequest<UmiwiResultBeans.ResultBeansRequestData>(
						url, ResponseParser.class, listener);
				request.go();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
//			showLoading();
		}
	};
	private TextView username;
	private TextView wechatText;
	private TextView phoneText;
	private TextView careerText;
	
	class NoUnderlineSpan extends UnderlineSpan {
		 
	    @Override
	    public void updateDrawState(TextPaint ds) {
	        ds.setColor(Color.BLACK);
	        ds.setUnderlineText(false);
	    }
	}
}
