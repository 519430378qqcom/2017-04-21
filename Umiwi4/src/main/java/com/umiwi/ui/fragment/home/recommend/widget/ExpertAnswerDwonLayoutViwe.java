package com.umiwi.ui.fragment.home.recommend.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.updateadapter.ExpertAnswerDowndapter;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.fragment.home.alreadyshopping.AnswerDetailsFragment;
import com.umiwi.ui.managers.YoumiRoomUserManager;

import java.util.ArrayList;

/**
 * 首页-推荐-行家回答
 * Created by ${Gpsi} on 2017/2/27.
 */
public class ExpertAnswerDwonLayoutViwe extends LinearLayout {

    private Activity mContext;
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
        mContext = (Activity) context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.home_expert_answer_dwon_layout, this);
        expert_answer_dwon_root = (LinearLayout) findViewById(R.id.expert_answer_dwon_root);
        lv_expert_dwon_answer = (ListView) findViewById(R.id.lv_expert_dwon_answer);
        lv_expert_dwon_answer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RecommendBean.RBean.QuestionBean question = mList.get(i);
                String title = question.getTitle();
                String buttontag = question.getButtontag();
                String tavatar = question.getTavatar();
                String playtime = question.getPlaytime();
                String answertime = question.getAnswertime();
                String listennum = question.getListennum();
                String goodnum = question.getGoodnum();
                String id = question.getId();
                String tuid = question.getTuid();
                Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, AnswerDetailsFragment.class);
                intent.putExtra("title",title);
                intent.putExtra("buttontag",buttontag);
                intent.putExtra("tavatar",tavatar);
                intent.putExtra("playtime",playtime);
                intent.putExtra("answertime",answertime);
                intent.putExtra("listennum",listennum);
                intent.putExtra("goodnum",goodnum);
                intent.putExtra("id",id);
                intent.putExtra("uid",tuid);
                mContext.startActivity(intent);
            }
        });
        tv_expert_answer_fask_ask = (TextView) findViewById(R.id.tv_expert_answer_fask_ask);
        expert_answer_dwon_root.setVisibility(GONE);
    }

    public void setData(ArrayList<RecommendBean.RBean.QuestionBean> questionBeen, String fask_ask) {

        tv_expert_answer_fask_ask.setText(fask_ask);
        Log.e("TAG", "fask_ask=" + fask_ask);
        mList = questionBeen;
        if (null == mList || mList.size() == 0)
            return;
        expert_answer_dwon_root.setVisibility(VISIBLE);
        mExpertADAdapter = new ExpertAnswerDowndapter(mContext, mList);
        lv_expert_dwon_answer.setAdapter(mExpertADAdapter);
        handler.sendEmptyMessageDelayed(0, 2000);

        //快速提问
        tv_expert_answer_fask_ask.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mExpertADAdapter.notifyDataSetChanged();
            super.handleMessage(msg);
        }
    };
}
