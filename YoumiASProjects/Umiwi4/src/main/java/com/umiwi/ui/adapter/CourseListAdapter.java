package com.umiwi.ui.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.youmi.framework.main.ContextProvider;
import cn.youmi.framework.util.ImageLoader;

import com.umiwi.ui.R;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.model.CourseListModel;

/**
 * 
 * @author tangixyong
 * @version 2015-5-8 下午2:29:21
 *
 */
public class CourseListAdapter extends BaseAdapter {

	private LayoutInflater mLayoutInflater;

	private ArrayList<CourseListModel> mList;

	public CourseListAdapter(Context context) {
		super();
		mLayoutInflater = LayoutInflater.from(UmiwiApplication.getInstance());
	}

	public CourseListAdapter(Context context, ArrayList<CourseListModel> mList) {
		mLayoutInflater  = LayoutInflater.from(UmiwiApplication.getInstance());
		this.mList = mList;
	}

	public void setData(ArrayList<CourseListModel> mList) {
		this.mList = mList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mList != null)
			return mList.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mList != null && position < mList.size() - 1) {
			return mList.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "InflateParams", "ViewHolder" })
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		view = mLayoutInflater.inflate(R.layout.item_course, null);

		Holder holder = getHolder(view);

		final CourseListModel listBeans = mList.get(position);

		ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
		mImageLoader.loadImage(listBeans.image, holder.image);
		holder.title.setText(listBeans.title);
		holder.authorname.setText(listBeans.authorname);

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
