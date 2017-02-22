package com.umiwi.ui.fragment.splash;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.HomeMainActivity;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.fragment.user.RegisterShowFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.util.LoginUtil;

import java.util.List;

/**
 * @author tjie00
 * @version 2015年01月24日
 *  引导页第三屏
 */
public class SplashHorizontalChild3Fragment extends BaseConstantFragment implements OnGestureListener {

	private LinearLayout rootLinearLayout;
	
	private ImageView loginImageView;
	private ImageView registImageView;
	private ImageView tipTextView;
	
	private boolean isAnimationShowed = false;
	
	private SharedPreferences mSharedPreferences;
	
	
	private GestureDetector gestureDetector;

	private LinearLayout bottomLinearlayout;

	private FrameLayout rootLayout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		
		View view = inflater.inflate(R.layout.splash_horizontal_03_layout, null);
		rootLinearLayout = (LinearLayout) view.findViewById(R.id.root_linearlayout);
		rootLinearLayout.removeAllViews();
		rootLayout = (FrameLayout) view.findViewById(R.id.root_framelayout);
		
		bottomLinearlayout = (LinearLayout) view.findViewById(R.id.bottom_linearlayout);
		
		gestureDetector = new GestureDetector(this);
		
		loginImageView = new ImageView(getActivity());
		loginImageView.setScaleType(ScaleType.CENTER_CROP);
		loginImageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.horizontal_3_01));
		loginImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isActivityExist(getActivity(), HomeMainActivity.class)) {
					LoginUtil.getInstance().showLoginView(getActivity());
					mSharedPreferences.edit().putBoolean("isCanShowGift", false).commit();
				} else {
					LoginUtil.getInstance().showLoginView(getActivity());
					mSharedPreferences.edit().putBoolean("isCanShowGift", false).commit();
				}
				getActivity().finish();
			}
		});
		
		registImageView = new ImageView(getActivity());
		registImageView.setScaleType(ScaleType.CENTER_CROP);
		registImageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.horizontal_3_03));
		registImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isActivityExist(getActivity(), HomeMainActivity.class)) {
					Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
					i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, RegisterShowFragment.class);
					startActivity(i);
					mSharedPreferences.edit().putBoolean("isCanShowGift", false).commit();
				} else {
					Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
					i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, RegisterShowFragment.class);
					startActivity(i);
					mSharedPreferences.edit().putBoolean("isCanShowGift", false).commit();
				}
				getActivity().finish();
			}
		});
		
		tipTextView = new ImageView(getActivity());
		tipTextView.setScaleType(ScaleType.CENTER_CROP);
		tipTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.horizontal_3_02));
		tipTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				enterHomeView();
			}
		});
		
		
		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(!isAnimationShowed) {
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

	private void showAnimation() {
		
		rootLinearLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		});
		
		showLoginAnimation();
		showRegistAnimation();
		showTextTipAnimation();
		showTextAnimation();
		showIconAnimation();
	}

	// 文字提示加载动画
	private void showTextTipAnimation() {
		LinearLayout.LayoutParams textTipParam = 
				new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
						LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
		
		textTipParam.bottomMargin = 100;
		
		AnimationSet textTipAnimationSet = new AnimationSet(true);
		textTipAnimationSet.setFillAfter(true);
		
		TranslateAnimation textTipTranslateAnimation = 
				new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, 
						Animation.RELATIVE_TO_SELF, 0.0f, 
						Animation.RELATIVE_TO_SELF, 1.0f, 
						Animation.RELATIVE_TO_SELF, 0.0f);
		textTipTranslateAnimation.setDuration(500);
		
		AlphaAnimation textTipAlphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		textTipAlphaAnimation.setDuration(500);
		
		textTipAnimationSet.addAnimation(textTipAlphaAnimation);
		textTipAnimationSet.addAnimation(textTipTranslateAnimation);
		
		tipTextView.startAnimation(textTipAnimationSet);
		if(tipTextView.getParent() == null) {
			bottomLinearlayout.addView(tipTextView, textTipParam);
		}
	}

	// 注册按钮动画
	private void showRegistAnimation() {
		LinearLayout.LayoutParams registParam = 
				new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
						LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
		
		registParam.bottomMargin = 60;
		registParam.leftMargin = 20;
		
		AnimationSet registAnimationSet = new AnimationSet(true);
		registAnimationSet.setFillAfter(true);
		
		TranslateAnimation registTranslateAnimation = 
				new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, 
						Animation.RELATIVE_TO_SELF, 0.0f, 
						Animation.RELATIVE_TO_SELF, 1.0f, 
						Animation.RELATIVE_TO_SELF, 0.0f);
		registTranslateAnimation.setDuration(500);
		
		AlphaAnimation registAlphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		registAlphaAnimation.setDuration(500);
		
		registAnimationSet.addAnimation(registTranslateAnimation);
		registAnimationSet.addAnimation(registAlphaAnimation);
		
		registImageView.startAnimation(registAnimationSet);
		if(registImageView.getParent() == null) {
			rootLinearLayout.addView(registImageView, registParam);
		}
	}
	
	
	private void showTextAnimation() {
		ImageView tipBottomImageView = new ImageView(getActivity());
		tipBottomImageView.setScaleType(ScaleType.CENTER_CROP);
		tipBottomImageView.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.horizontal_3_04));


		FrameLayout.LayoutParams bottomParam = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT,
				Gravity.CENTER_HORIZONTAL | Gravity.TOP);

		bottomParam.rightMargin = 0;
		bottomParam.topMargin = 500;

		AnimationSet bottomAnimationSet = new AnimationSet(false);
		bottomAnimationSet.setInterpolator(new DecelerateInterpolator());
		bottomAnimationSet.setFillAfter(true);

		ScaleAnimation bottomTranslateAnimation = new ScaleAnimation(0.0f,
				1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		bottomTranslateAnimation.setDuration(500);

		bottomAnimationSet.addAnimation(bottomTranslateAnimation);

		AlphaAnimation bottomAlphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		bottomAlphaAnimation.setDuration(500);
		bottomAnimationSet.addAnimation(bottomAlphaAnimation);

		tipBottomImageView.startAnimation(bottomAnimationSet);

		if (tipBottomImageView.getParent() == null) {
			rootLayout.addView(tipBottomImageView, bottomParam);
		}
	}
	
	private void showIconAnimation() {
		ImageView tipBottomImageView = new ImageView(getActivity());
		tipBottomImageView.setScaleType(ScaleType.CENTER_CROP);
		tipBottomImageView.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.horizontal_3_05));


		FrameLayout.LayoutParams bottomParam = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT,
				Gravity.CENTER_HORIZONTAL | Gravity.TOP);

		bottomParam.rightMargin = 0;
		bottomParam.topMargin = 200;

		AnimationSet bottomAnimationSet = new AnimationSet(false);
		bottomAnimationSet.setInterpolator(new DecelerateInterpolator());
		bottomAnimationSet.setFillAfter(true);

		ScaleAnimation bottomTranslateAnimation = new ScaleAnimation(0.0f,
				1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		bottomTranslateAnimation.setDuration(500);

		bottomAnimationSet.addAnimation(bottomTranslateAnimation);

		AlphaAnimation bottomAlphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		bottomAlphaAnimation.setDuration(500);
		bottomAnimationSet.addAnimation(bottomAlphaAnimation);

		tipBottomImageView.startAnimation(bottomAnimationSet);

		if (tipBottomImageView.getParent() == null) {
			rootLayout.addView(tipBottomImageView, bottomParam);
		}
	}

	// 登陆按钮加载动画
	private void showLoginAnimation() {
		LinearLayout.LayoutParams loginParam = 
				new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
						LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
		
		loginParam.bottomMargin = 60;
		loginParam.rightMargin = 20;
		
		AnimationSet loginAnimationSet = new AnimationSet(true);
		loginAnimationSet.setFillAfter(true);
		
		TranslateAnimation loginTranslateAnimation = 
				new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, 
						Animation.RELATIVE_TO_SELF, 0.0f, 
						Animation.RELATIVE_TO_SELF, 1.0f, 
						Animation.RELATIVE_TO_SELF, 0.0f);
		loginTranslateAnimation.setDuration(500);
		
		AlphaAnimation loginAlphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		loginAlphaAnimation.setDuration(500);
		
		loginAnimationSet.addAnimation(loginTranslateAnimation);
		loginAnimationSet.addAnimation(loginAlphaAnimation);
		
		loginImageView.startAnimation(loginAnimationSet);
		if(loginImageView.getParent() == null) {
			rootLinearLayout.addView(loginImageView, loginParam);
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if(distanceY > 0) {
			enterHomeView();
			return true;
		}
		return false;
	}

	// 进入首页
	private void enterHomeView() {
		mSharedPreferences.edit().putBoolean("isCanShowGift", true).commit();
		startActivity(new Intent(getActivity(), HomeMainActivity.class));
		getActivity().finish();
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}
	
	protected boolean isActivityExist(Context packageContext,
			Class<?> activityName) {
		Intent intent = new Intent(getActivity(), activityName);
		ComponentName cmpName = intent.resolveActivity(packageContext.getPackageManager());
		boolean bIsExist = false;
		if (cmpName != null) { // 说明系统中存在这个activity
			ActivityManager am = (ActivityManager) packageContext.getSystemService(Activity.ACTIVITY_SERVICE);
			List<RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
			for (RunningTaskInfo taskInfo : taskInfoList) {
				if (taskInfo.baseActivity.equals(cmpName)) {
					bIsExist = true;
					break;
				}
			}
		}
		if (!bIsExist) {
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			this.startActivity(intent);
			bIsExist = true;
		}
		return bIsExist;
	}
	
}
