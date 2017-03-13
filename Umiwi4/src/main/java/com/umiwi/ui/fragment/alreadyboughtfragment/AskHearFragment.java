package com.umiwi.ui.fragment.alreadyboughtfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.AskHearAdapter;
import com.umiwi.ui.adapter.updateadapter.HearAdapter;
import com.umiwi.ui.beans.updatebeans.AlreadyAskBean;
import com.umiwi.ui.fragment.home.alreadyshopping.AskFragment;
import com.umiwi.ui.fragment.home.alreadyshopping.HearFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.view.RefreshLayout;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

/**
 * 已购-问听
 * Created by ${Gpsi} on 2017/2/23.
 */

public class AskHearFragment extends BaseConstantFragment implements View.OnClickListener {
    private TextView tv_hear;
    private TextView tv_ask;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ask_hear, null);
        initview(view);
        initOnListener();
        return view;
    }

    private void initOnListener() {
        tv_hear.setOnClickListener(this);
        tv_ask.setOnClickListener(this);
    }


    private void initview(View view) {
        replaceFragment(0);
        tv_hear = (TextView) view.findViewById(R.id.tv_hear);
        tv_ask = (TextView) view.findViewById(R.id.tv_ask);
        tv_hear.setSelected(false);
        tv_ask.setSelected(true);
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_hear:
                replaceFragment(1);

                break;
            case R.id.tv_ask:
                replaceFragment(2);

                break;
        }
    }

    private void replaceFragment(int i) {
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (i){
            case 0:
                fragmentTransaction.replace(R.id.content,new AskFragment());
                break;
            case 1:
                tv_hear.setSelected(true);
                tv_ask.setSelected(false);
                fragmentTransaction.replace(R.id.content,new HearFragment());
                break;
            case 2:
                tv_hear.setSelected(false);
                tv_ask.setSelected(true);
                fragmentTransaction.replace(R.id.content,new AskFragment());
                break;
        }
         fragmentTransaction.commit();

    }

}
