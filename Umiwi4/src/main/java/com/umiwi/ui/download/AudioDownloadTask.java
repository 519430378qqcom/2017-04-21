package com.umiwi.ui.download;

import android.os.Looper;
import android.widget.Toast;

import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.AudioDownloadManager;
import com.umiwi.ui.managers.AudioManager;
import com.umiwi.ui.model.AudioModel;
import com.umiwi.ui.model.AudioModel.DownloadStatus1;
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

public class AudioDownloadTask implements Runnable {
	private AudioModel mAudio;

	public AudioDownloadTask(AudioModel v) {
		this.mAudio = v;
	}

	public void cancel(){
		
	}
	
	public AudioModel getAudio() {
		return mAudio;
	}

	private long downloadSize=0;// 已经下载大小
	private long totalSize=0;// 文件大小

	public final static int TIME_OUT = 30000;
	private final static int BUFFER_SIZE = 1024 * 8;
	@Override
	public void run() {
 		mAudio.setDownloadStatus1(DownloadStatus1.DOWNLOAD_IN);
		AudioDownloadManager.getInstance().notifyStatusChange(mAudio, DownloadStatus1.DOWNLOAD_IN, "");
		long range = 0;
 		try {
			HttpURLConnection urlConnection = (HttpURLConnection) new URL(mAudio.getVideoUrl()).openConnection();
			
			if(mAudio.getFileName() == null ) {
				mAudio.setFileName(CommonHelper.encodeMD5(mAudio.getVideoId()));
			}
			if(mAudio.getExt() == null) {
				mAudio.setExt("mp3");
			}

			File file = null;	
 			urlConnection.setConnectTimeout(TIME_OUT);// 设置连接超时时间
 			
 			if(mAudio.getFilePath() == null){
 				mAudio.setFilePath(getFilePath());
 				AudioManager.getInstance().saveVideo(mAudio);
 			}
 			
 			if(mAudio.getFilePath() != null){
 				file = new File(mAudio.getFilePath());
 				range = file.length();
 				if (range > 0) {// 如果range>0)
 					urlConnection.addRequestProperty("RANGE", "bytes=" + range + "-");// 设置下载的字节范围
 				}
 			}
			
 			int readen = 0;
 			
 			int contentLenght = urlConnection.getContentLength();// 获得文件的大小
 			
			if (SDCardManager.getAvailableExternalMemorySize(SDCardManager.getDownloadPath())
					< ((long)contentLenght + (20 * 1024 * 1024L))) {
				AudioDownloadManager.getInstance().deleteDownloadingByAlbumId(mAudio.getAlbumId());
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
				mAudio.setFileSize(total);

				long reported = 0;
				long atime 	= System.currentTimeMillis();
				long btime = System.currentTimeMillis();
				long predownloadsize = downloadSize+0;
				int speed = 0;
 				while ((len = is.read(buf)) >= 0) {
					if (mAudio.getDownloadStatus() != DownloadStatus1.DOWNLOAD_IN) {
						AudioDownloadManager.getInstance().endDownload(this);
						return;
					}
					fos.write(buf, 0, len);
					downloadSize += len;// 文件的长度
					readen += len;
					if (reported > BUFFER_SIZE / 2) {						
						AudioDownloadManager.getInstance().notifyProgressChange(mAudio, readen, total, speed);
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
						EncryptTools.encriptAudioFile(mAudio, file);
					}
					mAudio.setDownloadStatus1(AudioModel.DownloadStatus1.DOWNLOAD_COMPLETE);
					AudioDownloadManager.getInstance().notifyStatusChange(
							mAudio, DownloadStatus1.DOWNLOAD_COMPLETE, "");
					AudioDownloadManager.getInstance().endDownload(this);
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
		mAudio.setFileName(null);
		mAudio.setDownloadStatus1(AudioModel.DownloadStatus1.DOWNLOAD_ERROR);
		AudioDownloadManager.getInstance().notifyStatusChange(mAudio,
				DownloadStatus1.DOWNLOAD_ERROR, "");
		AudioDownloadManager.getInstance().endDownload(this);
	}
	
	private String getFilePath() {
		return new File(SDCardManager.getDownloadPath(), mAudio.getFileName() + ".download").getAbsolutePath();

	}
}
