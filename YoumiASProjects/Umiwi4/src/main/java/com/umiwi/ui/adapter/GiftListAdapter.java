package com.umiwi.ui.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import cn.youmi.framework.main.ContextProvider;
import cn.youmi.framework.util.ImageLoader;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.GiftListBeans;
import com.umiwi.ui.main.UmiwiApplication;

/**
 * 列表适配器
 * 
 * @author tangxiyong 2013-11-29下午2:58:01
 * 
 */
public class GiftListAdapter extends BaseAdapter {
	
	private LayoutInflater mLayoutInflater;

	private ArrayList<GiftListBeans> mList;

	public GiftListAdapter(Context context,
			ArrayList<GiftListBeans> mList) {
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

	@SuppressLint({ "ViewHolder", "InflateParams" }) @Override
	public View getView(int position, View view, ViewGroup parent) {
		view = mLayoutInflater.inflate(R.layout.image_layout, null);
		Holder holder = getHolder(view);
		
		LayoutParams para = holder.image.getLayoutParams();
		para.width = parent.getWidth();
		para.height = (para.width * 237) / 800;
		holder.image.setLayoutParams(para);

		final GiftListBeans listBeans = mList.get(position);
		
		ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
		mImageLoader.loadImage(listBeans.getImageUrl(), holder.image);
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
		}
	}

}
