package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.umiwi.ui.R;
import com.umiwi.ui.main.BaseConstantFragment;
import java.util.ArrayList;


/**
 * Created by lws on 2017/2/28.
 */

public class ExperDetailsFragment extends BaseConstantFragment {
    public static final String KEY_DEFAULT_TUTORUID = "key.defaulttutoruid";
    private RelativeLayout head;
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expert_details_layout, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        isFirstOnMeasure = true;
        head = (RelativeLayout) view.findViewById(R.id.head);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        iv_shared = (ImageView) view.findViewById(R.id.iv_shared);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_describe = (TextView) view.findViewById(R.id.tv_describe);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_unfold = (TextView) view.findViewById(R.id.tv_unfold);
        tabsOrder = (TabLayout) view.findViewById(R.id.tabs_order);
        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);

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
                if (detailsColumnFragment == null) {
                    detailsColumnFragment = new DetailsColumnFragment();
                    transaction.add(R.id.fl_content, detailsColumnFragment);
                } else {
                    transaction.show(detailsColumnFragment);
                }
                break;
            case 1:
                if (experDetailsVoiceFragment == null) {
                    experDetailsVoiceFragment = new ExperDetailsVoiceFragment();
                    transaction.add(R.id.fl_content, experDetailsVoiceFragment);
                } else {
                    transaction.show(experDetailsVoiceFragment);
                }
                break;
            case 2:
                if (experDetailsVideoFragment == null) {
                    experDetailsVideoFragment = new ExperDetailsVideoFragment();
                    transaction.add(R.id.fl_content, experDetailsVideoFragment);
                } else {
                    transaction.show(experDetailsVideoFragment);
                }
                break;
            case 3:
                if (experDetailsWendaFragment == null) {
                    experDetailsWendaFragment = new ExperDetailsWendaFragment();
                    transaction.add(R.id.fl_content, experDetailsWendaFragment);
                } else {
                    transaction.show(experDetailsWendaFragment);
                }
                break;
            case 4:
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
