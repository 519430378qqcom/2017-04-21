package com.umiwi.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.LiveDetailsBean;
import com.umiwi.ui.main.UmiwiAPI;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

import static com.umiwi.ui.activity.UmiwiDetailActivity.activity;
import static com.umiwi.ui.fragment.audiolive.LiveDetailsFragment.DETAILS_ID;

public class LiveRoomActivity extends Activity {
    private String roomId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room);
        loginNIM();
        initData();
    }
    public static void start(Activity activity, String detailsId){
        Intent intent = new Intent(activity, LiveRoomActivity.class);
        intent.putExtra(DETAILS_ID,detailsId);
        activity.startActivity(intent);
    }

    /**
     * 登录网易云信服务端
     */
    private void loginNIM() {

    }
    private void initData() {
        String id = activity.getIntent().getStringExtra(DETAILS_ID);
        GetRequest<LiveDetailsBean> request = new GetRequest<>(
                UmiwiAPI.LIVE_DETAILS+id, GsonParser.class, LiveDetailsBean.class, new AbstractRequest.Listener<LiveDetailsBean>() {
            @Override
            public void onResult(AbstractRequest<LiveDetailsBean> request, LiveDetailsBean liveDetailsBean) {
                if(liveDetailsBean !=null) {
                }
            }

            @Override
            public void onError(AbstractRequest<LiveDetailsBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }
}
