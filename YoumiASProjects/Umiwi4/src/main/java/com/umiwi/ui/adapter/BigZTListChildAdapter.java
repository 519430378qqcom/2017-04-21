package com.umiwi.ui.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.youmi.framework.main.ContextProvider;
import cn.youmi.framework.util.DimensionUtil;
import cn.youmi.framework.util.ImageLoader;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.model.BigZTCourseListModel;
/**
 * 
 * @author umiwi
 *
 */
public class BigZTListChildAdapter extends BaseAdapter {

	private Context mContext;
	
	private Activity mActivity;
	
	private LayoutInflater mLayoutInflater;
	
	private ArrayList<BigZTCourseListModel> datas;
	
	public BigZTListChildAdapter(Context context) {
		this.mContext = context;
		this.mActivity = (Activity) context;
		mLayoutInflater = LayoutInflater.from(UmiwiApplication.getContext());
	}
	
	public void setData(ArrayList<BigZTCourseListModel> datas) {
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
			convertView = mLayoutInflater.inflate(R.layout.item_bigztlist_grand, null);
			
			viewHolder = new StageSectionChildViewHolder();
			viewHolder.stageSectionTextView = (TextView) convertView.findViewById(R.id.title);
			viewHolder.subscriptName = (TextView) convertView.findViewById(R.id.subscript_name);
			viewHolder.stageSectionImageView = (ImageView) convertView.findViewById(R.id.image);
			
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (StageSectionChildViewHolder)convertView.getTag();
		}
		
		BigZTCourseListModel data = datas.get(position);
		
		convertView.setTag(R.id.key_stage_section, data);
		
		convertView.setOnClickListener(onClickListener);
		
		viewHolder.stageSectionTextView.setText(data.title);
		viewHolder.subscriptName.setText(data.price);
		
		ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
		
		int w = (parent.getWidth() == 0) ? DimensionUtil.getScreenWidth(mActivity) : parent.getWidth();
		int h = (3 * w) / 4;
		LayoutParams para = viewHolder.stageSectionImageView.getLayoutParams();
		para.width = w / 2 ;
		para.height = h / 2 ;
		viewHolder.stageSectionImageView.setLayoutParams(para);
		
		mImageLoader.loadImage(data.image, viewHolder.stageSectionImageView);
		
		
		return convertView;
	}
	
	static class StageSectionChildViewHolder {
		ImageView stageSectionImageView;
		TextView stageSectionTextView;
		TextView subscriptName;
	}

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			BigZTCourseListModel data = (BigZTCourseListModel) v.getTag(R.id.key_stage_section);

			Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
			intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
			intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, data.detailurl);
			mContext.startActivity(intent);
		
				
		}
	};
	
}
