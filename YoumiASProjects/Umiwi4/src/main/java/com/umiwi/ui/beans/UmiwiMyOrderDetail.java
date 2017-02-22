package com.umiwi.ui.beans;

import com.google.gson.annotations.SerializedName;

/**
 * 我的订单 跳转详情页
 * 
 * @author tangxiyong 2013-12-6下午4:07:29
 * 
 */
public class UmiwiMyOrderDetail {

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private String title;

	@SerializedName("product_type")
	private String product_type;

	@SerializedName("expire_time")
	private String expire_time;

	@SerializedName("price")
	private String price;

	@SerializedName("image")
	private String image;

	@SerializedName("authorname")
	private String authorname;

	@SerializedName("detailurl")
	private String detailurl;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProduct_type() {
		return product_type;
	}

	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}

	public String getExpire_time() {
		return expire_time;
	}

	public void setExpire_time(String expire_time) {
		this.expire_time = expire_time;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAuthorname() {
		return authorname;
	}

	public void setAuthorname(String authorname) {
		this.authorname = authorname;
	}

	public String getDetailurl() {
		return detailurl;
	}

	public void setDetailurl(String detailurl) {
		this.detailurl = detailurl;
	}

}
