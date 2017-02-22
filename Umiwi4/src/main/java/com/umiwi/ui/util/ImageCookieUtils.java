package com.umiwi.ui.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.umiwi.ui.main.UmiwiApplication;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import cn.youmi.framework.http.CookieDao;
import cn.youmi.framework.main.ConstantProvider;
import cn.youmi.framework.util.SingletonFactory;

/**
 * @author tangxiyong
 * @version 2015-4-24 下午4:16:56 
 */
public class ImageCookieUtils {
	
	public static ImageCookieUtils getInstance(){
		return SingletonFactory.getInstance(ImageCookieUtils.class);
	}
	
	/** 图片带cookie*/
    public void getImagecode(String codeImageUrl, final ImageView imageView, Response.Listener<Bitmap> responseListener, ErrorListener errorListener) {
		RequestQueue requestQueue = Volley.newRequestQueue(UmiwiApplication.getContext());
		ImageRequest imgRequest = new ImageRequest(codeImageUrl, responseListener, 203, 93, Config.ARGB_8888, errorListener) {
			@Override
			protected Response<Bitmap> parseNetworkResponse(
					NetworkResponse response) {
				try {
					Bitmap bitmap = BytesToBimap(response.data);
					CookieSpec cookiespec = new BrowserCompatSpec();
					CookieOrigin origin = new CookieOrigin(ConstantProvider.getInstance().setCookieOrigin(), 80, "/", false);
					List<Cookie> cookies = new ArrayList<Cookie>();
					Set<String> headerkeys = response.headers.keySet();
					for (Iterator<String> it = headerkeys.iterator(); it.hasNext();) {
						String headerkey = it.next();

						if (headerkey.toLowerCase(Locale.ENGLISH).contains(
								"set-cookie")) {
							Header header = new BasicHeader("Set-Cookie", response.headers.get(headerkey));
							try {
								cookies = cookiespec.parse(header, origin);
								if (!cookies.isEmpty()) {
									CookieDao cookiedao = CookieDao.getInstance(UmiwiApplication.getApplication());
									cookiedao.saveCookies((ArrayList<Cookie>) cookies);
								}
							} catch (Exception e) {

							}
						}
					}
					return Response.success(bitmap, HttpHeaderParser.parseCacheHeaders(response));
				} catch (Exception e) {
					return Response.error(new ParseError(e));
				}
			}
		};
		requestQueue.add(imgRequest);
		
	}
    
    public static Bitmap BytesToBimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}
}
