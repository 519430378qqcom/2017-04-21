package com.umiwi.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.AudioDownloadManager;
import com.umiwi.ui.managers.AudioDownloadManager.DownloadStatusListener1;
import com.umiwi.ui.model.AudioModel;
import com.umiwi.ui.model.AudioModel.DownloadStatus1;

import java.util.ArrayList;
import java.util.List;

public class DownloadAudioListDialogAdapter1 extends BaseAdapter implements
		DownloadStatusListener1 {

	private List<AudioModel> audios = new ArrayList<AudioModel>();

	public DownloadAudioListDialogAdapter1() {
		AudioDownloadManager.getInstance().registerDownloadStatusListener(this);
	}

	public void setVideos(List<AudioModel> audios) {
		this.audios = audios;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return this.audios.size();
	}

	@Override
	public AudioModel getItem(int position) {
		return this.audios.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean isEnabled(int position) {
		AudioModel audio = getItem(position);
		return audio.getDownloadStatus() == DownloadStatus1.DOWNLOAD_PAUSE
				|| audio.getDownloadStatus() == DownloadStatus1.DOWNLOAD_ERROR
				|| audio.getDownloadStatus() == DownloadStatus1.NOTIN;
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

		AudioModel audio = this.audios.get(position);
		
		String title = "";
 		int visibility =0;
		
		if(audio.getVideoId() == null) {
			title = " ";
 			visibility = View.GONE;

 		} else {
			title = audio.getTitle();

 			if ( audio.getDownloadStatus() == DownloadStatus1.NOTIN) {
				visibility = View.VISIBLE;
 				holder.downtitleTextview.setVisibility(View.GONE);
  				holder.titleTextView.setTextColor(Color.BLACK);

 			} else if(audio.getDownloadStatus() == AudioModel.DownloadStatus1.DOWNLOAD_COMPLETE) {
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
	public void onDownloadStatusChange(AudioModel audio, DownloadStatus1 ds, String msg) {
		int index = audios.indexOf(audio);
		if (index >= 0) {

			audios.set(index, audio);
			AudioModel v = audios.get(index);
			v.setDownloadStatus1(ds);
			this.notifyDataSetChanged();
		}
	}

	@Override
	public void onAudioProgressChange(AudioModel audio, int current, int total, int speed) {

	}



	 
}
