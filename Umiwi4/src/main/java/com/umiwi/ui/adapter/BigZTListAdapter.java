package com.umiwi.ui.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.model.BigZTCourseListModel;
import com.umiwi.ui.model.BigZTList;
import com.umiwi.ui.view.PayGridView;

/**
 * 
 * @author umiwi
 *
 */
public class BigZTListAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	
	private Activity mActivity;

	private LayoutInflater mLayoutInflater;
	
	private ArrayList<BigZTList> datas;
	
	public BigZTListAdapter(Context context) {
		this.mContext = context;
//		this.mActivity = (Activity) context;
		mLayoutInflater = LayoutInflater.from(UmiwiApplication.getContext());
	}
	
	public void setData(ArrayList<BigZTList> datas) {
		this.datas = datas;
		this.notifyDataSetChanged();
	}
	
	public ArrayList<BigZTList> getDatas() {
		return this.datas;
	}
	
	@Override
	public int getGroupCount() {
		if(datas != null && datas.size() > 0) {
			return datas.size();
		}
		return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(datas != null && datas.size() > groupPosition) {
			return (datas.get(groupPosition) != null) ? 1 : 0;
		}
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		if(datas != null && datas.size() > groupPosition) {
			return datas.get(groupPosition);
		}
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		if(datas != null && datas.size() > groupPosition) {
			BigZTList children = datas.get(groupPosition);
			if(children != null && children.bigZTCourseList != null && children.bigZTCourseList.size() > childPosition) {
				return children.bigZTCourseList.get(childPosition);
			}
		}
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@SuppressLint("InflateParams") @Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ParentViewHolder viewHolder = null;
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.item_bigztlist_parent, null);
			
			viewHolder = new ParentViewHolder();
			viewHolder.subTitle = (TextView) convertView.findViewById(R.id.sub_title);
			viewHolder.subDescription = (TextView) convertView.findViewById(R.id.sub_description);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ParentViewHolder)convertView.getTag();
		}
		
		viewHolder.subTitle.setText(datas.get(groupPosition).subTitle);
		viewHolder.subDescription.setText(datas.get(groupPosition).subDescription);
		
		return convertView;
	}

	@SuppressLint("InflateParams") @Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder viewHolder = null;
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.item_stage_section_child, null);
			
			viewHolder = new ChildViewHolder();
			viewHolder.childGridView = (PayGridView) convertView.findViewById(R.id.stage_section_child_gridview);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ChildViewHolder)convertView.getTag();
		}
		
		ArrayList<BigZTCourseListModel> data = datas.get(groupPosition).bigZTCourseList;
		
		BigZTListChildAdapter childAdapter = new BigZTListChildAdapter(mContext);
		viewHolder.childGridView.setAdapter(childAdapter);
		childAdapter.setData(data);
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	static class ParentViewHolder {
		TextView subTitle, subDescription;
	}
	
	static class ChildViewHolder {
		PayGridView childGridView;
	}
}
