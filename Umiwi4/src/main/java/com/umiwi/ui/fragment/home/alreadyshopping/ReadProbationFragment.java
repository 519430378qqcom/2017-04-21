package com.umiwi.ui.fragment.home.alreadyshopping;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umiwi.ui.R;
import com.umiwi.ui.main.BaseConstantFragment;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class ReadProbationFragment extends BaseConstantFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read_probation,null);

        ButterKnife.inject(this,view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }
}
