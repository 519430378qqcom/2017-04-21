package com.umiwi.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;

import java.util.ArrayList;

import static com.umiwi.ui.main.YoumiConfiguration.context;

/**
 * Created by Administrator on 2017/3/6.
 */

public class AskHearAdapter extends BaseAdapter{

    private  FragmentActivity activity;
    private  ArrayList mList;

    public AskHearAdapter(FragmentActivity activity, ArrayList mList) {
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(activity,R.layout.ask_hear,null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.buttontag = (TextView) convertView.findViewById(R.id.buttontag);
            viewHolder.tavatar = (ImageView) convertView.findViewById(R.id.tavatar);
            viewHolder.listennum = (TextView) convertView.findViewById(R.id.listennum);
            viewHolder.goodnum = (TextView) convertView.findViewById(R.id.goodnum);
            viewHolder.playtime = (TextView) convertView.findViewById(R.id.playtime);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        viewHolder.title.setText("");
//        viewHolder.buttontag.setText("");
//        viewHolder.listennum.setText("");
//        viewHolder.goodnum.setText("");
//        viewHolder.playtime.setText("");
//        Glide.with(activity).load("").into(viewHolder.tavatar);

        return convertView;
    }

    public class ViewHolder{
        private TextView title,listennum,goodnum,playtime;
        private ImageView tavatar;
        private TextView buttontag;
    }
}
