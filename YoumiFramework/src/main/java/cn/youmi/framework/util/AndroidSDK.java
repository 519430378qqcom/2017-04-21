package cn.youmi.framework.util;

import android.os.Build;

/**
 * @author tangxiyong
 * @version 2014年9月16日 下午7:03:03
 */
public class AndroidSDK {

	/** 大于等于3.0  HONEYCOMB 11*/
	public static boolean isHONEYCOMB() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}
	/** >=ICE_CREAM_SANDWICH  14*/
	public static boolean isICS() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}
	/** JELLY_BEAN_MR2 18*/ 
	public static boolean isJELLY_BEAN_MR2() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
	}
	
	/** JELLY_BEAN 16*/ 
	public static boolean isJELLY_BEAN() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}
	/** isKK */ 
	public static boolean isKK() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	}
	
	public static boolean isLOLLIPOP() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
	}

	public static boolean isMarshmallow() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
	}
}
