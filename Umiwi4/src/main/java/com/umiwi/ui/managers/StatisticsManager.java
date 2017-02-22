package com.umiwi.ui.managers;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.ResultParser;
import cn.youmi.framework.manager.ModelManager;
import cn.youmi.framework.model.ResultModel;
import cn.youmi.framework.util.SingletonFactory;

/**
 * e,m,结果处理
 * 
 * @author tangxiyong 2014-5-14下午3:45:21
 * 
 */
public class StatisticsManager extends ModelManager<String, ResultModel> {

	public static StatisticsManager getInstance() {
		return SingletonFactory.getInstance(StatisticsManager.class);
	}

	/**
	 * @param url
	 *            请求链接
	 * @param tag
	 *            请求标识
	 */
	public void getResultInfo(String url, String tag) {
		GetRequest<ResultModel> get = new GetRequest<ResultModel>(url,
				ResultParser.class, userRegisteListener);
		get.setTag(tag);
		HttpDispatcher.getInstance().go(get);
	}
	public void getResultInfo(String spmurl) {
		GetRequest<ResultModel> get = new GetRequest<ResultModel>(spmurl,
				ResultParser.class, userRegisteListener);
		HttpDispatcher.getInstance().go(get);
	}

	private Listener<ResultModel> userRegisteListener = new Listener<ResultModel>() {

		@Override
		public void onResult(AbstractRequest<ResultModel> request, ResultModel t) {
			String key = (String) request.getTag();
			for (ModelManager.ModelStatusListener<String, ResultModel> l : listeners) {
				l.onModelUpdate(key, t);
			}
		}

		@Override
		public void onError(AbstractRequest<ResultModel> requet,
				int statusCode, String body) {
		}
	};

}
