package com.umiwi.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPager extends ViewPager {

	private boolean scrollEnabled;
	
	public NoScrollViewPager(Context context) {
		super(context);
		this.scrollEnabled = false;
	}

	public NoScrollViewPager(Context context, AttributeSet atrributeSet) {
		super(context, atrributeSet);
		this.scrollEnabled = false;
	}

	//触摸没有反应就可以了
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.scrollEnabled) {
            return super.onTouchEvent(event);
        }
  
        return false;
    }

	
	@Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.scrollEnabled) {
            return super.onInterceptTouchEvent(event);
        }
 
        return false;
    }
 
    public void setPagingEnabled(boolean scrollEnabled) {
        this.scrollEnabled = scrollEnabled;
    }
	
}
