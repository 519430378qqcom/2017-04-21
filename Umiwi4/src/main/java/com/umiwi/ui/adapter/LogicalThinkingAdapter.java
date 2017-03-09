package com.umiwi.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.LogincalThinkingBean;

import java.util.ArrayList;
import java.util.List;

import static com.umiwi.ui.R.id.tv_title;

/**
 * Created by Administrator on 2017/2/24.
 */


public class LogicalThinkingAdapter extends BaseAdapter {
    private FragmentActivity activity;
    private List<LogincalThinkingBean.RecordBean> mList;
    public LogicalThinkingAdapter(FragmentActivity activity, List<LogincalThinkingBean.RecordBean> mList) {
        this.activity = activity;
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
         ViewHolder viewHolder = null;
        if (convertView == null){
           viewHolder = new ViewHolder();
            convertView = View.inflate(activity, R.layout.logical_thinking_item,null);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_goodnum = (TextView) convertView.findViewById(R.id.tv_goodnum);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_watchnum = (TextView) convertView.findViewById(R.id.tv_watchnum);
            viewHolder.tv_imageview = (ImageView) convertView.findViewById(R.id.tv_imageview);
            viewHolder.tv_description = (TextView) convertView.findViewById(R.id.tv_description);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_title.setText(mList.get(position).getTitle());
        viewHolder.tv_goodnum.setText(mList.get(position).getGoodnum());
        viewHolder.tv_time.setText(mList.get(position).getOnlinetime());
        viewHolder.tv_watchnum.setText(mList.get(position).getWatchnum()+"人读过");
        viewHolder.tv_description.setText(mList.get(position).getDescription());
        Glide.with(activity).load(mList.get(position).getImage()).into(viewHolder.tv_imageview);
        return convertView;
    }

    class ViewHolder{
        private TextView tv_title,tv_goodnum,tv_time,tv_watchnum,tv_description;
        private ImageView tv_imageview;
    }
}
