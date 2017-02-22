package com.umiwi.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.UmiwiPayDoingPaymentBeans;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.ArrayList;

import cn.youmi.framework.util.ImageLoader;

/**
 * 支付界面 sdk 支付方式 适配器
 * 
 * @author tangxiyong 2014-3-17下午2:09:16
 * 
 */
public class PaymentSdkAdapter extends BaseAdapter {

	private LayoutInflater mLayoutInflater;

	private ArrayList<UmiwiPayDoingPaymentBeans> mList;

	public PaymentSdkAdapter(Context context, ArrayList<UmiwiPayDoingPaymentBeans> mList) {
		super();
		mLayoutInflater = LayoutInflater.from(UmiwiApplication.getInstance());
		this.mList = mList;
	}

	@Override
	public int getCount() {
		return mList.size();
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
		view = mLayoutInflater.inflate(R.layout.item_payment_sdk, null, false);

		Holder holder = getHolder(view);

		final UmiwiPayDoingPaymentBeans listBeans = mList.get(position);
		
		if (mList != null && mList.size() > 0) {
			if (listBeans.getIcon() != null) {
				ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
				mImageLoader.loadImage(listBeans.getIcon(), holder.image);
			}
			if (listBeans.getName() != null) {
				holder.title.setText(
						Html.fromHtml("<font color='#000000'><big>"
								+ listBeans.getName() + "</big></font>"
								+ "<br>" + "<font color='#999999'>"
								+ listBeans.getDesc() + "</font>"));
			}
			/*if (listBeans.getDesc() != null) {
				holder.authorname.setText(listBeans.getDesc());
			}*/
		} else {
			throw new IllegalStateException(view.getClass().getName()
					+ "data is null or to is null");
		}

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

		public ImageView image;

		public TextView title;

//		public TextView authorname;

		public Holder(View view) {
			image = (ImageView) view.findViewById(R.id.image);
			title = (TextView) view.findViewById(R.id.title);
//			authorname = (TextView) view.findViewById(R.id.authorname);
			// 设置图片显示大小 待定
			LayoutParams para = image.getLayoutParams();
			para.width = 81;
			para.height = 81;
			image.setLayoutParams(para);			
		}
	}
}
