package com.umiwi.ui.adapter.updateadapter;

import android.media.MediaPlayer;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.AnswerBean;
import com.umiwi.ui.beans.updatebeans.DelayAnswerVoiceBean;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.view.CircleImageView;
import com.umiwi.video.recorder.MediaManager;

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
    private ArrayList<View> viewlist = new ArrayList();

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

        if (postion+1>viewlist.size()){
            hoder = new ViewHoder();
            view = View.inflate(activity, R.layout.item_my_already_answer,null);
            hoder.header = (CircleImageView) view.findViewById(R.id.head);
            hoder.miaoshu = (TextView) view.findViewById(R.id.miaoshu);
            hoder.name = (TextView) view.findViewById(R.id.name);
            hoder.times = (TextView) view.findViewById(R.id.times);
            hoder.paly_voice = (TextView) view.findViewById(R.id.paly_voice);
            hoder.paly_times = (TextView) view.findViewById(R.id.paly_times);
            view.setTag(hoder);
            viewlist.add(view);
        } else{
            view=viewlist.get(postion);
            hoder = (ViewHoder) view.getTag();
        }
        final AnswerBean.RAnser.Question question = questionsInfos.get(postion);

        hoder.miaoshu.setText(question.getTitle());
        hoder.name.setText(question.getTname());
        hoder.times.setText(question.getAnswertime());
        hoder.paly_times.setText(question.getPlaytime());
        cn.youmi.framework.util.ImageLoader imageLoader = new cn.youmi.framework.util.ImageLoader(activity);
        imageLoader.loadImage(question.getTavatar(),hoder.header);
        if (!TextUtils.isEmpty(question.getPalyname())){
            hoder.paly_voice.setText(question.getPalyname());
        }
        hoder.paly_voice.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               try {
                   if (UmiwiApplication.mainActivity.service != null && UmiwiApplication.mainActivity.service.isPlaying()) {
                       UmiwiApplication.mainActivity.service.pause();
                   }
               } catch (RemoteException e) {
                   e.printStackTrace();
               }
               if (currentpos!=-1){
//                   mCurrentVoiceListener.getCurrentView(currentpos);
                   questionsInfos.get(currentpos).setPalyname("播放");
                   notifyDataSetChanged();
                   if (currentpos == postion){
                     if (MediaManager.mediaPlayer.isPlaying()){
                         MediaManager.pause();
                         hoder.paly_voice.setText("播放");
                         Log.e("ISPALY","PAUSE");
                     }else{
                         MediaManager.resume();
                         Log.e("ISPALY","resume");
                         questionsInfos.get(postion).setPalyname("正在播放");
                         notifyDataSetChanged();

                     }
                        MediaManager.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                hoder.paly_voice.setText("播放");

                            }
                        });

                       return;
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
                                    paly_voice.setText("播放");

                            }
                        });
                        if (MediaManager.mediaPlayer.isPlaying()){
                                paly_voice.setText("正在播放");

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

    public interface CurrentVoiceListener{
        void getCurrentView(int currentpos);
    }

    public CurrentVoiceListener mCurrentVoiceListener;
    public void setCurrentVoiceListener(CurrentVoiceListener currentVoiceListener){
        mCurrentVoiceListener = currentVoiceListener;
    }
}
