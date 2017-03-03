package com.umiwi.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.AudioBean;

import java.util.List;

/**
 * Created by sll on 2017/2/28.
 */

public class AudioAdapter extends BaseAdapter {


    private final Context context;
    private final List<AudioBean.RecordBean> mList;


    public AudioAdapter(Context context, List<AudioBean.RecordBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
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

        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.audio_item,null);

            viewHolder.tv_audio_name = (TextView) convertView.findViewById(R.id.tv_audio_name);
            viewHolder.tv_prize = (TextView) convertView.findViewById(R.id.tv_prize);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_watchnum = (TextView) convertView.findViewById(R.id.tv_watchnum);
            viewHolder.tv_percent = (TextView) convertView.findViewById(R.id.tv_percent);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_audio_name.setText(mList.get(position).getTitle());
        viewHolder.tv_prize.setText(mList.get(position).getPrice());
        viewHolder.tv_time.setText(mList.get(position).getPlaytime());
        viewHolder.tv_percent.setText("已播"+mList.get(position).getProcess());
        viewHolder.tv_watchnum.setText("播放"+mList.get(position).getWatchnum()+"次");

        return convertView;
    }

    class ViewHolder{
        private TextView tv_audio_name;
        private TextView tv_prize;
        private TextView tv_time;
        private TextView tv_percent;
        private TextView tv_watchnum;
    }
}
