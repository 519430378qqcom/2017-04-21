package com.umiwi.ui.fragment.alreadyboughtfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.AskHearAdapter;
import com.umiwi.ui.adapter.ColumnAdapter;
import com.umiwi.ui.beans.updatebeans.HomeCoumnBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.util.JsonUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;

import cn.youmi.framework.view.LoadingFooter;

import static android.R.attr.data;
import static com.umiwi.ui.R.id.listView;
import static u.aly.x.S;

/**
 * 已购-问听
 * Created by ${Gpsi} on 2017/2/23.
 */

public class AskHearFragment extends BaseConstantFragment {

    private TextView tv_hear;
    private TextView tv_ask;

    private ListView listview;
    private ArrayList mList = new ArrayList();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_ask_hear,null);

        tv_hear = (TextView) view.findViewById(R.id.tv_hear);
        tv_hear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_hear.setSelected(true);
                tv_ask.setSelected(false);
            }
        });
        tv_ask = (TextView) view.findViewById(R.id.tv_ask);
        tv_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_hear.setSelected(false);
                tv_ask.setSelected(true);
            }
        });
        tv_hear.setSelected(true);
        tv_ask.setSelected(false);

        listview = (ListView) view.findViewById(R.id.listview);
        listview.setAdapter(new AskHearAdapter(getActivity(),mList));
        return view;
    }

    @Override
    public void onLoadData(int page) {
        super.onLoadData();
        String url = UmiwiAPI.Ask_Hear+"?p="+page;

        OkHttpUtils.get().url(url).build().execute(new CustomStringCallBack() {
            @Override
            public void onFaild() {
                Log.e("data","听失败");
            }

            @Override
            public void onSucess(String data) {
                Log.e("data","听成功"+data);
                if (!TextUtils.isEmpty(data)){

                }
            }
        });


    }
}
