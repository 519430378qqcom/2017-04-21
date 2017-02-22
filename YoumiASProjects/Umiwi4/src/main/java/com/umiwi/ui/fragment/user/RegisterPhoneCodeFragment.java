package com.umiwi.ui.fragment.user;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.CommonHelper;
import com.umiwi.ui.util.ImageCookieUtils;
import com.umiwi.ui.util.ManifestUtils;

import java.util.Calendar;
import java.util.List;

import cn.youmi.account.event.UserEvent;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.CookieDao;
import cn.youmi.framework.http.CookieModel;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.PostRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.manager.ModelManager.ModelStatusListener;
import cn.youmi.framework.model.ResultModel;
import cn.youmi.framework.util.KeyboardUtils;
import cn.youmi.framework.util.ToastU;

/**
 * 手机号注册设置信息
 *
 * @author tangxiong
 * @version 2014年9月12日 下午4:44:40
 */
public class RegisterPhoneCodeFragment extends BaseFragment {

    private ProgressBar mProgressBar;
    private EditText phoneCodeNum;
    private TextView resendPhoneCode;
    private TextView countDown;
    private EditText username;
    private EditText password;
    private EditText passwordRepeat;
    private TextView succContext;
    private TextView commit;
    private String strMobileNumber;

    private RelativeLayout imageCodeLayout;
    private ImageView codeImage;
    private EditText codeNum;

    private boolean isImageCodeNull;
    private boolean isImageCodeFail;
    private boolean isCodeNum;

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    public static final String PHONE_NUMBER = "PHONE_NUMBER";

    private int countNum = 60;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            countNum--;
            countDown.setText(Html.fromHtml("<font color='#7eb706'>" + String.valueOf(countNum) + "</font>" + " 秒后重新获取"));
            if (countNum >= 1) {
                handler.postDelayed(this, 1000);
            } else {
                countDown.setVisibility(View.GONE);
                resendPhoneCode.setText(Html.fromHtml("收不到验证码?  " + "<font color='#006ed3'>" + "重新发送" + "</font>"));
                resendPhoneCode.setVisibility(View.VISIBLE);
                countNum = 60;
                handler.removeCallbacks(runnable);
            }

        }
    };


    public static RegisterPhoneCodeFragment newInstance(String strPhone) {
        RegisterPhoneCodeFragment fragment = new RegisterPhoneCodeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PHONE_NUMBER, strPhone);
        fragment.setArguments(bundle);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_phone_code, null);
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        setSupportActionBarAndToolbarTitle(mActionBarToolbar, "设置信息");
//		Bundle bundle = getArguments();
//		strMobileNumber = bundle.getString(PHONE_NUMBER);
        strMobileNumber = getActivity().getIntent().getStringExtra(PHONE_NUMBER);

        mProgressBar = (ProgressBar) view.findViewById(R.id.loading);

        phoneCodeNum = (EditText) view.findViewById(R.id.phone_code_number_et);
        resendPhoneCode = (TextView) view.findViewById(R.id.phone_resend_code_textview);

        countDown = (TextView) view.findViewById(R.id.countdown);

        username = (EditText) view.findViewById(R.id.register_username_edittext);
        password = (EditText) view.findViewById(R.id.register_password_edittext);
        passwordRepeat = (EditText) view.findViewById(R.id.register_password_repeat_edittext);

        succContext = (TextView) view.findViewById(R.id.register_succ_context);
        commit = (TextView) view.findViewById(R.id.register_commit);

        imageCodeLayout = (RelativeLayout) view.findViewById(R.id.imagecode_layout);
        codeImage = (ImageView) view.findViewById(R.id.code_imageview);
        codeNum = (EditText) view.findViewById(R.id.code_number_et);

        succContext.setText("已经发送验证码到: " + strMobileNumber);
        handler.postDelayed(runnable, 1000);

        resendPhoneCode.setOnClickListener(new resendPhonecodeListener());
        commit.setOnClickListener(new CommitListener());

        codeImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isImageCodeFail) {
                    ImageCookieUtils.getInstance().getImagecode(
                            String.format(UmiwiAPI.USER_GET_IMAGE_CODE,
                                    System.currentTimeMillis() + ""),
                            codeImage, responseListener, errorListener);
                }
            }
        });

        YoumiRoomUserManager.getInstance().registerListener(mUserListener);

        return view;

    }

    private class CommitListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                String strPhoneCode = phoneCodeNum.getText().toString().trim();
                String strUsername = username.getText().toString().trim();
                String strPassword = password.getText().toString().trim();
                String strPasswordRepeat = passwordRepeat.getText().toString().trim();
                if (TextUtils.isEmpty(strPhoneCode)) {
                    showMsg("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(strUsername)) {
                    showMsg("请输入用户名");
                    return;
                }
                if (TextUtils.isEmpty(strPassword)) {
                    showMsg("请输入密码");
                    return;
                }
                if (strPassword.length() < 6 || strPassword.length() > 16) {
                    showMsg("密码长度必须是6-16位");
                    return;
                }
                if (TextUtils.isEmpty(strPasswordRepeat)) {
                    showMsg("请确认密码");
                    return;
                }
                if (!strPassword.equals(strPasswordRepeat)) {
                    showMsg("两次密码不一致，请重新设置");
                    return;
                }

                showProgress();
                KeyboardUtils.hideKeyboard(getActivity());

                //registerMobile(strMobileNumber, CommonHelper.encodeMD5(strPassword), URLEncoder.encode(strUsername, "UTF-8"), strPhoneCode);
                registerMobile(strMobileNumber, CommonHelper.encodeMD5(strPassword), strUsername, strPhoneCode);
            }

        }
    }

    /**
     * 注册 http://passport.youmi.cn/mobile/newreg/mobile=%s&password=%s&username=%s&captcha=%s
     */
    private void registerMobile(String strMobileNumber, String strPassword, String strUsername, String strPhoneCode) {
        String versonStr = null;
        try {
            versonStr = CommonHelper.getVersionName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        PostRequest<ResultModel> get = new PostRequest<ResultModel>("http://passport.youmi.cn/mobile/newreg/", GsonParser.class, ResultModel.class, mResultListener);
        get.addParam("mobile", strMobileNumber);
        get.addParam("password", strPassword);
        get.addParam("username", strUsername);
        get.addParam("captcha", strPhoneCode);
        get.addParam("mid", "2");
        get.addParam("ext1", CommonHelper.getModel());
        get.addParam("ref", "a-" + ManifestUtils.getChannelString(UmiwiApplication.getContext()));
        get.addParam("channel", ManifestUtils.getChannelString(UmiwiApplication.getContext()));
        get.addParam("version", versonStr);

        get.go();
    }

    private class resendPhonecodeListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if (isCodeNum) {// 请求手机验证码
                String strImageCodeNum = codeNum.getText().toString().trim();
                if (TextUtils.isEmpty(strImageCodeNum)) {
                    showMsg("验证码不能为空");
                    return;
                }
                showProgress();

                getPhoneAndImagesCode(strMobileNumber, strImageCodeNum);
            } else {
                showProgress();

                getPhoneAndImagesCode(strMobileNumber, "");
            }

        }
    }

    /**
     * 获取手机验证码
     *
     * @param phoneNum
     * @param imageCodeNum 图片验证码
     */
    private void getPhoneAndImagesCode(String phoneNum, String imageCodeNum) {
        String loginURL;
        if (TextUtils.isEmpty(imageCodeNum)) {
            loginURL = String.format(UmiwiAPI.USER_SEND_PHONES_CODE, phoneNum);
            isImageCodeNull = true;
        } else {
            loginURL = String.format(UmiwiAPI.USER_MOBILE_CAPTCHA_IMAGE, phoneNum, imageCodeNum);
            isImageCodeNull = false;
        }

        final String imageCodeNumTemp = imageCodeNum;

        GetRequest<ResultModel> get = new GetRequest<ResultModel>(loginURL, GsonParser.class, ResultModel.class, phoneListener);

        if (!TextUtils.isEmpty(imageCodeNumTemp)) {
            CookieModel model = CookieDao.getInstance(
                    UmiwiApplication.getInstance()).getByName("passport");
            if (model != null && !TextUtils.isEmpty(model.getValue())) {
                get.setHeader("COOKIE", "passport=" + model.getValue() + ";");
            }
        }

        HttpDispatcher.getInstance().go(get);
    }

    private Listener<ResultModel> phoneListener = new Listener<ResultModel>() {

        @Override
        public void onResult(AbstractRequest<ResultModel> request, ResultModel t) {
            if (!t.isSucc()) {// 需要输入图片验证码
                ImageCookieUtils.getInstance().getImagecode(
                        String.format(UmiwiAPI.USER_GET_IMAGE_CODE,
                                System.currentTimeMillis() + ""),
                        codeImage, responseListener, errorListener);
                if (!isImageCodeNull) {
                    ToastU.showShort(getActivity(), t.showMsg());
                }
                imageCodeLayout.setVisibility(View.VISIBLE);
                isCodeNum = true;
            } else {// 发送手机验证码成功
                countDown.setVisibility(View.VISIBLE);
                resendPhoneCode.setVisibility(View.GONE);
                dissmiss();
            }
        }

        @Override
        public void onError(AbstractRequest<ResultModel> requet,
                            int statusCode, String body) {
            showMsg(body);
            dissmiss();
        }

    };

    Response.Listener<Bitmap> responseListener = new Response.Listener<Bitmap>() {
        @Override
        public void onResponse(Bitmap bitmap) {
            codeImage.setImageBitmap(bitmap);
            dissmiss();
        }
    };
    ErrorListener errorListener = new ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError arg0) {
            codeImage.setImageDrawable(UmiwiApplication.getInstance().getResources().getDrawable(R.drawable.image_loader_min));
            showMsg("获取图片验证码失败,点击重试");
            isImageCodeFail = true;
            dissmiss();
        }
    };


    /**
     * 注册结果是否成功
     */
    private Listener<ResultModel> mResultListener = new Listener<ResultModel>() {
        @Override
        public void onResult(AbstractRequest<ResultModel> request, ResultModel resultModel) {
            if (null != resultModel && resultModel.isSucc()) {// 注册结果成功
                YoumiRoomUserManager.getInstance().getUserInfoSave(UserEvent.USER_MOBILE_REGISTE_SUCC_NO_INFO, "");
            } else {
                ToastU.showShort(getActivity(), resultModel.showMsg());
                dissmiss();
            }
        }

        @Override
        public void onError(AbstractRequest<ResultModel> requet, int statusCode, String body) {
            ToastU.showShort(getActivity(), body);
            dissmiss();
        }
    };

    /**
     * 手机注册成功后获取用户信息
     */
    private ModelStatusListener<UserEvent, UserModel> mUserListener = new ModelStatusListener<UserEvent, UserModel>() {

        @Override
        public void onModelGet(UserEvent key, UserModel models) {
        }

        @Override
        public void onModelUpdate(UserEvent key, UserModel model) {
            switch (key) {
                case USER_MOBILE_REGISTE_SUCC_NO_INFO:
                    dissmiss();
                    UmiwiContainerActivity ca = (UmiwiContainerActivity) getActivity();
                    ca.slideToFinishActivity();
                    break;

                default:
                    break;
            }
            dissmiss();


        }

        @Override
        public void onModelsGet(UserEvent key, List<UserModel> models) {
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);
        YoumiRoomUserManager.getInstance().unregisterListener(mUserListener);
    }

    protected void dissmiss() {
        if (mProgressBar.isShown()) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    protected void showProgress() {
        if (!mProgressBar.isShown()) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }
}
