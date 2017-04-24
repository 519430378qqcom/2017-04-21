package com.umiwi.ui.fragment.audiolive;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.umiwi.ui.R;
import com.umiwi.ui.main.BaseConstantFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/4/24 0024.
 */

public class AudioLiveFragment extends BaseConstantFragment {
    @InjectView(R.id.tab_liveon)
    TextView tab_liveon;
    @InjectView(R.id.tab_nostart)
    TextView tab_nostart;
    @InjectView(R.id.tab_alreadyoff)
    TextView tab_alreadyoff;
    @InjectView(R.id.ab_line)
    View ab_line;
    @InjectView(R.id.vp_viewPager)
    ViewPager vp_viewPager;
    private ArrayList<Fragment> fragments;
    private int line_width;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_live,null);
        ButterKnife.inject(this,view);
        initMenuTab();
        return view;
    }


    private void initMenuTab() {
        ViewPropertyAnimator.animate(tab_liveon).scaleX(1.0f).setDuration(0);
        ViewPropertyAnimator.animate(tab_nostart).scaleX(1.0f).setDuration(0);
        ViewPropertyAnimator.animate(tab_alreadyoff).scaleX(1.0f).setDuration(0);
        fragments = new ArrayList<Fragment>();
        fragments.add(new AudioLiveOnFragment());
        fragments.add(new AudioLiveNoStartFragment());
        fragments.add(new AudioLiveAlreadyOffFrgament());
        line_width = getActivity().getWindowManager().getDefaultDisplay().getWidth()/fragments.size();
        ab_line.getLayoutParams().width = line_width-180;
        ab_line.requestLayout();
        vp_viewPager.setOffscreenPageLimit(2);
        vp_viewPager.setCurrentItem(0);
        vp_viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        vp_viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                float tagerX = position * line_width + positionOffsetPixels / fragments.size();
                ViewPropertyAnimator.animate(ab_line).translationX(tagerX+90).setDuration(0);
            }

            @Override
            public void onPageSelected(int position) {
                changeState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tab_liveon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp_viewPager.setCurrentItem(0);
            }
        });
        tab_nostart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp_viewPager.setCurrentItem(1);
            }
        });
        tab_alreadyoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp_viewPager.setCurrentItem(2);
            }
        });
    }

    private void changeState(int position) {
        if(position == 0) {

            tab_liveon.setTextColor(getResources().getColor(R.color.main_color));
            tab_nostart.setTextColor(getResources().getColor(R.color.black));
            tab_alreadyoff.setTextColor(getResources().getColor(R.color.black));

            ViewPropertyAnimator.animate(tab_liveon).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(tab_liveon).scaleY(1.1f).setDuration(200);

            ViewPropertyAnimator.animate(tab_nostart).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tab_nostart).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(tab_alreadyoff).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tab_alreadyoff).scaleY(1.0f).setDuration(200);
        }else if(position == 1) {
            tab_liveon.setTextColor(getResources().getColor(R.color.black));
            tab_nostart.setTextColor(getResources().getColor(R.color.main_color));
            tab_alreadyoff.setTextColor(getResources().getColor(R.color.black));

            ViewPropertyAnimator.animate(tab_liveon).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tab_liveon).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(tab_nostart).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(tab_nostart).scaleY(1.1f).setDuration(200);

            ViewPropertyAnimator.animate(tab_alreadyoff).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tab_alreadyoff).scaleY(1.0f).setDuration(200);
        }else if(position ==2) {
            tab_liveon.setTextColor(getResources().getColor(R.color.black));
            tab_nostart.setTextColor(getResources().getColor(R.color.black));
            tab_alreadyoff.setTextColor(getResources().getColor(R.color.main_color));

            ViewPropertyAnimator.animate(tab_liveon).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tab_liveon).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(tab_nostart).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tab_nostart).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(tab_alreadyoff).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(tab_alreadyoff).scaleY(1.1f).setDuration(200);
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        ButterKnife.reset(this);
    }
}
