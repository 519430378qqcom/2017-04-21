package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.StageSectionAdapter;
import com.umiwi.ui.beans.StageSectionBean;
import com.umiwi.ui.fragment.search.SearchFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

/**
 * Created by LvDabing on 2017/2/16.
 * Email：lvdabing@lvshandian.com
 * Detail: 老优米
 */

public class OldYoumiFragment   extends BaseConstantFragment {
 /*   @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_old_youmi_layout,null);
        return view;
    }*/
    private ExpandableListView stageSectionListView;

    private StageSectionAdapter stageSectionAdapter;

    private LoadingFooter mLoadingFooter;
    private ListViewScrollLoader mScrollLoader;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stage_section, null);

//        TextView title = (TextView) view.findViewById(R.id.title);
//        title.setText("分类");
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        mActionBarToolbar.inflateMenu(R.menu.toolbar_search);
        mActionBarToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
                i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, SearchFragment.class);
                startActivity(i);
                return true;
            }
        });

        stageSectionListView = (ExpandableListView) view.findViewById(R.id.expandableListView);

        mLoadingFooter = new LoadingFooter(getActivity());
        stageSectionListView.addFooterView(mLoadingFooter.getView());

        // don't allowed contract
        stageSectionListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent,
                                        View v, int groupPosition, long id) {
                return true;
            }
        });

        mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);

        stageSectionAdapter = new StageSectionAdapter(getActivity());
        stageSectionListView.setAdapter(stageSectionAdapter);

        return view;
    }

    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();
//		if (stageSectionAdapter == null
//				|| stageSectionAdapter.getDatas() == null) {
//
//		}
        mScrollLoader.onLoadErrorPage();
    }

    @Override
    public void onLoadData() {
        GetRequest<StageSectionBean.StageSectionBeanRequestData> request = new GetRequest<StageSectionBean.StageSectionBeanRequestData>(
                UmiwiAPI.CATEGORY_LIST, GsonParser.class,
                StageSectionBean.StageSectionBeanRequestData.class, stageSectionListner);
        HttpDispatcher.getInstance().go(request);
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

    private AbstractRequest.Listener<StageSectionBean.StageSectionBeanRequestData> stageSectionListner = new AbstractRequest.Listener<StageSectionBean.StageSectionBeanRequestData>() {
        @Override
        public void onResult(
                AbstractRequest<StageSectionBean.StageSectionBeanRequestData> request,
                StageSectionBean.StageSectionBeanRequestData t) {
            if (t == null) {
                mLoadingFooter.setState(LoadingFooter.State.TheEnd);
                return;
            }

            if (t.getRecord() == null && t.getRecord().size() == 0) {
                mLoadingFooter.setState(LoadingFooter.State.TheEnd);
                return;
            }

            stageSectionAdapter.setData(t.getRecord());
            int groupCount = t.getRecord().size();
            for (int i = 0; i < groupCount; i++) {
                stageSectionListView.expandGroup(i);
            }
            mLoadingFooter.setState(LoadingFooter.State.TheEnd);

        }

        @Override
        public void onError(
                AbstractRequest<StageSectionBean.StageSectionBeanRequestData> requet,
                int statusCode, String body) {
            mLoadingFooter.setState(LoadingFooter.State.Error);
            mLoadingFooter.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mScrollLoader.onLoadErrorPage();
                }
            });
        }

    };

}