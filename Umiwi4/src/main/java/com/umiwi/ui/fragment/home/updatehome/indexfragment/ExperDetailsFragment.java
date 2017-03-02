package com.umiwi.ui.fragment.home.updatehome.indexfragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.widget.TextView;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.ExperDetailsBean;
import com.umiwi.ui.main.BaseConstantFragment;

import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.util.JsonUtil;
import com.umiwi.ui.view.TopFloatScrollView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;

import cn.youmi.framework.util.ImageLoader;

/**
 * Created by lws on 2017/2/28.
 */

public class ExperDetailsFragment extends BaseConstantFragment {
    public static final String KEY_DEFAULT_TUTORUID = "key.defaulttutoruid";
    private ImageView head;
    private ImageView iv_back;
    private ImageView iv_shared;
    private TextView tv_name;
    private TextView tv_describe;
    private TextView tv_content;
    private TextView tv_unfold;
    private boolean isUnfold = true;
    private boolean isFirstOnMeasure;
    private ArrayList<String> mTitleList = new ArrayList<String>();
    private TabLayout tabsOrder;
    private FrameLayout fl_content;
    private Fragment detailsColumnFragment, experDetailsVoiceFragment, experDetailsVideoFragment, experDetailsWendaFragment,experDetailsCommentFragment;
    private TopFloatScrollView scroll_view;
    public static int CurrentPosition ;
    private String uid;
    private String experName;
    private String tutorimage;
    private String description;
    private String tutortitle;
    public static String albumurl;
    public static String audioalbumurl;
    public static String questionurl;
    public static String tcolumnurl;
    public static String threadurl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expert_details_layout, null);
        uid = getActivity().getIntent().getStringExtra(ExperDetailsFragment.this.KEY_DEFAULT_TUTORUID);
        getInfo();

        initView(view);
        return view;
    }

    private void getInfo() {
        OkHttpUtils.get().url(UmiwiAPI.CELEBRTYY_DETAILS+uid).build().execute(new CustomStringCallBack() {

            @Override
            public void onFaild() {
                Log.e("data","请求数据失败");
            }

            @Override
            public void onSucess(String data) {
                Log.e("data","请求数据成功"+data);
                ExperDetailsBean experDetailsBean = JsonUtil.json2Bean(data, ExperDetailsBean.class);
                experName = experDetailsBean.getName();
                tutorimage = experDetailsBean.getTutorimage();
                description = experDetailsBean.getDescription();
                tutortitle = experDetailsBean.getTutortitle();
                tv_name.setText(experName);
                tv_describe.setText(tutortitle);
                tv_content.setText(description);
                ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
                mImageLoader.loadImage(tutorimage,head);
                ExperDetailsBean.ResultBean resultUrl = experDetailsBean.getResult();
                if (resultUrl!=null){
                    //专栏
                    albumurl = resultUrl.getAlbumurl();
                    //音频
                    audioalbumurl = resultUrl.getAudioalbumurl();
                    //问答数据
                    questionurl = resultUrl.getQuestionurl();
                    //视频
                    tcolumnurl = resultUrl.getTcolumnurl();
                    //评论
                    threadurl = resultUrl.getThreadurl();
                }

            }
        });
    }

    private void initView(View view) {
        isFirstOnMeasure = true;
        head = (ImageView) view.findViewById(R.id.head);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        iv_shared = (ImageView) view.findViewById(R.id.iv_shared);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_describe = (TextView) view.findViewById(R.id.tv_describe);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_unfold = (TextView) view.findViewById(R.id.tv_unfold);
        tabsOrder = (TabLayout) view.findViewById(R.id.tabs_order);
        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
        final LinearLayout tv_more = (LinearLayout) view.findViewById(R.id.more);
        scroll_view = (TopFloatScrollView) view.findViewById(R.id.scroll_view);
        scroll_view.registerOnScrollViewScrollToBottom(new TopFloatScrollView.OnScrollBottomListener() {
            @Override
            public void onLoading() {
                tv_more.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_more.setVisibility(View.GONE);

                            }
                        });
                    }
                }).start();

            }
        });
        tv_unfold.setOnClickListener(new UnfoldOnClickListener());
        iv_back.setOnClickListener(new BackOnClickListener());
        cutTheme(0);

        initTabLayout();

    }
    //初始化 TabLayout
    private void initTabLayout() {
        mTitleList.add("专栏");
        mTitleList.add("音频");
        mTitleList.add("视频");
        mTitleList.add("问答");
        mTitleList.add("评论");
        //设置tab模式，当前为系统默认模式
        tabsOrder.setTabMode(TabLayout.MODE_SCROLLABLE);
        //添加tab选项卡
        tabsOrder.addTab(tabsOrder.newTab().setText(mTitleList.get(0)));
        tabsOrder.addTab(tabsOrder.newTab().setText(mTitleList.get(1)));
        tabsOrder.addTab(tabsOrder.newTab().setText(mTitleList.get(2)));
        tabsOrder.addTab(tabsOrder.newTab().setText(mTitleList.get(3)));
        tabsOrder.addTab(tabsOrder.newTab().setText(mTitleList.get(4)));
        tabsOrder.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                 cutTheme(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }



    private void cutTheme(int position) {
        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragments(transaction);
        switch (position){
            case 0:
                CurrentPosition = 0;
                if (detailsColumnFragment == null) {
                    detailsColumnFragment = new DetailsColumnFragment();
                    transaction.add(R.id.fl_content, detailsColumnFragment);
                } else {
                    transaction.show(detailsColumnFragment);
                }
                break;
            case 1:
                CurrentPosition = 1;

                if (experDetailsVoiceFragment == null) {
                    experDetailsVoiceFragment = new ExperDetailsVoiceFragment();
                    transaction.add(R.id.fl_content, experDetailsVoiceFragment);
                } else {
                    transaction.show(experDetailsVoiceFragment);
                }
                break;
            case 2:
                CurrentPosition = 2;

                if (experDetailsVideoFragment == null) {
                    experDetailsVideoFragment = new ExperDetailsVideoFragment();
                    transaction.add(R.id.fl_content, experDetailsVideoFragment);
                } else {
                    transaction.show(experDetailsVideoFragment);
                }
                break;
            case 3:
                CurrentPosition = 3;

                if (experDetailsWendaFragment == null) {
                    experDetailsWendaFragment = new ExperDetailsWendaFragment();
                    transaction.add(R.id.fl_content, experDetailsWendaFragment);
                } else {
                    transaction.show(experDetailsWendaFragment);
                }
                break;
            case 4:
                CurrentPosition = 4;

                if (experDetailsCommentFragment == null) {
                    experDetailsCommentFragment = new ExperDetailsCommentFragment();
                    transaction.add(R.id.fl_content, experDetailsCommentFragment);
                } else {
                    transaction.show(experDetailsCommentFragment);
                }
                break;

        }
        transaction.commitAllowingStateLoss();

    }
    public void hideFragments(FragmentTransaction ft) {
        if (detailsColumnFragment != null)
            ft.hide(detailsColumnFragment);
        if (experDetailsVoiceFragment != null)
            ft.hide(experDetailsVoiceFragment);
        if (experDetailsVideoFragment != null)
            ft.hide(experDetailsVideoFragment);
        if (experDetailsWendaFragment != null)
            ft.hide(experDetailsWendaFragment);
        if (experDetailsCommentFragment!=null){
            ft.hide(experDetailsCommentFragment);
        }
    }

    class UnfoldOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (isUnfold) { //开
                isUnfold = false;
                tv_content.setMaxLines(Integer.MAX_VALUE);
            } else {  //关
                isUnfold = true;
                tv_content.setMaxLines(3);
            }
        }
    }

    class BackOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
          getActivity().finish();
        }
    }

}
