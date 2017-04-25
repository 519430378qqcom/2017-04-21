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

public class ColumnDetailsAdapter extends BaseAdapter {


    private final FragmentActivity activity;
    private final ArrayList<AudioSpecialDetailsBean.RAudioSpecialDetails.ContentWord> content;

    public ColumnDetailsAdapter(FragmentActivity activity, ArrayList<AudioSpecialDetailsBean.RAudioSpecialDetails.ContentWord> content) {
        this.activity = activity;
        this.content = content;
    }




    @Override
    public int getCount() {
        return content.size();
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

        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(activity, R.layout.column_details,null);
            viewHolder.tv_jianjie = (TextView) convertView.findViewById(R.id.tv_jianjie);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_jianjie.setText(content.get(position).getWord());
        return convertView;
    }

    public class ViewHolder{
        TextView tv_jianjie;
    }
}

