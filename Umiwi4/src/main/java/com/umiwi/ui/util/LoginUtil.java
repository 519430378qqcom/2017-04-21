package com.umiwi.ui.util;

import android.content.Context;
import android.content.Intent;

import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.activity.YMLoginActivity;
import com.umiwi.ui.fragment.login.LoginMemoryFragment;
import com.umiwi.ui.fragment.login.LoginPureFragment;
import com.umiwi.ui.fragment.user.ForgetPasswordFragment;
import com.umiwi.ui.fragment.user.RegisterShowFragment;

import cn.youmi.framework.util.PreferenceUtils;
import cn.youmi.framework.util.SingletonFactory;

/**
 * Created by umiwi on 15/7/15.
 */
public class LoginUtil {

    public static LoginUtil getInstance() {
        return SingletonFactory.getInstance(LoginUtil.class);
    }

    public void showLoginView(Context context) {

        Intent intent = new Intent(context, UmiwiContainerActivity.class);
        if (0 == PreferenceUtils.getPrefInt(context, YMLoginActivity.MEMORY_LOGIN, 0)) {
            intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LoginPureFragment.class);
        } else {
            intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LoginMemoryFragment.class);
        }
        context.startActivity(intent);
    }

    public void goLogin(Context context, int loginType) {
        goLogin(context, loginType, "");
    }

    public void goLogin(Context context, int loginType, String youmiUrl) {
        Intent intent = new Intent(context, YMLoginActivity.class);
        intent.putExtra(YMLoginActivity.LOGIN_TYPE, loginType);
        intent.putExtra(YMLoginActivity.YOUMI_LOGIN_URL, youmiUrl);
        context.startActivity(intent);
    }

    public void goRegister(Context context) {
        Intent i = new Intent(context, UmiwiContainerActivity.class);
        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, RegisterShowFragment.class);
        context.startActivity(i);
    }

    public void geResetPassword(Context context) {
        Intent i = new Intent(context, UmiwiContainerActivity.class);
        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ForgetPasswordFragment.class);
        context.startActivity(i);
    }
}
