package com.umiwi.ui.fragment.home.alreadyshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.ColumnAdapter;
import com.umiwi.ui.beans.ActivityItemBean;
import com.umiwi.ui.beans.updatebeans.HomeCoumnBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.util.JsonUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

/**
 * Created by LvDabing on 2017/2/16.
 * Email：lvdabing@lvshandian.com
 * Detail:专栏
 */

public class ColumnFragment extends BaseConstantFragment {

    private ListView listView;
    private ArrayList<ActivityItemBean> mList;
    private LoadingFooter loadingFooter;
    private ListViewScrollLoader mScrollLoader;
    private ColumnAdapter columnAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_column_layout,null);
        listView = (ListView) view.findViewById(R.id.listView);
        mList = new ArrayList<>();
        columnAdapter = new ColumnAdapter(getActivity(), mList);

        loadingFooter = new LoadingFooter(getActivity());
        listView.addFooterView(loadingFooter.getView());

        mScrollLoader = new ListViewScrollLoader(this, loadingFooter);
        listView.setOnScrollListener(mScrollLoader);
        listView.setAdapter(columnAdapter);

        mScrollLoader.onLoadFirstPage();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LogicalThinkingFragment.class);
                startActivity(intent);
            }
        });
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
        MobclickAgent.onPageStart(fragmentName);
    }

    @Override
    public void onLoadData(int page) {
        String url = UmiwiAPI.TUTORCOLUMN+page;
        OkHttpUtils.get().url(url).build().execute(new CustomStringCallBack() {
            @Override
            public void onFaild() {
                Log.e("data","首页专栏列表请求数据失败");
            }

            @Override
            public void onSucess(String data) {
                Log.e("data","首页专栏列表请求数据成功"+data);
                if (!TextUtils.isEmpty(data)){
                    HomeCoumnBean homeCoumnBean = JsonUtil.json2Bean(data, HomeCoumnBean.class);
                    Log.e("data",homeCoumnBean.getPage().getTotalpage()+"");
                }

            }
        });
    }
}

