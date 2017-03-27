package com.umiwi.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.AudioSpecialDetailsBean;

import java.util.ArrayList;

import static com.umiwi.ui.main.YoumiConfiguration.context;

/**
 * Created by Administrator on 2017/3/6.
 */

public class ColumnRecordAdapter extends BaseAdapter {


    private final FragmentActivity activity;
    private final ArrayList<AudioSpecialDetailsBean.RAudioSpecialDetails.LastRecord> last_record;

    public ColumnRecordAdapter(FragmentActivity activity, ArrayList<AudioSpecialDetailsBean.RAudioSpecialDetails.LastRecord> last_record) {
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ColumnRecordAdapter.ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.column_record,null);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ColumnRecordAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.tv_title.setText(last_record.get(position).getTitle());
        viewHolder.tv_time.setText(last_record.get(position).getOnlinetime());
        return convertView;
    }

    public class ViewHolder{
        TextView tv_title,tv_time;
    }
}
