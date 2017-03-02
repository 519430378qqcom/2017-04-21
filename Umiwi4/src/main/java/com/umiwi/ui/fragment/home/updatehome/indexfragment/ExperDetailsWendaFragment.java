package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.ExperDetailsWendaAdapter;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.view.NoScrollListview;
import com.umiwi.ui.view.TopFloatScrollView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 行家详情问答
 * Created by Administrator on 2017/2/28.
 */

public class ExperDetailsWendaFragment extends BaseConstantFragment {

    @InjectView(R.id.noscroll_listview)
    NoScrollListview noscrollListview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exper_details_wenda_layout, null);
        ButterKnife.inject(this, view);
        ExperDetailsWendaAdapter experDetailsWendaAdapter = new ExperDetailsWendaAdapter(getActivity());
        noscrollListview.setAdapter(experDetailsWendaAdapter);

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
