package com.umiwi.ui.adapter.updateadapter;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.view.XCRoundRectImageView;

import java.util.ArrayList;

import static com.umiwi.ui.main.YoumiConfiguration.context;

/**
 * Created by lenovo on 2017/4/25.
 */

public class AudioLiveAdapter extends BaseAdapter {
    private FragmentActivity mActivity;
    private ArrayList<RecommendBean.RBean.HotLiveBean.HotLiveRecord> mList;
    public AudioLiveAdapter(FragmentActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        return mList.size();
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mActivity, R.layout.hot_audiolive_item, null);
            holder.iv_author = (XCRoundRectImageView) convertView.findViewById(R.id.iv_author);
            holder.special_name_textView = (TextView) convertView.findViewById(R.id.special_name_textView);
            holder.special_context = (TextView) convertView.findViewById(R.id.special_context);
            holder.expter_time_textView = (TextView) convertView.findViewById(R.id.expter_time_textView);
            holder.special_price = (TextView) convertView.findViewById(R.id.special_price);
            holder.special_subscribe_number = (TextView) convertView.findViewById(R.id.special_subscribe_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RecommendBean.RBean.HotLiveBean.HotLiveRecord hotLiveRecord = mList.get(position);
        Glide.with(context).load(hotLiveRecord.getLimage()).into(holder.iv_author);
        holder.special_name_textView.setText(hotLiveRecord.getTitle());
        holder.special_context.setText(hotLiveRecord.getSubtitle());
        if (!TextUtils.isEmpty(hotLiveRecord.getPrice())) {
            holder.special_price.setText(hotLiveRecord.getPrice());
        } else {
            holder.special_price.setText("免费");
        }
        if("已结束".equals(hotLiveRecord.getStatus())) {
            holder.expter_time_textView.setBackgroundResource(R.drawable.textview_fillet_bg);
            holder.expter_time_textView.setTextColor(Color.GRAY);
        }
        holder.expter_time_textView.setText(hotLiveRecord.getStatus());
        holder.special_subscribe_number.setText(hotLiveRecord.getPartakenum() + "人参与");
        return convertView;
    }

    public void setData(ArrayList<RecommendBean.RBean.HotLiveBean.HotLiveRecord> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }
    class ViewHolder{
        XCRoundRectImageView iv_author;
        TextView special_name_textView,special_context,expter_time_textView,special_price,special_subscribe_number;
    }
}
