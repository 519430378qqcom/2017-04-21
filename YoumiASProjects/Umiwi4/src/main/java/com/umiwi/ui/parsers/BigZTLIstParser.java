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

public class BigZTLIstParser<E> implements Parser<BigZTListResult<E>, String>, ParserTarget {

	static Gson gson = new Gson();
	private Class<?> mTargetClass;

	@Override
	public BigZTListResult<E> parse(AbstractRequest<BigZTListResult<E>> request, String json) throws Exception {
		LogUtils.e("xx", "url:" + request.getURL());
		LogUtils.e("xx", "json:" + json);
		JsonObject data = new JsonParser().parse(json).getAsJsonObject();

		BigZTListResult<E> result = new BigZTListResult<E>();
		
		result.setId(data.get("id") != null ? data.get("id").getAsInt() : 0);
		result.setTitle(data.get("title") != null ? data.get("title").getAsString() : " ");
		result.setIntroduce(data.get("introduce") != null ? data.get("introduce").getAsString() : " ");
		result.setBuy(data.get("isbuy") != null ? data.get("isbuy").getAsBoolean() : false);
		result.setPrice(data.get("price") != null ? data.get("price").getAsInt() : 0);
		result.setRawPrice(data.get("raw_price") != null ? data.get("raw_price").getAsInt() : 0);
		result.setDiscountPrice(data.get("discount_price") != null ? data.get("discount_price").getAsInt() : 0);
		result.setSectionId(data.get("sectionid") != null ? data.get("sectionid").getAsInt() : 0);
		result.setImage(data.get("image") != null ? data.get("image").getAsString() : " ");
		result.setImageTwo(data.get("image2") != null ? data.get("image2").getAsString() : " ");

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
