package com.umiwi.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import za.co.immedia.pinnedheaderlistview.SectionedBaseAdapter;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.youmi.framework.util.DimensionUtil;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.UmiwiMyRecordBeans;
import com.umiwi.ui.main.UmiwiApplication;

/**
 * 播放记录 适配器
 * 
 * @author tangxiyong 2013-12-9上午10:57:24
 * @param <key>
 * 
 */
public class MyRecordAdapter extends SectionedBaseAdapter {
	
	public interface OnSelectionNumberChangeListener{
		void onSelectionNumberChange(int num);
	}
	
	private OnSelectionNumberChangeListener mOnSelectionNumberChangeListener;
	
	public void setOnSelectionNumberChangeListener(OnSelectionNumberChangeListener l){
		mOnSelectionNumberChangeListener = l;
	}
	private class SectionedBean {
		private String sectionName;
		private ArrayList<UmiwiMyRecordBeans> beans = new ArrayList<UmiwiMyRecordBeans>();

		@Override
		public boolean equals(Object o) {
			if ((o instanceof SectionedBean)) {
				SectionedBean other = (SectionedBean) o;
				return other.sectionName.equals(sectionName);
			}
			return super.equals(o);
		}
	}

	private LayoutInflater mLayoutInflater;
	private ArrayList<SectionedBean> mList;
	private boolean mEditMode;
	private HashMap<UmiwiMyRecordBeans, Boolean> selectionMap = new HashMap<UmiwiMyRecordBeans, Boolean>();

	public void setEditModel(boolean editMode) {
		this.mEditMode = editMode;
		notifyDataSetChanged();
	}

	private HashMap<String, Integer> deviceMap = new HashMap<String, Integer>();
	private int[] ballResIds = new int[]{R.drawable.circle_green,R.drawable.circle_blue,R.drawable.circle_gray};

	public MyRecordAdapter() {
		super();
		mLayoutInflater = LayoutInflater.from(UmiwiApplication.getInstance());
		deviceMap.put("pad", R.drawable.ic_pad);
		deviceMap.put("phone", R.drawable.ic_phone);
		deviceMap.put("pc", R.drawable.ic_pc);
	}

	public void remove(String id) {
		SectionedBean targetSection = null;
		UmiwiMyRecordBeans targetBean = null;
		for (SectionedBean sb : mList) {
			for (UmiwiMyRecordBeans rb : sb.beans) {
				if (rb.getVid().equals(id)) {
					targetSection = sb;
					targetBean = rb;
					break;
				}
			}
		}
		if (targetSection != null) {
			targetSection.beans.remove(targetBean);
			if (targetSection.beans.isEmpty()) {
				mList.remove(targetSection);
			}
			notifyDataSetChanged();
		}
	}

	public void remove(List<UmiwiMyRecordBeans> beans) {
		for (UmiwiMyRecordBeans rb : beans) {
			for (SectionedBean sb : mList) {
				sb.beans.remove(rb);
			}
			selectionMap.remove(rb);
		}
		notifyDataSetChanged();
	}

	public int size() {
		int count = 0;
		for (SectionedBean sb : mList) {
			count += sb.beans.size();
		}
		return count;
	}

	public void append(ArrayList<UmiwiMyRecordBeans> beans) {
		if (mList == null) {
			mList = new ArrayList<SectionedBean>();
		}
		for (UmiwiMyRecordBeans b : beans) {
			boolean found = false;
			innerLoop: for (SectionedBean s : mList) {
				if (s.sectionName.equals(b.getGroupname())) {
					found = true;
					s.beans.add(b);
					break innerLoop;
				}
			}
			if (!found) {
				SectionedBean sb = new SectionedBean();
				sb.sectionName = b.getGroupname();
				sb.beans.add(b);
				mList.add(sb);
			}
		}
		notifyDataSetChanged();
	}

	private Holder getHolder(final View view, int section, int position) {
		Holder holder = (Holder) view.getTag();
		if (holder == null) {
			holder = new Holder(view, section, position);
			view.setTag(holder);
		}
		holder.section = section;
		holder.position = position;
		return holder;
	}

	private class Holder {
		/** 课程名 */
		public TextView tv_title;
		/** 播放时间 */
		public TextView tv_time;
		private int position;
		private int section;
		CheckBox selectionCheckBox;
		private OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				selectionMap.put(mList.get(section).beans.get(position),
						Boolean.valueOf(Boolean.valueOf(isChecked)));
				if(mOnSelectionNumberChangeListener != null){
					mOnSelectionNumberChangeListener.onSelectionNumberChange(getSelectionNumber());
				}
			}
		};

		private GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				selectionCheckBox.setChecked(!selectionCheckBox.isChecked());
				return true;
			}
		};

		GestureDetector mGestureDetector = new GestureDetector(
				UmiwiApplication.getInstance(), mGestureListener);

		private OnTouchListener mOnTouchListener = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mEditMode) {
					mGestureDetector.onTouchEvent(event);
					return true;
				}
				return false;
			}
		};

		/** 播放按钮 */

		public Holder(View view, int section, int position) {
			tv_title = (TextView) view.findViewById(R.id.myrecord_title);
			tv_time = (TextView) view.findViewById(R.id.myrecord_time);
			this.section = section;
			this.position = position;
			selectionCheckBox = (CheckBox) view
					.findViewById(R.id.selection_checkbox);
			selectionCheckBox
					.setOnCheckedChangeListener(mCheckedChangeListener);
			view.setOnTouchListener(mOnTouchListener);
		}
	}

	@Override
	public UmiwiMyRecordBeans getItem(int section, int position) {
		return mList.get(section).beans.get(position);
	}

	@Override
	public long getItemId(int section, int position) {
		return section * 100 + position;
	}

	@Override
	public int getSectionCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public int getCountForSection(int section) {
		return mList.get(section).beans.size();
	}

	@Override
	public View getItemView(int section, int position, View convertView,
			ViewGroup parent) {
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.listitem_myrecord,
					null);
		}

		Holder holder = getHolder(convertView, section, position);
		final UmiwiMyRecordBeans listBeans = mList.get(section).beans
				.get(position);
		holder.tv_title.setText(listBeans.getTitle());
		holder.selectionCheckBox.setVisibility(mEditMode ? View.VISIBLE
				: View.GONE);
		Boolean selected = selectionMap.get(mList.get(section).beans
				.get(position));
		if (selected == null) {
			selected = Boolean.FALSE;
		}
		holder.selectionCheckBox.setChecked(selected);

		Integer resId = deviceMap.get(listBeans.getTerminal());
		if(resId == null){
			resId = R.drawable.ic_pc;
		}

		holder.tv_time.setText(listBeans.getProgressText());
		Drawable d = UmiwiApplication.getInstance().getResources()
				.getDrawable(resId);
		d.setBounds(0, 0, DimensionUtil.sp2px(15), DimensionUtil.sp2px(15));
		holder.tv_time.setCompoundDrawables(d, null, null, null);
		holder.tv_time.setCompoundDrawablePadding(8);
		return convertView;
	}

	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(UmiwiApplication.getInstance())
					.inflate(R.layout.section_item_recent_updates, null);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.textview);
		tv.setText(mList.get(section).sectionName);
		ImageView dotImage = (ImageView) convertView.findViewById(R.id.dot_image);
		if(section <= 2){
			dotImage.setImageResource(ballResIds[section]);
		}else{
			dotImage.setImageResource(R.drawable.circle_gray);
		}
		return convertView;
	}

	public void clear() {
		if(mList != null){
			mList.clear();
			notifyDataSetChanged();
		}
	}

	public boolean isEditMode() {
		return mEditMode;
	}

	public ArrayList<UmiwiMyRecordBeans> getSelected() {
		ArrayList<UmiwiMyRecordBeans> selected = new ArrayList<UmiwiMyRecordBeans>();
		for (UmiwiMyRecordBeans key : selectionMap.keySet()) {
			if (selectionMap.get(key) != Boolean.FALSE) {
				selected.add(key);
			}
		}
		return selected;
	}
	
	private int getSelectionNumber(){
		int sum = 0;
		for(UmiwiMyRecordBeans key :selectionMap.keySet()){
			Boolean val = selectionMap.get(key);
			if(val == Boolean.TRUE){
				sum = sum + 1;
			}
		}
		return sum;
	}
}
