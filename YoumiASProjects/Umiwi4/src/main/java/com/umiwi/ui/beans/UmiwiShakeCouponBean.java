package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 摇一摇优惠券
 * 
 * @author tjie00
 * 2014/01/10 13:30
 * 
 */
public class UmiwiShakeCouponBean extends BaseGsonBeans {

	@SerializedName("num")
	private String num;
	
	@SerializedName("lotteryuser")
	private String lotteryuser;
	
	@SerializedName("status")
	private String status;

	@SerializedName("record")
	private ArrayList<String> record;
	
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getLotteryuser() {
		return lotteryuser;
	}

	public void setLotteryuser(String lotteryuser) {
		this.lotteryuser = lotteryuser;
	}

	public ArrayList<String> getRecord() {
		return record;
	}

	public void setRecord(ArrayList<String> record) {
		this.record = record;
	}

	public UmiwiShakeCouponBean fromJson(String json) {
		return new Gson().fromJson(json, UmiwiShakeCouponBean.class);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
}
