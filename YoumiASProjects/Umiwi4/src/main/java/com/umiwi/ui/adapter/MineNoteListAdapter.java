package com.umiwi.ui.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.youmi.framework.main.ContextProvider;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.util.ListViewUtils;

import com.umiwi.ui.R;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.model.CourseListModel;

/**
 * 我的笔记
 * 
 * @author tangxiong
 * @version 2014年6月16日 下午5:56:34
 */
public class MineNoteListAdapter extends BaseAdapter {

	private LayoutInflater mLayoutInflater;

	private ArrayList<CourseListModel> mList;

	public MineNoteListAdapter(Context context) {
		super();
		mLayoutInflater = LayoutInflater.from(UmiwiApplication.getInstance());
	}

	public MineNoteListAdapter(Context context, ArrayList<CourseListModel> mList) {
		mLayoutInflater = ((Activity) context).getLayoutInflater();
		this.mList = mList;
	}

	@Override
	public int getCount() {
		return ListViewUtils.getSize(mList);
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position % mList.size());
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		view = mLayoutInflater.inflate(R.layout.item_course, null);

		Holder holder = getHolder(view);

		final CourseListModel listBeans = mList.get(position);

		ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
		mImageLoader.loadImage(listBeans.image, holder.image);
		holder.title.setText(listBeans.title + "");
		holder.authorname.setText(listBeans.notesnum + "");

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
