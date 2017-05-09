package com.umiwi.ui.fragment.home.updatehome;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.updateadapter.MyFragmentStatePagerAdapter;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.HotListFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.NewTendencyFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.RecommendFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.StartBusinessFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.WorkPlaceFragment;
import com.umiwi.ui.fragment.search.SearchFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.util.CommonHelper;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;



/**
 * Created by LvDabing on 2017/2/16.
 * Email：lvdabing@lvshandian.com
 * Detail:新版本 《首页》
 */

public class NewHomeRecommendFragment extends BaseConstantFragment {

    //    @InjectView(R.id.search)
//    TextView search;
//    @InjectView(R.id.record)
//    ImageView record;
//    @InjectView(R.id.toolbar_actionbar)
//    Toolbar toolbarActionbar;
//    @InjectView(R.id.tab_recommend)
//    TextView tabRecommend;
//    @InjectView(R.id.tab_expert)
//    TextView tabExpert;
//    @InjectView(R.id.tab_column)
//    TextView tabColumn;
//    @InjectView(R.id.tab_audio)
//    TextView tabAudio;
//    @InjectView(R.id.tab_video)
//    TextView tabVideo;
//    @InjectView(R.id.tab_old_youmi)
//    TextView tabOldYoumi;
//    @InjectView(R.id.tab_startbus)
//    TextView tab_startbus;
//    @InjectView(R.id.tab_workplace)
//    TextView tab_workplace;
//    @InjectView(R.id.tab_newtende)
//    TextView tab_newtende;
//    @InjectView(R.id.tab_rank)
//    TextView tab_rank;
//    @InjectView(R.id.line)
//    View line;
//    @InjectView(R.id.viewPager)
//    ViewPager viewPager;
//    @InjectView(R.id.mTabLayout)
//    TabLayout mTabLayout;
    private TextView search;
    private ImageView record;
    private Toolbar toolbar_actionbar;
    //    private TabLayout mTabLayout;
    private ViewPager viewPager;
    private MagicIndicator magic_indicator;
    private ArrayList<Fragment> fragments;
    private int line_width;

    private static ViewPager rootviewPager;
    private AnimationDrawable background;
    private ImageView iv_next_tab;
    private ProgressBar pb_loading;
    private TextView tv_nowifi;
    private boolean isClick = true;

    public static ViewPager getRootViewpager() {
        return rootviewPager;
    }

    private String[] tabTitle = {" 推荐 ", " 创业 ", " 职场 ", " 新趋势 ", " 排行榜 ", "测试一页面", "测试二页面", "测试三页面"};
    //    private List<String> mDataList = Arrays.asList(tabTitle);
    private List<String> mDataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_recommend_new, container, false);
        initViews(view);
//        ButterKnife.inject(this, view);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
                i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, SearchFragment.class);
                startActivity(i);
            }
        });
        rootviewPager = viewPager;
//        initMenuTab();
        initRecord();
//        initData();
        getTagsInfo();

        tv_nowifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonHelper.checkNetWifi(getActivity())) {
                    if(isClick) {
                        getTagsInfo();
                        isClick = false;
                    }
                }
//                Log.e("TAG", "1212133213");
            }
        });

        return view;
    }

    //获取导航栏字段
    private void getTagsInfo() {
        if (!CommonHelper.checkNetWifi(getActivity())) {
            pb_loading.setVisibility(View.GONE);
            tv_nowifi.setVisibility(View.VISIBLE);
        }
        GetRequest<RecommendBean> request = new GetRequest<>(
                UmiwiAPI.VIDEO_TUIJIAN, GsonParser.class, RecommendBean.class, new AbstractRequest.Listener<RecommendBean>() {
            @Override
            public void onResult(AbstractRequest<RecommendBean> request, RecommendBean recommendBean) {
                ArrayList<RecommendBean.RBean.MenuBean> menu = recommendBean.getR().getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    String catname = menu.get(i).getCatname();
                    mDataList.add(catname);
//                    Log.e("TAG", "catname=" + catname);
                }
                if(mDataList != null) {
                    initData();
                }
                pb_loading.setVisibility(View.GONE);
                tv_nowifi.setVisibility(View.GONE);
            }

            @Override
            public void onError(AbstractRequest<RecommendBean> requet, int statusCode, String body) {

            }
        });
        request.go();

    }

    private void initViews(View view) {
        tv_nowifi = (TextView) view.findViewById(R.id.tv_nowifi);
        pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);
        search = (TextView) view.findViewById(R.id.search);
        record = (ImageView) view.findViewById(R.id.record);
        toolbar_actionbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
//        mTabLayout = (TabLayout) view.findViewById(mTabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        magic_indicator = (MagicIndicator) view.findViewById(R.id.magic_indicator);
        iv_next_tab = (ImageView) view.findViewById(R.id.iv_next_tab);
    }

    private void initData() {
//        for (int i = 0; i < tabTitle.length;i ++) {
//            mTabLayout.addTab(mTabLayout.newTab().setText(tabTitle[i]));
//        }
//        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        mTabLayout.setTabTextColors(Color.BLACK,getResources().getColor(R.color.main_color));
//        mTabLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                setIndicator(mTabLayout,1,1);
//          }
//        });
        if(mDataList.size() <= 5) {
            iv_next_tab.setVisibility(View.GONE);
        }

        mDataList.add("测试1");
        mDataList.add("测试2");
        mDataList.add("测试3");
        viewPager.setAdapter(new MyFragmentStatePagerAdapter(getChildFragmentManager(), mDataList));
//        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        viewPager.setOffscreenPageLimit(8);//4 comment by wangsan
        magic_indicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        //均分tag
//        commonNavigator.setAdjustMode(true);
        commonNavigator.setScrollPivotX(0.5f);



        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                if (mDataList.get(index).length() <= 3) {
                    simplePagerTitleView.setText(" " + mDataList.get(index) + " ");
                } else {
                    simplePagerTitleView.setText(mDataList.get(index));
                }
                simplePagerTitleView.setNormalColor(Color.BLACK);
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.main_color));
                simplePagerTitleView.setTextSize(15);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setLineHeight(UIUtil.dip2px(context, 2));
                linePagerIndicator.setColors(getResources().getColor(R.color.main_color));

                return linePagerIndicator;
            }
        });
        magic_indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magic_indicator, viewPager);
        iv_next_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });
    }

    @Override
    public void onResume() {
//        Log.e("TAG", "首页里的onResume()");
        super.onResume();
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
//                                Log.e("TAG", "UmiwiApplication.mainActivity.herfUrl=" + UmiwiApplication.mainActivity.herfUrl);
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
        });
    }

    //设置TabLayout下划线宽度
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    /**
     * 初始化导航菜单了
     **/
    void initMenuTab() {
//        ViewPropertyAnimator.animate(tabRecommend).scaleX(1.1f).setDuration(0);
//        ViewPropertyAnimator.animate(tabExpert).scaleY(1.1f).setDuration(0);
//        ViewPropertyAnimator.animate(tabColumn).scaleX(1.1f).setDuration(0);
//        ViewPropertyAnimator.animate(tabAudio).scaleY(1.1f).setDuration(0);
//        ViewPropertyAnimator.animate(tabVideo).scaleX(1.1f).setDuration(0);
//        ViewPropertyAnimator.animate(tabOldYoumi).scaleY(1.1f).setDuration(0);
//        ViewPropertyAnimator.animate(tab_startbus).scaleX(1.1f).setDuration(0);
//        ViewPropertyAnimator.animate(tab_workplace).scaleX(1.1f).setDuration(0);
//        ViewPropertyAnimator.animate(tab_newtende).scaleX(1.1f).setDuration(0);
//        ViewPropertyAnimator.animate(tab_rank).scaleX(1.1f).setDuration(0);

        fragments = new ArrayList<Fragment>();
        fragments.add(new RecommendFragment());
//        fragments.add(new ExpertFragment());
//        fragments.add(new LecturerListFragment());
//        fragments.add(new ColumnFragment());
//        fragments.add(new HomeAudioFragment());
//        fragments.add(new HomeVideoFragment());
//        fragments.add(new OldYoumiFragment());
        fragments.add(new StartBusinessFragment());
        fragments.add(new WorkPlaceFragment());
        fragments.add(new NewTendencyFragment());
        fragments.add(new HotListFragment());

        line_width = getActivity().getWindowManager().getDefaultDisplay().getWidth()
                / fragments.size();
//        line.getLayoutParams().width = line_width-90;
//        line.requestLayout();
        viewPager.setOffscreenPageLimit(4);
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
//                changeState(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
//                float tagerX = arg0 * line_width + arg2 / fragments.size();
//                ViewPropertyAnimator.animate(line).translationX(tagerX+45)
//                        .setDuration(0);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
//        tabRecommend.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                viewPager.setCurrentItem(0);
//
//            }
//        });

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
//        tabAudio.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                viewPager.setCurrentItem(1);
//
//            }
//        });
//        tabVideo.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                viewPager.setCurrentItem(2);
//
//            }
//        });
//        tabOldYoumi.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                viewPager.setCurrentItem(3);
//
//            }
//        });
//        tab_startbus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewPager.setCurrentItem(1);
//            }
//        });
//        tab_workplace.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewPager.setCurrentItem(2);
//            }
//        });
//        tab_newtende.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewPager.setCurrentItem(3);
//            }
//        });
//        tab_rank.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewPager.setCurrentItem(4);
//            }
//        });
    }


//    private void changeState(int position) {
//        if (position == 0) {
//            tabRecommend.setTextColor(getResources().getColor(R.color.main_color));
//
////            tabExpert.setTextColor(getResources().getColor(R.color.black));
////            tabColumn.setTextColor(getResources().getColor(R.color.black));
////            tabAudio.setTextColor(getResources().getColor(R.color.black));
////            tabVideo.setTextColor(getResources().getColor(R.color.black));
////            tabOldYoumi.setTextColor(getResources().getColor(R.color.black));
//
//            tab_startbus.setTextColor(getResources().getColor(R.color.black));
//            tab_workplace.setTextColor(getResources().getColor(R.color.black));
//            tab_newtende.setTextColor(getResources().getColor(R.color.black));
//            tab_rank.setTextColor(getResources().getColor(R.color.black));
//
//
//            ViewPropertyAnimator.animate(tabRecommend).scaleX(1.1f).setDuration(200);
//            ViewPropertyAnimator.animate(tabRecommend).scaleY(1.1f).setDuration(200);
//
////            ViewPropertyAnimator.animate(tabExpert).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabExpert).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabColumn).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabColumn).scaleY(1.0f).setDuration(200);
//
////            ViewPropertyAnimator.animate(tabAudio).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabAudio).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabVideo).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabVideo).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabOldYoumi).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabOldYoumi).scaleY(1.0f).setDuration(200);
//
//            ViewPropertyAnimator.animate(tab_startbus).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_startbus).scaleY(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_workplace).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_workplace).scaleY(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_newtende).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_newtende).scaleY(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_rank).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_rank).scaleY(1.0f).setDuration(200);
//
//
//        } else if (position==1){
//            tabRecommend.setTextColor(getResources().getColor(R.color.black));
//
////            tabExpert.setTextColor(getResources().getColor(R.color.main_color));
////            tabColumn.setTextColor(getResources().getColor(R.color.black));
////            tabAudio.setTextColor(getResources().getColor(R.color.main_color));
////            tabVideo.setTextColor(getResources().getColor(R.color.black));
////            tabOldYoumi.setTextColor(getResources().getColor(R.color.black));
//
//            tab_startbus.setTextColor(getResources().getColor(R.color.main_color));
//            tab_workplace.setTextColor(getResources().getColor(R.color.black));
//            tab_newtende.setTextColor(getResources().getColor(R.color.black));
//            tab_rank.setTextColor(getResources().getColor(R.color.black));
//
//            ViewPropertyAnimator.animate(tabRecommend).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tabRecommend).scaleY(1.0f).setDuration(200);
//
////            ViewPropertyAnimator.animate(tabExpert).scaleX(1.1f).setDuration(200);
////            ViewPropertyAnimator.animate(tabExpert).scaleY(1.1f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabColumn).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabColumn).scaleY(1.0f).setDuration(200);
//
////            ViewPropertyAnimator.animate(tabAudio).scaleX(1.1f).setDuration(200);
////            ViewPropertyAnimator.animate(tabAudio).scaleY(1.1f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabVideo).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabVideo).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabOldYoumi).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabOldYoumi).scaleY(1.0f).setDuration(200);
//
//            ViewPropertyAnimator.animate(tab_startbus).scaleX(1.1f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_startbus).scaleY(1.1f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_workplace).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_workplace).scaleY(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_newtende).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_newtende).scaleY(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_rank).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_rank).scaleY(1.0f).setDuration(200);
//        }
//        else if (position==2){
//            tabRecommend.setTextColor(getResources().getColor(R.color.black));
//
////            tabExpert.setTextColor(getResources().getColor(R.color.black));
////            tabColumn.setTextColor(getResources().getColor(R.color.main_color));
////            tabAudio.setTextColor(getResources().getColor(R.color.black));
////            tabVideo.setTextColor(getResources().getColor(R.color.main_color));
////            tabOldYoumi.setTextColor(getResources().getColor(R.color.black));
//
//            tab_startbus.setTextColor(getResources().getColor(R.color.black));
//            tab_workplace.setTextColor(getResources().getColor(R.color.main_color));
//            tab_newtende.setTextColor(getResources().getColor(R.color.black));
//            tab_rank.setTextColor(getResources().getColor(R.color.black));
//
//
//
//            ViewPropertyAnimator.animate(tabRecommend).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tabRecommend).scaleY(1.0f).setDuration(200);
//
////            ViewPropertyAnimator.animate(tabExpert).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabExpert).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabColumn).scaleX(1.1f).setDuration(200);
////            ViewPropertyAnimator.animate(tabColumn).scaleY(1.1f).setDuration(200);
//
////            ViewPropertyAnimator.animate(tabAudio).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabAudio).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabVideo).scaleX(1.1f).setDuration(200);
////            ViewPropertyAnimator.animate(tabVideo).scaleY(1.1f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabOldYoumi).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabOldYoumi).scaleY(1.0f).setDuration(200);
//
//            ViewPropertyAnimator.animate(tab_startbus).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_startbus).scaleY(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_workplace).scaleX(1.1f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_workplace).scaleY(1.1f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_newtende).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_newtende).scaleY(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_rank).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_rank).scaleY(1.0f).setDuration(200);
//        }
//        else if (position==3){
//            tabRecommend.setTextColor(getResources().getColor(R.color.black));
//
////            tabExpert.setTextColor(getResources().getColor(R.color.black));
////            tabColumn.setTextColor(getResources().getColor(R.color.black));
////            tabAudio.setTextColor(getResources().getColor(R.color.black));
////            tabVideo.setTextColor(getResources().getColor(R.color.black));
////            tabOldYoumi.setTextColor(getResources().getColor(R.color.main_color));
//
//            tab_startbus.setTextColor(getResources().getColor(R.color.black));
//            tab_workplace.setTextColor(getResources().getColor(R.color.black));
//            tab_newtende.setTextColor(getResources().getColor(R.color.main_color));
//            tab_rank.setTextColor(getResources().getColor(R.color.black));
//
//            ViewPropertyAnimator.animate(tabRecommend).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tabRecommend).scaleY(1.0f).setDuration(200);
//
////            ViewPropertyAnimator.animate(tabExpert).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabExpert).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabColumn).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabColumn).scaleY(1.0f).setDuration(200);
//
////            ViewPropertyAnimator.animate(tabAudio).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabAudio).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabVideo).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabVideo).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabOldYoumi).scaleX(1.1f).setDuration(200);
////            ViewPropertyAnimator.animate(tabOldYoumi).scaleY(1.1f).setDuration(200);
//
//            ViewPropertyAnimator.animate(tab_startbus).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_startbus).scaleY(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_workplace).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_workplace).scaleY(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_newtende).scaleX(1.1f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_newtende).scaleY(1.1f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_rank).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_rank).scaleY(1.0f).setDuration(200);
//        }else if (position==4) {
//            tabRecommend.setTextColor(getResources().getColor(R.color.black));
//
////            tabExpert.setTextColor(getResources().getColor(R.color.black));
////            tabColumn.setTextColor(getResources().getColor(R.color.black));
////            tabAudio.setTextColor(getResources().getColor(R.color.black));
////            tabVideo.setTextColor(getResources().getColor(R.color.black));
////            tabOldYoumi.setTextColor(getResources().getColor(R.color.black));
//
//            tab_startbus.setTextColor(getResources().getColor(R.color.black));
//            tab_workplace.setTextColor(getResources().getColor(R.color.black));
//            tab_newtende.setTextColor(getResources().getColor(R.color.black));
//            tab_rank.setTextColor(getResources().getColor(R.color.main_color));
//
//            ViewPropertyAnimator.animate(tabRecommend).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tabRecommend).scaleY(1.0f).setDuration(200);
//
////            ViewPropertyAnimator.animate(tabExpert).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabExpert).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabColumn).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabColumn).scaleY(1.0f).setDuration(200);
//
////            ViewPropertyAnimator.animate(tabAudio).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabAudio).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabVideo).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabVideo).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabOldYoumi).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabOldYoumi).scaleY(1.0f).setDuration(200);
//
//            ViewPropertyAnimator.animate(tab_startbus).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_startbus).scaleY(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_workplace).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_workplace).scaleY(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_newtende).scaleX(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_newtende).scaleY(1.0f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_rank).scaleX(1.1f).setDuration(200);
//            ViewPropertyAnimator.animate(tab_rank).scaleY(1.1f).setDuration(200);
//        }
////        }else if (position==5){
////            tabRecommend.setTextColor(getResources().getColor(R.color.black));
//////
//////            tabExpert.setTextColor(getResources().getColor(R.color.black));
//////            tabColumn.setTextColor(getResources().getColor(R.color.black));
////            tabAudio.setTextColor(getResources().getColor(R.color.black));
////            tabVideo.setTextColor(getResources().getColor(R.color.black));
////            tabOldYoumi.setTextColor(getResources().getColor(R.color.black));
////
////            tab_startbus.setTextColor(getResources().getColor(R.color.black));
////            tab_workplace.setTextColor(getResources().getColor(R.color.main_color));
////            tab_newtende.setTextColor(getResources().getColor(R.color.black));
////            tab_rank.setTextColor(getResources().getColor(R.color.black));
////
////            ViewPropertyAnimator.animate(tabRecommend).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabRecommend).scaleY(1.0f).setDuration(200);
////
//////            ViewPropertyAnimator.animate(tabExpert).scaleX(1.0f).setDuration(200);
//////            ViewPropertyAnimator.animate(tabExpert).scaleY(1.0f).setDuration(200);
//////
//////            ViewPropertyAnimator.animate(tabColumn).scaleX(1.0f).setDuration(200);
//////            ViewPropertyAnimator.animate(tabColumn).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabAudio).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabAudio).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabVideo).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabVideo).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabOldYoumi).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabOldYoumi).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tab_startbus).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tab_startbus).scaleY(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tab_workplace).scaleX(1.1f).setDuration(200);
////            ViewPropertyAnimator.animate(tab_workplace).scaleY(1.1f).setDuration(200);
////            ViewPropertyAnimator.animate(tab_newtende).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tab_newtende).scaleY(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tab_rank).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tab_rank).scaleY(1.0f).setDuration(200);
////
////        }else if(position == 6) {
////            tabRecommend.setTextColor(getResources().getColor(R.color.black));
//////
//////            tabExpert.setTextColor(getResources().getColor(R.color.black));
//////            tabColumn.setTextColor(getResources().getColor(R.color.black));
////            tabAudio.setTextColor(getResources().getColor(R.color.black));
////            tabVideo.setTextColor(getResources().getColor(R.color.black));
////            tabOldYoumi.setTextColor(getResources().getColor(R.color.black));
////
////            tab_startbus.setTextColor(getResources().getColor(R.color.black));
////            tab_workplace.setTextColor(getResources().getColor(R.color.black));
////            tab_newtende.setTextColor(getResources().getColor(R.color.main_color));
////            tab_rank.setTextColor(getResources().getColor(R.color.black));
////
////            ViewPropertyAnimator.animate(tabRecommend).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabRecommend).scaleY(1.0f).setDuration(200);
////
//////            ViewPropertyAnimator.animate(tabExpert).scaleX(1.0f).setDuration(200);
//////            ViewPropertyAnimator.animate(tabExpert).scaleY(1.0f).setDuration(200);
//////
//////            ViewPropertyAnimator.animate(tabColumn).scaleX(1.0f).setDuration(200);
//////            ViewPropertyAnimator.animate(tabColumn).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabAudio).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabAudio).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabVideo).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabVideo).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabOldYoumi).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabOldYoumi).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tab_startbus).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tab_startbus).scaleY(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tab_workplace).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tab_workplace).scaleY(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tab_newtende).scaleX(1.1f).setDuration(200);
////            ViewPropertyAnimator.animate(tab_newtende).scaleY(1.1f).setDuration(200);
////            ViewPropertyAnimator.animate(tab_rank).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tab_rank).scaleY(1.0f).setDuration(200);
////        }else if(position == 7) {
////            tabRecommend.setTextColor(getResources().getColor(R.color.black));
//////
//////            tabExpert.setTextColor(getResources().getColor(R.color.black));
//////            tabColumn.setTextColor(getResources().getColor(R.color.black));
////            tabAudio.setTextColor(getResources().getColor(R.color.black));
////            tabVideo.setTextColor(getResources().getColor(R.color.black));
////            tabOldYoumi.setTextColor(getResources().getColor(R.color.black));
////
////            tab_startbus.setTextColor(getResources().getColor(R.color.black));
////            tab_workplace.setTextColor(getResources().getColor(R.color.black));
////            tab_newtende.setTextColor(getResources().getColor(R.color.black));
////            tab_rank.setTextColor(getResources().getColor(R.color.main_color));
////
////            ViewPropertyAnimator.animate(tabRecommend).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabRecommend).scaleY(1.0f).setDuration(200);
////
//////            ViewPropertyAnimator.animate(tabExpert).scaleX(1.0f).setDuration(200);
//////            ViewPropertyAnimator.animate(tabExpert).scaleY(1.0f).setDuration(200);
//////
//////            ViewPropertyAnimator.animate(tabColumn).scaleX(1.0f).setDuration(200);
//////            ViewPropertyAnimator.animate(tabColumn).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabAudio).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabAudio).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabVideo).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tabVideo).scaleY(1.0f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tabOldYoumi).scaleX(1.1f).setDuration(200);
////            ViewPropertyAnimator.animate(tabOldYoumi).scaleY(1.1f).setDuration(200);
////
////            ViewPropertyAnimator.animate(tab_startbus).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tab_startbus).scaleY(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tab_workplace).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tab_workplace).scaleY(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tab_newtende).scaleX(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tab_newtende).scaleY(1.0f).setDuration(200);
////            ViewPropertyAnimator.animate(tab_rank).scaleX(1.1f).setDuration(200);
////            ViewPropertyAnimator.animate(tab_rank).scaleY(1.1f).setDuration(200);
////        }
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ButterKnife.reset(this);
    }
}
