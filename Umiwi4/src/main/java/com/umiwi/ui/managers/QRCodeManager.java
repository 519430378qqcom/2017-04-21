package com.umiwi.ui.managers;

import cn.youmi.framework.manager.ModelManager;
import cn.youmi.framework.manager.ResultEvent;
import cn.youmi.framework.util.SingletonFactory;

/**
 * Created by umiwi on 15/7/27.
 */
public class QRCodeManager extends ModelManager<ResultEvent, String> {

    public static QRCodeManager getInstance() {
        return SingletonFactory.getInstance(QRCodeManager.class);
    }

    public void setQRString(String scanResult) {
        for (ModelManager.ModelStatusListener<ResultEvent, String> l : listeners) {
            l.onModelUpdate(ResultEvent.QR_CODE, scanResult);
        }
    }
}
