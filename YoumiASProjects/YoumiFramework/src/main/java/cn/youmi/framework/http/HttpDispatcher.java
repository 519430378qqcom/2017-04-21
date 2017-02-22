package cn.youmi.framework.http;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Looper;

import cn.youmi.framework.http.AbstractRequest.Parser;
import cn.youmi.framework.http.okhttp.OkHttpStack;
import cn.youmi.framework.http.volley.HeaderedStringRequest;
import cn.youmi.framework.util.CacheUtils;

import com.android.volley.Cache;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.OkHttpClient;

public class HttpDispatcher {
	private static HttpDispatcher _instance;
	private RequestQueue mRequestQueue;
	private ExecutorService mThreadPool = Executors.newCachedThreadPool();
	private Handler mHandler = new Handler(Looper.getMainLooper());
	
	private DispatcherObserver mDispatcherObserver;

	protected Handler getHandler(){
		return mHandler;
	}

	public static HttpDispatcher getInstance() {
		if (_instance == null) {
			_instance = new HttpDispatcher();
		}
		return _instance;
	}
	//@Nullable rootDirectory
	private static Cache openCache() {
		return new DiskBasedCache(CacheUtils.getDiskCacheDir(),
				10 * 1024 * 1024);
	}

	HttpDispatcher() {
//		mRequestQueue = new RequestQueue(openCache(), new BasicNetwork(
//				new MHurlStack()));
		mRequestQueue = new RequestQueue(openCache(), new BasicNetwork(new OkHttpStack(new OkHttpClient())));
//		mRequestQueue = Volley.newRequestQueue(ContextProvider.getApplication(), new OkHttpStack(new OkHttpClient()));
		mRequestQueue.start();
	}


	protected ExecutorService getTheradPool(){
		return mThreadPool;
	}

	private HashMap<Class<? extends Parser<?, ?>>, Object> mParserCache = new HashMap<Class<? extends Parser<?, ?>>, Object>();

	@SuppressWarnings("unchecked")
	protected <T, I> Parser<T, I> getParserForRequest(AbstractRequest<T> r,
			Class<I> clz) {
		if (r.getParser() != null) {
			return (Parser<T, I>) r.getParser();
		}
		Parser<T, I> p = (Parser<T, I>) mParserCache.get(r.getParserClass());
		if (p == null) {
			try {
				p = (Parser<T, I>) r.getParserClass().newInstance();
				mParserCache.put(r.getParserClass(), p);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return p;
	}

	private <T> void dispatchInputTypeByteArray(
			final AbstractRequest<T> request, final Type parserInputType) {

	}

	private <T> void dispatchInputTypeInputStream(final AbstractRequest<T> req) {
		mThreadPool.submit(new Runnable() {
			@Override
			public void run() {
				URL url;
				try {
					url = new URL(req.getURL());
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod(req.getMethod());

					conn.connect();
					InputStream is = conn.getInputStream();
					Parser<T, InputStream> parser = getParserForRequest(req,
							InputStream.class);
					T result = null;
					try {
						result = parser.parse(req, is);
					} catch (Exception e) {
						e.printStackTrace();
					}
					is.close();
					final T resultCopy = result;
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							req.getListener().onResult(req, resultCopy);
						}
					});
				} catch (final MalformedURLException e) {
					e.printStackTrace();
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							req.getListener().onError(req, -1, e.toString());

						}
					});
				} catch (final IOException e) {
					e.printStackTrace();
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							req.getListener().onError(req, -1, e.toString());

						}
					});
				}
			}
		});
	}

	private <T> void dispatchInputTypeString(final AbstractRequest<T> request,
			final Type parserInputType) {
		final long start = System.currentTimeMillis();
		Listener<String> responseListener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				T result = null;
				Exception exeption = null;
				if (parserInputType == String.class) {
					Parser<T, String> parser = getParserForRequest(request,
							String.class);
					try {
						result = parser.parse(request, response);
					} catch (JsonSyntaxException jse) {
						exeption = jse;
						request.setException(jse);
						jse.printStackTrace();
					} catch(Exception e){
						exeption = e;
						e.printStackTrace();
					}

				} else if (parserInputType == JSONObject.class) {
					Parser<T, JSONObject> parser = getParserForRequest(request,
							JSONObject.class);
					JSONObject jobj;
					try {
						jobj = new JSONObject(response);
						result = parser.parse(request, jobj);
					} catch (Exception e) {
						exeption = e;
						e.printStackTrace();
					} 
				} else if (parserInputType == JSONArray.class) {
					Parser<T, JSONArray> parser = getParserForRequest(request,
							JSONArray.class);
					JSONArray jArray;
					try {
						jArray = new JSONArray(response);
						result = parser.parse(request, jArray);
					} catch (JSONException e) {
						exeption = e;
						e.printStackTrace();
					} catch (JsonSyntaxException jse) {
						exeption = jse;
						jse.printStackTrace();
					} catch (Exception e) {
						exeption = e;
						e.printStackTrace();
					}
				}
				long timeUsed = System.currentTimeMillis() - start;
				long delay = 400 - timeUsed;
				if (delay < 0) {
					delay = 0;
				}
				delay = Math.min(delay, 300);

				final T resultCopy = result;
				final Exception exceptionCopy = exeption;
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						if (exceptionCopy != null) {
							request.setException(exceptionCopy);
							request.getListener().onError(request, -1,
									exceptionCopy.toString());
						} else {
							try {//TODO null
								request.getListener().onResult(request, resultCopy);
							} catch (NullPointerException e){
								e.printStackTrace();
							}

						}

					}
				}, delay);
				// request.getListener().onResult(request, result);
			}
		};

		int method = 0;
		if (request.getMethod().equals("GET")) {
			method = Method.GET;
		} else if (request.getMethod().equals("POST")) {
			method = Method.POST;
		} else if (request.getMethod().equals("PUT")) {
			method = Method.PUT;
		} else if (request.getMethod().equals("DELETE")) {
			method = Method.DELETE;
		}
		HeaderedStringRequest volleyRequest = new HeaderedStringRequest(method,
				request.getURL(), responseListener, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError response) {
						String body = null;
						int code = -1;
						if (response.networkResponse != null
								&& response.networkResponse.data != null) {
							new String(response.networkResponse.data);
							code = response.networkResponse.statusCode;
						}
						request.getListener().onError(request, code, body);
					}
				});
		volleyRequest.setParams(request.getParams());
		
		volleyRequest.setShouldCache(true);
		volleyRequest.setHeaders(request.getHeaders());
		
		mRequestQueue.add(volleyRequest);
	}

	public <T> void go(final AbstractRequest<T> request) {
		if (request.getURL() == null) {
			request.getListener().onError(request, -1, null);
			return;
		}
		if(mDispatcherObserver != null){
			mDispatcherObserver.willDispatch(request);
		}
		Class<? extends Parser<T, Class<?>>> pp = request.getParserClass();
		Type[] types = pp.getGenericInterfaces();
		Class<?> parserInputType = null;
		for (Type t : types) {
			if (t instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) t;
				if (pt.getRawType() == Parser.class) {
					parserInputType = (Class<?>) pt.getActualTypeArguments()[1];
				}
			}
		}
		if (parserInputType == String.class
				|| parserInputType == JSONObject.class
				|| parserInputType == JSONArray.class) {
			dispatchInputTypeString(request, parserInputType);
		} else if (parserInputType == byte[].class) {
			dispatchInputTypeByteArray(request, parserInputType);
		} else if (parserInputType == InputStream.class) {
			dispatchInputTypeInputStream(request);
		}
	}
	
	public void registerDispatcherObserver(DispatcherObserver observer){
		this.mDispatcherObserver = observer;
	}

	public void cancelAllRequest(String tag) {
		if (mRequestQueue != null)
		{
			mRequestQueue.cancelAll(tag);
		}
	}

}
