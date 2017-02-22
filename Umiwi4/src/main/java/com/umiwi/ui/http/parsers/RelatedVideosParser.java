package com.umiwi.ui.http.parsers;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;
import cn.youmi.framework.util.SingletonFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.umiwi.ui.beans.UmiwiListBeans;
import com.umiwi.ui.managers.VideoManager;
import com.umiwi.ui.model.VideoModel;

public class RelatedVideosParser implements
		Parser<ArrayList<UmiwiListBeans>, String> {

	@Override
	public ArrayList<UmiwiListBeans> parse(
			AbstractRequest<ArrayList<UmiwiListBeans>> request, String json) {
		Type listType = new TypeToken<ArrayList<UmiwiListBeans>>() {
		}.getType();
		JsonObject rootObj = new JsonParser().parse(json).getAsJsonObject();
		JsonElement records = rootObj.get("record");
		ArrayList<UmiwiListBeans> beans = SingletonFactory.getInstance(Gson.class).fromJson(records, listType);
//		for (UmiwiListBeans db : beans) {
//			VideoModel vm = VideoManager.getInstance().getVideoById(
//					db.getId() + "");
//			if (vm == null) {
//				vm = new VideoModel();
//			}
//			vm.setTitle(db.getTitle());
//			vm.setAlbumId((String) request.getTag());
//			vm.setVideoUrl(db.getUrl());
//			vm.setVideoId(db.getId() + "");
//			vm.setImageURL(db.getImage());
//			vm.setAlbumImageurl(db.getImage());
//			VideoManager.getInstance().saveVideo(vm);
//		}
		return beans;
	}
}
