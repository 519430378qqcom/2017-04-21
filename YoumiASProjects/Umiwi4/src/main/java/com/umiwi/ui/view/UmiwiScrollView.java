package com.umiwi.ui.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

public class UmiwiScrollView extends ScrollView {
	private View mView;
	private Rect mRect = new Rect();
	private float y;

	public UmiwiScrollView(Context context) {
		super(context);
	}

	public UmiwiScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public UmiwiScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFinishInflate() {
		if (getChildCount() > 0) {
			this.mView = getChildAt(0);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mView == null) {
			return super.onTouchEvent(ev);
		} else {
			commOnTouchEvent(ev);
		}
		return super.onTouchEvent(ev);
	}

	private void commOnTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			y = ev.getY();
			break;
		case MotionEvent.ACTION_UP:
			if (isNeedAnimation()) {
				animation();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			final float preY = y;
			float nowY = ev.getY();
			int deltaY = (int) (preY - nowY);
			scrollBy(0, deltaY);
			y = nowY;
			if (isNeedMove()) {
				if (mRect.isEmpty()) {mRect.set(mView.getLeft(), mView.getTop(), mView.getRight(), mView.getBottom());
				}
				if (mView.getTop() - deltaY < 200 && mView.getTop() - deltaY > -200) {
					mView.layout(mView.getLeft(), mView.getTop() - deltaY, mView.getRight(), mView.getBottom() - deltaY);
				}
			}
			break;
		default:
			break;
		}
	}

	private boolean isNeedMove() {
		int offset = mView.getMeasuredHeight() - getHeight();
		int scrollY = getScrollY();
		return scrollY == 0 || scrollY == offset;
	}

	private boolean isNeedAnimation() {
		return !mRect.isEmpty();
	}

	private void animation() {
		TranslateAnimation ta = new TranslateAnimation(0, 0, mView.getTop(), mRect.top);
		ta.setDuration(200);
		mView.startAnimation(ta);
		mView.layout(mRect.left, mRect.top, mRect.right, mRect.bottom);
		mRect.setEmpty();
	}
}