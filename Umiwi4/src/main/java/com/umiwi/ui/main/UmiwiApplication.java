package com.umiwi.ui.main;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.HomeMainActivity;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.push.Utils;
import com.umiwi.ui.util.ManifestUtils;
import com.umiwi.video.application.Settings;
import com.umiwi.video.services.VoiceService;

import org.xutils.x;

import cn.youmi.account.manager.UserManager;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.PostRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.main.BaseApplication;
import cn.youmi.framework.main.ConstantProvider;
import cn.youmi.framework.model.ResultModel;
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
    public static HomeMainActivity mainActivity;
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
        x.Ext.init(this);
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

        new Thread() {
            public void run() {
                if (YoumiRoomUserManager.getInstance().isLogin()) {
                    PushString();
//            String cookie1 = getCookie();
//            Log.e("TAG", "cookie=" + cookie1 );
                }
                if (!mSpUtil.getDisturb()) {
                    PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, Utils.getMetaValue(getApplicationContext(), "api_key"));
//			PushManager.disableLbs(this);// 关闭精准lbs
//            Log.e("TAG", "百度云推送的api_key=" + Utils.getMetaValue(HomeMainActivity.this, "api_key"));
//            PushManager.resumeWork(this);
                    Log.e("TAG", "resumeWork()");
                }
            }
        }.start();


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
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        NIMClient.init(this, loginInfo(), options());

    }

    // 如果返回值为 null，则全部使用默认参数。
    private SDKOptions options() {
        SDKOptions options = new SDKOptions();

//        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
//        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
//        config.notificationEntrance = WelcomeActivity.class; // 点击通知栏跳转到该Activity
//        config.notificationSmallIconId = R.drawable.ic_stat_notify_msg;
        // 呼吸灯配置
//        config.ledARGB = Color.GREEN;
//        config.ledOnMs = 1000;
//        config.ledOffMs = 1500;
//        // 通知铃声的uri字符串
//        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
//        options.statusBarNotificationConfig = config;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";
        options.sdkStorageRootPath = sdkPath;
// 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        options.thumbnailSize = 1080/2;

        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        options.userInfoProvider = new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return null;
            }

            @Override
            public int getDefaultIconResId() {
                return R.drawable.default_voice;
            }

            @Override
            public Bitmap getTeamIcon(String tid) {
                return null;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(String account) {
                return null;
            }

            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId,
                                                           SessionTypeEnum sessionType) {
                return null;
            }
        };
        return options;
    }

    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
    private LoginInfo loginInfo() {
        return null;
    }


    public void PushString() {
        PostRequest<ResultModel> prequest = new PostRequest<ResultModel>(UmiwiAPI.PUSH_BIND, GsonParser.class, ResultModel.class, pushListener);
        prequest.addParam("app_id", mSpUtil.getAppId());
        prequest.addParam("user_id", mSpUtil.getUserId());
        prequest.addParam("channel_id", mSpUtil.getChannelId());
        prequest.go();
//        Log.e("TAG", "mSpUtil.getUserId()=" + mSpUtil.getAppId() + "," + mSpUtil.getUserId() + "," + mSpUtil.getChannelId() + "," + mSpUtil.getDeviceId());
    }

    private AbstractRequest.Listener<ResultModel> pushListener = new AbstractRequest.Listener<ResultModel>() {

        @Override
        public void onResult(AbstractRequest<ResultModel> request, ResultModel t) {
            // TODO Auto-generated method stub
            Log.e("TAG", "ResultModel1234=" + t.toString());
        }

        @Override
        public void onError(AbstractRequest<ResultModel> requet, int statusCode, String body) {
            // TODO Auto-generated method stub

        }
    };

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
