package com.umiwi.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.AudioSpecialDetailsBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/6.
 */

public class ColumnAttentionAdapter extends BaseAdapter {


    private final FragmentActivity activity;
    private  ArrayList<AudioSpecialDetailsBean.RAudioSpecialDetails.AttentionWord> attention;

    public ColumnAttentionAdapter(FragmentActivity activity, ArrayList<AudioSpecialDetailsBean.RAudioSpecialDetails.AttentionWord> attention) {
        this.activity = activity;
        this.attention = attention;
    }

    @Override
    public int getCount() {
        return attention.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ColumnAttentionAdapter.ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ColumnAttentionAdapter.ViewHolder();
            convertView = View.inflate(activity, R.layout.column_details,null);
            viewHolder.tv_jianjie = (TextView) convertView.findViewById(R.id.tv_jianjie);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ColumnAttentionAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.tv_jianjie.setText(attention.get(position).getWord());
        return convertView;
    }

    public class ViewHolder{
        TextView tv_jianjie;
    }
}