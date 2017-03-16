package com.umiwi.ui.fragment.home.alreadyshopping;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.updateadapter.BuyVideoAdapter;
import com.umiwi.ui.beans.ActivityBean;
import com.umiwi.ui.beans.updatebeans.AlreadyVideoBean;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
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
    private int page = 1;
    private boolean isla = false;
    private boolean isLoad = false;;
    private ArrayList<AlreadyVideoBean.RalreadyVideo.RecordInfo> buyVideoInfos = new ArrayList<>();
    private int totalpage;
    private BuyVideoAdapter buyVideoAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_layout_new, null);

        listview = (ListView) view.findViewById(R.id.listview);
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        buyVideoAdapter = new BuyVideoAdapter(getActivity());
        buyVideoAdapter.setData(buyVideoInfos);
        listview.setAdapter(buyVideoAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = buyVideoInfos.get(i).getId();
                String hrefurl = UmiwiAPI.VODEI_URL +id;
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
                intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, hrefurl);
                startActivity(intent);
            }
        });
        initrefreshLayout();
        getInfos();
        return view;
    }

    private void initrefreshLayout() {
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.main_color));
        refreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                listview.setEnabled(false);
                isLoad = true;
                page++;
                if (page<=totalpage){
                     getInfos();
                }else{
                    refreshLayout.setLoading(false);
                }
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isla = true;
                buyVideoInfos.clear();
                page = 1;
                getInfos();
            }
        });
    }

    private void getInfos() {
        String url = UmiwiAPI.ALREADY_VIDEO+"?p="+page;
        GetRequest<AlreadyVideoBean> req = new GetRequest<AlreadyVideoBean>(url, GsonParser.class, AlreadyVideoBean.class, new AbstractRequest.Listener<AlreadyVideoBean>() {
            @Override
            public void onResult(AbstractRequest<AlreadyVideoBean> request, AlreadyVideoBean alreadyVideoBean) {
                AlreadyVideoBean.RalreadyVideo r = alreadyVideoBean.getR();
                AlreadyVideoBean.RalreadyVideo.PageBean page = r.getPage();
                totalpage = page.getTotalpage();
                ArrayList<AlreadyVideoBean.RalreadyVideo.RecordInfo> record = r.getRecord();
                buyVideoInfos.addAll(record);
                buyVideoAdapter.setData(buyVideoInfos);
                if (isla){
                    listview.setEnabled(true);
                    refreshLayout.setRefreshing(false);
                    isla = false;
                }else if (isLoad){
                    refreshLayout.setLoading(false);
                    isLoad = false;
                }
            }

             @Override
            public void onError(AbstractRequest<AlreadyVideoBean> requet, int statusCode, String body) {

            }
        });

        req.go();
    }

}