package com.umiwi.ui.adapter.updateadapter;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.BuySpecialBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/7 0007.
 */

public class BuySpecialAdapter extends BaseAdapter {
    private FragmentActivity activity;
    private ArrayList<BuySpecialBean.RBuySpecial.BuySpecialRecord> mList;
    public BuySpecialAdapter(FragmentActivity activity) {
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(activity, R.layout.subject_audio_video_item, null);
            holder.iv_author = (ImageView) convertView.findViewById(R.id.iv_author);
            holder.tv_video_audio = (TextView) convertView.findViewById(R.id.tv_video_audio);
            holder.special_name_textView_1 = (TextView) convertView.findViewById(R.id.special_name_textView_1);
            holder.special_price_1 = (TextView) convertView.findViewById(R.id.special_price_1);
            holder.special_context_1 = (TextView) convertView.findViewById(R.id.special_context_1);
            holder.expter_time_textView = (TextView) convertView.findViewById(R.id.expter_time_textView);
            holder.expter_tag = (TextView) convertView.findViewById(R.id.expter_tag);
            holder.expter_detail_textView = (TextView) convertView.findViewById(R.id.expter_detail_textView);
            holder.special_subscribe_number_1 = (TextView) convertView.findViewById(R.id.special_subscribe_number_1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BuySpecialBean.RBuySpecial.BuySpecialRecord buySpecialRecord = mList.get(position);
//        Log.e("TAG", "lbumlistRecord=" + lbumlistRecord.toString());
        Glide.with(activity).load(buySpecialRecord.getImage()).into(holder.iv_author);
        if (buySpecialRecord.getTitle() != null) {
            holder.special_name_textView_1.setText(buySpecialRecord.getTitle());
        } else {
            holder.special_name_textView_1.setVisibility(View.INVISIBLE);
        }
        holder.special_context_1.setText(buySpecialRecord.getShortcontent());
        holder.tv_video_audio.setText(buySpecialRecord.getType());
        holder.special_price_1.setText(buySpecialRecord.getPrice());
        holder.expter_time_textView.setText(buySpecialRecord.getOnlinetime());
        if (!TextUtils.isEmpty(buySpecialRecord.getCatname())) {
            holder.expter_tag.setText(buySpecialRecord.getCatname());
            holder.expter_tag.setVisibility(View.VISIBLE);
        } else {
            holder.expter_tag.setVisibility(View.GONE);
        }
        holder.expter_detail_textView.setText(buySpecialRecord.getAudiotitle());
        holder.special_subscribe_number_1.setText("播放" + buySpecialRecord.getWatchnum() + "次");

        return convertView;
    }
    class ViewHolder{
        ImageView iv_author;//图片
        TextView tv_video_audio;//视频/音频
        TextView special_name_textView_1;//title
        TextView special_price_1;//price
        TextView special_context_1;
        TextView expter_time_textView;//更新
        TextView expter_tag;//标签
        TextView expter_detail_textView;
        TextView special_subscribe_number_1;//播放次数
    }
    public void setData(ArrayList<BuySpecialBean.RBuySpecial.BuySpecialRecord> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }
}
