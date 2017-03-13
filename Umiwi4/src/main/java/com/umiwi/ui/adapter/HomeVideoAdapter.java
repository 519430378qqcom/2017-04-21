package com.umiwi.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.VideoBean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/2.
 */
public class HomeVideoAdapter extends BaseAdapter {
    private final Context context;
    private final List<VideoBean.RecordBean> mList;

    public HomeVideoAdapter(Context context, List<VideoBean.RecordBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return (mList == null ? 0 : mList.size());
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.home_video_item,null);

            viewHolder.iv_author = (ImageView) convertView.findViewById(R.id.iv_author);
            viewHolder.tv_video_time = (TextView) convertView.findViewById(R.id.tv_video_time);
            viewHolder.tv_video_name = (TextView) convertView.findViewById(R.id.tv_video_name);
            viewHolder.tv_video_detail = (TextView) convertView.findViewById(R.id.tv_video_detail);
            viewHolder.tv_playnum = (TextView) convertView.findViewById(R.id.tv_playnum);
            viewHolder.tv_playname = (TextView) convertView.findViewById(R.id.tv_playname);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(mList.get(position).getLimage()).into(viewHolder.iv_author);
        viewHolder.tv_video_time.setText(mList.get(position).getPlaytime());
        viewHolder.tv_video_name.setText(mList.get(position).getShortX());
        viewHolder.tv_video_detail.setText(mList.get(position).getTutortitle());
        viewHolder.tv_playnum.setText("播放"+mList.get(position).getWatchnum());
        viewHolder.tv_playname.setText(mList.get(position).getTime());

        return convertView;
    }

    class ViewHolder{
        private ImageView iv_author;
        private TextView tv_video_time;
        private TextView tv_video_name;
        private TextView tv_video_detail;
        private TextView tv_playnum;
        private TextView tv_playname;
    }
}
