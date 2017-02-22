package com.umiwi.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.youmi.framework.main.ContextProvider;
import cn.youmi.framework.util.ImageLoader;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.ActivityItemBean;
import com.umiwi.ui.main.UmiwiApplication;

public class ActivityAdapter extends BaseAdapter {

    private ArrayList<ActivityItemBean> mList;

    private LayoutInflater mLayoutInflater;

    public ActivityAdapter(Context context, ArrayList<ActivityItemBean> mList) {
        mLayoutInflater = LayoutInflater.from(UmiwiApplication.getInstance());
        this.mList = mList;
    }

    public void setData(ArrayList<ActivityItemBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ActivityItemBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mLayoutInflater.inflate(R.layout.item_activity, null);
        ViewHolder viewHolder = getHolder(convertView);

        ActivityItemBean ab = getItem(position);
        viewHolder.nameTextView.setText(ab.getTitle());
        viewHolder.dateTextView.setText("时间: " + ab.getStartTime());
        viewHolder.locationTextView.setText("地点: " + ab.getCity());

        ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
        mImageLoader.loadImage(ab.getImageURL(), viewHolder.imageView);

        if (!ab.isEnd()) {
            viewHolder.statusImageView.setBackgroundDrawable(UmiwiApplication.getInstance().getResources().getDrawable(R.drawable.activity_stuts_active));
        } else {
            viewHolder.statusImageView.setBackgroundDrawable(UmiwiApplication.getInstance().getResources().getDrawable(R.drawable.activity_stuts_over));
        }

        return convertView;
    }

    private ViewHolder getHolder(final View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        return holder;
    }

    private class ViewHolder {
        public ViewHolder(View v) {
            v.setTag(this);
            imageView = (ImageView) v.findViewById(R.id.image_view);
            nameTextView = (TextView) v.findViewById(R.id.name_text_view);
            dateTextView = (TextView) v.findViewById(R.id.date_text_view);
            statusImageView = (ImageView) v.findViewById(R.id.status_image_view);
            locationTextView = (TextView) v.findViewById(R.id.location_text_view);
        }

        private ImageView imageView;
        private TextView nameTextView;
        private TextView dateTextView;
        private ImageView statusImageView;
        private TextView locationTextView;
    }
}
