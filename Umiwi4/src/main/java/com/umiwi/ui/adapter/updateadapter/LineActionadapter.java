package com.umiwi.ui.adapter.updateadapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
 * 首页-推荐-热门活动 - Adapter
 * Created by ${Gpsi} on 2017/2/27.
 */

public class LineActionadapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;

    private ArrayList<RecommendBean.RBean.HuodongBean> mList;
    private ImageLoader mImageLoader;
    private Activity mActivity;
    public LineActionadapter(Context context,ArrayList<RecommendBean.RBean.HuodongBean> mList){
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
            convertView = mLayoutInflater.inflate(R.layout.line_action_item,null);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        RecommendBean.RBean.HuodongBean huodongBean = mList.get(position);

        mImageLoader.loadImage(huodongBean.getImage(),mViewHolder.line_action_imageview_1,R.drawable.ic_launcher);
        mViewHolder.line_action_name_1.setText(huodongBean.getTitle());
        mViewHolder.line_action_context_1.setText(huodongBean.getContent());
        mViewHolder.status = huodongBean.getStatus();
        if (mViewHolder.status.toString().equals("2")){
            //TODO
            mViewHolder.line_action_static_1.setText("报名中");
            mViewHolder.line_action_static_1.setBackgroundResource(R.drawable.textview_fillet_question_bg);
            mViewHolder.line_action_static_1.setTextColor(mActivity.getResources().getColorStateList(R.color.main_color));
        }if(mViewHolder.status.equals("3")){
            mViewHolder.line_action_static_1.setText("以结束");
            mViewHolder.line_action_static_1.setBackgroundResource(R.drawable.textview_line_action_end_bg);
            mViewHolder.line_action_static_1.setTextColor(mActivity.getResources().getColorStateList(R.color.umiwi_gray_b));
        }

        return convertView;
    }
    public class ViewHolder{
        public View rootView;
        public ImageView line_action_imageview_1;
        //报名-互联网大咖秀-活动内容介绍-
        public TextView line_action_static_1, line_action_name_1, line_action_context_1;
        //活动是否开始类型
        public String status;
        //id
        public String id;

        public ViewHolder(View convertView) {
            this.rootView = convertView;
            line_action_imageview_1 = (ImageView) rootView.findViewById(R.id.line_action_imageview_1);
            line_action_static_1 = (TextView) rootView.findViewById(R.id.line_action_static_1);
            line_action_name_1 = (TextView) rootView.findViewById(R.id.line_action_name_1);
            line_action_context_1 = (TextView) rootView.findViewById(R.id.line_action_context_1);
            status = "2";
            id = "";
        }
    }
}
