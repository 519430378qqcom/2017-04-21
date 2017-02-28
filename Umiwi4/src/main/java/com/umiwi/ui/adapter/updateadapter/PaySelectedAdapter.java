package com.umiwi.ui.adapter.updateadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.ArrayList;

import cn.youmi.framework.util.ImageLoader;

/**
 * 首页-推荐-付费精选 - Adapter
 * Created by ${Gpsi} on 2017/2/27.
 */

public class PaySelectedAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;

    private ArrayList<RecommendBean.RBean.ChargeBean.RecordBeanX> mList;
    private ImageLoader mImageLoader;
    private Activity mActivity;
    private int videoSize;

    public PaySelectedAdapter(Context context, ArrayList<RecommendBean.RBean.ChargeBean.RecordBeanX> mList) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mActivity = (Activity) context;
        this.mList = mList;
        mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
        videoSize = 0;

        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getType().equals("video"))
                videoSize = videoSize + 1;
        }
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
            convertView = mLayoutInflater.inflate(R.layout.pay_selected_item, null);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        RecommendBean.RBean.ChargeBean.RecordBeanX recordBeanX = mList.get(position);

        if (recordBeanX.getType().equals("video")) {

            if (videoSize == 1) {
                mImageLoader.loadImage(recordBeanX.getImage(), mViewHolder.iv_pay_video, R.drawable.ic_launcher);
                mViewHolder.tv_pay_video_tag_right.setText(recordBeanX.getPricetag());
                mViewHolder.tv_pay_video_tag_left.setText(recordBeanX.getPlaytime());
                mViewHolder.ll_pay_audio.setVisibility(View.GONE);
                mViewHolder.ll_pay_right_video.setVisibility(View.GONE);
            } else if (videoSize == 2) {
                if (position == 0) {
                    mImageLoader.loadImage(recordBeanX.getImage(), mViewHolder.iv_pay_video, R.drawable.ic_launcher);
                    mViewHolder.tv_pay_video_tag_right.setText(recordBeanX.getPricetag());
                    mViewHolder.tv_pay_video_tag_left.setText(recordBeanX.getPlaytime());
                    mViewHolder.ll_pay_audio.setVisibility(View.GONE);
                } else if (position == 1) {
                    mViewHolder.ll_pay_audio.setVisibility(View.GONE);
                    mImageLoader.loadImage(recordBeanX.getImage(), mViewHolder.iv_pay_video_1, R.drawable.ic_launcher);
                    mViewHolder.tv_pay_video_tag_right_1.setText(recordBeanX.getPricetag());
                    mViewHolder.tv_pay_video_tag_left_1.setText(recordBeanX.getPlaytime());
                }
            }

        } else {
            mViewHolder.ll_pay_video.setVisibility(View.GONE);
            mImageLoader.loadImage(recordBeanX.getImage(), mViewHolder.iv_audio_pay_type);
            mViewHolder.tv_audio_pay_context.setText(recordBeanX.getTitle());
            mViewHolder.tv_audio_pay_price.setText(recordBeanX.getPricetag());
            mViewHolder.tv_audio_pay_time.setText(recordBeanX.getPlaytime());
        }

        return convertView;
    }

    public class ViewHolder {
        public LinearLayout ll_pay_audio, ll_pay_video, ll_pay_right_video;
        public View rootView;
        public ImageView iv_pay_video, iv_pay_video_1;
        //报名-互联网大咖秀-活动内容介绍-
        public TextView tv_pay_video_tag_left, tv_pay_video_tag_right, tv_pay_video_tag_left_1, tv_pay_video_tag_right_1;
        //音频
        public ImageView iv_audio_pay_type;
        public TextView tv_audio_pay_time, tv_audio_pay_context, tv_audio_pay_price;
        //类型-视频or音频
        public String type;
        //id
        public String id;
        //Url
        public String Url;

        public ViewHolder(View convertView) {
            this.rootView = convertView;
            iv_pay_video = (ImageView) rootView.findViewById(R.id.iv_pay_video);
            tv_pay_video_tag_left = (TextView) rootView.findViewById(R.id.tv_pay_video_tag_left);
            tv_pay_video_tag_right = (TextView) rootView.findViewById(R.id.tv_pay_video_tag_right);
            ll_pay_video = (LinearLayout) rootView.findViewById(R.id.ll_pay_video);
            //
            ll_pay_audio = (LinearLayout) rootView.findViewById(R.id.ll_pay_audio);
            iv_audio_pay_type = (ImageView) rootView.findViewById(R.id.iv_audio_pay_type);
            tv_audio_pay_time = (TextView) rootView.findViewById(R.id.tv_audio_pay_time);
            tv_audio_pay_context = (TextView) rootView.findViewById(R.id.tv_audio_pay_context);
            tv_audio_pay_price = (TextView) rootView.findViewById(R.id.tv_audio_pay_price);
            //
            ll_pay_right_video = (LinearLayout) rootView.findViewById(R.id.ll_pay_right_video);
            iv_pay_video_1 = (ImageView) rootView.findViewById(R.id.iv_pay_video_1);
            tv_pay_video_tag_left_1 = (TextView) rootView.findViewById(R.id.tv_pay_video_tag_left_1);
            tv_pay_video_tag_right_1 = (TextView) rootView.findViewById(R.id.tv_pay_video_tag_right_1);
        }
    }
}
