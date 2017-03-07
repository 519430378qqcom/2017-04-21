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

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.ExperDetailsVoiceAdapter;
import com.umiwi.ui.beans.VoiceBean;
import com.umiwi.ui.beans.updatebeans.VideoBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.util.JsonUtil;
import com.umiwi.ui.view.NoScrollListview;
import com.umiwi.ui.view.TopFloatScrollView;
import com.umiwi.video.control.PlayerController;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 行家详情音频
 * Created by Administrator on 2017/2/28.
 */

public class ExperDetailsVoiceFragment extends BaseConstantFragment {
    @InjectView(R.id.noscroll_listview)
    NoScrollListview noscrollListview;
    private List<VoiceBean.RecordBean> voiceList = new ArrayList<>();
    private ExperDetailsVoiceAdapter experDetailsVoiceAdapter;
    private int page = 1;
    private String url;
    private int totalpage;
    private boolean isBottom = false;
    private Handler handler;
    private Runnable runnable;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exper_details_voice_layout, null);
        ButterKnife.inject(this, view);
        experDetailsVoiceAdapter = new ExperDetailsVoiceAdapter(getActivity());
        experDetailsVoiceAdapter.setData(voiceList);
        noscrollListview.setAdapter(experDetailsVoiceAdapter);
        noscrollListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VoiceDetailsFragment.class);
                startActivity(intent);
            }
        });
        ExperDetailsFragment.setOnScrollListenerVoice(new ExperDetailsFragment.OnScrollListenerVoice() {
            @Override
            public void IsvoiceBottom() {
                page++;
                isBottom = true;
                if (page<=totalpage){
                    ExperDetailsFragment.tv_more.setVisibility(View.VISIBLE);
                    getInfos();
                }
            }
        });
        getInfos();
        handler = new Handler();
        return view;
    }

    private void getInfos() {
        Log.d("data","名人详情音频列表请求了。。。");

        String audioalbumurl = ExperDetailsFragment.audioalbumurl;

        if (!TextUtils.isEmpty(audioalbumurl)){
            url = audioalbumurl+"/?p="+page;
            if (url!=null||url!=""){
                Log.e("data","名人详情音频列表请求了。。");

                OkHttpUtils.get().url(url).build().execute(new CustomStringCallBack() {
                    @Override
                    public void onFaild() {
                        Log.e("data","名人详情音频列表请求失败");
                    }

                    @Override
                    public void onSucess(final String data) {
                        if (isBottom == true){
                            if (runnable==null){
                                runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ExperDetailsFragment.tv_more.setVisibility(View.GONE);
                                                VoiceBean voiceBean = JsonUtil.json2Bean(data, VoiceBean.class);
                                                totalpage = voiceBean.getPage().getTotalpage();
                                                Log.e("totalpage", totalpage +"");
                                                List<VoiceBean.RecordBean> records = voiceBean.getRecord();
                                                voiceList.addAll(records);
                                                experDetailsVoiceAdapter.setData(voiceList);
                                            }
                                        });
                                    }
                                };
                            }
                            handler.postDelayed(runnable,1000);
                        }else{
                            Log.e("data","名人详情音频列表请求成功"+data);
                            if (data!=null){
                                VoiceBean voiceBean = JsonUtil.json2Bean(data, VoiceBean.class);
                                totalpage = voiceBean.getPage().getTotalpage();
                                Log.e("totalpage", totalpage +"");
                                List<VoiceBean.RecordBean> records = voiceBean.getRecord();
                                voiceList.addAll(records);
                                experDetailsVoiceAdapter.setData(voiceList);
                            }
                        }


                    }
                });
            }
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
