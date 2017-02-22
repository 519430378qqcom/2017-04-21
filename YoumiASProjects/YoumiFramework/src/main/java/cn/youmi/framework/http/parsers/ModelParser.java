package cn.youmi.framework.http.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cn.youmi.framework.debug.LogUtils;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;
import cn.youmi.framework.http.ParserTarget;
import cn.youmi.framework.util.SingletonFactory;

public class ModelParser<E> implements Parser<E, String>,ParserTarget{

	private Class<?> mTarget;
	@Override
	public void setTargetClass(Class<?> tc) {
		mTarget = tc;
	}

	@Override
	public Class<?> getTargetClass() {
		return mTarget;
	}

	@Override
	public E parse(AbstractRequest<E> request, String json) throws Exception {
		JsonObject rootObj = new JsonParser().parse(json).getAsJsonObject();
		String e = rootObj.get("e").getAsString();
		String m = rootObj.get("m").getAsString();
		
//		ListResult<E> result = new ListResult<E>();

		LogUtils.e("model_json", json);
		
		JsonObject data = rootObj.get("r").getAsJsonObject();
		if("9999".equals(e)){
			@SuppressWarnings("unchecked")
			E model = (E) SingletonFactory.getInstance(Gson.class).fromJson(data, mTarget);
			return model;
		}else{
			
		}
		return null;
	}


}
