package com.umiwi.ui.adapter.updateadapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.AudioSpecialDetailBean;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/4/5.
 */

public class AudioSpecialListAdapter extends BaseAdapter {
    private FragmentActivity activity;
    private ArrayList<AudioSpecialDetailBean.RAudioSpecial.LastRecordList> mList;

    public AudioSpecialListAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return mList.size();
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
            convertView = View.inflate(activity, R.layout.audio_special_list, null);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.playtime = (TextView) convertView.findViewById(R.id.playtime);
            holder.watchnum = (TextView) convertView.findViewById(R.id.watchnum);
            holder.process = (TextView) convertView.findViewById(R.id.process);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AudioSpecialDetailBean.RAudioSpecial.LastRecordList lastRecordList = mList.get(position);
        holder.title.setText(lastRecordList.getTitle());
        holder.playtime.setText(lastRecordList.getPlaytime());
        holder.watchnum.setText(lastRecordList.getWatchnum() + "次");
        holder.process.setText(lastRecordList.getOnlinetime());
        if (lastRecordList.isaudition()) {
            holder.price.setVisibility(View.VISIBLE);
        } else {
            holder.price.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void setData(ArrayList<AudioSpecialDetailBean.RAudioSpecial.LastRecordList> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }
    class ViewHolder{
        TextView title;
        TextView price;
        TextView playtime;
        TextView watchnum;
        TextView process;
    }
}
