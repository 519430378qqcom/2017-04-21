package com.umiwi.ui.adapter.updateadapter;

import android.media.MediaPlayer;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.AnswerBean;
import com.umiwi.ui.beans.updatebeans.DelayAnswerVoiceBean;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.view.CircleImageView;
import com.umiwi.video.recorder.MediaManager;
import com.zhy.http.okhttp.request.GetRequest;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.parsers.GsonParser;

/**
 * Created by Administrator on 2017/3/11.
 */

public class MyAlreadyAnswerAdapter extends BaseAdapter {
    private ArrayList<AnswerBean.RAnser.Question> questionsInfos;
    FragmentActivity activity;
    private int currentpos = -1;
    public MyAlreadyAnswerAdapter(FragmentActivity activity) {
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
    public View getView(final int postion, View view, final ViewGroup viewGroup) {
        final ViewHoder hoder;

        if (view == null){
            hoder = new ViewHoder();
            view = View.inflate(activity, R.layout.item_my_already_answer,null);
            hoder.header = (CircleImageView) view.findViewById(R.id.head);
            hoder.miaoshu = (TextView) view.findViewById(R.id.miaoshu);
            hoder.name = (TextView) view.findViewById(R.id.name);
            hoder.times = (TextView) view.findViewById(R.id.times);
            hoder.paly_voice = (TextView) view.findViewById(R.id.paly_voice);
            hoder.paly_times = (TextView) view.findViewById(R.id.paly_times);
            view.setTag(hoder);
        } else{
            hoder = (ViewHoder) view.getTag();
        }
        final AnswerBean.RAnser.Question question = questionsInfos.get(postion);

        hoder.miaoshu.setText(question.getTitle());
        hoder.name.setText(question.getTname());
        hoder.times.setText(question.getAnswertime());
        hoder.paly_times.setText(question.getPlaytime());
        cn.youmi.framework.util.ImageLoader imageLoader = new cn.youmi.framework.util.ImageLoader(activity);
        imageLoader.loadImage(question.getTavatar(),hoder.header);
        hoder.paly_voice.setTag("tag");
        hoder.paly_voice.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (currentpos!=-1){
                   View childAt = viewGroup.getChildAt(currentpos);
                   TextView textView = (TextView) childAt.findViewById(R.id.paly_voice);
                   textView.setText("播放");
                   if (currentpos == postion){
                     if (MediaManager.mediaPlayer.isPlaying()){
                         MediaManager.pause();
                     }else{
                         MediaManager.resume();
                     }
                   }
               }

               currentpos = postion;

               AnswerBean.RAnser.Question infos = questionsInfos.get(postion);
               String playsource = infos.getPlaysource();
               Log.e("aaa",playsource);
               if (!TextUtils.isEmpty(playsource)){
                  getsorveInfos(playsource,hoder.paly_voice);
               }

           }
       });
        return view;
    }

    private void getsorveInfos(String playsource, final TextView paly_voice) {
        cn.youmi.framework.http.GetRequest<DelayAnswerVoiceBean> request = new cn.youmi.framework.http.GetRequest<DelayAnswerVoiceBean>(
                playsource, GsonParser.class,
                DelayAnswerVoiceBean.class,
                new AbstractRequest.Listener<DelayAnswerVoiceBean>() {
                    @Override
                    public void onResult(AbstractRequest<DelayAnswerVoiceBean> request, DelayAnswerVoiceBean delayAnswerVoiceBean) {
                        String source = delayAnswerVoiceBean.getrDelayAnserBeans().getSource();
                        MediaManager.relese();
                        MediaManager.playSound(source, new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                if (paly_voice.getTag()!=null&&paly_voice.getTag() == "tag"){
                                    paly_voice.setText("播放");

                                }

                            }
                        });
                        if (MediaManager.mediaPlayer.isPlaying()){
                            if (paly_voice.getTag()!=null&&paly_voice.getTag() == "tag"){
                                paly_voice.setText("正在播放");

                            }else {
                                paly_voice.setText("播放");
                            }
                        }
                    }

                    @Override
                    public void onError(AbstractRequest<DelayAnswerVoiceBean> requet, int statusCode, String body) {

                    }
                });
        request.go();
    }

    public void setData(ArrayList<AnswerBean.RAnser.Question> questionsInfos) {
        this.questionsInfos = questionsInfos;
        notifyDataSetChanged();
    }

    private class ViewHoder{
        CircleImageView  header;
        TextView name;
        TextView times;
        TextView miaoshu;
        TextView paly_voice;
        TextView paly_times;
    }
}
