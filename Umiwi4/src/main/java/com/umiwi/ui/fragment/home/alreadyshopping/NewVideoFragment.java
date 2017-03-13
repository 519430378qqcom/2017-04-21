package com.umiwi.ui.fragment.home.alreadyshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.NewVideoAdapter;
import com.umiwi.ui.beans.VideoBean;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

/**
 * Created by LvDabing on 2017/2/16.
 * Email：lvdabing@lvshandian.com
 * Detail:视频
 */

public class NewVideoFragment extends BaseConstantFragment {

    private ListView listView;
    private LoadingFooter mLoadingFooter;
    private ListViewScrollLoader mScrollLoader;

    private String price = "";//free-免费，charge-收费
    private String orderby = "ctime";//ctime-最新，watchnum-最热,usefulnum-好评

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_layout_new, null);

        onLoadData(1);
        initView(view);
        return view;
    }

    private void initView(View view) {

        listView = (ListView) view.findViewById(R.id.listView);

        mLoadingFooter = new LoadingFooter(getActivity());
        listView.addFooterView(mLoadingFooter.getView());

        mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);
        listView.setOnScrollListener(mScrollLoader);

        mScrollLoader.onLoadFirstPage();
    }

    private int p = 1;
    private List<VideoBean.RecordBean> mList = new ArrayList<>();

    public void onLoadData(final int page) {
        String url = UmiwiAPI.Login_Video + "?orderby=" + orderby;

        if (price != null && price != "") {
            url += "&price=" + price;
        }

        if (String.valueOf(p) != null && String.valueOf(p) != "") {
            url += "&p=" + page;
        }

        Log.e("MZX", url);
        OkHttpUtils.get().url(url).build().execute(new CustomStringCallBack() {
            private NewVideoAdapter videoAdapter;

            @Override
            public void onFaild() {
//                mScrollLoader.onLoadErrorPage();
            }

            @Override
            public void onSucess(String data) {
                Log.e("MZX", "数据进行请求成功了--x-" + data);

                VideoBean audioBean = new Gson().fromJson(data, VideoBean.class);
                VideoBean.PageBean pagebean = audioBean.getPage();

                if (audioBean.getPage().getCurrentpage() >= audioBean.getPage().getTotalpage()) {
                    mLoadingFooter.setState(LoadingFooter.State.NoMore);
                    mScrollLoader.setEnd(true);
                    return;
                }

                if (pagebean.getCurrentpage() == 1) {
                    mList.clear();
                }

                mScrollLoader.setPage(pagebean.getCurrentpage());
                mScrollLoader.setloading(false);

                if (mList == null) {
                    mList = audioBean.getRecord();
                } else {
                    mList.addAll(audioBean.getRecord());
                }
                videoAdapter = new NewVideoAdapter(getActivity(), mList);
                listView.setAdapter(videoAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
                        intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, mList.get(position).getId());

                        getActivity().startActivity(intent);
                    }
                });
            }
        });
    }
}