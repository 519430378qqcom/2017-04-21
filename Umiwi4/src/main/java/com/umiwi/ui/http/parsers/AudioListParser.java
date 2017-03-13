package com.umiwi.ui.http.parsers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umiwi.ui.beans.AudioListBeans;
import com.umiwi.ui.beans.VideoListBeans;
import com.umiwi.ui.managers.AudioManager;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.model.AudioModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;
import cn.youmi.framework.util.SingletonFactory;

public class AudioListParser implements Parser<ArrayList<AudioModel>, String> {
	@Override
	public ArrayList<AudioModel> parse(AbstractRequest<ArrayList<AudioModel>> request, String json) {
		ArrayList<AudioModel> result = new ArrayList<AudioModel>();
		Type listType = new TypeToken<ArrayList<AudioListBeans>>() {
		}.getType();
		
		ArrayList<VideoListBeans> res = SingletonFactory.getInstance(Gson.class).fromJson(json, listType);
		
		for(VideoListBeans db :res){
			AudioModel vm = AudioManager.getInstance().getVideoById(db.getVid()+"");
			if(vm == null){
				vm = new AudioModel();
			}
			vm.setVideoId(db.getVid());
			vm.setAlbumId(db.getAlbumid());
			vm.setAlbumTitle(db.getAlbumtitle());
//			vm.setAlbumImageurl(db.getAlbumimageurl());
			vm.setTitle(db.getTitle());
 			vm.setVideoUrl(db.getUrl());
			vm.setExpiretime(db.getExpiretime());
			vm.setUid(YoumiRoomUserManager.getInstance().getUid());
			vm.setTry(false);
			AudioManager.getInstance().saveVideo(vm);
			
			result.add(vm);
		}
		return result;
	}
}
