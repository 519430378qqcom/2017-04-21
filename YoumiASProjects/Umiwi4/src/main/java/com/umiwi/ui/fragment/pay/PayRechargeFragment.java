package com.umiwi.ui.fragment.pay;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.util.KeyboardUtils;
import cn.youmi.framework.util.ToastU;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.main.UmiwiAPI;

/**
 * @author tangxiyong
 * @version 2015-5-28 下午5:09:42
 */
public class PayRechargeFragment extends BaseFragment {

	private EditText recharge;

	@SuppressLint("InflateParams") @Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_pay_recharge, null);
		
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, "充值");

		recharge = (EditText) view.findViewById(R.id.recharge_et_input);
		TextView submit = (TextView) view.findViewById(R.id.recharge_tv_submit);
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!"".equals(recharge.getText().toString()) && Long.valueOf(recharge.getText().toString()) > 0) {
					KeyboardUtils.hideKeyboard(getActivity());
					
					PayingFragment courseDetailFragment = new PayingFragment();
					courseDetailFragment.pay_url = String.format(UmiwiAPI.UMIWI_PAY_RECHARGE_API, recharge.getText().toString().trim());
					courseDetailFragment.toolbarTitle = "充值";
					UmiwiContainerActivity ca = (UmiwiContainerActivity) getActivity();
					ca.getNavigationController().pushFragment(courseDetailFragment);
					
				} else {
					ToastU.showShort(getActivity(), "充值金额不能为空");
				}
			}
		});	
		
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
