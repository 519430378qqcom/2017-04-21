package com.umiwi.ui.adapter.updateadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.NewFree;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.model.HomeRecommend;

import java.util.ArrayList;

/**
 * Created by LvDabing on 2017/2/17.
 * Email：lvdabing@lvshandian.com
 * Detail:最新免费
 */

public class NewfreeAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;

    private ArrayList<NewFree> mList;

    private Activity mActivity;

    public NewfreeAdapter(Context context) {
        super();
        mLayoutInflater = LayoutInflater.from(UmiwiApplication.getInstance());
        this.mActivity = (Activity) context;
    }

    public NewfreeAdapter(Context context, ArrayList<NewFree> mList) {
        mLayoutInflater = LayoutInflater.from(UmiwiApplication.getInstance());
        this.mActivity = (Activity) context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }


    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            convertView = mLayoutInflater.inflate(R.layout.new_free_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NewFree newFree = mList.get(position);

        viewHolder.freeContext.setText(newFree.getTitle());
        viewHolder.freeTime.setText(newFree.getPlaytime());
        if (newFree.getIcontype().equals("video")) {
            viewHolder.freeType.setImageResource(R.drawable.video_small);
        } else {
            viewHolder.freeType.setImageResource(R.drawable.audio);
        }

        return convertView;
    }


}

class ViewHolder {
    public View rootView;

    public TextView freeContext;
    public TextView freeTime;
    public ImageView freeType;

    public ViewHolder(View rootView) {
        this.rootView = rootView;
        freeType = (ImageView) rootView.findViewById(R.id.free_type_imageview_1);
        freeTime = (TextView) rootView.findViewById(R.id.free_time_textview_1);
        freeContext = (TextView) rootView.findViewById(R.id.free_context_textview_1);
    }


}
