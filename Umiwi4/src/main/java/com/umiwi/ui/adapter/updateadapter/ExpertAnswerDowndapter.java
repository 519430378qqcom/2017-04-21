package com.umiwi.ui.adapter.updateadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.AskQuestionFragment;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.view.CircleImageView;

import java.util.ArrayList;

import cn.youmi.framework.util.ImageLoader;

/**
 * 首页-推荐-行家回答 下部分- Adapter
 * Created by ${Gpsi} on 2017/2/28.
 */

public class ExpertAnswerDowndapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;

    private ArrayList<RecommendBean.RBean.QuestionBean> mList;
    private ImageLoader mImageLoader;
    private Activity mActivity;
    public ExpertAnswerDowndapter(Context context, ArrayList<RecommendBean.RBean.QuestionBean> mList){
        mLayoutInflater = LayoutInflater.from(context);
        this.mActivity = (Activity) context;
        this.mList = mList;
        mImageLoader = new ImageLoader(UmiwiApplication.getApplication());

    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.home_expert_answer_down_item,null);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final RecommendBean.RBean.QuestionBean questionBean = mList.get(position);

        mImageLoader.loadImage(questionBean.getTavatar(),mViewHolder.iv_expert_header,R.drawable.ic_launcher);
        mViewHolder.tv_expert_question.setText(questionBean.getTitle());
        mViewHolder.tv_time_limit_hear.setText(questionBean.getButtontag());
        mViewHolder.tv_head_time.setText(questionBean.getPlaytime());
        mViewHolder.tv_time_limit_number.setText("听过 "+questionBean.getListennum()+"人");
        mViewHolder.tv_expert_question_name.setText(questionBean.getTname());
        mViewHolder.tv_expert_question_job.setText(questionBean.getTtitle());

        mViewHolder.iv_expert_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent (mActivity,UmiwiContainerActivity.class);
//                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VoiceDetailsFragment.class);
//                intent.putExtra(VoiceDetailsFragment.KEY_DETAILURL,);
//                mActivity.startActivity(intent);
                Intent intent = new Intent(mActivity, UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, AskQuestionFragment.class);
                intent.putExtra("uid",questionBean.getTuid());
                mActivity.startActivity(intent);
            }
        });
        return convertView;
    }
    public class ViewHolder{
        public View rootView;
        public ImageView iv_expert_header;
        //微信小程序可以直播么？- 限时免费听 - 58″ - 听过55555人 - 姓名 - 职位
        public TextView tv_expert_question, tv_time_limit_hear, tv_head_time
                ,tv_time_limit_number,tv_expert_question_name,tv_expert_question_job;
        //id:2
        public String id;
        //tuid:7106276
        public String tuid;
        //listentype:2
        public String listentype;
        //playsource:http://i.v.youmi.cn/question/playsourceapi?id=2
        public String playsource;

        public ViewHolder(View convertView) {
            this.rootView = convertView;
            iv_expert_header = (CircleImageView) rootView.findViewById(R.id.expert_header_imageview);
            tv_expert_question = (TextView) rootView.findViewById(R.id.expert_question_textView_1);
            tv_time_limit_hear = (TextView) rootView.findViewById(R.id.time_limit_hear_textview_1);
            tv_head_time = (TextView) rootView.findViewById(R.id.head_time_textview_1);
            tv_time_limit_number = (TextView) rootView.findViewById(R.id.time_limit_number_textview_1);
            tv_expert_question_name = (TextView) rootView.findViewById(R.id.expert_question_name_textView_1);
            tv_expert_question_job = (TextView) rootView.findViewById(R.id.expert_question_job_textview_1);

        }
    }
}
