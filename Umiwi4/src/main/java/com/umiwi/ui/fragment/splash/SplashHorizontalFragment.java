package com.umiwi.ui.fragment.splash;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.youmi.framework.fragment.BaseFragment;

import com.umiwi.ui.R;
import com.umiwi.ui.view.CirclePageIndicator;

/**
 * @author tangxiyong
 * @version 2014年6月25日 上午10:10:56 新用户第一次启动
 */
public class SplashHorizontalFragment extends BaseFragment {
	private boolean isUpdate;

	private CategoryPagerAdapter mAdapter;
	private ArrayList<Fragment> fragments;
	private ViewPager mViewPager;

	private CirclePageIndicator mIndicator;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.splash_horizontal_layout, null);
		
		mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
		mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);

		fragments = new ArrayList<Fragment>();
		SplashHorizontalChild1Fragment child1Fragment = new SplashHorizontalChild1Fragment();
		fragments.add(child1Fragment);

		SplashHorizontalChild2Fragment child2Fragment = new SplashHorizontalChild2Fragment();
		fragments.add(child2Fragment);

		SplashHorizontalChild3Fragment child3Fragment = new SplashHorizontalChild3Fragment();
		// child3Fragment.isUpdate = isUpdate;
		fragments.add(child3Fragment);

		mAdapter = new CategoryPagerAdapter(getActivity(), getChildFragmentManager(), fragments);

		mViewPager.setAdapter(mAdapter);
		mViewPager.setOffscreenPageLimit(fragments.size());
		mViewPager.setCurrentItem(0);
		mIndicator.setViewPager(mViewPager);

		return view;
	}

	public void initData(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public class CategoryPagerAdapter extends FragmentPagerAdapter {
		private ArrayList<Fragment> fragments = null;

		public CategoryPagerAdapter(Context context, FragmentManager fm,
				ArrayList<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}
	}

}
