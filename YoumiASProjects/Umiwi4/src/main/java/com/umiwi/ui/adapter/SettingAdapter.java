package com.umiwi.ui.adapter;

import com.umiwi.ui.R;
import com.umiwi.ui.main.UmiwiApplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SettingAdapter extends BaseAdapter {

	@Override
	public int getCount() {
		return 10;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(UmiwiApplication.getInstance()).inflate(R.layout.item_settings, null);
		}
		return convertView;
	}

}
