package com.umiwi.ui.fragment.alreadyboughtfragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.ColumnAttentionAdapter;
import com.umiwi.ui.adapter.ColumnDetailsAdapter;
import com.umiwi.ui.adapter.LogicalThinkingAdapter;
import com.umiwi.ui.beans.updatebeans.AttemptBean;
import com.umiwi.ui.beans.updatebeans.AudioSpecialDetailsBean;
import com.umiwi.ui.dialog.ShareDialog;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.view.NoScrollListview;
import com.umiwi.ui.view.ScrollChangeScrollView;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;


/**
 * Created by Administrator on 2017/4/25 0025.
 * 已购专栏详情页面
 */

public class BuyColumnDetailsFragment extends BaseConstantFragment implements View.OnClickListener {
    ScrollChangeScrollView scrollview;
    ImageView iv_image;
    ImageView iv_fold_down;
    TextView tv_name;
    TextView tv_title;
    LinearLayout ll_orderby;
    ImageView iv_sort;
    TextView orderby;
    TextView update_count;
    TextView tv_buynumber;
//    @InjectView(R.id.lv_buycolumn)
//    NoScrollListview lv_buycolumn;
    LinearLayout ll_listvisable;
    LinearLayout ll_column_details;
    NoScrollListview description;
    TextView targetuser;
    NoScrollListview attention_listview;
    View rl_background;
    ImageView iv_back;
    TextView tab_title;
    ImageView iv_shared;
    ImageView record;
    RelativeLayout rl_bottom_up;
    ImageView iv_up;

    private String orderbyId = "new";
    private String id;//
    private ArrayList<AttemptBean.RAttenmpInfo.RecordsBean> recordList = new ArrayList<>();
    private LogicalThinkingAdapter logicalThinkingAdapter;
    private AudioSpecialDetailsBean.RAudioSpecialDetails details;
    private String columnurl;
    private NoScrollListview lv_buycolumn;
    private AnimationDrawable background;
    private int height;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_column_details,null);
//        ButterKnife.inject(this,view);

        id = getActivity().getIntent().getStringExtra("id");
        columnurl = getActivity().getIntent().getStringExtra("columnurl");

        initView(view);
        getDetailsData();
        getListData();
        initMediaPlay();
        return view;
    }

    private void initView(View view) {
        scrollview = (ScrollChangeScrollView) view.findViewById(R.id.scrollview);
        scrollview.scrollTo(0,0);
        lv_buycolumn = (NoScrollListview) view.findViewById(R.id.lv_buycolumn);
        iv_image = (ImageView) view.findViewById(R.id.iv_image);
        iv_fold_down = (ImageView) view.findViewById(R.id.iv_fold_down);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        ll_orderby = (LinearLayout) view.findViewById(R.id.ll_orderby);
        iv_sort = (ImageView) view.findViewById(R.id.iv_sort);
        orderby = (TextView) view.findViewById(R.id.orderby);
        update_count = (TextView) view.findViewById(R.id.update_count);
        tv_buynumber = (TextView) view.findViewById(R.id.tv_buynumber);
        ll_listvisable = (LinearLayout) view.findViewById(R.id.ll_listvisable);
        ll_column_details = (LinearLayout) view.findViewById(R.id.ll_column_details);
        description = (NoScrollListview) view.findViewById(R.id.description);
        targetuser = (TextView) view.findViewById(R.id.targetuser);
        attention_listview = (NoScrollListview) view.findViewById(R.id.attention_listview);
        rl_background = view.findViewById(R.id.rl_background);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        tab_title = (TextView) view.findViewById(R.id.tab_title);
        iv_shared = (ImageView) view.findViewById(R.id.iv_shared);
        record = (ImageView) view.findViewById(R.id.record);
        iv_up = (ImageView) view.findViewById(R.id.iv_up);
        rl_bottom_up = (RelativeLayout) view.findViewById(R.id.rl_bottom_up);
        lv_buycolumn.setFocusable(false);

        lv_buycolumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = iv_image.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                iv_image.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                height = iv_image.getHeight();
                scrollview.setScrollViewListener(mScrollViewListener);
            }
        });

        iv_fold_down.setOnClickListener(this);
        iv_sort.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_shared.setOnClickListener(this);
        record.setOnClickListener(this);
        iv_up.setOnClickListener(this);
        ll_orderby.setOnClickListener(this);
    }

    private ScrollChangeScrollView.ScrollViewListener mScrollViewListener = new ScrollChangeScrollView.ScrollViewListener() {
        @Override
        public void onScrollChanged(ScrollChangeScrollView scrollView, int x, int y, int oldx, int oldy) {
            if (y <= 0) {
                tab_title.setVisibility(View.INVISIBLE);
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
                    tab_title.setVisibility(View.VISIBLE);
                } else {
//                    iv_back.setImageResource(R.drawable.back_white);
//                    iv_shared.setImageResource(R.drawable.share_img);
//                    record.setBackgroundResource(R.drawable.anim_music_white);
                    tab_title.setVisibility(View.INVISIBLE);
                }
                tab_title.setTextColor(Color.argb((int) alpha, 0, 0, 0));
//                Log.e("111", "y > 0 && y <= imageHeight");
            } else {
//                iv_back.setImageResource(R.drawable.back_black);
//                iv_shared.setImageResource(R.drawable.share_black);
//                record.setBackgroundResource(R.drawable.anim_music_black);
                //白色不透明
                tab_title.setVisibility(View.VISIBLE);
                rl_background.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                //设置文字颜色
                //黑色
                tab_title.setTextColor(Color.argb((int) 255, 0, 0, 0));
//                Log.e("111", "else");
            }
        }
    };

    //获取专栏简介数据
    private void getDetailsData() {
       String url = String.format(UmiwiAPI.UMIWI_NOBUY_COLUMN,id + "");

        Log.e("TAG", "已购买的专栏url="  + url);
        GetRequest<AudioSpecialDetailsBean> request = new GetRequest<AudioSpecialDetailsBean>(url, GsonParser.class, AudioSpecialDetailsBean.class, new AbstractRequest.Listener<AudioSpecialDetailsBean>() {
            @Override
            public void onResult(AbstractRequest<AudioSpecialDetailsBean> request, AudioSpecialDetailsBean audioSpecialDetailsBean) {
                details = audioSpecialDetailsBean.getR();
                ArrayList<AudioSpecialDetailsBean.RAudioSpecialDetails.ContentWord> content = details.getContent();//详情简介
                description.setAdapter(new ColumnDetailsAdapter(getActivity(), content));
                targetuser.setText(details.getTargetuser());

                attention_listview.setAdapter(new ColumnAttentionAdapter(getActivity(), details.getAttention()));
                tab_title.setText(details.getTitle());
                Glide.with(getActivity()).load(details.getImage()).into(iv_image);
                tv_name.setText(details.getTitle());
                tv_title.setText(details.getShortcontent());
                tv_buynumber.setText(details.getSalenum());
            }

            @Override
            public void onError(AbstractRequest<AudioSpecialDetailsBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }
    //获取列表数据
    private void getListData() {
        String url = String.format(UmiwiAPI.Logincal_thinking, id, orderbyId);
        Log.e("TAG", "已购买的专栏url=" + url);
        GetRequest<AttemptBean> request = new GetRequest<AttemptBean>(url, GsonParser.class, AttemptBean.class, new AbstractRequest.Listener<AttemptBean>() {
            @Override
            public void onResult(AbstractRequest<AttemptBean> request, AttemptBean attemptBean) {
                ArrayList<AttemptBean.RAttenmpInfo.RecordsBean> recordsBeen = attemptBean.getR().getRecord();
                if(recordsBeen != null) {
//                    Log.e("TAG", "recordsBean=" + recordsBeen.toString());
                    recordList.clear();
                    update_count.setText(String.format("已更新%s条", recordsBeen.size()));
//                    Log.e("TAG", "recordsBeen.size()=" + recordsBeen.size());
                    recordList.addAll(recordsBeen);
                    logicalThinkingAdapter = new LogicalThinkingAdapter(getActivity(), recordList);
                    lv_buycolumn.setAdapter(logicalThinkingAdapter);
                }
            }
            @Override
            public void onError(AbstractRequest<AttemptBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //ButterKnife.reset(this);
    }
    /**
     * 播放按钮
     */
    private void initMediaPlay() {
        if (UmiwiApplication.mainActivity != null) {
            if (UmiwiApplication.mainActivity.service != null) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back :
                getActivity().finish();
                break;
            case R.id.iv_fold_down:
                ll_column_details.setVisibility(View.VISIBLE);
                ll_listvisable.setVisibility(View.GONE);
                rl_bottom_up.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_up:
                ll_column_details.setVisibility(View.GONE);
                ll_listvisable.setVisibility(View.VISIBLE);
                rl_bottom_up.setVisibility(View.GONE);
                break;
            case R.id.iv_shared:
                ShareDialog.getInstance().showDialog(getActivity(), details.getSharetitle(),
                        details.getSharecontent(), details.getShareurl(), details.getShareimg());
                break;
            case R.id.record:
                initRecord();
                break;
            case R.id.ll_orderby:
                initSort();
                break;
            case R.id.iv_sort:
                initSort();
                break;
        }
    }

    private void initSort() {
        if (orderby.getText().toString().equals("正序")) {
            orderby.setText("倒序");
            orderbyId = "old";
            getListData();
            rotateImpl();
        } else {
            orderby.setText("正序");
            orderbyId = "new";
            getListData();
            rotateImpl1();
        }
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

    private void initRecord() {
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
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
