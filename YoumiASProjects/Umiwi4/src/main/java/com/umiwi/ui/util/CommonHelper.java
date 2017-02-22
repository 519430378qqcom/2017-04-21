package com.umiwi.ui.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;

import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.Settings;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用帮助类
 * 
 * @author tangxiyong 2013-12-2下午3:23:45
 * 
 */
public class CommonHelper {

	/** 删除文字里的空白 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * MD5加密 将传进来的图片的url地址加密
	 * 
	 * @param s
	 *            传进的url
	 * @return 加密后的值
	 */
	public static String encodeMD5(String s) {
		byte[] input = s.getBytes();
		String output = null;
		// 声明16进制字母
		char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			// 获得一个MD5摘要算法的对象
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(input);
			/*
			 * MD5算法的结果是128位一个整数,在这里javaAPI已经把结果转换成字节数组了
			 */
			byte[] tmp = md.digest();// 获得MD5的摘要结果
			char[] str = new char[32];
			byte b = 0;
			for (int i = 0; i < 16; i++) {
				b = tmp[i];
				str[2 * i] = hexChar[b >>> 4 & 0xf];// 取每一个字节的低四位换成16进制字母
				str[2 * i + 1] = hexChar[b & 0xf];// 取每一个字节的高四位换成16进制字母
			}
			output = new String(str);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return output;

	}


	/**
	 * 获取手机型号
	 * 
	 * @return 型号
	 */
	public static String getModel() {
		String model = null;
		try {
			model = URLEncoder.encode(Build.MODEL, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return model;

	}

	/**
	 * 获取手机Mac地址，并进行MD5加密，用于判断deviceid
	 * 
	 * @return
	 */
	public static String getMacMD5() {
		// 获取设备的mac地址
		String macStr = null;
		WifiManager wifi = (WifiManager) UmiwiApplication.getContext()
				.getSystemService(Context.WIFI_SERVICE);
		if (wifi != null) {
			WifiInfo info = wifi.getConnectionInfo();
			if(info != null && info.getMacAddress() != null){
				macStr = CommonHelper.encodeMD5(info.getMacAddress());
			}
		}
		if(macStr != null){
			Settings.put(Settings.KEY_UUID, macStr);
		}else{
			macStr = Settings.getString(Settings.KEY_UUID);
		}
		return macStr;
	}
	
	/**
	 * 截取mac的md5最后一位
	 * @return
	 */
	public static String getMacMD5LastStr() {
		String str = "";
		str = getMacMD5().substring(getMacMD5().length() - 1, getMacMD5().length());
		if (TextUtils.isEmpty(str)) {
			return "9999";
		}
		return str;	
	}


	/** 获取版本号 */
	public static String getVersionName() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = UmiwiApplication.getContext()
				.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(UmiwiApplication
				.getContext().getPackageName(), 0);
		String version = packInfo.versionName;
		return version;
	}

	/** 获取应用的versionCode 用更新次数判断大小 是否升级 */
	public static int versionCode() {
		int verCode = -1;
		try {
			PackageManager packageManager = UmiwiApplication.getContext()
					.getPackageManager();
			verCode = packageManager.getPackageInfo(UmiwiApplication
					.getContext().getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verCode;
	}

	/** 购买链接后添加的机型渠道版本参数 */
	public static String getChannelModelViesion() {
		String channel_model_viesion = null;
		try {
			channel_model_viesion = "&mid=2&ext1="
					+ CommonHelper.getModel()
					+ "&ref=a-"
					+ ManifestUtils.getChannelString(UmiwiApplication.getContext())
					+ "&channel="
					+ ManifestUtils.getChannelString(UmiwiApplication.getContext()) + "&version="
					+ CommonHelper.getVersionName();
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return channel_model_viesion;
	}

	/** 检查是否有网络 */
	public static boolean checkNetWifi(Context context) {
		try {
			// 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
			ConnectivityManager connecttivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connecttivity != null) {
				// 获取网络连接管理对象
				NetworkInfo info = connecttivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	/**
	 * 删除文件夹里面的所有文件
	 * 
	 * @param path
	 *            String 文件夹路径 如 c:/fqf
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
			}
		}
	}
	
	// 将返回的分数折算成评分
	public static float convertScore2Rating(String scoreStr) {
		float scoreTemp = 0;
	
		if(TextUtils.isEmpty(scoreStr)) 
			scoreTemp = 0;
		try{
			scoreTemp = Float.parseFloat(scoreStr);
		} catch (NumberFormatException e) {
			scoreTemp = 0;
		}
		
		BigDecimal b = new BigDecimal(scoreTemp / 20.0f);
		float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue(); 
		
		return f1;
	}
	
}
