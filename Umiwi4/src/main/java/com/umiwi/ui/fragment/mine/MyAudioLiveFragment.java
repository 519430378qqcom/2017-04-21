package com.umiwi.ui.fragment.mine;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.umiwi.ui.R;
import com.umiwi.ui.adapter.MyLiveFragmentAdapter;
import com.umiwi.ui.main.BaseConstantFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.umiwi.ui.R.id.ab_line;
import static com.umiwi.ui.R.id.vp_viewPager;

/**
 * Created by dong on 2017/5/2 0002.
 * 我的直播页
 */

public class MyAudioLiveFragment extends BaseConstantFragment {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tab_living)
    TextView tabLiving;
    @InjectView(R.id.tab_immediately_start)
    TextView tabImmediatelyStart;
    @InjectView(R.id.tab_already_end)
    TextView tabAlreadyEnd;
    @InjectView(ab_line)
    View abLine;
    @InjectView(vp_viewPager)
    ViewPager vpViewPager;
    private Activity activity;
    private int line_width;
    private ArrayList<Fragment> fragments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_my, container, false);
        ButterKnife.inject(this, view);
        activity = getActivity();
        initData();
        return view;
    }

    /**
     * 加载数据
     */
    private void initData() {
        fragments = new ArrayList<>();
        fragments.add(new LivingFragment());
        fragments.add(new ImmediatelyLiveFragment());
        fragments.add(new EndLiveFragment());
        line_width = getActivity().getWindowManager().getDefaultDisplay().getWidth()/ fragments.size();
        abLine.getLayoutParams().width = line_width -120;
        abLine.requestLayout();
        vpViewPager.setOffscreenPageLimit(2);
        vpViewPager.setCurrentItem(0);
        vpViewPager.setAdapter(new MyLiveFragmentAdapter(getFragmentManager(), fragments));
        vpViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                float tagerX = position * line_width + positionOffsetPixels / fragments.size();
                ViewPropertyAnimator.animate(abLine).translationX(tagerX+60).setDuration(0);
            }

            @Override
            public void onPageSelected(int position) {
                changeState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeState(int position) {
        if(position == 0) {

            tabLiving.setTextColor(getResources().getColor(R.color.main_color));
            tabImmediatelyStart.setTextColor(getResources().getColor(R.color.black));
            tabAlreadyEnd.setTextColor(getResources().getColor(R.color.black));

            ViewPropertyAnimator.animate(tabLiving).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(tabLiving).scaleY(1.1f).setDuration(200);

            ViewPropertyAnimator.animate(tabImmediatelyStart).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tabImmediatelyStart).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(tabAlreadyEnd).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tabAlreadyEnd).scaleY(1.0f).setDuration(200);
        }else if(position == 1) {
            tabLiving.setTextColor(getResources().getColor(R.color.black));
            tabImmediatelyStart.setTextColor(getResources().getColor(R.color.main_color));
            tabAlreadyEnd.setTextColor(getResources().getColor(R.color.black));

            ViewPropertyAnimator.animate(tabLiving).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tabLiving).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(tabImmediatelyStart).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(tabImmediatelyStart).scaleY(1.1f).setDuration(200);

            ViewPropertyAnimator.animate(tabAlreadyEnd).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tabAlreadyEnd).scaleY(1.0f).setDuration(200);
        }else if(position ==2) {
            tabLiving.setTextColor(getResources().getColor(R.color.black));
            tabImmediatelyStart.setTextColor(getResources().getColor(R.color.black));
            tabAlreadyEnd.setTextColor(getResources().getColor(R.color.main_color));

            ViewPropertyAnimator.animate(tabLiving).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tabLiving).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(tabImmediatelyStart).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tabImmediatelyStart).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(tabAlreadyEnd).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(tabAlreadyEnd).scaleY(1.1f).setDuration(200);
        }

    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    @OnClick({R.id.iv_back, R.id.tab_living, R.id.tab_immediately_start, R.id.tab_already_end, ab_line})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                activity.finish();
                break;
            case R.id.tab_living:
                vpViewPager.setCurrentItem(0);
                break;
            case R.id.tab_immediately_start:
                vpViewPager.setCurrentItem(1);
                break;
            case R.id.tab_already_end:
                vpViewPager.setCurrentItem(2);
                break;
            case ab_line:
                break;
        }
    }
}
