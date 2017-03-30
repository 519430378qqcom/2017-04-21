package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.HotListAdapter;
import com.umiwi.ui.beans.TheHotListBeans;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.util.JsonUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 热播榜
 * Created by shangshuaibo on 2017/3/12 14:23
 */

public class TheHitListFragment extends BaseConstantFragment {
    private View view;
    private ListView mListView;
    private List<TheHotListBeans.RecordBean> recordBeanList = new ArrayList<>();
    private HotListAdapter hotListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hot_list, null);
        ButterKnife.inject(this, view);
        initView();
        getData();
        return view;
    }

    private void initView() {
        mListView = (ListView) view.findViewById(R.id.listview);
        hotListAdapter = new HotListAdapter(getActivity(), recordBeanList);
        mListView.setAdapter(hotListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TheHotListBeans.RecordBean recordBean = recordBeanList.get(position);
                String from = recordBean.getFrom();
                if ("audioalbum".equals(from)) {
                    Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VoiceDetailsFragment.class);
                    intent.putExtra(VoiceDetailsFragment.KEY_DETAILURL, recordBeanList.get(position).getHrefurl());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
                    intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, recordBeanList.get(position).getHrefurl());
                    startActivity(intent);
                }
            }
        });
    }

    public void getData() {
        OkHttpUtils.get().url(UmiwiAPI.REBO_BANG).build().execute(new CustomStringCallBack() {
            @Override
            public void onFaild() {

            }

            @Override
            public void onSucess(String data) {
                recordBeanList.clear();
                recordBeanList.addAll(JsonUtil.json2Bean(data, TheHotListBeans.class).getRecord());
                hotListAdapter.notifyDataSetChanged();
            }
        });
    }
}
