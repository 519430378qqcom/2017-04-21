package com.umiwi.ui.fragment.audiolive;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.umiwi.ui.adapter.updateadapter.AudioLiveDetailsAdapter;
import com.umiwi.ui.beans.updatebeans.AudioLiveDetailsBean;
import com.umiwi.ui.dialog.ShareDialog;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.view.NoScrollListview;
import com.umiwi.ui.view.ScrollChangeScrollView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

import static com.umiwi.ui.main.YoumiConfiguration.context;

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



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audiolive_details, null);
        ButterKnife.inject(this, view);

        liveId = getActivity().getIntent().getStringExtra(LIVEID);

        record.setVisibility(View.GONE);
        description.setFocusable(false);
        getInfo();
        initScroll();
        return view;
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
        GetRequest<AudioLiveDetailsBean> request = new GetRequest<AudioLiveDetailsBean>(url, GsonParser.class, AudioLiveDetailsBean.class,getinfoListener);
        request.go();
    }
    private AbstractRequest.Listener<AudioLiveDetailsBean> getinfoListener = new AbstractRequest.Listener<AudioLiveDetailsBean>() {
        @Override
        public void onResult(AbstractRequest<AudioLiveDetailsBean> request, AudioLiveDetailsBean audioLiveDetailsBean) {

            if(detailsRecord == null) {
                detailsRecord = audioLiveDetailsBean.getR().getRecord();
                share = audioLiveDetailsBean.getR().getShare();

                Glide.with(context).load(detailsRecord.getImage()).into(ivImage);
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
                //底部参与价格
                if (detailsRecord.isbuy()) {
                    tvGotoliveroom.setText(detailsRecord.getPrice());
                } else {
                    tvGotoliveroom.setText("立即参与(" + detailsRecord.getPrice() + ")");
                }
                //开始时间
                tvStarttime.setText(detailsRecord.getLive_time());
            }
        }

        @Override
        public void onError(AbstractRequest<AudioLiveDetailsBean> requet, int statusCode, String body) {
            Toast.makeText(getActivity(),"ERROR",Toast.LENGTH_LONG).show();
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

                break;
            case R.id.rl_bottom_back:

                break;
        }
    }
}
