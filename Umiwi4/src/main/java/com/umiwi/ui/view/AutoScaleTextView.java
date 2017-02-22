package com.umiwi.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.umiwi.ui.R;

/**
 * @author tangxiyong
 * @version 2014年7月25日 上午11:16:39
 */
public class AutoScaleTextView extends TextView {
	private Paint textPaint;
	private float preferredTextSize;
	private float minTextSize;
	
	private double mHeightRatio;
    private double mWidthRatio;
    
	public AutoScaleTextView(Context context) {
		super(context);
		init(context, null);
	}

	public AutoScaleTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public AutoScaleTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}
	
	private void init(Context context, AttributeSet attrs) {
		this.textPaint = new Paint();

		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.AutoScaleTextView);
		this.minTextSize = a.getDimension(R.styleable.AutoScaleTextView_minTextSize, 10f);
		a.recycle();

		this.preferredTextSize = this.getTextSize();
	}
	// set view width & height 
	public void setWidthAndHeight(double width, double height) {
    	if (width != mWidthRatio) {
    		mWidthRatio = width;
		}
    	if (height != mHeightRatio) {
    		mHeightRatio = height;
		}
    	requestLayout();
    }
	
	public double getHeightRatio() {
        return mHeightRatio;
    }

    public double getmWidthRatio() {
		return mWidthRatio;
	}
	
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mWidthRatio > 0.0) {
            // set the image views size
//            int width = MeasureSpec.getSize(widthMeasureSpec);
//            int height = (int) (width * mHeightRatio);
        	int width = (int) mWidthRatio;
        	int height = (int) mHeightRatio;
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

	// =============autoscaletext===========
	public void setMinTextSize(float minTextSize) {
		this.minTextSize = minTextSize;
	}

	/***
	 * 如果 text 是要从网络获取的，不要在xml 里写上默认的text
	 * @param text
	 * @param textWidth
	 */
	private void refitText(String text, int textWidth) {
		if (textWidth <= 0 || text == null || text.length() == 0)
			return;

		int targetWidth = textWidth - this.getPaddingLeft()
				- this.getPaddingRight();

		final float threshold = 0.5f;

		this.textPaint.set(this.getPaint());

		while ((this.preferredTextSize - this.minTextSize) > threshold) {
			float size = (this.preferredTextSize + this.minTextSize) / 2;
			this.textPaint.setTextSize(size);
			if (this.textPaint.measureText(text) >= targetWidth)
				this.preferredTextSize = size; // big
			else
				this.minTextSize = size; // small
		}
		this.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.minTextSize);
	}

	@Override
	protected void onTextChanged(final CharSequence text, final int start,
			final int before, final int after) {
		this.refitText(text.toString(), this.getWidth());
	}

	@Override
	protected void onSizeChanged(int width, int height, int oldwidth,
			int oldheight) {
		if (width != oldwidth)
			this.refitText(this.getText().toString(), width);
	}

}
