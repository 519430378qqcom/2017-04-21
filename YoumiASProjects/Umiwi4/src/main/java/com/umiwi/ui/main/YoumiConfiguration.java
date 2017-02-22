package com.umiwi.ui.main;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by tangixyong on 16/1/14.
 */
public abstract class YoumiConfiguration {
    public static Context context;
    private static String DOWNLOAD_PATH = "";


    public YoumiConfiguration(Context applicationContext) {
        context = applicationContext;
        DOWNLOAD_PATH = this.configDownloadPath();
    }

    public static String getDownloadPath() {
        if (!TextUtils.isEmpty(DOWNLOAD_PATH)) {
            if (!DOWNLOAD_PATH.startsWith("/")) {
                DOWNLOAD_PATH = "/" + DOWNLOAD_PATH;
            }

            if (DOWNLOAD_PATH.endsWith("/")) {
                DOWNLOAD_PATH = DOWNLOAD_PATH + "/";
            }
        } else {
            String pkg = context.getPackageName();
            DOWNLOAD_PATH = "/" + pkg + "/videocache/";
        }

        return DOWNLOAD_PATH;
    }

    public abstract String configDownloadPath();
}
