package cn.youmi.framework.util;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

/**
 * @author tangxiyong
 * @version 2014年10月30日 下午6:00:14
 */
public class ActivityUtils {
	
	public static boolean isIntentSafe(Activity activity, Uri uri) {
        Intent mapCall = new Intent(Intent.ACTION_VIEW, uri);
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapCall, 0);
        return activities.size() > 0;
    }

    public static boolean isIntentSafe(Activity activity, Intent intent) {
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        return activities.size() > 0;
    }
	
	public static boolean isActivityExist(Context packageContext,
			Class<?> activityName) {
		Intent intent = new Intent(packageContext, activityName);
		ComponentName cmpName = intent.resolveActivity(packageContext
				.getPackageManager());
		boolean bIsExist = false;
		if (cmpName != null) { // 说明系统中存在这个activity
			ActivityManager am = (ActivityManager) packageContext.getSystemService(Activity.ACTIVITY_SERVICE);
			List<RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
			for (RunningTaskInfo taskInfo : taskInfoList) {
				if (taskInfo.baseActivity.equals(cmpName)) {
					bIsExist = true;
					break;
				}
			}
		}
		if (!bIsExist) {
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			packageContext.startActivity(intent);
			bIsExist = true;
		}
		return bIsExist;
	}
	
	/** Activity是否在栈顶*/
	public static boolean isActivityReception(Context context, String packageName) {
		List<RunningTaskInfo> tasksInfo = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			// 应用程序位于堆栈的顶层
			if (packageName.equals(tasksInfo.get(0).topActivity.getClassName())) {
				return true;
			}
		}
		return false;
	}
	
	/** 应用是否在后台*/
	public static boolean isAppBackstage(Context context) {
	    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
	    for (RunningAppProcessInfo appProcess : appProcesses) {
	         if (appProcess.processName.equals(context.getPackageName())) {
				 return appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND;
	           }
	    }
	    return false;
	}
	
	public static boolean isApplicationSentToBackground(final Context context) {
	    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningTaskInfo> tasks = am.getRunningTasks(1);
	    if (!tasks.isEmpty()) {
	        ComponentName topActivity = tasks.get(0).topActivity;
	        if (!topActivity.getPackageName().equals(context.getPackageName())) {
	            return true;
	        }
	    }
	    return false;
	}
}
