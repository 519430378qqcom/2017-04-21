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
import com.umiwi.ui.beans.CommentListBeans;
import com.umiwi.ui.main.UmiwiApplication;

/**
 * 课程详情页面的评论Adapter 
 */
public class CommentAdapter extends BaseAdapter {
	private Context mContext;

	private LayoutInflater mLayoutInflater;
	
	private ArrayList<CommentListBeans> datas;
	
	public CommentAdapter(Context context) {
		this.mContext = context;
		this.mLayoutInflater = LayoutInflater.from(context);
	}
	
	public void setData(ArrayList<CommentListBeans> datas) {
		this.datas = datas;
		this.notifyDataSetChanged();
	}

	public CommentAdapter(Context mContext, ArrayList<CommentListBeans> datas) {
		super();
		this.mContext = mContext;
		this.datas = datas;
		this.mLayoutInflater = LayoutInflater.from(mContext);
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
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.item_comment, null);
			holder = new ViewHolder();
			holder.iconImageView = (ImageView) convertView
					.findViewById(R.id.userhead_imageview);
				
			holder.nameTextView = (TextView) convertView
					.findViewById(R.id.username_textview);
			holder.timeTextView = (TextView) convertView
					.findViewById(R.id.time_textview);
			holder.contentTextView = (TextView) convertView
					.findViewById(R.id.content_textview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		CommentListBeans commentBean = datas.get(position);
		if(!TextUtils.isEmpty(commentBean.getUsername())) {
			holder.nameTextView.setText(commentBean.getUsername());
		}
		
		if(!TextUtils.isEmpty(commentBean.getHeadimg())) {
			ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
			mImageLoader.loadImage(commentBean
					.getHeadimg(), holder.iconImageView);
			
		}
		if(!TextUtils.isEmpty(commentBean.getCtime()) && commentBean.getCtime().split(" ").length > 1) {
			holder.timeTextView.setText(commentBean.getCtime().split(" ")[0]);
		}
		
		if(!TextUtils.isEmpty(commentBean.getQuestion())) {
			holder.contentTextView.setText(commentBean.getQuestion());
		}

		return convertView;
	}

	private static class ViewHolder {

		private TextView nameTextView;
		private TextView timeTextView;
		private ImageView iconImageView;
		private TextView contentTextView;
	}

}
