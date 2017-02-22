package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ActivityBean extends BaseGsonBeans {
	public static class Info{
		@SerializedName("id")
		public String id;
		@SerializedName("title")
		public String title;
		@SerializedName("thumb")
		public String imageURL;
		@SerializedName("huodong_type")
		public String type;
		@SerializedName("user_type")
		public String userType;
		@SerializedName("maxperson")
		public String maxPersion;
		@SerializedName("starttime")
		public String startTime;
		@SerializedName("endtime")
		public String endTime;
		@SerializedName("servertime")
		public String servertime;
		@SerializedName("enter_endtime_timestamp")
		public String enter_endtime_timestamp;
		@SerializedName("course_description")
		public String description;
		@SerializedName("total")
		public String total;
		@SerializedName("checkeds")
		public String chekedNum;
		
		@SerializedName("address")
		public String address;
		@SerializedName("sharecontent")
		public String shareContent;
		@SerializedName("shareurl")
		public String shareUrl;

		
		@SerializedName("course_mainpoint")
		private ArrayList<String> precedures;
		
		
		
		public ArrayList<String> getPrecedures(){
			return precedures;
		}
		
		@Override
		public String toString() {
			return "Info [id=" + id + ", title=" + title + ", type=" + type
					+ ", userType=" + userType + ", maxPersion=" + maxPersion
					+ ", startTime=" + startTime + ", endTime=" + endTime
					+ ", description=" + description + ", total=" + total
					+ ", chekedNum=" + chekedNum + "]";
		}
		
		
	}
	
	public static class TutorInfo{
		@SerializedName("uid")
		public String uid;
		@SerializedName("username")
		public String name;
		@SerializedName("title")
		public String title;
		@SerializedName("avatar3")
		public String avatarURL;
		@SerializedName("intro")
		public String intro;
		@SerializedName("total")
		public String total;
		
		@SerializedName("courseurl")
		public String courseURL;
		
	}
	
	
	public static class Joiner{
		@SerializedName("username")
		public String username;
		
		@SerializedName("avtar")
		public String avatarURL;
	}
	
	@SerializedName("huodonginfo")
	private ArrayList<Info> infos;
	
	@SerializedName("tutorinfo")
	private ArrayList<TutorInfo> tutorInfos;

	@SerializedName("joinuserinfo")
	private ArrayList<Joiner> joiners;
	
	public Info getInfo(){
		if(infos != null && !infos.isEmpty()){
			return infos.get(0);
		}
		return null;
	}
	
	public ArrayList<TutorInfo> getTutors(){
		return tutorInfos;
	}
	
	public ArrayList<Joiner> getJoiners(){
		return joiners;
	}
	
	@Override
	public String toString() {
		return "ActivityBean [infos=" + infos + "]";
	}
	
	
}
