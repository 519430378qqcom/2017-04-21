package com.umiwi.ui.fragment.user;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.managers.YoumiRoomUserManager;

/**
 * @author tjie00
 * @version 2014年12月17日 
 * 绑定手机号成功页面
 */
public class BindingPhoneOkFragment extends BaseConstantFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_binding_mobile_ok, null);
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, "绑定手机号");
		TextView mobileTextView = (TextView) view.findViewById(R.id.mobile_number_textview);
		
		mobileTextView.setText("已绑定："+ YoumiRoomUserManager.getInstance().getUser().getMobile());
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
