package com.umiwi.ui.http.parsers.newhttpparsers;

import android.app.Application;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.umiwi.ui.beans.UmiwiListBeans.ChartsListRequestData;
import com.umiwi.ui.beans.updatebeans.NewFree;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.parsers.HomeRecommendResult;
import com.umiwi.ui.parsers.newparsers.NewFreeResult;

import java.util.ArrayList;

import cn.youmi.framework.debug.LogUtils;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;
import cn.youmi.framework.http.ParserTarget;
import cn.youmi.framework.util.SingletonFactory;

/**
 * @author lvdabing
 * @version 2017年2月17日 下午2:32:50
 *
 */
public class NewFreeParser<E> implements Parser<NewFreeResult<E>, String> ,ParserTarget{

	static Gson gson = new Gson();
	private Class<?> mTargetClass;

	@Override
	public NewFreeResult<E> parse(AbstractRequest<NewFreeResult<E>> request, String json) throws Exception {
		LogUtils.i("url:" + request.getURL());
		LogUtils.i("json:" + json);
		JsonObject data = new JsonParser().parse(json).getAsJsonObject();

		NewFreeResult<E> result = new NewFreeResult<E>();

		JsonObject r = data.get("r").getAsJsonObject();

		JsonArray records = r.get("record").getAsJsonArray();


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
