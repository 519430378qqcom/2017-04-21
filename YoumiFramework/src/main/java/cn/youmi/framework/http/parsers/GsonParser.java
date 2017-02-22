package cn.youmi.framework.http.parsers;

import com.google.gson.Gson;

import cn.youmi.framework.debug.LogUtils;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;
import cn.youmi.framework.http.ParserTarget;
import cn.youmi.framework.util.SingletonFactory;

public class GsonParser<T> implements Parser<T, String>,ParserTarget {
	private Class<?> targetClass;
	
	private static Gson gson = new Gson();
	@Override
	public void setTargetClass(Class<?> tc){
		targetClass = tc;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T parse(AbstractRequest<T> request, String json) {
		T res = (T) SingletonFactory.getInstance(Gson.class).fromJson(json, targetClass);
		LogUtils.e("json:", json);
		return res;
	}

	@Override
	public Class<?> getTargetClass() {
		return null;
	}
}
