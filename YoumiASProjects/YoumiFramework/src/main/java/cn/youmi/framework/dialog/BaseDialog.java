package cn.youmi.framework.dialog;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.youmi.framework.R;
import cn.youmi.framework.activity.ActivityEventListener;
import cn.youmi.framework.main.BaseApplication;
import cn.youmi.framework.view.StrokeButton;

public class BaseDialog extends DialogFragment implements ActivityEventListener{
	protected StrokeButton positiveButton;
	protected StrokeButton negativeButton;
	private TextView titleTextView;
	private String mTitle;
	private boolean isOnTouchCancelable = true;
	
	private OnClickListener mPositiveButtonListener;
	private OnClickListener mNegativeButtonListener;
	
	private String mPositiveButtonText;
	private String mNegativeButtonText;
	
	
	@Override
	public int show(FragmentTransaction transaction, String tag) {
		return super.show(transaction, tag);
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
	}
	
	private boolean canCommit = true;
	private ActivityLifecycleCallbacks mActivityMonitor = new ActivityLifecycleCallbacks() {
		@Override
		public void onActivityStopped(Activity activity) {
			
		}
		
		@Override
		public void onActivityStarted(Activity activity) {
			
		}
		
		@Override
		public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
			
		}
		
		@Override
		public void onActivityResumed(Activity activity) {
			
		}
		
		@Override
		public void onActivityPaused(Activity activity) {
			
		}
		
		@Override
		public void onActivityDestroyed(Activity activity) {
			
		}
		
		@Override
		public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
			
		}
	};
	
	public BaseDialog(){
		BaseApplication.getApplication().registerActivityLifecycleCallbacks(mActivityMonitor);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		BaseApplication.getApplication().unregisterActivityLifecycleCallbacks(mActivityMonitor);
	}
	


	public void setPositiveButtonListener(OnClickListener l){
		mPositiveButtonListener = l;
		if(positiveButton != null){
			positiveButton.setOnClickListener(mPositiveButtonListener);
		}
	}
	
	public void setNegativeButtonListener(OnClickListener l){
		mNegativeButtonListener = l;
		if(negativeButton != null){
			negativeButton.setOnClickListener(l);
		}
	}
	
	
	public void setHidePositiveButton() {
		if (positiveButton != null) {
			positiveButton.setVisibility(View.GONE);
		}
	}
	
	public void setHideNegativeButton() {
		if (negativeButton != null) {
			negativeButton.setVisibility(View.GONE);
		}
	}
	
	public void setPositiveButtonText(String s){
		mPositiveButtonText = s;
		if(positiveButton != null){
			positiveButton.setText(s);
		}
	}
	
	public void setPositiveButtonText(int resId){
		String s = BaseApplication.getContext().getResources().getString(resId);
		setPositiveButtonText(s);
	}
	
	public void setNegativeButtonText(String s){
		mNegativeButtonText = s;
		if(negativeButton != null){
			negativeButton.setText(s);
		}
	}
	
	public void setNegativeButtonText(int resId){
		String s = BaseApplication.getContext().getResources().getString(resId);
		setNegativeButtonText(s);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Translucent);
	}
	
	public void setTitleVisible(int visible) {
		if (titleTextView != null) {
			titleTextView.setVisibility(visible);
		}
	}
	
	public void setTitleColor(int color){
		if (titleTextView != null) {
			titleTextView.setTextColor(color);
		}
	}
	
	public void setTitle(String title){
		mTitle = title;
		if(titleTextView != null){
			titleTextView.setText(title);
		}
	}
	
	public void setTitle(int resId){
		String title = BaseApplication.getContext().getResources().getString(resId);
		setTitle(title);
	}
	
	public boolean isOnTouchCancelable() {
		return isOnTouchCancelable;
	}

	public void setOnTouchCancelable(boolean isOnTouchCancelable) {
		this.isOnTouchCancelable = isOnTouchCancelable;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dialog_base,null);
		rootView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (isOnTouchCancelable) {
					dismissAllowingStateLoss();
				} 
//				if (isCancelable()) {//不需要屏蔽返回键
//					dismissAllowingStateLoss();
//				}
				return true;
			}
		});
		titleTextView = (TextView) rootView.findViewById(R.id.title_text_view);
		titleTextView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		positiveButton = (StrokeButton) rootView.findViewById(R.id.positive_button);
		negativeButton = (StrokeButton) rootView.findViewById(R.id.negative_button);
		positiveButton.setText("Okay");
		negativeButton.setText("Cancle");
		negativeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismissAllowingStateLoss();
			}
		});
		FrameLayout contentViewContainer = (FrameLayout) rootView.findViewById(R.id.content_view_container);
		onPostInflaterView(inflater,rootView,contentViewContainer);
		if(mTitle != null){
			titleTextView.setText(mTitle);
		}else{
			titleTextView.setVisibility(View.GONE);
			rootView.findViewById(R.id.title_line_view).setVisibility(View.GONE);
		}
		
		setPositiveButtonListener(mPositiveButtonListener);
		if(mNegativeButtonListener != null){
			setNegativeButtonListener(mNegativeButtonListener);
		}
		
		setPositiveButtonText(mPositiveButtonText);
		if(mNegativeButtonText != null){
			setNegativeButtonText(mNegativeButtonText);
		}else{
			setNegativeButtonText(R.string.cancel);
		}
		
		return rootView;
	}
	
	protected void onPostInflaterView(LayoutInflater inflater,View rootView,FrameLayout container){
		
	}

	@Override
	public void onActivityCreated(AppCompatActivity a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void willSetContentView(AppCompatActivity a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didSetContentView(AppCompatActivity a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPaused(AppCompatActivity a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResumed(AppCompatActivity a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActivityDestroyed(AppCompatActivity a) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onClickBack(AppCompatActivity a) {
		// TODO Auto-generated method stub
		return false;
	}

}
