package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.HomeMainActivity;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.ColumnAttentionAdapter;
import com.umiwi.ui.adapter.ColumnDetailsAdapter;
import com.umiwi.ui.adapter.ColumnRecordAdapter;
import com.umiwi.ui.beans.UmiwiBuyCreateOrderBeans;
import com.umiwi.ui.beans.updatebeans.AudioSpecialDetailsBean;
import com.umiwi.ui.dialog.ShareDialog;
import com.umiwi.ui.fragment.home.alreadyshopping.LogicalThinkingFragment;
import com.umiwi.ui.fragment.pay.PayingFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.LoginUtil;
import com.umiwi.ui.view.NoScrollListview;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

import static cn.youmi.framework.main.BaseApplication.getApplication;

/**
 * Created by Administrator on 2017/3/6.
 */

//专栏详情
public class ColumnDetailsFragment extends BaseConstantFragment {

    private String columnurl;
    private TextView targetuser;
    private TextView title;
    private TextView priceinfo;
    private TextView shortcontent;
    private TextView salenum;
    private TextView tv_name;
    private TextView tv_title;
    private TextView tv_prize;
    private TextView tv_free_read;

    private ImageView iv_image;
    private ImageView iv_back;
    private ImageView iv_shared;
    private ImageView record;
    private NoScrollListview description;
    private NoScrollListview attention_listview;
    private NoScrollListview last_record;
//    private ColumnDetailsBean columnDetailsBean;
    private AudioSpecialDetailsBean.RAudioSpecialDetails details;
    private AnimationDrawable background;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_column_layout, null);

        columnurl = getActivity().getIntent().getStringExtra("columnurl");
        Log.e("data", columnurl);

        initView(view);
        getData();

        return view;
    }

    private void initView(View view) {

        targetuser = (TextView) view.findViewById(R.id.targetuser);
        title = (TextView) view.findViewById(R.id.title);
        priceinfo = (TextView) view.findViewById(R.id.priceinfo);
        shortcontent = (TextView) view.findViewById(R.id.shortcontent);
        salenum = (TextView) view.findViewById(R.id.salenum);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_prize = (TextView) view.findViewById(R.id.tv_prize);
        tv_free_read = (TextView) view.findViewById(R.id.tv_free_read);
        tv_free_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(), "免费试读", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LogicalThinkingFragment.class);
                intent.putExtra("id",details.getId());
                intent.putExtra("title",details.getTitle());
                startActivity(intent);
            }
        });

        iv_image = (ImageView) view.findViewById(R.id.iv_image);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        record = (ImageView) view.findViewById(R.id.record);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        iv_shared = (ImageView) view.findViewById(R.id.iv_shared);
        iv_shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareDialog.getInstance().showDialog(getActivity(), details.getSharetitle(),
                        details.getSharecontent(),details.getShareurl(), details.getShareimg());
            }
        });
        tv_title = (TextView) view.findViewById(R.id.tv_title);

        description = (NoScrollListview) view. findViewById(R.id.description);
        attention_listview = (NoScrollListview) view. findViewById(R.id.attention_listview);
        last_record = (NoScrollListview) view. findViewById(R.id.last_record);
        initMediaPlay();
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
    }

    /**
     * 播放按钮
     */
    private void initMediaPlay() {
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
    }

    private void getData() {
        GetRequest<AudioSpecialDetailsBean> request = new GetRequest<AudioSpecialDetailsBean>(columnurl, GsonParser.class, AudioSpecialDetailsBean.class, new AbstractRequest.Listener<AudioSpecialDetailsBean>() {
            @Override
            public void onResult(AbstractRequest<AudioSpecialDetailsBean> request, AudioSpecialDetailsBean audioSpecialDetailsBean) {
                details = audioSpecialDetailsBean.getR();
                ArrayList<AudioSpecialDetailsBean.RAudioSpecialDetails.ContentWord> content = details.getContent();//详情简介
                description.setAdapter(new ColumnDetailsAdapter(getActivity(),content));
                targetuser.setText(details.getTargetuser());
                ArrayList<AudioSpecialDetailsBean.RAudioSpecialDetails.AttentionWord> attention = details.getAttention();
                attention_listview.setAdapter(new ColumnAttentionAdapter(getActivity(), details.getAttention()));
                last_record.setAdapter(new ColumnRecordAdapter(getActivity(), details.getLast_record()));
                title.setText(details.getTitle());
                priceinfo.setText(details.getPriceinfo());
                shortcontent.setText(details.getShortcontent());

                salenum.setText(details.getSalenum());

                Glide.with(getActivity()).load(details.getImage()).into(iv_image);
                tv_name.setText(details.getTutor_name());
                tv_title.setText(details.getTutor_title());

                Log.e("TAG", "columnDetailsBean.isIsbuy=" + details.isbuy());
                if (details.isbuy()){
                    tv_free_read.setText("进入专栏");
                    tv_prize.setText("已订阅");
                    tv_prize.setEnabled(false);
                }else {
                    tv_free_read.setText("免费阅读");
                    tv_prize.setEnabled(true);
                    tv_prize.setText(String.format("订阅:  %s元/年", details.getPrice()));
                }
                tv_prize.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!YoumiRoomUserManager.getInstance().isLogin()) {
                            LoginUtil.getInstance().showLoginView(getApplication());
                            return;
                        }
                        getSubscriber(details.getId());
                    }
                });

            }

            @Override
            public void onError(AbstractRequest<AudioSpecialDetailsBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    /**
     * 获取订阅payurl
     */
    private void getSubscriber(String id) {
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
    public void onDestroy() {
        super.onDestroy();
        if (HomeMainActivity.isForeground) {

        } else {
            Intent intent = new Intent(getActivity(),HomeMainActivity .class);
            startActivity(intent);
        }

    }
}
