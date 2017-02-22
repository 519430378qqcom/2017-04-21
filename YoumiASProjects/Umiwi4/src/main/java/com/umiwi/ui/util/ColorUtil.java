package com.umiwi.ui.util;

import android.graphics.Color;
import android.text.TextUtils;

/**
 * @author tjie00
 * @version 2014年6月18日 下午1:56:09
 * 颜色工具类
 */
public class ColorUtil {
	private ColorUtil() {
	}
	
	public static int transferFromString(String colorStr) {
		if(TextUtils.isEmpty(colorStr)) {
			return -1;
		} 
		
		if(colorStr.startsWith("0x")) {
			colorStr = colorStr.substring(2);
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(colorStr.charAt(0));
        sb.append(colorStr.charAt(1));
		
        int r = Integer.parseInt(sb.toString(), 16);
        sb.setLength(0);
        sb.append(colorStr.charAt(2));
        sb.append(colorStr.charAt(3));
        int g = Integer.parseInt(sb.toString(), 16);
        sb.setLength(0);
        sb.append(colorStr.charAt(4));
        sb.append(colorStr.charAt(5));
        int b = Integer.parseInt(sb.toString(), 16);
        
        int color = Color.rgb(r, g, b);
        
        return color;
	}
}
