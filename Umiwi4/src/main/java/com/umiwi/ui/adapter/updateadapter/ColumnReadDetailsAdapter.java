package com.umiwi.ui.adapter.updateadapter;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.ColumnReadBean;

import java.util.ArrayList;

import static com.umiwi.ui.main.YoumiConfiguration.context;

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
            convertView = View.inflate(mActivity, R.layout.column_content_details,null);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.iv_column = (ImageView) convertView.findViewById(R.id.iv_column);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ColumnReadBean.RColumnRead.ReadContentWord readContentWord = content.get(position);
        if (!TextUtils.isEmpty(readContentWord.getStrong())) {
            viewHolder.tv_title.setVisibility(View.VISIBLE);
            viewHolder.tv_title.setText(readContentWord.getStrong());
        } else {
            viewHolder.tv_title.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(readContentWord.getWord())) {
            viewHolder.tv_content.setVisibility(View.VISIBLE);
            viewHolder.tv_content.setText(readContentWord.getWord());
        } else {
            viewHolder.tv_content.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(readContentWord.getImage())) {
            viewHolder.iv_column.setVisibility(View.VISIBLE);
            Glide.with(context).load(readContentWord.getImage()).into(viewHolder.iv_column);
        } else {
            viewHolder.iv_column.setVisibility(View.GONE);
        }

        return convertView;
    }
    class ViewHolder{
        TextView tv_title,tv_content;
        ImageView iv_column;
    }
}
