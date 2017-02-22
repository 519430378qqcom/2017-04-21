package com.umiwi.ui.http.parsers;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;
import cn.youmi.framework.util.SingletonFactory;

import com.google.gson.Gson;
import com.umiwi.ui.beans.FeedbackResultBean;

public class SearchFeedbackParser implements Parser<FeedbackResultBean.FeedbackResultRequestData, String> {

	@Override
	public FeedbackResultBean.FeedbackResultRequestData parse(AbstractRequest<FeedbackResultBean.FeedbackResultRequestData> request, String is) {
		return SingletonFactory.getInstance(Gson.class).fromJson(is, FeedbackResultBean.FeedbackResultRequestData.class);
	}

}