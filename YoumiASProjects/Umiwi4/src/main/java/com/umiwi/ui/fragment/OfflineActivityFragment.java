package com.umiwi.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.main.BaseConstantFragment;

/**
 * Created by tangxiyong on 15/8/13.
 */
public class OfflineActivityFragment extends BaseConstantFragment {
    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_smarttab_toolbar_overly, null);

        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        setSupportActionBarAndToolbarTitle(mActionBarToolbar, "线下活动");

        SmartTabLayout viewPagerTab = (SmartTabLayout) view.findViewById(R.id.viewpagertab);
        ViewPager rootPager = (ViewPager) view.findViewById(R.id.viewpager);

        Bundle allBundle = new Bundle();
        allBundle.putString(OfflineActivityListFragment.KEY_INPROGRESS, "0");

        Bundle goingBundle = new Bundle();
        goingBundle.putString(OfflineActivityListFragment.KEY_INPROGRESS, "1");

        Bundle endBundle = new Bundle();
        endBundle.putString(OfflineActivityListFragment.KEY_INPROGRESS, "2");

        FragmentPagerItems pages = new FragmentPagerItems(getActivity());
        pages.add(FragmentPagerItem.of("全部", OfflineActivityListFragment.class, allBundle));
        pages.add(FragmentPagerItem.of("进行中", OfflineActivityListFragment.class, goingBundle));
        pages.add(FragmentPagerItem.of("已结束", OfflineActivityListFragment.class, endBundle));

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getChildFragmentManager(), pages);
        rootPager.setOffscreenPageLimit(2);
        rootPager.setAdapter(adapter);
        viewPagerTab.setViewPager(rootPager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(fragmentName);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(fragmentName);
    }
}
