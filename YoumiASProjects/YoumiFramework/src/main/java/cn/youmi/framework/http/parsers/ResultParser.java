package cn.youmi.framework.http.parsers;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;
import cn.youmi.framework.model.ResultModel;
import cn.youmi.framework.util.SingletonFactory;

import com.google.gson.Gson;

public class ResultParser implements Parser<ResultModel, String> {
	@Override
	public ResultModel parse(AbstractRequest<ResultModel> request, String json) {
		ResultModel user = SingletonFactory.getInstance(Gson.class).fromJson(
				json, ResultModel.class);
		return user;
	}
}
