package com.umiwi.ui.download;

import android.os.Looper;
import android.widget.Toast;

import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.VideoDownloadManager;
import com.umiwi.ui.managers.VideoManager;
import com.umiwi.ui.model.VideoModel;
import com.umiwi.ui.model.VideoModel.DownloadStatus;
import com.umiwi.ui.util.CommonHelper;
import com.umiwi.ui.util.EncryptTools;
import com.umiwi.ui.util.SDCardManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class VideoDownloadTask implements Runnable {
	private VideoModel mVideo;

	public VideoDownloadTask(VideoModel v) {
		this.mVideo = v;
	}

	public void cancel(){
		
	}
	
	public VideoModel getVideo() {
		return mVideo;
	}

	private long downloadSize=0;// 已经下载大小
	private long totalSize=0;// 文件大小

	public final static int TIME_OUT = 30000;
	private final static int BUFFER_SIZE = 1024 * 8;
	@Override
	public void run() {
 		mVideo.setDownloadStatus(DownloadStatus.DOWNLOAD_IN);
		VideoDownloadManager.getInstance().notifyStatusChange(mVideo, DownloadStatus.DOWNLOAD_IN, "");
		long range = 0;
 		try {
			HttpURLConnection urlConnection = (HttpURLConnection) new URL(mVideo.getVideoUrl()).openConnection();
			
			if(mVideo.getFileName() == null ) {
				mVideo.setFileName(CommonHelper.encodeMD5(mVideo.getVideoId()));
			}
			if(mVideo.getExt() == null) {
				mVideo.setExt("mp4");
			}

			File file = null;	
 			urlConnection.setConnectTimeout(TIME_OUT);// 设置连接超时时间
 			
 			if(mVideo.getFilePath() == null){
 				mVideo.setFilePath(getFilePath());
 				VideoManager.getInstance().saveVideo(mVideo);
 			}
 			
 			if(mVideo.getFilePath() != null){
 				file = new File(mVideo.getFilePath());
 				range = file.length();
 				if (range > 0) {// 如果range>0)
 					urlConnection.addRequestProperty("RANGE", "bytes=" + range + "-");// 设置下载的字节范围
 				}
 			}
			
 			int readen = 0;
 			
 			int contentLenght = urlConnection.getContentLength();// 获得文件的大小
 			
			if (SDCardManager.getAvailableExternalMemorySize(SDCardManager.getDownloadPath())
					< ((long)contentLenght + (20 * 1024 * 1024L))) {
				VideoDownloadManager.getInstance().deleteDownloadingByAlbumId(mVideo.getAlbumId());
				Looper.prepare();//java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()
				Toast.makeText(UmiwiApplication.getContext(), "当前存储路径空间不足，请重新选择下载路径", Toast.LENGTH_LONG).show();
				Looper.loop();
				return;
			}
			if (contentLenght >= 0) {// 文件的大小不为0的话
				totalSize = range + contentLenght;
			} else {
				file.delete();
				notifyError();
				return;
			}
 
			FileOutputStream fos = new FileOutputStream(file, range > 0);
			InputStream is = urlConnection.getInputStream();
			try {
				byte[] buf = new byte[BUFFER_SIZE];
				downloadSize = range;// 文件大小
				int len = 0;
				readen = (int) range;
				int total = (int) totalSize;
				mVideo.setFileSize(total);

				long reported = 0;
				long atime 	= System.currentTimeMillis();
				long btime = System.currentTimeMillis();
				long predownloadsize = downloadSize+0;
				int speed = 0;
 				while ((len = is.read(buf)) >= 0) {
					if (mVideo.getDownloadStatus() != DownloadStatus.DOWNLOAD_IN) {
						VideoDownloadManager.getInstance().endDownload(this);
						return;
					}
					fos.write(buf, 0, len);
					downloadSize += len;// 文件的长度
					readen += len;
					if (reported > BUFFER_SIZE / 2) {						
						VideoDownloadManager.getInstance().notifyProgressChange(mVideo, readen, total, speed);
						reported = 0;
					}
					reported += len;
					
					btime = System.currentTimeMillis();
					if((btime-atime)>=1000) {
						speed = (int) (downloadSize - predownloadsize);
						predownloadsize = downloadSize+0;
						atime = System.currentTimeMillis();
					}
				}
				is.close();
 				if (downloadSize == totalSize) {
					if(range<totalSize) {
						EncryptTools.encriptVideoFile(mVideo, file);
					}
					mVideo.setDownloadStatus(DownloadStatus.DOWNLOAD_COMPLETE);
					VideoDownloadManager.getInstance().notifyStatusChange(
							mVideo, DownloadStatus.DOWNLOAD_COMPLETE, "");
					VideoDownloadManager.getInstance().endDownload(this);
				} else {
 					notifyError();
				}
			} catch (FileNotFoundException eFile) {
				file.deleteOnExit();
				eFile.printStackTrace();
 				notifyError();
			} catch (Exception e) {
				e.printStackTrace();
 				notifyError();
			} finally {
				is.close();
				fos.close();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
 			notifyError();
		} catch (IOException e) {
 			e.printStackTrace();
 			notifyError();
		}
	}

	private void notifyError() {
		mVideo.setFileName(null);
		mVideo.setDownloadStatus(DownloadStatus.DOWNLOAD_ERROR);
		VideoDownloadManager.getInstance().notifyStatusChange(mVideo,
				DownloadStatus.DOWNLOAD_ERROR, "");
		VideoDownloadManager.getInstance().endDownload(this);
	}
	
	private String getFilePath() {
		return new File(SDCardManager.getDownloadPath(), mVideo.getFileName() + ".download").getAbsolutePath();

	}
}
