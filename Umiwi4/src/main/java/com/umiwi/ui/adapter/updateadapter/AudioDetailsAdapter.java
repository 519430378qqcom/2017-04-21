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

public class AudioDetailsAdapter extends BaseAdapter {
    private FragmentActivity activity;
    private ArrayList<AudioSpecialDetailBean.RAudioSpecial.RAudioSpecialContent> content;
    public AudioDetailsAdapter(FragmentActivity activity, ArrayList<AudioSpecialDetailBean.RAudioSpecial.RAudioSpecialContent> content) {
        this.activity = activity;
        this.content = content;
    }

    @Override
    public int getCount() {
        return content.size();
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
            convertView = View.inflate(activity, R.layout.column_details, null);
            holder.tv_jianjie = (TextView) convertView.findViewById(R.id.tv_jianjie);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.tv_jianjie.setText(content.get(position).getWord());
        return convertView;
    }
    class ViewHolder{
        TextView tv_jianjie;
    }
}
