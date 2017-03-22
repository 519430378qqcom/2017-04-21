package com.umiwi.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.IVoiceService;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.AddFavBeans;
import com.umiwi.ui.beans.QRCodeBeans;
import com.umiwi.ui.beans.UmiwiResultBeans.ResultBeansRequestData;
import com.umiwi.ui.dao.CollectionDao;
import com.umiwi.ui.fragment.WebFragment;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.fragment.course.JPZTDetailFragment;
import com.umiwi.ui.fragment.setting.FeedbackFragment;
import com.umiwi.ui.http.parsers.ResponseParser;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.QRCodeManager;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.model.ADSplashModel;
import com.umiwi.ui.push.Utils;
import com.umiwi.ui.util.SDCardManager;
import com.umiwi.video.control.PlayerController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cn.sharesdk.framework.ShareSDK;
import cn.youmi.account.event.UserEvent;
import cn.youmi.framework.dialog.MsgDialog;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.PostRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.http.parsers.ModelParser;
import cn.youmi.framework.http.parsers.ResultParser;
import cn.youmi.framework.manager.ModelManager.ModelStatusListener;
import cn.youmi.framework.manager.ResultEvent;
import cn.youmi.framework.manager.VersionManager;
import cn.youmi.framework.model.ResultModel;
import cn.youmi.framework.model.VersionModel;
import cn.youmi.framework.util.AndroidSDK;
import cn.youmi.framework.util.NetworkManager;
import cn.youmi.framework.util.PreferenceUtils;
import cn.youmi.framework.util.SharePreferenceUtil;
import cn.youmi.framework.util.ToastU;
import cn.youmi.framework.util.UpdateUtils;

/**
 * 主activity添加注释
 */
public class HomeMainActivity extends AppCompatActivity {


    private ProgressBar mProgressBar;

    private CollectionDao collectDao;

    public static final String KEY_AD_SPLASH = "key.ad.splash";
    public static final String KEY_AD_DETAIL_URL = "key.ad.detail.url";
    public static final String KEY_AD_IMAGE_URL = "key.ad.image.url";
    public static final String KEY_AD_TIME = "key.ad.time";

    public static final String mPicturePath = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + UmiwiApplication.getContext().getPackageName() + "/cache/adpicture.png";
    public String musicUrl;
    public String herfUrl;
    public String url;
    public boolean isPause;
    /**
     * VoiceService的代理类
     */
    public IVoiceService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_home);

        mSpUtil = UmiwiApplication.getInstance().getSpUtil();
        UmiwiApplication.mainActivity = this;
        createProgressBar();

        ShareSDK.initSDK(getApplicationContext());

        if (YoumiRoomUserManager.getInstance().isLogin()) {
            PushString();
        }

        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setCatchUncaughtExceptions(false);//关掉友盟的错误统计

        if (NetworkManager.getInstance().isWifi()) {
            VersionManager.getInstance().registerListener(versionListener);
            String path = null;
            if (AndroidSDK.isICS()) {
                path = UmiwiApplication.getInstance().getResources().getString(R.string.serverurlics);
            } else {
                path = UmiwiApplication.getInstance().getResources().getString(R.string.serverurlgingerbread);
            }
            VersionManager.getInstance().checkNewVersion("home", path);
            updateManager = new UpdateUtils(HomeMainActivity.this, appUpdate);

            if (YoumiRoomUserManager.getInstance().isLogin()) {
                collectDao = new CollectionDao();
                if (collectDao.isCollectionNeed2Update()) {
                    uploadCollectionsData();
                }
            }
        }
        if (!mSpUtil.getDisturb()) {
            PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, Utils.getMetaValue(HomeMainActivity.this, "api_key"));
//			PushManager.disableLbs(this);// 关闭精准lbs
        }
        YoumiRoomUserManager.getInstance().getUserInfoSave(UserEvent.START_COUNT);//统计

        if (NetworkManager.getInstance().checkNet(this) && isMoreThanTwoDay(System.currentTimeMillis(), PreferenceUtils.getPrefLong(this, KEY_AD_TIME, 0l))) {
            saveAd();
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {//改成按两次退出
            long mNowTime = System.currentTimeMillis();
            if (mNowTime - mPressedTime > 2000) {
                ToastU.showShort(this, "再按一次退出程序");
                mPressedTime = mNowTime;
            } else {
                finish();
                UmiwiApplication.getInstance().exitApp();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private long mPressedTime = 0;

    @Override
    protected void onStart() {
        super.onStart();
//        if (!mSpUtil.getDisturb() && !PushManager.isPushEnabled(getApplicationContext())) {
//            PushManager.resumeWork(getApplicationContext());
//        }
        SDCardManager.createDownloadPath();
    }
    //TODO  测试
    public void feedback_clock(View view){
            Intent i = new Intent(HomeMainActivity.this,UmiwiContainerActivity.class);
            i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, FeedbackFragment.class);
            startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (!mSpUtil.getDisturb() && !PushManager.isPushEnabled(getApplicationContext())) {
//            PushManager.resumeWork(getApplicationContext());
//        }

        QRCodeManager.getInstance().registerListener(qrCodeManagerListener);
        MobclickAgent.onResume(this);
        PlayerController.getInstance().releaseAndStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /***
     * savedInstanceState 异常 建议在这处理（可能还有问题，测）
     */
    @Override
    protected void onResumeFragments() {
        // TODO Auto-generated method stub
        super.onResumeFragments();
        if (dialogToShow != null) {
            dialogToShow.show(getSupportFragmentManager(), "dialog");
            dialogToShow = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!mSpUtil.getDisturb()) {
            PushManager.stopWork(getApplicationContext());
        }
    }

    @Override
    protected void onDestroy() {
        ShareSDK.stopSDK();
        VersionManager.getInstance().unregisterListener(versionListener);
        QRCodeManager.getInstance().unregisterListener(qrCodeManagerListener);
        PreferenceUtils.setPrefBoolean(this, "isShowGiftOnceAndNoToShowAgain", false);
        UmiwiApplication.mainActivity = null;
        super.onDestroy();


    }

    private ModelStatusListener<ResultEvent, String> qrCodeManagerListener = new ModelStatusListener<ResultEvent, String>() {
        @Override
        public void onModelGet(ResultEvent key, String models) {

        }

        @Override
        public void onModelUpdate(ResultEvent key, String model) {
            switch (key) {
                case QR_CODE:
                    onCaptureStringClick(model);
                    break;
            }
        }

        @Override
        public void onModelsGet(ResultEvent key, List<String> models) {

        }
    };

    public void onCaptureStringClick(@NonNull final String scanResult) {
        if (!TextUtils.isEmpty(scanResult) && scanResult.contains("v.umiwi.com/") || scanResult.contains("v.youmi.cn/") || scanResult.contains("passport.umiwi.com/") || scanResult.contains("passport.youmi.cn/")) {// .umiwi.com/include/
            mProgressBar.setVisibility(View.VISIBLE);
            QRCodeString(scanResult + "&app=android");
        } else {
            final MsgDialog dialog = new MsgDialog();
            dialog.setTitle(R.string.net_address);
            dialog.setMessage(scanResult);
            dialog.setPositiveButtonText(R.string.visit);
            dialog.setPositiveButtonListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismissAllowingStateLoss();
                    if (!TextUtils.isEmpty(scanResult) && scanResult.contains("http://") || scanResult.contains("https://")) {
                        Uri uri = Uri.parse(scanResult);
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    } else {
                        showMsg("不是有效的链接,请重新扫描");
                    }
                }
            });
            dialog.setNegativeButtonText(R.string.cancel);
            dialog.setCancelable(false);
            // dialog.show(getSupportFragmentManager(), "dialog");
            dialogToShow = dialog;
        }
    }

    private MsgDialog dialogToShow;

    private void QRCodeString(String str) {
        GetRequest<QRCodeBeans> request = new GetRequest<QRCodeBeans>(str, GsonParser.class, QRCodeBeans.class, qrCodeListener);
        request.go();
    }

    private Listener<QRCodeBeans> qrCodeListener = new Listener<QRCodeBeans>() {

        @Override
        public void onResult(AbstractRequest<QRCodeBeans> request, QRCodeBeans requestData) {
            final String str = request.getURL();
            String type = requestData.getType();
            String url = requestData.getUrl();
            if ("login".equals(type)) {// 网页登录
                LoginQRCode(url);
            } else if ("album".equals(type)) {// 专辑
                Intent intent = new Intent(HomeMainActivity.this, UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
                intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, url);
                startActivity(intent);
                mProgressBar.setVisibility(View.GONE);
            } else if ("zhuanti".equals(type)) {// 专题
                Intent intent = new Intent(HomeMainActivity.this, UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, JPZTDetailFragment.class);
                intent.putExtra(JPZTDetailFragment.KEY_URL, url);
                startActivity(intent);
                mProgressBar.setVisibility(View.GONE);
            } else if ("article".equals(type)) {// 广告
                Intent i = new Intent(HomeMainActivity.this, UmiwiContainerActivity.class);
                i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, WebFragment.class);
                i.putExtra(WebFragment.WEB_URL, url);
                startActivity(i);
                mProgressBar.setVisibility(View.GONE);
            } else {
                mProgressBar.setVisibility(View.GONE);
                final MsgDialog dialog = new MsgDialog();
                dialog.setTitle(R.string.net_address);
                dialog.setMessage(str);
                dialog.setPositiveButtonText(R.string.visit);
                dialog.setPositiveButtonListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismissAllowingStateLoss();
                        Uri uri = Uri.parse(str);
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    }
                });
                dialog.setNegativeButtonText(R.string.cancel);
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        }

        @Override
        public void onError(AbstractRequest<QRCodeBeans> requet, int statusCode, String body) {
            mProgressBar.setVisibility(View.GONE);
        }
    };

    private void LoginQRCode(String url) {
        GetRequest<ResultBeansRequestData> request = new GetRequest<ResultBeansRequestData>(url, ResponseParser.class, loginListener);
        HttpDispatcher.getInstance().go(request);
    }

    private Listener<ResultBeansRequestData> loginListener = new Listener<ResultBeansRequestData>() {
        @Override
        public void onResult(AbstractRequest<ResultBeansRequestData> request, ResultBeansRequestData t) {
            if ("9999".equals(t.getE())) {
                showMsg("网页登录成功");
            } else {
                showMsg(t.getM());
            }
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onError(AbstractRequest<ResultBeansRequestData> requet, int statusCode, String body) {
            // TODO
            mProgressBar.setVisibility(View.GONE);
            showMsg("未知错误,请重试");
        }
    };

    private void createProgressBar() {
        FrameLayout rootFrameLayout = (FrameLayout) findViewById(android.R.id.content);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        mProgressBar = new ProgressBar(HomeMainActivity.this);
        mProgressBar.setLayoutParams(layoutParams);
        mProgressBar.setVisibility(View.GONE);
        rootFrameLayout.addView(mProgressBar);
    }

    private UpdateUtils updateManager;
    private String updateMessage;
    private String uadateUrl;
    private ModelStatusListener<String, VersionModel> versionListener = new ModelStatusListener<String, VersionModel>() {

        @Override
        public void onModelGet(String key, final VersionModel models) {
            if (key.contains("homeupdate")) {
                String versionStr = PreferenceUtils.getPrefString(HomeMainActivity.this, "version", "1");
                if (!versionStr.equals(models.getVersion())) {
                    if (NetworkManager.getInstance().isWifi()) {
                        updateManager.isUpdate(models.getUrl());
                        updateMessage = models.getDescription();
                        uadateUrl = models.getUrl();
                        PreferenceUtils.setPrefString(HomeMainActivity.this, "version", models.getVersion());
                    }
                } else {// 更新过了，不提示

                }

            }
        }

        @Override
        public void onModelUpdate(String key, VersionModel model) {

        }

        @Override
        public void onModelsGet(String key, List<VersionModel> models) {

        }
    };
    UpdateUtils.UpdateCallback appUpdate = new UpdateUtils.UpdateCallback() {

        public void downloadProgressChanged(int progress) {
        }

        public void downloadCompleted(Boolean sucess, CharSequence errorMsg) {
            if (sucess) {
                final MsgDialog dialog = new MsgDialog();
                dialog.setOnTouchCancelable(false);
                dialog.setCancelable(false);
                dialog.setOnTouchCancelable(false);
                dialog.setTitle(R.string.new_version_detected);
                dialog.setMessage(updateMessage, true);
                dialog.setPositiveButtonText(R.string.update);
                dialog.setPositiveButtonListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismissAllowingStateLoss();
                        UpdateUtils.installApk(UpdateUtils.updateFile);
                    }
                });
                // dialog.show(getSupportFragmentManager(), "dialog");
                dialogToShow = dialog;
            } else {
                if (NetworkManager.getInstance().isWifi()) {
                    updateManager.isUpdate(uadateUrl);
                } else {
                    updateManager.cancelDownload();
                }
            }
        }

        public void downloadCanceled() {
        }

        public void downloading() {
            updateManager.downloadPackage();

        }
    };

    public void showMsg(String str) {
        if (!TextUtils.isEmpty(str)) {
            ToastU.showShort(HomeMainActivity.this, str);
        }
    }

    public void PushString() {
        PostRequest<ResultModel> prequest = new PostRequest<ResultModel>(UmiwiAPI.PUSH_BIND, GsonParser.class, ResultModel.class, pushListener);
        prequest.addParam("app_id", mSpUtil.getAppId());
        prequest.addParam("user_id", mSpUtil.getUserId());
        prequest.addParam("channel_id", mSpUtil.getChannelId());
        prequest.go();

    }

    private Listener<ResultModel> pushListener = new Listener<ResultModel>() {

        @Override
        public void onResult(AbstractRequest<ResultModel> request, ResultModel t) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onError(AbstractRequest<ResultModel> requet, int statusCode, String body) {
            // TODO Auto-generated method stub

        }
    };
    private SharePreferenceUtil mSpUtil;

    private Listener<ResultModel> deleteListener = new Listener<ResultModel>() {
        @Override
        public void onResult(AbstractRequest<ResultModel> request, ResultModel t) {
            if (t.isSucc()) {
                String delFavidString = (String) request.getTag();
                String[] delFavids = delFavidString.split(",");

                if (delFavids != null) {
                    for (String delFavid : delFavids) {
                        collectDao.deleteCollectionCompelete(delFavid);
                    }
                }
            }
        }

        @Override
        public void onError(AbstractRequest<ResultModel> requet,
                            int statusCode, String body) {

        }
    };

    private Listener<AddFavBeans.AddFavBeansRequestData> saveListener = new Listener<AddFavBeans.AddFavBeansRequestData>() {
        @Override
        public void onResult(AbstractRequest<AddFavBeans.AddFavBeansRequestData> request,
                             AddFavBeans.AddFavBeansRequestData t) {
            String albumIDStrings = (String) request.getTag();
            String[] albumIDs = albumIDStrings.split(",");
            if (albumIDs != null) {
                for (String albumID : albumIDs) {
                    collectDao.updateCollection(albumID);
                }
            }
        }

        @Override
        public void onError(AbstractRequest<AddFavBeans.AddFavBeansRequestData> requet,
                            int statusCode, String body) {
        }

    };

    // 与服务器同步数据
    private void uploadCollectionsData() {

        List<String> saveCollections = collectDao.getUnuploadSaveCollections();

        StringBuilder sb = new StringBuilder();

        for (String saveCol : saveCollections) {
            sb.append(saveCol).append(",");
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());

            String saveStrings = sb.toString();
            String favStr = String.format(UmiwiAPI.UMIWI_FAV_ADD_VIDEO_ALBUMID,
                    saveStrings);

            GetRequest<AddFavBeans.AddFavBeansRequestData> req = new GetRequest<AddFavBeans.AddFavBeansRequestData>(
                    favStr, GsonParser.class,
                    AddFavBeans.AddFavBeansRequestData.class, saveListener);
            req.setTag(saveStrings);
            HttpDispatcher.getInstance().go(req);
        }

        List<String> deleteCollections = collectDao
                .getUnuploadDeleteCollections();
        sb.delete(0, sb.length());
        for (String delCol : deleteCollections) {
            sb.append(delCol).append(",");
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());

            String deleteStrings = sb.toString();

            String delStr = String.format(
                    UmiwiAPI.UMIWI_FAV_DELETE_VIDEO_FAVID, deleteStrings);

            GetRequest<ResultModel> request = new GetRequest<ResultModel>(
                    delStr, ResultParser.class, deleteListener);
            request.setTag(deleteStrings);
            HttpDispatcher.getInstance().go(request);
        }

    }

    private void saveAd() {
        GetRequest<ADSplashModel> request = new GetRequest<ADSplashModel>("http://i.v.youmi.cn/api7/sectionad", ModelParser.class, ADSplashModel.class, adListener);
        request.go();
    }

    private Listener<ADSplashModel> adListener = new Listener<ADSplashModel>() {

        @Override
        public void onResult(AbstractRequest<ADSplashModel> request, final ADSplashModel adSplashModel) {

            if (!TextUtils.isEmpty(adSplashModel.getImageUrl())) {

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        try {

                            Bitmap bitmap = Glide.with(HomeMainActivity.this).load(adSplashModel.getImageUrl()).asBitmap().into(1280, 800).get();//java.util.concurrent.TimeoutException
                            try {
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);

                                File file = new File(mPicturePath);
                                FileOutputStream fos = new FileOutputStream(file);
                                fos.write(bytes.toByteArray());
                                fos.close();
                                PreferenceUtils.setPrefString(HomeMainActivity.this, KEY_AD_IMAGE_URL, adSplashModel.getImageUrl());
                                PreferenceUtils.setPrefString(HomeMainActivity.this, KEY_AD_DETAIL_URL, adSplashModel.getDetailUrl());
                                PreferenceUtils.setPrefInt(HomeMainActivity.this, KEY_AD_SPLASH, 0);
                                PreferenceUtils.setPrefLong(HomeMainActivity.this, KEY_AD_TIME, System.currentTimeMillis());
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                };
                new Thread(runnable).start();
            }
        }

        @Override
        public void onError(AbstractRequest<ADSplashModel> requet, int statusCode, String body) {
        }
    };

    public static boolean isMoreThanTwoDay(long afterTime, long beforeTime) {
        long day = (afterTime - beforeTime) / (24 * 60 * 60 * 1000);
        return day >= 1;
    }

}
