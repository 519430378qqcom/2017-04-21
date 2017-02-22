package com.umiwi.ui.model;

import cn.youmi.framework.model.BaseModel;

public class AlbumModel extends BaseModel {
	private String albumId;
	private String imageURL;
	private String title;
	private String expiretime;
	private String uid;
	private int downloadVideoCount;//已下载视频数量
	private int downloadFilesize;//已经下载文件大小
	private boolean watched = false;
	
	/**
	 * @return the albumId
	 */
	public String getAlbumId() {
		return albumId;
	}
	/**
	 * @param albumId the albumId to set
	 */
	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}
	/**
	 * @return the imageURL
	 */
	public String getImageURL() {
		return imageURL;
	}
	/**
	 * @param imageURL the imageURL to set
	 */
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the expiretime
	 */
	public String getExpiretime() {
		return expiretime;
	}
	/**
	 * @param expiretime the expiretime to set
	 */
	public void setExpiretime(String expiretime) {
		this.expiretime = expiretime;
	}
	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	/**
	 * @return the downloadVideoCount
	 */
	public int getDownloadVideoCount() {
		return downloadVideoCount;
	}
	/**
	 * @param downloadVideoCount the downloadVideoCount to set
	 */
	public void setDownloadVideoCount(int downloadVideoCount) {
		this.downloadVideoCount = downloadVideoCount;
	}
	/**
	 * @return the downloadFilesize
	 */
	public int getDownloadFilesize() {
		return downloadFilesize;
	}
	/**
	 * @param downloadFilesize the downloadFilesize to set
	 */
	public void setDownloadFilesize(int downloadFilesize) {
		this.downloadFilesize = downloadFilesize;
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
