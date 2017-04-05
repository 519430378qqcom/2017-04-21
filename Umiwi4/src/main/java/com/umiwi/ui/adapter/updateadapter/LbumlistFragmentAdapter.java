package com.umiwi.ui.adapter.updateadapter;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.LbumListBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class LbumlistFragmentAdapter extends BaseAdapter {
    private FragmentActivity mActivity;
    private ArrayList<LbumListBean.RLbumlist.LbumlistRecord> lbumlists;
    public LbumlistFragmentAdapter(FragmentActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        return (lbumlists == null ? 0:lbumlists.size());
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
            convertView = View.inflate(mActivity, R.layout.subject_audio_video_item, null);
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
        LbumListBean.RLbumlist.LbumlistRecord lbumlistRecord = lbumlists.get(position);
        Log.e("TAG", "lbumlistRecord=" + lbumlistRecord.toString());
        Glide.with(mActivity).load(lbumlistRecord.getImage()).into(holder.iv_author);
        if (lbumlistRecord.getTitle() != null) {
            holder.special_name_textView_1.setText(lbumlistRecord.getTitle());
        } else {
            holder.special_name_textView_1.setVisibility(View.INVISIBLE);
        }
        holder.tv_video_audio.setText(lbumlistRecord.getType());
        if (lbumlistRecord.getPrice() != null) {
            holder.special_price_1.setText(lbumlistRecord.getPrice());
        } else {
            holder.special_price_1.setVisibility(View.INVISIBLE);
        }
        if (lbumlistRecord.getShortcontent() != null) {
            holder.special_context_1.setText(lbumlistRecord.getShortcontent());
        } else {
            holder.special_context_1.setVisibility(View.INVISIBLE);
        }
        if (lbumlistRecord.getOnlinetime() != null) {
            holder.expter_time_textView.setText(lbumlistRecord.getOnlinetime());
        } else {
            holder.expter_time_textView.setVisibility(View.INVISIBLE);
        }
        if (!TextUtils.isEmpty(lbumlistRecord.getCatname())) {
            holder.expter_tag.setText(lbumlistRecord.getCatname());
            holder.expter_tag.setVisibility(View.VISIBLE);
        } else {
            holder.expter_tag.setVisibility(View.INVISIBLE);
        }
        if (lbumlistRecord.getAudiotitle() != null) {
            holder.expter_detail_textView.setText(lbumlistRecord.getAudiotitle());
        } else {
            holder.expter_detail_textView.setVisibility(View.INVISIBLE);
        }
        holder.special_subscribe_number_1.setText("播放"+lbumlistRecord.getWatchnum() + "次");
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

    public void setData(ArrayList<LbumListBean.RLbumlist.LbumlistRecord> lbumlists) {
        this.lbumlists = lbumlists;
        notifyDataSetChanged();
    }
}
