package com.umiwi.ui.fragment.home.recommend.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.updateadapter.ExpertAnswerAdapter;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.fragment.ExpertQuestAnswerFragment;

import java.util.ArrayList;

/**
 * 首页-推荐-行家回答
 * Created by ${Gpsi} on 2017/2/27.
 */
public class ExpertAnswerLayoutViwe extends LinearLayout {

    private Context mContext;
    private LinearLayout expert_answer_root;
    private TextView tv_expert_answer_title,tv_experttitle_tag;
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
        inflater.inflate(R.layout.home_expert_answer_layout, this);
        expert_answer_root = (LinearLayout) findViewById(R.id.expert_answer_root);
        tv_expert_answer_title = (TextView) findViewById(R.id.tv_expert_answer_title);
        tv_experttitle_tag = (TextView) findViewById(R.id.tv_experttitle_tag);
        lv_expert_answer = (ListView) findViewById(R.id.lv_expert_answer);
        expert_answer_root.setVisibility(GONE);
    }

    public void setData(ArrayList<RecommendBean.RBean.AsktutorBean> asktutorBeen, String answerTitle, final String experttitleTag) {

        tv_expert_answer_title.setText(answerTitle);
        tv_experttitle_tag.setText(experttitleTag);
//        Log.e("TAG", "experttitleTag=" + experttitleTag);
        mList = asktutorBeen;
        if (null == mList || mList.size() == 0)
            return;
        expert_answer_root.setVisibility(VISIBLE);
        mExpertAnswerAdapter = new ExpertAnswerAdapter(mContext, mList);
        lv_expert_answer.setAdapter(mExpertAnswerAdapter);
        tv_experttitle_tag.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "experttitleTag=" + experttitleTag);
                Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ExpertQuestAnswerFragment.class);
                mContext.startActivity(intent);
            }
        });
    }
}
