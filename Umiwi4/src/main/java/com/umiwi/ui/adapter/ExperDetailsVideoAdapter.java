package com.umiwi.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.VideoBean;
import com.umiwi.ui.main.UmiwiApplication;


import java.util.List;

import cn.youmi.framework.util.ImageLoader;

/**
 * Created by Administrator on 2017/2/28.
 */

public class ExperDetailsVideoAdapter extends BaseAdapter {
    private FragmentActivity activity;
    private List<VideoBean.RecordBean> videoInfos;

    public ExperDetailsVideoAdapter(FragmentActivity activity) {
        this.activity = activity;

    }

    @Override
    public int getCount() {
        return videoInfos.size();
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
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(activity, R.layout.exper_details_video_item, null);
            viewHolder.limage = (ImageView) view.findViewById(R.id.limage);
            viewHolder.tv_short = (TextView) view.findViewById(R.id.tv_short);
            viewHolder.watchnum = (TextView) view.findViewById(R.id.watchnum);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.tutortitle = (TextView) view.findViewById(R.id.tutortitle);
            viewHolder.price = (TextView) view.findViewById(R.id.price);
            viewHolder.time = (TextView) view.findViewById(R.id.time);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        VideoBean.RecordBean recordBean = videoInfos.get(i);
        String limage = recordBean.getLimage();
        String name = recordBean.getName();
        String price = recordBean.getPrice();
        String watchnum = recordBean.getWatchnum();
        String time = recordBean.getTime();
        String tutortitle = recordBean.getTutortitle();
        String shortX = recordBean.getShorttitle();
        viewHolder.time.setText(time);
        viewHolder.tv_short.setText(shortX);
        viewHolder.tutortitle.setText(tutortitle);
        viewHolder.watchnum.setText("播放 "+watchnum);
        viewHolder.time.setText(time);
        viewHolder.price.setText(price);
        viewHolder.name.setText(name);
        ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
        mImageLoader.loadImage(limage, viewHolder.limage);
        return view;
    }

    public void setData(List<VideoBean.RecordBean> videoInfos) {
        this.videoInfos = videoInfos;
        notifyDataSetChanged();
    }

    class ViewHolder {
        ImageView limage;
        TextView tv_short;
        TextView watchnum;
        TextView name;
        TextView tutortitle;
        TextView price;
        TextView time;
    }
}
