package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.umiwi.ui.R;
import com.umiwi.ui.adapter.ExperDetailsVideoAdapter;
import com.umiwi.ui.beans.ActivityItemBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.view.NoScrollListview;
import java.util.ArrayList;

/**
 * 行家详情视频
 * Created by Administrator on 2017/2/28.
 */

public class ExperDetailsVideoFragment extends BaseConstantFragment {
    private NoScrollListview listView;
    private ArrayList<ActivityItemBean> mList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exper_details_video_layout, null);
        listView = (NoScrollListview) view.findViewById(R.id.noscroll_listview);
        mList = new ArrayList<>();
        ExperDetailsVideoAdapter experDetailsVideoAdapter = new ExperDetailsVideoAdapter(getActivity(), mList);
        listView.setAdapter(experDetailsVideoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        return view;
    }
}
