package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.view.FlowLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/3/14.
 */

public class FastQuizFragment extends BaseConstantFragment {
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.tv_all_catid1)
    TextView tvAllCatid1;
    @InjectView(R.id.flow_catid1)
    FlowLayout flowCatid1;
    @InjectView(R.id.listview)
    ListView listview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_fast_quiz, null);

        ButterKnife.inject(this, inflate);

        return inflate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
