package com.umiwi.ui.adapter.updateadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.beans.UmiwiBuyCreateOrderBeans;
import com.umiwi.ui.beans.updatebeans.AnswerBean;
import com.umiwi.ui.beans.updatebeans.DelayAnswerVoiceBean;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.fragment.pay.PayingFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.LoginUtil;
import com.umiwi.ui.view.CircleImageView;
import com.umiwi.video.recorder.MediaManager;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ImageLoader;

/**
 * 首页-推荐-行家回答 下部分- Adapter
 * Created by ${Gpsi} on 2017/2/28.
 */

public class ExpertAnswerDowndapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private int currentpos = -1;
    private boolean isStop = false;
    private ArrayList<RecommendBean.RBean.QuestionBean> mList;
    private ImageLoader mImageLoader;
    private Activity mActivity;

    public ExpertAnswerDowndapter(Context context, ArrayList<RecommendBean.RBean.QuestionBean> mList) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mActivity = (Activity) context;
        this.mList = mList;
        mImageLoader = new ImageLoader(UmiwiApplication.getApplication());

    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.home_expert_answer_down_item, null);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final RecommendBean.RBean.QuestionBean questionBean = mList.get(position);

        mImageLoader.loadImage(questionBean.getTavatar(), mViewHolder.iv_expert_header, R.drawable.ic_launcher);
        final String listentype = questionBean.getListentype();
        if (listentype.equals("1")) {
            mViewHolder.tv_time_limit_hear.setText(questionBean.getButtontag());
            mViewHolder.tv_time_limit_hear.setBackgroundResource(R.drawable.changer);
        } else {
            mViewHolder.tv_time_limit_hear.setText(questionBean.getButtontag());
            mViewHolder.tv_time_limit_hear.setBackgroundResource(R.drawable.time_limit_hear);
        }

        mViewHolder.tv_expert_question.setText(questionBean.getTitle());
        mViewHolder.tv_head_time.setText(questionBean.getPlaytime());
        mViewHolder.tv_time_limit_number.setText("听过 " + questionBean.getListennum() + "人");
        mViewHolder.tv_expert_question_name.setText(questionBean.getTname());
        mViewHolder.tv_expert_question_job.setText(questionBean.getTtitle());
        mViewHolder.tv_time_limit_hear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStop == true){
                    String playsource = mList.get(position).getPlaysource();
                    String buttontag = mList.get(position).getButtontag();
                    getsorceInfos(playsource, mViewHolder.tv_time_limit_hear, buttontag);
                }

                try {
                    if (UmiwiApplication.mainActivity.service != null && UmiwiApplication.mainActivity.service.isPlaying()) {
                        UmiwiApplication.mainActivity.service.pause();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                if (listentype.equals("1")) {
                    //添加登录判断
                    if (!YoumiRoomUserManager.getInstance().isLogin()) {
                        LoginUtil.getInstance().showLoginView(mActivity);
                    } else {
                        getOrderId(questionBean.getId());
                    }
                } else {
                    if (currentpos != -1) {
//                   mCurrentVoiceListener.getCurrentView(currentpos);
                        mList.get(currentpos).setButtontag(questionBean.getButtontag());
                        notifyDataSetChanged();
                        if (currentpos == position) {
                            if (MediaManager.mediaPlayer.isPlaying() && MediaManager.mediaPlayer != null) {
                                MediaManager.pause();
                                mList.get(position).setButtontag("立即听");
                                notifyDataSetChanged();

                                Log.e("ISPALY", "PAUSE");
                            } else {
                                MediaManager.resume();
                                Log.e("ISPALY", "resume");
                                mList.get(position).setButtontag("正在播放");
                                notifyDataSetChanged();
                            }

                            MediaManager.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    mViewHolder.tv_time_limit_hear.setText("立即听");
                                    isStop = true;
                                }
                            });

                            return;
                        }

                    }

                    currentpos = position;
                    String playsource = mList.get(position).getPlaysource();
                    String buttontag = mList.get(position).getButtontag();
                    getsorceInfos(playsource, mViewHolder.tv_time_limit_hear, buttontag);
                }

            }
        });
        mViewHolder.iv_expert_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent (mActivity,UmiwiContainerActivity.class);
//                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VoiceDetailsFragment.class);
//                intent.putExtra(VoiceDetailsFragment.KEY_DETAILURL,);
//                mActivity.startActivity(intent);
//                Intent intent = new Intent(mActivity, UmiwiContainerActivity.class);
//                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, AskQuestionFragment.class);
//                intent.putExtra("uid",questionBean.getTuid());
//                mActivity.startActivity(intent);
            }
        });
        return convertView;
    }

    private void getsorceInfos(String playsource, final TextView tv_time_limit_hear, final String buttontag) {
        GetRequest<DelayAnswerVoiceBean> request = new GetRequest<DelayAnswerVoiceBean>(
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
                                tv_time_limit_hear.setText("立即听");
                                isStop = true;

                            }
                        });
                        if (MediaManager.mediaPlayer.isPlaying()) {
                            tv_time_limit_hear.setText("正在播放");
                            isStop = false;
                        }
                    }


                    @Override
                    public void onError(AbstractRequest<DelayAnswerVoiceBean> requet, int statusCode, String body) {

                    }
                });
        request.go();
    }

    public class ViewHolder {
        public View rootView;
        public ImageView iv_expert_header;
        //微信小程序可以直播么？- 限时免费听 - 58″ - 听过55555人 - 姓名 - 职位
        public TextView tv_expert_question, tv_time_limit_hear, tv_head_time, tv_time_limit_number, tv_expert_question_name, tv_expert_question_job;
        //id:2
        public String id;
        //tuid:7106276
        public String tuid;
        //listentype:2
        public String listentype;
        //playsource:http://i.v.youmi.cn/question/playsourceapi?id=2
        public String playsource;

        public ViewHolder(View convertView) {
            this.rootView = convertView;
            iv_expert_header = (CircleImageView) rootView.findViewById(R.id.expert_header_imageview);
            tv_expert_question = (TextView) rootView.findViewById(R.id.expert_question_textView_1);
            tv_time_limit_hear = (TextView) rootView.findViewById(R.id.time_limit_hear_textview_1);
            tv_head_time = (TextView) rootView.findViewById(R.id.head_time_textview_1);
            tv_time_limit_number = (TextView) rootView.findViewById(R.id.time_limit_number_textview_1);
            tv_expert_question_name = (TextView) rootView.findViewById(R.id.expert_question_name_textView_1);
            tv_expert_question_job = (TextView) rootView.findViewById(R.id.expert_question_job_textview_1);

        }
    }


    /**
     * 获取提问的payurl
     *
     * @param questionId 问题id
     */
    private void getOrderId(String questionId) {
        String url = null;
        url = String.format(UmiwiAPI.yiyuan_listener, questionId, "json");
        GetRequest<UmiwiBuyCreateOrderBeans> request = new GetRequest<UmiwiBuyCreateOrderBeans>(
                url, GsonParser.class,
                UmiwiBuyCreateOrderBeans.class,
                addQuestionOrderListener);
        request.go();
    }

    private AbstractRequest.Listener<UmiwiBuyCreateOrderBeans> addQuestionOrderListener = new AbstractRequest.Listener<UmiwiBuyCreateOrderBeans>() {
        @Override
        public void onResult(AbstractRequest<UmiwiBuyCreateOrderBeans> request, UmiwiBuyCreateOrderBeans umiwiBuyCreateOrderBeans) {
            String payurl = umiwiBuyCreateOrderBeans.getR().getPayurl();
            questionBuyDialog(payurl);
        }

        @Override
        public void onError(AbstractRequest<UmiwiBuyCreateOrderBeans> requet, int statusCode, String body) {

        }
    };


    /**
     * 跳转到购买界面
     *
     * @param payurl
     */
    public void questionBuyDialog(String payurl) {
        Intent i = new Intent(mActivity, UmiwiContainerActivity.class);
        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayingFragment.class);
        i.putExtra(PayingFragment.KEY_PAY_URL, payurl);
        mActivity.startActivity(i);
    }
}
