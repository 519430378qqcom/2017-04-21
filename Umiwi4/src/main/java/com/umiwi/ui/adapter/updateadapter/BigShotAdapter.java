package com.umiwi.ui.adapter.updateadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sina.weibo.sdk.api.share.Base;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.ArrayList;

import cn.youmi.framework.util.ImageLoader;

/**
 * 类描述：
 * Created by Gpsi on 2017-02-27.
 */

public class BigShotAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<RecommendBean.RBean.DalaoBean> mList;
    private Activity mActivity;
    private ImageLoader mImageLoader;

    public BigShotAdapter(Context context, ArrayList<RecommendBean.RBean.DalaoBean> mList) {
        inflater = LayoutInflater.from(context);
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
            convertView = inflater.inflate(R.layout.item_big_shot, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RecommendBean.RBean.DalaoBean dalaoBean = mList.get(position);
        mImageLoader.loadImage(dalaoBean.getThumb(),viewHolder.iv_big_shot_image,R.drawable.ic_launcher);
        viewHolder.big_sot_name.setText(dalaoBean.getName());

        return convertView;
    }

    private class ViewHolder {
        public View rootView;
        public ImageView iv_big_shot_image;
        public TextView big_sot_name;

        public ViewHolder(View convertView) {
            this.rootView = convertView;
            big_sot_name = (TextView) rootView.findViewById(R.id.big_sot_name);
            iv_big_shot_image = (ImageView) rootView.findViewById(R.id.iv_big_shot_image);

        }
    }
}
