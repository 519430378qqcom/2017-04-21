package com.umiwi.ui.managers;

import cn.youmi.account.manager.UserManager;
import cn.youmi.framework.util.SingletonFactory;

import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.util.CommonHelper;

public class YoumiRoomUserManager extends UserManager {

	public static YoumiRoomUserManager getInstance() {
		return SingletonFactory.getInstance(YoumiRoomUserManager.class);
	}

	@Override
	public boolean isConsult() {
		return false;
	}

	@Override
	public String getUserInfoUrl() {
		return UmiwiAPI.UMIWI_USER_INFO + CommonHelper.getChannelModelViesion();
	}

	@Override
	public String getLogoutUrl() {
		return UmiwiAPI.UMIWI_USER_LOGOUT;
	}

	@Override
	public String getMethod() {
		return "POST";
//		return "GET";
	}
}
