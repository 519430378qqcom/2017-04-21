package com.umiwi.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.model.AlbumModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.youmi.framework.util.ImageLoader;

/**
 * @Author xiaobo
 * @Version 2014年6月12日下午2:44:00
 */
public class DownloadedListAdapter extends BaseAdapter {
	
	private Context context;
	private Boolean trashStatus = false;
	private SparseBooleanArray checkedAlbumIds = new SparseBooleanArray();
	private ArrayList<AlbumModel> albums;
 	private float speed =0;
 	private int downloadNum = 0 ;
 	private boolean pauseAll = false;
 	
 	
	public void setAlbums(ArrayList<AlbumModel> albums) {
		this.albums = albums;
		this.notifyDataSetChanged();
	}
	
	public ArrayList<AlbumModel> getAlbums() {
		
		return this.albums;
	}
	

	public Boolean  getTrashStatus() {
		return trashStatus;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public void setDownloadNum(int downloadNum) {
		this.downloadNum = downloadNum;
	}
	
	public void setPauseAll(boolean pauseAll) {
		this.pauseAll = pauseAll;
	}

	public void toggleTrashStatus() {
		initCheckedAlbumIds();
		trashStatus = trashStatus ? false : true;
	}

	public DownloadedListAdapter(Context context,ArrayList<AlbumModel> albums) {
		super();
		this.context = context;
		this.albums = albums;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return albums.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return albums.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup arg2) {
		AlbumModel album = (AlbumModel) getItem(position);
		
		if(album.getAlbumId() == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = layoutInflater.inflate(R.layout.fragment_downloaded_list_item_first, null);
			HolderFirst holder = new HolderFirst(v, position);
			
			CheckBox albumCheckBox = (CheckBox) v.findViewById(R.id.album_checkbox);
			if (trashStatus) {
				albumCheckBox.setVisibility(View.INVISIBLE);
				albumCheckBox.setChecked(false);

			} else {
				albumCheckBox.setVisibility(View.GONE);
			} 
			
			
			holder.albumTitleTextView.setText("正在下载");
			
			DecimalFormat df = new DecimalFormat("0.00");
			String speedtext  = "";
			if(speed >(1024*1024)) {
				speedtext = df.format(speed/(1024*1024))+" M/s";
			} else {
				speedtext = df.format(speed/(1024))+" KB/s";
			}
			
			if(pauseAll == true) {
				holder.speedTextView.setText("已暂停");
			} else {
				holder.speedTextView.setText(speedtext);
			}
			holder.videocountTextView.setText("正在下载("+downloadNum+")");
			
		} else {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = layoutInflater.inflate(R.layout.fragment_downloaded_list_item, null);
			Holder holder = getHolder(v);
			
			if (trashStatus) {
				holder.albumCheckBox.setVisibility(View.VISIBLE);
				holder.albumCheckBox.setChecked(false);
			} else {
				holder.albumCheckBox.setVisibility(View.GONE);
			}
			
			holder.albumCheckBox.setTag(album.getAlbumId());
			
			holder.albumCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton v, boolean isChecked) {
					String albumId = (String) v.getTag();
					setClicked(albumId);
				}
			});

			
			holder.albumtitleTextView.setText(album.getTitle());
			DecimalFormat df = new DecimalFormat("0.00");

			holder.filesizeTextView.setText(df.format((album.getDownloadFilesize()/(1024*1024))) + "MB");
		//	System.out.println("TMD===下载数量"+album.getDownloadVideoCount()+"");
			if (album.getUrl().contains("mp3")) {
				holder.videocountTextView.setText(album.getDownLoadAudioCount() + "");

			} else {
				holder.videocountTextView.setText(album.getDownloadVideoCount() + "");
			}

			
			
			if(album.isWatched()) {
				holder.albumNewIconImageView.setVisibility(View.GONE);
			} else {
				holder.albumNewIconImageView.setVisibility(View.VISIBLE);
			}
			if (!TextUtils.isEmpty(album.getImageURL())) {
				ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
				mImageLoader.loadImage(album.getImageURL(), holder.albumImageView);
			}

		} 
		
//		v.setTag(R.id.trashconfirm_textview, album.getAlbumId());
		
		return v;
	}
	
	private class Holder {

		public ImageView albumImageView;
		public ImageView albumNewIconImageView;
		public TextView albumtitleTextView;
		public TextView filesizeTextView;
		public TextView videocountTextView;
		public CheckBox albumCheckBox;


		public Holder(View v) {
			albumImageView = (ImageView) v.findViewById(R.id.album_imageview);
			albumtitleTextView = (TextView) v.findViewById(R.id.albumtitle_textview);
			filesizeTextView = (TextView) v.findViewById(R.id.filesize_textview);
			videocountTextView = (TextView) v.findViewById(R.id.videocount_textview);
			albumNewIconImageView = (ImageView)v.findViewById(R.id.album_newicon);
			albumCheckBox = (CheckBox) v.findViewById(R.id.album_checkbox);
		}
	}
	
	private Holder getHolder(final View view) {
		Holder holder = (Holder) view.getTag();
		if (holder == null) {
			holder = new Holder(view);
			view.setTag(holder);
		}
		return holder;
	}
	
	public class HolderFirst {
		public TextView speedTextView;
		public TextView albumTitleTextView;
		public TextView videocountTextView;
		
		public HolderFirst(View v, int position) {
			speedTextView = (TextView)v.findViewById(R.id.speed_textview);
			albumTitleTextView = (TextView)v.findViewById(R.id.albumtitle_textview);
			videocountTextView = (TextView)v.findViewById(R.id.videocount_textview);
			v.setTag(this);
		}
		
	} 

	public void setClicked(String albumid) {
		
		if(albumid == null) {
			return ;
		}
		int albumId = Integer.parseInt(albumid);
		boolean oldState = checkedAlbumIds.get(albumId);
		
		// if in the group there was not any child checked
		if (oldState == true) {
			checkedAlbumIds.put(albumId, false);
		} else {
			checkedAlbumIds.put(albumId, true);
		}
		
	}

	public SparseBooleanArray getCheckedAlbumIds() {
		return checkedAlbumIds;
	}

	public void initCheckedAlbumIds() {
		checkedAlbumIds.clear();
		
		if(albums != null && albums.size() > 0) {
			for(int i = 0; i<albums.size(); i++ ) {
				if(!TextUtils.isEmpty((albums.get(i)).getAlbumId())) {
					int albumId = Integer.parseInt((albums.get(i)).getAlbumId());
					checkedAlbumIds.put(albumId, false);
				}
			}
		}
		
		notifyDataSetChanged();
	}

}
