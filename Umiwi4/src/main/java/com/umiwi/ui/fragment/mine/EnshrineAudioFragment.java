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

import com.umeng.analytics.MobclickAgent;
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

/**
 * Created by Administrator on 2017/3/23.
 */

public class EnshrineAudioFragment extends BaseConstantFragment {
    private RefreshLayout refreshLayout;
    private ListView listview;
    private RecentPlayAudioAdapter audioAdapter;
    private int totalpage ;
    private int page = 1;
    private boolean isla = false;
    private boolean isload = false;
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
                isload = true;
                page++;
                if (page <= totalpage) {
                    getInfos();
                } else {
                    refreshLayout.setLoading(false);
                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listview.setEnabled(false);
                isla = true;
                page = 1;
                mList.clear();

                getInfos();
            }
        });
    }

    //请求数据
    private void getInfos() {
        String format = String.format(UmiwiAPI.UMIWI_ENSH_AUDIO, page);
        Log.e("TAG", "收藏列表视频format=" + format);
        GetRequest<AlreadyShopVoiceBean> request = new GetRequest<AlreadyShopVoiceBean>(
                format, GsonParser.class,
                AlreadyShopVoiceBean.class,
                new AbstractRequest.Listener<AlreadyShopVoiceBean>() {
                    @Override
                    public void onResult(AbstractRequest<AlreadyShopVoiceBean> request, AlreadyShopVoiceBean alreadyShopVoiceBean) {
                        AlreadyShopVoiceBean.RAlreadyVoice.PageBean page = alreadyShopVoiceBean.getR().getPage();
                        totalpage = page.getTotalpage();
                        ArrayList<AlreadyShopVoiceBean.RAlreadyVoice.Record> record = alreadyShopVoiceBean.getR().getRecord();
//                        mList.clear();
                        mList.addAll(record);
                        audioAdapter.setData(mList);

                        if (isla) {
                            listview.setEnabled(true);
                            isla = false;
                            refreshLayout.setRefreshing(false);
                        } else if (isload) {
                            isload = false;
                            refreshLayout.setLoading(false);
                        }
                    }

                    @Override
                    public void onError(AbstractRequest<AlreadyShopVoiceBean> requet, int statusCode, String body) {

                    }
                });
        request.go();

    }

    @Override
    public void onResume() {
        super.onResume();
        String format = String.format(UmiwiAPI.UMIWI_ENSH_AUDIO, 1);
        Log.e("TAG", "收藏列表视频format=" + format);
        GetRequest<AlreadyShopVoiceBean> request = new GetRequest<AlreadyShopVoiceBean>(
                format, GsonParser.class,
                AlreadyShopVoiceBean.class,
                new AbstractRequest.Listener<AlreadyShopVoiceBean>() {
                    @Override
                    public void onResult(AbstractRequest<AlreadyShopVoiceBean> request, AlreadyShopVoiceBean alreadyShopVoiceBean) {
                        AlreadyShopVoiceBean.RAlreadyVoice.PageBean page = alreadyShopVoiceBean.getR().getPage();
                        totalpage = page.getTotalpage();
                        ArrayList<AlreadyShopVoiceBean.RAlreadyVoice.Record> record = alreadyShopVoiceBean.getR().getRecord();
                        mList.clear();
                        mList.addAll(record);
                        audioAdapter.setData(mList);
                    }

                    @Override
                    public void onError(AbstractRequest<AlreadyShopVoiceBean> requet, int statusCode, String body) {

                    }
                });
        request.go();
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageStart(fragmentName);
    }


}
