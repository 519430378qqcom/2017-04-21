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
import com.umiwi.ui.adapter.BuyColumnAdapter;
import com.umiwi.ui.beans.updatebeans.BuyCoumnBean;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.ColumnDetailsFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.util.JsonUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

/**
 * 类描述：已购-专栏
 * Created by Gpsi on 2017-03-13.
 */

public class BuyCoumnFragment extends BaseConstantFragment {

    private ListView listView;
    private List<BuyCoumnBean.RecordBean> mList;
    private LoadingFooter loadingFooter;
    private ListViewScrollLoader mScrollLoader;
    private BuyColumnAdapter buyColumnAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_column_layout,null);
        listView = (ListView) view.findViewById(R.id.listView);

        mList = new ArrayList<>();

        loadingFooter = new LoadingFooter(getActivity());
        listView.addFooterView(loadingFooter.getView());

        mScrollLoader = new ListViewScrollLoader(this, loadingFooter);
        listView.setOnScrollListener(mScrollLoader);

        mScrollLoader.onLoadFirstPage();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
//                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ColumnDetailsFragment.class);//详情页
//                intent.putExtra("columnurl", mList.get(i).getColumnurl());
//                startActivity(intent);
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LogicalThinkingFragment.class);
                intent.putExtra("id",mList.get(i).getId());
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

        Log.e("TAG",url);
        OkHttpUtils.get().url(url).build().execute(new CustomStringCallBack() {
            @Override
            public void onFaild() {
                Log.e("data","首页专栏列表请求数据失败");
            }

            @Override
            public void onSucess(String data) {
                Log.e("data","首页专栏列表请求数据成功"+data);
                if (!TextUtils.isEmpty(data)){
                    BuyCoumnBean buyCoumnBean = JsonUtil.json2Bean(data, BuyCoumnBean.class);
//                    BuyCoumnBean.PageBean pageBean = buyCoumnBean.getPage();
                    BuyCoumnBean.PageBean pageBean = buyCoumnBean.getPage();

                    if(buyCoumnBean.getPage().getCurrentpage() >= buyCoumnBean.getPage().getTotalpage()){
                        mScrollLoader.setEnd(true);
                        loadingFooter.setState(LoadingFooter.State.NoMore);
                        return;
                    }

                    if(pageBean.getCurrentpage() == 1){
                        mList.clear();
                    }

                    mScrollLoader.setPage(pageBean.getCurrentpage());
                    mScrollLoader.setloading(false);

                    if (mList == null) {
                        mList = buyCoumnBean.getRecord();
                    } else {
//                        if(homeCoumnBean.getPage().getCurrentpage() == 1){
//                            mList.clear();
//                        }
                        mList.addAll(buyCoumnBean.getRecord());
                    }

                    buyColumnAdapter = new BuyColumnAdapter(getActivity(), mList);
                    listView.setAdapter(buyColumnAdapter);
                }
            }
        });
    }
}
