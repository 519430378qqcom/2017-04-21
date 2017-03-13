package com.umiwi.ui.fragment.home.alreadyshopping;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.beans.updatebeans.AlreadyAskBean;
import com.umiwi.ui.beans.updatebeans.DelayAnswerVoiceBean;
import com.umiwi.ui.beans.updatebeans.QuestionBean;
import com.umiwi.ui.beans.updatebeans.ZanBean;
import com.umiwi.ui.dialog.updatedialog.NewShareDialog;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.AskQuestionFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.video.recorder.MediaManager;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.view.CircleImageView;

/**
 * Created by Administrator on 2017/3/12.
 */

public class AnswerDetailsFragment extends BaseConstantFragment implements View.OnClickListener {

    @InjectView(R.id.back)
    ImageView back;
//    @InjectView(R.id.share)
//    ImageView share;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.tavatar)
    CircleImageView tavatar;
    @InjectView(R.id.buttontag)
    TextView buttontag;
    @InjectView(R.id.listennum)
    TextView listennum;
    @InjectView(R.id.goodnum)
    TextView goodnum;
    @InjectView(R.id.playtime)
    TextView playtime;
    @InjectView(R.id.header)
    CircleImageView header;
    @InjectView(R.id.name)
    TextView name;
    @InjectView(R.id.duty)
    TextView duty;
    @InjectView(R.id.obligation)
    TextView obligation;
    @InjectView(R.id.all_q_a)
    TextView allQA;
    @InjectView(R.id.answertime)
    TextView answertime;
    private String uid;
    private String id;
    private QuestionBean.RQuestionr.Share shareContent;
    private String playsource;
    private boolean goodstate;
    private String url;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragmeny_answer_details, null);

        ButterKnife.inject(this, inflate);
        getIntentInfos();
        initOnClickLintenrs();
        getInfos();
        return inflate;
    }

    private void getInfos() {
        String url = UmiwiAPI.QUESTION_DES + "?id=" + id;

        GetRequest<QuestionBean> request = new GetRequest<QuestionBean>(
                url, GsonParser.class,
                QuestionBean.class,
                new AbstractRequest.Listener<QuestionBean>() {
                    @Override
                    public void onResult(AbstractRequest<QuestionBean> request, QuestionBean questionBean) {
                        QuestionBean.RQuestionr r = questionBean.getR();
                        shareContent = r.getShare();
                        playsource = r.getPlaysource();
                        String listentype = questionBean.getR().getListentype();
                        if (listentype.equals("1")) {
                            buttontag.setText(r.getButtontag());
                            buttontag.setBackgroundResource(R.drawable.changer);
                        } else {
                            buttontag.setText(r.getButtontag());
                            buttontag.setBackgroundResource(R.drawable.time_limit_hear);
                        }
                        goodnum.setText(r.getGoodnum());
                        answertime.setText(r.getAnswertime());
                        listennum.setText(r.getListennum());
                        name.setText(r.getTutor_name());
                        duty.setText(r.getTutor_title());
                        obligation.setText(r.getTutor_description());
                        ImageLoader imageLoader = new ImageLoader(getActivity());
                        imageLoader.loadImage(r.getTavatar(), header);
                        imageLoader.loadImage(r.getTutor_avatar(), tavatar);
                        goodstate = r.getGoodstate();
                        if (goodstate){ //点赞了
                            Drawable img = getActivity().getResources().getDrawable(R.drawable.zan_main);
                            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
                            goodnum.setCompoundDrawables(img,null,null,null);
                        }else{
                            Drawable img = getActivity().getResources().getDrawable(R.drawable.zan_img);
                            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
                            goodnum.setCompoundDrawables(img,null,null,null);
                        }
                    }

                    @Override
                    public void onError(AbstractRequest<QuestionBean> requet, int statusCode, String body) {
                        Log.e("onResult", "请求失败" + body);

                    }
                });
        request.go();
    }

    private void initOnClickLintenrs() {
        back.setOnClickListener(this);
//        share.setOnClickListener(this);
        allQA.setOnClickListener(this);
        buttontag.setOnClickListener(this);
        goodnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (goodstate == true){ //取消点赞
                    url = String.format(UmiwiAPI.ZAN,id,2);

                }else{  //点赞
                    url = String.format(UmiwiAPI.ZAN,id,1);
                }

                cn.youmi.framework.http.GetRequest<ZanBean> request = new cn.youmi.framework.http.GetRequest<ZanBean>(
                        url, GsonParser.class,
                        ZanBean.class, new AbstractRequest.Listener<ZanBean>() {
                    @Override
                    public void onResult(AbstractRequest<ZanBean> request, ZanBean zanBean) {
                        getInfos();
                    }

                    @Override
                    public void onError(AbstractRequest<ZanBean> requet, int statusCode, String body) {
                        Log.e("onresult","失败了");

                    }
                });
                request.go();

            }
        });
    }

    private void getIntentInfos() {
        String titleinfo = getActivity().getIntent().getStringExtra("title");
        String buttontaginfo = getActivity().getIntent().getStringExtra("buttontag");
        String tavatarinfo = getActivity().getIntent().getStringExtra("tavatar");
        String playtimeinfo = getActivity().getIntent().getStringExtra("playtime");
        String answertimeinfo = getActivity().getIntent().getStringExtra("answertime");
        String goodnuminfo = getActivity().getIntent().getStringExtra("goodnum");
        String listennuminfo = getActivity().getIntent().getStringExtra("listennum");
        uid = getActivity().getIntent().getStringExtra("uid");
        id = getActivity().getIntent().getStringExtra("id");

        title.setText(titleinfo);
        playtime.setText(playtimeinfo);
        listennum.setText(listennuminfo);
        goodnum.setText(goodnuminfo);
        answertime.setText(answertimeinfo);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.share:
//                if (shareContent.getSharecontent() != null && shareContent.getShareimg() != null && shareContent.getSharetitle() != null && shareContent.getShareurl() != null) {
//                    NewShareDialog.getInstance().showDialog(getActivity(), shareContent.getSharetitle(),
//                            shareContent.getSharecontent(), shareContent.getShareurl(), shareContent.getShareimg());
//                }
                break;
            case R.id.all_q_a:
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, AskQuestionFragment.class);
                intent.putExtra("uid", uid);
                startActivity(intent);

                break;
            case R.id.buttontag:
                getsorceInfos();
                break;
        }
    }

    private void getsorceInfos() {
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
                        if (MediaManager.mediaPlayer.isPlaying()) {
                            buttontag.setText("正在播放");

                        }
                    }

                    @Override
                    public void onError(AbstractRequest<DelayAnswerVoiceBean> requet, int statusCode, String body) {

                    }
                });
        request.go();
    }

}
