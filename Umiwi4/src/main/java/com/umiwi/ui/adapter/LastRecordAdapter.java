package com.umiwi.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.ExperDetailsAlbumbean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/2.
 */

public class LastRecordAdapter extends BaseAdapter {
    private FragmentActivity activity;
    private List<ExperDetailsAlbumbean.LastRecordBean> last_record;
    public LastRecordAdapter(FragmentActivity activity, List<ExperDetailsAlbumbean.LastRecordBean> last_record) {
        this.activity = activity;
        this.last_record = last_record;
    }

    @Override
    public int getCount() {
        return last_record.size();
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
        Viewholder viewHolder= null;
        if (view == null){
            viewHolder = new Viewholder();
            view = View.inflate(activity, R.layout.last_record_item,null);
            viewHolder.title = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.onlinetime = (TextView) view.findViewById(R.id.onlinetime);
            view.setTag(viewHolder);
        }else{
            viewHolder = (Viewholder) view.getTag();
        }

        viewHolder.title.setText(last_record.get(i).getTitle());
        viewHolder.onlinetime.setText(last_record.get(i).getOnlinetime());
        return view;
    }

    class Viewholder{
        TextView title;
        TextView onlinetime;
    }
}
