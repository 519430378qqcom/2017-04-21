package com.umiwi.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.LogoUserInfoBeans;
import com.umiwi.ui.view.RoundButton;

/**
 * 
 * @author tangxiyong 2014-4-28下午3:50:59
 * 
 */
public class LogoUserInfoGridViewAdapter extends BaseAdapter {
	public int selectedId = -1;
	private LayoutInflater mLayoutInflater;
	private static HashMap<Integer, Boolean> isSelected;
	
	private ArrayList<LogoUserInfoBeans> mList;
	private String nexturl;
	

	public LogoUserInfoGridViewAdapter(Context context,
			ArrayList<LogoUserInfoBeans> mList, String nexturl) {
		super();
		this.mList = mList;
		this.nexturl = nexturl;
		mLayoutInflater = ((Activity) context).getLayoutInflater();
		
		isSelected = new HashMap<Integer, Boolean>();
		initDate();
	}

	private void initDate() {
		for (int i = 0; i < mList.size(); i++) {
			getIsSelected().put(i, false);
		}
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.user_info_single_item, null);
			holder.image = (RoundButton) convertView.findViewById(R.id.round_button);
			holder.image_one = (RoundButton) convertView.findViewById(R.id.round_button_one);
			holder.content = (CheckedTextView) convertView.findViewById(R.id.checked_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (null != nexturl && !nexturl.contains("tagids") && mList != null && mList.size() != 0) {
			if (getIsSelected().get(position) != null) {
				holder.content.setChecked(getIsSelected().get(position));
			}
		}

		final LogoUserInfoBeans listBeans = mList.get(position);
		if (listBeans.getName() != null) {
			
			if ("学生".equals(listBeans.getName())) {
				holder.image_one.setBackgroundResource(R.drawable.user_test_student);
			}
			if ("职场人".equals(listBeans.getName())) {
				holder.image_one.setBackgroundResource(R.drawable.user_test_worker);
			}
			if ("创业者".equals(listBeans.getName())) {
				holder.image_one.setBackgroundResource(R.drawable.user_test_business);
			}
			
		}
		if (listBeans.getColor() != null) {
			holder.image.setColor(Color.parseColor("#"+listBeans.getColor()));
			holder.image_one.setVisibility(View.GONE);
			holder.image.setVisibility(View.VISIBLE);
			holder.content.setText(listBeans.getName());
		}
		return convertView;
	}
	
	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		LogoUserInfoGridViewAdapter.isSelected = isSelected;
	}
}
