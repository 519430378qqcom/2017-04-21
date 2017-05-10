package com.umiwi.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.MyLiveBean;
import com.umiwi.ui.view.XCRoundRectImageView;

import java.util.List;

/**
 * Created by dong on 2017/5/3.
 */

public class MyLiveAdapter extends BaseAdapter {
    private Context context;
    private List<MyLiveBean.RBean.RecordBean> record;
    public void addItems(List<MyLiveBean.RBean.RecordBean> record){
        this.record.addAll(record);
    }
    public void setItems(List<MyLiveBean.RBean.RecordBean> record){
        this.record = record;
    }
    public MyLiveAdapter(Context context, List<MyLiveBean.RBean.RecordBean> record) {
        this.context = context;
        this.record = record;
    }

    @Override
    public int getCount() {
        return record.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_my_live, null);
            holder = new ViewHolder();
            holder.ivAuthor = (XCRoundRectImageView) convertView.findViewById(R.id.iv_author);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tvSubtitle = (TextView) convertView.findViewById(R.id.tv_subtitle);
            holder.tvStarttime = (TextView) convertView.findViewById(R.id.tv_starttime);
            holder.tv_starttime1 = (TextView) convertView.findViewById(R.id.tv_starttime1);
            holder.iv_already_off = (ImageView) convertView.findViewById(R.id.iv_already_off);
            holder.tvProfit = (TextView) convertView.findViewById(R.id.tv_profit);
            holder.tvPartakenum = (TextView) convertView.findViewById(R.id.tv_partakenum);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyLiveBean.RBean.RecordBean recordBean = record.get(position);
        Glide.with(context).load(recordBean.getLimage()).placeholder(R.drawable.ic_launcher).into(holder.ivAuthor);
        holder.tvTitle.setText(recordBean.getTitle());
        holder.tvPrice.setText("已参与");
        holder.tvSubtitle.setText(recordBean.getSubtitle());
        if( "直播中".equals(recordBean.getStatus())) {
            holder.tvStarttime.setText("直播中");
            holder.iv_already_off.setVisibility(View.GONE);
            holder.tv_starttime1.setVisibility(View.GONE);
        }else if("未开始".equals(recordBean.getStatus())) {
            holder.tvStarttime.setText(recordBean.getLive_time());
            holder.iv_already_off.setVisibility(View.GONE);
            holder.tv_starttime1.setVisibility(View.GONE);
        }else if("已结束".equals(recordBean.getStatus())) {
            holder.iv_already_off.setVisibility(View.VISIBLE);
            holder.tv_starttime1.setVisibility(View.VISIBLE);
            holder.tvStarttime.setVisibility(View.GONE);
            holder.tv_starttime1.setText(recordBean.getLive_time());
        }
        holder.tvProfit.setText("累计收益"+recordBean.getProfit());
        holder.tvPartakenum.setText(recordBean.getPartakenum()+"参与");
        return convertView;
    }

    class ViewHolder {
        XCRoundRectImageView ivAuthor;
        TextView tvTitle;
        TextView tvPrice;
        TextView tvSubtitle;
        TextView tvStarttime,tv_starttime1 ;
        ImageView iv_already_off;
        TextView tvProfit;
        TextView tvPartakenum;
    }
}
