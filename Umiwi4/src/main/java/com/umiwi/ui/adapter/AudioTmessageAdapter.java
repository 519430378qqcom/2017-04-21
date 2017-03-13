package com.umiwi.ui.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.AudioTmessageListBeans;
import com.umiwi.ui.beans.updatebeans.HomeCoumnBean;

import java.util.List;

import static com.umiwi.ui.main.YoumiConfiguration.context;

/**
 * Created by Administrator on 2017/2/24.
 */

public class AudioTmessageAdapter extends BaseAdapter {
    private Context context;
    private List<AudioTmessageListBeans.RecordX.Record> mList;

    public AudioTmessageAdapter(Context context, List<AudioTmessageListBeans.RecordX.Record> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewholder = null;
        if (convertView == null) {
            viewholder = new ViewHolder();
            convertView = View.inflate(context, R.layout.audio_tmessage_item, null);

            viewholder.iv_heade = (ImageView) convertView.findViewById(R.id.iv_heade);
            viewholder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewholder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewholder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);

            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(mList.get(position).getAvatar()).into(viewholder.iv_heade);
        viewholder.tv_time.setText(mList.get(position).getTime());
        viewholder.tv_content.setText(mList.get(position).getContent());
        viewholder.tv_name.setText(mList.get(position).getName());
        return convertView;
    }

    private static class ViewHolder {
        private ImageView iv_heade;
        private TextView tv_name, tv_time, tv_content;
    }
}
