package com.umiwi.ui.model;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

/**
 * @author tangxiyong
 * @version 2015-6-11 下午4:01:02
 */
public class BigZTList {

	@SerializedName("subtitle")
	public String subTitle;

	@SerializedName("subdescription")
	public String subDescription;

	@SerializedName("courselist")
	public ArrayList<BigZTCourseListModel> bigZTCourseList;

	@Override
	public boolean equals(Object o) {
		if (o instanceof BigZTList) {
			BigZTList other = (BigZTList) o;
			return other.bigZTCourseList.equals(bigZTCourseList);
		}
		return super.equals(o);
	}
}
