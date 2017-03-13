package com.umiwi.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.updateadapter.ExpertAnswerDowndapter;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

/**
 * Created by ${Gpsi} on 2017/3/8.
 */

public class ExpertQuestAnswerFragment extends BaseConstantFragment {


    private Context mContext;
    @InjectView(R.id.iv_back)
    ImageView iv_back;
    @InjectView(R.id.pull_to_refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @InjectView(R.id.lv_expert_question_answer)
    ListView listView;
    ArrayList<RecommendBean.RBean.QuestionBean> questionBeen;
    private ExpertAnswerDowndapter mExpertADAdapter;
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            questionBeen.clear();
            mExpertADAdapter.notifyDataSetChanged();
            initData();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_answer, null);
        ButterKnife.inject(this, view);
        mContext = this.getContext();


        refreshLayout.setOnRefreshListener(mOnRefreshListener);
        int color = getResources().getColor(R.color.umiwi_orange);
        refreshLayout.setColorSchemeColors(color, color, Color.YELLOW, Color.WHITE);

//        initData();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;

    }

    @Override
    public void onResume() {
        initData();
        super.onResume();
    }

    /**
     * 显示视图
     */
    private void initView() {
        if (questionBeen != null) {
            mExpertADAdapter = new ExpertAnswerDowndapter(mContext, questionBeen);
            listView.setAdapter(mExpertADAdapter);
        }
    }


    /**
     * 请求数据
     */
    private void initData() {

        GetRequest<RecommendBean> request = new GetRequest<>(
                UmiwiAPI.VIDEO_TUIJIAN, GsonParser.class, RecommendBean.class, indexActionListener);
        request.go();

    }

    private AbstractRequest.Listener<RecommendBean> indexActionListener = new AbstractRequest.Listener<RecommendBean>() {
        @Override
        public void onResult(AbstractRequest<RecommendBean> request, RecommendBean recommendBean) {
            if (recommendBean != null) {
                questionBeen = recommendBean.getR().getQuestion();
                Log.e("TAG", "questionBean=" + questionBeen.toString());
                initView();
            }
            refreshLayout.setRefreshing(false);
        }

        @Override
        public void onError(AbstractRequest<RecommendBean> requet, int statusCode, String body) {
            refreshLayout.setRefreshing(false);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
