package com.umiwi.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.youmi.framework.main.ContextProvider;
import cn.youmi.framework.util.ImageLoader;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.CourseBean;
import com.umiwi.ui.main.UmiwiApplication;

/**
 * 讲师详情页面课程列表 适配器
 * 
 */
public class LecturerCourseListAdapter extends BaseAdapter {

	private LayoutInflater mLayoutInflater;
	private ArrayList<CourseBean> courseList;
	public LecturerCourseListAdapter(Context context) {
		super();
		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if(courseList != null) 
			return courseList.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(courseList != null) 
			return courseList.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		CouserViewHolder holder = null;
		
		if(view == null) {
			view = mLayoutInflater.inflate(R.layout.course_content_item, null);
			
			holder = new CouserViewHolder();
			holder.title = (TextView) view.findViewById(R.id.course_titile);
			holder.playtimes = (TextView) view.findViewById(R.id.playtimes);
			holder.couserThumbnail = (ImageView) view.findViewById(R.id.course_image);
			
			view.setTag(holder);
		} else {
			holder = (CouserViewHolder) view.getTag();
		}
		
		
		final CourseBean course = courseList.get(position);
		if(!TextUtils.isEmpty(course.getTitle())) {
			holder.title.setVisibility(View.VISIBLE);
			holder.title.setText(course.getTitle());
		} else {
			holder.title.setVisibility(View.INVISIBLE);
		}
		
		if(!TextUtils.isEmpty(course.getWatchnum())) {
			holder.playtimes.setVisibility(View.VISIBLE);
			holder.playtimes.setText(course.getWatchnum());
		} else {
			holder.playtimes.setVisibility(View.INVISIBLE);
		}
		
		ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
		mImageLoader.loadImage(course.getImage(), holder.couserThumbnail);
		
		return view;
	}
	
	private static class CouserViewHolder {
		/** 课程名 */
		public TextView title;
		/** 播放次数 */
		public TextView playtimes;
		/** 课程图片 */
		public ImageView couserThumbnail;
		
	}
	
	public void setCourses(ArrayList<CourseBean> courseList) {
		this.courseList = courseList;
		this.notifyDataSetChanged();
	}

}
