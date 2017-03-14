package com.umiwi.ui.fragment.home.alreadyshopping;


import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.ActivityBean;
import com.umiwi.ui.beans.updatebeans.AlreadyVideoBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.view.RefreshLayout;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;


/**
 * Created by LvDabing on 2017/2/16.
 * Email：lvdabing@lvshandian.com
 * Detail:视频
 */

public class NewVideoFragment extends BaseConstantFragment {

    private ListView listview;
    private RefreshLayout refreshLayout;
    private int page;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_layout_new, null);

        listview = (ListView) view.findViewById(R.id.listview);
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        getInfos();

        return view;
    }

    private void getInfos() {
        String url = UmiwiAPI.ALREADY_VIDEO+"?p="+page;
        GetRequest<AlreadyVideoBean> req = new GetRequest<AlreadyVideoBean>(url, GsonParser.class, AlreadyVideoBean.class, new AbstractRequest.Listener<AlreadyVideoBean>() {
            @Override
            public void onResult(AbstractRequest<AlreadyVideoBean> request, AlreadyVideoBean alreadyVideoBean) {
                AlreadyVideoBean.RalreadyVideo r = alreadyVideoBean.getR();
                AlreadyVideoBean.RalreadyVideo.PageBean page = r.getPage();
                ArrayList<AlreadyVideoBean.RalreadyVideo.RecordInfo> record = r.getRecord();

            }

             @Override
            public void onError(AbstractRequest<AlreadyVideoBean> requet, int statusCode, String body) {

            }
        });

        req.go();
    }

}