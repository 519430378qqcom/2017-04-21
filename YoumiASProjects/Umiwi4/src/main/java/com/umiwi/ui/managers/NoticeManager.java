package com.umiwi.ui.managers;

import com.umiwi.ui.event.NoticeEvent;
import com.umiwi.ui.model.NoticeModel;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.ModelParser;
import cn.youmi.framework.manager.ModelManager;
import cn.youmi.framework.util.SingletonFactory;

/**
 * Created by txy on 15/11/4.
 */
public class NoticeManager extends ModelManager<NoticeEvent, NoticeModel> {

    public static NoticeManager getInstance() {
        return SingletonFactory.getInstance(NoticeManager.class);
    }

    public void setNoticeResult(NoticeEvent event, String result) {
        for (ModelManager.ModelStatusListener<NoticeEvent, NoticeModel> l : listeners) {
            l.onModelUpdate(event, null);
        }
    }

    public void loadNotice() {
        GetRequest<NoticeModel> get = new GetRequest<NoticeModel>("http://i.v.youmi.cn/ClientApi/mynotice", ModelParser.class, NoticeModel.class, noticeListener);
        get.go();
    }

    public void loadNoticeLoginOut(){
        NoticeModel noticeModel = new NoticeModel();
        noticeModel.setActivity("0");
        noticeModel.setCoin_goods("0");
        noticeModel.setCoupon("0");
        noticeModel.setMessage("0");
        noticeModel.setShake("0");
        for (ModelManager.ModelStatusListener<NoticeEvent, NoticeModel> l : listeners) {
            l.onModelUpdate(NoticeEvent.ALL, noticeModel);
        }
    }

    private Listener<NoticeModel> noticeListener = new Listener<NoticeModel>() {
        @Override
        public void onResult(AbstractRequest<NoticeModel> request, NoticeModel noticeModel) {
            for (ModelManager.ModelStatusListener<NoticeEvent, NoticeModel> l : listeners) {
                l.onModelUpdate(NoticeEvent.ALL, noticeModel);
            }
        }

        @Override
        public void onError(AbstractRequest<NoticeModel> requet, int statusCode, String body) {

        }
    };

}
