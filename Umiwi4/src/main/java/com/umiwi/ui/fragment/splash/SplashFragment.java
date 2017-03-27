package com.umiwi.ui.fragment.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.HomeMainActivity;
import com.umiwi.ui.activity.SplashActivity;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.fragment.WebFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.CommonHelper;

import java.io.File;

import cn.youmi.framework.dialog.MsgDialog;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.util.PreferenceUtils;

/**
 * 第二次启动后，使用的
 *
 * @author tangxiyong
 * @version 2014年6月25日 上午10:11:33
 */
public class SplashFragment extends BaseConstantFragment {

    private SharedPreferences mSharedPreferences;

    Handler mHandler = new Handler();

    public static SplashFragment newInstance() {
        SplashFragment f = new SplashFragment();
        return f;
    }

    ImageView splash_image;
    ImageLoader mImageLoader;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(fragmentName);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(fragmentName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mImageLoader = null;
        PreferenceUtils.setPrefInt(getActivity(), HomeMainActivity.KEY_AD_SPLASH, PreferenceUtils.getPrefInt(getActivity(), HomeMainActivity.KEY_AD_SPLASH, 0) + 1);
        if (isMoreThanTwoDay(System.currentTimeMillis(), PreferenceUtils.getPrefLong(getActivity(), HomeMainActivity.KEY_AD_TIME, 0l))) {
            PreferenceUtils.setPrefInt(getActivity(), HomeMainActivity.KEY_AD_SPLASH, 0);
            PreferenceUtils.setPrefLong(getActivity(), HomeMainActivity.KEY_AD_TIME, System.currentTimeMillis());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash_layout, null);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        splash_image = (ImageView) view.findViewById(R.id.splash_image);


        if (!isMoreThanTwoDay(System.currentTimeMillis(), PreferenceUtils.getPrefLong(getActivity(), HomeMainActivity.KEY_AD_TIME, System.currentTimeMillis()))
                && !TextUtils.isEmpty(PreferenceUtils.getPrefString(getActivity(), HomeMainActivity.KEY_AD_DETAIL_URL, ""))
                && PreferenceUtils.getPrefInt(getActivity(), HomeMainActivity.KEY_AD_SPLASH, 0) < 3) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getActivity()).load(new File(HomeMainActivity.mPicturePath)).asBitmap().into(splash_image).getRequest();
                }
            });
            splash_image.setOnClickListener(adClickListener);
        } else {
            if (!isRemoving()) {//http://bugly.qq.com/detail?app=900012791&pid=1&ii=90#stack
                mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
                mImageLoader.loadImage(R.drawable.guide_splash, splash_image, R.drawable.guide_splash);
            }
        }

        if (!CommonHelper.checkNetWifi(getActivity())) {
            // 弹出对话框 提示用户设置网络
            final MsgDialog dialog = new MsgDialog();
            dialog.setTitle(R.string.set_network);
            dialog.setMsg(R.string.network_error);
            dialog.setPositiveButtonText(R.string.set_network);
            dialog.setPositiveButtonListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismissAllowingStateLoss();
                    Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
                    startActivity(wifiSettingsIntent);
                }
            });
            dialog.setNegativeButtonListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismissAllowingStateLoss();
                    mSharedPreferences.edit().putBoolean("isCanShowGift", true).commit();
                    startActivity(new Intent(getActivity(), HomeMainActivity.class));
                    getActivity().finish();
                }
            });
            dialog.show(getChildFragmentManager(), "dialog");
        }

        if (YoumiRoomUserManager.getInstance().isLogin()) {
            mHandler.postDelayed(loginRun, 3000);
        } else {
            mHandler.postDelayed(noLoginRun, 3000);
        }


        return view;
    }

    Runnable loginRun = new Runnable() {
        @Override
        public void run() {
            if(SplashActivity.isKeyBack) {
                return;
            }
            Intent i = null;
            if (getActivity() != null) {// 当用户按返回键时
//                i = new Intent(getActivity(), HomeMainActivity.class);
                i = new Intent(getActivity(), HomeMainActivity.class);
                mSharedPreferences.edit().putBoolean("isCanShowGift", true).commit();
            }
            if (i != null) {
                mSharedPreferences.edit().putBoolean("isCanShowGift", true).commit();
                startActivity(i);

            }
//            Intent i2 = null;
//            if (getActivity() != null) {
//                mSharedPreferences.edit().putBoolean("isCanShowGift", true).commit();
//                i2 = new Intent(getActivity(), ColumnDetailsSplashActivity.class);
//
//            }
//            if (i2 != null) {
//                mSharedPreferences.edit().putBoolean("isCanShowGift", true).commit();
//                startActivity(i2);
//            }
            getActivity().finish();
        }
    };

    Runnable noLoginRun = new Runnable() {
        @Override
        public void run() {
            if(SplashActivity.isKeyBack) {
                return;
            }
            Intent i = null;
            if (getActivity() != null) {// 当用户按返回键时
//                i = new Intent(getActivity(), HomeMainActivity.class);
                i = new Intent(getActivity(), HomeMainActivity.class);
            }
            if (i != null) {
                startActivity(i);

            }
//            Intent i2 = null;
//            if (getActivity() != null) {
//                i2 = new Intent(getActivity(), ColumnDetailsSplashActivity.class);
//            }
//            if (i2 != null) {
//                startActivity(i2);
//            }
            getActivity().finish();
        }
    };

    private View.OnClickListener adClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mHandler.removeCallbacks(loginRun);
            mHandler.removeCallbacks(noLoginRun);

            Intent intent = new Intent(getActivity(), HomeMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

            Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
            i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, WebFragment.class);
            i.putExtra(WebFragment.WEB_URL, PreferenceUtils.getPrefString(getActivity(), HomeMainActivity.KEY_AD_DETAIL_URL, "http://m.youmi.cn/m.shtml"));
            startActivity(i);

            getActivity().finish();
        }
    };


    public static boolean isMoreThanTwoDay(long afterTime, long beforeTime) {
        long day = (afterTime - beforeTime) / (24 * 60 * 60 * 1000);
        return day >= 1;
    }
}
