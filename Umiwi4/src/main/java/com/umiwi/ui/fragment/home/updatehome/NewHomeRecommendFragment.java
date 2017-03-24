package com.umiwi.ui.fragment.home.updatehome;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.HomeAudioFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.HomeVideoFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.OldYoumiFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.RecommendFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;
import com.umiwi.ui.fragment.search.SearchFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by LvDabing on 2017/2/16.
 * Email：lvdabing@lvshandian.com
 * Detail:新版本 《首页》
 */

public class NewHomeRecommendFragment extends BaseConstantFragment {

    @InjectView(R.id.search)
    TextView search;
    @InjectView(R.id.record)
    ImageView record;
    @InjectView(R.id.toolbar_actionbar)
    Toolbar toolbarActionbar;
    @InjectView(R.id.tab_recommend)
    TextView tabRecommend;
//    @InjectView(R.id.tab_expert)
//    TextView tabExpert;
//    @InjectView(R.id.tab_column)
//    TextView tabColumn;
    @InjectView(R.id.tab_audio)
    TextView tabAudio;
    @InjectView(R.id.tab_video)
    TextView tabVideo;
    @InjectView(R.id.tab_old_youmi)
    TextView tabOldYoumi;
    @InjectView(R.id.line)
    View line;
    @InjectView(R.id.viewPager)
    ViewPager viewPager;

    private ArrayList<Fragment> fragments;
    private int line_width;

    private static ViewPager rootviewPager;
    private AnimationDrawable background;

    public static ViewPager getRootViewpager(){
        return rootviewPager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_recommend_new, null);

        ButterKnife.inject(this, view);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
                i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, SearchFragment.class);
                startActivity(i);
            }
        });
        rootviewPager = viewPager;
        initMenuTab();
        initRecord();
        return view;
    }

    @Override
    public void onResume() {
        Log.e("TAG", "首页里的onResume()");
        super.onResume();
        if(UmiwiApplication.mainActivity.service != null) {
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

    /**
     * 音频播放按钮
     */
    private void initRecord() {

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }
        });
    }

    /**初始化导航菜单了**/
    void initMenuTab(){
        ViewPropertyAnimator.animate(tabRecommend).scaleX(1.1f).setDuration(0);
//        ViewPropertyAnimator.animate(tabExpert).scaleY(1.1f).setDuration(0);
//        ViewPropertyAnimator.animate(tabColumn).scaleX(1.1f).setDuration(0);
        ViewPropertyAnimator.animate(tabAudio).scaleY(1.1f).setDuration(0);
        ViewPropertyAnimator.animate(tabVideo).scaleX(1.1f).setDuration(0);
        ViewPropertyAnimator.animate(tabOldYoumi).scaleY(1.1f).setDuration(0);

        fragments = new ArrayList<Fragment>();
        fragments.add(new RecommendFragment());
//        fragments.add(new ExpertFragment());
//        fragments.add(new LecturerListFragment());
//        fragments.add(new ColumnFragment());
        fragments.add(new HomeAudioFragment());
        fragments.add(new HomeVideoFragment());
        fragments.add(new OldYoumiFragment());


        line_width = getActivity().getWindowManager().getDefaultDisplay().getWidth()
                / fragments.size();
        line.getLayoutParams().width = line_width-120;
        line.requestLayout();
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new FragmentStatePagerAdapter(
                getChildFragmentManager()) {

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragments.get(arg0);
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                changeState(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                float tagerX = arg0 * line_width + arg2 / fragments.size();
                ViewPropertyAnimator.animate(line).translationX(tagerX+60)
                        .setDuration(0);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        tabRecommend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                viewPager.setCurrentItem(0);

            }
        });

//        tabExpert.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                viewPager.setCurrentItem(1);
//
//            }
//        });
//        tabColumn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                viewPager.setCurrentItem(2);
//
//            }
//        });
        tabAudio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                viewPager.setCurrentItem(1);

            }
        });
        tabVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                viewPager.setCurrentItem(2);

            }
        });
        tabOldYoumi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                viewPager.setCurrentItem(3);

            }
        });

    }


    private void changeState(int position) {
        if (position == 0) {
            tabRecommend.setTextColor(getResources().getColor(R.color.main_color));

//            tabExpert.setTextColor(getResources().getColor(R.color.black));
//            tabColumn.setTextColor(getResources().getColor(R.color.black));
            tabAudio.setTextColor(getResources().getColor(R.color.black));
            tabVideo.setTextColor(getResources().getColor(R.color.black));
            tabOldYoumi.setTextColor(getResources().getColor(R.color.black));


            ViewPropertyAnimator.animate(tabRecommend).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(tabRecommend).scaleY(1.1f).setDuration(200);

//            ViewPropertyAnimator.animate(tabExpert).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tabExpert).scaleY(1.0f).setDuration(200);
//
//            ViewPropertyAnimator.animate(tabColumn).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tabColumn).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(tabAudio).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tabAudio).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(tabVideo).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tabVideo).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(tabOldYoumi).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tabOldYoumi).scaleY(1.0f).setDuration(200);

        } else if (position==1){
            tabRecommend.setTextColor(getResources().getColor(R.color.black));

//            tabExpert.setTextColor(getResources().getColor(R.color.main_color));
//            tabColumn.setTextColor(getResources().getColor(R.color.black));
            tabAudio.setTextColor(getResources().getColor(R.color.main_color));
            tabVideo.setTextColor(getResources().getColor(R.color.black));
            tabOldYoumi.setTextColor(getResources().getColor(R.color.black));


            ViewPropertyAnimator.animate(tabRecommend).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tabRecommend).scaleY(1.0f).setDuration(200);

//            ViewPropertyAnimator.animate(tabExpert).scaleX(1.1f).setDuration(200);
//            ViewPropertyAnimator.animate(tabExpert).scaleY(1.1f).setDuration(200);
//
//            ViewPropertyAnimator.animate(tabColumn).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tabColumn).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(tabAudio).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(tabAudio).scaleY(1.1f).setDuration(200);

            ViewPropertyAnimator.animate(tabVideo).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tabVideo).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(tabOldYoumi).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tabOldYoumi).scaleY(1.0f).setDuration(200);
        }
        else if (position==2){
            tabRecommend.setTextColor(getResources().getColor(R.color.black));

//            tabExpert.setTextColor(getResources().getColor(R.color.black));
//            tabColumn.setTextColor(getResources().getColor(R.color.main_color));
            tabAudio.setTextColor(getResources().getColor(R.color.black));
            tabVideo.setTextColor(getResources().getColor(R.color.main_color));
            tabOldYoumi.setTextColor(getResources().getColor(R.color.black));




            ViewPropertyAnimator.animate(tabRecommend).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tabRecommend).scaleY(1.0f).setDuration(200);

//            ViewPropertyAnimator.animate(tabExpert).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tabExpert).scaleY(1.0f).setDuration(200);
//
//            ViewPropertyAnimator.animate(tabColumn).scaleX(1.1f).setDuration(200);
//            ViewPropertyAnimator.animate(tabColumn).scaleY(1.1f).setDuration(200);

            ViewPropertyAnimator.animate(tabAudio).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tabAudio).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(tabVideo).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(tabVideo).scaleY(1.1f).setDuration(200);

            ViewPropertyAnimator.animate(tabOldYoumi).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tabOldYoumi).scaleY(1.0f).setDuration(200);
        }
        else if (position==3){
            tabRecommend.setTextColor(getResources().getColor(R.color.black));

//            tabExpert.setTextColor(getResources().getColor(R.color.black));
//            tabColumn.setTextColor(getResources().getColor(R.color.black));
            tabAudio.setTextColor(getResources().getColor(R.color.black));
            tabVideo.setTextColor(getResources().getColor(R.color.black));
            tabOldYoumi.setTextColor(getResources().getColor(R.color.main_color));


            ViewPropertyAnimator.animate(tabRecommend).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tabRecommend).scaleY(1.0f).setDuration(200);

//            ViewPropertyAnimator.animate(tabExpert).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tabExpert).scaleY(1.0f).setDuration(200);
//
//            ViewPropertyAnimator.animate(tabColumn).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tabColumn).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(tabAudio).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tabAudio).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(tabVideo).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tabVideo).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(tabOldYoumi).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(tabOldYoumi).scaleY(1.1f).setDuration(200);
        }
//        else if (position==4){
//            tabRecommend.setTextColor(getResources().getColor(R.color.black));
//
//            tabExpert.setTextColor(getResources().getColor(R.color.black));
//            tabColumn.setTextColor(getResources().getColor(R.color.black));
//            tabAudio.setTextColor(getResources().getColor(R.color.black));
//            tabVideo.setTextColor(getResources().getColor(R.color.main_color));
//            tabOldYoumi.setTextColor(getResources().getColor(R.color.black));
//
//
//            ViewPropertyAnimator.animate(tabRecommend).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tabRecommend).scaleY(1.0f).setDuration(200);
//
//            ViewPropertyAnimator.animate(tabExpert).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tabExpert).scaleY(1.0f).setDuration(200);
//
//            ViewPropertyAnimator.animate(tabColumn).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tabColumn).scaleY(1.0f).setDuration(200);
//
//            ViewPropertyAnimator.animate(tabAudio).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tabAudio).scaleY(1.0f).setDuration(200);
//
//            ViewPropertyAnimator.animate(tabVideo).scaleX(1.1f).setDuration(200);
//            ViewPropertyAnimator.animate(tabVideo).scaleY(1.1f).setDuration(200);
//
//            ViewPropertyAnimator.animate(tabOldYoumi).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tabOldYoumi).scaleY(1.0f).setDuration(200);
//        }else if (position==5){
//            tabRecommend.setTextColor(getResources().getColor(R.color.black));
//
//            tabExpert.setTextColor(getResources().getColor(R.color.black));
//            tabColumn.setTextColor(getResources().getColor(R.color.black));
//            tabAudio.setTextColor(getResources().getColor(R.color.black));
//            tabVideo.setTextColor(getResources().getColor(R.color.black));
//            tabOldYoumi.setTextColor(getResources().getColor(R.color.main_color));
//
//
//
//            ViewPropertyAnimator.animate(tabRecommend).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tabRecommend).scaleY(1.0f).setDuration(200);
//
//            ViewPropertyAnimator.animate(tabExpert).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tabExpert).scaleY(1.0f).setDuration(200);
//
//            ViewPropertyAnimator.animate(tabColumn).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tabColumn).scaleY(1.0f).setDuration(200);
//
//            ViewPropertyAnimator.animate(tabAudio).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tabAudio).scaleY(1.0f).setDuration(200);
//
//            ViewPropertyAnimator.animate(tabVideo).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tabVideo).scaleY(1.0f).setDuration(200);
//
//            ViewPropertyAnimator.animate(tabOldYoumi).scaleX(1.1f).setDuration(200);
//            ViewPropertyAnimator.animate(tabOldYoumi).scaleY(1.1f).setDuration(200);
//        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
