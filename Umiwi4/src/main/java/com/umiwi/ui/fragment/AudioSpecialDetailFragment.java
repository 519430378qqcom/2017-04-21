package com.umiwi.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.AudioSpecialDetailBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.view.NoScrollListview;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class AudioSpecialDetailFragment extends BaseConstantFragment {
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
    ListView lv_audio_item;
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
    private AudioSpecialDetailBean.RAudioSpecial details;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_special_detail, null);
        ButterKnife.inject(this,view);
        mContext = getActivity();
        detailurl = getActivity().getIntent().getStringExtra("detailurl");
        getInfo();
        return view;

    }

    /**
     * 获取数据
     */
    private void getInfo() {
        GetRequest<AudioSpecialDetailBean> request = new GetRequest<AudioSpecialDetailBean>(detailurl, GsonParser.class, AudioSpecialDetailBean.class, new AbstractRequest.Listener<AudioSpecialDetailBean>() {
            @Override
            public void onResult(AbstractRequest<AudioSpecialDetailBean> request, AudioSpecialDetailBean detailBean) {
                details = detailBean.getR();
                ArrayList<AudioSpecialDetailBean.RAudioSpecial.RAudioSpecialContent> content = details.getContent();//详情
                title.setText(details.getTitle());
                shortcontent.setText(details.getShortcontent());
                salenum.setText(details.getSalenum());
                Glide.with(getActivity()).load(details.getImage()).into(iv_image);
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
}
