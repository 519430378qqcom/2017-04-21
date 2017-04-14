package com.umiwi.ui.adapter.updateadapter;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.AlreadyShopVoiceBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/12.
 */

public class AlreadyVoiceAdapter extends BaseAdapter {
    private FragmentActivity activity;
    private ArrayList<AlreadyShopVoiceBean.RAlreadyVoice.Record> infos;

    public AlreadyVoiceAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return (infos == null ? 0 : infos.size());
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
        VoiceHoder hoder;
        if (view == null) {
            hoder = new VoiceHoder();
            view = View.inflate(activity, R.layout.item_already_voice, null);
            hoder.cat = (TextView) view.findViewById(R.id.cat);
            hoder.title = (TextView) view.findViewById(R.id.title);
            hoder.price = (TextView) view.findViewById(R.id.price);
            hoder.playtime = (TextView) view.findViewById(R.id.playtime);
            hoder.watchnum = (TextView) view.findViewById(R.id.watchnum);
            hoder.process = (TextView) view.findViewById(R.id.process);
            hoder.view_firstvisable = view.findViewById(R.id.view_firstvisable);
            hoder.price.setVisibility(View.GONE);
            hoder.process.setVisibility(View.INVISIBLE);
            view.setTag(hoder);
        } else {
            hoder = (VoiceHoder) view.getTag();
        }
        AlreadyShopVoiceBean.RAlreadyVoice.Record record = infos.get(i);
        String cat = record.getCat();
        if (i == 0) {
            hoder.view_firstvisable.setVisibility(View.GONE);
        } else {
            hoder.view_firstvisable.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(cat)) {
            hoder.cat.setText(cat);
            hoder.cat.setVisibility(View.VISIBLE);
        } else {
            hoder.cat.setVisibility(View.GONE);
        }
        hoder.title.setText(record.getTitle());

        hoder.price.setText(record.getPrice());
//        Log.e("TAG", "record.getPrice()=" + record.getPrice());
        hoder.playtime.setText(record.getPlaytime());
        hoder.watchnum.setText("播放"+record.getWatchnum()+"次");
        hoder.process.setText("已播"+record.getProcess());
        return view;
    }

    public void setData(ArrayList<AlreadyShopVoiceBean.RAlreadyVoice.Record> infos) {
        this.infos = infos;
        notifyDataSetChanged();
    }

    private class VoiceHoder {

        TextView title;
        TextView cat;
        TextView price;
        TextView playtime;
        TextView watchnum;
        TextView process;
        View view_firstvisable;
    }
}
