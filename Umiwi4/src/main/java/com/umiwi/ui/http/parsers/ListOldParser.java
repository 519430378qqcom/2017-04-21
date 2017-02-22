package com.umiwi.ui.http.parsers;


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
import cn.youmi.framework.http.parsers.ListResult;

public class ListOldParser<E> implements Parser<ListResult<E>, String>,
        ParserTarget {

    static Gson gson = new Gson();
    private Class<?> mTargetClass;

    @Override
    public ListResult<E> parse(AbstractRequest<ListResult<E>> request,
                               String json) throws Exception {
        LogUtils.e("ListOldParser", "url:" + request.getURL());
        LogUtils.e("ListOldParser", "json:" + json);
        JsonObject rootObj = new JsonParser().parse(json).getAsJsonObject();

        ListResult<E> result = new ListResult<E>();

        if (rootObj.get("curr_page") != null) {
            int currentPage = rootObj.get("curr_page").getAsInt();
            result.setCurrentPage(currentPage);
        }
        if (rootObj.get("pages") != null) {
            int totalPage = rootObj.get("pages").getAsInt();
            result.setToalPage(totalPage);
        }
        if (rootObj.get("totals") != null) {
            int totalRows = rootObj.get("totals").getAsInt();
            result.setRows(totalRows);
        }
        if (rootObj.get("total") != null) {
            int total = rootObj.get("total").getAsInt();
            result.setRow(total);
        }

        JsonArray records = rootObj.get("record").getAsJsonArray();

        if (records.isJsonNull()) {
            return null;
        }
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
