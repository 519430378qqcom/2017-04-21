package cn.youmi.framework.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class URLCoderUtils {

	/**
	 * 编码 utf-8
	 * @param str
	 * @return
	 */
	public static String URLEncoder(String str) {
		String encoder = "";
		try {
			encoder = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encoder;
	}
	/**
	 * 解码 utf-8
	 * @param str
	 * @return
	 */
	public static String URLDecoder(String str) {
		String encoder = "";
		try {
			encoder = URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encoder;
	}
	
	

}
