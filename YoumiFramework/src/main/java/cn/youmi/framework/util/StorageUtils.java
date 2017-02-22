package cn.youmi.framework.util;

import android.annotation.SuppressLint;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

@SuppressLint("NewApi")
public class StorageUtils {

    public static final String DOWNPATH = new File("youmi").getPath();

    public static final String APP_UPDATE_PATH = new File(DOWNPATH, "update").getPath();

    /**
     * 创建文件夹
     */
    public static void mkdir(File file) {
        if (!file.exists() || !file.isDirectory()) {
            if (!file.mkdirs()) {
                return;
            }
            try {
                new File(file, ".nomedia").createNewFile();
            } catch (IOException e) {
            }
        }

    }

    public static boolean isExternalStorageMounted() {

        boolean canRead = Environment.getExternalStorageDirectory().canRead();
        boolean onlyRead = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED_READ_ONLY);
        boolean unMounted = Environment.getExternalStorageState().equals(
                Environment.MEDIA_UNMOUNTED);
        return !(!canRead || onlyRead || unMounted);
    }


    /**
     * apk update
     */
    public static String getApkUpdatePath() {

        if (isExternalStorageMounted()) {
            File path = getAppUpdatePath();
            if (path != null) {
                return path.getAbsolutePath();
            } else {
            }
        } else {
            return "";
        }

        return "";

    }

    private static File getAppUpdatePath() {
        mkdir(new File(Environment.getExternalStorageDirectory(),
                APP_UPDATE_PATH));
        return new File(Environment.getExternalStorageDirectory(),
                APP_UPDATE_PATH);
    }

}
