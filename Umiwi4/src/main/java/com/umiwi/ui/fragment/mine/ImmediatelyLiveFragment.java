package com.umiwi.ui.fragment.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.AuthorChatRoomActivity;
import com.umiwi.ui.activity.LiveChatRoomActivity;
import com.umiwi.ui.adapter.MyLiveAdapter;
import com.umiwi.ui.beans.MyLiveBean;
import com.umiwi.ui.fragment.audiolive.LiveDetailsFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.view.RefreshLayout;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ToastU;

/**
 * Created by dong on 2017/5/2.
 * 我的直播---》即将开始
 */

public class ImmediatelyLiveFragment extends Fragment {
    @InjectView(R.id.listview)
    ListView listview;
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @InjectView(R.id.iv_image_noclass)
    ImageView iv_image_noclass;
    private Context context;
    private MyLiveAdapter myLiveAdapter;
    private int page = 1;
    private int totalpage;
    private boolean isRefresh = true;
    private MyLiveBean myLiveBean1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_immediately, container, false);
        ButterKnife.inject(this, view);
        context = getActivity();
        initRefreshLayout();
        initData();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), AuthorChatRoomActivity.class);
                intent.putExtra(LiveDetailsFragment.DETAILS_ID, myLiveBean1.getR().getRecord().get(position).getId());
                intent.putExtra(LiveChatRoomActivity.ROOM_ID, myLiveBean1.getR().getRecord().get(position).getRoomid());
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        String url = String.format(UmiwiAPI.MY_LIVE, 1, page);
        GetRequest<MyLiveBean> request = new GetRequest<>(
                url, GsonParser.class, MyLiveBean.class, new AbstractRequest.Listener<MyLiveBean>() {
            @Override
            public void onResult(AbstractRequest<MyLiveBean> request, MyLiveBean myLiveBean) {
                if (myLiveBean != null) {
                    myLiveBean1 = myLiveBean;
                    totalpage = myLiveBean.getR().getPage().getTotalpage();
                    List<MyLiveBean.RBean.RecordBean> record = myLiveBean.getR().getRecord();
                    if (record.size() == 0) {
                        iv_image_noclass.setVisibility(View.VISIBLE);
                    } else {
                        iv_image_noclass.setVisibility(View.GONE);
                    }
                    if (isRefresh) {
                        refreshLayout.setRefreshing(false);
                        if (myLiveAdapter == null) {
                            myLiveAdapter = new MyLiveAdapter(context, record);
                            listview.setAdapter(myLiveAdapter);
                        } else {
                            myLiveAdapter.setItems(record);
                            myLiveAdapter.notifyDataSetChanged();
                        }
                    } else {
                        refreshLayout.setLoading(false);
                        if (myLiveAdapter == null) {
                            myLiveAdapter = new MyLiveAdapter(context, record);
                            listview.setAdapter(myLiveAdapter);
                        } else {
                            myLiveAdapter.addItems(record);
                            myLiveAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onError(AbstractRequest<MyLiveBean> requet, int statusCode, String body) {
                refreshLayout.setRefreshing(false);
                refreshLayout.setLoading(false);
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
                    initData();

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
                initData();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
