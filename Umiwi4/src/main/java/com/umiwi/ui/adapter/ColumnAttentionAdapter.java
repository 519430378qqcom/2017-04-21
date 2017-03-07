package com.umiwi.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.ColumnDetailsBean;

import java.util.List;

import static com.umiwi.ui.main.YoumiConfiguration.context;

/**
 * Created by Administrator on 2017/3/6.
 */

public class ColumnAttentionAdapter extends BaseAdapter {


    private final FragmentActivity activity;
    private  List<ColumnDetailsBean.AttentionBean> attention;

    public ColumnAttentionAdapter(FragmentActivity activity, List<ColumnDetailsBean.AttentionBean> attention) {
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
            convertView = View.inflate(context, R.layout.column_details,null);
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