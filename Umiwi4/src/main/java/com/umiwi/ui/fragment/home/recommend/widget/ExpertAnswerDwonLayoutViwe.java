package com.umiwi.ui.fragment.home.recommend.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.updateadapter.ExpertAnswerAdapter;
import com.umiwi.ui.adapter.updateadapter.ExpertAnswerDowndapter;
import com.umiwi.ui.beans.updatebeans.RecommendBean;

import java.util.ArrayList;

/**
 * 首页-推荐-行家回答
 * Created by ${Gpsi} on 2017/2/27.
 */
public class ExpertAnswerDwonLayoutViwe extends LinearLayout {

    private Context mContext;
    private LinearLayout expert_answer_dwon_root;
    private ListView lv_expert_dwon_answer;
    private TextView tv_expert_answer_fask_ask;
    private ExpertAnswerDowndapter mExpertADAdapter;
    private ArrayList<RecommendBean.RBean.QuestionBean> mList;

    public ExpertAnswerDwonLayoutViwe(Context context) {
        super(context);
        init(context);
    }

    public ExpertAnswerDwonLayoutViwe(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.home_expert_answer_dwon_layout, this);
        expert_answer_dwon_root = (LinearLayout) findViewById(R.id.expert_answer_dwon_root);
        lv_expert_dwon_answer = (ListView) findViewById(R.id.lv_expert_dwon_answer);
        tv_expert_answer_fask_ask = (TextView) findViewById(R.id.tv_expert_answer_fask_ask);
        expert_answer_dwon_root.setVisibility(GONE);
    }

    public void setData(ArrayList<RecommendBean.RBean.QuestionBean> questionBeen, String fask_ask) {

        tv_expert_answer_fask_ask.setText(fask_ask);
        mList = questionBeen;
        if (null == mList || mList.size() == 0)
            return;
        expert_answer_dwon_root.setVisibility(VISIBLE);
        mExpertADAdapter = new ExpertAnswerDowndapter(mContext, mList);
        lv_expert_dwon_answer.setAdapter(mExpertADAdapter);

    }
}
