package com.umiwi.ui.fragment.home.alreadyshopping;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.ColumnAdapter;
import com.umiwi.ui.adapter.LogicalThinkingAdapter;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;

import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

import static android.R.attr.id;
import static com.umiwi.ui.R.id.listView;
import static u.aly.x.S;

/**
 * Created by Administrator on 2017/2/24.
 */

public class LogicalThinkingFragment extends BaseConstantFragment {
   private ArrayList mList;
    private LoadingFooter loadingFooter;
    private ListView mListview;
    private ListViewScrollLoader mScrollLoader;
    private LogicalThinkingAdapter logicalThinkingAdapter;
    private String id;

    private ImageView iv_back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logical_thinking, null);
        id = getActivity().getIntent().getStringExtra("id");

        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        mListview = (ListView) view.findViewById(listView);
        mList = new ArrayList<>();
        logicalThinkingAdapter = new LogicalThinkingAdapter(getActivity(), mList);

        loadingFooter = new LoadingFooter(getActivity());
        mListview.addFooterView(loadingFooter.getView());

        mScrollLoader = new ListViewScrollLoader(this, loadingFooter);

        mListview.setOnScrollListener(mScrollLoader);
        mListview.setAdapter(logicalThinkingAdapter);
        mScrollLoader.onLoadFirstPage();
        return view;
    }
}
