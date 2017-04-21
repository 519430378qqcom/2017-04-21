package com.umiwi.ui.fragment.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.activity.YMLoginActivity;
import com.umiwi.ui.fragment.UserTestInfoFragment;
import com.umiwi.ui.fragment.user.BindingPhoneFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.CommonHelper;
import com.umiwi.ui.util.LoginUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import cn.youmi.account.activity.AbstractLoginActivity;
import cn.youmi.account.event.UserEvent;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.manager.ModelManager;
import cn.youmi.framework.util.KeyboardUtils;
import cn.youmi.framework.util.PreferenceUtils;
import cn.youmi.framework.util.ToastU;

/**
 */
public class LoginMemoryFragment extends BaseFragment {

    private int classestest;
    private EditText et_username;
    private EditText et_password;
    private TextView login_umiwi;
    private TextView register;
    private TextView replace_password;
    private TextView login_qq;
    private TextView login_weixin;
    private TextView login_weibo;
    private ImageView memory_login;
    private TextView memory_lgoin_text;
    private TextView login_umiwi_view;
    private ViewStub login_youmi_layout;
    private ViewStub login_memory_button;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("TAG", "登陆onCreateView()");
        View view = inflater.inflate(R.layout.fragment_login_memory, container, false);
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        setSupportActionBarAndToolbarTitle(mActionBarToolbar, "登录");
        YoumiRoomUserManager.getInstance().registerListener(mUserListener);
        setViewVisibility(view, PreferenceUtils.getPrefInt(getActivity(), YMLoginActivity.MEMORY_LOGIN, 1));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.e("TAG", "登陆onResume()");
        MobclickAgent.onPageStart(fragmentName);
    }

    @Override
    public void onPause() {
        super.onPause();
//        Log.e("TAG", "登陆onPause()");
        MobclickAgent.onPageEnd(fragmentName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        Log.e("TAG", "登陆onDestroyView()");
        YoumiRoomUserManager.getInstance().unregisterListener(mUserListener);
    }

    private void setViewVisibility(final View view, int login_mode) {

        login_youmi_layout = (ViewStub) view.findViewById(R.id.login_youmi_view);
        login_memory_button = (ViewStub) view.findViewById(R.id.login_memory_button);

        login_umiwi_view = (TextView) view.findViewById(R.id.login_tv_umiwi);
        login_qq = (TextView) view.findViewById(R.id.login_tv_qqzone);
        login_weixin = (TextView) view.findViewById(R.id.login_tv_weixin);
        login_weibo = (TextView) view.findViewById(R.id.login_tv_sinaweibo);

        login_qq.setOnClickListener(new QQLoginListener());
        login_weixin.setOnClickListener(new WeiXinLoginListener());
        login_weibo.setOnClickListener(new WeiboLoginListener());

        login_umiwi_view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login_youmi_layout.setVisibility(View.VISIBLE);
                login_memory_button.setVisibility(View.GONE);
                setYoumiLayout(view);
                youmiViewClickListener();
                setVisibleButton(true, false, false, false);
            }


        });
        switch (login_mode) {//mode 1->umiwi, 5->weixin, 4->qq, 2->sinaweibo
            case 1:
                login_youmi_layout.setVisibility(View.VISIBLE);
                setYoumiLayout(view);
                youmiViewClickListener();
                setVisibleButton(true, false, false, false);
                break;
            case 5://weixin
                login_memory_button.setVisibility(View.VISIBLE);
                setMemoryButton(view);
                memory_login.setBackgroundResource(R.drawable.login_wechet_big);
                memory_lgoin_text.setText("上次你使用的是微信登录");
                memory_login.setOnClickListener(new WeiXinLoginListener());

                setVisibleButton(false, false, false, true);
                login_umiwi_view.setVisibility(View.VISIBLE);
                login_weibo.setVisibility(View.VISIBLE);
                login_qq.setVisibility(View.VISIBLE);
                login_weixin.setVisibility(View.GONE);
                break;
            case 4://qq
                login_memory_button.setVisibility(View.VISIBLE);
                setMemoryButton(view);
                memory_login.setBackgroundResource(R.drawable.login_qq_big);
                memory_lgoin_text.setText("上次你使用的是 Q Q 登录");
                memory_login.setOnClickListener(new QQLoginListener());

                setVisibleButton(false, false, true, false);
                login_umiwi_view.setVisibility(View.VISIBLE);
                login_weibo.setVisibility(View.VISIBLE);
                login_qq.setVisibility(View.GONE);
                login_weixin.setVisibility(View.VISIBLE);
                break;
            case 2://sina
                login_memory_button.setVisibility(View.VISIBLE);
                setMemoryButton(view);
                memory_login.setBackgroundResource(R.drawable.login_sinaweibo_big);
                memory_lgoin_text.setText("上次你使用的是微博登录");
                memory_login.setOnClickListener(new WeiboLoginListener());
                setVisibleButton(false, true, false, false);
                login_umiwi_view.setVisibility(View.VISIBLE);
                login_weibo.setVisibility(View.GONE);
                login_qq.setVisibility(View.VISIBLE);
                login_weixin.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }

    private void setVisibleButton(boolean isYoumiGone, boolean isWeiboGone, boolean isQQGone, boolean isWeixinGone) {
        login_umiwi_view.setVisibility(isYoumiGone ? View.GONE : View.VISIBLE);
        login_weibo.setVisibility(isWeiboGone ? View.GONE : View.VISIBLE);
        login_qq.setVisibility(isQQGone ? View.GONE : View.VISIBLE);
        login_weixin.setVisibility(isWeixinGone ? View.GONE : View.VISIBLE);
    }

    private void setYoumiLayout(View view) {
        login_umiwi = (TextView) view.findViewById(R.id.login_bt_login);
        et_username = (EditText) view.findViewById(R.id.login_et_username);
        et_password = (EditText) view.findViewById(R.id.login_et_password);
        replace_password = (TextView) view.findViewById(R.id.login_tv_forget_password);
        register = (TextView) view.findViewById(R.id.login_tv_fast_regist);
    }

    private void setMemoryButton(View view) {
        memory_login = (ImageView) view.findViewById(R.id.memory_login_image);
        memory_lgoin_text = (TextView) view.findViewById(R.id.memory_login_text);
    }

    private void youmiViewClickListener() {
        login_umiwi.setOnClickListener(new UmiwiLoginListener());
        register.setOnClickListener(new RegisterListener());
        replace_password.setOnClickListener(new ReplaceListener());
        et_password.setOnKeyListener(new UmiwiLoginKeyListener());
    }


    /**
     * 注册
     *
     * @author tangxiong
     * @version 2014年6月4日 下午3:14:55
     */
    private class RegisterListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            LoginUtil.getInstance().goRegister(getActivity());
            getActivity().finish();
        }
    }

    /**
     * 重置密码
     *
     * @author tangxiong
     * @version 2014年6月5日 上午11:31:13
     */
    private class ReplaceListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            LoginUtil.getInstance().geResetPassword(getActivity());
            getActivity().finish();
        }
    }

    /**
     * 优米帐户登入
     *
     * @author tangxiong
     * @version 2014年6月4日 下午2:56:08
     */
    private class UmiwiLoginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if ("".equals(et_username.getText().toString())) {
                ToastU.showShort(getActivity(), "用户名不能为空");
            } else if ("".equals(et_password.getText().toString())) {
                ToastU.showShort(getActivity(), "密码不能为空");
            } else {
                KeyboardUtils.hideKeyboard(getActivity());
                userLogin();
            }
        }
    }

    /**
     * 优米帐户登入 回车键
     *
     * @author tangxiong
     * @version 2014年6月4日 下午2:56:08
     */
    private class UmiwiLoginKeyListener implements View.OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if ("".equals(et_username.getText().toString())) {
                    ToastU.showShort(getActivity(), "用户名不能为空");
                } else if ("".equals(et_password.getText().toString())) {
                    ToastU.showShort(getActivity(), "密码不能为空");
                } else {
                    KeyboardUtils.hideKeyboard(getActivity());
                    userLogin();
                }
                return true;
            }
            return false;
        }
    }

    private class QQLoginListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            LoginUtil.getInstance().goLogin(getActivity(), YMLoginActivity.LOGIN_QQ);
        }

    }

    private class WeiXinLoginListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            LoginUtil.getInstance().goLogin(getActivity(), YMLoginActivity.LOGIN_WEIXIN);
        }

    }

    private class WeiboLoginListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            LoginUtil.getInstance().goLogin(getActivity(), YMLoginActivity.LOGIN_SINAWEIBO);
        }

    }

    /**
     * umiwi帐户登录
     */
    public void userLogin() {
        String strUserName = et_username.getText().toString().trim();// 用户名
        String strPassword = et_password.getText().toString();// 密码
        String md5Password = CommonHelper.encodeMD5(strPassword);// 给密码进行MD5加密
        String strUserNameU8 = null;
        try {
            strUserNameU8 = URLEncoder.encode(strUserName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String str = "username=" + strUserNameU8 + "&password="
                + md5Password + "&logintype=token";

        LoginUtil.getInstance().goLogin(getActivity(), YMLoginActivity.LOGIN_YOUMI, String.format(UmiwiAPI.UMIWI_USER_TOKEN, str));
    }

    /**
     * 登录成功后获取用户信息
     */
    private ModelManager.ModelStatusListener<UserEvent, UserModel> mUserListener = new ModelManager.ModelStatusListener<UserEvent, UserModel>() {

        @Override
        public void onModelGet(UserEvent key, UserModel models) {

        }

        @Override
        public void onModelUpdate(UserEvent key, UserModel model) {
            boolean isFirstOuthLogin = PreferenceUtils.getPrefBoolean(getActivity(), AbstractLoginActivity.FIRST_OUTH_LOGIN, false);
            switch (key) {
                case HOME_LOGIN:
                    if (YoumiRoomUserManager.getInstance().isLogin()) {// 如果登录了，记录测试题是否弹出过一次
                        if (!isFirstOuthLogin && !"yes".equals(YoumiRoomUserManager.getInstance().getUser().getUsertest())) {
                            if (88 == classestest) {
                                Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
                                i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, UserTestInfoFragment.class);
                                i.putExtra(UserTestInfoFragment.URL_CATEGORY, UmiwiAPI.USER_TEST_COURSE);
                                startActivity(i);
                                getActivity().finish();
                                return;
                            }
                        }
                    }
                    if (getActivity() != null) {
                        getActivity().finish();
                    }

                    if (isFirstOuthLogin) { // 顺序区分第三方的注册与登录
                        Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
                        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, BindingPhoneFragment.class);
                        startActivity(i);
                        return;
                    }

                    if (model.isDobindmobile()) {
                        Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
                        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, BindingPhoneFragment.class);
                        startActivity(i);
                    }
                    break;


                default:
                    break;
            }

        }

        @Override
        public void onModelsGet(UserEvent key, List<UserModel> models) {

        }
    };

}
