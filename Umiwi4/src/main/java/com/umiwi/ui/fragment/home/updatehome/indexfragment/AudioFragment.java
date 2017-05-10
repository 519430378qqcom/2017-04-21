package com.umiwi.ui.fragment.home.updatehome.indexfragment;

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
import com.umiwi.ui.adapter.updateadapter.AlreadyVoiceAdapter;
import com.umiwi.ui.beans.updatebeans.AlreadyShopVoiceBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.view.RefreshLayout;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ToastU;

/**
 * Created by LvDabing on 2017/2/16.
 * Email：lvdabing@lvshandian.com
 * Detail:音频
 */
public class AudioFragment extends BaseConstantFragment {
    @InjectView(R.id.listview)
    ListView listview;
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private int page = 1;
    private int totalpage;
    private ArrayList<AlreadyShopVoiceBean.RAlreadyVoice.Record> infos = new ArrayList<>();
    private AlreadyVoiceAdapter alreadyVoiceAdapter;
    private boolean isRefresh = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_layout, null);
        ButterKnife.inject(this, view);
        initRefreshLayout();
        alreadyVoiceAdapter = new AlreadyVoiceAdapter(getActivity());
        alreadyVoiceAdapter.setData(infos);
        listview.setAdapter(alreadyVoiceAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlreadyShopVoiceBean.RAlreadyVoice.Record record = infos.get(i);
                String hrefurl = record.getHrefurl();
                Intent intent = new Intent(getActivity(),UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VoiceDetailsFragment.class);
                intent.putExtra(VoiceDetailsFragment.KEY_DETAILURL,hrefurl);
                startActivity(intent);
            }
        });
        getinfos();
        return view;
    }

    private void getinfos() {
        String url = UmiwiAPI.ALREADY_VOICE + "?p=" + page;
        Log.e("TAG", "已购音频=" + url);
        GetRequest<AlreadyShopVoiceBean> request = new GetRequest<AlreadyShopVoiceBean>(
                url, GsonParser.class,
                AlreadyShopVoiceBean.class,
                AnswerListener);
        request.go();
    }
    private AbstractRequest.Listener<AlreadyShopVoiceBean> AnswerListener = new AbstractRequest.Listener<AlreadyShopVoiceBean>() {

        @Override
        public void onResult(AbstractRequest<AlreadyShopVoiceBean> request, AlreadyShopVoiceBean umiAnwebeans) {
            AlreadyShopVoiceBean.RAlreadyVoice.PageBean page = umiAnwebeans.getR().getPage();
            totalpage = page.getTotalpage();
            ArrayList<AlreadyShopVoiceBean.RAlreadyVoice.Record> record = umiAnwebeans.getR().getRecord();

            if (isRefresh) {
                refreshLayout.setRefreshing(false);
                infos.clear();
            } else {
                refreshLayout.setLoading(false);
            }
            infos.addAll(record);
            alreadyVoiceAdapter.setData(infos);
        }

        @Override
        public void onError(AbstractRequest<AlreadyShopVoiceBean> requet, int statusCode, String body) {
            if (isRefresh) {
                refreshLayout.setRefreshing(false);
            } else {
                refreshLayout.setLoading(false);
            }

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
        String url = UmiwiAPI.ALREADY_VOICE + "?p=" + page;
        GetRequest<AlreadyShopVoiceBean> request = new GetRequest<AlreadyShopVoiceBean>(
                url, GsonParser.class,
                AlreadyShopVoiceBean.class, new AbstractRequest.Listener<AlreadyShopVoiceBean>() {
            @Override
            public void onResult(AbstractRequest<AlreadyShopVoiceBean> request, AlreadyShopVoiceBean alreadyShopVoiceBean) {
                ArrayList<AlreadyShopVoiceBean.RAlreadyVoice.Record> record = alreadyShopVoiceBean.getR().getRecord();
                infos.clear();
                infos.addAll(record);
                alreadyVoiceAdapter.setData(infos);
            }

            @Override
            public void onError(AbstractRequest<AlreadyShopVoiceBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    private void initRefreshLayout() {
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
                            getinfos();
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
                getinfos();
            }
        });

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}




