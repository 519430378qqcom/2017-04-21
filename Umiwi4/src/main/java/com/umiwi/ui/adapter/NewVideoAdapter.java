package com.umiwi.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.VideoBean;

import java.util.List;

/**
 * Created by Gpsi on 2017/3/13.
 */
public class NewVideoAdapter extends BaseAdapter {
    private final Context context;
    private final List<VideoBean.RecordBean> mList;

    public NewVideoAdapter(Context context, List<VideoBean.RecordBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
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

        NewVideoAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new NewVideoAdapter.ViewHolder();
            convertView = View.inflate(context, R.layout.video_item_new, null);

            viewHolder.iv_author = (ImageView) convertView.findViewById(R.id.iv_author);
            viewHolder.iv_big_shot_image = (ImageView) convertView.findViewById(R.id.iv_big_shot_image);
            viewHolder.tv_video_time = (TextView) convertView.findViewById(R.id.tv_video_time);
            viewHolder.tv_video_type = (TextView) convertView.findViewById(R.id.tv_video_type);
            viewHolder.tv_dalao_type = (TextView) convertView.findViewById(R.id.tv_dalao_type);
            viewHolder.tv_big_shot_name = (TextView) convertView.findViewById(R.id.tv_big_shot_name);
            viewHolder.tv_big_shot_type = (TextView) convertView.findViewById(R.id.tv_big_shot_type);
            viewHolder.tv_comments = (TextView) convertView.findViewById(R.id.tv_comments);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NewVideoAdapter.ViewHolder) convertView.getTag();
        }

        Glide.with(context).load(mList.get(position).getLimage()).into(viewHolder.iv_author);
        Glide.with(context).load(mList.get(position).getLimage()).into(viewHolder.iv_big_shot_image);
        viewHolder.tv_video_time.setText(mList.get(position).getPlaytime());
        viewHolder.tv_video_type.setText(mList.get(position).getShortX());
        viewHolder.tv_dalao_type.setText(mList.get(position).getTutortitle());
        viewHolder.tv_big_shot_name.setText(mList.get(position).getName());
        viewHolder.tv_big_shot_type.setText(mList.get(position).getTutortitle());
        viewHolder.tv_comments.setText(mList.get(position).getWatchnum());

        return convertView;
    }

    class ViewHolder {

        private TextView tv_video_type, tv_dalao_type, tv_video_time, tv_big_shot_name, tv_big_shot_type, tv_comments;
        private ImageView iv_author, iv_big_shot_image;
    }
}
