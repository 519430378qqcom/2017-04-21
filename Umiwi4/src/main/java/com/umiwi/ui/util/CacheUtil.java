package com.umiwi.ui.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

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

    public static void putStringFile(Context context, String key, String values) {

        //判断是否有sdcard，如果存在，就保存到sdcard
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                String fileName = MD5Encoder.encode(key);
                File file = new File(Environment.getExternalStorageDirectory() + "/umiwifile" + fileName);

                File parentFile = file.getParentFile();
                if(!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                //创建文本文件
                if(!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(values.getBytes());
                fos.flush();
                fos.close();
                //保存文件

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            SharedPreferences sp = context.getSharedPreferences("umiwi", Context.MODE_PRIVATE);
            sp.edit().putString(key, values).commit();
        }
    }

    public static String getStringFile(Context context, String key) {
        String result = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                String fileName = MD5Encoder.encode(key);
                File file = new File(Environment.getExternalStorageDirectory() + "/umiwifile" + fileName);
                if(file.exists()) {
                    FileInputStream fis = new FileInputStream(file);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    int len = -1;
                    byte[] buffer = new byte[1024];
                    while ((len = fis.read(buffer)) != -1) {
                        stream.write(buffer,0,len);
                    }
                    fis.close();
                    stream.close();
                    result = stream.toString();
                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            SharedPreferences sp = context.getSharedPreferences("umiwi", Context.MODE_PRIVATE);
            return sp.getString(key, "");
        }
        return result;
    }
}
