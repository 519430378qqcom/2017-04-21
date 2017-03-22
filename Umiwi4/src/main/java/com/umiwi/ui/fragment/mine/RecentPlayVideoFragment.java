package com.umiwi.ui.fragment.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.updateadapter.RecentPlayVideoAdapter;
import com.umiwi.ui.beans.updatebeans.RecentPlayVideoBean;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.view.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;


/**
 * Created by Administrator on 2017/3/22.
 */

public class RecentPlayVideoFragment extends BaseConstantFragment {
//    @InjectView(R.id.listview)
//    ListView listview;
//    @InjectView(R.id.refreshLayout)
//    RefreshLayout refreshLayout;
    private int page = 1;
    private List<RecentPlayVideoBean.RecentPlayVideo> mList;
    private int totalpage;
    private boolean isRefresh = true;
    private RecentPlayVideoAdapter videoAdapter;
    private RefreshLayout refreshLayout;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recentplay_video, null);
//        ButterKnife.inject(this, view);
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        listView = (ListView) view.findViewById(R.id.listView);
        mList = new ArrayList<>();
        videoAdapter = new RecentPlayVideoAdapter(getActivity());
        videoAdapter.setData(mList);
        listView.setAdapter(videoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String hrefurl = mList.get(i).getDetailurl();
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
                intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, hrefurl);
                startActivity(intent);
            }
        });
        initRefreshLayout();
        getinfos();
        return view;
    }
    private void initRefreshLayout() {
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.main_color));
//        refreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
//            @Override
//            public void onLoad() {
////                page++;
//                isRefresh = false;
//                refreshLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        getinfos();
//                    }
//                }, 1000);
////                if (page <= totalpage) {
////                } else {
////                    ToastU.showLong(getActivity(), "没有更多了!");
////                    refreshLayout.setLoading(false);
////
////                }
//            }
//        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
//                page = 1;

                getinfos();
            }
        });
    }
    //联网获取信息
    private void getinfos() {
        String url = UmiwiAPI.UMIWI_VIDEO_RECORD;
        GetRequest<RecentPlayVideoBean> request = new GetRequest<RecentPlayVideoBean>(url, GsonParser.class, RecentPlayVideoBean.class, new AbstractRequest.Listener<RecentPlayVideoBean>() {
            @Override
            public void onResult(AbstractRequest<RecentPlayVideoBean> request, RecentPlayVideoBean recentPlayVideoBean) {
                ArrayList<RecentPlayVideoBean.RecentPlayVideo> r = recentPlayVideoBean.getR();
                mList.clear();
                mList.addAll(r);
                videoAdapter.setData(mList);

                if (isRefresh) {
//                    listView.setEnabled(false);
                    isRefresh = false;
                    refreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onError(AbstractRequest<RecentPlayVideoBean> requet, int statusCode, String body) {

            }
        });
        request.go();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ButterKnife.reset(this);
    }
}
