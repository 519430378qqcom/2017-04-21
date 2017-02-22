package com.umiwi.ui.camera;

import cn.youmi.framework.manager.ModelManager;
import cn.youmi.framework.util.SingletonFactory;

public class CameraManager extends ModelManager<CameraResultEvent, String> {

	public static CameraManager getInstance() {
		return SingletonFactory.getInstance(CameraManager.class);
	}

	public void setCameraResult(CameraResultEvent cameraResultEvent) {
		for (ModelStatusListener<CameraResultEvent, String> l : listeners) {
			l.onModelUpdate(cameraResultEvent, " ");
		}
	}

	public void setCameraResult(CameraResultEvent cameraResultEvent, String str) {
		for (ModelStatusListener<CameraResultEvent, String> l : listeners) {
			l.onModelUpdate(cameraResultEvent, str);
		}
	}

}
