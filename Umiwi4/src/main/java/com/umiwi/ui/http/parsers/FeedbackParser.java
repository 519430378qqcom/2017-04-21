package com.umiwi.ui.http.parsers;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;

public class FeedbackParser implements Parser<String, String> {

	@Override
	public String parse(AbstractRequest<String> request, String is) {
		return is;
	}

}
