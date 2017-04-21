package com.umiwi.ui.adapter.updateadapter;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.AlreadyVideoBean;

import java.util.ArrayList;

import static com.umiwi.ui.R.id.cat;
import static com.umiwi.ui.R.id.tv_name;
import static com.umiwi.ui.R.id.tv_tutortitle;

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
        ViewHoder hoder;
        if (view == null){
            hoder = new ViewHoder();
//            view = View.inflate(activity, R.layout.item_buy_video, null);
//            hoder.video_img = (ImageView) view.findViewById(R.id.video_img);
//            hoder.paly_num = (TextView) view.findViewById(R.id.paly_num);
//            hoder.title = (TextView) view.findViewById(R.id.title);
//            hoder.times = (TextView) view.findViewById(R.id.times);
//            hoder.describe = (TextView) view.findViewById(R.id.describe);
//            hoder.paly_length = (TextView) view.findViewById(R.id.paly_length);
            view = View.inflate(activity, R.layout.item_buy_video_new, null);
            hoder.cat = (TextView) view.findViewById(cat);
            hoder.title = (TextView) view.findViewById(R.id.title);
            hoder.price = (TextView) view.findViewById(R.id.price);
            hoder.playtime = (TextView) view.findViewById(R.id.playtime);
            hoder.watchnum = (TextView) view.findViewById(R.id.watchnum);
//            hoder.process = (TextView) view.findViewById(R.id.process);
            hoder.view_firstvisable = view.findViewById(R.id.view_firstvisable);
            hoder.tv_tutortitle = (TextView) view.findViewById(tv_tutortitle);
            hoder.tv_name = (TextView) view.findViewById(tv_name);
            view.setTag(hoder);
        }else {
            hoder = (ViewHoder) view.getTag();
        }
        AlreadyVideoBean.RalreadyVideo.RecordInfo recordInfo = buyVideoInfos.get(i);
//        ImageLoader imageLoader = new ImageLoader(activity);
//        imageLoader.loadImage(recordInfo.getLimage(),hoder.video_img);
//        hoder.paly_num.setText("播放 "+recordInfo.getWatchnum());
//        hoder.title.setText(recordInfo.getShortx());
//        hoder.times.setText(recordInfo.getTime());
//        hoder.describe.setText(recordInfo.getTutortitle());
//        hoder.paly_length.setText(recordInfo.getPlaytime());
        String cat = recordInfo.getTagname();
        if (!TextUtils.isEmpty(cat)) {
            hoder.cat.setText(cat);
            hoder.cat.setVisibility(View.VISIBLE);
        } else {
            hoder.cat.setVisibility(View.GONE);
        }
        if (i == 0) {
            hoder.view_firstvisable.setVisibility(View.GONE);
        } else {
            hoder.view_firstvisable.setVisibility(View.VISIBLE);
        }
        hoder.title.setText(recordInfo.getShortx());
        hoder.price.setText(recordInfo.getPrice());
        hoder.tv_name.setText(recordInfo.getName());
        hoder.tv_tutortitle.setText(recordInfo.getTutortitle());
        hoder.playtime.setText(recordInfo.getPlaytime());
        hoder.watchnum.setText("播放 "+recordInfo.getWatchnum());
        return view;
    }

    public void setData(ArrayList<AlreadyVideoBean.RalreadyVideo.RecordInfo> buyVideoInfos) {
         this.buyVideoInfos = buyVideoInfos;
         notifyDataSetChanged();
    }

    private class ViewHoder{
//        ImageView video_img;
//        TextView title;
//        TextView paly_num;
//        TextView times;
//        TextView describe;
//        TextView paly_length;

        TextView title;
        TextView cat;
        TextView tv_name,tv_tutortitle;
        TextView playtime;
        TextView watchnum;
        TextView price;
        View view_firstvisable;

    }
}
