package com.umiwi.video.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by txy on 15/9/8.
 */
public final class PlayUtils {

    public static Boolean isLandscape(Context c) {
        return c.getResources().getConfiguration().orientation == 2 ? Boolean.valueOf(true) : Boolean.valueOf(false);
    }

    /**
     * 监听当前的电池容量
     * http://developer.android.com/training/monitoring-device-state/battery-monitoring.html#MonitorChargeState
     *
     * @param context
     * @return num%
     */
    public static String getBatteryLevel(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(mBatteryLevelReceiver, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int batteryPct = level * 100 / scale;
        return batteryPct + "%";
    }

    /**
     * 获取系统当前时间
     *
     * @return timeNow
     */
    public static String getSystemTime() {
        Date mDate = new Date();
        SimpleDateFormat mTimeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        mDate.setTime(System.currentTimeMillis());
        String timeNow = mTimeFormat.format(mDate);
        return timeNow;
    }

    static BroadcastReceiver mBatteryLevelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    public static void unregisterBatteryLevelReceiver(Context context) {
        try {
            context.unregisterReceiver(mBatteryLevelReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
