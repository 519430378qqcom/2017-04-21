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
public class SplashHorizontalChild1Fragment extends BaseFragment {

	private FrameLayout rootLayout;

	private ImageView tipBottomImageView;
	private ImageView tipMiddleImageView;
	private ImageView tipTopImageView;


	private boolean isAnimationShowed = false;

	private WindowManager windowManager;

	private int windowWidth;
	private int windowHeight;

	private static final long COMMON_DURATION = 300;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.splash_horizontal_01_layout, null);

		rootLayout = (FrameLayout) view.findViewById(R.id.root_layout);
		
		ImageView tipsText = (ImageView) view.findViewById(R.id.tips_text_imageview);
		tipsText.setBackgroundDrawable(getActivity().getResources().getDrawable(
				R.drawable.horizontal_1_04));

		windowManager = getActivity().getWindowManager();
		windowWidth = windowManager.getDefaultDisplay().getWidth();
		windowHeight = windowManager.getDefaultDisplay().getHeight();


		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		if (!isAnimationShowed) {
			showAnimation();
			isAnimationShowed = true;
		}
	}

	// 显示动画
	@SuppressWarnings("deprecation")
	private void showAnimation() {

		tipBottomImageView = new ImageView(getActivity());
		tipBottomImageView.setScaleType(ScaleType.CENTER_CROP);
		tipBottomImageView.setBackgroundDrawable(getActivity().getResources().getDrawable(
				R.drawable.horizontal_1_03));

		int businessXLocationMargin = 0;
		int businessYLocation = (6 * windowHeight) / 11;

		FrameLayout.LayoutParams bottomParam = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT,
				Gravity.CENTER_HORIZONTAL | Gravity.TOP);

		bottomParam.rightMargin = businessXLocationMargin;
		bottomParam.topMargin = businessYLocation;

		AnimationSet bottomAnimationSet = new AnimationSet(false);
		bottomAnimationSet.setInterpolator(new DecelerateInterpolator());
		bottomAnimationSet.setFillAfter(true);
		bottomAnimationSet.setAnimationListener(bgAniamtionListener);

		ScaleAnimation bottomTranslateAnimation = new ScaleAnimation(0.0f,
				1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		bottomTranslateAnimation.setDuration(COMMON_DURATION);

		bottomAnimationSet.addAnimation(bottomTranslateAnimation);

		AlphaAnimation bottomAlphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		bottomAlphaAnimation.setDuration(COMMON_DURATION);
		bottomAnimationSet.addAnimation(bottomAlphaAnimation);

		tipBottomImageView.startAnimation(bottomAnimationSet);

		if (tipBottomImageView.getParent() == null) {
			rootLayout.addView(tipBottomImageView, bottomParam);
		}
	}
	
	AnimationListener bgAniamtionListener = new AnimationListener() {
		
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
			showbgAnimation();
		}
		
		@SuppressWarnings("deprecation")
		private void showbgAnimation() {


			tipMiddleImageView = new ImageView(getActivity());
			tipMiddleImageView.setScaleType(ScaleType.CENTER_CROP);
			tipMiddleImageView.setBackgroundDrawable(getActivity().getResources().getDrawable(
					R.drawable.horizontal_1_02));

			int businessXLocationMargin = 0;
			int businessYLocation = (1 * windowHeight) / 4;

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

			tipMiddleImageView.startAnimation(bottomAnimationSet);

			if (tipMiddleImageView.getParent() == null) {
				rootLayout.addView(tipMiddleImageView, bottomParam);
			}
		
		}
	};
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
			showbgAnimation();
			showcouldAnimation();
		}
		
		@SuppressWarnings("deprecation")
		private void showcouldAnimation() {
			
			ImageView tipTopImageView = new ImageView(getActivity());
			tipTopImageView.setScaleType(ScaleType.CENTER_CROP);
			tipTopImageView.setBackgroundDrawable(getActivity().getResources().getDrawable(
					R.drawable.horizontal_1_05));
			
			
			FrameLayout.LayoutParams bottomParam = 
					new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, 
							FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM|Gravity.RIGHT);
			bottomParam.bottomMargin = (2*windowHeight) / 13;
			bottomParam.leftMargin = windowWidth/2;
			
			
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
		
		@SuppressWarnings("deprecation")
		private void showbgAnimation() {
			
			
			tipTopImageView = new ImageView(getActivity());
			tipTopImageView.setScaleType(ScaleType.CENTER_CROP);
			tipTopImageView.setBackgroundDrawable(getActivity().getResources().getDrawable(
					R.drawable.horizontal_1_01));
			
			
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
	

}
