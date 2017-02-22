package cn.youmi.framework.http.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.message.BasicHeader;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import cn.youmi.framework.http.CookieDao;
import cn.youmi.framework.http.CookieModel;
import cn.youmi.framework.main.BaseApplication;
import cn.youmi.framework.main.ConstantProvider;
import cn.youmi.framework.manager.UserKillLogoutManager;

public class HeaderedStringRequest extends Request<String> {
	private static final int SOCKET_TIMEOUT = 15 * 1000;
	private static CookieDao cookiedao = CookieDao.getInstance(BaseApplication.getApplication());
	Listener<String> mListener;

	private HashMap<String, String> params = null;

	public void setParams(HashMap<String, String> ps) {
		this.params = ps;
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		// HashMap<String,String> newParams = new HashMap<String, String>();
		return params;
	}

	public HeaderedStringRequest(int method, String url, ErrorListener listener) {
		super(method, url, listener);
	}

	public HeaderedStringRequest(int method, String url,
			Listener<String> listener, ErrorListener el) {
		this(method, url, el);
		this.mListener = listener;
	}

	private HashMap<String, String> mHeaders = new HashMap<String, String>();

	public void setHeaders(HashMap<String, String> headers) {
		String cookiestr = getCoookieFromDB();
		HashMap<String, String> myHeaders = headers;
		if (myHeaders == null) {
			myHeaders = new HashMap<String, String>();
		}
		if (myHeaders.get("COOKIE") != null) {
			cookiestr = cookiestr + myHeaders.get("COOKIE");
		}
		myHeaders.put("COOKIE", cookiestr);
		for (String key : myHeaders.keySet()) {
			mHeaders.put(key, myHeaders.get(key));
		}
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return mHeaders;
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			CookieSpec cookiespec = new BrowserCompatSpec();
			CookieOrigin origin = new CookieOrigin(ConstantProvider.getInstance().setCookieOrigin(), 80, "/", false);
			List<Cookie> cookies = new ArrayList<Cookie>();
			Set<String> headerkeys = response.headers.keySet();
			for (Iterator<String> it = headerkeys.iterator(); it.hasNext();) {
				String headerkey = it.next();

				if (headerkey.toLowerCase(Locale.ENGLISH)
						.contains("set-cookie")) {
					Header header = new BasicHeader("Set-Cookie",
							response.headers.get(headerkey));
					try {
						cookies = cookiespec.parse(header, origin);
						if (!cookies.isEmpty()) {
							 cookiedao.saveCookies((ArrayList<Cookie>) cookies);
						}
					} catch (Exception e) {
						// TODO
					}
				}
				if (headerkey.toLowerCase(Locale.ENGLISH).contains("logout")) {
					UserKillLogoutManager.getInstance().setKillMessage("您的账号在其他设备上登录，如非本人操作，请修改密码");
					// UserManager.getInstance().logout("您的账号在其他设备上登录，如非本人操作，请修改密码");
					return Response.error(new VolleyError("logout"));
				}
			}
			return Response.success(json,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}

	@Override
	protected void deliverResponse(String response) {
		this.mListener.onResponse(response);
	}

	/** 从数据库里取cookie */
	public static String getCoookieFromDB() {
		String cookiestr = "";
		List<CookieModel> cookiemodels = cookiedao.listAvailable();
		for (CookieModel cookiemodel : cookiemodels) {
			String name = cookiemodel.getName();
			String value = cookiemodel.getValue();
			cookiestr += name + "=" + value + ";";
		}
		return cookiestr;
	}

	@Override
	public RetryPolicy getRetryPolicy() {
		RetryPolicy retryPolicy = new DefaultRetryPolicy(SOCKET_TIMEOUT, 2,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		return retryPolicy;
	}
}
