package com.umiwi.ui.main;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.ManifestUtils;
import com.umiwi.video.application.Settings;
import com.umiwi.video.services.VoiceService;

import cn.youmi.account.manager.UserManager;
import cn.youmi.framework.main.BaseApplication;
import cn.youmi.framework.main.ConstantProvider;
import cn.youmi.framework.util.AndroidSDK;
import cn.youmi.framework.util.SharePreferenceUtil;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

//import com.squareup.leakcanary.LeakCanary;
//import com.squareup.leakcanary.RefWatcher;

/**
 * @author tangxiyong 2013-11-21下午6:22:32
 */
public class UmiwiApplication extends BaseApplication implements ServiceConnection {

    private static Context sContext;
//    private static VoiceService.VoiceBinder mBinder;

    private Activity activity = null;

    private static UmiwiApplication umiwiApplication = null;

    public static Context getContext() {
        return sContext;
    }

    public static UmiwiApplication getInstance() {
        return umiwiApplication;
    }

    private Handler handler = new Handler();
    public static final String SP_PUSH_NAME = "com.umiwi.ui.push_msg";
    private SharePreferenceUtil mSpUtil;

    public static YoumiConfiguration configuration;

//    public static RefWatcher getRefWatcher(Context context) {
//        UmiwiApplication application = (UmiwiApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }
//
//	private RefWatcher refWatcher;

    //	private String userAgentString = null;
//	private DispatcherObserver observer = new DispatcherObserver() {
//		@Override
//		public void willDispatch(AbstractRequest<?> req) {
//			req.setHeader("User-Agent", userAgentString);
////			try {
////				LogUtils.e("xx","userAgentString:" + URLDecoder.decode(userAgentString,"UTF-8"));
////			} catch (UnsupportedEncodingException e) {
////				e.printStackTrace();
////			}
//		}
//	};
    @Override
    public void onCreate() {
        super.onCreate();
        umiwiApplication = this;
        ConstantProvider.setInstance(YoumiConstantProvider.getInstance());

        sContext = getApplicationContext();
//        bindservice();

//		HttpDispatcher.getInstance().registerDispatcherObserver(observer);
//		String version = Build.VERSION.RELEASE;
//
//		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//		Display display = wm.getDefaultDisplay();
//		float width = display.getWidth();
//		float height = display.getHeight();
//		try {//注意格式， 有空格
//			userAgentString = URLEncoder.encode(
//					"优米教育/" + SystemUtils.getVersionName() + " ("
//							+ SystemUtils.getModel() + "; Android " + version
//							+ "; Scale/" + width + ":" + height + "/"
//							+ SystemUtils.getMacMD5() + "/zhongchuang)", "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}

        UserManager.setInstance(YoumiRoomUserManager.getInstance());

        mSpUtil = new SharePreferenceUtil(this, SP_PUSH_NAME);

        configuration = new YoumiConfiguration(this) {
            @Override
            public String configDownloadPath() {
                return "/youmi/videocache";
            }
        };

//        refWatcher = LeakCanary.install(this);

        if (AndroidSDK.isICS()) {
            SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String key = this.getString(R.string.pref_key_enable_texture_view);//android.content.res.Resources$NotFoundException
            mSharedPreferences.edit().putBoolean(key, true).commit();
        }
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String key = this.getString(R.string.pref_key_player);
            mSharedPreferences.edit().putString(key, Settings.PV_PLAYER__IjkMediaPlayer + "").commit();
        } catch (UnsatisfiedLinkError e) {
            //用系统原生
            SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String key = this.getString(R.string.pref_key_player);
            mSharedPreferences.edit().putString(key, Settings.PV_PLAYER__AndroidMediaPlayer + "").commit();
        } finally {
            Settings settings = new Settings(this);
            if (settings.getPlayer() == Settings.PV_PLAYER__IjkMediaPlayer) {
                IjkMediaPlayer.native_profileEnd();
            }
        }

        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        strategy.setAppChannel(ManifestUtils.getChannelString(this));
        strategy.setAppReportDelay(20000);
        CrashReport.initCrashReport(this, "900012791", false, strategy);
        if (YoumiRoomUserManager.getInstance().isLogin()) {
            CrashReport.setUserSceneTag(this, Integer.valueOf(YoumiRoomUserManager.getInstance().getUid()));
        }
    }

    private void bindservice() {
        Intent intent = new Intent(this, VoiceService.class);
        this.bindService(intent, this, BIND_AUTO_CREATE);
    }

    public synchronized SharePreferenceUtil getSpUtil() {
        if (mSpUtil == null)
            mSpUtil = new SharePreferenceUtil(this, SP_PUSH_NAME);
        return mSpUtil;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Handler getUIHandler() {
        return handler;
    }
//    public VoiceService.VoiceBinder  getBinder(){
//         return mBinder;
//    }
    public void exitApp() {
        System.gc();
        MobclickAgent.onKillProcess(this);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//        mBinder = (VoiceService.VoiceBinder) iBinder;

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
}
