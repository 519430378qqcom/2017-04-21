package com.umiwi.ui.view;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import cn.youmi.framework.util.DimensionUtil;

import com.umiwi.ui.beans.SearchCloudBean;

/**
 * @Author buhe 2014-6-10下午4:25:55
 */
public class GlobTagView extends View implements OnGestureListener {
	
	public interface OnTagClickListener{
		void onTagClick(SearchCloudBean tag);
	}

	private ArrayList<TDPoint> points = new ArrayList<GlobTagView.TDPoint>();
	private OnTagClickListener mOnTagClickListener;
	public void setOnTagClickListener(OnTagClickListener l){
		this.mOnTagClickListener = l;
	}
	
	private static class TDPoint {
		double x;
		double y, z;

		int color;
		RectF rect;
		private SearchCloudBean data;

		TDPoint(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.color = Color.BLACK;
			this.rect = new RectF();

		}

		@Override
		public String toString() {
			return "TDPoint [x=" + x + ", y=" + y + ", z=" + z + "]";
		}
	}

	static int PFAxisDirectionNone = 0;
	static int PFAxisDirectionPositive = 1;
	static int PFAxisDirectionNegative = -1;

	class CGPoint {
		double x, y;
	}

	private static class PFMatrix {
		private static final int matrixSize = 4;

		private int m, n;
		double[][] data = new double[matrixSize][matrixSize];

		public PFMatrix(int m, int n) {
			this.m = m;
			this.n = n;
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					data[i][j] = 0;
				}
			}
		}

		static PFMatrix PFMatrixMakeFromArray(int m, int n, double[][] data) {
			PFMatrix matrix = new PFMatrix(m, n);
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					matrix.data[i][j] = data[i][j];
				}
			}
			return matrix;
		}

		static PFMatrix PFMatrixMakeIdentity(int m, int n) {
			PFMatrix matrix = new PFMatrix(m, n);
			for (int i = 0; i < m; i++) {
				matrix.data[i][i] = 1;
			}
			return matrix;
		}
		
		static PFMatrix PFMatrixMakeIdentity(PFMatrix matrix,int m, int n) {
			for (int i = 0; i < m; i++) {
				matrix.data[i][i] = 1;
			}
			return matrix;
		}

		static PFMatrix PFMatrixMultiply(PFMatrix A, PFMatrix B) {
			PFMatrix R = new PFMatrix(A.m, B.n);

			for (int i = 0; i < A.m; i++) {
				for (int j = 0; j < B.n; j++) {
					for (int k = 0; k < A.n; k++) {
						R.data[i][j] += A.data[i][k] * B.data[k][j];
					}
				}
			}
			return R;
		}

		static PFMatrix PFMatrixTransform3DMakeFromPFPoint(TDPoint point) {
			double[][] pointRef = new double[][] { { point.x, point.y, point.z,
					1 } };
			PFMatrix matrix = PFMatrixMakeFromArray(1, 4, pointRef);
			return matrix;
		}

		static PFMatrix PFMatrixTransform3DMakeTranslation(TDPoint point) {
			double T[][] = new double[][] { { 1, 0, 0, 0 }, { 0, 1, 0, 0 },
					{ 0, 0, 1, 0 }, { point.x, point.y, point.z, 1 } };

			PFMatrix matrix = PFMatrixMakeFromArray(4, 4, T);

			return matrix;
		}

		static double PFRadianMake(double grades) {
			return (Math.PI * grades / 180.0);
		}

		static PFMatrix PFMatrixTransform3DMakeXRotation(double angle) {
			double c = Math.cos(PFRadianMake(angle));
			double s = Math.sin(PFRadianMake(angle));

			double T[][] = { { 1, 0, 0, 0 }, { 0, c, s, 0 }, { 0, -s, c, 0 },
					{ 0, 0, 0, 1 } };

			PFMatrix matrix = PFMatrixMakeFromArray(4, 4, T);
			return matrix;
		}

		static PFMatrix PFMatrixTransform3DMakeXRotationOnPoint(TDPoint point,
				double angle) {
			PFMatrix T = PFMatrixTransform3DMakeTranslation(new TDPoint(
					-point.x, -point.y, -point.z));
			PFMatrix R = PFMatrixTransform3DMakeXRotation(angle);
			PFMatrix T1 = PFMatrixTransform3DMakeTranslation(point);
			return PFMatrixMultiply(PFMatrixMultiply(T, R), T1);
		}

		static PFMatrix PFMatrixTransform3DMakeYRotation(double angle) {
			double c = Math.cos(PFRadianMake(angle));
			double s = Math.sin(PFRadianMake(angle));

			double T[][] = { { c, 0, -s, 0 }, { 0, 1, 0, 0 }, { s, 0, c, 0 },
					{ 0, 0, 0, 1 } };

			PFMatrix matrix = PFMatrixMakeFromArray(4, 4, T);
			return matrix;
		}

		static PFMatrix PFMatrixTransform3DMakeYRotationOnPoint(TDPoint point,
				double angle) {
			PFMatrix T = PFMatrixTransform3DMakeTranslation(new TDPoint(
					-point.x, -point.y, -point.z));
			PFMatrix R = PFMatrixTransform3DMakeYRotation(angle);
			PFMatrix T1 = PFMatrixTransform3DMakeTranslation(point);
			return PFMatrixMultiply(PFMatrixMultiply(T, R), T1);
		}

		static PFMatrix PFMatrixTransform3DMakeZRotation(double angle) {
			double c = Math.cos(PFRadianMake(angle));
			double s = Math.sin(PFRadianMake(angle));

			double T[][] = { { c, s, 0, 0 }, { -s, c, 0, 0 }, { 0, 0, 1, 0 },
					{ 0, 0, 0, 1 } };

			PFMatrix matrix = PFMatrixMakeFromArray(4, 4, T);

			return matrix;
		}

		static PFMatrix PFMatrixTransform3DMakeZRotationOnPoint(TDPoint point,
				Double angle) {
			PFMatrix T = PFMatrixTransform3DMakeTranslation(new TDPoint(
					-point.x, -point.y, -point.z));
			PFMatrix R = PFMatrixTransform3DMakeZRotation(angle);
			PFMatrix T1 = PFMatrixTransform3DMakeTranslation(point);

			return PFMatrixMultiply(PFMatrixMultiply(T, R), T1);
		}

		static void PFPointMakeFromMatrix(PFMatrix matrix, TDPoint point) {
			// matrix.data[0][2]);
			point.x = matrix.data[0][0];
			point.y = matrix.data[0][1];
			point.z = matrix.data[0][2];
		}

		static double PFAxisDirectionMinimumDistance = 0.033f;

		static int PFAxisDirectionMake(double fromCoordinate,
				double toCoordinate, boolean sensitive) {
			int direction = PFAxisDirectionNone;

			double distance = Math.abs(fromCoordinate - toCoordinate);

			if (distance > PFAxisDirectionMinimumDistance || sensitive) {
				if (fromCoordinate > toCoordinate) {
					direction = PFAxisDirectionPositive;
				} else if (fromCoordinate < toCoordinate) {
					direction = PFAxisDirectionNegative;
				}
			}

			return direction;
		}

		static int PFDirectionMakeXAxis(CGPoint fromPoint, CGPoint toPoint) {
			return PFAxisDirectionMake(fromPoint.x, toPoint.x, false);
		}

		static int PFDirectionMakeYAxis(CGPoint fromPoint, CGPoint toPoint) {
			return PFAxisDirectionMake(fromPoint.y, toPoint.y, false);
		}

		static int PFDirectionMakeXAxisSensitive(CGPoint fromPoint,
				CGPoint toPoint) {
			return PFAxisDirectionMake(fromPoint.x, toPoint.x, true);
		}

		static int PFDirectionMakeYAxisSensitive(CGPoint fromPoint,
				CGPoint toPoint) {
			return PFAxisDirectionMake(fromPoint.y, toPoint.y, true);
		}
	}

	public GlobTagView(Context context) {
		super(context);
		init();
	}

	public GlobTagView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		gd = new GestureDetector(getContext(), this);
	}

	public void setTags(ArrayList<SearchCloudBean> tags) {
		points.clear();
		int n = tags.size();
		double inc = Math.PI * (3 - Math.sqrt(5));
		double off = 2 * 1.0 / n * 1.0;
		for (int k = 0; k < n; k++) {
			double y = k * off - 1 + (off / 2);
			double r = Math.sqrt(1 - y * y);
			double phi = k * inc;
			TDPoint point = new TDPoint(Math.cos(phi) * r, y, Math.sin(phi) * r);
			points.add(point);
			point.data = tags.get(k);
		}
		invalidate();
	}

	Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	{
		mPaint.setColor(Color.RED);
	}

	static Random random = new Random();
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = getWidth() / 2 - 80;
		
		if (points == null) {
			return;
		}
		long start = System.currentTimeMillis();
		
		float dip40 = DimensionUtil.dip2px(40);
		float dip10 = DimensionUtil.dip2px(10);
		for (TDPoint p : points) {
			float size = (float) ((1 + p.z) * dip40 + dip10);
			mPaint.setTextSize(size);
			mPaint.setColor(p.color);
			mPaint.setTextAlign(Align.LEFT);

			int alpah = (int) ((1 + p.z) * 100) + 50;
			alpah = Math.min(alpah, 255);
			alpah = Math.max(alpah, 0);
			mPaint.setAlpha(alpah);
			
			long mstart = System.currentTimeMillis();
			float x = (float) (p.x * width + width);
			x = x - mPaint.measureText(p.data.getTitle()) / 2;
			float y = (float) (p.y * width + getHeight() / 2);
			

			int textWidth = (int) mPaint.measureText(p.data.getTitle());
			p.rect.left = x;
			p.rect.top = y - size;
			p.rect.right = p.rect.left + textWidth;
			p.rect.bottom = y;
			canvas.drawText(p.data.getTitle(), x, y, mPaint);
		}
	}
	
	TDPoint mOriginPoint = new TDPoint(0, 0, 0);
	CGPoint mStartPoint = new CGPoint();
	CGPoint mEndPoint = new CGPoint();
	
	PFMatrix mTransformMatrix = PFMatrix.PFMatrixMakeIdentity(4, 4);
	
	private void rotate(float sx, float sy, float dx, float dy) {
		long start = System.currentTimeMillis();
		if (Math.abs(sy - dy) < 4) {
			sy = dy;
		}
		if (Math.abs(sx - dx) < 4) {
			sx = dx;
		}
		double angle = 8;
		mStartPoint.x = sx;
		mStartPoint.y = sy;

		mEndPoint.x = dx;
		mEndPoint.y = dy;

		
		for (int i = 0; i < points.size(); i++) {
			TDPoint point = points.get(i);
			PFMatrix coordinate = PFMatrix
					.PFMatrixTransform3DMakeFromPFPoint(point);

			int xAxisDirection = PFMatrix.PFDirectionMakeXAxis(mStartPoint,
					mEndPoint);
			PFMatrix transform = mTransformMatrix;
			if (xAxisDirection != PFAxisDirectionNone) {
				transform = PFMatrix.PFMatrixMultiply(mTransformMatrix, PFMatrix
						.PFMatrixTransform3DMakeYRotationOnPoint(mOriginPoint,
								xAxisDirection * -angle));
			}

			int yAxisDirection = PFMatrix.PFDirectionMakeYAxis(mStartPoint,
					mEndPoint);
			if (yAxisDirection != PFAxisDirectionNone) {
				transform = PFMatrix.PFMatrixMultiply(transform, PFMatrix
						.PFMatrixTransform3DMakeXRotationOnPoint(mOriginPoint,
								yAxisDirection * angle));
			}

			PFMatrix.PFPointMakeFromMatrix(
					PFMatrix.PFMatrixMultiply(coordinate, transform), point);
		}
		java.util.Collections.sort(points, new Comparator<TDPoint>() {
			@Override
			public int compare(TDPoint lhs, TDPoint rhs) {
				if (lhs.z > rhs.z) {
					return -1;
				} else {
					return 1;
				}
			}
		});
	}

	private float startX, startY;

	private GestureDetector gd;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float x = event.getX() - getWidth() / 2;
			float y = event.getY() - getHeight() / 2;
			if (Math.sqrt(x * x + y * y) > getWidth() / 2) {
				return false;
			}
		}
		gd.onTouchEvent(event);
		startX = event.getX();
		startY = event.getY();
		
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		mScroller.forceFinished(true);
		invalidate();
		return true;
	}

	Scroller mScroller = new Scroller(getContext());

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		mScroller.startScroll(0, 20, 0, 0, 3000);
		invalidate();
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		// float disX = Math.abs(event.getX() - startX);
		// float disY = Math.abs(event.getY() - startY);
		// if(Math.abs(distanceX) < 0.2 || Math.abs(distanceX) < 0.2){
		// return true;
		// }
		rotate(startX, startY, e2.getX(), e2.getY());
		// startX = event.getX();
		// startY = event.getY();
		invalidate();
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	Rect mRect = new Rect();

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		
		for(int i = 0;i < points.size();i++){
			TDPoint p = points.get(i);
//			Log.e("xx","p:" + p.data.getTitle() + " x:" + e.getX() + "y:" + e.getY() + "|" + p.rect);
			if(p.rect.contains(e.getX(), e.getY())){
				if(mOnTagClickListener != null){
					mOnTagClickListener.onTagClick(p.data);
				}
				return true;
			}
		}
		if(mOnTagClickListener != null){
			mOnTagClickListener.onTagClick(null);
		}
		return true;
	}
}
