package com.umiwi.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.UmiwiMyCardBeans;
import com.umiwi.ui.main.UmiwiApplication;

/**
 * 我的兑换卡 兑换码 适配器
 * 
 * @author xiaobo
 * 
 */
public class UmiwiMyCardAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;

	private ArrayList<UmiwiMyCardBeans> mList;
	private Context mContext;

	public UmiwiMyCardAdapter(Context context, ArrayList<UmiwiMyCardBeans> mList) {
		super();
		mLayoutInflater = LayoutInflater.from(UmiwiApplication.getInstance());
		this.mList = mList;
		this.mContext = context;
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
		view = mLayoutInflater.inflate(R.layout.listitem_mycard, null);
		Holder holder = getHolder(view);
		final UmiwiMyCardBeans listBeans = mList.get(position);

		holder.code.setText(listBeans.getCode());
		holder.usedtime.setText(listBeans.getUsedtime().substring(0, 10));
		if (!("album".equals(listBeans.getType()) || "section".equals(listBeans
				.getType()))) {
			holder.product.setTextColor(mContext.getResources().getColor(
					R.color.umiwi_black));
		}
		holder.product.setText(listBeans.getProducts().getTitle()+"");

//		ArrayList<UmiwiMyCardProducts> products = listBeans.getProducts();
//		for(int i=0 ;i<products.size();i++)
//		{
//			UmiwiMyCardProducts product = (UmiwiMyCardProducts)products.get(i);
// 			String title = product.getTitle();
// 			if(title.length()>16)
// 			{
// 				title = title.substring(0,16);
// 			}
//
// 			if(i==0)
// 			{
// 				holder.tv_product1.setText("兑换商品: "+title);
// 				continue;	
// 			}
// 			//对其
// 			title = "                  " + title;
//			TextView tv = new TextView(UmiwiApplication.getInstance());
//			tv.setText(title);
//			holder.ll_3.addView(tv);
// 		}

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
		/** 兑换号码 */
		public TextView code;
		/** 兑换时间 */
		public TextView usedtime;
		/** 兑换商品 */
		public TextView product;

		public Holder(View view) {
			code = (TextView) view.findViewById(R.id.mycard_code);
			usedtime = (TextView) view.findViewById(R.id.mycard_usedtime);
			product = (TextView) view.findViewById(R.id.mycard_product1);

		}
	}

}
