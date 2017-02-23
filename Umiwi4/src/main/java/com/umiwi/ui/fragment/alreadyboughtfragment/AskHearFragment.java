package com.umiwi.ui.fragment.alreadyboughtfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umiwi.ui.R;
import com.umiwi.ui.main.BaseConstantFragment;

/**
 * 已购-问听
 * Created by ${Gpsi} on 2017/2/23.
 */

public class AskHearFragment extends BaseConstantFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_ask_hear,null);
        return view;
    }
}
