package com.umiwi.ui.adapter.updateadapter;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
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
import com.umiwi.ui.adapter.AskHearAdapter;
import com.umiwi.ui.beans.updatebeans.AlreadyAskBean;
import com.umiwi.ui.beans.updatebeans.DelayAnswerVoiceBean;
import com.umiwi.ui.beans.updatebeans.ZanBean;
import com.umiwi.ui.main.UmiwiAPI;
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
    private ArrayList<View> viewlist = new ArrayList();
    private int currentpos = -1;
    private String url;
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (position+1>viewlist.size()){
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
            viewlist.add(view);

        }else{
            view=viewlist.get(position);
            viewHolder = (ViewHolder) view.getTag();
        }
        final AlreadyAskBean.RAlreadyAnser.Question question = hearInfos.get(position);
        boolean goodstate = question.getGoodstate();
        if (goodstate){ //点赞了
            Drawable img = activity.getResources().getDrawable(R.drawable.zan_main);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            viewHolder.goodnum.setCompoundDrawables(img,null,null,null);
        }else{
            Drawable img = activity.getResources().getDrawable(R.drawable.zan_img);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            viewHolder.goodnum.setCompoundDrawables(img,null,null,null);
        }
        viewHolder.title.setText(question.getTitle());
        viewHolder.buttontag.setText(question.getButtontag());
        viewHolder.listennum.setText(question.getListennum());
        viewHolder.goodnum.setText(question.getGoodnum());
        viewHolder.playtime.setText(question.getPlaytime());
        viewHolder.answertime.setText(question.getAnswertime());
        Glide.with(activity).load(question.getTavatar()).into(viewHolder.tavatar);
        if (!TextUtils.isEmpty(question.getButtontag())){
            viewHolder.buttontag.setText(question.getButtontag());
        }


        viewHolder.goodnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean state = hearInfos.get(position).getGoodstate();
                String id = hearInfos.get(position).getId();

                if (state == true){ //取消点赞
                    url = String.format(UmiwiAPI.ZAN,id,2);

                }else{  //点赞
                    url = String.format(UmiwiAPI.ZAN,id,1);
                }

                cn.youmi.framework.http.GetRequest<ZanBean> request = new cn.youmi.framework.http.GetRequest<ZanBean>(
                        url, GsonParser.class,
                        ZanBean.class, new AbstractRequest.Listener<ZanBean>() {
                    @Override
                    public void onResult(AbstractRequest<ZanBean> request, ZanBean zanBean) {
                        if (state){
                            hearInfos.get(position).setGoodstate(false);
                            String goodnum = hearInfos.get(position).getGoodnum();
                            int goonumint = Integer.parseInt(goodnum);
                            hearInfos.get(position).setGoodnum(goonumint-1+"");
                        }else{
                            String goodnum = hearInfos.get(position).getGoodnum();
                            int goonumint = Integer.parseInt(goodnum);
                            hearInfos.get(position).setGoodstate(true);
                            hearInfos.get(position).setGoodnum(goonumint+1+"");
                        }
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onError(AbstractRequest<ZanBean> requet, int statusCode, String body) {
                        Log.e("onresult","失败了");

                    }
                });
                request.go();
            }
        });


        viewHolder.buttontag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentpos!=-1){
//                   mCurrentVoiceListener.getCurrentView(currentpos);
                    hearInfos.get(currentpos).setButtontag("立即听");
                    notifyDataSetChanged();
                    if (currentpos == position){
                        if (MediaManager.mediaPlayer.isPlaying()){
                            MediaManager.pause();
                            viewHolder.buttontag.setText("立即听");
                            Log.e("ISPALY","PAUSE");
                        }else{
                            MediaManager.resume();
                            Log.e("ISPALY","resume");
                            hearInfos.get(position).setButtontag("正在播放");
                            notifyDataSetChanged();

                        }
                        MediaManager.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                viewHolder.buttontag.setText("立即听");

                            }
                        });

                        return;
                    }

                }

                currentpos = position;
                String playsource = hearInfos.get(position).getPlaysource();
                Log.e("aaa",playsource);
                getsorceInfos(playsource,viewHolder.buttontag);
            }
        });
        return view;
    }

    private void getsorceInfos(String playsource, final TextView buttontag) {
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
                                buttontag.setText("立即听");

                            }
                        });
                        if (MediaManager.mediaPlayer.isPlaying()){
                            buttontag.setText("正在播放");

                        }
                    }

                    @Override
                    public void onError(AbstractRequest<DelayAnswerVoiceBean> requet, int statusCode, String body) {

                    }
                });
        request.go();
    }



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
