package com.umiwi.ui.fragment.mine;

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
import com.umiwi.ui.adapter.updateadapter.RecentPlayAudioAdapter;
import com.umiwi.ui.beans.updatebeans.AlreadyShopVoiceBean;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.view.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ToastU;

/**
 * Created by Administrator on 2017/3/22.
 */

public class RecentPlayAudioFragment extends BaseConstantFragment {

    private RefreshLayout refreshLayout;
    private ListView listview;
    private RecentPlayAudioAdapter audioAdapter;
    private int totalpage ;
    private int page = 1;
    private boolean isla = false;
    private boolean isload = false;
    private boolean isRefresh = true;
    private List<AlreadyShopVoiceBean.RAlreadyVoice.Record> mList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recentplay_audio, null);
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        listview = (ListView) view.findViewById(R.id.listview);
        mList = new ArrayList<>();
        audioAdapter = new RecentPlayAudioAdapter(getActivity());
        audioAdapter.setData(mList);
        listview.setAdapter(audioAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlreadyShopVoiceBean.RAlreadyVoice.Record record = mList.get(position);
                String hrefurl = record.getHrefurl();
                Intent intent = new Intent(getActivity(),UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VoiceDetailsFragment.class);
                intent.putExtra(VoiceDetailsFragment.KEY_DETAILURL,hrefurl);
                startActivity(intent);
            }
        });
        getInfos();
        initrefreshLayout();
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

    //请求数据
    private void getInfos() {
//        String format = String.format(UmiwiAPI.UMIWI_ENSH_VIDEO, 1);
//        Log.e("TAG", "浏览记录=" + format);
        String url = String.format(UmiwiAPI.UMIWI_AUDIO_RECORD,page);
        GetRequest<AlreadyShopVoiceBean> request = new GetRequest<AlreadyShopVoiceBean>(
                url, GsonParser.class,
                AlreadyShopVoiceBean.class,
                new AbstractRequest.Listener<AlreadyShopVoiceBean>() {
                    @Override
                    public void onResult(AbstractRequest<AlreadyShopVoiceBean> request, AlreadyShopVoiceBean alreadyShopVoiceBean) {
                        AlreadyShopVoiceBean.RAlreadyVoice.PageBean page = alreadyShopVoiceBean.getR().getPage();
                        totalpage = page.getTotalpage();
                        Log.e("TAG", "toata=" + totalpage);
                        ArrayList<AlreadyShopVoiceBean.RAlreadyVoice.Record> record = alreadyShopVoiceBean.getR().getRecord();

                        if (isRefresh) {
                            refreshLayout.setRefreshing(false);
                            mList.clear();
                        } else {
                            refreshLayout.setLoading(false);
                        }
                        mList.addAll(record);
                        audioAdapter.setData(mList);


                    }

                    @Override
                    public void onError(AbstractRequest<AlreadyShopVoiceBean> requet, int statusCode, String body) {
                        if (isRefresh) {
                            refreshLayout.setRefreshing(false);
                        } else {
                            refreshLayout.setLoading(false);
                        }
                    }
                });
        request.go();

    }
}
