package com.umiwi.ui.http.parsers;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;
import cn.youmi.framework.util.SingletonFactory;

import com.google.gson.Gson;
import com.umiwi.ui.beans.UmiwiListDetailBeans;
import com.umiwi.ui.beans.UmiwiListDetailBeans.ListDetailRequestData;
import com.umiwi.ui.managers.VideoManager;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.model.VideoModel;

public class CourseDetailParser implements Parser<ListDetailRequestData, String> {
	@Override
	public ListDetailRequestData parse(
			AbstractRequest<ListDetailRequestData> request, String json) {
		
		ListDetailRequestData res = SingletonFactory.getInstance(Gson.class).fromJson(json, ListDetailRequestData.class);

		if(res != null && res.getCourse() !=null) {
			for(UmiwiListDetailBeans db :res.getCourse()){
				VideoModel vm = VideoManager.getInstance().getVideoById(db.getVid()+"");
				if(vm == null){
					vm = new VideoModel();
				}
				
				String albumTitle = res.getShorts();
				if(albumTitle == null) {
					albumTitle = res.getTitle();
				}
				
	 			vm.setTitle(db.getTitle());
 				vm.setVideoUrl(db.getUrl());
				vm.setVideoId(db.getVid() + "");
				vm.setAlbumId(res.getId() + "");
				vm.setAlbumTitle(albumTitle);
				vm.setAlbumImageurl(res.getImage());
				vm.setExpiretime(db.getExpiretime());
				vm.setUid(YoumiRoomUserManager.getInstance().getUid());
				vm.setImageURL(res.getImage());
				vm.setTry(false);
				vm.setLastwatchposition((int) (db.getDuration() * db.getWatchProgress() / 100.0f * 1000));
				
				VideoManager.getInstance().saveVideo(vm);
			}
			
			if(res.getTryvideo() != null){
				for(UmiwiListDetailBeans db:res.getTryvideo()){
					VideoModel vm = VideoManager.getInstance().getVideoById(db.getVid()+"");
					if(vm == null){
						vm = new VideoModel();
					}

					String albumTitle = res.getShorts();
					if(albumTitle == null) {
						albumTitle = res.getTitle();
					}

					vm.setTitle(db.getTitle());
					vm.setVideoUrl(db.getUrl());
					vm.setVideoId(db.getVid() + "");
					vm.setAlbumId(res.getId()+"");
					vm.setImageURL(res.getImage());
					vm.setAlbumTitle(albumTitle);
					vm.setAlbumImageurl(res.getImage());
					vm.setTry(true);

					vm.setUid(YoumiRoomUserManager.getInstance().getUid());

					VideoManager.getInstance().saveVideo(vm);
				}
			}
		}
		
		return res;
	}
}
