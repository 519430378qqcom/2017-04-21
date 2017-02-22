package com.umiwi.ui.parsers;

import java.util.ArrayList;

import cn.youmi.framework.debug.LogUtils;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;
import cn.youmi.framework.http.ParserTarget;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HomeRecommendParser<E> implements Parser<HomeRecommendResult<E>, String>, ParserTarget {

	static Gson gson = new Gson();
	private Class<?> mTargetClass;

	@Override
	public HomeRecommendResult<E> parse(AbstractRequest<HomeRecommendResult<E>> request, String json) throws Exception {
		LogUtils.e("xx", "url:" + request.getURL());
		LogUtils.e("xx", "json:" + json);
		JsonObject data = new JsonParser().parse(json).getAsJsonObject();

		HomeRecommendResult<E> result = new HomeRecommendResult<E>();

		JsonArray records = data.get("record").getAsJsonArray();

		@SuppressWarnings("unchecked")
		Class<E> c = (Class<E>) getTargetClass();

		ArrayList<E> items = new ArrayList<E>();
		for (int i = 0; i < records.size(); i++) {
			JsonElement je = records.get(i);
			E item = gson.fromJson(je, c);
			items.add(item);
		}

		result.setItems(items);

		return result;
	}

	@Override
	public void setTargetClass(Class<?> tc) {
		mTargetClass = tc;
	}

	@Override
	public Class<?> getTargetClass() {
		return mTargetClass;
	}
}
