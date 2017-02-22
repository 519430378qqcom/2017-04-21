package com.umiwi.ui.fragment.splash;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import cn.youmi.framework.fragment.BaseFragment;

import com.umiwi.ui.R;

/**
 * @author tjie00
 * @version 2015年01月24日 引导页第一屏
 */
public class SplashHorizontalChild2Fragment extends BaseFragment {

	private FrameLayout rootLayout;

	private ImageView tipBottomImageView;


	private boolean isAnimationShowed = false;

	private WindowManager windowManager;

	private int windowWidth;
	private int windowHeight;

	private static final long COMMON_DURATION = 300;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.splash_horizontal_01_layout, null);

		rootLayout = (FrameLayout) view.findViewById(R.id.root_layout);
		ImageView tipsText = (ImageView) view.findViewById(R.id.tips_text_imageview);
		tipsText.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.horizontal_2_06));

		windowManager = getActivity().getWindowManager();
		windowWidth = windowManager.getDefaultDisplay().getWidth();
		windowHeight = windowManager.getDefaultDisplay().getHeight();


		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		if (!isAnimationShowed) {
			isAnimationShowed = true;
		}
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if(isVisibleToUser && isAnimationShowed) {
			showAnimation();
			isAnimationShowed = false;
		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	// 显示动画
	private void showAnimation() {

		earthImageView = new ImageView(getActivity());
		earthImageView.setScaleType(ScaleType.CENTER_CROP);
		earthImageView.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.horizontal_2_01));

		int businessXLocationMargin = 0;
		int businessYLocation = (1 * windowHeight) / 3;

		FrameLayout.LayoutParams bottomParam = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT,
				Gravity.CENTER_HORIZONTAL | Gravity.TOP);

		bottomParam.rightMargin = businessXLocationMargin;
		bottomParam.topMargin = businessYLocation;

		AnimationSet bottomAnimationSet = new AnimationSet(false);
		bottomAnimationSet.setInterpolator(new DecelerateInterpolator());
		bottomAnimationSet.setFillAfter(true);
		bottomAnimationSet.setAnimationListener(lightAniamtionListener);

		ScaleAnimation bottomTranslateAnimation = new ScaleAnimation(0.0f,
				1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		bottomTranslateAnimation.setDuration(COMMON_DURATION);

		bottomAnimationSet.addAnimation(bottomTranslateAnimation);

		AlphaAnimation bottomAlphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		bottomAlphaAnimation.setDuration(COMMON_DURATION);
		bottomAnimationSet.addAnimation(bottomAlphaAnimation);

		earthImageView.startAnimation(bottomAnimationSet);

		if (earthImageView.getParent() == null) {
			rootLayout.addView(earthImageView, bottomParam);
		}
	}
	
	AnimationListener lightAniamtionListener = new AnimationListener() {
		
		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			showbgAnimation1();
			showbgAnimation2();
			showbgAnimation3();
			showbgAnimation4();
		}
		
		private void showbgAnimation1() {
			ImageView tipTopImageView = new ImageView(getActivity());
			tipTopImageView.setScaleType(ScaleType.CENTER_CROP);
			tipTopImageView.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.horizontal_2_02));
			
			
			FrameLayout.LayoutParams bottomParam = 
					new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, 
							FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM|Gravity.RIGHT);
			bottomParam.bottomMargin = windowHeight / 4;
			bottomParam.rightMargin = windowWidth / 6;
			
			
			
			AnimationSet bottomAnimationSet = new AnimationSet(true);
			bottomAnimationSet.setInterpolator(new DecelerateInterpolator());
			bottomAnimationSet.setFillAfter(true);
//			bottomAnimationSet.setAnimationListener(dispathLeavesAnimationListener);
			
			ScaleAnimation bottomTranslateAnimation = new ScaleAnimation(0.0f,
					1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			bottomTranslateAnimation.setDuration(COMMON_DURATION);
			
			bottomAnimationSet.addAnimation(bottomTranslateAnimation);
			
			AlphaAnimation bottomAlphaAnimation = new AlphaAnimation(0.0f, 1.0f);
			bottomAlphaAnimation.setDuration(COMMON_DURATION);
			bottomAnimationSet.addAnimation(bottomAlphaAnimation);
			
			tipTopImageView.startAnimation(bottomAnimationSet);
			
			if (tipTopImageView.getParent() == null) {
				rootLayout.addView(tipTopImageView, bottomParam);
			}
			
		}
		private void showbgAnimation2() {
			ImageView tipTopImageView = new ImageView(getActivity());
			tipTopImageView.setScaleType(ScaleType.CENTER_CROP);
			tipTopImageView.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.horizontal_2_04));
			
			
			FrameLayout.LayoutParams bottomParam = 
					new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, 
							FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.TOP|Gravity.CENTER_HORIZONTAL);
			bottomParam.topMargin = (2*windowHeight) / 7;
			bottomParam.rightMargin = windowWidth / 6;
			
			
			
			AnimationSet bottomAnimationSet = new AnimationSet(true);
			bottomAnimationSet.setInterpolator(new DecelerateInterpolator());
			bottomAnimationSet.setFillAfter(true);
//			bottomAnimationSet.setAnimationListener(dispathLeavesAnimationListener);
			
			ScaleAnimation bottomTranslateAnimation = new ScaleAnimation(0.0f,
					1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			bottomTranslateAnimation.setDuration(COMMON_DURATION);
			
			bottomAnimationSet.addAnimation(bottomTranslateAnimation);
			
			AlphaAnimation bottomAlphaAnimation = new AlphaAnimation(0.0f, 1.0f);
			bottomAlphaAnimation.setDuration(COMMON_DURATION);
			bottomAnimationSet.addAnimation(bottomAlphaAnimation);
			
			tipTopImageView.startAnimation(bottomAnimationSet);
			
			if (tipTopImageView.getParent() == null) {
				rootLayout.addView(tipTopImageView, bottomParam);
			}
			
		}
		private void showbgAnimation3() {
			ImageView tipTopImageView = new ImageView(getActivity());
			tipTopImageView.setScaleType(ScaleType.CENTER_CROP);
			tipTopImageView.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.horizontal_2_03));
			
			
			FrameLayout.LayoutParams bottomParam = 
					new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, 
							FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.TOP|Gravity.RIGHT);
			bottomParam.topMargin = windowHeight / 4;
			bottomParam.rightMargin = windowWidth / 5;
			
			
			
			AnimationSet bottomAnimationSet = new AnimationSet(true);
			bottomAnimationSet.setInterpolator(new DecelerateInterpolator());
			bottomAnimationSet.setFillAfter(true);
//			bottomAnimationSet.setAnimationListener(dispathLeavesAnimationListener);
			
			ScaleAnimation bottomTranslateAnimation = new ScaleAnimation(0.0f,
					1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			bottomTranslateAnimation.setDuration(COMMON_DURATION);
			
			bottomAnimationSet.addAnimation(bottomTranslateAnimation);
			
			AlphaAnimation bottomAlphaAnimation = new AlphaAnimation(0.0f, 1.0f);
			bottomAlphaAnimation.setDuration(COMMON_DURATION);
			bottomAnimationSet.addAnimation(bottomAlphaAnimation);
			
			tipTopImageView.startAnimation(bottomAnimationSet);
			
			if (tipTopImageView.getParent() == null) {
				rootLayout.addView(tipTopImageView, bottomParam);
			}
			
		}
		private void showbgAnimation4() {
			ImageView tipTopImageView = new ImageView(getActivity());
			tipTopImageView.setScaleType(ScaleType.CENTER_CROP);
			tipTopImageView.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.horizontal_2_05));
			
			
			FrameLayout.LayoutParams bottomParam = 
					new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, 
							FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.TOP|Gravity.RIGHT);
			bottomParam.topMargin = windowHeight / 3;
			bottomParam.rightMargin = windowWidth / 6;
			
			
			
			AnimationSet bottomAnimationSet = new AnimationSet(true);
			bottomAnimationSet.setInterpolator(new DecelerateInterpolator());
			bottomAnimationSet.setFillAfter(true);
//			bottomAnimationSet.setAnimationListener(dispathLeavesAnimationListener);
			
			ScaleAnimation bottomTranslateAnimation = new ScaleAnimation(0.0f,
					1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			bottomTranslateAnimation.setDuration(COMMON_DURATION);
			
			bottomAnimationSet.addAnimation(bottomTranslateAnimation);
			
			AlphaAnimation bottomAlphaAnimation = new AlphaAnimation(0.0f, 1.0f);
			bottomAlphaAnimation.setDuration(COMMON_DURATION);
			bottomAnimationSet.addAnimation(bottomAlphaAnimation);
			
			tipTopImageView.startAnimation(bottomAnimationSet);
			
			if (tipTopImageView.getParent() == null) {
				rootLayout.addView(tipTopImageView, bottomParam);
			}
			
		}
	};

	private ImageView earthImageView;
	

}
