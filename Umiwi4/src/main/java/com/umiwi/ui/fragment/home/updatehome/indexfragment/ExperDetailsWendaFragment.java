package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.ExperDetailsWendaAdapter;
import com.umiwi.ui.beans.VoiceBean;
import com.umiwi.ui.beans.updatebeans.VideoBean;
import com.umiwi.ui.beans.updatebeans.WenDaBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.util.JsonUtil;
import com.umiwi.ui.view.NoScrollListview;
import com.umiwi.ui.view.TopFloatScrollView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 行家详情问答
 * Created by Administrator on 2017/2/28.
 */

public class ExperDetailsWendaFragment extends BaseConstantFragment {

    @InjectView(R.id.noscroll_listview)
    NoScrollListview noscrollListview;
    private int page;
    private int totalpage;
    private boolean isBottom = false;
    private List<WenDaBean.RecordBean> wendaInfos = new ArrayList<>();
    private ExperDetailsWendaAdapter experDetailsWendaAdapter;
    private Handler handler;
    private Runnable runnable;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exper_details_wenda_layout, null);
        ButterKnife.inject(this, view);
        experDetailsWendaAdapter = new ExperDetailsWendaAdapter(getActivity());
        experDetailsWendaAdapter.setData(wendaInfos);
        noscrollListview.setAdapter(experDetailsWendaAdapter);
        ExperDetailsFragment.setOnScrollListenerWenda(new ExperDetailsFragment.OnScrollListenerWenda() {
            @Override
            public void IswendaBottom() {
                page++;
                isBottom = true;
                if (page<=totalpage){
                    ExperDetailsFragment.tv_more.setVisibility(View.VISIBLE);
                    getInfos();
                }
            }
        });
        handler = new Handler();
        getInfos();
        return view;
    }

    private void getInfos() {
        String questionurl = ExperDetailsFragment.questionurl;
        if (!TextUtils.isEmpty(questionurl)){
            String url = questionurl+"/?p="+page;

            OkHttpUtils.get().url(url).build().execute(new CustomStringCallBack() {
                @Override
                public void onFaild() {
                    Log.e("data","名人详情问答数据请求失败");

                }

                @Override
                public void onSucess(final String data) {
                    Log.e("data","名人详情问答数据请求成功"+data);
                    if (!TextUtils.isEmpty(data)){
                        if (isBottom == true){
                            if (runnable==null){
                                runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ExperDetailsFragment.tv_more.setVisibility(View.GONE);
                                                WenDaBean wenDaBean = JsonUtil.json2Bean(data, WenDaBean.class);
                                                totalpage = wenDaBean.getPage().getTotalpage();
                                                List<WenDaBean.RecordBean> record = wenDaBean.getRecord();
                                                wendaInfos.addAll(record);
                                                experDetailsWendaAdapter.setData(wendaInfos);
                                            }
                                        });
                                    }
                                };
                            }
                            handler.postDelayed(runnable,1000);
                        }else{
                            WenDaBean wenDaBean = JsonUtil.json2Bean(data, WenDaBean.class);
                            totalpage = wenDaBean.getPage().getTotalpage();
                            List<WenDaBean.RecordBean> record = wenDaBean.getRecord();
                            wendaInfos.addAll(record);
                            experDetailsWendaAdapter.setData(wendaInfos);
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
