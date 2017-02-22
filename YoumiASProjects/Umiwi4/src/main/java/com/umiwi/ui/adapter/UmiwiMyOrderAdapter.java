package com.umiwi.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.UmiwiMyOrderBeans;
import com.umiwi.ui.main.UmiwiApplication;

/**
 * 我的订单 适配器
 * @author tangxiyong
 * 2013-12-6下午3:32:32
 *
 */
public class UmiwiMyOrderAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;

	private ArrayList<UmiwiMyOrderBeans> mList;

	public UmiwiMyOrderAdapter(Context context,
			ArrayList<UmiwiMyOrderBeans> mList) {
		super();
		mLayoutInflater = LayoutInflater.from(UmiwiApplication.getInstance());
		this.mList = mList;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
 		view = mLayoutInflater.inflate(R.layout.listitem_myorder, null);
		Holder holder = getHolder(view);
		final UmiwiMyOrderBeans listBeans = mList.get(position);
		// 1 => '未付款'
		// 2 => '付款成功'
		// 3 => '已关闭'
		// 4 => '已退款'
		//TODO 
		if ("1".equals(listBeans.getStatusnum())) {
			holder.tv_status.setText(Html.fromHtml("<u>"+"立即支付"+"</u>"));
			holder.tv_status.setTextColor(UmiwiApplication.getInstance().getResources().getColor(R.color.umiwi_black));
			holder.tv_price.setTextColor(UmiwiApplication.getInstance().getResources().getColor(R.color.umiwi_orange));
		}else {
			holder.tv_status.setText(listBeans.getStatus());
		}
		holder.tv_code.setText("订单号："+listBeans.getCode());
		holder.tv_ctime.setText("订单时间："+listBeans.getCtime().substring(0, 10));
		holder.tv_title.setText(listBeans.getDetail().get(0).getTitle());
		holder.tv_price.setText(listBeans.getTotalprice()+"元");
		
		return view;
	}

	private Holder getHolder(final View view) {
		Holder holder = (Holder) view.getTag();
		if (holder == null) {
			holder = new Holder(view);
			view.setTag(holder);
		}
		return holder;
	}

	private class Holder {
		/** 订单号 */
		public TextView tv_code;
		/** 下单时间 */
		public TextView tv_ctime;
		/** 课程名 */
		public TextView tv_title;
		/** 课程价格 */
		public TextView tv_price;
		/** 付款状态 */
		public TextView tv_status;
		

		public Holder(View view) {
			tv_code = (TextView) view.findViewById(R.id.myorder_code);
			tv_ctime = (TextView) view.findViewById(R.id.myorder_ctime);
			tv_title = (TextView) view.findViewById(R.id.myorder_title);
			tv_price = (TextView) view.findViewById(R.id.myorder_price);
			tv_status = (TextView) view.findViewById(R.id.myorder_status);
		}
	}

}
