package com.umiwi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.UmiwiJPZTBetailBeans;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.ArrayList;

import cn.youmi.framework.util.ImageLoader;

/**
 * 精品专题 详情页适配器
 * @author tangxiyong
 * 2013-12-19下午4:53:25
 *
 */
public class UmiwiJPZTDetailAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;

	private ArrayList<UmiwiJPZTBetailBeans> mList;

	public UmiwiJPZTDetailAdapter(Context context,
			ArrayList<UmiwiJPZTBetailBeans> mList) {
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
		view = mLayoutInflater.inflate(R.layout.item_course, null);
		Holder holder = getHolder(view);

		final UmiwiJPZTBetailBeans listBeans = mList.get(position);
		
		ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
		mImageLoader.loadImage(listBeans.getImage(), holder.image);
		
		holder.title.setText(listBeans.getTitle());
		holder.authorname.setText(listBeans.getAuthorname());
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
		public TextView authorname;

		public Holder(View view) {
			image = (ImageView) view.findViewById(R.id.image);
			title = (TextView) view.findViewById(R.id.title);
			authorname = (TextView) view.findViewById(R.id.authorname);
		}
	}

}
