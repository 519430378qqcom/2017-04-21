package com.umiwi.ui.http.parsers;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;
import cn.youmi.framework.util.SingletonFactory;

import com.google.gson.Gson;
import com.umiwi.ui.beans.HomeADBeans;
import com.umiwi.ui.beans.HomeADBeans.HomeADBeansRequestData;

/**
 * @author tangxiyong
 * @version 2014年5月29日 下午6:53:37
 * TODO 首页广告弹窗
 */
public class ADParser implements Parser<HomeADBeans.HomeADBeansRequestData, String>{

	@Override
	public HomeADBeansRequestData parse(
			AbstractRequest<HomeADBeansRequestData> request, String is) {
		return SingletonFactory.getInstance(Gson.class).fromJson(is, HomeADBeans.HomeADBeansRequestData.class);
	}

}
