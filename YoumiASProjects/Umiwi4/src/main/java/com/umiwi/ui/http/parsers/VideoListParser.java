package com.umiwi.ui.http.parsers;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;
import cn.youmi.framework.util.SingletonFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umiwi.ui.beans.VideoListBeans;
import com.umiwi.ui.managers.VideoManager;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.model.VideoModel;

public class VideoListParser implements Parser<ArrayList<VideoModel>, String> {
	@Override
	public ArrayList<VideoModel> parse(AbstractRequest<ArrayList<VideoModel>> request, String json) {
		ArrayList<VideoModel> result = new ArrayList<VideoModel>();
		Type listType = new TypeToken<ArrayList<VideoListBeans>>() {
		}.getType();
		
		ArrayList<VideoListBeans> res = SingletonFactory.getInstance(Gson.class).fromJson(json, listType);
		
		for(VideoListBeans db :res){
			VideoModel vm = VideoManager.getInstance().getVideoById(db.getVid()+"");
			if(vm == null){
				vm = new VideoModel();
			}
			vm.setVideoId(db.getVid());
			vm.setAlbumId(db.getAlbumid());
			vm.setAlbumTitle(db.getAlbumtitle());
			vm.setAlbumImageurl(db.getAlbumimageurl());
			vm.setTitle(db.getTitle());
 			vm.setVideoUrl(db.getUrl());
			vm.setExpiretime(db.getExpiretime());
			vm.setUid(YoumiRoomUserManager.getInstance().getUid());
			vm.setTry(false);
			VideoManager.getInstance().saveVideo(vm);
			
			result.add(vm);
		}
		return result;
	}
}
