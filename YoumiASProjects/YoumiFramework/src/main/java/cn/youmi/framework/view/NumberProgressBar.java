package cn.youmi.framework.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import cn.youmi.framework.R;

public class NumberProgressBar extends View {

    private int mMax = 100;

    private int mProgress = 0;

    private int mChangeBarColor;

    private int mUnchangeBarColor;

    private int mTextColor;

    private float mTextSize;

    private float mChangeBarHeight;

    private float mUnchangeBarHeight;

    private final int default_text_color = Color.rgb(66, 145, 241);
    private final int default_change_color = Color.rgb(66,145,241);
    private final int default_unchange_color = Color.rgb(204, 204, 204);
    private final float default_progress_text_offset;
    private final float default_text_size;
    private final float default_change_bar_height;
    private final float default_unchange_bar_height;

    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_TEXT_COLOR = "text_color";
    private static final String INSTANCE_TEXT_SIZE = "text_size";
    private static final String INSTANCE_CHANGE_BAR_HEIGHT = "change_bar_height";
    private static final String INSTANCE_CHANGE_BAR_COLOR = "change_bar_color";
    private static final String INSTANCE_UNCHANGE_BAR_HEIGHT = "unchange_bar_height";
    private static final String INSTANCE_UNCHANGE_BAR_COLOR = "unchange_bar_color";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_PROGRESS = "progress";

    private static final int PROGRESS_TEXT_VISIBLE = 0;
    private static final int PROGRESS_TEXT_INVISIBLE = 1;

    private float mDrawTextWidth;

    private float mDrawTextStart;

    private float mDrawTextEnd;

    private String mCurrentDrawText;

    private Paint mChangeBarPaint;
    private Paint mUnchangeBarPaint;
    private Paint mTextPaint;

    private RectF mUnchangeRectF = new RectF(0,0,0,0);
    private RectF mChangeRectF = new RectF(0,0,0,0);

    private float mOffset;

    private boolean mDrawUnchangeBar = true;

    private boolean mDrawChangeBar = true;

    private boolean mIfDrawText = true;

    public enum ProgressTextVisibility{
        Visible,Invisible
    }


    public NumberProgressBar(Context context) {
        this(context, null);
    }

    public NumberProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.numberProgressBarStyle);
    }

    public NumberProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        default_change_bar_height = dp2px(1.5f);
        default_unchange_bar_height = dp2px(1.0f);
        default_text_size = sp2px(10);
        default_progress_text_offset = dp2px(3.0f);

        //load styled attributes.
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NumberProgressBar,
                defStyleAttr, 0);

        mChangeBarColor = attributes.getColor(R.styleable.NumberProgressBar_progress_change_color, default_change_color);
        mUnchangeBarColor = attributes.getColor(R.styleable.NumberProgressBar_progress_unchange_color,default_unchange_color);
        mTextColor = attributes.getColor(R.styleable.NumberProgressBar_progress_text_color,default_text_color);
        mTextSize = attributes.getDimension(R.styleable.NumberProgressBar_progress_text_size, default_text_size);

        mChangeBarHeight = attributes.getDimension(R.styleable.NumberProgressBar_progress_change_bar_height,default_change_bar_height);
        mUnchangeBarHeight = attributes.getDimension(R.styleable.NumberProgressBar_progress_unchange_bar_height,default_unchange_bar_height);
        mOffset = attributes.getDimension(R.styleable.NumberProgressBar_progress_text_offset,default_progress_text_offset);

        int textVisible = attributes.getInt(R.styleable.NumberProgressBar_progress_text_visibility,PROGRESS_TEXT_VISIBLE);
        if(textVisible != PROGRESS_TEXT_VISIBLE){
            mIfDrawText = false;
        }

        setProgress(attributes.getInt(R.styleable.NumberProgressBar_progress,0));
        setMax(attributes.getInt(R.styleable.NumberProgressBar_max, 100));
        //
        attributes.recycle();

        initializePainters();

    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return (int)mTextSize;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return Math.max((int)mTextSize,Math.max((int)mChangeBarHeight,(int)mUnchangeBarHeight));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec,true), measure(heightMeasureSpec,false));
    }

    private int measure(int measureSpec,boolean isWidth){
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int padding = isWidth?getPaddingLeft()+getPaddingRight():getPaddingTop()+getPaddingBottom();
        if(mode == MeasureSpec.EXACTLY){
            result = size;
        }else{
            result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
            result += padding;
            if(mode == MeasureSpec.AT_MOST){
                if(isWidth) {
                    result = Math.max(result, size);
                }
                else{
                    result = Math.min(result, size);
                }
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mIfDrawText){
            calculateDrawRectF();
        }else{
            calculateDrawRectFWithoutProgressText();
        }

        if(mDrawChangeBar){
            canvas.drawRect(mChangeRectF,mChangeBarPaint);
        }

        if(mDrawUnchangeBar) {
            canvas.drawRect(mUnchangeRectF, mUnchangeBarPaint);
        }

        if(mIfDrawText)
            canvas.drawText(mCurrentDrawText,mDrawTextStart,mDrawTextEnd,mTextPaint);
    }

    private void initializePainters(){
        mChangeBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mChangeBarPaint.setColor(mChangeBarColor);

        mUnchangeBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnchangeBarPaint.setColor(mUnchangeBarColor);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
    }


    private void calculateDrawRectFWithoutProgressText(){
        mChangeRectF.left = getPaddingLeft();
        mChangeRectF.top = getHeight()/2.0f - mChangeBarHeight / 2.0f;
        mChangeRectF.right = (getWidth() - getPaddingLeft() - getPaddingRight() )/(getMax()*1.0f) * getProgress() + getPaddingLeft();
        mChangeRectF.bottom = getHeight()/2.0f + mChangeBarHeight / 2.0f;

        mUnchangeRectF.left = mChangeRectF.right;
        mUnchangeRectF.right = getWidth() - getPaddingRight();
        mUnchangeRectF.top = getHeight()/2.0f +  - mUnchangeBarHeight / 2.0f;
        mUnchangeRectF.bottom = getHeight()/2.0f  + mUnchangeBarHeight / 2.0f;
    }

    private void calculateDrawRectF(){

        mCurrentDrawText = String.format("%d%%",getProgress()*100/getMax());
        mDrawTextWidth = mTextPaint.measureText(mCurrentDrawText);

        if(getProgress() == 0){
            mDrawChangeBar = false;
            mDrawTextStart = getPaddingLeft();
        }else{
            mDrawChangeBar = true;
            mChangeRectF.left = getPaddingLeft();
            mChangeRectF.top = getHeight()/2.0f - mChangeBarHeight / 2.0f;
            mChangeRectF.right = (getWidth() - getPaddingLeft() - getPaddingRight() )/(getMax()*1.0f) * getProgress() - mOffset + getPaddingLeft();
            mChangeRectF.bottom = getHeight()/2.0f + mChangeBarHeight / 2.0f;
            mDrawTextStart = (mChangeRectF.right + mOffset);
        }

        mDrawTextEnd =  (int) ((getHeight() / 2.0f) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2.0f)) ;

        if((mDrawTextStart + mDrawTextWidth )>= getWidth() - getPaddingRight()){
            mDrawTextStart = getWidth() - getPaddingRight() - mDrawTextWidth;
            mChangeRectF.right = mDrawTextStart - mOffset;
        }

        float unreachedBarStart = mDrawTextStart + mDrawTextWidth + mOffset;
        if(unreachedBarStart >= getWidth() - getPaddingRight()){
            mDrawUnchangeBar = false;
        }else{
            mDrawUnchangeBar = true;
            mUnchangeRectF.left = unreachedBarStart;
            mUnchangeRectF.right = getWidth() - getPaddingRight();
            mUnchangeRectF.top = getHeight()/2.0f +  - mUnchangeBarHeight / 2.0f;
            mUnchangeRectF.bottom = getHeight()/2.0f  + mUnchangeBarHeight / 2.0f;
        }
    }
    public int getTextColor() {
        return mTextColor;
    }

    public float getProgressTextSize() {
        return mTextSize;
    }

    public int getUnreachedBarColor() {
        return mUnchangeBarColor;
    }

    public int getReachedBarColor() {
        return mChangeBarColor;
    }

    public int getProgress() {
        return mProgress;
    }

    public int getMax() {
        return mMax;
    }

    public float getReachedBarHeight(){
        return mChangeBarHeight;
    }

    public float getUnreachedBarHeight(){
        return mUnchangeBarHeight;
    }



    public void setProgressTextSize(float TextSize) {
        this.mTextSize = TextSize;
        mTextPaint.setTextSize(mTextSize);
        invalidate();
    }

    public void setProgressTextColor(int TextColor) {
        this.mTextColor = TextColor;
        mTextPaint.setColor(mTextColor);
        invalidate();
    }

    public void setUnreachedBarColor(int BarColor) {
        this.mUnchangeBarColor = BarColor;
        mUnchangeBarPaint.setColor(mChangeBarColor);
        invalidate();
    }

    public void setReachedBarColor(int ProgressColor) {
        this.mChangeBarColor = ProgressColor;
        mChangeBarPaint.setColor(mChangeBarColor);
        invalidate();
    }

    public void setMax(int Max) {
        if(Max > 0){
            this.mMax = Max;
            invalidate();
        }
    }

    public void incrementProgressBy(int by){
        if(by > 0){
            setProgress(getProgress() + by);
        }
    }

    public void setProgress(int Progress) {
        if(Progress <= getMax()  && Progress >= 0){
            this.mProgress = Progress;
            invalidate();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE,super.onSaveInstanceState());
        bundle.putInt(INSTANCE_TEXT_COLOR,getTextColor());
        bundle.putFloat(INSTANCE_TEXT_SIZE, getProgressTextSize());
        bundle.putFloat(INSTANCE_CHANGE_BAR_HEIGHT,getReachedBarHeight());
        bundle.putFloat(INSTANCE_UNCHANGE_BAR_HEIGHT,getUnreachedBarHeight());
        bundle.putInt(INSTANCE_CHANGE_BAR_COLOR,getReachedBarColor());
        bundle.putInt(INSTANCE_UNCHANGE_BAR_COLOR,getUnreachedBarColor());
        bundle.putInt(INSTANCE_MAX,getMax());
        bundle.putInt(INSTANCE_PROGRESS,getProgress());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            final Bundle bundle = (Bundle)state;
            mTextColor = bundle.getInt(INSTANCE_TEXT_COLOR);
            mTextSize = bundle.getFloat(INSTANCE_TEXT_SIZE);
            mChangeBarHeight = bundle.getFloat(INSTANCE_CHANGE_BAR_HEIGHT);
            mUnchangeBarHeight = bundle.getFloat(INSTANCE_UNCHANGE_BAR_HEIGHT);
            mChangeBarColor = bundle.getInt(INSTANCE_CHANGE_BAR_COLOR);
            mUnchangeBarColor = bundle.getInt(INSTANCE_UNCHANGE_BAR_COLOR);
            initializePainters();
            setMax(bundle.getInt(INSTANCE_MAX));
            setProgress(bundle.getInt(INSTANCE_PROGRESS));
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

    public float sp2px(float sp){
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public void setProgressTextVisibility(ProgressTextVisibility visibility){
        mIfDrawText = visibility == ProgressTextVisibility.Visible;
        invalidate();
    }

}
