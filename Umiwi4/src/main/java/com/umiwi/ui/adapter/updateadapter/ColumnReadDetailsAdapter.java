package com.umiwi.ui.adapter.updateadapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.ColumnReadBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class ColumnReadDetailsAdapter extends BaseAdapter {

    private FragmentActivity mActivity;
    private ArrayList<ColumnReadBean.RColumnRead.ReadContentWord> content;
    public ColumnReadDetailsAdapter(FragmentActivity activity, ArrayList<ColumnReadBean.RColumnRead.ReadContentWord> content) {
        this.mActivity = activity;
        this.content = content;
    }

    @Override
    public int getCount() {
        return content.size();
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
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(mActivity, R.layout.column_details,null);
            viewHolder.tv_jianjie = (TextView) convertView.findViewById(R.id.tv_jianjie);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_jianjie.setText(content.get(position).getWord());
        return convertView;
    }
    class ViewHolder{
        TextView tv_jianjie;
    }
}
