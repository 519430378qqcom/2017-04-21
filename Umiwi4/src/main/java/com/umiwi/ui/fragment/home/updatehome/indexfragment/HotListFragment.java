package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.umiwi.ui.R;
import com.umiwi.ui.main.BaseConstantFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by shangshuaibo on 2017/3/12 14:12
 */
public class HotListFragment extends BaseConstantFragment {
    @InjectView(R.id.iv_back)
    ImageView iv_back;
    @InjectView(R.id.tabs_order)
    TabLayout tabsOrder;

    private ArrayList<String> mTitleList = new ArrayList<>();
    Fragment theForwardestListFragment, theHitListFragment, theHotListFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bangdan, null);
        ButterKnife.inject(this, view);
        initTabLayout();
        cutTheme(0);
        iv_back.setOnClickListener(new BackOnClickListener());
        return view;
    }


    //初始化 TabLayout
    private void initTabLayout() {
        mTitleList.add("热播榜");
        mTitleList.add("热销榜");
        mTitleList.add("热评榜");
        //设置tab模式，当前为系统默认模式
        tabsOrder.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabsOrder.setTabMode(TabLayout.MODE_FIXED);
        //添加tab选项卡
        tabsOrder.addTab(tabsOrder.newTab().setText(mTitleList.get(0)));
        tabsOrder.addTab(tabsOrder.newTab().setText(mTitleList.get(1)));
        tabsOrder.addTab(tabsOrder.newTab().setText(mTitleList.get(2)));
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
        switch (position) {
            case 0:
                if (theHitListFragment == null) {
                    theHitListFragment = new TheHitListFragment();
                    transaction.add(R.id.fl_content, theHitListFragment);
                } else {
                    transaction.show(theHitListFragment);
                }

                break;
            case 1:
                if (theHotListFragment == null) {
                    theHotListFragment = new TheHotListFragment();
                    transaction.add(R.id.fl_content, theHotListFragment);
                } else {
                    transaction.show(theHotListFragment);
                }

                break;
            case 2:
                if (theForwardestListFragment == null) {
                    theForwardestListFragment = new TheForwardestListFragment();
                    transaction.add(R.id.fl_content, theForwardestListFragment);
                } else {
                    transaction.show(theForwardestListFragment);
                }
                break;

        }
        transaction.commitAllowingStateLoss();
    }


    public void hideFragments(FragmentTransaction ft) {
        if (theHitListFragment != null)
            ft.hide(theHitListFragment);
        if (theHotListFragment != null)
            ft.hide(theHotListFragment);
        if (theForwardestListFragment != null)
            ft.hide(theForwardestListFragment);
    }

    class BackOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            getActivity().finish();
        }
    }
}
