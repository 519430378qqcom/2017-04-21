package com.umiwi.ui.fragment.setting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.fragment.WebFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.util.ManifestUtils;

import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.util.SystemUtils;

/**
 * 版本信息
 * 
 * @author tangxiyong 2013-12-4下午6:34:42
 * 
 */
public class VersionFragment extends BaseFragment {

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_version, null);
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, getResources()
				.getString(R.string.about_umiwi));

		TextView versionNameTextView = (TextView) view
				.findViewById(R.id.version_name_text_view);
		TextView channel = (TextView) view.findViewById(R.id.channel_text_view);
		channel.setText("(" + ManifestUtils.getChannelString(getActivity()) + ")");
		TextView emailTextView = (TextView) view
				.findViewById(R.id.email_text_view);
		emailTextView.setText(Html.fromHtml("<font color='#999999'> " + "客服邮箱："
				+ "</font>" + "<font color='#666666'> " + "service@youmi.cn"
				+ "</font>"));
		emailTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				String[] tos = { "service@youmi.cn" };
				intent.putExtra(Intent.EXTRA_EMAIL, tos);
				intent.putExtra(Intent.EXTRA_SUBJECT, "优米创业Android反馈");
				intent.setType("image/*");
				intent.setType("message/rfc882");
				Intent.createChooser(intent, "Choose Email Client");
				startActivity(intent);
			}
		});
		TextView urlTextView = (TextView) view.findViewById(R.id.url_text_view);
		urlTextView.setText(Html.fromHtml("<font color='#999999'> " + "官方网址："
				+ "</font>" + "<font color='#666666'> " + "www.youmi.cn"
				+ "</font>"));
		urlTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, WebFragment.class);
				intent.putExtra(WebFragment.WEB_URL, UmiwiAPI.YOUMI_WEB);
				startActivity(intent);
			}
		});

		versionNameTextView.setText(Html.fromHtml("<font color='#999999'> "
				+ "当前版本：" + "</font>" + "<font color='#f44f0c'>"
				+ SystemUtils.getVersionName() + "</font>"));

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

}
