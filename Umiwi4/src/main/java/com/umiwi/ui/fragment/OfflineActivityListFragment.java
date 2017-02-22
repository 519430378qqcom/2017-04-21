package com.umiwi.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.ActivityAdapter;
import com.umiwi.ui.beans.ActivityItemBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.NoticeManager;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.parsers.UmiwiListParser;
import com.umiwi.ui.parsers.UmiwiListResult;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.util.ListViewPositionUtils;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

public class OfflineActivityListFragment extends BaseConstantFragment {

    public static final String KEY_INPROGRESS = "key.inprogress";

    private String status;

    private ListView mListView;
    private ActivityAdapter mAdapter;

    private ArrayList<ActivityItemBean> mList;

    private LoadingFooter mLoadingFooter;
    private ListViewScrollLoader mScrollLoader;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_INPROGRESS, status);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            status = savedInstanceState.getString(KEY_INPROGRESS);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                status = bundle.getString(KEY_INPROGRESS);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frame_notoolbar_listview_layout, null);

        mList = new ArrayList<>();
        mListView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new ActivityAdapter(getActivity(), mList);

        mLoadingFooter = new LoadingFooter(getActivity());
        mListView.addFooterView(mLoadingFooter.getView());

        mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);
        mListView.setOnScrollListener(mScrollLoader);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListView.clearChoices();
                ActivityItemBean mListBeans = mList.get(ListViewPositionUtils.indexInDataSource(position, mListView));
                if (ListViewPositionUtils.isPositionCanClick(mListBeans, position, mListView, mList)) {
                    Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
                    i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ActivityDetailFragment.class);
                    i.putExtra(ActivityDetailFragment.KEY_ACTIVITY_TITLE, mListBeans.getTitle());
                    i.putExtra(ActivityDetailFragment.KEY_DETAIL_URL, mListBeans.getDetailURL());
                    i.putExtra(ActivityDetailFragment.KEY_IS_OVER, mListBeans.isEnd());
                    startActivity(i);
                }
            }
        });

        mScrollLoader.onLoadFirstPage();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(fragmentName);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(fragmentName);
    }

    @Override
    public void onLoadData(int page) {
        GetRequest<UmiwiListResult<ActivityItemBean>> get = new GetRequest<UmiwiListResult<ActivityItemBean>>(String.format(UmiwiAPI.HUO_DONG_LIST, status, page), UmiwiListParser.class, ActivityItemBean.class, listener);
        get.go();
    }

    private Listener<UmiwiListResult<ActivityItemBean>> listener = new Listener<UmiwiListResult<ActivityItemBean>>() {
        @Override
        public void onResult(AbstractRequest<UmiwiListResult<ActivityItemBean>> request, UmiwiListResult<ActivityItemBean> t) {
            if (t == null) {// 主要用于防止服务器数据出错
                mScrollLoader.showLoadErrorView("未知错误,请重试");
                return;
            }
            if (t.isLoadsEnd()) {// 判断是否是最后一页
                mScrollLoader.setEnd(true);
            }

            if (t.isEmptyData()) {// 当前列表没有数据
                mScrollLoader.showContentView("当前没有活动");
                return;
            }
            mScrollLoader.setPage(t.getCurrentPage());// 用于分页请求
            mScrollLoader.setloading(false);//

            // 数据加载
            ArrayList<ActivityItemBean> charts = t.getItems();
            mList.addAll(charts);

            if (mAdapter == null) {
                mAdapter = new ActivityAdapter(getActivity().getApplication(), mList);
                mListView.setAdapter(mAdapter);// 解析成功 播放列表
            } else {
                mAdapter.notifyDataSetChanged();
            }
            if (YoumiRoomUserManager.getInstance().isLogin())
                NoticeManager.getInstance().loadNotice();
        }

        @Override
        public void onError(AbstractRequest<UmiwiListResult<ActivityItemBean>> requet, int statusCode, String body) {
            mScrollLoader.showLoadErrorView();
        }
    };

}
