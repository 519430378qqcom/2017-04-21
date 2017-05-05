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
import com.umiwi.ui.fragment.audiolive.BuyAudioLiveFragment;
import com.umiwi.ui.fragment.home.alreadyshopping.BuyCoumnFragment;
import com.umiwi.ui.fragment.home.alreadyshopping.NewVideoFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.AudioFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.BuySpecialFragment;
import com.umiwi.ui.main.BaseConstantFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 已购-
 * Created by ${Gpsi} on 2017/2/23.
 */

public class AlreadyBoughtFragment extends BaseConstantFragment {

    @InjectView(R.id.ab_tab_audio)
    TextView abAudio;
    @InjectView(R.id.ab_tab_video)
    TextView abVideo;
    @InjectView(R.id.ab_tab_column)
    TextView abColumn;
    @InjectView(R.id.ab_tab_special)
    TextView abspecial;
    //    @InjectView(R.id.ab_tab_ask_hear)
//    TextView abAskHear;
    @InjectView(R.id.ab_line)
    View ab_line;
    @InjectView(R.id.ab_viewPager)
    ViewPager viewPager;
    @InjectView(R.id.ab_tab_audiolive)
    TextView abTabAudiolive;

    private ArrayList<Fragment> fragments;
    private int line_width;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_already_bought, null);
        ButterKnife.inject(this, view);
        initMenuTab();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * c初始化导航菜单
     */
    void initMenuTab() {
        // ViewPropertyAnimator.animate(tabRecommend).scaleX(1.1f).setDuration(0);
        ViewPropertyAnimator.animate(abAudio).scaleX(1.0f).setDuration(0);
        ViewPropertyAnimator.animate(abVideo).scaleX(1.0f).setDuration(0);
        ViewPropertyAnimator.animate(abTabAudiolive).scaleX(1.0f).setDuration(0);
        ViewPropertyAnimator.animate(abColumn).scaleX(1.0f).setDuration(0);
//        ViewPropertyAnimator.animate(abAskHear).scaleX(1.0f).setDuration(0);
        ViewPropertyAnimator.animate(abspecial).scaleX(1.0f).setDuration(0);

        fragments = new ArrayList<Fragment>();
//        fragments.add(new ColumnFragment());
        fragments.add(new AudioFragment());
        fragments.add(new NewVideoFragment());
        fragments.add(new BuyAudioLiveFragment());
        fragments.add(new BuyCoumnFragment());
//        fragments.add(new AskHearFragment());
        fragments.add(new BuySpecialFragment());

        line_width = getActivity().getWindowManager().getDefaultDisplay().getWidth() / fragments.size();
        ab_line.getLayoutParams().width = line_width - 80;
        ab_line.requestLayout();
        viewPager.setOffscreenPageLimit(4);
        viewPager.setCurrentItem(0);
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
                ViewPropertyAnimator.animate(ab_line).translationX(tagerX + 40).setDuration(0);
            }

            @Override
            public void onPageSelected(int position) {
                changeState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        abAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        abVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        abTabAudiolive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });
        abColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
            }
        });
//        abAskHear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewPager.setCurrentItem(3);
//            }
//        });
        abspecial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(4);
            }
        });


    }

    private void changeState(int position) {
        if (position == 0) {
            abAudio.setTextColor(getResources().getColor(R.color.main_color));
            abVideo.setTextColor(getResources().getColor(R.color.black));
            abTabAudiolive.setTextColor(getResources().getColor(R.color.black));
            abColumn.setTextColor(getResources().getColor(R.color.black));
//            abAskHear.setTextColor(getResources().getColor(R.color.black));
            abspecial.setTextColor(getResources().getColor(R.color.black));
            ViewPropertyAnimator.animate(abspecial).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abspecial).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abAudio).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(abAudio).scaleY(1.1f).setDuration(200);


            ViewPropertyAnimator.animate(abVideo).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abVideo).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abTabAudiolive).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abTabAudiolive).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abColumn).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abColumn).scaleY(1.0f).setDuration(200);

//            ViewPropertyAnimator.animate(abAskHear).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(abAskHear).scaleY(1.0f).setDuration(200);
        } else if (position == 1) {

            abAudio.setTextColor(getResources().getColor(R.color.black));
            abVideo.setTextColor(getResources().getColor(R.color.main_color));
            abTabAudiolive.setTextColor(getResources().getColor(R.color.black));
            abColumn.setTextColor(getResources().getColor(R.color.black));
//            abAskHear.setTextColor(getResources().getColor(R.color.black));
            abspecial.setTextColor(getResources().getColor(R.color.black));

            ViewPropertyAnimator.animate(abspecial).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abspecial).scaleY(1.0f).setDuration(200);


            ViewPropertyAnimator.animate(abAudio).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abAudio).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abVideo).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(abVideo).scaleY(1.1f).setDuration(200);

            ViewPropertyAnimator.animate(abTabAudiolive).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abTabAudiolive).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abColumn).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abColumn).scaleY(1.0f).setDuration(200);

//            ViewPropertyAnimator.animate(abAskHear).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(abAskHear).scaleY(1.0f).setDuration(200);
        } else if (position == 2) {

            abAudio.setTextColor(getResources().getColor(R.color.black));
            abVideo.setTextColor(getResources().getColor(R.color.black));
            abTabAudiolive.setTextColor(getResources().getColor(R.color.main_color));
            abColumn.setTextColor(getResources().getColor(R.color.black));
//            abAskHear.setTextColor(getResources().getColor(R.color.black));
            abspecial.setTextColor(getResources().getColor(R.color.black));

            ViewPropertyAnimator.animate(abspecial).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abspecial).scaleY(1.0f).setDuration(200);


            ViewPropertyAnimator.animate(abAudio).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abAudio).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abVideo).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abVideo).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abTabAudiolive).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(abTabAudiolive).scaleY(1.1f).setDuration(200);

            ViewPropertyAnimator.animate(abColumn).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abColumn).scaleY(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(abAskHear).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(abAskHear).scaleY(1.0f).setDuration(200);
        } else if (position == 3) {

            abAudio.setTextColor(getResources().getColor(R.color.black));
            abVideo.setTextColor(getResources().getColor(R.color.black));
            abTabAudiolive.setTextColor(getResources().getColor(R.color.black));
            abColumn.setTextColor(getResources().getColor(R.color.main_color));
//            abAskHear.setTextColor(getResources().getColor(R.color.main_color));
            abspecial.setTextColor(getResources().getColor(R.color.black));

            ViewPropertyAnimator.animate(abspecial).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abspecial).scaleY(1.0f).setDuration(200);


            ViewPropertyAnimator.animate(abAudio).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abAudio).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abVideo).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abVideo).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abTabAudiolive).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abTabAudiolive).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abColumn).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(abColumn).scaleY(1.1f).setDuration(200);
        }else if (position == 4) {

            abAudio.setTextColor(getResources().getColor(R.color.black));
            abVideo.setTextColor(getResources().getColor(R.color.black));
            abTabAudiolive.setTextColor(getResources().getColor(R.color.black));
            abColumn.setTextColor(getResources().getColor(R.color.black));
//            abAskHear.setTextColor(getResources().getColor(R.color.main_color));
            abspecial.setTextColor(getResources().getColor(R.color.main_color));

            ViewPropertyAnimator.animate(abspecial).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(abspecial).scaleY(1.1f).setDuration(200);


            ViewPropertyAnimator.animate(abAudio).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abAudio).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abVideo).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abVideo).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abTabAudiolive).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abTabAudiolive).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(abColumn).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(abColumn).scaleY(1.0f).setDuration(200);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
