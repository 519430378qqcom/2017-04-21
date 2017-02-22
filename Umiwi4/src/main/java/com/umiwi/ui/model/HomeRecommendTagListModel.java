package com.umiwi.ui.model;

import cn.youmi.framework.model.BaseModel;

import com.google.gson.annotations.SerializedName;

/**
 * @author tangxiyong
 * @version 2015-6-10 上午10:53:32
 */
public class HomeRecommendTagListModel extends BaseModel {

	@SerializedName("title")
	public String title;

	@SerializedName("detailurl")
	public String detailUrl;

	@SerializedName("type")
	private String type;
	
	public TagType tagType() {
		if ("course".equals(type)) {
			return TagType.COURSE;
		} else if ("category".equals(type)) {
			return TagType.CATEGORY;
		} else if ("zhuanti2".equals(type)) {
			return TagType.ZHUANTI2;
		} else if ("zhuantilist".equals(type)) {
			return TagType.ZHUANTILIST;
		} else if ("zhuanti3".equals(type)) {
			return TagType.ZHUANTI3;
		} else if ("newcourse".equals(type)) {
			return TagType.NEWCOURSE;
		}
		return null;
	}

	public enum TagType {
		COURSE(1),//课程
		CATEGORY(2),//课程列表
		ZHUANTI2(3),//专题
		ZHUANTILIST(4),//专题列表
		ZHUANTI3(5),//大专题
		NEWCOURSE(6);//大专题

		private int value;

		public int getValue() {
			return value;
		}

		TagType(final int value) {
			this.value = value;
		}

		public String toString() {
			return String.valueOf(this.value);
		}

	}

}
