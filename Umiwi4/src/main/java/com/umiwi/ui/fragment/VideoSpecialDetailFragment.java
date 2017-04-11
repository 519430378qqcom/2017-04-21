package com.umiwi.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.HomeMainActivity;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.updateadapter.VideoSpecialDetailAdapter;
import com.umiwi.ui.beans.UmiwiBuyCreateOrderBeans;
import com.umiwi.ui.beans.updatebeans.VideoSpecialDetailBean;
import com.umiwi.ui.dialog.ShareDialog;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;
import com.umiwi.ui.fragment.pay.PayOrderDetailFragment;
import com.umiwi.ui.fragment.pay.PayTypeEvent;
import com.umiwi.ui.fragment.pay.PayingFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.StatisticsUrl;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.LoginUtil;
import com.umiwi.ui.view.NoScrollListview;
import com.umiwi.video.control.PlayerController;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

/**
 * Created by Administrator on 2017/4/6 0006.
 */

public class VideoSpecialDetailFragment extends BaseConstantFragment implements View.OnClickListener {
    @InjectView(R.id.iv_image)
    ImageView iv_image;
    @InjectView(R.id.iv_shared)
    ImageView iv_shared;
    @InjectView(R.id.record)
    ImageView record;
    @InjectView(R.id.iv_back)
    ImageView iv_back;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.shortcontent)
    TextView shortcontent;
    @InjectView(R.id.salenum)
    TextView salenum;
    @InjectView(R.id.description)
    TextView description;
    @InjectView(R.id.tv_changenum)
    TextView tv_changenum;
    @InjectView(R.id.lv_audio_item)
    NoScrollListview lv_audio_item;
    @InjectView(R.id.tv_priceold)
    TextView tv_priceold;
    @InjectView(R.id.tv_price)
    TextView tv_price;
    @InjectView(R.id.tv_buy)
    TextView tv_buy;
    @InjectView(R.id.yuedu)
    RelativeLayout yuedu;
    private Context mContext;
    private String detailurl;
    private ArrayList<VideoSpecialDetailBean.VideoSpecialRecord> mList = new ArrayList<>();
    private VideoSpecialDetailAdapter detailAdapter;
    private String id;
    private AnimationDrawable background;
    private VideoSpecialDetailBean.VideoSpecialShare share;
    private String price;
    private String sectionid;
    public static boolean isAlive = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_special_detail, null);
        mContext = getActivity();
        ButterKnife.inject(this,view);
        detailurl = getActivity().getIntent().getStringExtra("detailurl");
        UmiwiApplication.mainActivity.videoSpecaialDetailFragmentUrl = detailurl;
        detailAdapter = new VideoSpecialDetailAdapter(getActivity());
        detailAdapter.setData(mList);
        lv_audio_item.setAdapter(detailAdapter);
        lv_audio_item.setFocusable(false);
        getInfo();
        lv_audio_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoSpecialDetailBean.VideoSpecialRecord videoSpecialRecord = mList.get(position);
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
                intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, videoSpecialRecord.getDetailurl());
                getActivity().startActivity(intent);
            }
        });

        iv_back.setOnClickListener(this);
        iv_shared.setOnClickListener(this);
        record.setOnClickListener(this);
        yuedu.setOnClickListener(this);
        tv_buy.setOnClickListener(this);
        return view;

    }

    private void getInfo() {
//        String url = String.format(UmiwiAPI.UMIWI_VIDEO_SPECIAL_DETAIL, 1222);
        Log.e("TAG", "urlurl=" + detailurl);
        GetRequest<VideoSpecialDetailBean> request = new GetRequest<VideoSpecialDetailBean>(detailurl, GsonParser.class, VideoSpecialDetailBean.class, new AbstractRequest.Listener<VideoSpecialDetailBean>() {
            @Override
            public void onResult(AbstractRequest<VideoSpecialDetailBean> request, VideoSpecialDetailBean detailBean) {
                share = detailBean.getShare();
                id = detailBean.getId();
                sectionid = detailBean.getSectionid();
                Log.e("TAG", "id=" +id);
                title.setText(detailBean.getTitle());
                Glide.with(getActivity()).load(detailBean.getImage()).into(iv_image);
                tv_price.setText(detailBean.getPrice());
                price = detailBean.getPrice();
                if (detailBean.getRaw_price().equals(detailBean.getPrice())) {
                    tv_priceold.setVisibility(View.GONE);
                } else {
                    tv_priceold.setText(detailBean.getRaw_price());
                    tv_priceold.setVisibility(View.VISIBLE);
                    tv_priceold.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
                }
                if ("0".equals(detailBean.getSubtitle())) {
                    shortcontent.setVisibility(View.INVISIBLE);
                } else {
                    shortcontent.setText(detailBean.getSubtitle());
                }
                description.setText(detailBean.getIntroduce());
                if ("0".equals(detailBean.getSalenum())) {
                    salenum.setVisibility(View.INVISIBLE);
                } else {
                    salenum.setText(detailBean.getSalenum());
                }
                ArrayList<VideoSpecialDetailBean.VideoSpecialRecord> record = detailBean.getRecord();
                mList.clear();
                mList.addAll(record);
                detailAdapter.setData(mList);
                if (detailBean.isbuy()) {
                    yuedu.setVisibility(View.VISIBLE);
                } else {
                    yuedu.setVisibility(View.GONE);
                }
                tv_changenum.setText("总共" +detailBean.getTotal() +"条,已更新"+detailBean.getRecord().size() +"条视频");
            }

            @Override
            public void onError(AbstractRequest<VideoSpecialDetailBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.iv_shared:
                if(share == null) {
                    return;
                }
                PlayerController.getInstance().pause();
                ShareDialog.getInstance().showDialog(getActivity(),
                        share.getSharetitle(), share.getSharecontent(),
                        share.getShareurl(), share.getShareimg());
                break;

            case R.id.tv_buy :
//                getSubscriber(id);
                if (YoumiRoomUserManager.getInstance().isLogin()) {
                    Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayOrderDetailFragment.class);
                    intent.putExtra(PayOrderDetailFragment.KEY_ORDER_ID, sectionid);
                    intent.putExtra(PayOrderDetailFragment.KEY_ORDER_TYPE, PayTypeEvent.ZHUANTI);
                    intent.putExtra(PayOrderDetailFragment.KEY_SPM, String.format(StatisticsUrl.ORDER_JPZT_DETAIL, sectionid, price));
                    Log.e("TAG", "sectionid=" + sectionid + "," +String.format(StatisticsUrl.ORDER_JPZT_DETAIL, sectionid, price) );
                    startActivity(intent);
                } else {
                    LoginUtil.getInstance().showLoginView(getActivity());
                }
                break;
            case R.id.yuedu:
                lv_audio_item.setClickable(false);
                break;
            case R.id.record:
                if (UmiwiApplication.mainActivity.service != null) {
                    try {

                        if (UmiwiApplication.mainActivity.service.isPlaying() || UmiwiApplication.mainActivity.isPause) {
                            if (UmiwiApplication.mainActivity.herfUrl != null) {
                                Log.e("TAG", "UmiwiApplication.mainActivity.herfUrl=" + UmiwiApplication.mainActivity.herfUrl);
                                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VoiceDetailsFragment.class);
                                intent.putExtra(VoiceDetailsFragment.KEY_DETAILURL, UmiwiApplication.mainActivity.herfUrl);
                                getActivity().startActivity(intent);
                            }
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast toast = Toast.makeText(getActivity(), "没有正在播放的音频", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(UmiwiApplication.mainActivity != null) {
            if(UmiwiApplication.mainActivity.service != null) {
                background = (AnimationDrawable) record.getBackground();
                try {
                    if (UmiwiApplication.mainActivity.service.isPlaying()) {
                        background.start();
                    } else {
                        background.stop();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        isAlive = true;

    }

    /**
     * 获取订阅payurl
     */
    private void getSubscriber(String id) {
        if(id == null) {
            return;
        }
        String url = null;
        url = String.format(UmiwiAPI.CREATE_SUBSCRIBER_ORDERID, "json", id);
        GetRequest<UmiwiBuyCreateOrderBeans> request = new GetRequest<UmiwiBuyCreateOrderBeans>(
                url, GsonParser.class,
                UmiwiBuyCreateOrderBeans.class,
                subscriberListener);
        request.go();
    }
    private AbstractRequest.Listener<UmiwiBuyCreateOrderBeans> subscriberListener = new AbstractRequest.Listener<UmiwiBuyCreateOrderBeans>() {
        @Override
        public void onResult(AbstractRequest<UmiwiBuyCreateOrderBeans> request, UmiwiBuyCreateOrderBeans umiwiBuyCreateOrderBeans) {
            String payurl = umiwiBuyCreateOrderBeans.getR().getPayurl();
            subscriberBuyDialog(payurl);
            Log.e("TAG", "payurl==" + payurl);
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
        startActivity(i);
        getActivity().finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        isAlive = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
        if (HomeMainActivity.isForeground) {

        } else {
            Intent intent = new Intent(getActivity(),HomeMainActivity.class);
            startActivity(intent);
        }
    }
}
