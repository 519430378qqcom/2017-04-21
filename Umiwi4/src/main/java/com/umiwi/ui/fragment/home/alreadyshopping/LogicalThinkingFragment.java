package com.umiwi.ui.fragment.home.alreadyshopping;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.ColumnAdapter;
import com.umiwi.ui.adapter.LogicalThinkingAdapter;
import com.umiwi.ui.main.BaseConstantFragment;

import java.util.ArrayList;

import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

import static com.umiwi.ui.R.id.listView;

/**
 * Created by Administrator on 2017/2/24.
 */

public class LogicalThinkingFragment extends BaseConstantFragment {
   private ArrayList mList;
    private LoadingFooter loadingFooter;
    private ListView mListview;
    private ListViewScrollLoader mScrollLoader;
    private LogicalThinkingAdapter logicalThinkingAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logical_thinking, null);
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
