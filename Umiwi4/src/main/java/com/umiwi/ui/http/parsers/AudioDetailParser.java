package com.umiwi.ui.http.parsers;

import com.google.gson.Gson;
import com.umiwi.ui.beans.updatebeans.ExperDetailsVoiceBean;
import com.umiwi.ui.managers.AudioManager;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.model.AudioModel;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;
import cn.youmi.framework.util.SingletonFactory;

public class AudioDetailParser implements Parser<ExperDetailsVoiceBean, String> {
	@Override
	public ExperDetailsVoiceBean parse(
			AbstractRequest<ExperDetailsVoiceBean> request, String json) {
		
		ExperDetailsVoiceBean res = SingletonFactory.getInstance(Gson.class).fromJson(json, ExperDetailsVoiceBean.class);

		if(res != null && res.getAudiofile() !=null) {
			for(ExperDetailsVoiceBean.AudiofileBean db :res.getAudiofile()){
				AudioModel vm = AudioManager.getInstance().getVideoById(db.getAid()+"");
				if(vm == null){
					vm = new AudioModel();
				}
				
				String albumTitle = res.getTitle();
				if(albumTitle == null) {
					albumTitle = res.getTitle();
				}
				
	 			vm.setTitle(db.getAtitle());
 				vm.setVideoUrl(db.getSource());
				vm.setVideoId(db.getAid() + "");
				vm.setAlbumId(res.getId() + "");
				vm.setAlbumTitle(albumTitle);
				vm.setExpiretime(db.getAplaytime());
				vm.setUid(YoumiRoomUserManager.getInstance().getUid());
//				vm.setImageURL(res.getImage());
				vm.setTry(false);
//				vm.setLastwatchposition((int) (db.getDuration() * db.getWatchProgress() / 100.0f * 1000));
				
				AudioManager.getInstance().saveVideo(vm);
			}
			
//			if(res.getTryvideo() != null){
//				for(UmiwiListDetailBeans db:res.getTryvideo()){
//					VideoModel vm = VideoManager.getInstance().getVideoById(db.getVid()+"");
//					if(vm == null){
//						vm = new VideoModel();
//					}
//
//					String albumTitle = res.getShorts();
//					if(albumTitle == null) {
//						albumTitle = res.getTitle();
//					}
//
//					vm.setTitle(db.getTitle());
//					vm.setVideoUrl(db.getUrl());
//					vm.setVideoId(db.getVid() + "");
//					vm.setAlbumId(res.getId()+"");
//					vm.setImageURL(res.getImage());
//					vm.setAlbumTitle(albumTitle);
//					vm.setAlbumImageurl(res.getImage());
//					vm.setTry(true);
//
//					vm.setUid(YoumiRoomUserManager.getInstance().getUid());
//
//					VideoManager.getInstance().saveVideo(vm);
//				}
//			}
		}
		
		return res;
	}
}
