package com.umiwi.ui.adapter.updateadapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.AnswerBean;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.view.CircleImageView;

import java.util.ArrayList;

import cn.youmi.framework.util.ImageLoader;

/**
 * Created by Administrator on 2017/3/11.
 */

public class MyAnswerAdapter extends BaseAdapter {
    FragmentActivity activity;
    private ArrayList<AnswerBean.RAnser.Question> questionsInfos;

    public MyAnswerAdapter(FragmentActivity activity) {
        this.activity = activity;

    }

    @Override
    public int getCount() {
        return questionsInfos.size();
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
        if (view == null){
            viewHolder = new ViewHolder();
            view = View.inflate(activity, R.layout.item_my_answer, null);
            viewHolder.describe = (TextView) view.findViewById(R.id.describe);
            viewHolder.header = (CircleImageView) view.findViewById(R.id.icon_umiwi);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.times = (TextView) view.findViewById(R.id.times);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        AnswerBean.RAnser.Question question = questionsInfos.get(i);
        viewHolder.name.setText(question.getTname());
        viewHolder.describe.setText(question.getTitle());
        viewHolder.times.setText(question.getAnswertime());
        ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
        mImageLoader.loadImage(question.getTavatar(), viewHolder.header);

        return view;
    }


    public void setData(ArrayList<AnswerBean.RAnser.Question> questionsInfos) {
        this.questionsInfos = questionsInfos;
        notifyDataSetChanged();

    }

    private class ViewHolder{
        CircleImageView header;
        TextView name;
        TextView times;
        TextView describe;

    }
}
