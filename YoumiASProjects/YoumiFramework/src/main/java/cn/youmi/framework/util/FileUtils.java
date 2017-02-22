package cn.youmi.framework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import android.text.TextUtils;

public class FileUtils {

	public static void copyFile(InputStream in, File destFile)
			throws IOException {
		BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
		FileOutputStream outputStream = new FileOutputStream(destFile);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
				outputStream);
		byte[] buffer = new byte[1024];
		int len;
		while ((len = bufferedInputStream.read(buffer)) != -1) {
			bufferedOutputStream.write(buffer, 0, len);
		}
		closeSilently(bufferedInputStream);
		closeSilently(bufferedOutputStream);
	}

	public static void closeSilently(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException ignored) {

			}
		}
	}

	/**
	 * 删除文件夹里面的所有文件
	 * 
	 * @param path String 文件夹路径 如 c:/fqf
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
			}
		}
	}

	// 将返回的分数折算成评分
	public static float convertScore2Rating(String scoreStr) {
		float scoreTemp = 0;

		if (TextUtils.isEmpty(scoreStr))
			scoreTemp = 0;
		try {
			scoreTemp = Float.parseFloat(scoreStr);
		} catch (NumberFormatException e) {
			scoreTemp = 0;
		}

		BigDecimal b = new BigDecimal(scoreTemp / 20.0f);
		float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();

		return f1;
	}

}
