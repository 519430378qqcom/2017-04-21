package com.umiwi.ui.model;

import java.io.File;
import java.io.Serializable;

import cn.youmi.framework.model.BaseModel;

public class VideoModel extends BaseModel implements Serializable{
	private String albumId;
	private String albumTitle;
	private String albumImageurl;
 	private String videoId;
	private String videoUrl;// 视频下载地址
	private boolean isTry = false; //是否试看
	private String filePath;// 视频保存路径
	private String fileName;// 视频文件名
	private String ext;
	private String imageURL;
	private String title;
	private String expiretime;// 用户对视频观看权限的过期时间
	private String uid;// 当前用户
	private long progress;// 下载进度
	private int speed;// 当前下载速度
	private int filesize;
	private int downloadedSize;// does not go into db;
	private String classes;
	private byte[] fileheader;// 原视频文件头
	private int lastwatchposition;// 上次观看位置 第多少秒
	private boolean watched = false;

	private DownloadStatus mDownloadStatus;

	public enum DownloadStatus {

		NOTIN(0), DOWNLOAD_WAIT(1), DOWNLOAD_PAUSE(2), DOWNLOAD_IN(3), DOWNLOAD_COMPLETE(
				4), DOWNLOAD_ERROR(5);

		private final int Value;

		DownloadStatus(int value) {
			this.Value = value;
		}

		public static DownloadStatus ValueOf(int value) {
			switch (value) {
			case 0:
				return NOTIN;
			case 1:
				return DOWNLOAD_WAIT;
			case 2:
				return DOWNLOAD_PAUSE;
			case 3:
				return DOWNLOAD_IN;
			case 4:
				return DOWNLOAD_COMPLETE;
			case 5:
				return DOWNLOAD_ERROR;
			default:
				return null;
			}
		}

		public int getValue() {
			return this.Value;

		}

		public String getStringValue() {
			return String.valueOf(this.Value);
		}

	}

	// -------------- getters and setters--------

	public boolean isExpired() {
		return false;
//		if(expiretime == null || "".equals(expiretime)) {
//			return true;
//		}
//		
//		long currentSecond = System.currentTimeMillis()/1000;   
//		long mExpiretime = Long.parseLong(expiretime);
//		return currentSecond>mExpiretime;
	}
	
	public boolean isLocalFileValid() {
		boolean valid = filePath != null
				&& mDownloadStatus == DownloadStatus.DOWNLOAD_COMPLETE;
		if (valid) {
			File f = new File(getFilePath());
			valid = valid && f.exists();
		}
		return valid;
	}

	public int getDownloadedSize() {
		return downloadedSize;
	}

	public void setDownloadedSize(int downloadedSize) {
		this.downloadedSize = downloadedSize;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	/**
	 * @return the albumTitle
	 */
	public String getAlbumTitle() {
		return albumTitle;
	}

	/**
	 * @param albumTitle
	 *            the albumTitle to set
	 */
	public void setAlbumTitle(String albumTitle) {
		this.albumTitle = albumTitle;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getExpiretime() {
		return expiretime;
	}

	public void setExpiretime(String expiretime) {
		this.expiretime = expiretime;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the videoUrl
	 */
	public String getVideoUrl() {
		return videoUrl;
	}

	/**
	 * @param videoUrl
	 *            the videoUrl to set
	 */
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the ext
	 */
	public String getExt() {
		return ext;
	}

	/**
	 * @param ext
	 *            the ext to set
	 */
	public void setExt(String ext) {
		this.ext = ext;
	}

	/**
	 * @return the classes
	 */
	public String getClasses() {
		return classes;
	}

	/**
	 * @param classes
	 *            the classes to set
	 */
	public void setClasses(String classes) {
		this.classes = classes;
	}

	/**
	 * @return the fileheader
	 */
	public byte[] getFileheader() {
		return fileheader;
	}

	/**
	 * @param fileheader
	 *            the fileheader to set
	 */
	public void setFileheader(byte[] fileheader) {
		this.fileheader = fileheader;
	}

	/**
	 * @return the lastwatchposition
	 */
	public int getLastwatchposition() {
		return lastwatchposition;
	}

	/**
	 * @param lastwatchposition
	 *            the lastwatchposition to set
	 */
	public void setLastwatchposition(int lastwatchposition) {
		this.lastwatchposition = lastwatchposition;
	}

	/**
	 * @return the progress
	 */
	public long getProgress() {
		return progress;
	}

	/**
	 * @param progress
	 *            the progress to set
	 */
	public void setProgress(long progress) {
		this.progress = progress;
	}

	/**
	 * @return the speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * @param speed
	 *            the speed to set
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * @return the filesize
	 */
	public int getFileSize() {
		return filesize;
	}
	
	/**
	 * @param filesize
	 *            the filesize to set
	 */
	public void setFileSize(int filesize) {
		this.filesize = filesize;
	}
	
	
	/**
	 * @return the isTry
	 */
	public boolean isTry() {
		return isTry;
	}

	/**
	 * @param isTry the isTry to set
	 */
	public void setTry(boolean isTry) {
		this.isTry = isTry;
	}

	/**
	 * @return the albumImageurl
	 */
	public String getAlbumImageurl() {
		return albumImageurl;
	}

	/**
	 * @param albumImageurl the albumImageurl to set
	 */
	public void setAlbumImageurl(String albumImageurl) {
		this.albumImageurl = albumImageurl;
	}

	public synchronized DownloadStatus getDownloadStatus() {
		return mDownloadStatus;
	}

	public synchronized void setDownloadStatus(DownloadStatus value) {
		mDownloadStatus = value;
	}

	@Override
	public boolean equals(Object o) {
		if(o == null){
			return false;
		}
		if (o == this) {
			return true;
		}
		if (o instanceof VideoModel) {
			VideoModel that = (VideoModel) o;
			if (that.getVideoId() != null
					&& that.getVideoId().equals(this.getVideoId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (this.videoId != null) {
			return this.videoId.hashCode();
		}
		return super.hashCode();
	}

	/**
	 * @return the watched
	 */
	public boolean isWatched() {
		return watched;
	}

	/**
	 * @param watched the watched to set
	 */
	public void setWatched(boolean watched) {
		this.watched = watched;
	}
	
	
}
