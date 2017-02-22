package com.umiwi.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;
import cn.youmi.framework.util.DimensionUtil;

public class AutoResizeImageView extends ImageView {

	private Bitmap image;
	private String text;
	public int height = 120;
	public void setText(String s){
		text = s;
		invalidate();
	}
	
	public AutoResizeImageView(Context context) {
		super(context);
	}

	public AutoResizeImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	Rect rect = new Rect();
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	float textSize;
	
	{
		mPaint.setColor(Color.WHITE);
		if(!isInEditMode()){
			textSize = DimensionUtil.dip2px(16);
		}
	}
	
	@Override
	public void setImageBitmap(Bitmap bm) {
//		super.setImageBitmap(bm);
		image = bm;
		requestLayout();
	}
	
	public void setTextSize(float size){
		this.textSize = size;
		mPaint.setTextSize(size);
		invalidate();
	}
	
	public void setTextColor(int color){
		mPaint.setColor(color);
	}
	
	
	private float leftPadding;
	public void setTextLeftPadding(float leftPadding){
		this.leftPadding = leftPadding;
		invalidate();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int hPadding = getPaddingLeft() + getPaddingRight();
		int vPadding = getPaddingTop() + getPaddingBottom();
		
		width -= hPadding;
		if(image != null){
			if(MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED || 
					MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST){
				width = image.getWidth();
			}
			int height = (int) ( (float)(width) / image.getWidth() * image.getHeight());
			setMeasuredDimension(width + hPadding, height + vPadding);
		}else{
			setMeasuredDimension(width + hPadding, height);
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		rect.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
		if(image != null){
			canvas.drawBitmap(image, null, rect, null);
		}else{
//			canvas.drawRect(rect, mPaint);
		}
		if(text != null){
			mPaint.setTextSize(textSize);
			float textWidth = mPaint.measureText(text);
			canvas.drawText(text, (getWidth() - textWidth) / 2 + leftPadding, getHeight() / 2 + textSize / 4  , mPaint);
		}
	}
}
