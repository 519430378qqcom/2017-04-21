package com.umiwi.ui.fragment.home.alreadyshopping;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.LogicalThinkingAdapter;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.view.RefreshLayout;



import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by Administrator on 2017/2/24.
 */

public class LogicalThinkingFragment extends BaseConstantFragment {
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private String id;
    private ImageView iv_back;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logical_thinking, null);
        id = getActivity().getIntent().getStringExtra("id");
        listView = (ListView) view.findViewById(R.id.listview);
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        initRrefreshView();
        return view;
    }

    private void initRrefreshView() {
        refreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
