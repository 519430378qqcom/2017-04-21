package com.umiwi.ui.adapter.updateadapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.umiwi.ui.beans.updatebeans.VideoSpecialDetailBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/6 0006.
 */

public class VideoSpecialDetailAdapter extends BaseAdapter {
    private FragmentActivity activity;
    private ArrayList<VideoSpecialDetailBean.VideoSpecialRecord> mList;
    public VideoSpecialDetailAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return  mList.size();
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
        return null;
    }

    public void setData(ArrayList<VideoSpecialDetailBean.VideoSpecialRecord> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }
}
