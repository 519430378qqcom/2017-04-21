package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.ExperDetailsVoiceAdapter;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.view.NoScrollListview;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 行家详情音频
 * Created by Administrator on 2017/2/28.
 */

public class ExperDetailsVoiceFragment extends BaseConstantFragment {
    @InjectView(R.id.noscroll_listview)
    NoScrollListview noscrollListview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exper_details_voice_layout, null);
        ButterKnife.inject(this, view);
        ExperDetailsVoiceAdapter experDetailsVoiceAdapter = new ExperDetailsVoiceAdapter(getActivity());
        noscrollListview.setAdapter(experDetailsVoiceAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
