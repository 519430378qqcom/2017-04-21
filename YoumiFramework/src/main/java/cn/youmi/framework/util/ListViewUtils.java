package cn.youmi.framework.util;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListViewUtils {
	private ListViewUtils() {

	}

	public static final int SCROLL_DURATION = 150;

//	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	public static void scrollListView(final ListView listView,
//			final int position) {
//		if (listView == null) {
//			return;
//		}
//		if (VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
//			listView.smoothScrollToPositionFromTop(position, 0);
//			listView.postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					// Mock touchEvent to stop listView Scrolling.
//					listView.onTouchEvent(MotionEvent.obtain(
//							System.currentTimeMillis(),
//							System.currentTimeMillis(),
//							MotionEvent.ACTION_DOWN, 0, 0, 0));
//				}
//			}, SCROLL_DURATION - 20);
//
//			listView.postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					listView.setSelectionFromTop(position, 0);
//				}
//			}, SCROLL_DURATION);
//		} else {
//			listView.setSelectionFromTop(position, 0);
//		}
//	}
//
//	public static void scrollListViewToTop(final ListView listView) {
//		if (listView == null) {
//			return;
//		}
//		scrollListView(listView, 0);
//	}
	/**
	 * 滚动列表到顶端
	 * 
	 * @param listView
	 */
	public static void smoothScrollListViewToTop(final ListView listView) {
		if (listView == null) {
			return;
		}
		smoothScrollListView(listView, 0);
		listView.postDelayed(new Runnable() {

			@Override
			public void run() {
				listView.setSelection(0);
			}
		}, 2 * 1000);
	}

	/**
	 * 滚动列表到position
	 * 
	 * @param listView
	 * @param position
	 * @param offset
	 * @param duration
	 */
	@SuppressLint("NewApi")
	public static void smoothScrollListView(ListView listView, int position) {
		if (VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
			listView.smoothScrollToPositionFromTop(0, 0);
		} else {
			listView.setSelection(position);
		}
	}

	public static void stopListViewScrollingAndScrollToTop(
			final ListView listView) {
		if (listView == null) {
			return;
		}
		if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			listView.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					listView.setSelection(0);
				}
			}, 2 * 1000);
			listView.dispatchTouchEvent(MotionEvent.obtain(
					SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
					MotionEvent.ACTION_CANCEL, 0, 0, 0));

		} else {
			listView.dispatchTouchEvent(MotionEvent.obtain(
					SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
					MotionEvent.ACTION_DOWN, 0, 0, 0));
			listView.dispatchTouchEvent(MotionEvent.obtain(
					SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
					MotionEvent.ACTION_UP, 0, 0, 0));
			listView.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					listView.setSelection(0);
				}
			}, 2 * 1000);
		}
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public static <V> int getSize(List<V> sourceList) {
		return sourceList == null ? 0 : sourceList.size();
	}

}
