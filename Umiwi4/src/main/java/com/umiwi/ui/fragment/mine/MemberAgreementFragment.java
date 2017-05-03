package com.umiwi.ui.fragment.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umiwi.ui.R;
import com.umiwi.ui.main.BaseConstantFragment;

/**
 * Created by Administrator on 2017/5/3 0003.
 */

public class MemberAgreementFragment extends BaseConstantFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_agreement,null);
        return view;
    }
}
