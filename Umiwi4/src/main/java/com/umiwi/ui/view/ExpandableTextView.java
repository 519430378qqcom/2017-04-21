package com.umiwi.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.widget.TextView;

public class ExpandableTextView extends TextView {

	public interface OnMeasureWidthListener {
		void onWidthMeasured();
	}

	private OnMeasureWidthListener mOnMeasureWidthListener;

	public void setOnMeasureWidthListener(OnMeasureWidthListener l) {
		this.mOnMeasureWidthListener = l;
	}

	private String mText;
	private Layout mLayout;

	public ExpandableTextView(Context context) {
		super(context);
	}

	public ExpandableTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private int width;
	private boolean expand;

	public void setExpand(boolean ex) {
		this.expand = ex;
		requestLayout();
	}
	
	public boolean isExpand(){
		return expand;
	}

	public void toggleExpand() {
		this.expand = !this.expand;
		requestLayout();
	}

	public void setWidth(int w) {
		this.width = w;
	}

	public int setText(String text) {
		this.mText = text;
		if (width == 0) {
			return 0;
		}
		expand = false;
		mLayout = new StaticLayout(mText, getPaint(), width,
				Alignment.ALIGN_NORMAL, 1.0f, 0, true);
		requestLayout();
		return mLayout.getLineCount();
	}

	@Override
	public int getLineCount() {
		if (mLayout != null) {
			return mLayout.getLineCount();
		}
		return 0;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (width == 0
				 && MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
			width = MeasureSpec.getSize(widthMeasureSpec);
			if(mText != null){
				mLayout = new StaticLayout(mText, getPaint(), width,
					Alignment.ALIGN_NORMAL, 1.0f, 0, true);
			}
			if (mOnMeasureWidthListener != null) {
				mOnMeasureWidthListener.onWidthMeasured();
			}
			return;
		}
		if (mLayout == null) {
			setMeasuredDimension(width, 0);
			return;
		}
		if (expand || mLayout.getLineCount() <= 5) {
			setMeasuredDimension(width, mLayout.getHeight());
		} else {
			setMeasuredDimension(width, (int) (mLayout.getHeight() * 1.0
					/ mLayout.getLineCount() * 5));
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(mLayout != null){
			mLayout.draw(canvas);
		}
	}

}
