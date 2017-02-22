package com.umiwi.ui.http.parsers;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;
import cn.youmi.framework.util.SingletonFactory;

import com.google.gson.Gson;
import com.umiwi.ui.beans.UmiwiResultBeans;

public class ResponseParser implements Parser<UmiwiResultBeans.ResultBeansRequestData, String> {

	@Override
	public UmiwiResultBeans.ResultBeansRequestData parse(
			AbstractRequest<UmiwiResultBeans.ResultBeansRequestData> request, String is) {
		return SingletonFactory.getInstance(Gson.class).fromJson(is, UmiwiResultBeans.ResultBeansRequestData.class);
	}
	
}
