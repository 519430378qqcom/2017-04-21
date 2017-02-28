package com.umiwi.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.beans.StageSectionBean;
import com.umiwi.ui.fragment.LecturerListFragment;
import com.umiwi.ui.fragment.course.BigZTListFragment;
import com.umiwi.ui.fragment.course.ChartsListFragment;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.fragment.course.CourseSequenceListFragment;
import com.umiwi.ui.fragment.course.JPZTDetailFragment;
import com.umiwi.ui.fragment.course.JPZTListFragment;
import com.umiwi.ui.fragment.course.VipFragment;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.ArrayList;

import cn.youmi.framework.util.ImageLoader;

/**
 *  创业类别adapter的子GridView的adapter 
 */
public class StageSectionChildAdapter extends BaseAdapter {

	private Context mContext;
	
	private LayoutInflater mLayoutInflater;
	
	private ArrayList<StageSectionBean> datas;
	
	public StageSectionChildAdapter(Context context) {
		this.mContext = context;
		mLayoutInflater = LayoutInflater.from(context);
	}
	
	public void setData(ArrayList<StageSectionBean> datas) {
		this.datas = datas;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if(datas != null) {
			return datas.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(datas != null && datas.size() > position) {
			return datas.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		if(datas != null && datas.size() > position) {
			return position;
		}
		return 0;
	}

	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		StageSectionChildViewHolder viewHolder = null;
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.item_stage_section_grand, null);
			
			viewHolder = new StageSectionChildViewHolder();
			viewHolder.stageSectionTextView = (TextView) convertView.findViewById(R.id.stage_section_textview);
			viewHolder.stageSectionImageView = (ImageView) convertView.findViewById(R.id.stage_section_imageview);
			viewHolder.rightLine = convertView.findViewById(R.id.right_line);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (StageSectionChildViewHolder)convertView.getTag();
		}
		
		StageSectionBean data = datas.get(position);
		
		if (((position + 2) % 2) != 1) {
			viewHolder.rightLine.setVisibility(View.VISIBLE);
		}
		
		convertView.setTag(R.id.key_stage_section, data);
		
		convertView.setOnClickListener(stageClickListener);
		
		viewHolder.stageSectionTextView.setText(data.getName());
		
		ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
		mImageLoader.loadImage(data.getIcon(), viewHolder.stageSectionImageView);
		
		
		return convertView;
	}
	
	static class StageSectionChildViewHolder {
		ImageView stageSectionImageView;
		TextView stageSectionTextView;
		View rightLine;
	}

	private OnClickListener stageClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			StageSectionBean data = (StageSectionBean) v.getTag(R.id.key_stage_section);
			
			Intent intent = new Intent(mContext, UmiwiContainerActivity.class);

			switch (data.categoryType()) {
			case COURSE:
				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
				intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, data.getUrl());
				break;
			case NEWCOURSE:
			case CATEGORY:
				intent.putExtra(CourseSequenceListFragment.KEY_URL, data.getUrl());
				intent.putExtra(CourseSequenceListFragment.KEY_ACTION_TITLE, data.getName());
				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseSequenceListFragment.class);
				break;
			case ZHUANTI2:
				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, JPZTDetailFragment.class);
				intent.putExtra(JPZTDetailFragment.KEY_URL, data.getUrl());
				break;
			case ZHUANTILIST:
				intent.putExtra(JPZTListFragment.KEY_URL, data.getUrl());
				intent.putExtra(JPZTListFragment.KEY_ACTION_TITLE, data.getName());
				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, JPZTListFragment.class);
				break;
			case ZHUANTI3://BigZTListFragment
				intent.putExtra(BigZTListFragment.KEY_URL, data.getUrl());
				intent.putExtra(BigZTListFragment.KEY_ACTION_TITLE, data.getName());
				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, BigZTListFragment.class);
				break;
			case TUTOR:
				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LecturerListFragment.class);
				break;
			case RANKLIST:
				intent.putExtra(ChartsListFragment.KEY_URL, data.getUrl());
				intent.putExtra(ChartsListFragment.KEY_ACTION_TITLE, data.getName());
				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ChartsListFragment.class);
				break;
			case DIAMOND:
				intent.putExtra(VipFragment.KEY_ACTION_TITLE, data.getName());
				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VipFragment.class);
				break;

			default:
				break;
			}
			
			mContext.startActivity(intent);
		
				
		}
	};
	
}
