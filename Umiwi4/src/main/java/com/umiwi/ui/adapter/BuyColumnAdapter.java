package com.umiwi.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.AlreadShopColumnBean;
import com.umiwi.ui.view.XCRoundRectImageView;

import java.util.List;

import cn.youmi.framework.util.ImageLoader;

/**已购-专栏
 * Created by Gpsi on 2017/3/13.
 */

public class BuyColumnAdapter extends BaseAdapter {
    private FragmentActivity activity;
    private List<AlreadShopColumnBean.RalreadyColumn.RecordColumn> mList;
    public BuyColumnAdapter(FragmentActivity activity) {
        this.activity = activity;
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
            convertView = View.inflate(activity, R.layout.special_buy_column_item, null);
            viewholder.special_header_imageview_1 = (XCRoundRectImageView) convertView.findViewById(R.id.special_header_imageview_1);
            viewholder.special_name_textView_1 = (TextView) convertView.findViewById(R.id.special_name_textView_1);
            viewholder.special_context_1 = (TextView) convertView.findViewById(R.id.special_context_1);
            viewholder.expter_time_textView = (TextView) convertView.findViewById(R.id.expter_time_textView);
            viewholder.expter_detail_textView = (TextView) convertView.findViewById(R.id.expter_detail_textView);
            viewholder.special_subscribe_number_1 = (TextView) convertView.findViewById(R.id.special_subscribe_number_1);
            viewholder.special_subscribe_number_1.setVisibility(View.GONE);
            viewholder.special_price_1 = (TextView) convertView.findViewById(R.id.special_price_1);

            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        AlreadShopColumnBean.RalreadyColumn.RecordColumn recordColumn = mList.get(position);
        ImageLoader imageLoader = new ImageLoader(activity);
        imageLoader.loadImage(recordColumn.getImage(),viewholder.special_header_imageview_1);
        viewholder.special_name_textView_1.setText(recordColumn.getTitle());
        viewholder.special_context_1.setText(recordColumn.getTutortitle());
        viewholder.expter_time_textView.setText(recordColumn.getUpdatetime() + "更新");
        viewholder.expter_detail_textView.setText(recordColumn.getUpdateaudio());
        viewholder.special_subscribe_number_1.setText("已订阅"+recordColumn.getSalenum());
        viewholder.special_price_1.setText(recordColumn.getPrice());
        return convertView;
    }

    public void setData(List<AlreadShopColumnBean.RalreadyColumn.RecordColumn> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }


    private static class ViewHolder {
        XCRoundRectImageView special_header_imageview_1;
        TextView special_name_textView_1;
        TextView special_context_1;
        TextView expter_time_textView;
        TextView expter_detail_textView;
        TextView special_subscribe_number_1;
        TextView special_price_1;
    }
}
