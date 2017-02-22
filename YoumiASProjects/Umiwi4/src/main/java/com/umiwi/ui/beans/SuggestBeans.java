package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 搜索用的建议词
 * 
 */
public class SuggestBeans extends BaseGsonBeans {

	@SerializedName("total")
	private int total;
	
	@SerializedName("record")
	private ArrayList<String> record;

	public static SuggestBeans fromJson(String json) {
		return new Gson().fromJson(json, SuggestBeans.class);
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public ArrayList<String> getRecord() {
		return record;
	}

	public void setRecord(ArrayList<String> record) {
		this.record = record;
	}

	
}
