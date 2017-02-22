package com.umiwi.ui.adapter;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.youmi.framework.main.ContextProvider;
import cn.youmi.framework.util.ImageLoader;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.UmiwiListBeans;
import com.umiwi.ui.main.UmiwiApplication;

public class CourseAdapter extends BaseAdapter {

	private ArrayList<UmiwiListBeans> videos;

	public void setVideos(ArrayList<UmiwiListBeans> videos){
		this.videos = videos;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return videos == null ? 0 : (videos.size() > 6 ? 6 : videos.size());
	}

	@Override
	public UmiwiListBeans getItem(int position) {
		
		if(videos != null && videos.size() > 0) {
			return videos.get(position);
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
		Holder holder = null;
		if(view == null){
			view = LayoutInflater.from(UmiwiApplication.getInstance()).inflate(R.layout.item_course, null, false);
			holder = new Holder(view);
		}else{
			holder = (Holder) view.getTag();
		}
		UmiwiListBeans video = getItem(position);
		
		ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
		mImageLoader.loadImage(video.getImage(), holder.imageView);
        
		holder.titleTextView.setText(video.getTitle());
		holder.authorNameTextView.setText(video.getAuthorname());
		return view;
	}

	private class Holder {
		public ImageView imageView;
		public TextView titleTextView;
		public TextView authorNameTextView;
		public Holder(View view) {
			imageView = (ImageView) view.findViewById(R.id.image);
			titleTextView = (TextView) view.findViewById(R.id.title);
			authorNameTextView = (TextView) view.findViewById(R.id.authorname);
			view.setTag(this);
		}
	}
}
