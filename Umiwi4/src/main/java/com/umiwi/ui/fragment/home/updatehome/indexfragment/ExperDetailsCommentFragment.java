package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.ExperDetailsCommentAdapter;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.view.NoScrollListview;
import com.umiwi.ui.view.TopFloatScrollView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 行家详情评论
 * Created by Administrator on 2017/2/28.
 */

public class ExperDetailsCommentFragment extends BaseConstantFragment {
    @InjectView(R.id.noscroll_listview)
    NoScrollListview noscrollListview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exper_details_comment_layout, null);
        ButterKnife.inject(this, view);
        ExperDetailsCommentAdapter experDetailsCommentAdapter = new ExperDetailsCommentAdapter(getActivity());
        noscrollListview.setAdapter(experDetailsCommentAdapter);

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
