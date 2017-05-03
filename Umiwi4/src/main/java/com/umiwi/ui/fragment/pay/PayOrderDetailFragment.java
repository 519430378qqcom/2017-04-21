package com.umiwi.ui.fragment.pay;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.beans.UmiwiPayDoingBeans;
import com.umiwi.ui.beans.UmiwiPayDoingBeans.PayDoingBeansRequestData;
import com.umiwi.ui.beans.UmiwiPayOrderBeans;
import com.umiwi.ui.beans.UmiwiPayOrderBeans.PayOrderBeansRequestData;
import com.umiwi.ui.fragment.pay.PayCouponFragment.OnCouponListChooseListener;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.util.CommonHelper;

import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ToastU;

/**
 * @author tangxiyong
 * @version 2015-5-28 上午11:34:13
 */
public class PayOrderDetailFragment extends BaseFragment {

	private ProgressDialog mLoadingDialog = null;
	private String order_id;
	private String order_type;

	private UmiwiPayOrderBeans discountlist;

	public static final String KEY_ORDER_TYPE = "order_type";
	public static final String KEY_ORDER_ID = "order_id";
	public static final String KEY_SPM = "order_spm";

	private String spmurl;

	/** 订单id标识 */
	public static final String ORDER_ID = "ORDER_ID";
	/** 订单type标识 */
	public static final String ORDER_TYPE = "ORDER_TYPE";
	/** 回调回来的优惠方式选择*/
	public static final String ORDER_COUPON = "ORDER_COUPON";
	/** 回调回来的优惠方式选择*/
	public String ordercoupon = "";

	/** */
	private TextView order_title;
	/** 订单介绍 */
	private TextView order_desc;
	/** 原价 */
	private TextView order_price;
	/** 优惠金额 点击跳转优惠卷选择界面 */
	private TextView order_coupon;
	/** 应付金额 */
	private TextView order_money;
	/** 提交订单 */
	private TextView order_submit;
	/** 下单地址 */
	private String order_url;

	private RelativeLayout rl_order_coupon;
	private ImageView iv;
	private View lines_01;
	private View lines_02;
	private View lines_03;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pay_order_detail, null);

		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, "提交订单");

		findViewById(view);

		Intent i = getActivity().getIntent();
		order_id = i.getStringExtra(KEY_ORDER_ID);
		order_type = ((PayTypeEvent) i.getSerializableExtra(KEY_ORDER_TYPE)).getValue() +"";
		spmurl = i.getStringExtra(KEY_SPM);
		pay_order(order_id, order_type, ordercoupon);
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
		mLoadingDialog = new ProgressDialog(getActivity());
		mLoadingDialog.setMessage("加载中,请稍候...");
		mLoadingDialog.setIndeterminate(true);
		mLoadingDialog.setCancelable(true);
		mLoadingDialog.show();

		order_title = (TextView) view.findViewById(R.id.tv_pay_order_title);
		order_desc = (TextView) view.findViewById(R.id.tv_pay_order_desc);
		order_price = (TextView) view.findViewById(R.id.tv_pay_order_price);
		order_coupon = (TextView) view.findViewById(R.id.tv_pay_order_coupon);
		order_money = (TextView) view.findViewById(R.id.tv_pay_order_money);
		order_submit = (TextView) view.findViewById(R.id.tv_pay_order_submit);

		lines_01 = view.findViewById(R.id.lines_01);
		lines_02 = view.findViewById(R.id.lines_02);
		lines_03 = view.findViewById(R.id.lines_03);

		iv = (ImageView) view.findViewById(R.id.pay_iv_image);

		rl_order_coupon = (RelativeLayout) view.findViewById(R.id.rl_pay_order_coupon);

		order_submit.setOnClickListener(new SubmitOrderClickListener());
		rl_order_coupon.setOnClickListener(new ShowCouponListener());
	}


	/**
	 * 进入优惠方式选择
	 * @author tangxiong
	 * @version 2014年6月6日 下午5:47:19
	 */
	private class ShowCouponListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if (null != discountlist ) {
				String url = String.format(UmiwiAPI.UMIWI_PAY_API, order_id, order_type);//传递url

				UmiwiContainerActivity ca = (UmiwiContainerActivity) getActivity();
				PayCouponFragment payCouponFragmnt = new PayCouponFragment();
				payCouponFragmnt.coupon_url = url;
				payCouponFragmnt.ordercoupon = ordercoupon + "";
				payCouponFragmnt.setCouponListChooseListener(couponListListener);
				ca.getNavigationController().pushFragment(payCouponFragmnt);
			}
		}
	}
	/**
	 * 提交订单
	 * @author tangxiong
	 * @version 2014年6月6日 下午5:47:00
	 */
	private class SubmitOrderClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			mLoadingDialog.show();
			mconfirm(order_url);
		}
	}

	private Listener<UmiwiPayOrderBeans.PayOrderBeansRequestData> payUIListener = new Listener<UmiwiPayOrderBeans.PayOrderBeansRequestData>() {

		@Override
		public void onResult(AbstractRequest<PayOrderBeansRequestData> request,
							 PayOrderBeansRequestData t) {
			if (null != t) {
				if (null != t.getDiscountlist() || !"".equals(t.getDiscountlist())) {
					discountlist = t.getDiscountlist();
				}
				order_title.setText(t.getTitle() + "");
				order_desc.setText(Html.fromHtml(t.getDesc() + ""));
				order_price.setText(Html.fromHtml("单　　价："+"<font color='#7eb706'>"+t.getPrice()  + "" +"</font>"));
				order_coupon.setText(Html.fromHtml("优惠金额："+t.getOffsetdesc()  + ""));
				order_money.setText(Html.fromHtml(t.getMoney() + ""));

				order_url = t.getBuyurl();

				order_submit.setVisibility(View.VISIBLE);
				iv.setVisibility(View.VISIBLE);
				lines_01.setVisibility(View.VISIBLE);
				lines_02.setVisibility(View.VISIBLE);
				lines_03.setVisibility(View.VISIBLE);
			}
			progressdismiss();
		}

		@Override
		public void onError(AbstractRequest<PayOrderBeansRequestData> requet,
							int statusCode, String body) {
			ToastU.showShort(getActivity(), body);
			progressdismiss();
		}
	};

	/** 订单界面解析*/
	private void pay_order(String id, String type, String ordercoupon) {
		String url = null;
		if (null == ordercoupon || "".equals(ordercoupon)) {
			url = String.format(UmiwiAPI.UMIWI_PAY_API, id, type) + CommonHelper.getChannelModelViesion() + spmurl;
		} else {
			url = String.format(UmiwiAPI.UMIWI_PAY_API, id, type) + ordercoupon + CommonHelper.getChannelModelViesion() + spmurl;
		}
		Log.e("TAG", "url=" + url);
		GetRequest<UmiwiPayOrderBeans.PayOrderBeansRequestData> request = new GetRequest<UmiwiPayOrderBeans.PayOrderBeansRequestData>(
				url, GsonParser.class,
				UmiwiPayOrderBeans.PayOrderBeansRequestData.class,
				payUIListener);
		request.go();

	}

	private Listener<UmiwiPayDoingBeans.PayDoingBeansRequestData> mconfirmListener = new Listener<UmiwiPayDoingBeans.PayDoingBeansRequestData>() {

		@Override
		public void onResult(AbstractRequest<PayDoingBeansRequestData> request,
							 PayDoingBeansRequestData t) {
			if (null!=t) {
				if ("9999".equals(t.getDoing_e())) {
					Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
					i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayingFragment.class);
					i.putExtra(PayingFragment.KEY_PAY_URL, t.getDoing_r().getPayurl());
					startActivity(i);
					getActivity().finish();
//					UmiwiContainerActivity ca = (UmiwiContainerActivity) getActivity();
//					PayingFragment payingFragment = new PayingFragment();
//					payingFragment.pay_url = t.getDoing_r().getPayurl();
//					ca.getNavigationController().popFragment();
//					ca.getNavigationController().setBottomFragment(payingFragment);
//					ca.getNavigationController().popToBottom(true);
				} else {
					ToastU.showShort(getActivity(), t.getDoing_m());
				}
				progressdismiss();
			}
		}

		@Override
		public void onError(AbstractRequest<PayDoingBeansRequestData> requet,
							int statusCode, String body) {
			progressdismiss();
		}
	};

	/** 确认下单*/
	private void mconfirm(String order_url) {

		GetRequest<UmiwiPayDoingBeans.PayDoingBeansRequestData> request = new GetRequest<UmiwiPayDoingBeans.PayDoingBeansRequestData>(
				order_url, GsonParser.class,
				UmiwiPayDoingBeans.PayDoingBeansRequestData.class,
				mconfirmListener);

		request.go();

	}

	private void progressdismiss(){
		if (mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
		}
	}

	private OnCouponListChooseListener couponListListener = new OnCouponListChooseListener() {

		@Override
		public void onCouponListCallback(String couponType) {
			ordercoupon = couponType;
		}
	};

}
