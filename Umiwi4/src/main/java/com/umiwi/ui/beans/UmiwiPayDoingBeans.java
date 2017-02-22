package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

/**
 * 支付界面的beans
 * 
 * @author tangxiyong 2014-3-14下午6:58:49
 * 
 */
public class UmiwiPayDoingBeans extends BaseGsonBeans {

	/** 下单成功，用此链接跳转到支付界面 */
	@SerializedName("payurl")
	private String payurl;

	@SerializedName("balance")
	private String balance;
	@SerializedName("payment_id")
	private String payment_id;
	@SerializedName("order_id")
	private String order_id;
	@SerializedName("order_amt")
	private String order_amt;
	@SerializedName("amount")
	private String amount;
	@SerializedName("uid")
	private String uid;
	@SerializedName("confirmurl")
	private String confirmurl;
	@SerializedName("isenough")
	private boolean isenough;

	/** 第三方支付：支付宝等 */
	@SerializedName("sdk")
	private ArrayList<UmiwiPayDoingPaymentBeans> pay_sdk;
	/** 银联支付 */
	@SerializedName("bank")
	private ArrayList<UmiwiPayDoingPaymentBeans> pay_bank;

	public String getPayurl() {
		return payurl;
	}

	public void setPayurl(String payurl) {
		this.payurl = payurl;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getPayment_id() {
		return payment_id;
	}

	public void setPayment_id(String payment_id) {
		this.payment_id = payment_id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getOrder_amt() {
		return order_amt;
	}

	public void setOrder_amt(String order_amt) {
		this.order_amt = order_amt;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getConfirmurl() {
		return confirmurl;
	}

	public void setConfirmurl(String confirmurl) {
		this.confirmurl = confirmurl;
	}

	public boolean isIsenough() {
		return isenough;
	}

	public void setIsenough(boolean isenough) {
		this.isenough = isenough;
	}

	public ArrayList<UmiwiPayDoingPaymentBeans> getPay_sdk() {
		return pay_sdk;
	}

	public void setPay_sdk(ArrayList<UmiwiPayDoingPaymentBeans> pay_sdk) {
		this.pay_sdk = pay_sdk;
	}

	public ArrayList<UmiwiPayDoingPaymentBeans> getPay_bank() {
		return pay_bank;
	}

	public void setPay_bank(ArrayList<UmiwiPayDoingPaymentBeans> pay_bank) {
		this.pay_bank = pay_bank;
	}

	public static class PayDoingBeansRequestData {
		@SerializedName("e")
		private String doing_e;
		@SerializedName("m")
		private String doing_m;
		/** 下订单成功生成的链接：buyurl */
		@SerializedName("r")
		private UmiwiPayDoingBeans doing_r;

		@SerializedName("payment")
		private UmiwiPayDoingBeans payment;

		public String getDoing_e() {
			return doing_e;
		}

		public String getDoing_m() {
			return doing_m;
		}

		public UmiwiPayDoingBeans getDoing_r() {
			return doing_r;
		}

		public UmiwiPayDoingBeans getPayment() {
			return payment;
		}

	}

}
