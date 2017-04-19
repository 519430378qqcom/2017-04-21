package com.umiwi.ui.fragment.home.updatehome.indexfragment;

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
import com.umiwi.ui.main.BaseConstantFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by shangshuaibo on 2017/3/12 14:12
 */
public class HotListFragment extends BaseConstantFragment {
//    @InjectView(R.id.iv_back)
//    ImageView iv_back;
//    @InjectView(R.id.tabs_order)
//    TabLayout tabsOrder;
    @InjectView(R.id.ab_tab_hottop)
    TextView ab_tab_hottop;
    @InjectView(R.id.ab_tab_saletop)
    TextView ab_tab_saletop;
    @InjectView(R.id.ab_tab_threadtop)
    TextView ab_tab_threadtop;
    @InjectView(R.id.ab_line)
    View ab_line;
    @InjectView(R.id.ab_viewPager)
    ViewPager ab_viewPager;

    private ArrayList<String> mTitleList = new ArrayList<>();
    Fragment theForwardestListFragment, theHitListFragment, theHotListFragment;
    private ArrayList<Fragment> fragments;
    private int line_width;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bangdan, null);
        ButterKnife.inject(this, view);
        initTabLayout();
//        cutTheme(0);
//        iv_back.setOnClickListener(new BackOnClickListener());
        return view;
    }

    private void initTabLayout() {
        ViewPropertyAnimator.animate(ab_tab_hottop).scaleX(1.0f).setDuration(0);
        ViewPropertyAnimator.animate(ab_tab_saletop).scaleX(1.0f).setDuration(0);
        ViewPropertyAnimator.animate(ab_tab_threadtop).scaleX(1.0f).setDuration(0);

        fragments = new ArrayList<Fragment>();
        fragments.add(new TheHitListFragment());
        fragments.add(new TheHotListFragment());
        fragments.add(new TheForwardestListFragment());

        line_width = getActivity().getWindowManager().getDefaultDisplay().getWidth()/fragments.size();
        ab_line.getLayoutParams().width = line_width-120;
        ab_line.requestLayout();
        ab_viewPager.setOffscreenPageLimit(2);
        ab_viewPager.setCurrentItem(0);
        ab_viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });

        ab_viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                float tagerX = position * line_width + positionOffsetPixels / fragments.size();
                ViewPropertyAnimator.animate(ab_line).translationX(tagerX+60).setDuration(0);

            }

            @Override
            public void onPageSelected(int position) {
                changeState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        ab_tab_hottop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ab_viewPager.setCurrentItem(0);
            }
        });
        ab_tab_saletop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ab_viewPager.setCurrentItem(1);
            }
        });
        ab_tab_threadtop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ab_viewPager.setCurrentItem(2);
            }
        });
    }
    private void changeState(int position) {
        if (position == 0) {
            ab_tab_hottop.setTextColor(getResources().getColor(R.color.main_color));
            ab_tab_saletop.setTextColor(getResources().getColor(R.color.black));
            ab_tab_threadtop.setTextColor(getResources().getColor(R.color.black));

            ViewPropertyAnimator.animate(ab_tab_hottop).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(ab_tab_hottop).scaleY(1.1f).setDuration(200);


            ViewPropertyAnimator.animate(ab_tab_saletop).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(ab_tab_saletop).scaleY(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(ab_tab_threadtop).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(ab_tab_threadtop).scaleY(1.0f).setDuration(200);

        } else if (position == 1) {

            ab_tab_hottop.setTextColor(getResources().getColor(R.color.black));
            ab_tab_saletop.setTextColor(getResources().getColor(R.color.main_color));
            ab_tab_threadtop.setTextColor(getResources().getColor(R.color.black));


            ViewPropertyAnimator.animate(ab_tab_hottop).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(ab_tab_hottop).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(ab_tab_saletop).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(ab_tab_saletop).scaleY(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(ab_tab_threadtop).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(ab_tab_threadtop).scaleY(1.0f).setDuration(200);
        } else if (position == 2) {

            ab_tab_hottop.setTextColor(getResources().getColor(R.color.black));
            ab_tab_saletop.setTextColor(getResources().getColor(R.color.black));
            ab_tab_threadtop.setTextColor(getResources().getColor(R.color.main_color));


            ViewPropertyAnimator.animate(ab_tab_hottop).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(ab_tab_hottop).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(ab_tab_saletop).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(ab_tab_saletop).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(ab_tab_threadtop).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(ab_tab_threadtop).scaleY(1.1f).setDuration(200);
        }
    }
    //初始化 TabLayout
//    private void initTabLayout() {
//        mTitleList.add("热播榜");
//        mTitleList.add("热销榜");
//        mTitleList.add("热评榜");
//        //设置tab模式，当前为系统默认模式
//        tabsOrder.setTabMode(TabLayout.MODE_SCROLLABLE);
//        tabsOrder.setTabMode(TabLayout.MODE_FIXED);
//        //添加tab选项卡
//        tabsOrder.addTab(tabsOrder.newTab().setText(mTitleList.get(0)));
//        tabsOrder.addTab(tabsOrder.newTab().setText(mTitleList.get(1)));
//        tabsOrder.addTab(tabsOrder.newTab().setText(mTitleList.get(2)));
//        tabsOrder.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                int position = tab.getPosition();
//                cutTheme(position);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//    }

//    private void cutTheme(int position) {
//        FragmentManager fm = getFragmentManager();
//        // 开启Fragment事务
//        FragmentTransaction transaction = fm.beginTransaction();
//        hideFragments(transaction);
//        switch (position) {
//            case 0:
//                if (theHitListFragment == null) {
//                    theHitListFragment = new TheHitListFragment();
//                    transaction.add(R.id.fl_content, theHitListFragment);
//                } else {
//                    transaction.show(theHitListFragment);
//                }
//
//                break;
//            case 1:
//                if (theHotListFragment == null) {
//                    theHotListFragment = new TheHotListFragment();
//                    transaction.add(R.id.fl_content, theHotListFragment);
//                } else {
//                    transaction.show(theHotListFragment);
//                }
//
//                break;
//            case 2:
//                if (theForwardestListFragment == null) {
//                    theForwardestListFragment = new TheForwardestListFragment();
//                    transaction.add(R.id.fl_content, theForwardestListFragment);
//                } else {
//                    transaction.show(theForwardestListFragment);
//                }
//                break;
//
//        }
//        transaction.commitAllowingStateLoss();
//    }


//    public void hideFragments(FragmentTransaction ft) {
//        if (theHitListFragment != null)
//            ft.hide(theHitListFragment);
//        if (theHotListFragment != null)
//            ft.hide(theHotListFragment);
//        if (theForwardestListFragment != null)
//            ft.hide(theForwardestListFragment);
//    }

//    class BackOnClickListener implements View.OnClickListener {
//
//        @Override
//        public void onClick(View view) {
//            getActivity().finish();
//        }
//    }
}
