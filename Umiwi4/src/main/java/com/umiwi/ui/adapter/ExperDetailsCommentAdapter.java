package com.umiwi.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.umiwi.ui.R;

/**
 * Created by Administrator on 2017/2/28.
 */

public class ExperDetailsCommentAdapter extends BaseAdapter {
    private FragmentActivity activity;
    public ExperDetailsCommentAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 10;
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
        if (view == null){
            viewHolder = new ViewHolder();
            view = View.inflate(activity, R.layout.item_comment, null);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        return view;
    }

    class ViewHolder{

    }

}
