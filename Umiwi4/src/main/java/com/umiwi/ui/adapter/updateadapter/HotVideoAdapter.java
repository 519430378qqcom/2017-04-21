package com.umiwi.ui.adapter.updateadapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.RecommendBean;

import java.util.ArrayList;

import static com.umiwi.ui.main.YoumiConfiguration.context;

/**
 * Created by Administrator on 2017/3/24.
 */

public class HotVideoAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<RecommendBean.RBean.HotVideoBean.HotVideoRecordBean> mList;
    public HotVideoAdapter(Context mContext, ArrayList<RecommendBean.RBean.HotVideoBean.HotVideoRecordBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.recentpalyvideo_item, null);
            viewHolder.iv_author = (ImageView) convertView.findViewById(R.id.iv_author);
            viewHolder.tv_video_time = (TextView) convertView.findViewById(R.id.tv_video_time);
            viewHolder.tv_video_name = (TextView) convertView.findViewById(R.id.tv_video_name);
            viewHolder.tv_video_detail = (TextView) convertView.findViewById(R.id.tv_video_detail);
            viewHolder.tv_playnum = (TextView) convertView.findViewById(R.id.tv_playnum);
            viewHolder.tv_playname = (TextView) convertView.findViewById(R.id.tv_playname);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(mList.get(position).getLimage()).into(viewHolder.iv_author);
//        viewHolder.tv_video_time.setText(mList.get(position).getPlaytime());
        viewHolder.tv_video_name.setText(mList.get(position).getShorttitle());
        viewHolder.tv_video_detail.setText(mList.get(position).getName() + " " + mList.get(position).getTutortitle());
        viewHolder.tv_playnum.setText("播放: "+mList.get(position).getWatchnum());
        if (!TextUtils.isEmpty(mList.get(position).getTime())) {
            viewHolder.tv_playname.setText("时长: " + mList.get(position).getPlaytime());
        } else {
            viewHolder.tv_playname.setText("null");
        }
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
