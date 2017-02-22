package com.umiwi.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class CirclePercentageView extends View {

	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private RectF mRectF = new RectF();
	private Rect mBounds = new Rect();
	private Rect mTextBounds = new Rect();

	private int lineWith = 2;
	private int emptyColor = Color.rgb(200, 200, 200);
	private int progressColor = Color.rgb(80, 182, 57);
	private int percentage = 30;
	private int textSize = 12;
	private int textColor = Color.BLACK;
	private int radius = 0;

	private String text = "0%";
	private Drawable image;
	private float textX;
	private float textY;

	public boolean drawLine;

	public void setImage(Drawable d) {
		this.image = d;
		invalidate();
	}

	public void setImage(int resId) {
		setImage(getContext().getResources().getDrawable(resId));
	}

	private Shader gradient;

	{
		mPaint.setStrokeWidth(lineWith);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(progressColor);
	}

	public void setLineWith(int lineWith) {
		this.lineWith = lineWith;
		mPaint.setStrokeWidth(lineWith);
		invalidate();
	}

	public void setEmptyColor(int emptyColor) {
		this.emptyColor = emptyColor;
		invalidate();
	}

	public void setProgressColor(int progressColor) {
		this.progressColor = progressColor;
		invalidate();
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
		text = percentage + "%";

		invalidate();
	}

	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
		mTextPaint.setTextSize(textSize);
		mTextPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF,
				Typeface.BOLD));
		radius = (int) (mTextPaint.measureText("99%") / 2) + lineWith * 2;
		invalidate();
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
		invalidate();
	}

	private void init() {
		int size = (int) (getResources().getDisplayMetrics().scaledDensity * 12 + 0.5);
		this.lineWith = (int) (getResources().getDisplayMetrics().density * 2.4);
		mPaint.setStrokeWidth(lineWith);
		setTextSize(size);
	}

	public CirclePercentageView(Context context) {
		super(context);
		init();
	}

	public CirclePercentageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int size = 2 * radius + 10 * lineWith;
		int height = size;
		if(drawLine){
			height = MeasureSpec.getSize(heightMeasureSpec);
			setMeasuredDimension(size, heightMeasureSpec);
		}else{
			setMeasuredDimension(size, size);
		}
		height = Math.max(height, size);

		int green = Color.argb(200, 0, 145, 219);
		int blue = Color.argb(200, 80, 181, 57);
		float top = (height - size) / 2.0f;
		gradient = new LinearGradient(0, top , 0, top + size, green, blue,
				TileMode.MIRROR);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float oneDip = getResources().getDisplayMetrics().density + 0.5f;
		canvas.getClipBounds(mBounds);
		
		int innerRadius = radius + 1 * lineWith;
		
		mBounds.left = getWidth() / 2 - innerRadius;
		mBounds.top = getHeight() / 2 - innerRadius;
		mBounds.right = getWidth() / 2 + innerRadius;
		mBounds.bottom = getHeight() / 2 + innerRadius;
		mRectF.set(mBounds);
		mPaint.setColor(emptyColor);
		mPaint.setStrokeWidth(oneDip);

//		if (drawLine) {
//			 canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, mRectF.top,
//			 mPaint);
//			 canvas.drawLine(getWidth() / 2, mRectF.bottom, getWidth() / 2,
//			 getHeight(), mPaint);
//		}
		if (percentage >= 100) {
			mTextPaint.setColor(progressColor);
		} else {
			mTextPaint.setColor(this.textColor);
		}
		mPaint.setShader(null);
		mPaint.setStrokeWidth(lineWith);
		if (percentage < 100) {
			canvas.drawArc(mRectF, 0, 360, false, mPaint);
		}
		// mPaint.setColor(progressColor);
		mPaint.setShader(gradient);
		canvas.drawArc(mRectF, 270, 360 * percentage / 100, false, mPaint);
		if (image == null) {
			float height = textSize * 2 / 5;
			mPaint.getTextBounds(text, 0, text.length(), mTextBounds);
			float width = mTextPaint.measureText(text);
			if (percentage < 100) {
				textX += mTextPaint.measureText("%") * 2 / 3;
			}
			textX = Math.round((getWidth() - width) / 2.0f);
			textY = Math.round(getHeight() / 2 + height - 1);
			canvas.drawText(text, textX, textY, mTextPaint);
		} else {
			int size = (int) (mRectF.width() * 3.2f / 3);
			image.setBounds(size / 2, size / 2, getWidth() - size / 2,
					getHeight() - size / 2);
			image.draw(canvas);
		}
	}
}
