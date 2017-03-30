package com.umiwi.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.TheHotListBeans;

import java.util.List;

/**
 * Created by Administrator on 2017/3/2.
 */

public class HotListAdapter extends BaseAdapter {
    List<TheHotListBeans.RecordBean> recordBeanList;
    Context context;

    public HotListAdapter(Context context, List<TheHotListBeans.RecordBean>
            recordBeanList) {
        this.context = context;
        this.recordBeanList = recordBeanList;
    }

    @Override
    public int getCount() {
        return (recordBeanList == null ? 0 : recordBeanList.size());
    }

    @Override
    public Object getItem(int i) {
        return recordBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.hot_list_item, null);
            viewHolder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.pricetag = (TextView) view.findViewById(R.id.pricetag);
            viewHolder.tv_playtime = (TextView) view.findViewById(R.id.tv_playtime);
            viewHolder.tv_watchnum = (TextView) view.findViewById(R.id.tv_watchnum);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.tv_tutortitle = (TextView) view.findViewById(R.id.tv_tutortitle);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        TheHotListBeans.RecordBean recordBean = recordBeanList.get(i);
        if ("audioalbum".equals(recordBean.getFrom())) {
            viewHolder.iv_icon.setImageResource(R.drawable.default_voice);
        } else {
            viewHolder.iv_icon.setImageResource(R.drawable.video_small);
        }
        if (recordBean.getPricetag().equals("免费")){
            viewHolder.pricetag.setText(recordBean.getPricetag());
            viewHolder.pricetag.setTextColor(context.getResources().getColor(R.color.hot_list02));
            viewHolder.pricetag.setBackgroundResource(R.drawable.tv_hot_list02);
        }else {
            viewHolder.pricetag.setText(recordBean.getPricetag());
            viewHolder.pricetag.setTextColor(context.getResources().getColor(R.color.hot_list01));
            viewHolder.pricetag.setBackgroundResource(R.drawable.tv_hot_list01);
        }
        viewHolder.tv_title.setText(recordBean.getTitle());
        viewHolder.tv_playtime.setText(recordBean.getPlaytime());
        if (!TextUtils.isEmpty(recordBean.getWatchnum())){
            viewHolder.tv_watchnum.setText(recordBean.getWatchnum());
        }
        if (!TextUtils.isEmpty(recordBean.getThreadnum())){
            viewHolder.tv_watchnum.setText(recordBean.getThreadnum());
        }
        if (!TextUtils.isEmpty(recordBean.getSalenum())) {
            viewHolder.tv_watchnum.setText(recordBean.getSalenum());
        }
        viewHolder.tv_name.setText(recordBean.getName());
        viewHolder.tv_tutortitle.setText(recordBean.getTutortitle());
        return view;
    }

    class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
        TextView pricetag;
        TextView tv_playtime;
        TextView tv_watchnum;
        TextView tv_name;
        TextView tv_tutortitle;
    }
}
