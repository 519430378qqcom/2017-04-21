package com.umiwi.ui.fragment.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.adapter.MineMessageAdapter;
import com.umiwi.ui.http.parsers.ListOldParser;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.NoticeManager;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.model.MineMessageModel;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.ListResult;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

/**
 * Created by txy on 15/10/30. 私信
 */
public class MyMessageFragment extends BaseConstantFragment {

    private ArrayList<MineMessageModel> mList;
    private MineMessageAdapter mAdapter;
    private ListView mListView;

    private LoadingFooter mLoadingFooter;
    private ListViewScrollLoader mScrollLoader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frame_listview_layout, null);
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        setSupportActionBarAndToolbarTitle(mActionBarToolbar, "私信");

        mList = new ArrayList<MineMessageModel>();
        mListView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new MineMessageAdapter(getActivity().getApplication(), mList);

        mLoadingFooter = new LoadingFooter(getActivity().getApplication());// 加载更多的view
        mListView.addFooterView(mLoadingFooter.getView());

        mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);// 初始化加载更多监听

        mListView.setAdapter(mAdapter);

        mListView.setOnScrollListener(mScrollLoader);// 添加加载更多到listveiw

        mScrollLoader.onLoadFirstPage();// 初始化接口，加载第一页
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
        super.onLoadData(page);
        GetRequest<ListResult<MineMessageModel>> request = new GetRequest<ListResult<MineMessageModel>>(
                UmiwiAPI.MINE_MESSAGE + "&p=" + page, ListOldParser.class,
                MineMessageModel.class, listener);
        request.go();
    }

    private Listener<ListResult<MineMessageModel>> listener = new Listener<ListResult<MineMessageModel>>() {
        @Override
        public void onResult(AbstractRequest<ListResult<MineMessageModel>> request, ListResult<MineMessageModel> t) {
            if (t == null) {// 主要用于防止服务器数据出错
                mScrollLoader.showLoadErrorView("未知错误,请重试");
                return;
            }
            if (t.isLoadsEnd()) {// 判断是否是最后一页
                mScrollLoader.setEnd(true);
            }

            if (t.isEmptyData()) {// 当前列表没有数据
                mScrollLoader.showContentView("暂无私信消息");
                return;
            }
            mScrollLoader.setPage(t.getCurrentPage());// 用于分页请求
            mScrollLoader.setloading(false);//

            // 数据加载
            ArrayList<MineMessageModel> charts = t.getItems();
            mList.addAll(charts);

            if (mAdapter == null) {
                mAdapter = new MineMessageAdapter(getActivity().getApplication(), mList);
                mListView.setAdapter(mAdapter);// 解析成功 播放列表
            } else {
                mAdapter.notifyDataSetChanged();
            }
            if (YoumiRoomUserManager.getInstance().isLogin())
                NoticeManager.getInstance().loadNotice();
        }

        @Override
        public void onError(AbstractRequest<ListResult<MineMessageModel>> requet, int statusCode, String body) {
            mScrollLoader.showLoadErrorView();
        }
    };

}
