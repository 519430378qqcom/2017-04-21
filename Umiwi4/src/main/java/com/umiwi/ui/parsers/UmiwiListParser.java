package com.umiwi.ui.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import cn.youmi.framework.debug.LogUtils;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;
import cn.youmi.framework.http.ParserTarget;

public class UmiwiListParser<E> implements Parser<UmiwiListResult<E>, String>,
		ParserTarget {

	static Gson gson = new Gson();
	private Class<?> mTargetClass;

	@Override
	public UmiwiListResult<E> parse(AbstractRequest<UmiwiListResult<E>> request,
			String json) throws Exception {
		LogUtils.e("xx", "url:" + request.getURL());
		LogUtils.e("xx","json:" + json);
		JsonObject data = new JsonParser().parse(json).getAsJsonObject();

		UmiwiListResult<E> result = new UmiwiListResult<E>();

		if (data.get("curr_page") != null) {
			int currentPage = data.get("curr_page").getAsInt();
			result.setCurrentPage(currentPage);
		}
		if (data.get("pages") != null) {
			int totalPage = data.get("pages").getAsInt();
			result.setToalPage(totalPage);
		}
		if (data.get("totals") != null) {
			int totalRows = data.get("totals").getAsInt();
			result.setTotalRows(totalRows);
		}
		if (data.get("total") != null) {
			int total = data.get("total").getAsInt();
			result.setTotal(total);
		}

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
