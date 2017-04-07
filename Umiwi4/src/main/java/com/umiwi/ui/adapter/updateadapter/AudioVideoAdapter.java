package com.umiwi.ui.adapter.updateadapter;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.AudioVideoBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/1 0001.
 */

public class AudioVideoAdapter extends BaseAdapter {

    private FragmentActivity activity;
    private ArrayList<AudioVideoBean.RAUdioVideo.AudioVideoList> audioVideoList;
    public AudioVideoAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return audioVideoList.size();
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
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder hoder;
        if (view == null) {
            hoder = new ViewHolder();
            view = View.inflate(activity, R.layout.audio_video_item, null);
            hoder.cat = (TextView) view.findViewById(R.id.cat);
            hoder.title = (TextView) view.findViewById(R.id.title);
            hoder.price = (TextView) view.findViewById(R.id.price);
            hoder.playtime = (TextView) view.findViewById(R.id.playtime);
            hoder.watchnum = (TextView) view.findViewById(R.id.watchnum);
            hoder.voice_img = (ImageView) view.findViewById(R.id.voice_img);
//            hoder.process = (TextView) view.findViewById(R.id.process);
//            hoder.view_firstvisable = view.findViewById(R.id.view_firstvisable);
            view.setTag(hoder);
        } else {
            hoder = (ViewHolder) view.getTag();
        }
        AudioVideoBean.RAUdioVideo.AudioVideoList audioVideoList = this.audioVideoList.get(i);
        String cat = audioVideoList.getTagname();
//        if (i == 0) {
//            hoder.view_firstvisable.setVisibility(View.GONE);
//        } else {
//            hoder.view_firstvisable.setVisibility(View.VISIBLE);
//        }
        if (!TextUtils.isEmpty(cat)) {
            hoder.cat.setText(cat);
            hoder.cat.setVisibility(View.VISIBLE);
        } else {
            hoder.cat.setVisibility(View.GONE);
        }
        hoder.title.setText(audioVideoList.getTitle());
        hoder.price.setText(audioVideoList.getPrice());
        String type = audioVideoList.getType();
        if ("视频".equals(type)) {
            hoder.voice_img.setImageResource(R.drawable.video_small);
        } else {
            hoder.voice_img.setImageResource(R.drawable.default_voice);
        }
//        Log.e("TAG", "record.getPrice()=" + audioVideoList.getPrice());
        hoder.playtime.setText(audioVideoList.getPlaytime());
        hoder.watchnum.setText("播放"+audioVideoList.getWatchnum()+"次");
//        hoder.process.setText("已播"+record.getProcess());
        return view;
    }
    private class ViewHolder {
        ImageView voice_img;
        TextView title;
        TextView cat;
        TextView price;
        TextView playtime;
        TextView watchnum;
//        TextView process;
//        View view_firstvisable;
    }
    public void setData(ArrayList<AudioVideoBean.RAUdioVideo.AudioVideoList> audioVideoList) {
        this.audioVideoList = audioVideoList;
        notifyDataSetChanged();
    }
}
