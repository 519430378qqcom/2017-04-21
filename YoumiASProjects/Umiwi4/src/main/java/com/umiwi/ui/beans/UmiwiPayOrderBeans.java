package com.umiwi.ui.beans;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 下订单页面的beans
 * 
 * @author tangxiyong 2014-3-14下午6:20:59
 * 
 */
public class UmiwiPayOrderBeans extends BaseGsonBeans {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6028399847792590033L;

	/** 用户所有的优惠券 */
	@SerializedName("coupon")
	private UmiwiPayOrderDiscountlistBeans coupon;

	/** 限时折扣 */
	@SerializedName("timed")
	private UmiwiPayOrderCouponBeans timed;

	/** 会员折扣 */
	@SerializedName("vip")
	private UmiwiPayOrderCouponBeans vip;

	/** 白银 会员抵扣 */
	@SerializedName("hycd")
	private UmiwiPayOrderCouponBeans hycd;

	/** 白银打卡优惠 */
	@SerializedName("silverscore")
	private UmiwiPayOrderCouponBeans silverscore;

	public UmiwiPayOrderDiscountlistBeans getCoupon() {
		return coupon;
	}

	public void setCoupon(UmiwiPayOrderDiscountlistBeans coupon) {
		this.coupon = coupon;
	}

	public UmiwiPayOrderCouponBeans getTimed() {
		return timed;
	}

	public void setTimed(UmiwiPayOrderCouponBeans timed) {
		this.timed = timed;
	}

	public UmiwiPayOrderCouponBeans getVip() {
		return vip;
	}

	public void setVip(UmiwiPayOrderCouponBeans vip) {
		this.vip = vip;
	}

	public UmiwiPayOrderCouponBeans getHycd() {
		return hycd;
	}

	public void setHycd(UmiwiPayOrderCouponBeans hycd) {
		this.hycd = hycd;
	}

	public UmiwiPayOrderCouponBeans getSilverscore() {
		return silverscore;
	}

	public void setSilverscore(UmiwiPayOrderCouponBeans silverscore) {
		this.silverscore = silverscore;
	}

	public static UmiwiPayOrderBeans fromJson(String json) {
		return new Gson().fromJson(json, UmiwiPayOrderBeans.class);
	}

	public static class PayOrderBeansRequestData {
		@SerializedName("id")
		private String id;
		@SerializedName("type")
		private String type;
		@SerializedName("title")
		private String title;
		@SerializedName("desc")
		private String desc;
		@SerializedName("price")
		private String price;
		@SerializedName("money")
		private String money;
		@SerializedName("offset")
		private String offset;
		@SerializedName("offsetdesc")
		private String offsetdesc;
		@SerializedName("uid")
		private String uid;
		@SerializedName("balance")
		private String balance;

		@SerializedName("couponid")
		private String couponid;
		@SerializedName("couponcode")
		private String couponcode;
		@SerializedName("discounttype")
		private String discounttype;
		@SerializedName("hasdiscountlist")
		private String hasdiscountlist;
		@SerializedName("discountsort")
		private String discountsort;

		/** 各类优惠列表：优惠券、限时、打卡... */
		@SerializedName("discountlist")
		private UmiwiPayOrderBeans discountlist;

		/** 跳转到支付界面 */
		@SerializedName("buyurl")
		private String buyurl;

		public String getId() {
			return id;
		}

		public String getType() {
			return type;
		}

		public String getTitle() {
			return title;
		}

		public String getDesc() {
			return desc;
		}

		public String getPrice() {
			return price;
		}

		public String getMoney() {
			return money;
		}

		public String getOffset() {
			return offset;
		}

		public String getOffsetdesc() {
			return offsetdesc;
		}

		public String getUid() {
			return uid;
		}

		public String getBalance() {
			return balance;
		}

		public String getCouponid() {
			return couponid;
		}

		public String getCouponcode() {
			return couponcode;
		}

		public String getDiscounttype() {
			return discounttype;
		}

		public String getHasdiscountlist() {
			return hasdiscountlist;
		}

		public String getDiscountsort() {
			return discountsort;
		}

		public UmiwiPayOrderBeans getDiscountlist() {
			return discountlist;
		}

		public String getBuyurl() {
			return buyurl;
		}

	}


}
