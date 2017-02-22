package com.umiwi.ui.activity;

import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.util.ManifestUtils;

import cn.youmi.account.activity.AbstractLoginActivity;
import cn.youmi.account.event.ClassesEvent;
import cn.youmi.framework.util.PreferenceUtils;

/**
 * 登录界面
 *
 * @author tangxiyong 2013-11-15上午10:15:37
 */
public class YMLoginActivity extends AbstractLoginActivity {

    @Override
    protected String weixinAppId() {
        return UmiwiAPI.WEIXIN_APP_ID;
    }

    @Override
    protected String weixinAppKey() {
        return UmiwiAPI.WEIXIN_APP_KEY;
    }

    @Override
    protected String qqAppId() {
        return UmiwiAPI.QZone_APP_ID;
    }

    @Override
    protected String sinaWeiboAppKey() {
        return UmiwiAPI.SINA_APP_KEY;
    }

    @Override
    protected String sinaWeiboRediteUrl() {
        return UmiwiAPI.SINA_REDITE_URL;
    }

    @Override
    protected String sinaWeiboScope() {
        return UmiwiAPI.SINA_SCOPE;
    }

    @Override
    protected ClassesEvent getClassesFrom() {
        return ClassesEvent.LOGIN;
    }

    @Override
    protected String getChannel() {
        return ManifestUtils.getChannelString(YMLoginActivity.this);
//		return getResources().getString(R.string.channel);
    }

    @Override
    protected String getOAuthSinaWeiboLoginUrl() {
        return UmiwiAPI.UMIWI_THIRD_LOGIN;
    }

    @Override
    protected String postOAuthSinaWeiboLoginUrl() {
        return UmiwiAPI.UMIWI_THIRD_LOGIN;
    }

    @Override
    protected String getOAuthQQLoginUrl() {
        return UmiwiAPI.UMIWI_THIRD_LOGIN;
    }

    @Override
    protected String postOAuthQQLoginUrl() {
        return UmiwiAPI.UMIWI_THIRD_LOGIN;
    }

    @Override
    protected String getOAuthWeiXinLoginUrl() {
        return UmiwiAPI.UMIWI_THIRD_LOGIN;
    }

    @Override
    protected String postOAuthWeiXinLoginUrl() {
        return UmiwiAPI.UMIWI_THIRD_LOGIN;
    }

    @Override
    protected String getYoumiLoginUrl() {
//		return UmiwiAPI.UMIWI_USER_TOKEN;
        return umiwiLoginUrl;
    }

    private String umiwiLoginUrl;

    @Override
    protected String getWeiXinTokenUrl() {
        return UmiwiAPI.WEIXIN_ACCESS_TOKEN_URL;
    }

    @Override
    protected String getOAuthType() {
        // TODO Auto-generated method stub
        return oAuthType;
    }

    protected String oAuthType;

    private int classestest;

//    mode 1->umiwi, 2->weixin, 3->qq, 4->sinaweibo

    public static final int LOGIN_YOUMI = 1;
    public static final int LOGIN_WEIXIN = 2;
    public static final int LOGIN_QQ = 3;
    public static final int LOGIN_SINAWEIBO = 4;

    public static final String LOGIN_TYPE = "login.type";
    public static final String YOUMI_LOGIN_URL = "youmi.loginurl";

    public static final String MEMORY_LOGIN = "memory.login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTheme(R.style.Theme_Umiwi_Translucent);
        super.onCreate(savedInstanceState);

        umiwiLoginUrl = getIntent().getStringExtra(YOUMI_LOGIN_URL);

        switch (getIntent().getIntExtra(LOGIN_TYPE, 0)) {
            case LOGIN_YOUMI:
                getYoumiLogin();
                oAuthType = "1";
                break;
            case LOGIN_WEIXIN:
                oAuthType = "5";
                WeiXinLogin();
                break;
            case LOGIN_QQ:
                oAuthType = "4";
                QQLogin();
                break;
            case LOGIN_SINAWEIBO:
                oAuthType = "2";
                SinaWeiboLogin();
                break;
        }

        PreferenceUtils.setPrefInt(this, MEMORY_LOGIN, Integer.valueOf(oAuthType));

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}