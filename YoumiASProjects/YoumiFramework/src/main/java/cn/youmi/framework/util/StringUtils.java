package cn.youmi.framework.util;

import java.util.ArrayList;
import java.util.Arrays;

import android.text.TextUtils;

public class StringUtils {
	public static String arrayListToCSVString(ArrayList<String> sList) {
		if (sList == null || sList.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < sList.size() - 1; i++) {
			sb.append(sList.get(i));
			sb.append(",");
		}
		sb.append(sList.get(sList.size() - 1));
		return sb.toString();
	}
	
	public static ArrayList<String> csvStringToArrayList(String csv){
		if(TextUtils.isEmpty(csv)){
			return null;
		}
		String[] tagArray = csv.split(",");
		ArrayList<String> sList = new ArrayList<String>(Arrays.asList(tagArray));
		return sList;
	}
}
