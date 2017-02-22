package com.umiwi.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import cn.youmi.framework.util.DimensionUtil;

public class StepPercentageView extends View {

	private int steps = 10;
	private int currentStep;
	private float radius = DimensionUtil.dip2px(4);
	private Paint mPaint = new Paint();
	
	{
		mPaint.setStrokeWidth(DimensionUtil.dip2px(2));
		mPaint.setColor(Color.GREEN);
	}
	
	public void setSteps(int steps){
		this.steps = steps;
	}
	
	public void setCurrentStep(int cStep){
		this.currentStep = cStep;
		invalidate();
	}
	
	public int getSteps() {
		return steps;
	}
	
	public int getCurrentStep() {
		return currentStep;
	}
	
	public StepPercentageView(Context context) {
		super(context);
	}

	public StepPercentageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), (int)(radius * 4));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		float margin = radius * 4;
		float y = radius * 2;
		
		float width = (getWidth() - margin) / (float)(steps - 1);
		canvas.drawLine(radius, y, width * (steps - 1) + radius, y, mPaint);
		for(int i = 0;i < steps;i++){
			if(currentStep == i){
				mPaint.setColor(Color.YELLOW);
				canvas.drawCircle(margin / 2 + width * i, y, radius + radius * 0.32f, mPaint);
				mPaint.setColor(Color.GREEN);
			}
			canvas.drawCircle(margin / 2 + width * i, y, radius, mPaint);
		}
	}
}
