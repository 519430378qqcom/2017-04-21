package com.umiwi.ui.adapter.updateadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.ArrayList;

import cn.youmi.framework.util.ImageLoader;

/**
 * 首页-推荐-专家问答 - Adapter
 * Created by ${Gpsi} on 2017/2/27.
 */

public class ExpertAnswerAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;

    private ArrayList<RecommendBean.RBean.AsktutorBean> mList;
    private ImageLoader mImageLoader;
    private Activity mActivity;

    public ExpertAnswerAdapter(Context context, ArrayList<RecommendBean.RBean.AsktutorBean> mList) {
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
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.home_expert_answer_item, null);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        RecommendBean.RBean.AsktutorBean asktutorBean = mList.get(position);

        mImageLoader.loadImage(asktutorBean.getThumb(), mViewHolder.iv_expert_answer_header, R.drawable.ic_launcher);
        mViewHolder.tv_expert_name.setText(asktutorBean.getName());
        mViewHolder.expert_job_textview.setText(asktutorBean.getTitle());

        return convertView;
    }

    public class ViewHolder {
        public View rootView;
        public ImageView iv_expert_answer_header;
        //姓名-职位-提问按钮-
        public TextView tv_expert_name, expert_job_textview, tv_expert_put_questions;
        //thumbtype
        public String thumbtype;
        //id 70100476
        public String uid;

        public ViewHolder(View convertView) {
            this.rootView = convertView;
            iv_expert_answer_header = (ImageView) rootView.findViewById(R.id.iv_expert_answer_header);
            tv_expert_name = (TextView) rootView.findViewById(R.id.tv_expert_name);
            expert_job_textview = (TextView) rootView.findViewById(R.id.expert_job_textview);
            thumbtype = "2";
            uid = "0";
        }
    }
}
