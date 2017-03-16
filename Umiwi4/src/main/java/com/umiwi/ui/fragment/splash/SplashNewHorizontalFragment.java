package com.umiwi.ui.fragment.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.HomeMainActivity;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.LoginUtil;
import com.umiwi.ui.view.CirclePageIndicator;

import java.util.ArrayList;

import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.util.PreferenceUtils;

public class SplashNewHorizontalFragment extends BaseFragment {

	private CirclePageIndicator mIndicator;
	private ViewPager mViewPager;
	private GuideAdapter mAdapter;
	private ArrayList<View> views;
	private static final int[] images = { R.drawable.newguide_splash_01,
			R.drawable.newguide_splash_02, R.drawable.newguide_splash_03,R.drawable.newguide_splash_04};

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.guide_view, null);
		YoumiRoomUserManager.getInstance().logout();
		mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
		mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
		views = new ArrayList<View>();
		mAdapter = new GuideAdapter(views);
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);

		for (int i = 0; i < images.length; i++) {
			ImageView iv = new ImageView(getActivity());
			iv.setScaleType(ScaleType.FIT_CENTER);
			iv.setLayoutParams(mParams);
			iv.setImageResource(images[i]);
			views.add(iv);
		}

		view.findViewById(R.id.login).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(), HomeMainActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						startActivity(intent);

						LoginUtil.getInstance().showLoginView(getActivity());
						PreferenceUtils.setPrefBoolean(getActivity(), "isCanShowGift", false);

						getActivity().finish();
					}
				});
		view.findViewById(R.id.gohome).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						PreferenceUtils
								.setPrefBoolean(getActivity(), "isCanShowGift", true);
						startActivity(new Intent(getActivity(), HomeMainActivity.class));
						getActivity().finish();
					}
				});
		mViewPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mViewPager);
		return view;
	}

//	OnClickListener mGoHomeAcitivityListener = new View.OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			PreferenceUtils
//					.setPrefBoolean(getActivity(), "isCanShowGift", true);
//			startActivity(new Intent(getActivity(), HomeMainActivity.class));
//			getActivity().finish();
//		}
//	};
//	OnClickListener mLoginAcitivityListener = new View.OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			Intent intent = new Intent(getActivity(), HomeMainActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//			startActivity(intent);
//
//			LoginUtil.getInstance().showLoginView(getActivity());
//			PreferenceUtils.setPrefBoolean(getActivity(), "isCanShowGift", false);
//
//			getActivity().finish();
//		}
//	};

	public class GuideAdapter extends PagerAdapter {
		private ArrayList<View> views;

		public GuideAdapter(ArrayList<View> views) {
			this.views = views;
		}

		@Override
		public int getCount() {
			if (views != null) {
				return views.size();
			}
			return 0;
		}

		@Override
		public Object instantiateItem(View view, int position) {

			((ViewPager) view).addView(views.get(position), 0);

			return views.get(position);
		}

		@Override
		public boolean isViewFromObject(View view, Object arg1) {
			return (view == arg1);
		}

		@Override
		public void destroyItem(View view, int position, Object arg2) {
			((ViewPager) view).removeView(views.get(position));
		}
	}

}
