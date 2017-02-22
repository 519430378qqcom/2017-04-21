package cn.youmi.framework.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import cn.youmi.framework.R;
import cn.youmi.framework.main.BaseApplication;
import cn.youmi.framework.util.DimensionUtil;

public class StrokeButton extends TextView {

	private boolean isPressed;
	
	private boolean drawBorder ;
	
	public void setDrawBorder(boolean show){
		this.drawBorder = show;
		invalidate();
	}

	public StrokeButton(Context context) {
		super(context);
		init();
	}

	public StrokeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		
	}
	
	private void init(){
		mPaint = getPaint(); 
		if(!isInEditMode()){
			mPaint.setTextSize(DimensionUtil.dip2px(20));
		}
//		setBackground(null);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if(isInEditMode()){
			setMeasuredDimension(100, 40);
			return;
		}
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = DimensionUtil.dip2px(44);

		if(MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY && getText() != null){
			width = (int) mPaint.measureText(getText().toString()) + DimensionUtil.dip2px(20);//TODO use padding
		}
		if(MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY && getText() != null){
			height = (int) (mPaint.getTextSize() + DimensionUtil.dip2px(2));
		}
		if(drawBorder){
			width = (int) mPaint.measureText(getText().toString()) + getPaddingLeft() + getPaddingRight();
		}
		setMeasuredDimension(width, height);
	}

	
	private OnClickListener mOnClickListener;
	@Override
	public void setOnClickListener(OnClickListener l) {
		super.setOnClickListener(l);
		mOnClickListener = l;
	}
	
	public boolean hilight;
	
	public void setHilgiht(boolean h){
		this.hilight = h;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			isPressed = true;
			break;
		}
		case MotionEvent.ACTION_CANCEL:{
			isPressed = false;
			break;
		}
		case MotionEvent.ACTION_UP: {
			isPressed = false;
			if(event.getX() > 0 && event.getX() < getWidth() && event.getY() > 0 && event.getY() < getHeight()){
				//released inside
				if(mOnClickListener != null){
					mOnClickListener.onClick(this);
				}
			}
			break;
		}
		}
		invalidate();
		return true;
	}
	
	private RectF mBorderRect = new RectF();
	private Paint mPaint = null;
	
	private Rect rect = new Rect();
	private int mStrokeColor = Color.GREEN;
	private int mFillColor = Color.CYAN;
	private int mHilightColor = Color.GRAY;
	
	{
		if(!isInEditMode()){
			mStrokeColor = BaseApplication.getContext().getResources().getColor(R.color.umiwi_orange);
			mFillColor = mStrokeColor;
		}
	}
	
	public int getmStrokeColor() {
		return mStrokeColor;
	}

	public void setmStrokeColor(int mStrokeColor) {
		this.mStrokeColor = mStrokeColor;
	}


	Rect bounds  = new Rect();
	
	@Override
	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
		canvas.getClipBounds(rect);
		mBorderRect.set(rect.left + 4,rect.top + 2,rect.right - 8,rect.bottom - 8);
		mPaint.setAlpha(255);
		
		
		if(getText() != null){
			float textWidth = mPaint.measureText(getText().toString());
			mPaint.getTextBounds(getText().toString(), 0, getText().length(), bounds);
			float x = (getWidth() - textWidth) / 2;
			int y = (int) ((getHeight() - bounds.height()) / 2 - mPaint.getFontMetricsInt().ascent - mPaint.getFontMetricsInt().descent + mPaint.getFontMetrics().bottom / 2);
			if(isPressed || hilight){
				if(!isPressed){
					mPaint.setAlpha((int) (0.5 * 255));
					
					int r = Color.red(mFillColor);
					int g = Color.green(mFillColor);
					int b = Color.blue(mFillColor);
					mPaint.setColor(Color.argb(160, r, g, b));
				}else{
					mPaint.setColor(mFillColor);
				}
				mPaint.setStyle(Style.FILL_AND_STROKE);
			}else{
				mPaint.setColor(mStrokeColor);
				mPaint.setStyle(Style.STROKE);
			}
			
			if(drawBorder){
				if(isInEditMode()){
					mPaint.setColor(Color.GREEN);
					canvas.drawRoundRect(mBorderRect, 4, 4, mPaint);
				}else{
					canvas.drawRoundRect(mBorderRect, DimensionUtil.dip2px(2), DimensionUtil.dip2px(2), mPaint);
				}
			}

			if(isPressed || hilight){
				mPaint.setColor(mHilightColor);
			}else{
				mPaint.setColor(mStrokeColor);
			}
			mPaint.setTextSize(getTextSize());
			canvas.drawText(getText().toString(), x, y, mPaint);
		}
	}
}
