package com.umiwi.ui.fragment.audiolive;

import android.content.Context;
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
import com.umiwi.ui.adapter.updateadapter.AudioLiveAdapter;
import com.umiwi.ui.beans.updatebeans.AudioLiveBean;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
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
 * Created by lenovo on 2017/4/25.
 */

public class AudioLiveOnFragment extends BaseConstantFragment {
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @InjectView(R.id.listview)
    ListView listview;
    private int page = 1;
    private int status = 2;
    private int totalpage;
    private boolean isRefresh = true;

    private AudioLiveAdapter audioLiveAdapter;
    private ArrayList<RecommendBean.RBean.HotLiveBean.HotLiveRecord> mList = new ArrayList<>();
    private String sec_live_moreurl;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.audio_live_layout, null);
        ButterKnife.inject(this,view);
        mContext = getActivity();
        initRefreshLayout();
        audioLiveAdapter = new AudioLiveAdapter(getActivity());
        audioLiveAdapter.setData(mList);
        listview.setAdapter(audioLiveAdapter);
        getInfos();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecommendBean.RBean.HotLiveBean.HotLiveRecord hotLiveRecord = mList.get(position);
                String liveId = hotLiveRecord.getId();
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS,AudioLiveDetailsFragment.class);
                intent.putExtra(AudioLiveDetailsFragment.LIVEID,liveId);
                startActivity(intent);
            }
        });
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), LiveChatRoomActivity.class);
//                intent.putExtra(LiveDetailsFragment.DETAILS_ID,"5");
//                intent.putExtra(LiveChatRoomActivity.ROOM_ID,"8692675");
//                getActivity().startActivity(intent);
//            }
//        });
        return view;
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
        String url = String.format(UmiwiAPI.UMIWI_AUIDOLIVE,status,1);
        GetRequest<AudioLiveBean> request = new GetRequest<AudioLiveBean>(url, GsonParser.class, AudioLiveBean.class, new AbstractRequest.Listener<AudioLiveBean>() {
            @Override
            public void onResult(AbstractRequest<AudioLiveBean> request, AudioLiveBean audioLiveBean) {
                ArrayList<RecommendBean.RBean.HotLiveBean.HotLiveRecord> record = audioLiveBean.getR().getRecord();
                mList.clear();
                mList.addAll(record);
                audioLiveAdapter.setData(mList);

            }

            @Override
            public void onError(AbstractRequest<AudioLiveBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    private void getInfos() {
       String url = String.format(UmiwiAPI.UMIWI_AUIDOLIVE,status,page);
        GetRequest<AudioLiveBean> request = new GetRequest<AudioLiveBean>(url, GsonParser.class, AudioLiveBean.class, new AbstractRequest.Listener<AudioLiveBean>() {
            @Override
            public void onResult(AbstractRequest<AudioLiveBean> request, AudioLiveBean audioLiveBean) {
                ArrayList<RecommendBean.RBean.HotLiveBean.HotLiveRecord> record = audioLiveBean.getR().getRecord();
                totalpage = audioLiveBean.getR().getPage().getTotalpage();
                if (isRefresh) {
                    refreshLayout.setRefreshing(false);
                    mList.clear();
                } else {
                    refreshLayout.setLoading(false);
                }
                mList.addAll(record);
                audioLiveAdapter.setData(mList);

            }

            @Override
            public void onError(AbstractRequest<AudioLiveBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }
}
