package com.umiwi.ui.main;

import cn.youmi.framework.main.ConstantProvider;
import cn.youmi.framework.util.SingletonFactory;

public class YoumiConstantProvider extends ConstantProvider {

	public static YoumiConstantProvider getInstance(){
		return SingletonFactory.getInstance(YoumiConstantProvider.class);
	}
	
	@Override
	public String setCookieOrigin() {
		return "youmi.cn";
	}

}
