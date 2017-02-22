package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 创业类别bean
 */
public class StageSectionBean extends BaseGsonBeans {

	public static StageSectionBean fromJson(String json) {
		return new Gson().fromJson(json, StageSectionBean.class);
	}

	@SerializedName("catname")
	private String name;

	@SerializedName("detailurl")
	private String url;

	@SerializedName("icon")
	private String icon;

	@SerializedName("type")
	private String type;

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public String getIcon() {
		return icon;
	}

	public CategoryType categoryType() {
		if ("course".equals(type)) {
			return CategoryType.COURSE;
		} else if ("category".equals(type)) {
			return CategoryType.CATEGORY;
		} else if ("zhuanti2".equals(type)) {
			return CategoryType.ZHUANTI2;
		} else if ("zhuantilist".equals(type)) {
			return CategoryType.ZHUANTILIST;
		} else if ("zhuanti3".equals(type)) {
			return CategoryType.ZHUANTI3;
		} else if ("newcourse".equals(type)) {
			return CategoryType.NEWCOURSE;
		} else if ("tutor".equals(type)) {
			return CategoryType.TUTOR;
		} else if ("ranklist".equals(type)) {
			return CategoryType.RANKLIST;
		} else if ("diamond".equals(type)) {
			return CategoryType.DIAMOND;
		}
		return null;
	}

	public enum CategoryType {
		COURSE(1), // 课程
		CATEGORY(2), // 课程列表
		ZHUANTI2(3), // 专题
		ZHUANTILIST(4), // 专题列表
		ZHUANTI3(5), // 大专题
		TUTOR(7), // 讲师列表
		RANKLIST(8), // 排行榜
		DIAMOND(9), // 会员
		NEWCOURSE(6);// 大专题

		private int value;

		public int getValue() {
			return value;
		}

		CategoryType(final int value) {
			this.value = value;
		}

		public String toString() {
			return String.valueOf(this.value);
		}

	}

	public static class StageSectionBeanWapper {

		@SerializedName("name")
		private String name;

		@SerializedName("list")
		private ArrayList<StageSectionBean> list;

		public String getName() {
			return name;
		}

		public ArrayList<StageSectionBean> getList() {
			return list;
		}

	}

	public static class StageSectionBeanRequestData {

		@SerializedName("record")
		private ArrayList<StageSectionBeanWapper> record;

		public ArrayList<StageSectionBeanWapper> getRecord() {
			return record;
		}

	}
}
