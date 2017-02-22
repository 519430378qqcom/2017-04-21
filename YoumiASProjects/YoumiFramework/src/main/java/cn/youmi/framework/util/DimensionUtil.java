package cn.youmi.framework.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;

import cn.youmi.framework.main.BaseApplication;

/**
 * @Author buhe 2014-6-13下午2:51:13
 */
public class DimensionUtil {
	public static int dip2px(int dipValue) {
		float reSize = BaseApplication.getContext().getResources()
				.getDisplayMetrics().density;
		return (int) ((dipValue * reSize) + 0.5);
	}

	public static int px2dip(int pxValue) {
		float reSize = BaseApplication.getContext().getResources()
				.getDisplayMetrics().density;
		return (int) ((pxValue / reSize) + 0.5);
	}

	/** dip转px */
	public static int dip2px(float dip) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dip, BaseApplication.getContext().getResources()
						.getDisplayMetrics());
	}

	public static int px2sp(float pxValue) {
		final float fontScale = BaseApplication.getContext().getResources()
				.getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	public static int sp2px(float spValue) {
		final float fontScale = BaseApplication.getContext().getResources()
				.getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public static int getScreenWidth(Activity activity) {
		if (activity != null) {
			Display display = activity.getWindowManager().getDefaultDisplay();
			DisplayMetrics metrics = new DisplayMetrics();
			display.getMetrics(metrics);
			return metrics.widthPixels;
		}
		return 480;
	}

	/** 获取屏幕高px */
	public static int getScreenHeight(Activity activity) {
		if (activity != null) {
			Display display = activity.getWindowManager().getDefaultDisplay();
			DisplayMetrics metrics = new DisplayMetrics();
			display.getMetrics(metrics);
			return metrics.heightPixels;
		}
		return 800;
	}
	
}
