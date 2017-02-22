package com.umiwi.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.VideoDownloadManager;
import com.umiwi.ui.managers.VideoDownloadManager.DownloadStatusListener;
import com.umiwi.ui.model.VideoModel;
import com.umiwi.ui.model.VideoModel.DownloadStatus;

public class DownloadListDialogAdapter extends BaseAdapter implements
		DownloadStatusListener {

	private List<VideoModel> videos = new ArrayList<VideoModel>();

	public DownloadListDialogAdapter() {
		VideoDownloadManager.getInstance().registerDownloadStatusListener(this);
	}

	public void setVideos(List<VideoModel> videos) {
		this.videos = videos;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return this.videos.size();
	}

	@Override
	public VideoModel getItem(int position) {
		return this.videos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean isEnabled(int position) {
		VideoModel video = getItem(position);
		return video.getDownloadStatus() == DownloadStatus.DOWNLOAD_PAUSE
				|| video.getDownloadStatus() == DownloadStatus.DOWNLOAD_ERROR
				|| video.getDownloadStatus() == DownloadStatus.NOTIN;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Holder holder = null;
		if (view == null) {
			view = LayoutInflater.from(UmiwiApplication.getInstance()).inflate(
					R.layout.item_download_dialog, null, false);
			holder = new Holder(view);
		} else {
			holder = (Holder) view.getTag();
		}

		VideoModel video = this.videos.get(position);
		
		String title = "";
 		int visibility =0;
		
		if(video.getVideoId() == null) {
			title = " ";
 			visibility = View.GONE;

 		} else {
			title = video.getTitle();
 			if ( video.getDownloadStatus() == DownloadStatus.NOTIN) {
				visibility = View.VISIBLE;
 				holder.downtitleTextview.setVisibility(View.GONE);
  				holder.titleTextView.setTextColor(Color.BLACK);

 			} else if(video.getDownloadStatus() == DownloadStatus.DOWNLOAD_COMPLETE) {
  				holder.titleTextView.setTextColor(Color.GRAY);

 				holder.downtitleTextview.setText("已下载");
 				holder.downtitleTextview.setTextColor(UmiwiApplication.getContext().getResources().getColor(R.color.color_primary));
 				holder.downtitleTextview.setVisibility(View.VISIBLE);
				visibility = View.GONE;
  			} else {
  				holder.titleTextView.setTextColor(Color.GRAY);
				visibility = View.GONE;
				holder.downtitleTextview.setText("下载中");
 				holder.downtitleTextview.setTextColor(Color.GRAY);
 				holder.downtitleTextview.setVisibility(View.VISIBLE);
  			}
		}
		
		holder.titleTextView.setText(title);
		holder.downloadImageview.setVisibility(visibility);
		return view;
	}

	private class Holder {
		public TextView titleTextView;
		private TextView downtitleTextview;
		public ImageView downloadImageview;

		public Holder(View view) {
			titleTextView = (TextView) view.findViewById(R.id.title_textview);
			downtitleTextview = (TextView) view.findViewById(R.id.downtitle_textview);
			downloadImageview = (ImageView) view.findViewById(R.id.download_imageview);
			view.setTag(this);
		}
	}

	@Override
	public void onDownloadStatusChange(VideoModel video, DownloadStatus ds, String msg) {
		int index = videos.indexOf(video);
		if (index >= 0) {

			videos.set(index, video);
			VideoModel v = videos.get(index);
			v.setDownloadStatus(ds);
			this.notifyDataSetChanged();
		}
	}

	@Override
	public void onProgressChange(VideoModel video, int current, int total, int speed) {

	}

	 
}
