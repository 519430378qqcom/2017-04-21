package cn.youmi.framework.http;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

public abstract class AbstractRequest<T> {

	private static final int KEY_NOTIFY_PROGRESS = 100;
	
	private static Handler sHandler = new Handler(Looper.getMainLooper()){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case KEY_NOTIFY_PROGRESS:
				AbstractRequest<?> req = (AbstractRequest<?>) msg.obj;
				req.mProgressListener.onProgressChange(req,req.current, req.total);
				break;
			}
		}
	};

	public int mId;
	public void setId(int id) {
		this.mId = id;
	}

	public int getId() {
		return mId;
	}

	private Bundle mExtras;

	public Bundle getExtras() {
		return mExtras;
	}

	
	private Throwable mException;
	
	
	
	public Throwable getException() {
		return mException;
	}

	public void setException(Throwable exception) {
		this.mException = exception;
	}

	public void setExtras(Bundle b) {
		this.mExtras = b;
	}

	private HashMap<String, String> params = new HashMap<String, String>();

	public void addParam(String key, String value) {
		params.put(key, value);
//		params.put("Content-Type","application/x-www-form-urlencoded");
	}


	
	public void addParams(Map<String, String> param) {
		for (String key : param.keySet()) {
			String value = param.get(key);
			params.put(key, value);
		}
//		params.put("Content-Type","application/x-www-form-urlencoded");
	}

	public HashMap<String, String> getParams() {
		return params;
	}

	public interface Listener<T> {
		void onResult(AbstractRequest<T> request, T t);

		void onError(AbstractRequest<T> requet, int statusCode,
					 String body);
	}
	
	public interface ProgressListener<T> {
		void onProgressChange(AbstractRequest<?> req, long current, long total);
	}

	// T parsed object type ie:Model
	// I inputType for parsing ,String,InputStream,JSONObject ..
	public interface Parser<T, I> {
		T parse(AbstractRequest<T> request, I is) throws Exception;
	}

	public abstract String getMethod();

	private String mURL;
	private Class<? extends Parser<T, Class<?>>> mParserClass;
	private Parser<?, ?> mParser;
	private Listener<T> mListener;
	private ProgressListener<T> mProgressListener;
	private Object mTag;
	private HashMap<String, String> mHeaders;

	@SuppressWarnings("unchecked")
	public AbstractRequest(String url, Class<?> parserClass,
			Listener<T> listener) {
		if(TextUtils.isEmpty(url)){
			throw new IllegalArgumentException("url cannot be null");
		}
		
//		this.mURL = url + CommonHelper.getChannelModelViesion();
		this.mURL = url;
		// TODO
		this.mParserClass = (Class<? extends Parser<T, Class<?>>>) parserClass;
		this.mListener = listener;
	}

	public AbstractRequest(String url, Class<? extends ParserTarget> parserClass,
			Class<?> modelClass, Listener<T> listener) {
		this(url, parserClass, listener);
//		GsonParser<T> gParser = new GsonParser<T>();
		ParserTarget pt;
		try {
			pt = parserClass.newInstance();
			pt.setTargetClass(modelClass);
			this.mParser = (Parser<?, ?>) pt;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void setProgressListener(ProgressListener<T> l){
		this.mProgressListener = l;
	}
	
	public void setTag(Object tag) {
		this.mTag = tag;
	}

	public Object getTag() {
		return mTag;
	}

	public Parser<?, ?> getParser() {
		return mParser;
	}

	public String getURL() {
		return mURL;
	}

	public Class<? extends Parser<T, Class<?>>> getParserClass() {
		return mParserClass;
	}

	public Listener<T> getListener() {
		return mListener;
	}

	public void setHeader(String key, String value) {
		if (mHeaders == null) {
			mHeaders = new HashMap<String, String>();
		}
		mHeaders.put(key, value);
	}

	public HashMap<String, String> getHeaders() {
		return mHeaders;
	}

	public String getHeader(String key) {
		if (mHeaders == null) {
			return null;
		}
		return mHeaders.get(key);
	}
	
	HashMap<String,String> filesMap = new HashMap<String, String>();
	public void addFile(String name,String filePath){
		filesMap.put(name, filePath);
	}
	
	public HashMap<String, String> getFilesMap(){
		return filesMap;
	}
	
	public void go(){
		HttpDispatcher.getInstance().go(this);
	}
	
	
	private long current;
	private long total;
	
	public void setTogal(long total){
		this.total = total;
	}
	
	public void notifyProgress(int delta){
		current += delta;
		if(mProgressListener == null){
			return;
		}
		Message msg = sHandler.obtainMessage(KEY_NOTIFY_PROGRESS);
		msg.obj = this;
		sHandler.sendMessage(msg);
	}
	
	private AtomicBoolean mCanceld = new AtomicBoolean(false);
	public void cancel(){
		mCanceld.set(true);
	}
	
	public boolean isCanceled(){
		return mCanceld.get();
	}
}
