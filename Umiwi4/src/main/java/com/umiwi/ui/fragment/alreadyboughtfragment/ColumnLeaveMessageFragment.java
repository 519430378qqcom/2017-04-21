package com.umiwi.ui.fragment.alreadyboughtfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umiwi.ui.R;
import com.umiwi.ui.main.BaseConstantFragment;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class ColumnLeaveMessageFragment extends BaseConstantFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leave_message,null);
        ButterKnife.inject(this,view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }
}
