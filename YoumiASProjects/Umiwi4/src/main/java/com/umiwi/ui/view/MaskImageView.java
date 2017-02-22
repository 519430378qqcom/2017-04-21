package com.umiwi.ui.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Scroller;

public class MaskImageView extends ImageView {

	ArrayList<Bitmap> images = null;
	
	public static final int IMAGE_WIDTH = 640;
	public static final int IMAGE_HEIGHT = 1136;
	
	public static final int LOAD_IMAGE_WIDTH = 400;
	public static final int LOAD_IMAGE_HEIGHT = 400;
	private boolean over;
	
	public static ArrayList<Bitmap> slice(Bitmap b){
		ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>(5);
		bitmaps.add(Bitmap.createBitmap(b, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT));
		bitmaps.add(Bitmap.createBitmap(b, IMAGE_WIDTH, 0, IMAGE_WIDTH, IMAGE_HEIGHT));
		
		for(int i = 0;i < 3;i++){
			bitmaps.add(Bitmap.createBitmap(b, IMAGE_WIDTH * 2, i * LOAD_IMAGE_HEIGHT, LOAD_IMAGE_WIDTH, LOAD_IMAGE_HEIGHT));
		}
		return bitmaps;
	}

	public MaskImageView(Context context) {
		super(context);
		init();
	}

	
	
	@Override
	public void setImageBitmap(Bitmap bm) {
		// super.setImageBitmap(bm);
		images = slice(bm);
		invalidate();
	}

	public MaskImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	Scroller mScroller;
	Paint mPaint = new Paint();
	Rect bgSrcRect = new Rect();
	Rect bgDestRect = new Rect();

	Rect coverSrcRect = new Rect();
	Rect coverDestrect = new Rect();

	private void init() {
		mScroller = new Scroller(getContext(), new LinearInterpolator());
	}

	public void start() {
		init();
		mScroller.startScroll(0, 0, 100, 0, 3000);

		mScroller.extendDuration(2000);
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (images == null) {
			return;
		}
		// TODO scale to fit ?
		float radio = 1.0f * getWidth() / images.get(0).getWidth();

		bgDestRect.set(0, 0, getWidth(),
				(int) (images.get(0).getHeight() * radio));
		int left = (int) ((getWidth() - 400 * radio) / 2);
		int top = (int) (200 * radio);
		int right = (int) (left + 400 * radio);
		int bottom = (int) (top + 400 * radio);
		coverDestrect.set(left, top, right, bottom);
		canvas.drawBitmap(images.get(0), null, bgDestRect, null);
		if (!mScroller.isFinished()) {
			canvas.drawBitmap(images.get(2), null, coverDestrect, null);
		}


		if (mScroller.computeScrollOffset()) {
			invalidate();

			bgSrcRect.set((int) (mScroller.getCurrX() / 100.0f * images.get(1)
					.getWidth()), 0, images.get(1).getWidth(), images.get(1)
					.getHeight());
			bgDestRect.set((int) (mScroller.getCurrX() / 100.0f * getWidth()),
					0, getWidth(), (int) (images.get(0).getHeight() * radio));
			canvas.drawBitmap(images.get(1), bgSrcRect, bgDestRect, null);
		}
		float p = 0;
		if (mScroller.computeScrollOffset()) {
			float l = mScroller.getCurrX() / 100.0f * getWidth() - left;
			if (l > 0 && l < 400 * radio) {
				p = (float) (l / (400.0 * radio));

			}
			over = l >= 400 * radio;
		}
		


		coverSrcRect.set((int) (p * images.get(2).getWidth()), 0, images.get(2)
				.getWidth(), images.get(2).getHeight());
		coverDestrect.set((int) (left + p * images.get(2).getWidth() * radio),
				top, right, bottom);
		
		if(!over){
			canvas.drawBitmap(images.get(3), coverSrcRect, coverDestrect, null);
		}else{
			canvas.drawBitmap(images.get(2), coverSrcRect, coverDestrect, null);
			p = 1;
		}
		
		coverDestrect.set(left, top, right, bottom);
		mPaint.setAlpha((int) (50 + 160 * p));
		canvas.drawBitmap(images.get(4), null, coverDestrect,mPaint);
			

//		Log.e("xx", "src:" + coverSrcRect + "  " + coverDestrect);

	}
}
