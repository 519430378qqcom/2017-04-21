package com.umiwi.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.ActivityItemBean;
import com.umiwi.ui.beans.updatebeans.HomeCoumnBean;

import java.util.ArrayList;
import java.util.List;

import static com.umiwi.ui.R.id.special_context_1;
import static com.umiwi.ui.R.id.special_name_textView_1;
import static com.umiwi.ui.main.YoumiConfiguration.context;

/**
 * Created by Administrator on 2017/2/24.
 */

public class ColumnAdapter extends BaseAdapter {
    private FragmentActivity activity;
    private List<HomeCoumnBean.RecordBean> mList;

    public ColumnAdapter(FragmentActivity activity, List<HomeCoumnBean.RecordBean> mList) {
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
        ViewHolder viewholder = null;
        if (convertView == null) {
            viewholder = new ViewHolder();
            convertView = View.inflate(activity, R.layout.special_column_item, null);

            viewholder.special_header_imageview_1 = (ImageView) convertView.findViewById(R.id.special_header_imageview_1);
            viewholder.special_price_1 = (TextView) convertView.findViewById(R.id.special_price_1);
            viewholder.special_subscribe_number_1 = (TextView) convertView.findViewById(R.id.special_subscribe_number_1);
            viewholder.special_name_textView_1 = (TextView) convertView.findViewById(R.id.special_name_textView_1);
            viewholder.special_context_1 = (TextView) convertView.findViewById(R.id.special_context_1);
            viewholder.expter_time_textView = (TextView) convertView.findViewById(R.id.expter_time_textView);
            viewholder.expter_detail_textView = (TextView) convertView.findViewById(R.id.expter_detail_textView);

            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context).load(mList.get(position).getImage()).into(viewholder.special_header_imageview_1);
        viewholder.special_price_1.setText(mList.get(position).getPrice());
        viewholder.special_subscribe_number_1.setText("已订阅"+mList.get(position).getSalenum());
        viewholder.special_name_textView_1.setText(mList.get(position).getName()+" . "+mList.get(position).getTitle());
        viewholder.special_context_1.setText(mList.get(position).getUpdateaudio());
        viewholder.expter_detail_textView.setText("飞在互联网风口浪尖的猪");
        if(!TextUtils.isEmpty(mList.get(position).getUpdatetime())){
            viewholder.expter_time_textView.setText(mList.get(position).getUpdatetime()+"小时前更新");
        }else{
            viewholder.expter_time_textView.setText("0小时前更新");
        }

        return convertView;
    }

    private static class ViewHolder {
        private ImageView special_header_imageview_1;
        private TextView special_price_1,special_subscribe_number_1,special_name_textView_1,special_context_1,expter_time_textView,expter_detail_textView;
    }
}
