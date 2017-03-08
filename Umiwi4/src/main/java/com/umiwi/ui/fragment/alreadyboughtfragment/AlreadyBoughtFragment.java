package com.umiwi.ui.fragment.alreadyboughtfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.umiwi.ui.R;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.AudioFragment;
import com.umiwi.ui.fragment.home.alreadyshopping.ColumnFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VideoFragment;
import com.umiwi.ui.main.BaseConstantFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 已购-
 * Created by ${Gpsi} on 2017/2/23.
 */

public class AlreadyBoughtFragment extends BaseConstantFragment {

    @InjectView(R.id.ab_tab_column)
    TextView abColumn;
    @InjectView(R.id.ab_tab_audio)
    TextView abAudio;
    @InjectView(R.id.ab_tab_video)
    TextView abVideo;
    @InjectView(R.id.ab_tab_ask_hear)
    TextView abAskHear;
    @InjectView(R.id.ab_line)
    View ab_line;
    @InjectView(R.id.ab_viewPager)
    ViewPager viewPager;

    private ArrayList<Fragment> fragments;
    private int line_width;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_already_bought,null);
        ButterKnife.inject(this,view);
        initMenuTab();
        return view;
    }

    /**
     * c初始化导航菜单
     */
    void initMenuTab(){
        // ViewPropertyAnimator.animate(tabRecommend).scaleX(1.1f).setDuration(0);
        ViewPropertyAnimator.animate(abColumn).scaleX(1.3f).setDuration(0);
        ViewPropertyAnimator.animate(abAudio).scaleX(1.3f).setDuration(0);
        ViewPropertyAnimator.animate(abVideo).scaleX(1.3f).setDuration(0);
        ViewPropertyAnimator.animate(abAskHear).scaleX(1.3f).setDuration(0);

        fragments = new ArrayList<Fragment>();
        fragments.add(new ColumnFragment());
        fragments.add(new AudioFragment());
        fragments.add(new VideoFragment());
        fragments.add(new AskHearFragment());

        line_width = getActivity().getWindowManager().getDefaultDisplay().getWidth()/fragments.size();
        ab_line.getLayoutParams().width = line_width-10;
        ab_line.requestLayout();
        viewPager.setOffscreenPageLimit(3);
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
                ViewPropertyAnimator.animate(ab_line).translationX(tagerX+4).setDuration(0);
            }

            @Override
            public void onPageSelected(int position) {
                changeState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

         abColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        abAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        abVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });
        abAskHear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
            }
        });


    }

    private void changeState(int position) {
        if (position == 0) {
            abColumn.setTextColor(getResources().getColor(R.color.main_color));
            abAudio.setTextColor(getResources().getColor(R.color.black));
            abVideo.setTextColor(getResources().getColor(R.color.black));
            abAskHear.setTextColor(getResources().getColor(R.color.black));


            ViewPropertyAnimator.animate(abColumn).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(abColumn).scaleY(1.1f).setDuration(200);

            ViewPropertyAnimator.animate(abAudio).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abAudio).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abVideo).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abVideo).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abAskHear).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abAskHear).scaleY(1.0f).setDuration(200);
        } else if (position==1){
            abColumn.setTextColor(getResources().getColor(R.color.black));

            abAudio.setTextColor(getResources().getColor(R.color.main_color));
            abVideo.setTextColor(getResources().getColor(R.color.black));
            abAskHear.setTextColor(getResources().getColor(R.color.black));


            ViewPropertyAnimator.animate(abColumn).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abColumn).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abAudio).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(abAudio).scaleY(1.1f).setDuration(200);

            ViewPropertyAnimator.animate(abVideo).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abVideo).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abAskHear).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abAskHear).scaleY(1.0f).setDuration(200);
        }
        else if (position==2){
            abColumn.setTextColor(getResources().getColor(R.color.black));

            abAudio.setTextColor(getResources().getColor(R.color.black));
            abVideo.setTextColor(getResources().getColor(R.color.main_color));
            abAskHear.setTextColor(getResources().getColor(R.color.black));




            ViewPropertyAnimator.animate(abColumn).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abColumn).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abAudio).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abAudio).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abVideo).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(abVideo).scaleY(1.1f).setDuration(200);

            ViewPropertyAnimator.animate(abAskHear).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abAskHear).scaleY(1.0f).setDuration(200);
        }
        else if (position==3){
            abColumn.setTextColor(getResources().getColor(R.color.black));

            abAudio.setTextColor(getResources().getColor(R.color.black));
            abVideo.setTextColor(getResources().getColor(R.color.black));
            abAskHear.setTextColor(getResources().getColor(R.color.main_color));


            ViewPropertyAnimator.animate(abColumn).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abColumn).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abAudio).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abAudio).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abVideo).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abVideo).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abAskHear).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(abAskHear).scaleY(1.1f).setDuration(200);
        }else if (position==4){
            abColumn.setTextColor(getResources().getColor(R.color.black));

            abAudio.setTextColor(getResources().getColor(R.color.black));
            abVideo.setTextColor(getResources().getColor(R.color.black));
            abAskHear.setTextColor(getResources().getColor(R.color.black));


            ViewPropertyAnimator.animate(abColumn).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abColumn).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abAudio).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abAudio).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abVideo).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abVideo).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abAskHear).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abAskHear).scaleY(1.0f).setDuration(200);
        }else if (position==5){
            abColumn.setTextColor(getResources().getColor(R.color.black));

            abAudio.setTextColor(getResources().getColor(R.color.black));
            abVideo.setTextColor(getResources().getColor(R.color.black));
            abAskHear.setTextColor(getResources().getColor(R.color.black));



            ViewPropertyAnimator.animate(abColumn).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abColumn).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abAudio).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abAudio).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abVideo).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abVideo).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abAskHear).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abAskHear).scaleY(1.0f).setDuration(200);
        }
    }
}
