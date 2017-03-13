package com.umiwi.ui.managers;

import com.umiwi.ui.dao.AudioDao;
import com.umiwi.ui.http.parsers.AudioListParser;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.model.AudioModel;
import com.umiwi.ui.model.AudioModel.DownloadStatus1;

import java.util.ArrayList;
import java.util.HashMap;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.manager.ModelManager;
import cn.youmi.framework.util.SingletonFactory;

public class AudioManager extends ModelManager<String,AudioModel>{

	public static AudioManager getInstance(){
		return SingletonFactory.getInstance(AudioManager.class);
	}

 	private Listener<ArrayList<AudioModel>> audioListener = new Listener<ArrayList<AudioModel>>(){
		@Override
		public void onResult(AbstractRequest<ArrayList<AudioModel>> request, ArrayList<AudioModel> audios) {
			 onModelsGet("", audios);
			 if(audios != null) {
				 cache.put(audios.get(0).getAlbumId(), audios);
			 }
		}

		@Override
		public void onError(AbstractRequest<ArrayList<AudioModel>> requet, int statusCode,
				String body) {

		}
	};



	private HashMap<String,ArrayList<AudioModel>> cache = new HashMap<String, ArrayList<AudioModel>>();
	public ArrayList<AudioModel> getAudiosByAlbumId(String albumId,boolean requestFromServerIfNotPresent){
		ArrayList<AudioModel> audios = cache.get(albumId);
		if(audios == null){
			if(requestFromServerIfNotPresent) {
  				GetRequest<ArrayList<AudioModel>> get = new GetRequest<ArrayList<AudioModel>>(String.format(UmiwiAPI.ALBUM_VIDEOS_URL,String.format(albumId)), AudioListParser.class, audioListener);
				HttpDispatcher.getInstance().go(get);
			}
		}
		return audios;
	}




	public ArrayList<AudioModel> getVideosByAlbumId(String albumId) {
		return AudioDao.getInstance().getByAlbumid(albumId);
	}

	public AudioModel getVideoById(String vid){
		return AudioDao.getInstance().getByVideoid(vid);
	}

	public ArrayList<AudioModel> getDownloadingVideos(){
		ArrayList<AudioModel> audios = AudioDao.getInstance().getDownloadingAudios();
//		for(AudioModel video:audios){
			//VideoDownloadManager.getInstance().syncStatus(video);
//		}
		return audios;
	}


	public void saveVideos(ArrayList<AudioModel> audios){
		for(AudioModel v:audios){
			 AudioDao.getInstance().save(v);//TODO batch operation
		}
	}

	public void saveVideo(AudioModel v){
		if (v == null) {
			return;
		}
		 AudioDao.getInstance().save(v);
	}

	public void setDownloadStatusByVideoId(String VideoId, int status) {
		AudioDao.getInstance().setDownloadStatusByVideoId(VideoId,  status);
	}

	public void setDownloadWaitByAudioId(String VideoId) {
		setDownloadStatusByVideoId(VideoId, DownloadStatus1.DOWNLOAD_WAIT.getValue());
	}

	public void setDownloadPauseByVideoId(String VideoId) {
		setDownloadStatusByVideoId(VideoId, DownloadStatus1.DOWNLOAD_PAUSE.getValue());

	}
	public void setDownloadingByVideoId(String VideoId) {
		setDownloadStatusByVideoId(VideoId, DownloadStatus1.DOWNLOAD_IN.getValue());
	}

	public void setDownloadCompleteByVideoId(String VideoId) {
		setDownloadStatusByVideoId(VideoId, AudioModel.DownloadStatus1.DOWNLOAD_COMPLETE.getValue());
	}

	public void setDownloadErrorByVideoId(String VideoId) {
		setDownloadStatusByVideoId(VideoId, DownloadStatus1.DOWNLOAD_ERROR.getValue());
	}
	
	
	public void setWatchedByVideoId(String videoId) {
		
		AudioDao.getInstance().setWatchedByVideoId(videoId);
	}
	
	/**
	 * 返回正在下载和暂停下载的视频列表
	 * @return
	 */
	public ArrayList<AudioModel> getDownloadList() {
		AudioDao audiodao = SingletonFactory.getInstance(AudioDao.class);
  		ArrayList<AudioModel> audiolist =  audiodao.getDownloadingList();
		if(audiolist == null) {
			return new ArrayList<AudioModel>();
		}
		return audiolist;
	}
	
	public ArrayList<AudioModel> getDownloadingListByAlbumId(String albumId) {
 		return AudioDao.getInstance().getDownloadingListByAlbumId(albumId);
	}
	
	/**
	 * 返回已下载的视频列表
	 * @return
	 */
	public ArrayList<AudioModel> getDownloadedList() {
 		return AudioDao.getInstance().getDownloadedList();
	}
	
	 
	public ArrayList<AudioModel> getDownloadedListByAlbumId(String albumId) {
 		return AudioDao.getInstance().getDownloadedListByAlbumId(albumId);
	}
	
	public ArrayList<AudioModel> getDownloadingListByVideoId(String videoId) {
 		return AudioDao.getInstance().getDownloadingListByAudioId(videoId);

	}
	
	
	public void setStatusByVideoId(String VideoId, AudioModel.DownloadStatus1 downloadStatus1) {
		AudioDao.getInstance().setStatusByVideoId(VideoId, downloadStatus1);
	}
	
	public ArrayList<String> getAlbumIds() {
		return AudioDao.getInstance().getAlbumIds();
	}
	
 	 
}
