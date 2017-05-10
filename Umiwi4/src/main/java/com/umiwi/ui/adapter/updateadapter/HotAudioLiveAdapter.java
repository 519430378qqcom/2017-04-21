package com.umiwi.ui.adapter.updateadapter;

import android.content.Context;
import android.graphics.Color;
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
 * Created by Administrator on 2017/4/24 0024.
 */

public class HotAudioLiveAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<RecommendBean.RBean.HotLiveBean.HotLiveRecord> record;
    public HotAudioLiveAdapter(Context mContext, ArrayList<RecommendBean.RBean.HotLiveBean.HotLiveRecord> record) {
        this.mContext = mContext;
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
        ViewHolder viewHolder= null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.hot_audiolive_item, null);
            viewHolder.iv_author = (XCRoundRectImageView) convertView.findViewById(R.id.iv_author);
            viewHolder.special_name_textView = (TextView) convertView.findViewById(R.id.special_name_textView);
            viewHolder.special_context = (TextView) convertView.findViewById(R.id.special_context);
            viewHolder.expter_time_textView = (TextView) convertView.findViewById(R.id.expter_time_textView);
            viewHolder.special_price = (TextView) convertView.findViewById(R.id.special_price);
            viewHolder.special_subscribe_number = (TextView) convertView.findViewById(R.id.special_subscribe_number);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RecommendBean.RBean.HotLiveBean.HotLiveRecord hotLiveRecord = record.get(position);
        Glide.with(context).load(hotLiveRecord.getLimage()).into(viewHolder.iv_author);
        viewHolder.special_name_textView.setText(hotLiveRecord.getTitle());
        viewHolder.special_context.setText(hotLiveRecord.getSubtitle());
        if (!TextUtils.isEmpty(hotLiveRecord.getPrice())) {
            viewHolder.special_price.setText(hotLiveRecord.getPrice());
        } else {
            viewHolder.special_price.setText("免费");
        }

        if("已结束".equals(hotLiveRecord.getStatus())) {
            viewHolder.expter_time_textView.setBackgroundResource(R.drawable.textview_fillet_bg);
            viewHolder.expter_time_textView.setTextColor(Color.GRAY);
        }
        viewHolder.expter_time_textView.setText(hotLiveRecord.getStatus());
        viewHolder.special_subscribe_number.setText(hotLiveRecord.getPartakenum() + "参与");

        return convertView;
    }
    class ViewHolder{
        XCRoundRectImageView iv_author;
        TextView special_name_textView,special_context,expter_time_textView,special_price,special_subscribe_number;
    }
}
