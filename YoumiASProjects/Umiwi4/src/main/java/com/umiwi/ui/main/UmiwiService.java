package com.umiwi.ui.main;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 逻辑服务层,所有activities的任务都在这里来处理
 * 
 */
public class UmiwiService extends Service  {
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
