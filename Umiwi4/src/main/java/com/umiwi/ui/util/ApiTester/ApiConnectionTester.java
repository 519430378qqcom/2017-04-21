package com.umiwi.ui.util.ApiTester;

import android.util.Log;

import com.umiwi.ui.util.ApiTester.bean.NewFreeActionEntity;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

/**
 * api调试demo
 */
public class ApiConnectionTester {

    private static final String TAG_LOG = "Gpsi";

    /**
     * newfreeAction : 首页－最新免费－换一换（6.6.0ok）
     */
    public static void getNewfreeAction() {
        GetRequest<NewFreeActionEntity> request = new GetRequest<>(
                "http://i.v.youmi.cn/api8/newfree", GsonParser.class, NewFreeActionEntity.class, newFreeActionListener);
        request.go();
    }

    private static AbstractRequest.Listener<NewFreeActionEntity> newFreeActionListener = new AbstractRequest.Listener<NewFreeActionEntity>() {

        @Override
        public void onResult(AbstractRequest<NewFreeActionEntity> request, NewFreeActionEntity t) {
            if (null != t) {
                Log.d(TAG_LOG, "onResult");
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onError(AbstractRequest<NewFreeActionEntity> requet, int statusCode, String body) {
            Log.d(TAG_LOG, "onError：" + " - " + statusCode + " - " + body);
        }
    };
}