package com.umiwi.ui.adapter;

import java.util.ArrayList;

import za.co.immedia.pinnedheaderlistview.SectionedBaseAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.youmi.framework.util.ImageLoader;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.ChartsListBean;

/**
 * 最近更新列表Adapter
 * 
 */
public class ChatsListAdapter extends SectionedBaseAdapter {
	
	private static final int[] images = { R.drawable.ic_charts_01,
			R.drawable.ic_charts_02, R.drawable.ic_charts_03,
			R.drawable.ic_charts_04, R.drawable.ic_charts_05,
			R.drawable.ic_charts_06 };
	private LayoutInflater inflater;
	private Context mContext;
	private ChartsListBean.RecentChangeWrapper recentChangeWrapper;

	public ChatsListAdapter(Context context) {
		super();
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
	}

	public void setDatas(ChartsListBean.RecentChangeWrapper recentChangeWrapper) {
		this.recentChangeWrapper = recentChangeWrapper;
		this.notifyDataSetChanged();
	}

	public int findGroupCount() {
		ArrayList<ChartsListBean> child = null;
		int groupCount = 0;
		if (recentChangeWrapper != null) {
			child = recentChangeWrapper.getDay();
			if (child != null && child.size() > 0) {
				groupCount++;
			}

			child = recentChangeWrapper.getWeek();
			if (child != null && child.size() > 0) {
				groupCount++;
			}

			child = recentChangeWrapper.getMonth();
			if (child != null && child.size() > 0) {
				groupCount++;
			}

		}
		return groupCount;
	}

	public ArrayList<ChartsListBean> findGroup(int groupPosition) {
		ArrayList<ChartsListBean> group = null;
		if (recentChangeWrapper != null) {
			switch (groupPosition) {
			case 0:
				group = recentChangeWrapper.getDay();
				break;
			case 1:
				group = recentChangeWrapper.getWeek();
				break;
			case 2:
				group = recentChangeWrapper.getMonth();
				break;
			}
		}
		return group;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	private static class TypeViewHolder {
		ImageView dotImage;
		TextView typeTextView;
	}

	private static class RecentChangeViewHolder {
		ImageView iconImageView;
		TextView titleTextView;
		TextView desTextView;
	}

	@Override
	public Object getItem(int section, int position) {
		return null;
	}

	@Override
	public long getItemId(int section, int position) {
		return 0;
	}

	@Override
	public int getSectionCount() {
		return findGroupCount();
	}

	@Override
	public int getCountForSection(int section) {
		return findGroup(section).size();
	}

	@SuppressLint("InflateParams")
	@Override
	public View getItemView(int section, int position, View convertView,
			ViewGroup parent) {
		RecentChangeViewHolder holderView = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_chats, null);

			holderView = new RecentChangeViewHolder();
			holderView.titleTextView = (TextView) convertView
					.findViewById(R.id.title);
			holderView.desTextView = (TextView) convertView
					.findViewById(R.id.authorname);
			holderView.iconImageView = (ImageView) convertView
					.findViewById(R.id.image);

			convertView.setTag(holderView);
		} else {
			holderView = (RecentChangeViewHolder) convertView.getTag();
		}

		ImageLoader mImageLoader = new ImageLoader(mContext);
		ChartsListBean child = findGroup(section).get(position);
		if (child != null) {
			if (!TextUtils.isEmpty(child.getTitle())) {
				holderView.titleTextView.setVisibility(View.VISIBLE);
				holderView.titleTextView.setText(child.getTitle());
			} else {
				holderView.titleTextView.setVisibility(View.INVISIBLE);
			}

			if (!TextUtils.isEmpty(child.getAuthorname())) {
				holderView.desTextView.setVisibility(View.VISIBLE);
				holderView.desTextView.setText(child.getAuthorname());
			} else {
				holderView.desTextView.setVisibility(View.INVISIBLE);
			}
			if (position < images.length) {
				mImageLoader.loadImage(images[position], holderView.iconImageView);
			}

		}
		return convertView;
	}

	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		TypeViewHolder holderView = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.section_item_recent_updates, null);

			holderView = new TypeViewHolder();
			holderView.typeTextView = (TextView) convertView
					.findViewById(R.id.textview);
			holderView.dotImage = (ImageView) convertView
					.findViewById(R.id.dot_image);

			convertView.setTag(holderView);
		} else {
			holderView = (TypeViewHolder) convertView.getTag();
		}

		String typeStr = null;
		int resId = 0;

		switch (section) {
		case 0:
			typeStr = "热播榜";
			resId = R.drawable.home_grid_line;
			break;
		case 1:
			typeStr = "热销榜";
			resId = R.drawable.home_grid_line;
			break;
		case 2:
			typeStr = "热评榜";
			resId = R.drawable.home_grid_line;
			break;
		}

		holderView.typeTextView.setText(typeStr);
		holderView.dotImage.setImageResource(resId);
		convertView.setClickable(true);
		return convertView;
	}
}
