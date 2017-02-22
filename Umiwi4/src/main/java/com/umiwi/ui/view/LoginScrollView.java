package com.umiwi.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.widget.ScrollView;

public class LoginScrollView extends ScrollView {
	GestureDetector gestureDetector;
	/**
	 * 滚动view的监听器
	 */
	private OnScrollViewListener mOnScrollViewListener;

	public LoginScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LoginScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LoginScrollView(Context context) {
		super(context);
	}

	/**
	 * scrollview的滚动变换时候 的监听
	 */
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		if (this.mOnScrollViewListener != null) {// 当自定义的监听不是nulld时候，自定义的监听的方法。
			OnScrollViewListener localOnScrollViewListener = this.mOnScrollViewListener;
			LoginScrollView localUmiwiScrollView = this;
			// 调用自定义的监听器
			localOnScrollViewListener.onScrollChanged(localUmiwiScrollView, x,
					y, oldx, oldy);
		}
		super.onScrollChanged(x, y, oldx, oldy);
	}

	/**
	 * 给视图设置监听
	 * 
	 * @param listener
	 */
	public void setOnScrollViewListener(OnScrollViewListener listener) {
		this.mOnScrollViewListener = listener;
	}

	/**
	 * 首页的滚动view的监听接口
	 * 
	 * @author qiujiaheng
	 * 
	 */
	public interface OnScrollViewListener {
		void onScrollChanged(LoginScrollView loginScrollView,
							 int paramInt1, int paramInt2, int paramInt3, int paramInt4);
	}

	/*public void setGestureDetector(GestureDetector gestureDetector) {
		this.gestureDetector = gestureDetector;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		return gestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		gestureDetector.onTouchEvent(ev);
		super.dispatchTouchEvent(ev);
		return true;
	}*/
}
