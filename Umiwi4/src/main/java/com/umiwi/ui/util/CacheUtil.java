package com.umiwi.ui.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gyc on 2016/3/23.
 * 数据缓存工具类
 */
public class CacheUtil {


    /**
     * 保存数据
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("umiwi", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 得到缓存数据
     * @param context
     * @param key
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("umiwi", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    /**
     * 缓存文本数据
     * @param context
     * @param key
     * @param values
     */
    public static void putString(Activity context, String key, String values) {
        SharedPreferences sp = context.getSharedPreferences("umiwi", Context.MODE_PRIVATE);
        sp.edit().putString(key, values).commit();
    }

    /**
     * 得到缓存文本的数据
     * @param context
     * @param key
     * @return
     */
    public static String getString(Activity context, String key) {
        SharedPreferences sp = context.getSharedPreferences("umiwi", Context.MODE_PRIVATE);
        return sp.getString(key,"");//没有得到数据的情况下返回空字符串
    }
}
