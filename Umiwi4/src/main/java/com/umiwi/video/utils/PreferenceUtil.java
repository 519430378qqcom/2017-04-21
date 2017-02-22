package com.umiwi.video.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by txy on 15/9/9.
 */
public class PreferenceUtil {
    public PreferenceUtil() {
    }

    public static void savePreference(Context context, String key, String value) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        s.edit().putString(key, value).commit();
    }

    public static void savePreference(Context context, String key, int value) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        s.edit().putInt(key, value).commit();
    }

    public static void savePreference(Context context, String key, Boolean value) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        s.edit().putBoolean(key, value.booleanValue()).commit();
    }

    public static boolean getPreferenceBoolean(Context context, String key) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        return s.getBoolean(key, false);
    }

    public static String getPreference(Context context, String key) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        return s.getString(key, "");
    }

    public static int getPreferenceInt(Context context, String key) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        return s.getInt(key, 0);
    }

    public static boolean getPreferenceBoolean(Context context, String key, boolean def) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        return s.getBoolean(key, def);
    }

    public static String getPreference(Context context, String key, String def) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        return s.getString(key, def);
    }

    public static int getPreferenceInt(Context context, String key, int def) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        return s.getInt(key, def);
    }
}
