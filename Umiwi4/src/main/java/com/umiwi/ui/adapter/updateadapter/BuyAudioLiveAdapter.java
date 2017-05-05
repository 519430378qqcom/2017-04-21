package com.umiwi.ui.adapter.updateadapter;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.BuyAudioLiveBean;
import com.umiwi.ui.view.XCRoundRectImageView;

import java.util.ArrayList;

import static com.umiwi.ui.main.YoumiConfiguration.context;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class BuyAudioLiveAdapter extends BaseAdapter {
    private FragmentActivity activity;
    private ArrayList<BuyAudioLiveBean.RBuyAudioLive.BuyAudioLiveRecord> mList;
    public BuyAudioLiveAdapter(FragmentActivity activity) {
        this.activity = activity;
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

        ViewHolder viewHolder= null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(activity, R.layout.hot_audiolive_item, null);
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

        BuyAudioLiveBean.RBuyAudioLive.BuyAudioLiveRecord buyAudioLiveRecord = mList.get(position);
        Glide.with(context).load(buyAudioLiveRecord.getLimage()).into(viewHolder.iv_author);
        viewHolder.special_name_textView.setText(buyAudioLiveRecord.getTitle());
        viewHolder.special_context.setText(buyAudioLiveRecord.getSubtitle());
        viewHolder.special_price.setText(buyAudioLiveRecord.getPrice());
        if("已结束".equals(buyAudioLiveRecord.getStatus())) {
            viewHolder.expter_time_textView.setBackgroundResource(R.drawable.textview_fillet_bg);
            viewHolder.expter_time_textView.setTextColor(Color.GRAY);
        }
        viewHolder.expter_time_textView.setText(buyAudioLiveRecord.getStatus());
        viewHolder.special_subscribe_number.setText(buyAudioLiveRecord.getPartakenum() + "参与");

        return convertView;
    }

    class ViewHolder{
        XCRoundRectImageView iv_author;
        TextView special_name_textView,special_context,expter_time_textView,special_price,special_subscribe_number;
    }
    public void setData(ArrayList<BuyAudioLiveBean.RBuyAudioLive.BuyAudioLiveRecord> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }
}
