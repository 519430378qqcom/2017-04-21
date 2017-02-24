package com.umiwi.ui.fragment.home.updatehome.newtest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.HomeRecommendAdapter;
import com.umiwi.ui.adapter.LunboAdapter;
import com.umiwi.ui.beans.UmiwiListBeans;
import com.umiwi.ui.http.parsers.CourseListParser;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.util.CommonHelper;
import com.umiwi.ui.view.AutoViewPager;
import com.umiwi.ui.view.CirclePageIndicator;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.util.DimensionUtil;
import cn.youmi.framework.util.ListViewScrollLoader;

/**
 * 轮播测试
 */

public class LunBoTestFragment extends BaseConstantFragment {


    private View header;
    private RelativeLayout topic_rl;
    private ImageView error_empty;
    private HomeRecommendAdapter mAdapter;
    private LunboAdapter mLunboAdapter;
    private AutoViewPager mAutoViewPager;

    private CirclePageIndicator mIndicator;
    private ArrayList<UmiwiListBeans> mLunboList;
    private ListViewScrollLoader mScrollLoader;
    private SwipeRefreshLayout refreshLayout;

    private boolean isLunboShow;
    private boolean isRecommendShow;
    private boolean isTopic;

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mLunboList.clear();
//            mList.clear();
            mAdapter.notifyDataSetChanged();
            loadLunboData();
            mScrollLoader.onLoadFirstPage();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_column_layout,null);


        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pull_to_refresh_layout);//如果需要下拉刷新
        refreshLayout.setOnRefreshListener(mOnRefreshListener);
        int color = getResources().getColor(R.color.umiwi_orange);
        refreshLayout.setColorSchemeColors(color, color, Color.YELLOW, Color.WHITE);

        return view;
    }


    /**
     * 轮播数据
     */
    private void loadLunboData() {

        GetRequest<UmiwiListBeans.ChartsListRequestData> request = new GetRequest<UmiwiListBeans.ChartsListRequestData>(
                UmiwiAPI.VIDEO_LUNBO + CommonHelper.getChannelModelViesion(),
                CourseListParser.class, lunboListener);
        HttpDispatcher.getInstance().go(request);
    }
    private AbstractRequest.Listener<UmiwiListBeans.ChartsListRequestData> lunboListener = new AbstractRequest.Listener<UmiwiListBeans.ChartsListRequestData>() {

        @Override
        public void onResult(AbstractRequest<UmiwiListBeans.ChartsListRequestData> request, UmiwiListBeans.ChartsListRequestData t) {
            if (null != t && null != t.getRecord()) {
                mLunboAdapter = new LunboAdapter(getActivity(), t.getRecord());
                mAutoViewPager.setAdapter(mLunboAdapter);
                mIndicator.setViewPager(mAutoViewPager);
                mAutoViewPager.setInterval(5000);
                mAutoViewPager.setSlideBorderMode(AutoViewPager.SLIDE_BORDER_MODE_CYCLE);// 循环。
                mAutoViewPager.setAnimation(new AlphaAnimation(1, (float) 0.2));
                mAutoViewPager.startAutoScroll(5000);
                isLunboShow = true;
                refreshLayout.setRefreshing(false);
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onError(AbstractRequest<UmiwiListBeans.ChartsListRequestData> requet, int statusCode, String body) {
            isLunboShow = false;
            error_empty.setVisibility(View.VISIBLE);
            error_empty.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.refresh_network));
            error_empty.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    loadLunboData();
                    error_empty.setVisibility(View.GONE);
                    if (!isRecommendShow) {
                        mScrollLoader.onLoadErrorPage();
                    }
                }
            });

            refreshLayout.setRefreshing(false);
        }
    };
}

