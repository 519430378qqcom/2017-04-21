package com.umiwi.ui.adapter;

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
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.UmiwiJPZTBeans;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.util.CommonHelper;

import java.util.ArrayList;

import cn.youmi.framework.util.ImageLoader;

/**
 * 精品专题  适配器
 * @author tangxiyong
 * 2013-12-9下午2:03:28
 *
 */

public class UmiwiJPZTListAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;

	private ArrayList<UmiwiJPZTBeans> mList;

	private Context mContext;

	public UmiwiJPZTListAdapter(Context context,
			ArrayList<UmiwiJPZTBeans> mList) {
		super();
		mLayoutInflater = LayoutInflater.from(UmiwiApplication.getInstance());
		mContext = context;
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
		view = mLayoutInflater.inflate(R.layout.listitem_jpzt, null);
		Holder holder = getHolder(view);

		final UmiwiJPZTBeans listBeans = mList.get(position);
		
		ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
		mImageLoader.loadImage(listBeans.getImage2(), holder.image);
		
		String msg = CommonHelper.replaceBlank(listBeans.getIntroduce());
		holder.introduce.setText(msg);
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

		public TextView introduce;
		
		private int w;

		private int h;

		public Holder(View view) {
			image = (ImageView) view.findViewById(R.id.jpzt_iv_image_min);
			introduce = (TextView) view.findViewById(R.id.jpat_tv_introduce);
//			CommonHelper.px2dip(getHeighAndWidth().widthPixels);
			
			w = getHeighAndWidth().widthPixels;
			h = (300 * w) / 640;
	        LayoutParams para = image.getLayoutParams();
	        para.width = w;
	        para.height = h;
	        image.setLayoutParams(para);
		}
	}
	public  DisplayMetrics getHeighAndWidth() {
		Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		return metrics;
	}


}
