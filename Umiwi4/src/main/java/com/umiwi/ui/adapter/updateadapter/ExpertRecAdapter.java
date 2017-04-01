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
 * 类描述：首页—推荐—行家推荐 Adapter
 * Created by Gpsi on 2017-02-25.
 */

public class ExpertRecAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private ArrayList<RecommendBean.RBean.TutorBean> mList;
    private Activity mActivity;
    private ImageLoader mImageLoader;

    public ExpertRecAdapter(Context context, ArrayList<RecommendBean.RBean.TutorBean> mList) {
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

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.special_column_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RecommendBean.RBean.TutorBean tutorBean = mList.get(position);

        mImageLoader.loadImage(tutorBean.getImage(),viewHolder.expert_header_imageview,R.drawable.ic_launcher);
        viewHolder.expert_name_textView_1.setText(tutorBean.getTitle());
        viewHolder.expert_context_1.setText(tutorBean.getTutortitle().trim());
        viewHolder.expter_time_textView.setText(" "+tutorBean.getUpdatetime() + "更新 ");
        viewHolder.expter_detail_textView.setText(tutorBean.getUpdateaudio());
        viewHolder.expert_subscribe_number.setText("已订阅"+tutorBean.getSalenum());
        viewHolder.expert_price.setText(tutorBean.getPrice());
        viewHolder.isBuy = tutorBean.getIsbuy();
        viewHolder.uid = tutorBean.getUid();
        // TODO 是否已购
        return convertView;
    }

    private class ViewHolder {
        public View rootView;
        public ImageView expert_header_imageview;
        //¥299/年
        public TextView expert_price, expter_detail_textView, expter_time_textView, expert_context_1, expert_name_textView_1, expert_subscribe_number;
        //是否购买
        public boolean isBuy;

        public String uid;

        public ViewHolder(View convertView) {
            this.rootView = convertView;
            expert_price = (TextView) rootView.findViewById(R.id.special_price_1);
            expert_header_imageview = (ImageView) rootView.findViewById(R.id.special_header_imageview_1);
            expert_name_textView_1 = (TextView) rootView.findViewById(R.id.special_name_textView_1);
            expert_context_1 = (TextView) rootView.findViewById(R.id.special_context_1);
            expter_time_textView = (TextView) rootView.findViewById(R.id.expter_time_textView);
            expter_detail_textView = (TextView) rootView.findViewById(R.id.expter_detail_textView);
            expert_subscribe_number = (TextView) rootView.findViewById(R.id.special_subscribe_number_1);
            isBuy = false;
            uid = "";
        }
    }
}
