package com.umiwi.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.VoiceBean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/28.
 */

public class ExperDetailsVoiceAdapter extends BaseAdapter {
    private FragmentActivity activity;
    List<VoiceBean.RecordBean> voiceList;
    public ExperDetailsVoiceAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return voiceList.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            view = View.inflate(activity, R.layout.exper_details_voice_item, null);
            viewHolder.title = (TextView) view.findViewById(R.id.tv_audio_title);
            viewHolder.cat = (TextView) view.findViewById(R.id.tv_cat);
            viewHolder.price = (TextView) view.findViewById(R.id.tv_price);
            viewHolder.playtime = (TextView) view.findViewById(R.id.tv_playtime);
            viewHolder.watchnum = (TextView) view.findViewById(R.id.tv_watchnum);
            viewHolder.process = (TextView) view.findViewById(R.id.tv_process);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        String cat = voiceList.get(i).getCat();
        String playtime = voiceList.get(i).getPlaytime();
        String price = voiceList.get(i).getPrice();
        String title = voiceList.get(i).getTitle();
        String watchnum = voiceList.get(i).getWatchnum();
        String process = voiceList.get(i).getProcess();
        viewHolder.title.setText(title);
        if (!TextUtils.isEmpty(cat)){
            viewHolder.cat.setText(cat);
        }else{
            viewHolder.cat.setVisibility(View.INVISIBLE);
        }
        viewHolder.playtime.setText(playtime);
        viewHolder.price.setText(price);
        viewHolder.process.setText("已播"+process);
        viewHolder.watchnum.setText("播放"+watchnum+"次");
        return view;
    }

    public void setData(List<VoiceBean.RecordBean> voiceList) {
        this.voiceList = voiceList;
        notifyDataSetChanged();
    }

    class ViewHolder{
        ImageView voiceImg;
        TextView title;
        TextView cat;
        TextView price;
        TextView playtime;
        TextView watchnum;
        TextView process;
    }

}
