package com.umiwi.ui.fragment.pay;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ToastU;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.beans.UmiwiPayOrderBeans;
import com.umiwi.ui.beans.UmiwiPayOrderBeans.PayOrderBeansRequestData;
import com.umiwi.ui.fragment.pay.PayCouponDialog.OnCouponDialogChooseListener;
import com.umiwi.ui.util.CommonHelper;

/**
 * @author tangxiyong
 * @version 2015-5-28 下午3:02:20 
 */
public class PayCouponFragment extends BaseFragment {
	
	private UmiwiPayOrderBeans coupon_list;
	public String coupon_url;
	public String ordercoupon;
	
	private HashMap<Integer, String> coupon_choose;
	private RadioButton[] rbs;
	
	private RadioButton rbCoupon;
	private RadioButton rbTimed;
	private RadioButton rbVip;
	private TextView tvhycd;
	private TextView tvsilverscore;
	/** 共优惠*/
	private TextView tvcoupon_money;
	private RadioGroup rg_list;
	private TextView tvcontent;
	
	private String chooseCouponId;
	
	public interface OnCouponListChooseListener {
		void onCouponListCallback(String couponType);
	}
	
	private OnCouponListChooseListener couponListListener;
	
	public void setCouponListChooseListener(OnCouponListChooseListener l) {
		this.couponListListener = l;
	}
	
	@SuppressLint({ "InflateParams", "UseSparseArrays" }) 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pay_coupon, null);

		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, "优惠方式");

		findViewById(view);

		pay_order(coupon_url, ordercoupon);//

		rbs = new RadioButton[] { rbCoupon, rbTimed, rbVip };
		
		coupon_choose = new HashMap<Integer, String>();
		coupon_choose.put(R.id.pay_rb_coupon_list_coupon, "");
		coupon_choose.put(R.id.pay_rb_coupon_list_timed, "&discounttype=timed");
		coupon_choose.put(R.id.pay_rb_coupon_list_vip, "&discounttype=vip");
		
		rg_list.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.pay_rb_coupon_list_coupon:
					
					break;
				case R.id.pay_rb_coupon_list_timed:
					
					break;
				case R.id.pay_rb_coupon_list_vip:
					
					break;

				default:
					break;
				}
			}
		});
		
		rbCoupon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (null!=coupon_list.getCoupon().getCouponList() || "".equals(coupon_list.getCoupon().getCouponList())) {
					PayCouponDialog.getInstance().showDialog(getActivity(), coupon_list.getCoupon().getCouponList(), chooseCouponId);
					PayCouponDialog.getInstance().setCouponChooseListener(couponListener);
				} else {
					ToastU.showShort(getActivity(), "没有优惠券");
				}
			}
		});
		rbTimed.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pay_order(coupon_url, "&discounttype=timed");
			}
		});
		rbVip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pay_order(coupon_url, "&discounttype=vip");
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

	private void findViewById(View view) {
		rg_list = (RadioGroup) view.findViewById(R.id.pay_rg_coupon_list);
		rbCoupon = (RadioButton) view.findViewById(R.id.pay_rb_coupon_list_coupon);
		rbTimed = (RadioButton) view.findViewById(R.id.pay_rb_coupon_list_timed);
		rbVip = (RadioButton) view.findViewById(R.id.pay_rb_coupon_list_vip);
		
		tvhycd = (TextView) view.findViewById(R.id.tv_pay_coupon_list_hycd);
		tvsilverscore = (TextView) view.findViewById(R.id.tv_pay_coupon_list_silverscore);
		tvcoupon_money = (TextView) view.findViewById(R.id.pay_tv_coupon_money);
		tvcontent = (TextView) view.findViewById(R.id.pay_tv_content);	
		
		TextView okButton = (TextView) view.findViewById(R.id.ok);
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UmiwiContainerActivity ca = (UmiwiContainerActivity) getActivity();
				ca.getNavigationController().popFragment();
			}
		});
	}

	
	/** 选择了哪种优惠方式*/
	public String getQuestion() {
		
		String requests = "";
		for (int i = 0; i < rbs.length; i++) {
			if (rbs[i].isChecked()) {
					requests += coupon_choose.get(rbs[i].getId());
			}
		}
		if (TextUtils.isEmpty(requests)) {
			return ordercoupon;
		} else {
			return requests;
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if (couponListListener != null) {
			couponListListener.onCouponListCallback(getQuestion());
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	private Listener<UmiwiPayOrderBeans.PayOrderBeansRequestData> couponUIListener = new Listener<UmiwiPayOrderBeans.PayOrderBeansRequestData>() {

		@Override
		public void onResult(AbstractRequest<PayOrderBeansRequestData> request,
				PayOrderBeansRequestData t) {
			if (null != t) {
				coupon_list = t.getDiscountlist();
				
				if (null != coupon_list.getCoupon()) {
					rbCoupon.setVisibility(View.VISIBLE);
				}
				if (null != coupon_list.getTimed()) {
					rbTimed.setVisibility(View.VISIBLE);
				}
				if (null != coupon_list.getVip()) {
					rbVip.setVisibility(View.VISIBLE);
				}
				if (null != coupon_list.getHycd()) {
					tvhycd.setVisibility(View.VISIBLE);
					tvhycd.setText(coupon_list.getHycd().getName());
				}
				if (null != coupon_list.getSilverscore()) {
					tvsilverscore.setVisibility(View.VISIBLE);
					tvsilverscore.setText(coupon_list.getSilverscore().getName());
				}
				
				if ("coupon".equals(t.getDiscounttype())) {
					rbCoupon.setChecked(true);
				} else if ("timed".equals(t.getDiscounttype())) {
					rbTimed.setChecked(true);
				} else if ("vip".equals(t.getDiscounttype())) {
					rbVip.setChecked(true);
				}
				
				tvcoupon_money.setVisibility(View.VISIBLE);
				tvcoupon_money.setText(Html.fromHtml(t.getOffset()));
				tvcontent.setVisibility(View.VISIBLE);
				chooseCouponId = t.getCouponid();
				
			}
		}

		@Override
		public void onError(AbstractRequest<PayOrderBeansRequestData> requet,
				int statusCode, String body) {
			// TODO Auto-generated method stub
			
		}
	};
	
	/** 订单界面解析 用于优惠方式判断*/
	private void pay_order(String coupon_url, String ordercoupon) {
		String url = null;
		if (null == ordercoupon || "".equals(ordercoupon)) {
			url = coupon_url + CommonHelper.getChannelModelViesion();
		} else {
			url = coupon_url + ordercoupon + CommonHelper.getChannelModelViesion();
		}

		GetRequest<UmiwiPayOrderBeans.PayOrderBeansRequestData> request = new GetRequest<UmiwiPayOrderBeans.PayOrderBeansRequestData>(url, GsonParser.class, UmiwiPayOrderBeans.PayOrderBeansRequestData.class, couponUIListener);
		HttpDispatcher.getInstance().go(request);
	
	}

	private OnCouponDialogChooseListener couponListener = new OnCouponDialogChooseListener() {
		
		@Override
		public void onCoupongDialogCallback(String name) {
			chooseCouponId = name;
			coupon_choose.put(R.id.pay_rb_coupon_list_coupon, "&discounttype=coupon&couponid="+name);
			pay_order(coupon_url, "&discounttype=coupon&couponid="+name);
		}
	};

}
