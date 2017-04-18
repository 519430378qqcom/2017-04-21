package com.umiwi.ui.adapter.updateadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.umiwi.ui.fragment.home.updatehome.indexfragment.HotListFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.NewTendencyFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.RecommendFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.StartBusinessFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.WorkPlaceFragment;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    private List<String> tabTitle;
    public MyFragmentStatePagerAdapter(FragmentManager fm, List<String> tabTitle) {
        super(fm);
        this.tabTitle = tabTitle;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new RecommendFragment();
            case 1:
                return new StartBusinessFragment();
            case 2:
                return new WorkPlaceFragment();
            case 3:
                return new NewTendencyFragment();
            case 4:
                return new HotListFragment();
//            case 5:
//                return new VPFragment5();
//            case 6:
//                return new VPFragment6();
//            case 7:
//                return new VPFragment7();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabTitle.size();
    }
}
