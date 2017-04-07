package com.umiwi.ui.adapter.updateadapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.RecommendBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/1 0001.
 */

public class LbumListAdapter extends BaseAdapter {
    private ArrayList<RecommendBean.RBean.AlbumListBean.AlbumListRecord> record;
    private Context mContext;
    public LbumListAdapter(Context mContext, ArrayList<RecommendBean.RBean.AlbumListBean.AlbumListRecord> record) {
        this.record = record;
        this.mContext = mContext;
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.subject_audio_video_item, null);
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
        RecommendBean.RBean.AlbumListBean.AlbumListRecord albumListRecord = record.get(position);
        Glide.with(mContext).load(albumListRecord.getImage()).into(holder.iv_author);
        holder.tv_video_audio.setText(albumListRecord.getType());
        holder.special_name_textView_1.setText(albumListRecord.getTitle());
        holder.special_price_1.setText(albumListRecord.getPrice());
        if ("0".equals(albumListRecord.getShortcontent())) {
            holder.special_context_1.setVisibility(View.GONE);
        } else {
            holder.special_context_1.setVisibility(View.VISIBLE);
            holder.special_context_1.setText(albumListRecord.getShortcontent());
        }

        if (!TextUtils.isEmpty(albumListRecord.getOnlinetime())) {
            holder.expter_time_textView.setText(albumListRecord.getOnlinetime());
        } else {
            holder.expter_time_textView.setVisibility(View.INVISIBLE);
        }
        if (!TextUtils.isEmpty(albumListRecord.getCatname())) {
            holder.expter_tag.setText(albumListRecord.getCatname());
        } else {
            holder.expter_tag.setVisibility(View.INVISIBLE);
        }
        holder.expter_detail_textView.setText(albumListRecord.getAudiotitle());
        holder.special_subscribe_number_1.setText("播放"+albumListRecord.getWatchnum() + "次");
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
}
