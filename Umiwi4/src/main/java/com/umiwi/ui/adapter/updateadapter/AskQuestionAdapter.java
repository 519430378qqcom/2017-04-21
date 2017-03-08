package com.umiwi.ui.adapter.updateadapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.ExperDetailsWendaAdapter;
import com.umiwi.ui.beans.updatebeans.QuestionListBean;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.ArrayList;

import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.view.CircleImageView;

/**
 * Created by Administrator on 2017/3/8.
 */

public class AskQuestionAdapter extends BaseAdapter {
    private ArrayList<QuestionListBean.RecordBean> questionList;
    private FragmentActivity activity;
    public AskQuestionAdapter(FragmentActivity activity) {
        this.activity = activity;

    }

    @Override
    public int getCount() {
        return questionList.size();
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
        ViewHoder viewHoder = null;
        if (view == null){
            viewHoder = new ViewHoder();
            view= View.inflate(activity, R.layout.exper_details_wenda_item, null);
            viewHoder.tavatar = (CircleImageView) view.findViewById(R.id.tavatar);
            viewHoder.buttontag = (TextView) view.findViewById(R.id.buttontag);
            viewHoder.playtime = (TextView) view.findViewById(R.id.playtime);
            viewHoder.listennum = (TextView) view.findViewById(R.id.listennum);
            viewHoder.goodnum = (TextView) view.findViewById(R.id.goodnum);
            viewHoder.title = (TextView) view.findViewById(R.id.title);
            view.setTag(viewHoder);
        }else{
            viewHoder = (ViewHoder) view.getTag();
        }
        QuestionListBean.RecordBean recordBean = questionList.get(i);
        viewHoder.title.setText(recordBean.getTitle());
        viewHoder.buttontag.setText(recordBean.getButtontag());
        viewHoder.goodnum.setText(recordBean.getGoodnum());
        viewHoder.playtime.setText(recordBean.getAnswertime());
        viewHoder.listennum.setText(recordBean.getListennum());
        ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
        mImageLoader.loadImage(recordBean.getTavatar(), viewHoder.tavatar);
        return view;
    }

    public void setData(ArrayList<QuestionListBean.RecordBean> questionList) {
        this.questionList = questionList;
        notifyDataSetChanged();

    }

    class ViewHoder{
        CircleImageView tavatar;
        TextView buttontag;
        TextView playtime;
        TextView goodnum;
        TextView title;
        TextView listennum;
    }
}
