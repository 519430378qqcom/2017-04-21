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
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.ArrayList;

/**
 * 类描述：
 * Created by Gpsi on 2017-02-25.
 */

public class NewfreeAdapterV2 extends BaseAdapter {

    private LayoutInflater mLayoutInflater;

    private ArrayList<RecommendBean.RBean.FreeBean.RecordBean> mList;

    private Activity mActivity;

    public NewfreeAdapterV2(Context context) {
        super();
        mLayoutInflater = LayoutInflater.from(UmiwiApplication.getInstance());
        this.mActivity = (Activity) context;
    }

    public NewfreeAdapterV2(Context context, ArrayList<RecommendBean.RBean.FreeBean.RecordBean> mList) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mActivity = (Activity) context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }


    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderV2 viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.new_free_layout_item, null);
            viewHolder = new ViewHolderV2(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderV2) convertView.getTag();
        }

        RecommendBean.RBean.FreeBean.RecordBean newFree = mList.get(position);

        viewHolder.free_context_textview_1.setText(newFree.getTitle());
        viewHolder.free_time_textview_1.setText(newFree.getPlaytime());
        if (newFree.getType().equals("video")) {
            viewHolder.iv_audio.setVisibility(View.GONE);
            viewHolder.iv_vedio.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_audio.setVisibility(View.VISIBLE);
            viewHolder.iv_vedio.setVisibility(View.GONE);
        }

        return convertView;
    }


}

class ViewHolderV2 {
    public View rootView;

    public TextView free_time_textview_1, free_context_textview_1;
    public ImageView iv_audio, iv_vedio;

    public ViewHolderV2(View rootView) {
        this.rootView = rootView;
        iv_audio = (ImageView) rootView.findViewById(R.id.iv_audio);
        iv_vedio = (ImageView) rootView.findViewById(R.id.iv_vedio);
        free_time_textview_1 = (TextView) rootView.findViewById(R.id.free_time_textview_1);
        free_context_textview_1 = (TextView) rootView.findViewById(R.id.free_context_textview_1);
    }

}
