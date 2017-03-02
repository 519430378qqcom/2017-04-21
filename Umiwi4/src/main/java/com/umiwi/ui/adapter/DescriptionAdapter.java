package com.umiwi.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.ExperDetailsAlbumbean;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Administrator on 2017/3/2.
 */

public class DescriptionAdapter extends BaseAdapter {
    private FragmentActivity activity;
    private List<ExperDetailsAlbumbean.ContentBean> content;

    public DescriptionAdapter(FragmentActivity activity, List<ExperDetailsAlbumbean.ContentBean> content) {
        this.activity = activity;
        this.content = content;
    }

    @Override
    public int getCount() {
        return content.size();
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
        ViewHolder viewHolder;
        if (view==null){
            viewHolder = new ViewHolder();
            view = View.inflate(activity, R.layout.description_item,null);
            viewHolder.textview = (TextView) view.findViewById(R.id.tv_description);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textview.setText(content.get(i).getWord());
        return view;
    }

    private class ViewHolder{
        TextView textview;
    }
}
