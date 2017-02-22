package cn.youmi.framework.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;

public class ImageResizer {

	public enum ResizeMode {

		AUTOMATIC, FIT_TO_WIDTH, FIT_TO_HEIGHT, FIT_EXACT

	}

	public static boolean writeToFile(Bitmap image, File file) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 100, bytes);

		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bytes.toByteArray());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		if (reqWidth == -1) {
			reqWidth = options.outWidth;
		}

		if (reqHeight == -1) {
			reqHeight = options.outHeight;
		}

		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeFile(File bitmapFile, int reqWidth, int reqHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), options);
	}


	public static Bitmap resize(File original, int width, int height,
			ResizeMode mode) {
		Bitmap sampledSrcBitmap = decodeFile(original, width, height);
		if (sampledSrcBitmap == null) {
			return null;
		}

		return resize(sampledSrcBitmap, width, height, mode);
	}

	protected static Bitmap resize(Bitmap sampledSrcBitmap, int width,
			int height, ResizeMode mode) {
		int sourceWidth = sampledSrcBitmap.getWidth();
		int sourceHeight = sampledSrcBitmap.getHeight();

		if (mode == null || mode == ResizeMode.AUTOMATIC) {
			mode = calculateResizeMode(sourceWidth, sourceHeight);
		}

		if (mode == ResizeMode.FIT_TO_WIDTH) {
			height = calculateHeight(sourceWidth, sourceHeight, width);
		} else if (mode == ResizeMode.FIT_TO_HEIGHT) {
			width = calculateWidth(sourceWidth, sourceHeight, height);
		}

		return Bitmap.createScaledBitmap(sampledSrcBitmap, width, height, true);
	}

	private static ResizeMode calculateResizeMode(int width, int height) {
		if (width > height) {
			return ResizeMode.FIT_TO_WIDTH;
		}
		return ResizeMode.FIT_TO_HEIGHT;
	}

	private static int calculateWidth(int originalWidth, int originalHeight,
			int height) {
		return (int) Math.ceil(originalWidth
				/ ((double) originalHeight / height));
	}

	private static int calculateHeight(int originalWidth, int originalHeight,
			int width) {
		return (int) Math.ceil(originalHeight
				/ ((double) originalWidth / width));
	}

}
