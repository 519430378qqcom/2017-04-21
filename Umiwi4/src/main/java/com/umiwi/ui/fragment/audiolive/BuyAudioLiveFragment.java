package com.umiwi.ui.fragment.audiolive;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.LiveChatRoomActivity;
import com.umiwi.ui.adapter.updateadapter.BuyAudioLiveAdapter;
import com.umiwi.ui.beans.updatebeans.BuyAudioLiveBean;
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
 * Created by Administrator on 2017/5/5 0005.
 */

public class BuyAudioLiveFragment extends BaseConstantFragment {
    @InjectView(R.id.listview)
    ListView listview;
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private int page = 1;
    private int totalpage;
    private boolean isRefresh = true;
    private BuyAudioLiveAdapter adapter;
    private ArrayList<BuyAudioLiveBean.RBuyAudioLive.BuyAudioLiveRecord> mList = new ArrayList<>();
    private boolean isRequestSuccess;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buyaudiolive_layout, null);
        ButterKnife.inject(this, view);
        adapter = new BuyAudioLiveAdapter(getActivity());
        adapter.setData(mList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BuyAudioLiveBean.RBuyAudioLive.BuyAudioLiveRecord buyAudioLiveRecord = mList.get(position);
                Intent intent = new Intent(getActivity(), LiveChatRoomActivity.class);
                intent.putExtra(LiveDetailsFragment.DETAILS_ID, buyAudioLiveRecord.getId());
                intent.putExtra(LiveChatRoomActivity.ROOM_ID, buyAudioLiveRecord.getRoomid());
                getActivity().startActivity(intent);
            }
        });

        initrefreshLayout();
        getInfos();
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
                    getInfos();

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

    /**
     * 设置刷新请求超时提示
     * @param time
     */
    private void setRefreshTimeout(int time) {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isRequestSuccess) {
                    Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
                    refreshLayout.setLoading(false);
                }
            }
        },time);
    }

    private void getInfos() {
        isRequestSuccess = false;
        String url = String.format(UmiwiAPI.UMIWI_BUYAUDIOLIVE, page);
        GetRequest<BuyAudioLiveBean> request = new GetRequest<BuyAudioLiveBean>(url, GsonParser.class, BuyAudioLiveBean.class, new AbstractRequest.Listener<BuyAudioLiveBean>() {
            @Override
            public void onResult(AbstractRequest<BuyAudioLiveBean> request, BuyAudioLiveBean buyAudioLiveBean) {
                if (buyAudioLiveBean != null) {
                    isRequestSuccess = true;
                    totalpage = buyAudioLiveBean.getR().getPage().getTotalpage();
                    ArrayList<BuyAudioLiveBean.RBuyAudioLive.BuyAudioLiveRecord> record = buyAudioLiveBean.getR().getRecord();
                    if (isRefresh) {
                        refreshLayout.setRefreshing(false);
                        mList.clear();
                    } else {
                        refreshLayout.setLoading(false);
                    }
                    mList.addAll(record);
                    adapter.setData(mList);
                }
            }
            @Override
            public void onError(AbstractRequest<BuyAudioLiveBean> requet, int statusCode, String body) {
                    refreshLayout.setRefreshing(false);
                    refreshLayout.setLoading(false);
            }
        });
        request.go();
    }

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
        String url = String.format(UmiwiAPI.UMIWI_BUYAUDIOLIVE, page);
        GetRequest<BuyAudioLiveBean> request = new GetRequest<BuyAudioLiveBean>(url, GsonParser.class, BuyAudioLiveBean.class, new AbstractRequest.Listener<BuyAudioLiveBean>() {
            @Override
            public void onResult(AbstractRequest<BuyAudioLiveBean> request, BuyAudioLiveBean buyAudioLiveBean) {

                ArrayList<BuyAudioLiveBean.RBuyAudioLive.BuyAudioLiveRecord> record = buyAudioLiveBean.getR().getRecord();
                mList.clear();
                mList.addAll(record);
                adapter.setData(mList);
            }

            @Override
            public void onError(AbstractRequest<BuyAudioLiveBean> requet, int statusCode, String body) {
            }
        });
        request.go();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
