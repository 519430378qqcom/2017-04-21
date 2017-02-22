package com.umiwi.ui.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import cn.youmi.framework.main.ContextProvider;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.util.ListViewUtils;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.UmiwiPayDoingPaymentBeans;
import com.umiwi.ui.main.UmiwiApplication;

/**
 * 支付界面 bank支付方式 适配器
 * 
 * @author tangxiyong 2014-3-17下午3:05:36
 * 
 */
public class PaymentBankAdapter extends BaseAdapter {

	private LayoutInflater mLayoutInflater;

	private ArrayList<UmiwiPayDoingPaymentBeans> mList;
	private Context mContext;

	public PaymentBankAdapter(Context context,
			ArrayList<UmiwiPayDoingPaymentBeans> mList) {
		super();
		mLayoutInflater = LayoutInflater.from(UmiwiApplication.getInstance());
		this.mList = mList;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return ListViewUtils.getSize(mList);
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" }) @Override
	public View getView(int position, View view, ViewGroup parent) {
		view = mLayoutInflater.inflate(R.layout.image_layout, null);
		Holder holder = getHolder(view);
		if (mList != null && mList.size() > 0) {
			final UmiwiPayDoingPaymentBeans listBeans = mList.get(position);
			if (!"".equals(listBeans)) {
				ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
				mImageLoader.loadImage(listBeans.getIcon(), holder.image);
			}
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

		public Holder(View view) {
			image = (ImageView) view.findViewById(R.id.image_sc);
			// 设置图片显示大小 待定
			LayoutParams para = image.getLayoutParams();
			if ((getHeighAndWidth().widthPixels / 2) < 284) {
				para.width = (getHeighAndWidth().widthPixels / 2) - 8;
			} else {
				para.width = 284;
			}
			para.height = (para.width * 100) / 284;
			image.setLayoutParams(para);
		}
	}

	public DisplayMetrics getHeighAndWidth() {
		Display display = ((Activity) mContext).getWindowManager()
				.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		return metrics;
	}

}
