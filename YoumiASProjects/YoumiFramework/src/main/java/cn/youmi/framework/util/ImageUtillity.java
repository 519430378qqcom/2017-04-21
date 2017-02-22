package cn.youmi.framework.util;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

/**
 * @author tangxiyong
 * @version 2014年10月22日 下午8:41:56
 */
public class ImageUtillity {
	public static boolean isThisBitmapCanRead(String path) {
		if (TextUtils.isEmpty(path)) {
			return false;
		}
		File file = new File(path);

		if (!file.exists()) {
			return false;
		}

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		int width = options.outWidth;
		int height = options.outHeight;
		return !(width == -1 || height == -1);

	}

	public static Bitmap decodeBitmapFromSDCard(String path, int reqWidth,
			int reqHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(path, options);

	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (height > reqHeight && reqHeight != 0) {
				inSampleSize = (int) Math.floor((double) height / (double) reqHeight);
			}

			int tmp = 0;

			if (width > reqWidth && reqWidth != 0) {
				tmp = (int) Math.floor((double) width / (double) reqWidth);
			}

			inSampleSize = Math.max(inSampleSize, tmp);

		}
		int roundedSize;
		if (inSampleSize <= 8) {
			roundedSize = 1;
			while (roundedSize < inSampleSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (inSampleSize + 7) / 8 * 8;
		}

		return roundedSize;
	}
}
