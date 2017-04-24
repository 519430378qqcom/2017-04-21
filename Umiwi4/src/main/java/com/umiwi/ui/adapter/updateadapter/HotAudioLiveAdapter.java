package com.umiwi.ui.adapter.updateadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.view.XCRoundRectImageView;


/**
 * Created by Administrator on 2017/4/24 0024.
 */

public class HotAudioLiveAdapter extends BaseAdapter {

    private Context mContext;
    public HotAudioLiveAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 3;
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

        return convertView;
    }
    class ViewHolder{
        XCRoundRectImageView iv_author;
        TextView special_name_textView,special_context,expter_time_textView,special_price,special_subscribe_number;
    }
}
