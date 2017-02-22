package com.umiwi.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.ActivityBean.TutorInfo;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.ArrayList;

import cn.youmi.framework.util.ImageLoader;

public class TutorAdapter extends BaseAdapter {

	ArrayList<TutorInfo> tutors = null;
	
	public void setData(ArrayList<TutorInfo> tutors){
		this.tutors = tutors;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return tutors == null ? 0 : tutors.size();
	}

	@Override
	public TutorInfo getItem(int position) {
		return tutors.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = LayoutInflater.from(UmiwiApplication.getInstance()).inflate(R.layout.item_tutor, null);
		ImageView avatarImageView = (ImageView) v.findViewById(R.id.avatar_image);
		TextView nameTextView = (TextView) v.findViewById(R.id.name_text_view);
		TextView titleTextView = (TextView) v.findViewById(R.id.title_text_view);
		nameTextView.setText(getItem(position).name);
		titleTextView.setText(getItem(position).title);
		
		ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
		mImageLoader.loadImage(getItem(position).avatarURL, avatarImageView);
		
		return v;
	}

}
