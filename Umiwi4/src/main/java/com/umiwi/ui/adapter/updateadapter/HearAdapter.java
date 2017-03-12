package com.umiwi.ui.adapter.updateadapter;

import android.media.MediaPlayer;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.adapter.AskHearAdapter;
import com.umiwi.ui.beans.updatebeans.AlreadyAskBean;
import com.umiwi.ui.beans.updatebeans.DelayAnswerVoiceBean;
import com.umiwi.video.recorder.MediaManager;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.parsers.GsonParser;

/**
 * Created by Administrator on 2017/3/12.
 */

public class HearAdapter extends BaseAdapter {
    private FragmentActivity activity;
    private ArrayList<AlreadyAskBean.RAlreadyAnser.Question> hearInfos;
    public HearAdapter(FragmentActivity activity) {
        this.activity = activity;

    }

    @Override
    public int getCount() {
        return hearInfos.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = View.inflate(activity, R.layout.ask_hear,null);
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.buttontag = (TextView) view.findViewById(R.id.buttontag);
            viewHolder.tavatar = (ImageView) view.findViewById(R.id.tavatar);
            viewHolder.listennum = (TextView) view.findViewById(R.id.listennum);
            viewHolder.goodnum = (TextView) view.findViewById(R.id.goodnum);
            viewHolder.playtime = (TextView) view.findViewById(R.id.playtime);
            viewHolder.answertime = (TextView) view.findViewById(R.id.answertime);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        final AlreadyAskBean.RAlreadyAnser.Question question = hearInfos.get(i);

        viewHolder.title.setText(question.getTitle());
        viewHolder.buttontag.setText(question.getButtontag());
        viewHolder.listennum.setText(question.getListennum());
        viewHolder.goodnum.setText(question.getGoodnum());
        viewHolder.playtime.setText(question.getPlaytime());
        viewHolder.answertime.setText(question.getAnswertime());
        Glide.with(activity).load(question.getTavatar()).into(viewHolder.tavatar);
        viewHolder.buttontag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String playsource = hearInfos.get(i).getPlaysource();
                Log.e("aaa",playsource);
                getsorceInfos(playsource);
            }
        });
        return view;
    }

    private void getsorceInfos(String playsource) {
        cn.youmi.framework.http.GetRequest<DelayAnswerVoiceBean> request = new cn.youmi.framework.http.GetRequest<DelayAnswerVoiceBean>(
                playsource, GsonParser.class,
                DelayAnswerVoiceBean.class,
                AnswerListener);
        request.go();
    }

    private AbstractRequest.Listener<DelayAnswerVoiceBean> AnswerListener = new AbstractRequest.Listener<DelayAnswerVoiceBean>() {

        @Override
        public void onResult(AbstractRequest<DelayAnswerVoiceBean> request, DelayAnswerVoiceBean umiAnwebeans) {
            String source = umiAnwebeans.getrDelayAnserBeans().getSource();
            MediaManager.playSound(source, new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                }
            });

        }

        @Override
        public void onError(AbstractRequest<DelayAnswerVoiceBean> requet, int statusCode, String body) {

        }
    };

    public void setData(ArrayList<AlreadyAskBean.RAlreadyAnser.Question> hearInfos) {
        this.hearInfos = hearInfos;
        notifyDataSetChanged();
    }

    public class ViewHolder{
        private TextView title,listennum,goodnum,playtime,answertime;
        private ImageView tavatar;
        private TextView buttontag;
    }
}
