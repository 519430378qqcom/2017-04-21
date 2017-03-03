package com.umiwi.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umiwi.ui.R;

import cn.youmi.framework.fragment.BaseFragment;

//import static u.aly.x.R;

/**
 * Created by Administrator on 2017/3/3.
 */

//我答页面
public class MyAnswerFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_answer_fragment, null);
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        setSupportActionBarAndToolbarTitle(mActionBarToolbar, "下载");
        onPostInflateView(view);
        return view;
    }

    private void onPostInflateView(View view) {


    }
}
