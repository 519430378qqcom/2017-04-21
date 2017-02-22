package com.umiwi.ui.http.parsers;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;
import cn.youmi.framework.util.SingletonFactory;

import com.google.gson.Gson;
import com.umiwi.ui.beans.SearchBean;

public class SearchResultParser implements Parser<SearchBean.SearchBeanRequestData, String> {

	@Override
	public SearchBean.SearchBeanRequestData parse(AbstractRequest<SearchBean.SearchBeanRequestData> request, String is) {
		return SingletonFactory.getInstance(Gson.class).fromJson(is, SearchBean.SearchBeanRequestData.class);
	}

}