package com.umiwi.ui.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.TextView;
import cn.youmi.framework.util.DimensionUtil;

import com.umiwi.ui.R;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.model.VideoModel;
import com.umiwi.ui.model.VideoModel.DownloadStatus;
import com.umiwi.ui.view.CirclePercentageView;

public class DownloadingAdapter extends BaseExpandableListAdapter {
	private Context context;
	private ArrayList<String> listDataHeader;
	private Map<String, ArrayList<VideoModel>> listDataChild;
	private Boolean trashStatus = false;
	private SparseBooleanArray checkedVideoIds =  new SparseBooleanArray();
	private ExpandableListView expandableListView;
	public void setData(ArrayList<String> listDataHeader,
			Map<String, ArrayList<VideoModel>> listDataChild) {
		this.listDataChild = listDataChild;
		this.listDataHeader = listDataHeader;
		this.notifyDataSetChanged();
	}

	public void setListDataChild(
			Map<String, ArrayList<VideoModel>> listDataChild) {
		this.listDataChild = listDataChild;
		this.notifyDataSetChanged();
	}

	public void setListDataHeader(ArrayList<String> listDataHeader) {
		this.listDataHeader = listDataHeader;
		this.notifyDataSetChanged();
	}
	
	public void setExpandableListView(ExpandableListView expandableListView) {
		this.expandableListView = expandableListView;
	}

	public ArrayList<String> getListDataHeader() {
		return this.listDataHeader;
	}

	public Map<String, ArrayList<VideoModel>> getListDataChild() {
		return this.listDataChild;
	}
	
	public Boolean getTrashStatus() {
		return trashStatus;
	}

	public void toggleTrashStatus() {
		initCheckedVideoIds();
		trashStatus = trashStatus ? false : true;
	}

	public DownloadingAdapter(Context context,
			ArrayList<String> listDataHeader,
			Map<String, ArrayList<VideoModel>> listDataChild) {
		super();
		this.context = context;
		this.listDataHeader = listDataHeader;
		this.listDataChild = listDataChild;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		VideoModel video = this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(
				childPosititon);
		return video;
	}

	public ArrayList<VideoModel> getChilds(int groupPosition) {
		return this.listDataChild.get(this.listDataHeader.get(groupPosition));
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	DecimalFormat df = new DecimalFormat("0.00");
	
	public void updateView(View convertView,VideoModel video){
		final String title = video.getTitle();
		final String videoid = video.getVideoId();
		
		
		ChildHolder holder = (ChildHolder)convertView.getTag();
		
		double p = video.getDownloadedSize() * 1.0
				/ video.getFileSize() * 100;
		double totalSize = (video.getFileSize() / (1024.0 * 1024));
		double downloadSize = (video.getDownloadedSize() / (1024.0 * 1024));

		holder.percentageView.drawLine = false;
		holder.percentageView.setPercentage((int)p);
		holder.percentageView.setTextSize(DimensionUtil.dip2px(10));
		holder.video = video;

		if(video.getDownloadStatus() == DownloadStatus.DOWNLOAD_ERROR) {
			holder.percentageView.setImage(R.drawable.download_pause);
			holder.percentagetipTextView.setText("点击下载");
		} else if (video.getDownloadStatus() == DownloadStatus.DOWNLOAD_PAUSE) {
			holder.percentageView.setImage(R.drawable.download_pause);
			holder.percentagetipTextView.setText("暂停中");
		} else if(video.getDownloadStatus() == DownloadStatus.DOWNLOAD_IN) {
			holder.percentageView.setImage(null);
			String speedtext = "0 KB/s";
			float speed = video.getSpeed();
			if(speed>=(1000*1024)) {
				speedtext  =df.format(speed/(1024*1024))+" M/s";
			} else {
				speedtext  = ((int)(speed/(1024)))+" KB/s";
			}
			holder.percentagetipTextView.setText(speedtext);
		} else if(video.getDownloadStatus() == DownloadStatus.DOWNLOAD_WAIT) {
			holder.percentageView.setImage(null);
			holder.percentagetipTextView.setText("等待中");
		}
		
		holder.percentageTextView.setText(df.format(downloadSize) + "M / "
				+ df.format(totalSize) + "M");
		
		holder.percentageView.setTextColor(UmiwiApplication.getInstance().getResources().getColor(R.color.umiwi_gray_9));

		convertView.setTag(R.id.percentage_textview, video.getAlbumId());
		holder.itemCheckBox.setTag(videoid);
		if(getTrashStatus()) {
			holder.itemCheckBox.setVisibility(View.VISIBLE);
		} else {
			holder.itemCheckBox.setChecked(false);
			holder.itemCheckBox.setVisibility(View.GONE);
		}
		
		holder.itemCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
				String videoId = (String) v.getTag();
				setClicked(videoId);
			}
		});
		
		if (videoid != null) {
			holder.titleTextView.setText(title);
		}
		
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if(convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.downloading_item, null);
			new ChildHolder(convertView);
		}
		VideoModel video = (VideoModel) getChild(groupPosition, childPosition);
		updateView(convertView,video);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.listDataChild.get(
				listDataHeader.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return listDataHeader.size();
	}

	@Override
	public long getGroupId(int position) {
		return position;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		String title = this.listDataHeader.get(groupPosition);
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(
					R.layout.downloading_item_group, null);
			new GroupHolder(convertView);
		}

		GroupHolder holder = (GroupHolder) convertView.getTag();
 		holder.grouptitleTextView.setText(title);

		ArrayList<VideoModel> videolist = this.listDataChild.get(listDataHeader.get(groupPosition));
		int i = 0;
		for (VideoModel video : videolist) {
			if (video.getVideoId() == null) {

			} else {
				i = i + 1;
			}
		}
		
		if(!isExpanded) {
			this.expandableListView.expandGroup(groupPosition);
		}
  
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public class GroupHolder {
		public TextView grouptitleTextView;

		public GroupHolder(View v) {
 			grouptitleTextView = (TextView) v.findViewById(R.id.grouptitle_textview);
 
			v.setTag(this);
		}
	}
	
	public class ChildHolder {
		public TextView percentageTextView;
		public CirclePercentageView percentageView;
		public TextView percentagetipTextView;
		public TextView titleTextView;
		public CheckBox itemCheckBox;
		public VideoModel video;
		

		public ChildHolder(View v) {
			percentageTextView = (TextView) v.findViewById(R.id.percentage_textview);
			percentageView = (CirclePercentageView) v.findViewById(R.id.percentage_view);
			percentagetipTextView = (TextView) v.findViewById(R.id.percentage_tip_textview);
			titleTextView = (TextView) v.findViewById(R.id.title_textview);
			itemCheckBox = (CheckBox) v.findViewById(R.id.item_checkbox);
			v.setTag(this);
		}
	}

	
	
	
	public void setClicked(String videoid) {
		int videoId = Integer.parseInt(videoid);
		boolean oldState = checkedVideoIds.get(videoId);
		// if in the group there was not any child checked
		if(oldState == true) {
			checkedVideoIds.put(videoId, false);
		} else {
			checkedVideoIds.put(videoId, true);
		}
		
	} 
	
	public SparseBooleanArray getCheckedVideoIds() {
		return checkedVideoIds;
	}
	
	public void initCheckedVideoIds() {
		checkedVideoIds.clear();
		if(listDataChild != null && getGroupCount() > 0) {
			for(int i = 0; i<getGroupCount(); i++ ) {
				if(getChilds(i) != null) {
					for(int j=0; j<getChilds(i).size(); j++) {
						VideoModel videoModel = (VideoModel) getChild(i, j);
						int albumId = Integer.parseInt(videoModel.getAlbumId());
						checkedVideoIds.put(albumId, false);
						
					}
				
				}
				
			}
		}
		
		notifyDataSetChanged();
	}
	 
}
