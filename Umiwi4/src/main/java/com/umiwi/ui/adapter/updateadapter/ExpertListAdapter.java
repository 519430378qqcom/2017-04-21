package com.umiwi.ui.adapter.updateadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.LecturerBean.LecturerBeanWapper;
import com.umiwi.ui.beans.updatebeans.CelebrityBean;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.ArrayList;
import java.util.List;

import cn.youmi.framework.util.AndroidSDK;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.view.CircleImageView;
import za.co.immedia.pinnedheaderlistview.SectionedBaseAdapter;

/**
 * 讲师列表Adapter
 *  
 */
public class ExpertListAdapter extends SectionedBaseAdapter {

	private int height = 0;
	private int width = 0;

	private LayoutInflater inflater;

    List<CelebrityBean.CelebrityBeanRet>  lecturerWappers;

	public ExpertListAdapter(Context context) {
		super();
		this.inflater = LayoutInflater.from(context);
	}

	public void setLecturers(List<CelebrityBean.CelebrityBeanRet>  lecturerWappers) {
		this.lecturerWappers = lecturerWappers;
		this.notifyDataSetChanged();
	}
	
	@Override
	public boolean hasStableIds() {
		return false;
	}

	
	private static class CharacterViewHolder {
		TextView characterTextView;
	}
	
	private static class LecturerViewHolder {
		ImageView consultImageView;
		CircleImageView iconImageView;
		TextView nameTextView;
		TextView jobTitleTextView;
	}

	@Override
	public Object getItem(int section, int position) {
		return lecturerWappers.get(section).getContent().get(position);
	}

	@Override
	public long getItemId(int section, int position) {
		return position;
	}

	@Override
	public int getSectionCount() {
		if(lecturerWappers != null && lecturerWappers.size() > 0) {
			return lecturerWappers.size();
		}
		return 0;
	}

	@Override
	public int getCountForSection(int section) {
		if(lecturerWappers != null) {
			return lecturerWappers.get(section).getContent().size();
		}
		return 0;
	}

	@Override
	public View getItemView(int section, int position, View convertView,
			ViewGroup parent) {
		LecturerViewHolder holder = null;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.lecturer_content_item, null);
			
			holder = new LecturerViewHolder();
			holder.nameTextView = (TextView)convertView.findViewById(R.id.title); 
			holder.jobTitleTextView = (TextView)convertView.findViewById(R.id.lecturer_titile); 
			holder.iconImageView = (CircleImageView)convertView.findViewById(R.id.image); 
			holder.consultImageView=(ImageView) convertView.findViewById(R.id.consult_pic);

			final View rightContainer = convertView.findViewById(R.id.right_container);
			final View iconView = holder.iconImageView;
			
			if(height == 0 || width ==0) {
				rightContainer.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (AndroidSDK.isJELLY_BEAN()) {
							rightContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
						} else {
							rightContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
						}
						
						
						height = rightContainer.getHeight();
						width = rightContainer.getHeight();
						ViewGroup.LayoutParams params = iconView.getLayoutParams();
						params.width = width;
						params.height = height;
						iconView.setLayoutParams(params);
					}
				});
			} else {
				ViewGroup.LayoutParams params = iconView.getLayoutParams();
				params.width = width;
				params.height = height;
				iconView.setLayoutParams(params);
			}

			convertView.setTag(holder); 
		} else {
			holder = (LecturerViewHolder) convertView.getTag();
		}

		holder.nameTextView.setText(lecturerWappers.get(section).getContent().get(position).getName());
		holder.jobTitleTextView.setText(lecturerWappers.get(section).getContent().get(position).getTitle());

		ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
		mImageLoader.loadImage(lecturerWappers.get(section).getContent().get(position).getImage(), holder.iconImageView);

		
		return convertView;
	}

	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		CharacterViewHolder holder = null;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.lecturer_listadapter_item_title, null);
			
			holder = new CharacterViewHolder();
			holder.characterTextView = (TextView)convertView.findViewById(R.id.lastname_textview); 
            
			convertView.setTag(holder); 
		} else {
			holder = (CharacterViewHolder) convertView.getTag();
		}
		
		holder.characterTextView.setText(lecturerWappers.get(section).getPinyinname());
		
		convertView.setClickable(true);
		
		return convertView;
	}

}

