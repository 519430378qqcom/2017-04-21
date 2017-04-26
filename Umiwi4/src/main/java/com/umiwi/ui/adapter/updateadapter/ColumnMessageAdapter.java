package com.umiwi.ui.adapter.updateadapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.AudioTmessageListBeans;

import java.util.ArrayList;

import cn.youmi.framework.view.CircleImageView;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class ColumnMessageAdapter extends BaseAdapter {
    private ArrayList<AudioTmessageListBeans.RecordX.Record> record;
    private FragmentActivity activity;
    public ColumnMessageAdapter(FragmentActivity activity, ArrayList<AudioTmessageListBeans.RecordX.Record> record) {
        this.activity = activity;
        this.record = record;
    }

    @Override
    public int getCount() {
        return record.size();
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
            convertView = View.inflate(activity, R.layout.column_message_item, null);
            holder.userhead_imageview = (CircleImageView) convertView.findViewById(R.id.userhead_imageview);
            holder.username_textview = (TextView) convertView.findViewById(R.id.username_textview);
            holder.content_textview = (TextView) convertView.findViewById(R.id.content_textview);
            holder.time_textview = (TextView) convertView.findViewById(R.id.time_textview);
            convertView.setTag(holder);
        } else {
            holder= (ViewHolder) convertView.getTag();
        }

        AudioTmessageListBeans.RecordX.Record record = this.record.get(position);
        Glide.with(activity).load(record.getAvatar()).into(holder.userhead_imageview);
        holder.username_textview.setText(record.getName());
        holder.content_textview.setText(record.getContent());
        holder.time_textview.setText(record.getTime());

        return convertView;
    }
    class ViewHolder{
        CircleImageView userhead_imageview;
        TextView username_textview,content_textview,time_textview;

    }
}
