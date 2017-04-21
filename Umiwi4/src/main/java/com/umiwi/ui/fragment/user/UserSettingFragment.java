
package com.umiwi.ui.fragment.user;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushManager;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.activity.UmiwiDetailActivity;
import com.umiwi.ui.camera.CameraActivity;
import com.umiwi.ui.camera.CameraManager;
import com.umiwi.ui.camera.CameraResultEvent;
import com.umiwi.ui.dialog.SelectImageDialog;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.PermissionUtil;

import java.io.ByteArrayOutputStream;
import java.util.List;

import cn.youmi.account.event.UserEvent;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.dialog.MsgDialog;
import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.PostRequest;
import cn.youmi.framework.http.parsers.StringParser;
import cn.youmi.framework.manager.ModelManager.ModelStatusListener;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.util.ImageUtillity;
import cn.youmi.framework.util.ToastU;
import cn.youmi.framework.view.CircleImageView;


/**
 * 完善个人信息
 *
 * @author tangxiong
 * @version 2014年6月13日 下午3:58:05
 */
public class UserSettingFragment extends BaseFragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    private CircleImageView useravatar;
    private RelativeLayout name;
    private RelativeLayout pass;
    private RelativeLayout useravatarRL;
    private RelativeLayout bindingPhone;
    private ProgressDialog mProgressDialog;
    private TextView name_tv;
    private RelativeLayout mSeting;
    private Tencent mTencent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("图片上传中,请稍候...");

        View view = inflater.inflate(R.layout.fragment_mine_setting, null);
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        setSupportActionBarAndToolbarTitle(mActionBarToolbar, "完善个人信息");
        init(view);
        YoumiRoomUserManager.getInstance().registerListener(userListener);
        CameraManager.getInstance().registerListener(cameraListener);
        return view;
    }

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
        YoumiRoomUserManager.getInstance().unregisterListener(userListener);
        CameraManager.getInstance().unregisterListener(cameraListener);
    }

    private void init(View view) {
        useravatar = (CircleImageView) view.findViewById(R.id.f_mine_avatar);
        name = (RelativeLayout) view.findViewById(R.id.name_rl);
        useravatarRL = (RelativeLayout) view.findViewById(R.id.head_rl);
        name_tv = (TextView) view.findViewById(R.id.name_tv);
        pass = (RelativeLayout) view.findViewById(R.id.pass_rl);
        bindingPhone = (RelativeLayout) view.findViewById(R.id.binding_rl);
        mSeting = (RelativeLayout) view.findViewById(R.id.setting);
        String userName = YoumiRoomUserManager.getInstance().getUser().getUsername();
        if (userName.startsWith("#")) {
            name_tv.setText(userName);
            name.setVisibility(View.VISIBLE);
        } else {
            pass.setVisibility(View.VISIBLE);
        }
        useravatarRL.setOnClickListener(mAvatarOnClickListener);
        useravatar.setOnClickListener(mAvatarOnClickListener);
        name.setOnClickListener(new NameOnClickListener());
        pass.setOnClickListener(new PassOnClickListener());
        mSeting.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (YoumiRoomUserManager.getInstance().isLogin()) {
                    final MsgDialog dialog = new MsgDialog();
                    dialog.setTitle(R.string.quit_confirm);
                    dialog.setPositiveButtonText(R.string.logout);
                    dialog.setMsg(R.string.are_yout_sure_your_wanna_loggout);
                    dialog.setPositiveButtonListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismissAllowingStateLoss();
                            mProgressDialog.setTitle(R.string.logging_out);
                            mProgressDialog.setMessage("请稍候..");
                            mProgressDialog.show();
                            YoumiRoomUserManager.getInstance().logout();
                            YoumiRoomUserManager.getInstance().getUserInfoSave(UserEvent.HOME_LOGIN_OUT);
//                            if (mTencent != null) {
//                                mTencent.logout(getActivity());
//                            }
                            PushManager.stopWork(getActivity());
//                            UmiwiDetailActivity.activity.finish();
//                            UmiwiDetailActivity.this.finish();
                            startActivity(new Intent(getActivity(), UmiwiDetailActivity.class));
                        }
                    });
                    dialog.show(getChildFragmentManager(), "dialog");
                } else {
                    Toast.makeText(getActivity(), "当前没有帐号", Toast.LENGTH_SHORT).show();
                }

            }
        });
        bindingPhone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                UmiwiContainerActivity ca = (UmiwiContainerActivity) getActivity();
                if (!(YoumiRoomUserManager.getInstance().getUser().getMobile().length() >= 10)) {
                    BindingPhoneFragment bindingPhoneFragment = new BindingPhoneFragment();
                    ca.getNavigationController().pushFragment(bindingPhoneFragment);
                } else {
                    BindingPhoneOkFragment bindingPhoneOkFragment = new BindingPhoneOkFragment();
                    ca.getNavigationController().pushFragment(bindingPhoneOkFragment);
                }
            }
        });

        if (!isRemoving()) {//http://bugly.qq.com/detail?app=900012791&pid=1&ii=133#stack
            ImageLoader mImageLoader = new ImageLoader(this.getActivity());
            mImageLoader.loadImage(YoumiRoomUserManager.getInstance().getUser().getAvatar(), useravatar);
        }
    }

    /**
     * 重置带＃号的用户名
     *
     * @author tangxiong
     * @version 2014年6月12日 下午6:07:50
     *          TODO
     */
    class NameOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            ChangeUserNameAndPasswordFragment cardMineUserInfoFragment = new ChangeUserNameAndPasswordFragment();
            UmiwiContainerActivity ca = (UmiwiContainerActivity) getActivity();
            ca.getNavigationController().pushFragment(cardMineUserInfoFragment);
        }

    }

    /**
     * 修改密码
     *
     * @author tangxiong
     * @version 2014年6月12日 下午6:08:19
     */
    class PassOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            ChangePasswordFragment cardMineUserInfoFragment = new ChangePasswordFragment();
            UmiwiContainerActivity ca = (UmiwiContainerActivity) getActivity();
            ca.getNavigationController().pushFragment(cardMineUserInfoFragment);
        }

    }

    OnClickListener mAvatarOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final SelectImageDialog dialog = new SelectImageDialog();
            dialog.setTitle("选择上传头像方式");
            dialog.setUploadLocalListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismissAllowingStateLoss();
                    Intent i = new Intent(getActivity(), CameraActivity.class);
                    i.putExtra(CameraActivity.KEY_CUT, true);
                    i.putExtra(CameraActivity.KEY_CUT_WIDTH, 180);
                    i.putExtra(CameraActivity.KEY_CUT_HEIGHT, 180);
                    i.putExtra(CameraActivity.KEY_TYPE, CameraActivity.TYPE_PICTURE);
                    startActivity(i);
                }
            });
            dialog.setTakePictureListener(new OnClickListener() {
                @Override
                public void onClick(View v) {//拍照

                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                            Snackbar.make(useravatarRL, "需要授权使用摄像头", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("授权", new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            ActivityCompat.requestPermissions(getActivity(), REQUEST_CAMERA_PERMISSIONS, REQUEST_PERMISSION_CODE_TAKE_CAMERA);
                                        }
                                    })
                                    .show();
                        } else {
                            ActivityCompat.requestPermissions(getActivity(), REQUEST_CAMERA_PERMISSIONS, REQUEST_PERMISSION_CODE_TAKE_CAMERA);
                        }
                        return;
                    }

                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Snackbar.make(useravatarRL, "需要授权读取存储卡", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("授权", new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            ActivityCompat.requestPermissions(getActivity(), REQUEST_STORAGE_PERMISSIONS, REQUEST_PERMISSION_CODE_TAKE_STORAGE);
                                        }
                                    })
                                    .show();
                        } else {
                            ActivityCompat.requestPermissions(getActivity(), REQUEST_STORAGE_PERMISSIONS, REQUEST_PERMISSION_CODE_TAKE_STORAGE);
                        }
                        return;
                    }
                    dialog.dismissAllowingStateLoss();
                    Intent i = new Intent(getActivity(), CameraActivity.class);
                    i.putExtra(CameraActivity.KEY_CUT, true);
                    i.putExtra(CameraActivity.KEY_CUT_WIDTH, 180);
                    i.putExtra(CameraActivity.KEY_CUT_HEIGHT, 180);
                    i.putExtra(CameraActivity.KEY_TYPE, CameraActivity.TYPE_CAMERA);
                    startActivity(i);

                }
            });
            dialog.show(getChildFragmentManager(), "dialog");
        }
    };

    private String[] REQUEST_CAMERA_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    private String[] REQUEST_STORAGE_PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_PERMISSION_CODE_TAKE_CAMERA = 6;
    private static final int REQUEST_PERMISSION_CODE_TAKE_STORAGE = 7;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case REQUEST_PERMISSION_CODE_TAKE_CAMERA:
                if (PermissionUtil.verifyPermissions(grantResults)) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Snackbar.make(useravatarRL, "需要授权读取存储卡", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("授权", new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            ActivityCompat.requestPermissions(getActivity(), REQUEST_STORAGE_PERMISSIONS, REQUEST_PERMISSION_CODE_TAKE_STORAGE);
                                        }
                                    })
                                    .show();
                        } else {
                            ActivityCompat.requestPermissions(getActivity(), REQUEST_STORAGE_PERMISSIONS, REQUEST_PERMISSION_CODE_TAKE_STORAGE);
                        }
                        return;
                    }
                    Intent i = new Intent(getActivity(), CameraActivity.class);
                    i.putExtra(CameraActivity.KEY_CUT, true);
                    i.putExtra(CameraActivity.KEY_CUT_WIDTH, 180);
                    i.putExtra(CameraActivity.KEY_CUT_HEIGHT, 180);
                    i.putExtra(CameraActivity.KEY_TYPE, CameraActivity.TYPE_CAMERA);
                    startActivity(i);
                } else {
                    ToastU.showShort(getActivity(), "没有权限");
                }
                break;
            case REQUEST_PERMISSION_CODE_TAKE_STORAGE:
                if (PermissionUtil.verifyPermissions(grantResults)) {
                    Intent i = new Intent(getActivity(), CameraActivity.class);
                    i.putExtra(CameraActivity.KEY_CUT, true);
                    i.putExtra(CameraActivity.KEY_CUT_WIDTH, 180);
                    i.putExtra(CameraActivity.KEY_CUT_HEIGHT, 180);
                    i.putExtra(CameraActivity.KEY_TYPE, CameraActivity.TYPE_CAMERA);
                    startActivity(i);
                } else {
                    ToastU.showShort(getActivity(), "没有权限");
                }
                break;
        }
    }

    public void uploadAvatar(Bitmap bitmap) {
        mProgressDialog.show();
        if (null != bitmap) {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
            byte[] imageData = bao.toByteArray();
            final String encodedImageData = Base64.encodeToString(imageData, Base64.DEFAULT);
            PostRequest<String> prequest = new PostRequest<String>(UmiwiAPI.UMIWI_AVATAR_UPLOAD, StringParser.class, listener);
            prequest.addParam("image", encodedImageData);//String
            prequest.go();
        } else {
            ToastU.showLong(getActivity(), "裁剪失败，请重试");
        }

    }

    private Listener<String> listener = new Listener<String>() {
        @Override
        public void onResult(AbstractRequest<String> request, String t) {
            useravatar.setImageBitmap(ImageUtillity.decodeBitmapFromSDCard(pricPath, 0, 0));

            YoumiRoomUserManager.getInstance().getUserInfoSave(UserEvent.AVATAR);
        }

        @Override
        public void onError(AbstractRequest<String> requet, int statusCode,
                            String body) {
            mProgressDialog.dismiss();
            showMsg("上传失败,请重试");
        }
    };

    private ModelStatusListener<UserEvent, UserModel> userListener = new ModelStatusListener<UserEvent, UserModel>() {
        @Override
        public void onModelGet(UserEvent key, UserModel models) {

        }

        @Override
        public void onModelUpdate(UserEvent key, UserModel model) {
            switch (key) {
                case HOME_CHANGE_PASSWORD:
                    progressDissmiss();
                    getActivity().finish();
                    break;
                case AVATAR:
                    showMsg("上传成功");
                    mProgressDialog.dismiss();
                    break;
                case HOME_LOGIN_OUT:
                    progressDissmiss();
                    getActivity().finish();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onModelsGet(UserEvent key, List<UserModel> models) {

        }
    };


    private void progressDissmiss() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void progressShow(String message) {
        mProgressDialog.setMessage(message);
        if (mProgressDialog != null)
            mProgressDialog.show();
    }

    private String pricPath;

    private ModelStatusListener<CameraResultEvent, String> cameraListener = new ModelStatusListener<CameraResultEvent, String>() {

        @Override
        public void onModelGet(CameraResultEvent key, String models) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onModelUpdate(CameraResultEvent key, String s) {
            switch (key) {
                case SUCC:
                    pricPath = s;
                    uploadAvatar(ImageUtillity.decodeBitmapFromSDCard(pricPath, 0, 0));
                    break;
                case ERROR:
                    ToastU.showLong(getActivity(), "提取图片失败,请重新选取照片");
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onModelsGet(CameraResultEvent key, List<String> models) {
            // TODO Auto-generated method stub

        }
    };


}
