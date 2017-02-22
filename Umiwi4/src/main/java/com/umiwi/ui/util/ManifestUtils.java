package com.umiwi.ui.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

/**
 * Created by umiwi on 15/6/24.
 */
public class ManifestUtils {

    /**
     * @param context
     * @return UMENG_CHANNEL
     */
    public static String getChannelString(Context context) {
        return getManifestValue(context, "UMENG_CHANNEL");
    }

    public static String getManifestValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return apiKey;
    }


}
