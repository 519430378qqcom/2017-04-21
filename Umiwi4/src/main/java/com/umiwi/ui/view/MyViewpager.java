package com.umiwi.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import static u.aly.x.T;

/**
 * Created by Administrator on 2017/3/7.
 */

public class MyViewpager extends ViewPager {
    public MyViewpager(Context context) {
        super(context);
    }

    public MyViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        getParent().requestDisallowInterceptTouchEvent(true);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        getParent().requestDisallowInterceptTouchEvent(true);
        return true;
    }
}

