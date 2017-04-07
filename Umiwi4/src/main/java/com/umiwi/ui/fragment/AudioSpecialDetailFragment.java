package com.umiwi.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.updateadapter.AudioDetailsAdapter;
import com.umiwi.ui.adapter.updateadapter.AudioSpecialListAdapter;
import com.umiwi.ui.beans.UmiwiBuyCreateOrderBeans;
import com.umiwi.ui.beans.updatebeans.AudioSpecialDetailBean;
import com.umiwi.ui.dialog.ShareDialog;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;
import com.umiwi.ui.fragment.pay.PayingFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
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

import static cn.youmi.framework.main.BaseApplication.getApplication;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class AudioSpecialDetailFragment extends BaseConstantFragment implements View.OnClickListener {
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
    NoScrollListview description;
    @InjectView(R.id.iv_sort)
    ImageView iv_sort;
    @InjectView(R.id.textView4)
    TextView textView4;
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
    private String typeId;
    private AudioSpecialDetailBean.RAudioSpecial details;
    private AudioSpecialListAdapter audioSpecialListAdapter;
    private ArrayList<AudioSpecialDetailBean.RAudioSpecial.LastRecordList> mList = new ArrayList<>();
    private String orderBy = "desc";
    private AnimationDrawable background;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_special_detail, null);
        ButterKnife.inject(this,view);
        mContext = getActivity();
        typeId = getActivity().getIntent().getStringExtra("typeId");
        Log.e("TAG", "typeId=" + typeId);
        audioSpecialListAdapter = new AudioSpecialListAdapter(getActivity());
        audioSpecialListAdapter.setData(mList);
        lv_audio_item.setAdapter(audioSpecialListAdapter);
        lv_audio_item.setFocusable(false);
        lv_audio_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean isaudition = mList.get(position).isaudition();
                if (isaudition) {
                    lv_audio_item.setClickable(true);
                    Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VoiceDetailsFragment.class);
                    intent.putExtra(VoiceDetailsFragment.KEY_DETAILURL, mList.get(position).getUrl());
                    getActivity().startActivity(intent);

                } else {
                    Toast.makeText(mContext, "请购买此专题", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getInfo();
        changeOrder();

        tv_buy.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_shared.setOnClickListener(this);
        record.setOnClickListener(this);
        yuedu.setOnClickListener(this);
        return view;

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
    private void changeOrder() {
        iv_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView4.getText().toString().equals("正序")) {
                    textView4.setText("倒序");
                    orderBy = "asc";
                    getInfo();
                    rotateImpl();
                    audioSpecialListAdapter.setData(mList);
                } else {
                    textView4.setText("正序");
                    orderBy = "desc";
                    getInfo();
                    audioSpecialListAdapter.setData(mList);
                    rotateImpl1();
                }
            }
        });
    }

    private void rotateImpl1() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.sort_anim_an);
        iv_sort.startAnimation(animation);
    }

    public void rotateImpl() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.sort_anim);
        iv_sort.startAnimation(animation);
    }

    /**
     * 获取数据
     */
    private void getInfo() {
        String url = String.format(UmiwiAPI.UMIWI_AUDIO_SPECIAL_DETAIL,typeId,orderBy);
        Log.e("TAG", "url=" + url);
        GetRequest<AudioSpecialDetailBean> request = new GetRequest<AudioSpecialDetailBean>(url, GsonParser.class, AudioSpecialDetailBean.class, new AbstractRequest.Listener<AudioSpecialDetailBean>() {
            @Override
            public void onResult(AbstractRequest<AudioSpecialDetailBean> request, AudioSpecialDetailBean detailBean) {
                details = detailBean.getR();
                ArrayList<AudioSpecialDetailBean.RAudioSpecial.RAudioSpecialContent> content = details.getContent();//详情
                description.setAdapter(new AudioDetailsAdapter(getActivity(),content));
                title.setText(details.getTitle());
                shortcontent.setText(details.getShortcontent());
                salenum.setText(details.getSalenum());
                tv_price.setText(details.getPrice());
                Glide.with(getActivity()).load(details.getImage()).into(iv_image);
                ArrayList<AudioSpecialDetailBean.RAudioSpecial.LastRecordList> last_record = details.getLast_record();
                mList.clear();
                mList.addAll(last_record);
                if (details.isbuy()) {
                    yuedu.setVisibility(View.GONE);
                } else {
                    yuedu.setVisibility(View.VISIBLE);
                }
                audioSpecialListAdapter.setData(mList);
                if (!TextUtils.isEmpty(details.getDiscount_price()) && details.getDiscount_price() != details.getPrice()) {
                    tv_priceold.setText("原价:" + details.getDiscount_price());
                    tv_priceold.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
                    tv_priceold.setVisibility(View.VISIBLE);
                } else {
                    tv_priceold.setVisibility(View.GONE);
                }
                tv_changenum.setText("总共" +details.getTotalnum() +"条,已更新"+last_record.size() +"条音频");
//
            }

            @Override
            public void onError(AbstractRequest<AudioSpecialDetailBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    @Override
    public void onResume() {
        super.onResume();
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

    @Override
    public void onClick(View v) {
       switch (v.getId()) {
           case R.id.iv_back :
                getActivity().finish();
               break;
           case R.id.iv_shared:
                if(details == null) {
                    return;
                }
               PlayerController.getInstance().pause();
               ShareDialog.getInstance().showDialog(getActivity(),
                       details.getSharetitle(), details.getSharecontent(),
                       details.getShareurl(), details.getShareimg());
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
           case R.id.yuedu:
               lv_audio_item.setClickable(false);
               break;
           case R.id.tv_buy:
               lv_audio_item.setClickable(false);
               if (!YoumiRoomUserManager.getInstance().isLogin()) {
                   LoginUtil.getInstance().showLoginView(getApplication());
                   return;
               }
               getSubscriber(typeId);
               break;
       }

    }
}
