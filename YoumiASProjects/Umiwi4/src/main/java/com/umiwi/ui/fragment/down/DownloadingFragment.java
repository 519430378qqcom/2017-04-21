package com.umiwi.ui.fragment.down;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.HomeMainActivity;
import com.umiwi.ui.adapter.DownloadingAdapter;
import com.umiwi.ui.adapter.DownloadingAdapter.ChildHolder;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.VideoDownloadManager;
import com.umiwi.ui.managers.VideoDownloadManager.DownloadStatusListener;
import com.umiwi.ui.model.VideoModel;
import com.umiwi.ui.model.VideoModel.DownloadStatus;
import com.umiwi.ui.util.SDCardManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.util.NetworkManager;
import cn.youmi.framework.util.NetworkManager.OnNetworkChangeListener;

public class DownloadingFragment extends BaseFragment implements
		OnClickListener, DownloadStatusListener, OnChildClickListener,
		OnItemLongClickListener, OnNetworkChangeListener {

	private ExpandableListView videosListView;
	private DownloadingAdapter mAdapter;
	private ArrayList<String> listDataHeader;
	private Map<String, ArrayList<VideoModel>> listDataChild;

	private TextView downloadsizeTextView;
	private TextView usedspacesizeTextView;
	private ImageView pauseallImageView;
	private ImageView startallImageView;
	private String downloadedSize = "";
	private String avaliableSize = "";
	
	ActionMode mMode;
	Menu menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		this.menu = menu;
		inflater.inflate(R.menu.toolbar_delete, menu);
//		menu.findItem(R.id.delete).setVisible(false);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.delete:
			trashClick();
			if(!mAdapter.isEmpty()){
				mMode = mActionBarToolbar.startActionMode(new DeleteCallback());
			}
			
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressLint("InflateParams") 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_downloading, null);
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, "下载管理");

		pauseallImageView = (ImageView) view.findViewById(R.id.pauseall_imageview);
		startallImageView = (ImageView) view.findViewById(R.id.startall_imageview);

		downloadsizeTextView = (TextView) view.findViewById(R.id.downloadsize_textview);
		usedspacesizeTextView = (TextView) view.findViewById(R.id.usedspacesize_textview);

		videosListView = (ExpandableListView) view.findViewById(R.id.videos_expandablelistView);
		
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, ArrayList<VideoModel>>();
		
		mAdapter = new DownloadingAdapter(getActivity(), listDataHeader,
				listDataChild);
		
		mAdapter.setExpandableListView(videosListView);
		videosListView.setAdapter(mAdapter);
		videosListView.setGroupIndicator(null);

		videosListView.setOnChildClickListener(this);
		videosListView.setOnItemLongClickListener(this);

		pauseallImageView.setOnClickListener(this);
		startallImageView.setOnClickListener(this);

		VideoDownloadManager.getInstance().registerDownloadStatusListener(this);
		NetworkManager.getInstance().addListener(this);
	
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		downloadedSize = VideoDownloadManager.getInstance()
				.getStorageDownloadedSize();
//		avaliableSize = VideoDownloadManager.getInstance().getSotrageAvailableSize();
		avaliableSize = SDCardManager.getStorageFree(SDCardManager.getDownloadPath());
		downloadsizeTextView.setText(downloadedSize);
		usedspacesizeTextView.setText(avaliableSize);

		update();
		MobclickAgent.onPageStart(fragmentName);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(fragmentName);
	}
	
	
	private class DeleteCallback implements ActionMode.Callback {
		@Override
		public boolean onCreateActionMode(ActionMode arg0, Menu menu) {
			arg0.setTitle("删除");
			MenuItem deleteItem = menu.add(0, 1, 2, "删除").setIcon( R.drawable.ic_delete);
			MenuItemCompat.setShowAsAction(deleteItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

			switch (item.getItemId()) {
			case 1:
				SparseBooleanArray checkedVideoids = mAdapter.getCheckedVideoIds();
				ArrayList<Integer> videoids = new ArrayList<Integer>();
				for (int i = 0; i < checkedVideoids.size(); i++) {
					int VideoId = checkedVideoids.keyAt(i);
					if(checkedVideoids.get(VideoId)) {
						videoids.add(VideoId);
					}
				}

				if (!videoids.isEmpty()) {
					for (int videoId : videoids) {
						VideoDownloadManager.getInstance()
								.deleteDownloadingByVideoId("" + videoId);
					}
					update();
					mAdapter.initCheckedVideoIds();
				}
				mode.finish();
				break;

			default:
				break;
			}
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode arg0) {
			trashClick();
		}
	}

	private void update() {
		ArrayList<VideoModel> videos = VideoDownloadManager.getInstance().getDownloadingList();// VideoManager.getInstance().getDownloadingVideos();
//		listDataHeader = new ArrayList<String>();
//		listDataChild = new HashMap<String, ArrayList<VideoModel>>();
		
		if(listDataHeader != null) {
			listDataHeader.clear();
		}
		
		if(listDataChild != null) {
			listDataChild.clear();
		}
		
		boolean pauseAll = true;
		for (VideoModel video : videos) {
			if (video.getDownloadStatus() != DownloadStatus.DOWNLOAD_PAUSE) {
				pauseAll = false;
			}
			String albumTitle = video.getAlbumTitle();
			if (!listDataHeader.contains(albumTitle)) {
				listDataHeader.add(albumTitle);
			}

			if (!listDataChild.containsKey(albumTitle)) {
				listDataChild.put(albumTitle, new ArrayList<VideoModel>());
			}

			Boolean hasin = false;
			for (VideoModel mVideo : listDataChild.get(albumTitle)) {

				
				
				if (mVideo.getVideoId().equals(video.getVideoId())) {
					hasin = true;
				}
			}

			if (hasin == false) {
				listDataChild.get(albumTitle).add(video);
			}
			
		}

		if (pauseAll) {
			startallImageView.setVisibility(View.VISIBLE);
			pauseallImageView.setVisibility(View.GONE);
		} else {
			startallImageView.setVisibility(View.GONE);
			pauseallImageView.setVisibility(View.VISIBLE);
		}

		mAdapter.setData(listDataHeader, listDataChild);
	}

	@Override
	public void onDownloadStatusChange(VideoModel video, DownloadStatus ds,
			String msg) {

		switch (ds) {
		case DOWNLOAD_COMPLETE:
			downloadedSize = VideoDownloadManager.getInstance()
					.getStorageDownloadedSize();
			avaliableSize = SDCardManager.getStorageFree(SDCardManager.getDownloadPath());
//			avaliableSize = VideoDownloadManager.getInstance().getSotrageAvailableSize();
			downloadsizeTextView.setText(downloadedSize);
			usedspacesizeTextView.setText(avaliableSize);
			update();
			break;
		case DOWNLOAD_IN:
			update();
			break;
		default:
			updateVideo(video);
			break;
		}
	}

	@Override
	public void onProgressChange(VideoModel video, int current, int total,
			int speed) {
		video.setDownloadedSize(current);
		video.setFileSize(total);
		video.setSpeed(speed);
		updateVideo(video);
	}

	private void updateVideo(VideoModel video) {
		for (int i = 0; i < videosListView.getChildCount(); i++) {
			View child = videosListView.getChildAt(i);
			if (child.getTag() != null
					&& (child.getTag() instanceof ChildHolder)) {
				ChildHolder holder = (ChildHolder) child.getTag();
				if (holder.video.getVideoId().equals(video.getVideoId())) {

					if (video.getFileSize() > 0) {
						mAdapter.updateView(child, video);
					} else {
						holder.percentageTextView.setText("正在获取");
					}
				}
			}
		}
	}

	@Override
	public boolean onChildClick(ExpandableListView expandablelistview,
			View clickedView, int groupPosition, int childPosition, long childId) {
		// TODO Auto-generated method stub

		VideoModel v = (VideoModel) mAdapter.getChild(groupPosition,
				childPosition);

		if (mAdapter.getTrashStatus()) {
			ChildHolder holder = (ChildHolder) clickedView.getTag();
			holder.itemCheckBox.performClick();
		} else {

			switch (v.getDownloadStatus()) {
			case DOWNLOAD_ERROR:
				VideoDownloadManager.getInstance().addDownload(v);
				break;
			case DOWNLOAD_IN:
				VideoDownloadManager.getInstance().pause(v);
				break;
			case DOWNLOAD_PAUSE:
				VideoDownloadManager.getInstance().addDownload(v);
				break;
			case DOWNLOAD_WAIT:
				VideoDownloadManager.getInstance().pause(v);
				break;
			default:
				break;
			}
			update();

		}
		return false;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, final View view,
			int arg2, long arg3) {

		if (view.getId() == R.id.groupview_relativelayout) {
			return false;
		}

//		final ChildHolder holder = (ChildHolder) view.getTag();
//		String titletext = (String) holder.titleTextView.getText();
//		final MsgDialog dialog = new MsgDialog();
//		dialog.setTitle("提示");
//		dialog.setMessage("确认要删除吗？");
//		dialog.setTitle(titletext);
//		dialog.setPositiveButtonText("删除");
//		dialog.setPositiveButtonListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				VideoDownloadManager.getInstance().deleteDownloadingByVideoId(
//						holder.video.getVideoId());
//				update();
//				dialog.dismissAllowingStateLoss();
//			}
//		});
//		dialog.show(getChildFragmentManager(), "dialog");
		trashClick();
		mMode = mActionBarToolbar.startActionMode(new DeleteCallback());
		return false;
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.pauseall_imageview) {
			ArrayList<VideoModel> videos = VideoDownloadManager.getInstance()
					.getDownloadingList();
			for (VideoModel video : videos) {
				VideoDownloadManager.getInstance().pause(video);
			}
			update();
		} else if (v.getId() == R.id.startall_imageview) {
			ArrayList<VideoModel> videos = VideoDownloadManager.getInstance()
					.getDownloadingList();
			for (VideoModel video : videos) {
				VideoDownloadManager.getInstance().addDownload(video);
			}
			update();
		}
	}

	public void trashClick() {
		mAdapter.toggleTrashStatus();
		mAdapter.notifyDataSetChanged();
	}

	
	@Override
	public void onNetworkChange() {
		if (NetworkManager.getInstance().isWifi()) {
			ArrayList<VideoModel> videos = VideoDownloadManager.getInstance().getDownloadingList();
			
			for (VideoModel video : videos) {
				VideoDownloadManager.getInstance().addDownload(video);
			}
			update();
			
//			VideoDownloadManager.getInstance().tryStartDownload();
		} else {

			if (!VideoDownloadManager.getInstance().hasDownloading()) {
				return;
			}

			VideoDownloadManager.getInstance().pauseAll();


			// 设置通知的事件消息
			CharSequence contentTitle = "视频暂停下载！"; // 通知栏标题
			CharSequence contentText = "当前网络为非WIFI,连接WIFI后自动下载"; // 通知栏内容

			Intent notificationIntent = new Intent(getActivity().getApplication(), HomeMainActivity.class); // 点击该通知后要跳转的Activity

			Bitmap largeIcon = BitmapFactory.decodeResource(UmiwiApplication.getInstance().getResources(), R.drawable.ic_launcher);
			PendingIntent pendingIntent = PendingIntent.getActivity(UmiwiApplication.getContext(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
			Notification notification = new NotificationCompat.Builder(
					UmiwiApplication.getContext()).setAutoCancel(true)
					.setContentTitle(contentTitle).setContentText(contentText)
					.setDefaults(Notification.DEFAULT_ALL)
							// 设置使用所有默认值（声音、震动、闪屏等）
					.setLargeIcon(largeIcon).setContentIntent(pendingIntent)
					.setSmallIcon(R.drawable.ic_launcher)// 删除就显示不了了
					.setWhen(System.currentTimeMillis())
							// .setNumber(mNewNum)
					.build();

			NotificationManager mNotificationManager = (NotificationManager) UmiwiApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify(0, notification);
		}
	}
}
