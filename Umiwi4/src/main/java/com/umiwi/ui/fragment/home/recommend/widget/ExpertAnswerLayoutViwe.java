package com.umiwi.ui.fragment.home.recommend.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.updateadapter.ExpertAnswerAdapter;
import com.umiwi.ui.adapter.updateadapter.LineActionadapter;
import com.umiwi.ui.beans.updatebeans.RecommendBean;

import java.util.ArrayList;

/**
 * 首页-推荐-行家回答
 * Created by ${Gpsi} on 2017/2/27.
 */
public class ExpertAnswerLayoutViwe extends LinearLayout {

    private Context mContext;
    private LinearLayout expert_answer_root;
    private TextView tv_expert_answer_title,tv_experttitle_tag,tv_expert_answer_fask_ask;
    private ListView lv_expert_answer;
    private ExpertAnswerAdapter mExpertAnswerAdapter;
    private ArrayList<RecommendBean.RBean.AsktutorBean> mList;

    public ExpertAnswerLayoutViwe(Context context) {
        super(context);
        init(context);
    }

    public ExpertAnswerLayoutViwe(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.expert_answer_layout, this);
        expert_answer_root = (LinearLayout) findViewById(R.id.expert_answer_root);
        tv_expert_answer_title = (TextView) findViewById(R.id.tv_expert_answer_title);
        tv_experttitle_tag = (TextView) findViewById(R.id.tv_experttitle_tag);
        tv_expert_answer_fask_ask = (TextView) findViewById(R.id.tv_expert_answer_fask_ask);
        lv_expert_answer = (ListView) findViewById(R.id.lv_expert_answer);
        expert_answer_root.setVisibility(GONE);
    }

    public void setData(ArrayList<RecommendBean.RBean.AsktutorBean> asktutorBeen, String answerTitle,String experttitleTag,String faskAsk) {

        tv_expert_answer_title.setText(answerTitle);
        tv_experttitle_tag.setText(experttitleTag);
        tv_expert_answer_fask_ask.setText(faskAsk);
        mList = asktutorBeen;
        if (null == mList || mList.size() == 0)
            return;
        expert_answer_root.setVisibility(VISIBLE);
        mExpertAnswerAdapter = new ExpertAnswerAdapter(mContext, mList);
        lv_expert_answer.setAdapter(mExpertAnswerAdapter);

    }
}
