package com.umiwi.ui.model;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

/**
 * @author tangxiyong
 * @version 2015-6-10 上午10:46:08
 */
public class HomeRecommend {

	@SerializedName("type")
	public String recommendType;

	@SerializedName("name")
	public String recommendName;

	@SerializedName("courselist")
	public ArrayList<HomeRecommendCourseListModel> recommendCourseList;

	@SerializedName("taglist")
	public ArrayList<HomeRecommendTagListModel> recommendTagList;

	@Override
	public boolean equals(Object o) {
		if (o instanceof HomeRecommend) {
			HomeRecommend other = (HomeRecommend) o;
			return other.recommendCourseList.equals(recommendCourseList);
		}
		return super.equals(o);
	}
}
