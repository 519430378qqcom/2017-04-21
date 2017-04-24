package com.umiwi.ui.adapter.updateadapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.view.XCRoundRectImageView;

/**
 * Created by lenovo on 2017/4/25.
 */

public class AudioLiveAdapter extends BaseAdapter {
    private FragmentActivity mActivity;
    public AudioLiveAdapter(FragmentActivity activity) {
        this.mActivity = activity;
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
        return convertView;
    }

    public void setData() {

        notifyDataSetChanged();
    }
    class ViewHolder{
        XCRoundRectImageView iv_author;
        TextView special_name_textView,special_context,expter_time_textView,special_price,special_subscribe_number;
    }
}
