
package com.umiwi.ui.fragment.user;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.CommonHelper;
import com.umiwi.ui.util.LoginUtil;

import java.util.List;

import cn.youmi.account.event.UserEvent;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.dialog.MsgDialog;
import cn.youmi.framework.manager.ModelManager.ModelStatusListener;
import cn.youmi.framework.manager.ResultEvent;
import cn.youmi.framework.manager.ResultManager;
import cn.youmi.framework.model.ResultModel;
import cn.youmi.framework.util.KeyboardUtils;

/**
 * 修改密码
 * @author tangxiyong
 * 2014-5-15上午10:11:16
 *
 */
public class ChangePasswordFragment extends BaseConstantFragment {
 	private EditText oldPasswordEditText, newPasswordEditText, passwordRepeatEdiText;
 	private TextView Tbtn;

	private ProgressDialog mProgressDialog;
	
	@SuppressLint("InflateParams") @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View view = inflater.inflate(R.layout.fragment_change_password, null);
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        setSupportActionBarAndToolbarTitle(mActionBarToolbar, "修改密码");
        ResultManager.getInstance().registerListener(mResultListener);
		YoumiRoomUserManager.getInstance().registerListener(mUserListener);
        mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setTitle("与服务器通信中");
		mProgressDialog.setMessage("请稍候...");

		 //设置页面显示用户名
        oldPasswordEditText = (EditText)view.findViewById(R.id.old_password_edit_text); 
        newPasswordEditText = (EditText)view.findViewById(R.id.new_password_edit_text); 
        passwordRepeatEdiText = (EditText)view.findViewById(R.id.password_repeat_edit_text); 
        Tbtn = (TextView)view.findViewById(R.id.commit_button);
       
        //提交点击事件
       Tbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String password_old =  oldPasswordEditText.getText().toString().trim();
				String password_new = newPasswordEditText.getText().toString().trim();
				String password_new1 = passwordRepeatEdiText.getText().toString().trim();
				if(!TextUtils.isEmpty(password_new1)&&!TextUtils.isEmpty(password_new)){
					
				if(password_new.equals(password_new1)) {
					changePassword(password_old, password_new,password_new1);
				} else {
					Toast toast = Toast.makeText(getActivity(), "两次输入的新密码不一致", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				}else{
					showMsg("请输入新密码。");
				}
				KeyboardUtils.hideKeyboard(getActivity());
 			}
		});
       
      return view;
	
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
		KeyboardUtils.hideKeyboard(getActivity());
		ResultManager.getInstance().unregisterListener(mResultListener);
		YoumiRoomUserManager.getInstance().unregisterListener(mUserListener);
	}

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
 
    public void changePassword(String password_old, String password_new, String password_new1) {
    	
    	mProgressDialog.show();
		Object[] sendData = new Object[2];  
        sendData[0] = CommonHelper.encodeMD5(password_old);  
        sendData[1] = CommonHelper.encodeMD5(password_new);  
        String changePassword_url = String.format(UmiwiAPI.UMIWI_CHANGE_PASSPORT,(Object[])sendData );
        ResultManager.getInstance().getResultInfo(changePassword_url, ResultEvent.USER_CHANGE_PASSWORD_SUCC);//邮箱用户注册结果
    }
    
    /** 修改密码成功与否*/
	private ModelStatusListener<ResultEvent, ResultModel> mResultListener = new ModelStatusListener<ResultEvent, ResultModel>() {

		@Override
		public void onModelGet(ResultEvent key, ResultModel models) {
		}

		@Override
		public void onModelUpdate(ResultEvent key, ResultModel model) {
			switch (key) {
			case USER_CHANGE_PASSWORD_SUCC:

				if (model.isSucc()) {
					YoumiRoomUserManager.getInstance().logout();
					YoumiRoomUserManager.getInstance().getUserInfoSave(UserEvent.HOME_CHANGE_PASSWORD);
				} else {
					mProgressDialog.cancel();
					showMsg(model.showMsg());
				}
			
				break;

			default:
				break;
			}
		}

		@Override
		public void onModelsGet(ResultEvent key, List<ResultModel> models) {
		}};
	
	/** 修改密码成功后获取用户信息*/
	private ModelStatusListener<UserEvent, UserModel> mUserListener = new ModelStatusListener<UserEvent, UserModel>() {

		@Override
		public void onModelGet(UserEvent key, UserModel models) {
			
		}

		@Override
		public void onModelUpdate(UserEvent key, UserModel model) {
			switch (key) {
			case HOME_CHANGE_PASSWORD:
				mProgressDialog.cancel();
				final MsgDialog dialog = new MsgDialog();
				dialog.setTitle("修改成功");
				dialog.setMessage("密码修改成功，请重新登录");
				dialog.setPositiveButtonText("确定");
				dialog.setPositiveButtonListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismissAllowingStateLoss();
						LoginUtil.getInstance().showLoginView(getActivity());
						getActivity().finish();
					}
				});
				dialog.show(getChildFragmentManager(), "dialog");
				break;

			default:
				break;
			}
					
		}

		@Override
		public void onModelsGet(UserEvent key, List<UserModel> models) {
			
		}};
		
}
