package com.umiwi.ui.model;

import java.util.HashMap;

public enum UserClass {
	UNKNOWN(-1), UNREGISTERED(0), // unregistered user
	REGISTERED(1), SILVER(20), GOLDEN(22), DIAMOND(23);

	private int value;
	private static HashMap<String,UserClass> str2UserClassMap = new HashMap<String, UserClass>();
	
	static {
		str2UserClassMap.put("钻石会员", DIAMOND);
		str2UserClassMap.put("黄金会员", GOLDEN);
		str2UserClassMap.put("白银会员", SILVER);
		str2UserClassMap.put("注册会员", REGISTERED);
	}

	UserClass(int value) {
		this.value = value;
	}

	public int getIntValue() {
		return value;
	}

	public int toInt() {
		return value;
	}

	public static UserClass fromInt(int value) {
		switch (value) {
		case 0:
			return UNREGISTERED;
		case 1:
			return REGISTERED;
		case 20:
			return SILVER;
		case 22:
			return GOLDEN;
		case 23:
			return DIAMOND;
		default:
			return UNKNOWN;
		}
	}

	public static UserClass fromString(String value) {
		if (value == null) {
			return null;
		}
		return str2UserClassMap.get(value);
	}
}
