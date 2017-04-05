package com.umiwi.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.umiwi.ui.beans.updatebeans.AudioSpecialDetailBean;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;
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
    private AudioSpecialDetailBean.RAudioSpecial details;
    private AudioSpecialListAdapter audioSpecialListAdapter;
    private ArrayList<AudioSpecialDetailBean.RAudioSpecial.LastRecordList> mList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_special_detail, null);
        ButterKnife.inject(this,view);
        mContext = getActivity();
        detailurl = getActivity().getIntent().getStringExtra("detailurl");
        Log.e("TAG", "detailurl=" + detailurl);
        audioSpecialListAdapter = new AudioSpecialListAdapter(getActivity());
        audioSpecialListAdapter.setData(mList);
        lv_audio_item.setAdapter(audioSpecialListAdapter);
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
                    Toast.makeText(mContext, "请先购买", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getInfo();
        changeOrder();
        return view;

    }

    private void changeOrder() {
        iv_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView4.getText().toString().equals("正序")) {
                    textView4.setText("倒序");
                    rotateImpl();
                    audioSpecialListAdapter.setData(mList);
                } else {
                    textView4.setText("正序");
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
        GetRequest<AudioSpecialDetailBean> request = new GetRequest<AudioSpecialDetailBean>(detailurl, GsonParser.class, AudioSpecialDetailBean.class, new AbstractRequest.Listener<AudioSpecialDetailBean>() {
            @Override
            public void onResult(AbstractRequest<AudioSpecialDetailBean> request, AudioSpecialDetailBean detailBean) {
                details = detailBean.getR();
                ArrayList<AudioSpecialDetailBean.RAudioSpecial.RAudioSpecialContent> content = details.getContent();//详情
                description.setAdapter(new AudioDetailsAdapter(getActivity(),content));
                title.setText(details.getTitle());
                shortcontent.setText(details.getShortcontent());
                salenum.setText(details.getSalenum());
                Glide.with(getActivity()).load(details.getImage()).into(iv_image);
                ArrayList<AudioSpecialDetailBean.RAudioSpecial.LastRecordList> last_record = details.getLast_record();
                mList.clear();
                mList.addAll(last_record);

                audioSpecialListAdapter.setData(mList);
                tv_changenum.setText("总共" +last_record.size() +"条,已更新"+last_record.size() +"条音频");
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
