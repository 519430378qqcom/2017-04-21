package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.ExperDetailsVideoAdapter;
import com.umiwi.ui.beans.updatebeans.VideoBean;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.util.JsonUtil;
import com.umiwi.ui.view.NoScrollListview;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 行家详情视频
 * Created by Administrator on 2017/2/28.
 */

public class ExperDetailsVideoFragment extends BaseConstantFragment {
    @InjectView(R.id.no_data)
    TextView noData;
    private NoScrollListview listView;
    private List<VideoBean.RecordBean> videoInfos = new ArrayList<>();
    private int page;
    private int totalpage;
    private ExperDetailsVideoAdapter experDetailsVideoAdapter;
    private boolean isBottom = false;
    private boolean stopThread = false;
    private Handler handler;
    private Runnable runnable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exper_details_video_layout, null);
        listView = (NoScrollListview) view.findViewById(R.id.noscroll_listview);
        experDetailsVideoAdapter = new ExperDetailsVideoAdapter(getActivity());
        experDetailsVideoAdapter.setData(videoInfos);
        listView.setAdapter(experDetailsVideoAdapter);
        ExperDetailsFragment.setOnScrollListenerVideo(new ExperDetailsFragment.OnScrollListenerVideo() {
            @Override
            public void IsVideoBottom() {
                Log.e("is", "video");
                page++;
                isBottom = true;
                if (page <= totalpage) {
                    ExperDetailsFragment.tv_more.setVisibility(View.VISIBLE);
                    getInfos();
                }
            }
        });
        getInfos();
        handler = new Handler();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
                intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, UmiwiAPI.VODEI_URL + videoInfos.get(i).getId());
                getActivity().startActivity(intent);
            }
        });
        ButterKnife.inject(this, view);
        return view;
    }

    private void getInfos() {
        String albumurl = ExperDetailsFragment.albumurl;
        if (!TextUtils.isEmpty(albumurl)) {
            String url = albumurl + "/?p=" + page;
            OkHttpUtils.get().url(url).build().execute(new CustomStringCallBack() {

                @Override
                public void onFaild() {
                    Log.e("data", "名人详情视频数据请求失败");
                }

                @Override
                public void onSucess(final String data) {
                    Log.e("data", "名人详情视频数据请求成功" + data);
                    if (!TextUtils.isEmpty(data)) {
                        if (isBottom == true) {
                            if (runnable == null) {
                                runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ExperDetailsFragment.tv_more.setVisibility(View.GONE);
                                                VideoBean videoBean = JsonUtil.json2Bean(data, VideoBean.class);
                                                totalpage = videoBean.getPage().getTotalpage();
                                                List<VideoBean.RecordBean> record = videoBean.getRecord();
                                                if (record.size()>0&&record!=null){
                                                    noData.setVisibility(View.GONE);
                                                }else{
                                                    noData.setVisibility(View.VISIBLE);
                                                }
                                                videoInfos.addAll(record);
                                                experDetailsVideoAdapter.setData(videoInfos);
                                            }
                                        });
                                    }
                                };
                            }
                            handler.postDelayed(runnable, 1000);
                        } else {
                            VideoBean videoBean = JsonUtil.json2Bean(data, VideoBean.class);
                            totalpage = videoBean.getPage().getTotalpage();
                            List<VideoBean.RecordBean> record = videoBean.getRecord();
                            if (record.size()>0&&record!=null){
                                noData.setVisibility(View.GONE);
                            }else{
                                noData.setVisibility(View.VISIBLE);
                            }
                            videoInfos.addAll(record);
                            experDetailsVideoAdapter.setData(videoInfos);
                        }

                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
