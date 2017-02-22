package com.umiwi.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.UmiwiListBeans;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.ArrayList;

import cn.youmi.framework.util.ImageLoader;

/**
 * 列表适配器
 * 
 * @author tangxiyong 2013-11-29下午2:58:01
 * 
 */
public class UmiwiListAdapter extends BaseAdapter {
	
	private LayoutInflater mLayoutInflater;

	private ArrayList<UmiwiListBeans> mList;

	public UmiwiListAdapter(Context context) {
		super();
		mLayoutInflater = LayoutInflater.from(UmiwiApplication.getInstance());
	}

	
	public UmiwiListAdapter(Context context,
			ArrayList<UmiwiListBeans> mList) {
		mLayoutInflater = ((Activity) context).getLayoutInflater();
		this.mList = mList;
	}


	public void setData(ArrayList<UmiwiListBeans> mList) {
		this.mList = mList;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if(mList != null) 
			return mList.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(mList != null && position < mList.size() - 1) {
			return mList.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		view = mLayoutInflater.inflate(R.layout.item_course, null, false);
		 
		Holder holder = getHolder(view);

		final UmiwiListBeans listBeans = mList.get(position);

		if (mList != null && mList.size() > 0) {
			if (listBeans.getImage()!=null) {
				ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
				mImageLoader.loadImage(listBeans.getImage(), holder.image);
			} else {

			}
			if (listBeans.getTitle()!=null) {
				holder.title.setText(listBeans.getTitle());
			}
			if (listBeans.getAuthorname()!=null) {
				holder.authorname.setText(listBeans.getAuthorname());
			}
			if ("new".equals(listBeans.getIcontype())) {
				holder.type.setVisibility(View.VISIBLE);
			} else {
				holder.type.setVisibility(View.GONE);
			}
		} else {
			throw new IllegalStateException(view.getClass().getName() + "data is null or to is null");
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
		public ImageView type;

		public TextView title;

		public TextView authorname;

		public Holder(View view) {
			type = (ImageView) view.findViewById(R.id.coupon_type);
			image = (ImageView) view.findViewById(R.id.image);
			title = (TextView) view.findViewById(R.id.title);
			authorname = (TextView) view.findViewById(R.id.authorname);
		}
	}
}
