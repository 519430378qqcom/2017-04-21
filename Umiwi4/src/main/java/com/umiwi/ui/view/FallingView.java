package com.umiwi.ui.view;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

public class FallingView extends View {

	public Bitmap correctBitmap;
	public Bitmap incorrectBitmap;
	public Bitmap currentBitmap;

	static class Sinfo {
		int x, y, size;
		int delta;
		int stage;
		float factor;
	}

	public FallingView(Context context) {
		super(context);
	}

	public FallingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private Scroller mScroller;
	private ArrayList<Sinfo> sinfos = new ArrayList<FallingView.Sinfo>();
	private Random r = new Random();

	public void start(boolean correct) {
		currentBitmap = correct ? correctBitmap : incorrectBitmap;
		Interpolator interpolator = new LinearInterpolator();
		mScroller = new Scroller(getContext(), interpolator);
		mScroller.startScroll(0, 0, getHeight() / 2, 800, 1100);
		sinfos.clear();

		int num = r.nextInt(12) + 6;
		int width = (int) (getWidth() / num * 1.3f);
		for (int i = 0; i < num; i++) {
			Sinfo s = new Sinfo();
			s.x = width * i - r.nextInt(10);
			s.y = r.nextInt(getHeight() / 2);
			s.factor = (r.nextInt(64) + 16.0f) / 24.0f;
			if (r.nextInt(2) == 0) {
				s.delta += r.nextInt(10);
			} else {
				s.delta -= r.nextInt(10);
			}
			sinfos.add(s);
		}
		invalidate();
	}

	Paint mPaint = new Paint();
	Matrix m = new Matrix();
	{
		mPaint.setColor(Color.YELLOW);
		mPaint.setTextSize(64);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (isInEditMode() || mScroller == null) {
			return;
		}
		if (mScroller.computeScrollOffset()) {
			invalidate();
			int offset = mScroller.getCurrX() / 50;
			offset = Math.max(1, offset);
			for (Sinfo s : sinfos) {
				if (offset != s.stage) {
					s.stage = offset;
				} 
				s.x = s.x + s.delta;
				int y = s.y + mScroller.getCurrX();
				m.reset();
				m.postTranslate(s.x, y);
				m.postScale(s.factor, s.factor);
				canvas.drawBitmap(currentBitmap, m, mPaint);
			}
		}
	}
}
