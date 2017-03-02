package com.umiwi.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.ExperDetailsAlbumbean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/2.
 */

public class AttentionAdapter extends BaseAdapter {
    private FragmentActivity activity;
    List<ExperDetailsAlbumbean.AttentionBean> attention;

    public AttentionAdapter(FragmentActivity activity, List<ExperDetailsAlbumbean.AttentionBean> attention) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(activity,R.layout.attention_item,null);
            viewHolder.textView = (TextView) view.findViewById(R.id.tv_attention);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(attention.get(i).getWord());
        return view;
    }

    class ViewHolder {
        TextView textView;
    }
}
