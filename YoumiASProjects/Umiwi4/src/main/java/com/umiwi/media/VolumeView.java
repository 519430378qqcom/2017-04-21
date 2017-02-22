package com.umiwi.media;

import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import cn.youmi.framework.util.DimensionUtil;

import com.umiwi.ui.R;

public class VolumeView extends View {

	private int mCurrentVolume = 18;
	private int mMaxVolume = 20;
	private Drawable mThumb;
	private Drawable mVolumeDrawable;
	private Drawable mMuteDrawable;
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private RectF mRect = new RectF();
	private AudioManager mAudioManager;

	public void setVolume(int volume) {
		if (volume == mCurrentVolume) {
			return;
		}
		mCurrentVolume = volume;
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
				mCurrentVolume, 0);
		this.invalidate();
	}

	public void setMaxVolume(int max) {
		this.mMaxVolume = max;
		this.invalidate();
	}

	public VolumeView(Context context) {
		super(context);
		init();
	}

	public VolumeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	Handler mHandler = new Handler();
	private ContentObserver mVolumeObserver = new ContentObserver(mHandler) {
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			int volume = mAudioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC);
			setVolume(volume);
		}
	};

	private void init() {
		mThumb = getResources().getDrawable(
				R.drawable.video_player_seekbar_thumb);
		mVolumeDrawable = getResources().getDrawable(R.drawable.ic_volume);
		mMuteDrawable = getResources().getDrawable(R.drawable.ic_mute);
		if (isInEditMode()) {
			return;
		}
		mAudioManager = (AudioManager) getContext().getSystemService(
				Context.AUDIO_SERVICE);
		mMaxVolume = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mCurrentVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
	}

	private int dip2px(int dipValue) {
		float reSize = getResources().getDisplayMetrics().density;
		return (int) ((dipValue * reSize) + 0.5);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		if (height > dip2px(200)) {
			height = dip2px(200);
		}
		setMeasuredDimension(width, height);// TODO
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = getWidth();
		int lineWith = 4;
		int offset = 2;

		mPaint.setColor(Color.GRAY);
		mRect.set(width / 2 - lineWith / 2 - offset, getTopOffset(), width / 2
				+ lineWith - offset, getHeight() - getBottomOffset());
		canvas.drawRoundRect(mRect, 4, 4, mPaint);
		int y = getVolumeBarHeight() + getTopOffset() - getWidth() / 2;
		y = y - (int) (1.0f * mCurrentVolume / mMaxVolume * getVolumeBarHeight());

		int size = 30;
		if (!isInEditMode()) {
			size = DimensionUtil.dip2px(25);
		}

		int l = (width - size) / 2 - 2;
		mThumb.setBounds(l, y, l + size, y + size);
		mRect.set(mRect.left, y + getWidth() / 2, mRect.right, mRect.bottom);
		mPaint.setColor(Color.parseColor("#ff6600"));
		canvas.drawRoundRect(mRect, 4, 4, mPaint);
		// canvas.drawText("" + y, 0, 20, mPaint);
		mThumb.draw(canvas);

		if (mCurrentVolume == 0) {
			mMuteDrawable.setBounds(2, getHeight() - getWidth(), getWidth()
					- offset * 2, getHeight() - offset * 2);
			mMuteDrawable.draw(canvas);
		} else {
			mVolumeDrawable.setBounds(2, getHeight() - getWidth(), getWidth()
					- offset * 2, getHeight() - offset * 2);
			mVolumeDrawable.draw(canvas);
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
//		Uri uri = Settings.System
//				.getUriFor(Settings.System.VOLUME_SETTINGS[AudioManager.STREAM_MUSIC]);
//		Uri uri = Settings.System.getUriFor(Settings.System.);
//		getContext().getContentResolver().registerContentObserver(uri, true,
//				mVolumeObserver);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		getContext().getContentResolver().unregisterContentObserver(
				mVolumeObserver);
	}

	private int getVolumeBarHeight() {
		return getHeight() - getTopOffset() - getBottomOffset();
	}

	private int getTopOffset() {
		return (int) (0.5f * getWidth());
	}

	private int getBottomOffset() {
		return (int) (1.2 * getWidth());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int volumeBarHeight = getVolumeBarHeight();
		int volume = mMaxVolume
				- (int) (this.mMaxVolume * (event.getY() - 0.5 * getWidth()) / volumeBarHeight);
		volume = Math.max(volume, 0);
		volume = Math.min(volume, this.mMaxVolume);
		setVolume(volume);
		return true;
	}
}
