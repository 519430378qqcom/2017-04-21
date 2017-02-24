package com.umiwi.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.ActivityItemBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/24.
 */

public class ColumnAdapter extends BaseAdapter {
    private FragmentActivity activity;
    private ArrayList<ActivityItemBean> mList;

    public ColumnAdapter(FragmentActivity activity, ArrayList<ActivityItemBean> mList) {
        this.activity = activity;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return 2;
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
        ViewHolder viewholder = null;
        if (convertView == null) {
            viewholder = new ViewHolder();
            convertView = View.inflate(activity, R.layout.special_column_item, null);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    private static class ViewHolder {


    }
}
