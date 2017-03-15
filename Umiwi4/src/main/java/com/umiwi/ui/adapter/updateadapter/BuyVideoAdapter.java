package com.umiwi.ui.adapter.updateadapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.AlreadyVideoBean;

import java.util.ArrayList;

import cn.youmi.framework.util.ImageLoader;

/**
 * Created by Administrator on 2017/3/15.
 */

public class BuyVideoAdapter extends BaseAdapter {
    private ArrayList<AlreadyVideoBean.RalreadyVideo.RecordInfo> buyVideoInfos;
    FragmentActivity activity;
    public BuyVideoAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return buyVideoInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHoder viewHoder;
        if (view == null){
            viewHoder = new ViewHoder();
            view = View.inflate(activity, R.layout.item_buy_video, null);
            viewHoder.video_img = (ImageView) view.findViewById(R.id.video_img);
            viewHoder.paly_num = (TextView) view.findViewById(R.id.paly_num);
            viewHoder.title = (TextView) view.findViewById(R.id.title);
            viewHoder.times = (TextView) view.findViewById(R.id.times);
            viewHoder.describe = (TextView) view.findViewById(R.id.describe);
            viewHoder.paly_length = (TextView) view.findViewById(R.id.paly_length);

            view.setTag(viewHoder);
        }else {
            viewHoder = (ViewHoder) view.getTag();
        }
        AlreadyVideoBean.RalreadyVideo.RecordInfo recordInfo = buyVideoInfos.get(i);
        ImageLoader imageLoader = new ImageLoader(activity);
        imageLoader.loadImage(recordInfo.getLimage(),viewHoder.video_img);
        viewHoder.paly_num.setText("播放 "+recordInfo.getWatchnum());
        viewHoder.title.setText(recordInfo.getShortx());
        viewHoder.times.setText(recordInfo.getTime());
        viewHoder.describe.setText(recordInfo.getTutortitle());
        viewHoder.paly_length.setText(recordInfo.getPlaytime());
        return view;
    }

    public void setData(ArrayList<AlreadyVideoBean.RalreadyVideo.RecordInfo> buyVideoInfos) {
         this.buyVideoInfos = buyVideoInfos;
         notifyDataSetChanged();
    }

    private class ViewHoder{
        ImageView video_img;
        TextView title;
        TextView paly_num;
        TextView times;
        TextView describe;
        TextView paly_length;

    }
}
