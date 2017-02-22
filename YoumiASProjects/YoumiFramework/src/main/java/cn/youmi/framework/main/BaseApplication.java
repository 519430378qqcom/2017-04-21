package cn.youmi.framework.main;

import android.app.Application;
import android.content.Context;

/**
 * Created by tangixyong on 16/1/21.
 */
public class BaseApplication extends Application {
    private static Context sContext;

    private static BaseApplication baseApplication = null;

    public static Context getContext() {
        return sContext;
    }

    public static BaseApplication getApplication() {
        return baseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        sContext = getApplicationContext();
    }
}
