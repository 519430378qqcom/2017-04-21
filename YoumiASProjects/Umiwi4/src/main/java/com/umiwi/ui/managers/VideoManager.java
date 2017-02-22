package com.umiwi.ui.managers;

import java.util.ArrayList;
import java.util.HashMap;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.manager.ModelManager;
import cn.youmi.framework.util.SingletonFactory;

import com.umiwi.ui.dao.VideoDao;
import com.umiwi.ui.http.parsers.VideoListParser;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.model.VideoModel;
import com.umiwi.ui.model.VideoModel.DownloadStatus;
public class VideoManager extends ModelManager<String,VideoModel>{
	
	public static VideoManager getInstance(){
		return SingletonFactory.getInstance(VideoManager.class);
	}
	
 	private Listener<ArrayList<VideoModel>> videoListener = new Listener<ArrayList<VideoModel>>(){
		@Override
		public void onResult(AbstractRequest<ArrayList<VideoModel>> request, ArrayList<VideoModel> videos) {
			 onModelsGet("", videos);
			 if(videos != null) {
				 cache.put(videos.get(0).getAlbumId(), videos);
			 }
		}
		
		@Override
		public void onError(AbstractRequest<ArrayList<VideoModel>> requet, int statusCode,
				String body) {
			
		}
	};
	
	
	
	private HashMap<String,ArrayList<VideoModel>> cache = new HashMap<String, ArrayList<VideoModel>>();
	public ArrayList<VideoModel> getVideosByAlbumId(String albumId,boolean requestFromServerIfNotPresent){
		ArrayList<VideoModel> videos = cache.get(albumId);
		if(videos == null){
			if(requestFromServerIfNotPresent) {
  				GetRequest<ArrayList<VideoModel>> get = new GetRequest<ArrayList<VideoModel>>(String.format(UmiwiAPI.ALBUM_VIDEOS_URL,String.format(albumId)), VideoListParser.class, videoListener);
				HttpDispatcher.getInstance().go(get);
			}
		}
		return videos;
	}
	
	
	
	
	public ArrayList<VideoModel> getVideosByAlbumId(String albumId) {
		return VideoDao.getInstance().getByAlbumid(albumId);
	}
	
	public VideoModel getVideoById(String vid){
		return VideoDao.getInstance().getByVideoid(vid);
	}
	
	public ArrayList<VideoModel> getDownloadingVideos(){
		ArrayList<VideoModel> videos = VideoDao.getInstance().getDownloadingVideos();
//		for(VideoModel video:videos){
			//VideoDownloadManager.getInstance().syncStatus(video);
//		}
		return videos;
	}

	
	public void saveVideos(ArrayList<VideoModel> videos){
		for(VideoModel v:videos){
			 VideoDao.getInstance().save(v);//TODO batch operation
		}
	}
	
	public void saveVideo(VideoModel v){
		if (v == null) {
			return;
		}
		 VideoDao.getInstance().save(v);
	}
	
	public void setDownloadStatusByVideoId(String VideoId, int status) {
		VideoDao.getInstance().setDownloadStatusByVideoId(VideoId,  status);
	}
	
	public void setDownloadWaitByVideoId(String VideoId) {
		setDownloadStatusByVideoId(VideoId, VideoModel.DownloadStatus.DOWNLOAD_WAIT.getValue());
	}
	
	public void setDownloadPauseByVideoId(String VideoId) {
		setDownloadStatusByVideoId(VideoId, VideoModel.DownloadStatus.DOWNLOAD_PAUSE.getValue());

	}
	public void setDownloadingByVideoId(String VideoId) {
		setDownloadStatusByVideoId(VideoId, VideoModel.DownloadStatus.DOWNLOAD_IN.getValue());
	}
	
	public void setDownloadCompleteByVideoId(String VideoId) {
		setDownloadStatusByVideoId(VideoId, VideoModel.DownloadStatus.DOWNLOAD_COMPLETE.getValue());
	}
	
	public void setDownloadErrorByVideoId(String VideoId) {
		setDownloadStatusByVideoId(VideoId, VideoModel.DownloadStatus.DOWNLOAD_ERROR.getValue());
	}
	
	
	public void setWatchedByVideoId(String videoId) {
		
		VideoDao.getInstance().setWatchedByVideoId(videoId);
	}
	
	/**
	 * 返回正在下载和暂停下载的视频列表
	 * @return
	 */
	public ArrayList<VideoModel> getDownloadList() {
		VideoDao videodao = SingletonFactory.getInstance(VideoDao.class);
  		ArrayList<VideoModel> videolist =  videodao.getDownloadingList();
		if(videolist == null) {
			return new ArrayList<VideoModel>();
		}
		return videolist;
	}
	
	public ArrayList<VideoModel> getDownloadingListByAlbumId(String albumId) {
 		return VideoDao.getInstance().getDownloadingListByAlbumId(albumId);
	}
	
	/**
	 * 返回已下载的视频列表
	 * @return
	 */
	public ArrayList<VideoModel> getDownloadedList() {
 		return VideoDao.getInstance().getDownloadedList();
	}
	
	 
	public ArrayList<VideoModel> getDownloadedListByAlbumId(String albumId) {
 		return VideoDao.getInstance().getDownloadedListByAlbumId(albumId);
	}
	
	public ArrayList<VideoModel> getDownloadingListByVideoId(String videoId) {
 		return VideoDao.getInstance().getDownloadingListByVideoId(videoId);

	}
	
	
	public void setStatusByVideoId(String VideoId, DownloadStatus downloadStatus) {
		VideoDao.getInstance().setStatusByVideoId(VideoId, downloadStatus);
	}
	
	public ArrayList<String> getAlbumIds() {
		return VideoDao.getInstance().getAlbumIds();
	}
	
 	 
}
