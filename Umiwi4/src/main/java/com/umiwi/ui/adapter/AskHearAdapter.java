package com.umiwi.ui.adapter;

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
import com.umiwi.ui.beans.updatebeans.AlreadyAskBean;
import com.umiwi.ui.beans.updatebeans.DelayAnswerVoiceBean;
import com.umiwi.video.recorder.MediaManager;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.parsers.GsonParser;

import static com.umiwi.ui.main.YoumiConfiguration.context;

/**
 * Created by Administrator on 2017/3/6.
 */

public class AskHearAdapter extends BaseAdapter{

    private  FragmentActivity activity;
    private  ArrayList mList;
    private ArrayList<AlreadyAskBean.RAlreadyAnser.Question> askInfos;
    public AskHearAdapter(FragmentActivity activity) {
        this.activity = activity;

    }

    @Override
    public int getCount() {
        return askInfos.size();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(activity,R.layout.ask_hear,null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.buttontag = (TextView) convertView.findViewById(R.id.buttontag);
            viewHolder.tavatar = (ImageView) convertView.findViewById(R.id.tavatar);
            viewHolder.listennum = (TextView) convertView.findViewById(R.id.listennum);
            viewHolder.goodnum = (TextView) convertView.findViewById(R.id.goodnum);
            viewHolder.playtime = (TextView) convertView.findViewById(R.id.playtime);
            viewHolder.answertime = (TextView) convertView.findViewById(R.id.answertime);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final AlreadyAskBean.RAlreadyAnser.Question question = askInfos.get(position);

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
                String playsource = askInfos.get(position).getPlaysource();
                String id = askInfos.get(position).getId();
                Log.e("bbb",playsource+id);
                getsorceInfos(playsource+id);
            }
        });
        return convertView;
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
    public void setData(ArrayList<AlreadyAskBean.RAlreadyAnser.Question> askInfos) {
        this.askInfos = askInfos;
        notifyDataSetChanged();

    }

    public class ViewHolder{
        private TextView title,listennum,goodnum,playtime,answertime;
        private ImageView tavatar;
        private TextView buttontag;
    }
}
