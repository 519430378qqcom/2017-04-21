package com.umiwi.ui.view;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 *
 */
public class MonitorScrollView extends ScrollView {
	private OnScrollListener mListener;
   private Context context;
	private int _calCount;

	public MonitorScrollView(Context context) {
		this(context,null);
	}

	public MonitorScrollView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public MonitorScrollView(Context context, AttributeSet attrs,
							 int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;

	}


	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		View view = this.getChildAt(0);
		if (this.getHeight() + this.getScrollY() == view.getHeight()) {
			_calCount++;
			if (_calCount == 1) {
				 mListener.isBotton(true);
			}
		} else {
			_calCount = 0;
		}
		if (mListener != null)
			mListener.onScroll(t);
	}

	public void setOnScrollListener(OnScrollListener listener) {
		this.mListener = listener;
	}

	public interface OnScrollListener {
		public void onScroll(int y);
		public void isBotton(boolean isBotton);
	}
}
