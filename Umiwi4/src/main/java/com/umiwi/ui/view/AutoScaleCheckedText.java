package com.umiwi.ui.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.CheckedTextView;

import com.umiwi.ui.R;

/**
 * @author tangxiyong
 * @version 2014年7月25日 上午11:16:39
 */
public class AutoScaleCheckedText extends CheckedTextView {
	private Paint textPaint;
	private float preferredTextSize;
	private float minTextSize;
	
	private double mHeightRatio;
    private double mWidthRatio;
    
    private static final int PRESSED_COLOR_LIGHTUP = 255 / 25;
	private static final int PRESSED_RING_ALPHA = 75;
	private static final int DEFAULT_PRESSED_RING_WIDTH_DIP = 4;
	private static final int ANIMATION_TIME_ID = android.R.integer.config_shortAnimTime;


	private Paint circlePaint;
	private Paint focusPaint;

	private float animationProgress;

	private int pressedRingWidth;
	private int defaultColor = Color.BLACK;
	private int pressedColor;
	private ObjectAnimator pressedAnimator;
    
	public AutoScaleCheckedText(Context context) {
		super(context);
		init(context, null);
	}

	public AutoScaleCheckedText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public AutoScaleCheckedText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}
	
	private void init(Context context, AttributeSet attrs) {
		this.textPaint = new Paint();
		this.preferredTextSize = this.getTextSize();
		
		this.setChecked(false);
		this.setFocusable(false);
//		this.setScaleType(ScaleType.CENTER_INSIDE);
//		setClickable(false);

		circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlePaint.setStyle(Paint.Style.FILL);

		focusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		focusPaint.setStyle(Paint.Style.STROKE);

		pressedRingWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PRESSED_RING_WIDTH_DIP, getResources()
				.getDisplayMetrics());

		int color = Color.BLACK;
		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.AutoScaleCheckedText);
			this.minTextSize = a.getDimension(R.styleable.AutoScaleCheckedText_asct_minTextSize, 10f);
			color = a.getColor(R.styleable.AutoScaleCheckedText_asct_color, color);
			pressedRingWidth = (int) a.getDimension(R.styleable.AutoScaleCheckedText_asct_pressed_ring_width, pressedRingWidth);
			a.recycle();
		}

		setColor(color);

		focusPaint.setStrokeWidth(pressedRingWidth);
		final int pressedAnimationTime = getResources().getInteger(ANIMATION_TIME_ID);
		pressedAnimator = ObjectAnimator.ofFloat(this, "animationProgress", 0f, 0f);
		pressedAnimator.setDuration(pressedAnimationTime);
	
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

		super.onSizeChanged(width, height, oldwidth, oldheight);

		if (width != oldwidth) {
			this.refitText(this.getText().toString(), width);
			widthRect = width;
			heightRect = height;
		}
	}
	
	//================
	@Override
	public void setPressed(boolean pressed) {
		super.setPressed(pressed);

		if (circlePaint != null) {
			circlePaint.setColor(pressed ? pressedColor : defaultColor);
		}

		if (pressed) {
			showPressedRing();
		} else {
			hidePressedRing();
		}
	}
	private boolean isClickedButton;
	
	public void setClickedButton(boolean isClickedButton) {
		this.isClickedButton = isClickedButton;
	}
	
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private int widthRect;
	private int heightRect;
	@Override
	protected void onDraw(Canvas canvas) {
		if (!isClickedButton) {
			canvas.drawRect(new Rect(0, 0, widthRect, heightRect), focusPaint);
		}
		canvas.drawRect(new Rect(0, 0, widthRect, heightRect), circlePaint);
		
		super.onDraw(canvas);//放在下面。
		
		if(isClickedButton){
			mPaint.setColor(getResources().getColor(R.color.color_primary));
			canvas.drawRect(0, 0, widthRect, heightRect, mPaint);
		} 
	}


	public float getAnimationProgress() {
		return animationProgress;
	}

	public void setAnimationProgress(float animationProgress) {
		this.animationProgress = animationProgress;
		this.invalidate();
	}

	public void setColor(int color) {
		this.defaultColor = color;
		this.pressedColor = getHighlightColor(color, PRESSED_COLOR_LIGHTUP);

		circlePaint.setColor(defaultColor);
		focusPaint.setColor(defaultColor);
		focusPaint.setAlpha(PRESSED_RING_ALPHA);

		this.invalidate();
	}
	

	private void hidePressedRing() {
		pressedAnimator.setFloatValues(pressedRingWidth, 0f);
		pressedAnimator.start();
	}

	private void showPressedRing() {
		pressedAnimator.setFloatValues(animationProgress, pressedRingWidth);
		pressedAnimator.start();
	}
	
	private int getHighlightColor(int color, int amount) {
		return Color.argb(Math.min(255, Color.alpha(color)), Math.min(255, Color.red(color) + amount),
				Math.min(255, Color.green(color) + amount), Math.min(255, Color.blue(color) + amount));
	}

}
