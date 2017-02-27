package com.umiwi.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.StageSectionBean;
import com.umiwi.ui.beans.StageSectionBean.StageSectionBeanWapper;
import com.umiwi.ui.view.PayGridView;

/**
 * 创业类别的adapter 
 */
public class StageSectionAdapter extends BaseExpandableListAdapter {
	private Context mContext;

	private LayoutInflater mLayoutInflater;
	
	private ArrayList<StageSectionBeanWapper> datas;
	
	public StageSectionAdapter(Context context) {
		this.mContext = context;
		mLayoutInflater = LayoutInflater.from(context);
	}
	
	public void setData(ArrayList<StageSectionBeanWapper> datas) {
		this.datas = datas;
		this.notifyDataSetChanged();
	}
	
	public ArrayList<StageSectionBeanWapper> getDatas() {
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
			StageSectionBeanWapper children = datas.get(groupPosition);
			if(children != null && children.getList() != null && children.getList().size() > childPosition) {
				return children.getList().get(childPosition);
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

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ParentViewHolder viewHolder = null;
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.item_stage_section_parent, null);
			
			viewHolder = new ParentViewHolder();
			viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.stage_section_type_textview);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ParentViewHolder)convertView.getTag();
		}
		
		viewHolder.nameTextView.setText(datas.get(groupPosition).getName());
		
		return convertView;
	}

	@Override
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
		
		ArrayList<StageSectionBean> data = datas.get(groupPosition).getList();
		
		StageSectionChildAdapter childAdapter = new StageSectionChildAdapter(mContext);
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
		TextView nameTextView;
	}
	
	static class ChildViewHolder {
		PayGridView childGridView;
	}
}
