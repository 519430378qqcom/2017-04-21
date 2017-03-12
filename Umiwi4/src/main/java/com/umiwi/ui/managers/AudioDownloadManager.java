package com.umiwi.ui.managers;

import android.os.Handler;
import android.os.Looper;

import com.umiwi.ui.dao.AudioDao;
import com.umiwi.ui.download.AudioDownloadTask;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.model.AudioModel;
import com.umiwi.ui.model.AudioModel.DownloadStatus;
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

public class AudioDownloadManager extends ModelManager<String, AudioModel> {

	private static Handler _handler = new Handler(Looper.getMainLooper());

	public interface DownloadStatusListener {
		void onDownloadStatusChange(AudioModel audio, DownloadStatus ds, String msg);
		void onProgressChange(AudioModel audio, int current, int total, int speed);
 	}

 	private ArrayList<AudioDownloadTask> mExecutingQueue = new ArrayList<AudioDownloadTask>();
 	private ArrayList<AudioModel> DownloadingList= new ArrayList<AudioModel>();

	private HashSet<WeakReference<DownloadStatusListener>> mDownloadProgressListeners = new HashSet<WeakReference<AudioDownloadManager.DownloadStatusListener>>();

	synchronized public void registerDownloadStatusListener(DownloadStatusListener dl) {
		for(WeakReference<DownloadStatusListener> ref:mDownloadProgressListeners){
			if(ref.get() == dl){
				return;
			}
		}
		mDownloadProgressListeners
				.add(new WeakReference<AudioDownloadManager.DownloadStatusListener>(
						dl));
	}

	private Object mQueueLock = new Object();

	private static int THREAD_POOL_SIZE = 2;
	private ExecutorService mThreadPool = Executors
			.newFixedThreadPool(THREAD_POOL_SIZE);
	public AudioDownloadManager() {

 		ArrayList<AudioModel> audios = AudioManager.getInstance().getDownloadingVideos();
 		if(audios != null) {
 			setDownloadingList(audios);
 		}
	}

	public static AudioDownloadManager getInstance() {
		return SingletonFactory.getInstance(AudioDownloadManager.class);
	}

	public void delete(AudioModel v) {
		v.setDownloadStatus(DownloadStatus.NOTIN);
		AudioManager.getInstance().saveVideo(v);
		DownloadingList.remove(v);
		for(AudioModel audio:DownloadingList) {
			if(audio.equals(v)) {
				v.setDownloadStatus(DownloadStatus.NOTIN);
				audio.setDownloadStatus(DownloadStatus.NOTIN);
				notifyStatusChange(audio, DownloadStatus.NOTIN, "");
			}
		}

		for(AudioDownloadTask t:mExecutingQueue) {
			AudioModel audio = t.getAudio();
			if(audio.equals(v)) {
				v.setDownloadStatus(DownloadStatus.NOTIN);
				audio.setDownloadStatus(DownloadStatus.NOTIN);
				notifyStatusChange(audio, DownloadStatus.NOTIN, "");
			}
		}
	}
	public void pause(final AudioModel v){
		v.setDownloadStatus(DownloadStatus.DOWNLOAD_PAUSE);
		v.setDownloadStatus(DownloadStatus.DOWNLOAD_PAUSE);
		notifyStatusChange(v, DownloadStatus.DOWNLOAD_PAUSE, "");

		for(AudioDownloadTask t:mExecutingQueue){
			if(t.getAudio().equals(v)){
				t.getAudio().setDownloadStatus(DownloadStatus.DOWNLOAD_PAUSE);
				break;
			}
		}
//		for(AudioModel audio:DownloadingList) {
//			if(audio.equals(v)) {
//
//			}
//		}
		if (NetworkManager.getInstance().isWifi()) {
			tryStartDownload();
		}
	}

	public void pauseAll() {
		for(AudioModel audio:DownloadingList) {
 			audio.setDownloadStatus(DownloadStatus.DOWNLOAD_PAUSE);
			notifyStatusChange(audio, DownloadStatus.DOWNLOAD_PAUSE, "");
		}
	}

	public boolean addDownload(AudioModel audio) {
		synchronized (mQueueLock) {
			int index = DownloadingList.indexOf(audio);

			if(!SDCardManager.isStorageAvailable(SDCardManager.getDownloadPath())) {
				audio.setDownloadStatus(DownloadStatus.DOWNLOAD_ERROR);
 				notifyStatusChange(audio, DownloadStatus.DOWNLOAD_ERROR, "当前存储路径没有准备好或者无法读取，请检查后再试");
			} else if(!SDCardManager.isSotrageSpaceEnough()) {
				audio.setDownloadStatus(DownloadStatus.DOWNLOAD_ERROR);
 				notifyStatusChange(audio, DownloadStatus.DOWNLOAD_ERROR, "当前存储空间不足，请更换存储卡");
			} else {
			}
			if(index>=0) {
				AudioModel v = DownloadingList.get(index);
				v.setDownloadStatus(DownloadStatus.DOWNLOAD_WAIT);
 				notifyStatusChange(v, DownloadStatus.DOWNLOAD_WAIT, "");
			} else {
				audio.setDownloadStatus(DownloadStatus.DOWNLOAD_WAIT);
				DownloadingList.add(audio);
 				notifyStatusChange(audio, DownloadStatus.DOWNLOAD_WAIT, "");
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

				for(AudioModel audio : DownloadingList) {
					if(audio.getDownloadStatus() == DownloadStatus.DOWNLOAD_WAIT) {

						boolean hasVideoId =false;//是否有其他专辑的同一个视频在下载
						for(AudioDownloadTask vdt: mExecutingQueue) {
							if(vdt.getAudio().getVideoId()==audio.getVideoId()) {
								hasVideoId = true;
								break;
							}
						}

						if(!hasVideoId)  {
							i = i-1;
							AudioDownloadTask t = new AudioDownloadTask(audio);
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

	public void endDownload(AudioDownloadTask task) {
		synchronized (mQueueLock) {
			mExecutingQueue.remove(task);
			if(task.getAudio().getDownloadStatus() == DownloadStatus.DOWNLOAD_COMPLETE) {
				DownloadingList.remove(task.getAudio());
			}
		}
		if (NetworkManager.getInstance().isWifi()) {
			tryStartDownload();
		} else {
			showNoWifiToast("当前网络为非WIFI,请连接WIFI");

		}
	}

	synchronized public void notifyStatusChange(final AudioModel audio,
			final DownloadStatus ds, final String msg) {

 			AudioManager.getInstance().saveVideo(audio);

			_handler.post(new Runnable() {
				@Override
				public void run() {
					synchronized (AudioDownloadManager.this) {
						HashSet<WeakReference<DownloadStatusListener>> tobeRemoved = null;

						for (WeakReference<DownloadStatusListener> sf : mDownloadProgressListeners) {
							DownloadStatusListener dl = sf.get();
							if (dl != null) {
								dl.onDownloadStatusChange(audio, ds, msg);
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
				updateCallbackCache.remove(audio);//we don't need it any more
			}
	}

	synchronized public void notifyProgressChange(AudioModel audio, int current, int total, int speed) {
		if (mDownloadProgressListeners.isEmpty()) {// no one is listening so why
													// bother
			return;
		}
		ReportProgressRun run = updateCallbackCache.get(audio);
		if (run == null) {
			run = new ReportProgressRun();
			updateCallbackCache.put(audio, run);
		}
		run.audio = audio;
		run.current = current;
		run.total = total;
		run.speed = speed;
		_handler.post(run);
	}

	private ConcurrentHashMap<AudioModel, ReportProgressRun> updateCallbackCache = new ConcurrentHashMap<AudioModel, AudioDownloadManager.ReportProgressRun>();

	private class ReportProgressRun implements Runnable {
		int current;
		int total;
		int speed;
		AudioModel audio;

		@Override
		 public void run() {
			synchronized(AudioDownloadManager.this) {
				HashSet<WeakReference<DownloadStatusListener>> tobeRemoved = null;
				for (WeakReference<DownloadStatusListener> sf : mDownloadProgressListeners) {
					DownloadStatusListener dl = sf.get();
					if (dl != null) {
						dl.onProgressChange(audio, current, total, speed);
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
	
	public ArrayList<AudioModel> getDownloadingList() {
		return DownloadingList;
	}
	
	public void setDownloadingList(ArrayList<AudioModel> DownloadingList) {
		this.DownloadingList = DownloadingList;
		for(AudioModel audio :DownloadingList) {
			if(audio.getDownloadStatus() == DownloadStatus.DOWNLOAD_IN) {
				audio.setDownloadStatus(DownloadStatus.DOWNLOAD_WAIT);
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
		for(AudioModel audio : DownloadingList) {
 			if(audio.getDownloadStatus() == DownloadStatus.DOWNLOAD_IN || audio.getDownloadStatus() == DownloadStatus.DOWNLOAD_ERROR) {
				result = true;
				break;
			}
		}
		return result;
 	}
	
	
	public void deleteDownloadedByAlbumId(String albumId) {
		ArrayList<AudioModel> audios = AudioManager.getInstance().getDownloadedListByAlbumId(albumId);
		for(AudioModel audio :audios) {
			AudioManager.getInstance().setStatusByVideoId(audio.getVideoId(), DownloadStatus.NOTIN);
			SDCardManager.deleteFile(audio.getFilePath());
			AudioDao.getInstance().delete(audio);//删除数据库列
		}
	}
	
	public void deleteDownloadingByAlbumId(String albumId) {
		ArrayList<AudioModel> audios = AudioManager.getInstance().getDownloadingListByAlbumId(albumId);
		synchronized (mQueueLock) {
			for(AudioModel audio :audios) {
				delete(audio);
				SDCardManager.deleteFile(audio.getFilePath());
				AudioDao.getInstance().delete(audio);//删除数据库列
			}
		}
	}
	
	public void deleteDownloadedByVideoId(String AudioId) {
		
		AudioModel audio = AudioManager.getInstance().getVideoById(AudioId);
		AudioManager.getInstance().setStatusByVideoId(audio.getVideoId(), DownloadStatus.NOTIN);
		SDCardManager.deleteFile(audio.getFilePath());
		AudioDao.getInstance().delete(audio);//删除数据库列
	}
	
	public void deleteDownloadingByVideoId(String AudioId) {
		ArrayList<AudioModel> audios = AudioManager.getInstance().getDownloadingListByVideoId(AudioId);
		synchronized (mQueueLock) {
			for(AudioModel audio :audios) {
				delete(audio);
				SDCardManager.deleteFile(audio.getFilePath());
				AudioDao.getInstance().delete(audio);//删除数据库列
			}	
		}
	}
	
	
	/**已经下载的文件大小*/
	public String getStorageDownloadedSize() {
		ArrayList<AudioModel> videoList = AudioManager.getInstance().getDownloadedList();
		long size =0;
		for(AudioModel audio :videoList) {
			size = size+audio.getFileSize();
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
