package com.umiwi.ui.adapter.updateadapter;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.AlreadyShopVoiceBean;

import java.util.List;

/**
 * Created by lenovo on 2017/3/22.
 */

public class RecentPlayAudioAdapter extends BaseAdapter {
    private FragmentActivity activity;
    private List<AlreadyShopVoiceBean.RAlreadyVoice.Record> mList;

    public RecentPlayAudioAdapter(FragmentActivity activity) {
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(activity, R.layout.item_already_voice, null);
            viewHolder.cat = (TextView) convertView.findViewById(R.id.cat);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.playtime = (TextView) convertView.findViewById(R.id.playtime);
            viewHolder.watchnum = (TextView) convertView.findViewById(R.id.watchnum);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_tutortitle = (TextView) convertView.findViewById(R.id.tv_tutortitle);
//            viewHolder.process = (TextView) convertView.findViewById(R.id.process);
            viewHolder.view_firstvisable = convertView.findViewById(R.id.view_firstvisable);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AlreadyShopVoiceBean.RAlreadyVoice.Record record = mList.get(position);
        String cat = record.getCat();
        if (position == 0) {
            viewHolder.view_firstvisable.setVisibility(View.GONE);
        } else {
            viewHolder.view_firstvisable.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(cat)) {
            viewHolder.cat.setText(cat);
            viewHolder.cat.setVisibility(View.VISIBLE);
        } else {
            viewHolder.cat.setVisibility(View.GONE);
        }
        viewHolder.title.setText(record.getTitle());
        viewHolder.price.setText(record.getPrice());
        viewHolder.playtime.setText(record.getPlaytime());
        viewHolder.watchnum.setText("播放" + record.getWatchnum() + "次");
//        viewHolder.process.setText("已播" + record.getProcess());
        viewHolder.tv_name.setText(record.getName());
        viewHolder.tv_tutortitle.setText(record.getTutortitle());
        return convertView;
    }

    private class ViewHolder {
        View view_firstvisable;
        TextView title;
        TextView cat;
        TextView price;
        TextView playtime,tv_name,tv_tutortitle;
        TextView watchnum;
        TextView process;

    }

    public void setData(List<AlreadyShopVoiceBean.RAlreadyVoice.Record> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }
}
