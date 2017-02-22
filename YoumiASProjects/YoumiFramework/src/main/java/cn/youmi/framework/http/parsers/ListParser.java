package cn.youmi.framework.http.parsers;

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

public class ListParser<E> implements Parser<ListResult<E>, String>,
		ParserTarget {

	static Gson gson = new Gson();
	private Class<?> mTargetClass;

	@Override
	public ListResult<E> parse(AbstractRequest<ListResult<E>> request,
			String json) throws Exception {
		LogUtils.e("xx", "url:" + request.getURL());
		LogUtils.e("xx","json:" + json);
		JsonObject rootObj = new JsonParser().parse(json).getAsJsonObject();
		String e = rootObj.get("e").getAsString();

		ListResult<E> result = new ListResult<E>();

		JsonObject data = rootObj.get("r").getAsJsonObject();
		if ("9999".equals(e)) {
			if(data.get("page") != null){
				JsonObject page = data.get("page").getAsJsonObject();
				int currentPage = page.get("currentpage").getAsInt();
				int totalPage = page.get("totalpage").getAsInt();
				int rows = page.get("rows").getAsInt();
				result.setCurrentPage(currentPage);
				result.setToalPage(totalPage);
				result.setRows(rows);
			}

			JsonArray records = data.get("records").getAsJsonArray();

			@SuppressWarnings("unchecked")
			Class<E> c = (Class<E>) getTargetClass();

			ArrayList<E> items = new ArrayList<E>();
			for (int i = 0; i < records.size(); i++) {
				JsonElement je = records.get(i);
				E item = gson.fromJson(je, c);
				items.add(item);
			}

			result.setItems(items);

		} else {
			return null;
		}
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
