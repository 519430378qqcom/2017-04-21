package com.umiwi.ui.fragment.audiolive;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.updateadapter.AudioLiveAdapter;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.view.RefreshLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.util.ToastU;

/**
 * Created by lenovo on 2017/4/25.
 */

public class AudioLiveNoStartFragment extends BaseConstantFragment {
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @InjectView(R.id.listview)
    ListView listview;
    private int page = 1;
    private int totalpage;
    private boolean isRefresh = true;

    private AudioLiveAdapter audioLiveAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.audio_live_layout, null);
        ButterKnife.inject(this, view);

        audioLiveAdapter = new AudioLiveAdapter(getActivity());
//        audioLiveAdapter.setData();
        listview.setAdapter(audioLiveAdapter);
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
//                            getinfos();
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
//                getinfos();
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }
}
