package com.umiwi.ui.fragment.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.umiwi.ui.R;
import com.umiwi.ui.main.BaseConstantFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/3/22.
 * 浏览记录
 */

public class RecentPlayRecordFragment extends BaseConstantFragment {

    @InjectView(R.id.ab_tab_audio)
    TextView rpAudio;
    @InjectView(R.id.ab_tab_video)
    TextView rpVideo;
    @InjectView(R.id.ab_line)
    View ab_line;
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.ab_viewPager)
    ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private int line_width;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recentplay_layout, null);
        ButterKnife.inject(this,view);
        initMenuTab();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        return view;
    }

    /**
     * 初始化导航栏
     */
    private void initMenuTab() {
        ViewPropertyAnimator.animate(rpAudio).scaleX(1.0f).setDuration(0);
        ViewPropertyAnimator.animate(rpVideo).scaleX(1.0f).setDuration(0);

        fragments = new ArrayList<Fragment>();
        fragments.add(new RecentPlayAudioFragment());
        fragments.add(new RecentPlayVideoFragment());

        line_width = getActivity().getWindowManager().getDefaultDisplay().getWidth()/fragments.size();
        ab_line.getLayoutParams().width = line_width-300;
        ab_line.requestLayout();
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                float tagerX = position * line_width + positionOffsetPixels / fragments.size();
                ViewPropertyAnimator.animate(ab_line).translationX(tagerX+150).setDuration(0);
            }

            @Override
            public void onPageSelected(int position) {
                changeState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        rpAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        rpVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });


    }

    private void changeState(int position) {
        if (position == 0) {
            rpAudio.setTextColor(getResources().getColor(R.color.main_color));
            rpVideo.setTextColor(getResources().getColor(R.color.black));

            ViewPropertyAnimator.animate(rpAudio).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(rpAudio).scaleY(1.1f).setDuration(200);

            ViewPropertyAnimator.animate(rpVideo).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(rpVideo).scaleY(1.0f).setDuration(200);
        } else if(position == 1) {
            rpVideo.setTextColor(getResources().getColor(R.color.main_color));
            rpAudio.setTextColor(getResources().getColor(R.color.black));

            ViewPropertyAnimator.animate(rpVideo).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(rpVideo).scaleY(1.1f).setDuration(200);

            ViewPropertyAnimator.animate(rpAudio).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(rpAudio).scaleY(1.0f).setDuration(200);
        }else if(position == 2) {
            rpAudio.setTextColor(getResources().getColor(R.color.black));
            rpVideo.setTextColor(getResources().getColor(R.color.black));
            ViewPropertyAnimator.animate(rpAudio).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(rpAudio).scaleY(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(rpVideo).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(rpVideo).scaleY(1.0f).setDuration(200);
        }
    }
}
