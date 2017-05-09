package com.umiwi.ui.fragment.audiolive;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.AuthorChatRoomActivity;
import com.umiwi.ui.activity.ChatRecordActivity;
import com.umiwi.ui.activity.LiveChatRoomActivity;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.updateadapter.AudioLiveDetailsAdapter;
import com.umiwi.ui.beans.UmiwiBuyCreateOrderBeans;
import com.umiwi.ui.beans.updatebeans.AudioLiveDetailsBean;
import com.umiwi.ui.beans.updatebeans.JewelBuyAudioBena;
import com.umiwi.ui.dialog.ShareDialog;
import com.umiwi.ui.fragment.pay.PayingFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.LoginUtil;
import com.umiwi.ui.view.NoScrollListview;
import com.umiwi.ui.view.ScrollChangeScrollView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class AudioLiveDetailsFragment extends BaseConstantFragment {

    @InjectView(R.id.iv_image)
    ImageView ivImage;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_livestatus)
    TextView tvLivestatus;
    @InjectView(R.id.tv_starttime)
    TextView tvStarttime;
    @InjectView(R.id.tv_takepart)
    TextView tvTakepart;
    @InjectView(R.id.description)
    NoScrollListview description;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tab_title)
    TextView tabTitle;
    @InjectView(R.id.iv_shared)
    ImageView ivShared;
    @InjectView(R.id.record)
    ImageView record;
    @InjectView(R.id.tv_gotoliveroom)
    TextView tvGotoliveroom;
    @InjectView(R.id.rl_bottom_back)
    RelativeLayout rlBottomBack;
    @InjectView(R.id.scrollview)
    ScrollChangeScrollView scrollview;
    @InjectView(R.id.rl_background)
    View rl_background;
    public static final String LIVEID = "liveId";
    private String liveId;//id
    private AudioLiveDetailsBean.RAudioLiveDetails.AudioLiveDetailsRecord detailsRecord;//详情
    private AudioLiveDetailsBean.RAudioLiveDetails.AudioLiveDetailsShare share;//分享
    private int height;
    private String payurl;
    public static boolean isAutio;//是否进入过聊天室

    public static boolean isPause = false;
    private boolean isAuthor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audiolive_details, null);
        ButterKnife.inject(this, view);

        liveId = getActivity().getIntent().getStringExtra(LIVEID);
        isAuthor = getActivity().getIntent().getBooleanExtra("isAuthor", false);
        Log.e("TAG", "liveId=" + liveId);
        record.setVisibility(View.GONE);
        description.setFocusable(false);
//        getInfo();
        initScroll();
        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        isPause = true;
        Log.e("TAG", "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        isPause = false;
        Log.e("TAG", "onStop()");
    }

    @Override
    public void onResume() {
        super.onResume();
        getInfo();
    }

    private void initScroll() {
        ViewTreeObserver vto = ivImage.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                height = ivImage.getHeight();
                scrollview.setScrollViewListener(mScrollViewListener);
            }
        });
    }

    private ScrollChangeScrollView.ScrollViewListener mScrollViewListener = new ScrollChangeScrollView.ScrollViewListener() {
        @Override
        public void onScrollChanged(ScrollChangeScrollView scrollView, int x, int y, int oldx, int oldy) {
            if (y <= 0) {
                tabTitle.setVisibility(View.INVISIBLE);
//                iv_back.setImageResource(R.drawable.back_white);
//                iv_shared.setImageResource(R.drawable.share_img);
//                record.setBackgroundResource(R.drawable.anim_music_white);
                //设置文字背景颜色，白色
                rl_background.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));//AGB由相关工具获得，或者美工提供
                //设置文字颜色，黑色
//                tab_title.setTextColor(Color.argb((int) 0, 255, 255, 255));
//                Log.e("111", "y <= 0");
            } else if (y > 0 && y <= height) {
                float scale = (float) y / height;
                float alpha = (255 * scale);
                //只是layout背景透明(仿知乎滑动效果)白色透明
                rl_background.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                //设置文字颜色，黑色，加透明度
                if (y > height / 2 && y <= height) {
//                    iv_back.setImageResource(R.drawable.back_black);
//                    iv_shared.setImageResource(R.drawable.share_black);
//                    record.setBackgroundResource(R.drawable.anim_music_black);
                    tabTitle.setVisibility(View.VISIBLE);
                } else {
//                    iv_back.setImageResource(R.drawable.back_white);
//                    iv_shared.setImageResource(R.drawable.share_img);
//                    record.setBackgroundResource(R.drawable.anim_music_white);
                    tabTitle.setVisibility(View.INVISIBLE);
                }
                tabTitle.setTextColor(Color.argb((int) alpha, 0, 0, 0));
//                Log.e("111", "y > 0 && y <= imageHeight");
            } else {
//                iv_back.setImageResource(R.drawable.back_black);
//                iv_shared.setImageResource(R.drawable.share_black);
//                record.setBackgroundResource(R.drawable.anim_music_black);
                //白色不透明
                tabTitle.setVisibility(View.VISIBLE);
                rl_background.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                //设置文字颜色
                //黑色
                tabTitle.setTextColor(Color.argb((int) 255, 0, 0, 0));
//                Log.e("111", "else");
            }
        }
    };

    //获取详情数据
    private void getInfo() {
        String url = String.format(UmiwiAPI.LIVE_DETAILS, liveId);
        Log.e("TAG", "直播详情url=" + url);
        GetRequest<AudioLiveDetailsBean> request = new GetRequest<AudioLiveDetailsBean>(url, GsonParser.class, AudioLiveDetailsBean.class, getinfoListener);
        request.go();
    }


    private AbstractRequest.Listener<AudioLiveDetailsBean> getinfoListener = new AbstractRequest.Listener<AudioLiveDetailsBean>() {
        @Override
        public void onResult(AbstractRequest<AudioLiveDetailsBean> request, AudioLiveDetailsBean audioLiveDetailsBean) {

            detailsRecord = audioLiveDetailsBean.getR().getRecord();
            share = audioLiveDetailsBean.getR().getShare();

            Glide.with(getActivity()).load(detailsRecord.getImage()).into(ivImage);
            tvTitle.setText(detailsRecord.getTitle());
            tabTitle.setText(detailsRecord.getTitle());

            ArrayList<AudioLiveDetailsBean.RAudioLiveDetails.AudioLiveDetailsRecord.AudioDetailsDescription> content = detailsRecord.getDescription();
            description.setAdapter(new AudioLiveDetailsAdapter(getActivity(), content));
            tvLivestatus.setText(detailsRecord.getStatus());
            //直播状态
            if ("已结束".equals(detailsRecord.getStatus())) {
                tvLivestatus.setBackgroundResource(R.drawable.textview_fillet_bg);
                tvLivestatus.setTextColor(Color.GRAY);
            }
            if ("未开始".equals(detailsRecord.getStatus())) {
                tvLivestatus.setVisibility(View.GONE);
                tvStarttime.setTextColor(getResources().getColor(R.color.main_color));
                tvStarttime.setBackgroundResource(R.drawable.textview_orange_bg);
            }
            tvTakepart.setText(detailsRecord.getPartakenum() + "参与");
            //开始时间
            tvStarttime.setText(detailsRecord.getLive_time());

            //免费状态
            if (detailsRecord.isfree()) {
                //底部参与价格
                tvGotoliveroom.setText("立即参与");
                rlBottomBack.setBackgroundColor(getResources().getColor(R.color.green_color));
            } else {
                if (detailsRecord.isbuy()) {
                    tvGotoliveroom.setText("立即参与");
                    rlBottomBack.setBackgroundColor(getResources().getColor(R.color.green_color));
                } else {
                    rlBottomBack.setBackgroundColor(getResources().getColor(R.color.main_color));
                    tvGotoliveroom.setText("立即参与(" + detailsRecord.getPrice() + ")");
                }
            }


            //判断是否购买
            if (liveId.equals(PayingFragment.payId) && detailsRecord.isbuy() && !isAutio) {
                isAutio = true;
                if ("已结束".equals(detailsRecord.getStatus())) {
                    Intent intent = new Intent(getActivity(), ChatRecordActivity.class);
                    intent.putExtra(LiveDetailsFragment.DETAILS_ID, detailsRecord.getId());
                    intent.putExtra(ChatRecordActivity.ROOM_ID, detailsRecord.getRoomid());
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LiveChatRoomActivity.class);
                    intent.putExtra(LiveDetailsFragment.DETAILS_ID, detailsRecord.getId());
                    intent.putExtra(LiveChatRoomActivity.ROOM_ID, detailsRecord.getRoomid());
                    startActivity(intent);
                    getActivity().finish();
                }

            }
        }

        @Override
        public void onError(AbstractRequest<AudioLiveDetailsBean> requet, int statusCode, String body) {
            Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_LONG).show();
        }
    };

    //
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    @OnClick({R.id.iv_back, R.id.iv_shared, R.id.tv_gotoliveroom, R.id.rl_bottom_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.iv_shared:
                ShareDialog.getInstance().showDialog(getActivity(), share.getSharetitle(),
                        share.getSharecontent(), share.getShareurl(), share.getShareimg());
                break;
            case R.id.tv_gotoliveroom:
                if (!YoumiRoomUserManager.getInstance().isLogin()) {
                    showLogin();
                } else {
                    //免费
                    if (detailsRecord.isfree()) {
                        if (detailsRecord.isbuy()) {
                            if ("已结束".equals(detailsRecord.getStatus())) {
                                Intent intent = new Intent(getActivity(), ChatRecordActivity.class);
                                intent.putExtra(LiveDetailsFragment.DETAILS_ID, detailsRecord.getId());
                                intent.putExtra(ChatRecordActivity.ROOM_ID, detailsRecord.getRoomid());
                                getActivity().startActivity(intent);
                            } else {
                                if (isAuthor) {
                                    Intent intent = new Intent(getActivity(), AuthorChatRoomActivity.class);
                                    intent.putExtra(LiveDetailsFragment.DETAILS_ID, detailsRecord.getId());
                                    intent.putExtra(LiveChatRoomActivity.ROOM_ID, detailsRecord.getRoomid());
                                    getActivity().startActivity(intent);
                                } else {
                                    Intent intent = new Intent(getActivity(), LiveChatRoomActivity.class);
                                    intent.putExtra(LiveDetailsFragment.DETAILS_ID, detailsRecord.getId());
                                    intent.putExtra(LiveChatRoomActivity.ROOM_ID, detailsRecord.getRoomid());
                                    getActivity().startActivity(intent);
                                }
                            }
                        } else {
                            //没有购买 钻石购买
                            zuanShiBuy(detailsRecord.getId());
                        }
                    } else {
                        //不免费
                        if (detailsRecord.isbuy()) {
                            if ("已结束".equals(detailsRecord.getStatus())) {
                                Intent intent = new Intent(getActivity(), ChatRecordActivity.class);
                                intent.putExtra(LiveDetailsFragment.DETAILS_ID, detailsRecord.getId());
                                intent.putExtra(ChatRecordActivity.ROOM_ID, detailsRecord.getRoomid());
                                getActivity().startActivity(intent);
                            } else {
                                if (isAuthor) {
                                    Intent intent = new Intent(getActivity(), AuthorChatRoomActivity.class);
                                    intent.putExtra(LiveDetailsFragment.DETAILS_ID, detailsRecord.getId());
                                    intent.putExtra(LiveChatRoomActivity.ROOM_ID, detailsRecord.getRoomid());
                                    getActivity().startActivity(intent);
                                } else {
                                    Intent intent = new Intent(getActivity(), LiveChatRoomActivity.class);
                                    intent.putExtra(LiveDetailsFragment.DETAILS_ID, detailsRecord.getId());
                                    intent.putExtra(LiveChatRoomActivity.ROOM_ID, detailsRecord.getRoomid());
                                    getActivity().startActivity(intent);
                                }
                            }
                        } else {
                            goToBuy(detailsRecord.getId());
                        }
                    }

                }

                break;
            case R.id.rl_bottom_back:

                break;
        }
    }

    private void zuanShiBuy(String id) {
        String url = String.format(UmiwiAPI.UMIWI_AUDIOLIVE_FREE_ZUANSI, id);
        Log.e("TAG", "url====" + url);
        GetRequest<JewelBuyAudioBena> request = new GetRequest<JewelBuyAudioBena>(
                url, GsonParser.class,
                JewelBuyAudioBena.class,
                freeBuyListener);
        request.go();
    }
    private AbstractRequest.Listener<JewelBuyAudioBena> freeBuyListener= new AbstractRequest.Listener<JewelBuyAudioBena>() {
        @Override
        public void onResult(AbstractRequest<JewelBuyAudioBena> request, JewelBuyAudioBena jewelBuyAudioBena) {
//            payurl = umiwiBuyCreateOrderBeans.getR().getPayurl();
//            subscriberBuyDialog(payurl);
            if(jewelBuyAudioBena.getR().getId() != null) {
                if ("已结束".equals(detailsRecord.getStatus())) {
                    Intent intent = new Intent(getActivity(), ChatRecordActivity.class);
                    intent.putExtra(LiveDetailsFragment.DETAILS_ID, detailsRecord.getId());
                    intent.putExtra(ChatRecordActivity.ROOM_ID, detailsRecord.getRoomid());
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LiveChatRoomActivity.class);
                    intent.putExtra(LiveDetailsFragment.DETAILS_ID, detailsRecord.getId());
                    intent.putExtra(LiveChatRoomActivity.ROOM_ID, detailsRecord.getRoomid());
                    startActivity(intent);
                    getActivity().finish();
                }
            }


        }

        @Override
        public void onError(AbstractRequest<JewelBuyAudioBena> requet, int statusCode, String body) {

        }
    };
    //跳转登陆
    private void showLogin() {
        LoginUtil.getInstance().showLoginView(getActivity());
    }

    //获取购买url
    private void goToBuy(String id) {
        String url = String.format(UmiwiAPI.UMIWI_AUDIOLIVE_DETAILS_BUY, id);
        GetRequest<UmiwiBuyCreateOrderBeans> request = new GetRequest<UmiwiBuyCreateOrderBeans>(
                url, GsonParser.class,
                UmiwiBuyCreateOrderBeans.class,
                buyListener);
        request.go();
    }

    private AbstractRequest.Listener<UmiwiBuyCreateOrderBeans> buyListener = new AbstractRequest.Listener<UmiwiBuyCreateOrderBeans>() {
        @Override
        public void onResult(AbstractRequest<UmiwiBuyCreateOrderBeans> request, UmiwiBuyCreateOrderBeans umiwiBuyCreateOrderBeans) {
            payurl = umiwiBuyCreateOrderBeans.getR().getPayurl();
            subscriberBuyDialog(payurl);
//            Log.e("TAG", "buypayurl==" + payurl);
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
    public void subscriberBuyDialog(String payurl) {
        Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayingFragment.class);
        i.putExtra(PayingFragment.KEY_PAY_URL, payurl);
        i.putExtra(PayingFragment.PAY_ID, liveId);
        startActivity(i);
//        getActivity().startActivityForResult(i,1000);

    }

}
