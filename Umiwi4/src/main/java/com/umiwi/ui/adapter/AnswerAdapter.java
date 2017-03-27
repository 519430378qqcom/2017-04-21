package com.umiwi.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/4.
 */
public class AnswerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList mList;

    public AnswerAdapter(Context context, ArrayList mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
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
            convertView = View.inflate(context,R.layout.my_answer,null);

            viewHolder.mime_answer_imageview = (ImageView) convertView.findViewById(R.id.mime_answer_imageview);
            viewHolder.my_answer_name = (TextView) convertView.findViewById(R.id.my_answer_name);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.my_answer_textView_1 = (TextView) convertView.findViewById(R.id.my_answer_textView_1);
            viewHolder.time_limit_hear_textview_1 = (TextView) convertView.findViewById(R.id.time_limit_hear_textview_1);
            viewHolder.head_time_textview_1 = (TextView) convertView.findViewById(R.id.head_time_textview_1);

            convertView.setTag(viewHolder);
        }else{
           viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context).load("").into(viewHolder.mime_answer_imageview);
        viewHolder.my_answer_name.setText("");
        viewHolder.tv_time.setText("");
        viewHolder.my_answer_textView_1.setText("");
        viewHolder.time_limit_hear_textview_1.setText("");
        viewHolder.head_time_textview_1.setText("");

        return convertView;
    }

    public class ViewHolder{

        private ImageView mime_answer_imageview;
        private TextView my_answer_name,tv_time,my_answer_textView_1,time_limit_hear_textview_1,head_time_textview_1;
    }
}
