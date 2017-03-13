package com.umiwi.ui.adapter.updateadapter;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.DelayAnswerVoiceBean;
import com.umiwi.ui.beans.updatebeans.HomeAskBean;
import com.umiwi.ui.beans.updatebeans.ZanBean;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.video.recorder.MediaManager;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.view.CircleImageView;

/**
 * Created by Administrator on 2017/3/8.
 */

public class AskQuestionAdapter extends BaseAdapter {
    private ArrayList<HomeAskBean.RAlHomeAnser.Record> questionList;
    private FragmentActivity activity;
    private int currentpos = -1;
    private String url;

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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHoder viewHoder;
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
        final HomeAskBean.RAlHomeAnser.Record recordBean = questionList.get(position);
        viewHoder.title.setText(recordBean.getTitle());
        final String listentype = recordBean.getListentype();
        if (listentype.equals("1")){
            viewHoder.buttontag.setText(recordBean.getButtontag());
            viewHoder.buttontag.setBackgroundResource(R.drawable.changer);
        }else{
            viewHoder.buttontag.setText(recordBean.getButtontag());
            viewHoder.buttontag.setBackgroundResource(R.drawable.time_limit_hear);
        }
        boolean goodstate = recordBean.getGoodstate();
        if (goodstate){ //点赞了
            Drawable img = activity.getResources().getDrawable(R.drawable.zan_main);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            viewHoder.goodnum.setCompoundDrawables(img,null,null,null);
        }else{
            Drawable img = activity.getResources().getDrawable(R.drawable.zan_img);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            viewHoder.goodnum.setCompoundDrawables(img,null,null,null);
        }
        viewHoder.goodnum.setText(recordBean.getGoodnum());
        viewHoder.playtime.setText(recordBean.getAnswertime());
        viewHoder.listennum.setText(recordBean.getListennum());
        ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
        mImageLoader.loadImage(recordBean.getTavatar(), viewHoder.tavatar);


        viewHoder.goodnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean state = questionList.get(position).getGoodstate();
                String id = questionList.get(position).getId();

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
                            questionList.get(position).setGoodstate(false);
                            String goodnum = questionList.get(position).getGoodnum();
                            int goonumint = Integer.parseInt(goodnum);
                            questionList.get(position).setGoodnum(goonumint-1+"");
                        }else{
                            String goodnum = questionList.get(position).getGoodnum();
                            int goonumint = Integer.parseInt(goodnum);
                            questionList.get(position).setGoodstate(true);
                            questionList.get(position).setGoodnum(goonumint+1+"");
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


        viewHoder.buttontag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   if (listentype.equals("1")){
                       Toast.makeText(activity, "一元偷偷听", Toast.LENGTH_SHORT).show();
                   } else {
                       if (currentpos!=-1){
                           questionList.get(currentpos).setButtontag("立即听");
                           notifyDataSetChanged();
                           if (currentpos == position){
                               if (MediaManager.mediaPlayer.isPlaying()){
                                   MediaManager.pause();
                                   viewHoder.buttontag.setText("立即听");
                                   Log.e("ISPALY","PAUSE");
                               }else{
                                   MediaManager.resume();
                                   Log.e("ISPALY","resume");
                                   questionList.get(position).setButtontag("正在播放");
                                   notifyDataSetChanged();
                               }
                               MediaManager.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                   @Override
                                   public void onCompletion(MediaPlayer mediaPlayer) {
                                       viewHoder.buttontag.setText("立即听");

                                   }
                               });

                               return;
                           }

                       }

                       currentpos = position;
                       String playsource = questionList.get(position).getPlaysource();
                       Log.e("aaa",playsource);
                       getsorceInfos(playsource,viewHoder.buttontag);
                   }

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

    public void setData(ArrayList<HomeAskBean.RAlHomeAnser.Record> questionList) {
        this.questionList = questionList;
        notifyDataSetChanged();

    }

   private class ViewHoder{
        CircleImageView tavatar;
        TextView buttontag;
        TextView playtime;
        TextView goodnum;
        TextView title;
        TextView listennum;
    }
}
