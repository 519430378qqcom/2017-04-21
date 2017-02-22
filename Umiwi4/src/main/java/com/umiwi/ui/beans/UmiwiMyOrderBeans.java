package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 我的订单
 * 
 * @author tangxiyong 2013-12-6下午3:29:03
 * 
 */
public class UmiwiMyOrderBeans extends BaseGsonBeans {

	@SerializedName("code")
	private String code;

	@SerializedName("ctime")
	private String ctime;

	/** 判断订单是否可关闭 */
	@SerializedName("statusnum")
	private String statusnum;
	/** 判断订单当前状态 */
	@SerializedName("status")
	private String status;

	@SerializedName("remark")
	private String remark;

	@SerializedName("type")
	private String type;

	@SerializedName("totalprice")
	private String totalprice;

	@SerializedName("payurl")
	private String payurl;
	/** 关闭订单与删除不同 */
	@SerializedName("closeurl")
	private String closeurl;

	@SerializedName("detail")
	private ArrayList<UmiwiMyOrderDetail> detail;

	public static UmiwiMyOrderBeans fromJson(String json) {
		return new Gson().fromJson(json, UmiwiMyOrderBeans.class);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getStatusnum() {
		return statusnum;
	}

	public void setStatusnum(String statusnum) {
		this.statusnum = statusnum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(String totalprice) {
		this.totalprice = totalprice;
	}

	public String getPayurl() {
		return payurl;
	}

	public void setPayurl(String payurl) {
		this.payurl = payurl;
	}

	public String getCloseurl() {
		return closeurl;
	}

	public void setCloseurl(String closeurl) {
		this.closeurl = closeurl;
	}

	public ArrayList<UmiwiMyOrderDetail> getDetail() {
		return detail;
	}

	public void setDetail(ArrayList<UmiwiMyOrderDetail> detail) {
		this.detail = detail;
	}

	public static class MyOrderListRequestData {

		@SerializedName("total")
		private int total;

		@SerializedName("totals")
		private int totals;

		@SerializedName("curr_page")
		private int curr_page;

		@SerializedName("pages")
		private int pages;

		@SerializedName("record")
		private ArrayList<UmiwiMyOrderBeans> record;

		public int getTotal() {
			return total;
		}

		public int getTotals() {
			return totals;
		}

		public int getCurr_page() {
			return curr_page;
		}

		public int getPages() {
			return pages;
		}

		public ArrayList<UmiwiMyOrderBeans> getRecord() {
			return record;
		}

	}
}
