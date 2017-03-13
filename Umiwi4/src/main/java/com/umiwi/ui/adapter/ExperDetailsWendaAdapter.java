package com.umiwi.ui.adapter;

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
import com.umiwi.ui.beans.updatebeans.WenDaBean;
import com.umiwi.ui.beans.updatebeans.ZanBean;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.video.recorder.MediaManager;

import java.util.List;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.view.CircleImageView;


/**
 * Created by Administrator on 2017/2/28.
 */

public class ExperDetailsWendaAdapter extends BaseAdapter {
    FragmentActivity activity;
    List<HomeAskBean.RAlHomeAnser.Record> wendaInfos;
    private int currentpos = -1;
    private String url;
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder ;
        if (view == null){
            viewHolder = new ViewHolder();
            view = View.inflate(activity, R.layout.exper_details_wenda_item, null);
            viewHolder.tavatar = (CircleImageView) view.findViewById(R.id.tavatar);
            viewHolder.buttontag = (TextView) view.findViewById(R.id.buttontag);
            viewHolder.playtime = (TextView) view.findViewById(R.id.playtime);
            viewHolder.goodnum = (TextView) view.findViewById(R.id.goodnum);
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.awtimes = (TextView) view.findViewById(R.id.awtimes);
            viewHolder.listennum = (TextView) view.findViewById(R.id.listennum);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        HomeAskBean.RAlHomeAnser.Record recordBean = wendaInfos.get(position);
        String buttontag = recordBean.getButtontag();
        String tavatar = recordBean.getTavatar();
        String playtime = recordBean.getPlaytime();
        final String listentype = recordBean.getListentype();
        String goodnum = recordBean.getGoodnum();
        String title = recordBean.getTitle();
        viewHolder.awtimes.setText(recordBean.getAnswertime());
        viewHolder.title.setText(title);
        viewHolder.goodnum.setText(goodnum);
        viewHolder.playtime.setText(playtime);
        viewHolder.listennum.setText(recordBean.getListennum());
        boolean goodstate = recordBean.getGoodstate();
        if (goodstate){ //点赞了
            Drawable img = activity.getResources().getDrawable(R.drawable.zan_main);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            viewHolder.goodnum.setCompoundDrawables(img,null,null,null);
        }else{
            Drawable img = activity.getResources().getDrawable(R.drawable.zan_img);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            viewHolder.goodnum.setCompoundDrawables(img,null,null,null);
        }

        if (listentype.equals("1")){
            viewHolder.buttontag.setText(buttontag);
            viewHolder.buttontag.setBackgroundResource(R.drawable.changer);
        }else{
            viewHolder.buttontag.setText(buttontag);
            viewHolder.buttontag.setBackgroundResource(R.drawable.time_limit_hear);
        }

        ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
        mImageLoader.loadImage(tavatar, viewHolder.tavatar);



        viewHolder.goodnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean state = wendaInfos.get(position).getGoodstate();
                String id = wendaInfos.get(position).getId();

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
                            wendaInfos.get(position).setGoodstate(false);
                            String goodnum = wendaInfos.get(position).getGoodnum();
                            int goonumint = Integer.parseInt(goodnum);
                            wendaInfos.get(position).setGoodnum(goonumint-1+"");
                        }else{
                            String goodnum = wendaInfos.get(position).getGoodnum();
                            int goonumint = Integer.parseInt(goodnum);
                            wendaInfos.get(position).setGoodstate(true);
                            wendaInfos.get(position).setGoodnum(goonumint+1+"");
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
                if (listentype.equals("1")){
                    Toast.makeText(activity, "一元偷偷听", Toast.LENGTH_SHORT).show();
                }else{

                    if (currentpos!=-1){
//                   mCurrentVoiceListener.getCurrentView(currentpos);
                        wendaInfos.get(currentpos).setButtontag("立即听");
                        notifyDataSetChanged();
                        if (currentpos == position){
                            if (MediaManager.mediaPlayer.isPlaying()){
                                MediaManager.pause();
                                viewHolder.buttontag.setText("立即听");
                                Log.e("ISPALY","PAUSE");
                            }else{
                                MediaManager.resume();
                                Log.e("ISPALY","resume");
                                wendaInfos.get(position).setButtontag("正在播放");
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
                    String playsource = wendaInfos.get(position).getPlaysource();
                    getsorceInfos(playsource,viewHolder.buttontag);
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

    public void setData(List<HomeAskBean.RAlHomeAnser.Record> wendaInfos) {
        this.wendaInfos = wendaInfos;
        notifyDataSetChanged();

    }

    class ViewHolder{
        CircleImageView tavatar;
        TextView buttontag;
        TextView playtime,awtimes,listennum;
        TextView goodnum;
        TextView title;
    }

}
