package com.umiwi.ui.http.parsers;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;
import cn.youmi.framework.util.SingletonFactory;

import com.google.gson.Gson;
import com.umiwi.ui.beans.SuggestBeans;

/**
 * @author tjie00
 * @version 2014年6月9日 
 * TODO 搜索建议词
 */
public class SearchSuggestParser implements Parser<SuggestBeans, String>{

	@Override
	public SuggestBeans parse(
			AbstractRequest<SuggestBeans> request, String is) {
		return SingletonFactory.getInstance(Gson.class).fromJson(is, SuggestBeans.class);
	}

}
