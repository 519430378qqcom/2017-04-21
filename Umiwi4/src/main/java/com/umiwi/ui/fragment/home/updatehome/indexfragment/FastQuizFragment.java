package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.FastQuizAdapter;
import com.umiwi.ui.beans.FastQuizBeans;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.util.JsonUtil;
import com.umiwi.ui.view.FlowLayout;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.util.ToastU;

/**
 * Created by Administrator on 2017/3/14.
 */

public class FastQuizFragment extends BaseConstantFragment implements FastQuizAdapter.FastQuizClickListener {
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.tv_all_catid)
    TextView tvAllCatid;
    @InjectView(R.id.flow_catid)
    FlowLayout flowCatid;
    @InjectView(R.id.listview)
    ListView listview;
    private List<String> catidList = new ArrayList<>();//分类
    private List<FastQuizBeans.RecordBean> recordBeanList = new ArrayList<>();
    private FastQuizAdapter fastQuizAdapter;
    private Context mContext;
    private String catid = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_fast_quiz, null);
        ButterKnife.inject(this, inflate);
        mContext = getActivity();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        initFlowData();
        initFlowLayout();
        fastQuizAdapter = new FastQuizAdapter(mContext, recordBeanList);
        fastQuizAdapter.setOnFastQuizClickListener(this);
        listview.setAdapter(fastQuizAdapter);
        getinfos();
        return inflate;
    }

    private void initFlowData() {
        catidList.add("互联网");
        catidList.add("媒体");
        catidList.add("教育培训");
    }

    /**
     * 初始化流部局
     */
    private void initFlowLayout() {
        flowCatid.removeAllViews();
        for (int i = 0, j = catidList.size(); i < j; i++) {
            final TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R
                    .layout.flow_text, flowCatid, false);
            tv.setText(catidList.get(i));
            tv.setTextColor(mContext.getResources().getColor(R.color.gray_a));
            tvAllCatid.setTextColor(mContext.getResources().getColor(R.color.main_color));
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    catid = catidList.get(finalI);
                    getinfos();
                    for (int i = 0, j = catidList.size(); i < j; i++) {
                        TextView tv = (TextView) flowCatid.getChildAt(i);
                        tv.setTextColor(mContext.getResources().getColor(R.color.gray_a));
                    }
                    tv.setTextColor(mContext.getResources().getColor(R.color.main_color));
                    tvAllCatid.setTextColor(mContext.getResources().getColor(R.color.gray_a));
                }
            });
            flowCatid.addView(tv);
        }

        tvAllCatid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catid = "";
                getinfos();
                for (int i = 0, j = catidList.size(); i < j; i++) {
                    TextView tv = (TextView) flowCatid.getChildAt(i);
                    tv.setTextColor(mContext.getResources().getColor(R.color.gray_a));
                }
                tvAllCatid.setTextColor(mContext.getResources().getColor(R.color.main_color));
            }
        });

    }

    private void getinfos() {
        OkHttpUtils.get().url(String.format(UmiwiAPI.FAST_QUIZ, catid, 1)).build().execute(new CustomStringCallBack() {
            @Override
            public void onFaild() {

            }

            @Override
            public void onSucess(String data) {
                recordBeanList.clear();
                FastQuizBeans fastQuizBeans = JsonUtil.json2Bean(data, FastQuizBeans.class);
                recordBeanList.addAll(fastQuizBeans.getRecord());
                fastQuizAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void OnFastQuizClickListener(View view, int position) {
        Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, AskQuestionFragment.class);
        intent.putExtra("uid", recordBeanList.get(position).getUid());
        startActivity(intent);
    }
}
