package com.umiwi.ui.adapter.updateadapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.VideoSpecialDetailBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/6 0006.
 */

public class VideoSpecialDetailAdapter extends BaseAdapter {
    private FragmentActivity activity;
    private ArrayList<VideoSpecialDetailBean.VideoSpecialRecord> mList;
    public VideoSpecialDetailAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return  mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(activity, R.layout.video_special_list, null);
            holder.voice_img = (ImageView) convertView.findViewById(R.id.voice_img);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.price1 = (TextView) convertView.findViewById(R.id.price1);
            holder.playtime = (TextView) convertView.findViewById(R.id.playtime);
            holder.watchnum = (TextView) convertView.findViewById(R.id.watchnum);
            holder.process = (TextView) convertView.findViewById(R.id.process);

            holder.voice_img.setImageResource(R.drawable.video_small);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        VideoSpecialDetailBean.VideoSpecialRecord videoSpecialRecord = mList.get(position);
        holder.title.setText(videoSpecialRecord.getTitle());
        holder.playtime.setText(videoSpecialRecord.getPlaytime());
        holder.watchnum.setText("播放"+ videoSpecialRecord.getWatchnum() + "次");
        holder.process.setText(videoSpecialRecord.getTime());
        if ("试看".equals(videoSpecialRecord.getPrice())) {
            holder.price.setVisibility(View.VISIBLE);
            holder.price1.setVisibility(View.GONE);
            holder.price.setText("试看");
        } else {
            holder.price1.setText(videoSpecialRecord.getPrice());
            holder.price1.setVisibility(View.VISIBLE);
            holder.price.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder{
        ImageView voice_img;
        TextView title;
        TextView price;
        TextView price1;
        TextView playtime;
        TextView watchnum;
        TextView process;
    }
    public void setData(ArrayList<VideoSpecialDetailBean.VideoSpecialRecord> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }
}
