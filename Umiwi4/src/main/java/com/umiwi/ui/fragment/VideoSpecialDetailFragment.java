package com.umiwi.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.updateadapter.VideoSpecialDetailAdapter;
import com.umiwi.ui.beans.UmiwiBuyCreateOrderBeans;
import com.umiwi.ui.beans.updatebeans.VideoSpecialDetailBean;
import com.umiwi.ui.fragment.pay.PayingFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.view.NoScrollListview;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

import static com.umiwi.video.services.VoiceService.url;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_special_detail, null);
        mContext = getActivity();
        ButterKnife.inject(this,view);
        detailurl = getActivity().getIntent().getStringExtra("detailurl");
        detailAdapter = new VideoSpecialDetailAdapter(getActivity());
//        detailAdapter.setData(mList);
//        lv_audio_item.setAdapter(detailAdapter);
        getInfo();



        iv_back.setOnClickListener(this);
        iv_shared.setOnClickListener(this);
        record.setOnClickListener(this);
        yuedu.setOnClickListener(this);
        return view;

    }

    private void getInfo() {
//        String url = String.format(UmiwiAPI.UMIWI_VIDEO_SPECIAL_DETAIL, 1222);
        Log.e("TAG", "urlurl=" + url);
        GetRequest<VideoSpecialDetailBean> request = new GetRequest<VideoSpecialDetailBean>(detailurl, GsonParser.class, VideoSpecialDetailBean.class, new AbstractRequest.Listener<VideoSpecialDetailBean>() {
            @Override
            public void onResult(AbstractRequest<VideoSpecialDetailBean> request, VideoSpecialDetailBean detailBean) {
                id = detailBean.getId();
                title.setText(detailBean.getTitle());
                Glide.with(getActivity()).load(detailBean.getImage()).into(iv_image);
                tv_price.setText(detailBean.getPrice());
                if (detailBean.getRaw_price() == detailBean.getPrice()) {
                    tv_priceold.setVisibility(View.GONE);
                } else {
                    tv_priceold.setText(detailBean.getRaw_price());
                    tv_priceold.setVisibility(View.VISIBLE);
                    tv_priceold.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
                }
                shortcontent.setText(detailBean.getSubtitle());
                salenum.setText(detailBean.getSalenum());
                ArrayList<VideoSpecialDetailBean.VideoSpecialRecord> record = detailBean.getRecord();
                mList.clear();
                mList.addAll(record);
//                detailAdapter.setData(mList);
                if (detailBean.isbuy()) {
                    yuedu.setVisibility(View.GONE);
                } else {
                    yuedu.setVisibility(View.VISIBLE);
                }
                tv_changenum.setText("总共" +detailBean.getTotal() +"条,已更新"+detailBean.getRecord().size() +"条音频");
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
            case R.id.tv_buy :
                getSubscriber(id);
                break;
        }
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
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }
}
