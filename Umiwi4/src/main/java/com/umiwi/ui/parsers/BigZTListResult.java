package com.umiwi.ui.parsers;

import java.util.ArrayList;

public class BigZTListResult<E> {

	private int id;

	private String title;

	private String introduce;

	private boolean isBuy;

	private int price;

	private int rawPrice;

	private int discountPrice;

	private int sectionId;

	private String image;

	private String imageTwo;

	private ArrayList<E> items;

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

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public boolean isBuy() {
		return isBuy;
	}

	public void setBuy(boolean isBuy) {
		this.isBuy = isBuy;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getRawPrice() {
		return rawPrice;
	}

	public void setRawPrice(int rawPrice) {
		this.rawPrice = rawPrice;
	}

	public int getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(int discountPrice) {
		this.discountPrice = discountPrice;
	}

	public int getSectionId() {
		return sectionId;
	}

	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImageTwo() {
		return imageTwo;
	}

	public void setImageTwo(String imageTwo) {
		this.imageTwo = imageTwo;
	}

	public ArrayList<E> getItems() {
		return items;
	}

	public void setItems(ArrayList<E> items) {
		this.items = items;
	}

}