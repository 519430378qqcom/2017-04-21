package com.umiwi.ui.model;

import cn.youmi.framework.model.BaseModel;

import com.google.gson.annotations.SerializedName;

/**
 * @author tangxiyong
 * @version 2015-6-12 下午3:19:02
 */
public class VipIntroModel extends BaseModel {

	@SerializedName("image")
	public String image;

	@SerializedName("width")
	public int width;

	@SerializedName("height")
	public int height;

	@SerializedName("title")
	public String title;
}
