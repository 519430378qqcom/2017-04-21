package com.umiwi.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.FastQuizBeans;

import java.util.List;

/**
 * Created by Administrator on 2017/3/14.
 */

public class FastQuizAdapter extends BaseAdapter {
    private Context context;
    private List<FastQuizBeans.RecordBean> recordBeanList;
    private FastQuizClickListener fastQuizClickListener;

    public FastQuizAdapter(Context context, List<FastQuizBeans.RecordBean> recordBeanList) {
        this.context = context;
        this.recordBeanList = recordBeanList;
    }

    @Override
    public int getCount() {
        return (recordBeanList == null ? 0 : recordBeanList.size());
    }

    @Override
    public Object getItem(int i) {
        return recordBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.fast_quiz_item, null);
            viewHolder.iv_heade = (ImageView) view.findViewById(R.id.iv_heade);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.tv_askpriceinfo = (TextView) view.findViewById(R.id.tv_askpriceinfo);
            viewHolder.quiz = (TextView) view.findViewById(R.id.quiz);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Glide.with(context).load(recordBeanList.get(position).getImage()).into(viewHolder.iv_heade);
        viewHolder.tv_name.setText(recordBeanList.get(position).getName());
        viewHolder.tv_title.setText(recordBeanList.get(position).getTitle());
        viewHolder.tv_askpriceinfo.setText(recordBeanList.get(position).getAskpriceinfo());
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fastQuizClickListener.OnFastQuizClickListener(finalViewHolder.quiz, position);
            }
        });
        return view;
    }

    public interface FastQuizClickListener {
        void OnFastQuizClickListener(View view, int position);
    }

    public void setOnFastQuizClickListener(FastQuizClickListener fastQuizClickListener) {
        this.fastQuizClickListener = fastQuizClickListener;
    }

    class ViewHolder {
        ImageView iv_heade;
        TextView tv_name, tv_title, tv_askpriceinfo, quiz;
    }
}
