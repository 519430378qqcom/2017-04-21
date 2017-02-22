package com.umiwi.ui.managers;

import com.umiwi.ui.dao.SettingDao;

/**
@Author buhe
2014-6-13下午2:21:34
 */
public class Settings {
	public static final String KEY_UUID = "uuid";
	
	public static String getString(String key){
		return SettingDao.getInstance().get(key).getValue();
	}
	
	public static boolean getBoolean(String key){
		return Boolean.parseBoolean(SettingDao.getInstance().get(key).getValue());
	}
	
	public static void put(String key,String value){
		SettingDao.getInstance().save(key, value);
	}
	
	public static void put(String key,boolean value){
		SettingDao.getInstance().save(key, String.valueOf(value));
	}
}
