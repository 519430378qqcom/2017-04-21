
package cn.youmi.framework.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

import cn.youmi.framework.main.BaseApplication;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * 
 * @author tangxiyong 2013-11-21下午6:19:01
 * 
 */
public class CacheUtils {
	
	private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }
    public static File getOwnCacheDirectory(String cacheDir) {
		File appCacheDir = null;
		if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(BaseApplication.getContext())) {
			appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
		}
		if (appCacheDir == null || (!appCacheDir.exists() && !appCacheDir.mkdirs())) {
			appCacheDir = BaseApplication.getContext().getCacheDir();
		}
		return appCacheDir;
	}

	public static File getDiskCacheDir() {
		String cachePath;
		if (BaseApplication.getContext() == null) {
			return new File("/data/data/com.umiwi.ui/cache/network");
		}
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = BaseApplication.getContext().getExternalCacheDir().getPath();
		} else {
			cachePath = BaseApplication.getContext().getCacheDir().getPath();
		}

		if (!new File(cachePath).exists()) {
			return new File("/data/data/com.umiwi.ui/cache/network");
		}

		if (TextUtils.isEmpty(cachePath)) {
			return new File("/data/data/com.umiwi.ui/cache/network");
		}

		return new File(cachePath + File.separator + "network");
	}


	private static boolean hasExternalStoragePermission(Context context) {
		int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
		return perm == PackageManager.PERMISSION_GRANTED;
	}
}
