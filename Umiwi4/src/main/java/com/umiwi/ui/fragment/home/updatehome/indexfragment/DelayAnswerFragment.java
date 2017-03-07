package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.umiwi.ui.R;

import butterknife.ButterKnife;
import cn.youmi.framework.fragment.BaseFragment;

/**
 * Created by Administrator on 2017/3/3.
 */

//待回答页面
public class DelayAnswerFragment extends BaseFragment {

    private ListView lv_answer_answered;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delay_answer_fragment, null);
        return view;
    }
}
