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

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.updateadapter.EnshrineVideoAdapter;
import com.umiwi.ui.beans.updatebeans.EnshrineVideoBean;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.view.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ToastU;

/**
 * Created by Administrator on 2017/3/23.
 */

public class EnshrineVideoFragment extends BaseConstantFragment {
    @InjectView(R.id.listview)
    ListView listview;
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private List<EnshrineVideoBean.REnshrineVideo.EnshrineVideoInfo> mList;
    private EnshrineVideoAdapter videoAdapter;
    private int page = 1;
    private boolean isla = false;
    private boolean isload = false;
    private int totalpage;
    private boolean isRefresh = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enshrine_video_layout, null);
        mList = new ArrayList<>();


        ButterKnife.inject(this, view);

        videoAdapter = new EnshrineVideoAdapter(getActivity());
        videoAdapter.setData(mList);
        listview.setAdapter(videoAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                boolean isbuy = mList.get(i).getIsbuy();
//                if (isbuy) {
//                    Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
//                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LogicalThinkingFragment.class);
//                    intent.putExtra("id", mList.get(i).getId());
//                    intent.putExtra("title", mList.get(i).getTitle());
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
//                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ColumnDetailsFragment.class);//详情页
//                    intent.putExtra("columnurl", mList.get(i).getColumnurl());
//                    Log.e("TAG", "mList.get(i).getColumnurl()=" + mList.get(i).getColumnurl());
//                    startActivity(intent);
//                }
                String hrefurl = mList.get(i).getDetailurl();
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

    private void getInfos() {
        String url = String.format(UmiwiAPI.UMIWI_ENSH_VIDEO,page);
//        Log.e("TAG", "收藏视频列表=" + url);
        GetRequest<EnshrineVideoBean> request = new GetRequest<EnshrineVideoBean>(url, GsonParser.class, EnshrineVideoBean.class, new AbstractRequest.Listener<EnshrineVideoBean>() {
            @Override
            public void onResult(AbstractRequest<EnshrineVideoBean> request, EnshrineVideoBean enshrineVideoBean) {
                EnshrineVideoBean.REnshrineVideo.EnshrineVideoPage page = enshrineVideoBean.getR().getPage();
                totalpage = page.getTotalpage();
                ArrayList<EnshrineVideoBean.REnshrineVideo.EnshrineVideoInfo> record = enshrineVideoBean.getR().getRecord();
//                mList.clear();

                if (isRefresh) {
                    refreshLayout.setRefreshing(false);
                    mList.clear();
                } else {
                    refreshLayout.setLoading(false);
                }
//                Log.e("homecoulm", "size" + record.size() + "page" + totalpage);
                mList.addAll(record);
                videoAdapter.setData(mList);
            }

            @Override
            public void onError(AbstractRequest<EnshrineVideoBean> requet, int statusCode, String body) {
                if (isRefresh) {
                    refreshLayout.setRefreshing(false);
                } else {
                    refreshLayout.setLoading(false);
                }
            }
        });
        request.go();
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

    @Override
    public void onResume() {
        super.onResume();
        String url = String.format(UmiwiAPI.UMIWI_ENSH_VIDEO,1);
        GetRequest<EnshrineVideoBean> request = new GetRequest<EnshrineVideoBean>(url, GsonParser.class, EnshrineVideoBean.class, new AbstractRequest.Listener<EnshrineVideoBean>() {
            @Override
            public void onResult(AbstractRequest<EnshrineVideoBean> request, EnshrineVideoBean enshrineVideoBean) {
//                EnshrineVideoBean.REnshrineVideo.EnshrineVideoPage page = enshrineVideoBean.getR().getPage();
//                totalpage = page.getTotalpage();
                ArrayList<EnshrineVideoBean.REnshrineVideo.EnshrineVideoInfo> record = enshrineVideoBean.getR().getRecord();
                mList.clear();
                mList.addAll(record);
                videoAdapter.setData(mList);

            }

            @Override
            public void onError(AbstractRequest<EnshrineVideoBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageStart(fragmentName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
