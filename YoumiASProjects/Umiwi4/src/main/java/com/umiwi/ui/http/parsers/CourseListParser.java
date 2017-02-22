package com.umiwi.ui.http.parsers;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;
import cn.youmi.framework.util.SingletonFactory;

import com.google.gson.Gson;
import com.umiwi.ui.beans.UmiwiListBeans;
import com.umiwi.ui.beans.UmiwiListBeans.ChartsListRequestData;

/**
 * @author tangxiong
 * @version 2014年5月29日 下午2:32:50
 * TODO 默认的一级列表，解析：排行榜，会员列表等；
 */
public class CourseListParser implements Parser<UmiwiListBeans.ChartsListRequestData, String> {
	@Override
	public ChartsListRequestData parse(
			AbstractRequest<ChartsListRequestData> request, String is) {
		return SingletonFactory.getInstance(Gson.class).fromJson(is, UmiwiListBeans.ChartsListRequestData.class);
	}
}
