package com.umiwi.ui.managers;

import android.os.Handler;
import android.os.Looper;

import com.umiwi.ui.dao.VideoDao;
import com.umiwi.ui.download.VideoDownloadTask;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.model.VideoModel;
import com.umiwi.ui.model.VideoModel.DownloadStatus;
import com.umiwi.ui.util.SDCardManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.youmi.framework.manager.ModelManager;
import cn.youmi.framework.util.NetworkManager;
import cn.youmi.framework.util.SingletonFactory;
import cn.youmi.framework.util.ToastU;

public class VideoDownloadManager extends ModelManager<String, VideoModel> {

	private static Handler _handler = new Handler(Looper.getMainLooper());

	public interface DownloadStatusListener {
		void onDownloadStatusChange(VideoModel video, DownloadStatus ds, String msg);
		void onProgressChange(VideoModel video, int current, int total, int speed);
 	}
	
 	private ArrayList<VideoDownloadTask> mExecutingQueue = new ArrayList<VideoDownloadTask>();
 	private ArrayList<VideoModel> DownloadingList= new ArrayList<VideoModel>();
 	
	private HashSet<WeakReference<DownloadStatusListener>> mDownloadProgressListeners = new HashSet<WeakReference<VideoDownloadManager.DownloadStatusListener>>();

	synchronized public void registerDownloadStatusListener(DownloadStatusListener dl) {
		for(WeakReference<DownloadStatusListener> ref:mDownloadProgressListeners){
			if(ref.get() == dl){
				return;
			}
		}
		mDownloadProgressListeners
				.add(new WeakReference<VideoDownloadManager.DownloadStatusListener>(
						dl));
	}

	private Object mQueueLock = new Object();
 
	private static int THREAD_POOL_SIZE = 2;
	private ExecutorService mThreadPool = Executors
			.newFixedThreadPool(THREAD_POOL_SIZE);
	public VideoDownloadManager() {

 		ArrayList<VideoModel> videos = VideoManager.getInstance().getDownloadingVideos();
 		if(videos != null) {
 			setDownloadingList(videos);
 		}
	}
	
	public static VideoDownloadManager getInstance() {
		return SingletonFactory.getInstance(VideoDownloadManager.class);		
	}
	
	public void delete(VideoModel v) {
		v.setDownloadStatus(DownloadStatus.NOTIN);
		VideoManager.getInstance().saveVideo(v);
		DownloadingList.remove(v);
		for(VideoModel video:DownloadingList) {
			if(video.equals(v)) {
				v.setDownloadStatus(DownloadStatus.NOTIN);
				video.setDownloadStatus(DownloadStatus.NOTIN);	
				notifyStatusChange(video, DownloadStatus.NOTIN, "");
			}
		}
		
		for(VideoDownloadTask t:mExecutingQueue) {
			VideoModel video = t.getVideo();
			if(video.equals(v)) {
				v.setDownloadStatus(DownloadStatus.NOTIN);
				video.setDownloadStatus(DownloadStatus.NOTIN);	
				notifyStatusChange(video, DownloadStatus.NOTIN, "");
			}
		}
	}
	public void pause(final VideoModel v){
		v.setDownloadStatus(DownloadStatus.DOWNLOAD_PAUSE);
		v.setDownloadStatus(DownloadStatus.DOWNLOAD_PAUSE);	
		notifyStatusChange(v, DownloadStatus.DOWNLOAD_PAUSE, "");
		
		for(VideoDownloadTask t:mExecutingQueue){
			if(t.getVideo().equals(v)){
				t.getVideo().setDownloadStatus(DownloadStatus.DOWNLOAD_PAUSE);
				break;
			}
		}
//		for(VideoModel video:DownloadingList) {
//			if(video.equals(v)) {
//				
//			}
//		}
		if (NetworkManager.getInstance().isWifi()) {
			tryStartDownload();
		}
	}
	
	public void pauseAll() {
		for(VideoModel video:DownloadingList) {
 			video.setDownloadStatus(DownloadStatus.DOWNLOAD_PAUSE);	
			notifyStatusChange(video, DownloadStatus.DOWNLOAD_PAUSE, "");
		}
	}

	public boolean addDownload(VideoModel video) {
		synchronized (mQueueLock) {
			int index = DownloadingList.indexOf(video);
			
			if(!SDCardManager.isStorageAvailable(SDCardManager.getDownloadPath())) {
				video.setDownloadStatus(DownloadStatus.DOWNLOAD_ERROR);
 				notifyStatusChange(video, DownloadStatus.DOWNLOAD_ERROR, "当前存储路径没有准备好或者无法读取，请检查后再试");
			} else if(!SDCardManager.isSotrageSpaceEnough()) {
				video.setDownloadStatus(DownloadStatus.DOWNLOAD_ERROR);
 				notifyStatusChange(video, DownloadStatus.DOWNLOAD_ERROR, "当前存储空间不足，请更换存储卡");
			} else {
			}
			if(index>=0) {
				VideoModel v = DownloadingList.get(index);
				v.setDownloadStatus(DownloadStatus.DOWNLOAD_WAIT);
 				notifyStatusChange(v, DownloadStatus.DOWNLOAD_WAIT, "");
			} else {
				video.setDownloadStatus(DownloadStatus.DOWNLOAD_WAIT);
				DownloadingList.add(video);
 				notifyStatusChange(video, DownloadStatus.DOWNLOAD_WAIT, "");
			}

			if (NetworkManager.getInstance().isWifi()) {
				tryStartDownload();
			} else {
				showNoWifiToast("当前网络为非WIFI,连接WIFI后自动下载！");
//			final MsgDialog md = new MsgDialog();
//			md.setTitle("下载提示");
//			md.setMessage("当前网络是非WiFi状态，可能消耗你更多流量，是否继续？");
//			md.setNegativeButtonText("是");
//			md.setNegativeButtonListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					tryStartDownload();
//					md.dismissAllowingStateLoss();
//				}
//			});
//			md.setPositiveButtonText("否");
//			md.setPositiveButtonListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					md.dismissAllowingStateLoss();
//				}
//			});
//			md.show(ownerFragment.getChildFragmentManager().beginTransaction(), "dialog");
			}
		}

 		return true;
	}
	 
	// TODO
	public void tryStartDownload() {
		synchronized (mQueueLock) {
			 
			if (mExecutingQueue.size() < THREAD_POOL_SIZE ) {
				int i = THREAD_POOL_SIZE - mExecutingQueue.size() ;
				
				for(VideoModel video : DownloadingList) {
					if(video.getDownloadStatus() == DownloadStatus.DOWNLOAD_WAIT) {
						
						boolean hasVideoId =false;//是否有其他专辑的同一个视频在下载
						for(VideoDownloadTask vdt: mExecutingQueue) {
							if(vdt.getVideo().getVideoId()==video.getVideoId()) {
								hasVideoId = true;
								break;
							}
						}
						
						if(!hasVideoId)  {
							i = i-1;
							VideoDownloadTask t = new VideoDownloadTask(video);
 							mExecutingQueue.add(t);
							mThreadPool.execute(t);
							if(i<1) {
								break;
							}
						}
					}
				} 
			}
		}
	}
	 
	public void endDownload(VideoDownloadTask task) {
		synchronized (mQueueLock) {
			mExecutingQueue.remove(task);
			if(task.getVideo().getDownloadStatus() == DownloadStatus.DOWNLOAD_COMPLETE) {
				DownloadingList.remove(task.getVideo());
			}
		}
		if (NetworkManager.getInstance().isWifi()) {
			tryStartDownload();
		} else {
			showNoWifiToast("当前网络为非WIFI,请连接WIFI");
			
		}
	}

	synchronized public void notifyStatusChange(final VideoModel video,
			final DownloadStatus ds, final String msg) {
 			
 			VideoManager.getInstance().saveVideo(video);
	 		
			_handler.post(new Runnable() {
				@Override
				public void run() {
					synchronized (VideoDownloadManager.this) {
						HashSet<WeakReference<DownloadStatusListener>> tobeRemoved = null;
						
						for (WeakReference<DownloadStatusListener> sf : mDownloadProgressListeners) {
							DownloadStatusListener dl = sf.get();
							if (dl != null) {
								dl.onDownloadStatusChange(video, ds, msg);
							} else {
								if(tobeRemoved == null){
									tobeRemoved = new HashSet<WeakReference<DownloadStatusListener>>();
								}
								tobeRemoved.add(sf);
							}
						}
						
						if(tobeRemoved != null){
							mDownloadProgressListeners.removeAll(tobeRemoved);
						}
					}
				}
			});
			
			if(ds == DownloadStatus.DOWNLOAD_COMPLETE || ds == DownloadStatus.DOWNLOAD_ERROR){
				updateCallbackCache.remove(video);//we don't need it any more
			}
	}

	synchronized public void notifyProgressChange(VideoModel video, int current, int total, int speed) {
		if (mDownloadProgressListeners.isEmpty()) {// no one is listening so why
													// bother
			return;
		}
		ReportProgressRun run = updateCallbackCache.get(video);
		if (run == null) {
			run = new ReportProgressRun();
			updateCallbackCache.put(video, run);
		}
		run.video = video;
		run.current = current;
		run.total = total;
		run.speed = speed;
		_handler.post(run);
	}

	private ConcurrentHashMap<VideoModel, ReportProgressRun> updateCallbackCache = new ConcurrentHashMap<VideoModel, VideoDownloadManager.ReportProgressRun>();

	private class ReportProgressRun implements Runnable {
		int current;
		int total;
		int speed;
		VideoModel video;

		@Override
		 public void run() {
			synchronized(VideoDownloadManager.this) {
				HashSet<WeakReference<DownloadStatusListener>> tobeRemoved = null;
				for (WeakReference<DownloadStatusListener> sf : mDownloadProgressListeners) {
					DownloadStatusListener dl = sf.get();
					if (dl != null) {
						dl.onProgressChange(video, current, total, speed);
					} else {
						if(tobeRemoved == null){
							tobeRemoved = new HashSet<WeakReference<DownloadStatusListener>>();
						}
						tobeRemoved.add(sf);
					}
				}
				if(tobeRemoved != null){
					mDownloadProgressListeners.removeAll(tobeRemoved);
				}
			}
		}
	}
	
	public ArrayList<VideoModel> getDownloadingList() {
		return DownloadingList;
	}
	
	public void setDownloadingList(ArrayList<VideoModel> DownloadingList) {
		this.DownloadingList = DownloadingList;
		for(VideoModel video :DownloadingList) {
			if(video.getDownloadStatus() == DownloadStatus.DOWNLOAD_IN) {
				video.setDownloadStatus(DownloadStatus.DOWNLOAD_WAIT);
			}
 		}
		if (NetworkManager.getInstance().isWifi()) {
			tryStartDownload();
		} else {
			showNoWifiToast("当前网络为非WIFI,请连接WIFI！");
		}
	}
	
	/** 判断是否正在下载*/
	public Boolean hasDownloading() {
		boolean result = false;
		for(VideoModel video : DownloadingList) {
 			if(video.getDownloadStatus() == DownloadStatus.DOWNLOAD_IN || video.getDownloadStatus() == DownloadStatus.DOWNLOAD_ERROR) {
				result = true;
				break;
			}
		}
		return result;
 	}
	
	
	public void deleteDownloadedByAlbumId(String albumId) {
		ArrayList<VideoModel> videos = VideoManager.getInstance().getDownloadedListByAlbumId(albumId);
		for(VideoModel video :videos) {
			VideoManager.getInstance().setStatusByVideoId(video.getVideoId(), DownloadStatus.NOTIN);
			SDCardManager.deleteFile(video.getFilePath());
			VideoDao.getInstance().delete(video);//删除数据库列
		}
	}
	
	public void deleteDownloadingByAlbumId(String albumId) {
		ArrayList<VideoModel> videos = VideoManager.getInstance().getDownloadingListByAlbumId(albumId);
		synchronized (mQueueLock) {
			for(VideoModel video :videos) {
				delete(video);
				SDCardManager.deleteFile(video.getFilePath());
				VideoDao.getInstance().delete(video);//删除数据库列
			}
		}
	}
	
	public void deleteDownloadedByVideoId(String VideoId) {
		
		VideoModel video = VideoManager.getInstance().getVideoById(VideoId);
		VideoManager.getInstance().setStatusByVideoId(video.getVideoId(), DownloadStatus.NOTIN);
		SDCardManager.deleteFile(video.getFilePath());
		VideoDao.getInstance().delete(video);//删除数据库列
	}
	
	public void deleteDownloadingByVideoId(String VideoId) {
		ArrayList<VideoModel> videos = VideoManager.getInstance().getDownloadingListByVideoId(VideoId);
		synchronized (mQueueLock) {
			for(VideoModel video :videos) {
				delete(video);
				SDCardManager.deleteFile(video.getFilePath());
				VideoDao.getInstance().delete(video);//删除数据库列
			}	
		}
	}
	
	
	/**已经下载的文件大小*/
	public String getStorageDownloadedSize() {
		ArrayList<VideoModel> videoList = VideoManager.getInstance().getDownloadedList();
		long size =0;
		for(VideoModel video :videoList) {
			size = size+video.getFileSize();
		}
		return SDCardManager.convertStorage(size);
 	}
	
	private void showNoWifiToast(final String str) {
		Handler handler = new Handler(Looper.getMainLooper());

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				ToastU.showLong(UmiwiApplication.getContext(), str);
			}
		}, 0);

	}
	
}
