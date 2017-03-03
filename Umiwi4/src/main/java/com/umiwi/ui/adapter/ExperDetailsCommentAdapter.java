package com.umiwi.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.CommentBean;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.List;

import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.view.CircleImageView;

/**
 * Created by Administrator on 2017/2/28.
 */

public class ExperDetailsCommentAdapter extends BaseAdapter {
    private FragmentActivity activity;
    private List<CommentBean.RecordBean> commentInfos;
    public ExperDetailsCommentAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return commentInfos.size();
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
            view = View.inflate(activity, R.layout.item_comment, null);
            viewHolder.userhead_imageview = (CircleImageView) view.findViewById(R.id.userhead_imageview);
            viewHolder.time_textview = (TextView) view.findViewById(R.id.time_textview);
            viewHolder.username_textview = (TextView) view.findViewById(R.id.username_textview);
            viewHolder.content_textview = (TextView) view.findViewById(R.id.content_textview);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        CommentBean.RecordBean recordBean = commentInfos.get(i);
        viewHolder.content_textview.setText(recordBean.getContent());
        viewHolder.username_textview.setText(recordBean.getName());
        viewHolder.time_textview.setText(recordBean.getTime());
        ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
        mImageLoader.loadImage(recordBean.getAvatar(), viewHolder.userhead_imageview);

        return view;
    }

    public void setData(List<CommentBean.RecordBean> commentInfos) {

        this.commentInfos = commentInfos;
        notifyDataSetChanged();
    }

    class ViewHolder{
        CircleImageView userhead_imageview;
        TextView username_textview;
        TextView time_textview;
        TextView content_textview;
    }

}
