package com.umiwi.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.WenDaBean;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.List;

import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.view.CircleImageView;


/**
 * Created by Administrator on 2017/2/28.
 */

public class ExperDetailsWendaAdapter extends BaseAdapter {
    FragmentActivity activity;
    List<WenDaBean.RecordBean> wendaInfos;
    public ExperDetailsWendaAdapter(FragmentActivity activity) {
        this.activity = activity;

    }

    @Override
    public int getCount() {
        return wendaInfos.size();
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
            view = View.inflate(activity, R.layout.exper_details_wenda_item, null);
            viewHolder.tavatar = (CircleImageView) view.findViewById(R.id.tavatar);
            viewHolder.buttontag = (TextView) view.findViewById(R.id.buttontag);
            viewHolder.playtime = (TextView) view.findViewById(R.id.playtime);
            viewHolder.goodnum = (TextView) view.findViewById(R.id.goodnum);
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        WenDaBean.RecordBean recordBean = wendaInfos.get(i);
        String buttontag = recordBean.getButtontag();
        String tavatar = recordBean.getTavatar();
        String playtime = recordBean.getPlaytime();
        String goodnum = recordBean.getGoodnum();
        String title = recordBean.getTitle();
        viewHolder.title.setText(title);
        viewHolder.goodnum.setText(goodnum);
        viewHolder.playtime.setText(playtime);
        viewHolder.buttontag.setText(buttontag);
        ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
        mImageLoader.loadImage(tavatar, viewHolder.tavatar);
        return view;
    }

    public void setData(List<WenDaBean.RecordBean> wendaInfos) {
        this.wendaInfos = wendaInfos;
        notifyDataSetChanged();

    }

    class ViewHolder{
        CircleImageView tavatar;
        TextView buttontag;
        TextView playtime;
        TextView goodnum;
        TextView title;
    }

}
