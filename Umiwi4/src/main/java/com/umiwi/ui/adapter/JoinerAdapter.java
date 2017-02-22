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
import com.umiwi.ui.beans.ActivityBean.Joiner;
import com.umiwi.ui.main.UmiwiApplication;

public class JoinerAdapter extends BaseAdapter {
	
	ArrayList<Joiner> joiners;
	
	public void setData(ArrayList<Joiner> joiners){
		this.joiners = joiners;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return joiners == null ? 0 : joiners.size();
	}

	@Override
	public Joiner getItem(int position) {
		return joiners.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(UmiwiApplication.getInstance()).inflate(R.layout.item_joiner, null);
		}
		ImageView iv = (ImageView) convertView.findViewById(R.id.avatar_image_view);
		TextView nameText = (TextView) convertView.findViewById(R.id.name_text_view);
		
		ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
		mImageLoader.loadImage(getItem(position).avatarURL, iv);
		nameText.setText(getItem(position).username);
		return convertView;
		
	}

}
