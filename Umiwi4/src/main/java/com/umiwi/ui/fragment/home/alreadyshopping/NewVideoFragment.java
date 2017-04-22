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
import com.umiwi.ui.beans.updatebeans.AlreadyVideoBean;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.view.RefreshLayout;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ToastU;


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
    private boolean isRefresh = true;

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
                page++;
                isRefresh = false;
                if (page <= totalpage) {
                    refreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getInfos();
                        }
                    }, 1000);

                } else {
                    ToastU.showLong(getActivity(), "没有更多了!");
                    refreshLayout.setLoading(false);

                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                page = 1;
                getInfos();
            }
        });
    }

    private void getInfos() {
        String url = UmiwiAPI.ALREADY_VIDEO+"?p="+page;
        Log.e("TAG", "已购视频url=" + url);
        GetRequest<AlreadyVideoBean> req = new GetRequest<AlreadyVideoBean>(url, GsonParser.class, AlreadyVideoBean.class, new AbstractRequest.Listener<AlreadyVideoBean>() {
            @Override
            public void onResult(AbstractRequest<AlreadyVideoBean> request, AlreadyVideoBean alreadyVideoBean) {
                if(alreadyVideoBean != null) {
                    AlreadyVideoBean.RalreadyVideo r = alreadyVideoBean.getR();
                    AlreadyVideoBean.RalreadyVideo.PageBean page = r.getPage();
                    totalpage = page.getTotalpage();
                    ArrayList<AlreadyVideoBean.RalreadyVideo.RecordInfo> record = r.getRecord();
                    if (isRefresh) {
                        refreshLayout.setRefreshing(false);
                        buyVideoInfos.clear();
                    } else {
                        refreshLayout.setLoading(false);
                    }
                    buyVideoInfos.addAll(record);
                    buyVideoAdapter.setData(buyVideoInfos);


                }
            }

             @Override
            public void onError(AbstractRequest<AlreadyVideoBean> requet, int statusCode, String body) {

            }
        });

        req.go();
    }

    @Override
    public void onResume() {
        super.onResume();
        String url = UmiwiAPI.ALREADY_VIDEO+"?p="+1;
        GetRequest<AlreadyVideoBean> req = new GetRequest<AlreadyVideoBean>(url, GsonParser.class, AlreadyVideoBean.class, new AbstractRequest.Listener<AlreadyVideoBean>() {
            @Override
            public void onResult(AbstractRequest<AlreadyVideoBean> request, AlreadyVideoBean alreadyVideoBean) {
                ArrayList<AlreadyVideoBean.RalreadyVideo.RecordInfo> record = alreadyVideoBean.getR().getRecord();
                buyVideoInfos.clear();
                buyVideoInfos.addAll(record);
                buyVideoAdapter.setData(buyVideoInfos);

            }

            @Override
            public void onError(AbstractRequest<AlreadyVideoBean> requet, int statusCode, String body) {

            }
        });
        req.go();
    }
}