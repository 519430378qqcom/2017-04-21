package com.umiwi.ui.fragment.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.CommonHelper;
import com.umiwi.ui.util.LoginUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import cn.youmi.account.event.UserEvent;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.dialog.MsgDialog;
import cn.youmi.framework.manager.ModelManager.ModelStatusListener;
import cn.youmi.framework.manager.ResultEvent;
import cn.youmi.framework.manager.ResultManager;
import cn.youmi.framework.model.ResultModel;
import cn.youmi.framework.util.KeyboardUtils;

/***
 * #号用户：更改用户名和密码
 * 
 * @author tjie00 2014-04-22
 * 
 */
public class ChangeUserNameAndPasswordFragment extends BaseConstantFragment {

	/** 指定布局xml */
	private View view;

	private TextView tvCommit;
	private EditText etUsername;
	private EditText etPassword;

	private LinearLayout llPassword;

	private ProgressBar mProgressBar;

	private String strPass;

	@SuppressLint("InflateParams") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.userinfo_name_password_fragment, null);
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, "修改用户名");

		ResultManager.getInstance().registerListener(mResultListener);
		YoumiRoomUserManager.getInstance().registerListener(mUserListener);

		initView(view);

		setListener();

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
		ResultManager.getInstance().unregisterListener(mResultListener);
		YoumiRoomUserManager.getInstance().unregisterListener(mUserListener);
	}

	protected void dissmis() {
		if (mProgressBar != null && mProgressBar.isShown()) {
			mProgressBar.setVisibility(View.GONE);
		}
	}

	// 设置监听器
	private void setListener() {
		CommitListener commitButtonListener = new CommitListener();

		tvCommit.setOnClickListener(commitButtonListener);

	}

	/**
	 * 初始化
	 */
	private void initView(View view) {
		tvCommit = (TextView) view.findViewById(R.id.tv_commit);

		etUsername = (EditText) view.findViewById(R.id.et_user_name);
		etPassword = (EditText) view.findViewById(R.id.et_password);

		llPassword = (LinearLayout) view.findViewById(R.id.ll_password);
		llPassword.setVisibility(View.VISIBLE);

		mProgressBar = (ProgressBar) view.findViewById(R.id.loading);

	}

	private class CommitListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.tv_commit:
				String strUsername = etUsername.getText().toString().trim();
				if (TextUtils.isEmpty(strUsername)) {
					showMsg("用户名不能为空");
					return;
				}

				String strPassword = etPassword.getText().toString().trim();
				if (TextUtils.isEmpty(strPassword)) {
					showMsg("密码不能为空");
					return;
				} else if (strPassword.length() < 6) {
					showMsg("密码长度不对");
					return;
				}

				KeyboardUtils.hideKeyboard(getActivity());
				commit(strUsername, strPassword);

				break;

			default:
				break;
			}

		}
	}

	protected void commit(final String strUsername, final String strPassword) {

		if (!mProgressBar.isShown()) {
			mProgressBar.setVisibility(View.VISIBLE);
		}

		String strUserNameU8 = null;
		try {
			strUserNameU8 = URLEncoder.encode(strUsername, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String strPasswordMD5 = CommonHelper.encodeMD5(strPassword);

		final String strCommit = String.format(UmiwiAPI.COMMIT_URL,
				strUserNameU8, strPasswordMD5);
		ResultManager.getInstance().getResultInfo(strCommit, ResultEvent.USER_CHANGE_NAME_AND_PASSWORD_SUCC);
	}

	/** 修改用户名与密码成功与否 */
	private ModelStatusListener<ResultEvent, ResultModel> mResultListener = new ModelStatusListener<ResultEvent, ResultModel>() {

		@Override
		public void onModelGet(ResultEvent key, ResultModel models) {
		}

		@Override
		public void onModelUpdate(ResultEvent key, ResultModel model) {
			switch (key) {
			case USER_CHANGE_NAME_AND_PASSWORD_SUCC:
				if (model.isSucc()) {
					YoumiRoomUserManager.getInstance().logout();
					YoumiRoomUserManager.getInstance().getUserInfoSave(UserEvent.HOME_CHANGE_NAME_AND_PASSWORD);
				} else {
					showMsg(model.showMsg());
					dissmis();
				}
				break;

			default:
				break;
			}
		}

		@Override
		public void onModelsGet(ResultEvent key, List<ResultModel> models) {
		}};

	/** 修改密码成功后获取用户信息 */
	private ModelStatusListener<UserEvent, UserModel> mUserListener = new ModelStatusListener<UserEvent, UserModel>() {

		@Override
		public void onModelGet(UserEvent key, UserModel models) {
		}

		@Override
		public void onModelUpdate(UserEvent key, UserModel model) {
			switch (key) {
			case HOME_CHANGE_NAME_AND_PASSWORD:

				dissmis();
				final MsgDialog dialog = new MsgDialog();
				dialog.setTitle("修改成功");
				dialog.setMessage("用户名密码设置成功,请重新登录");
				dialog.setPositiveButtonText("确定");
				dialog.setPositiveButtonListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismissAllowingStateLoss();
						LoginUtil.getInstance().showLoginView(getActivity());
						getActivity().finish();
					}
				});
				dialog.setNegativeButtonListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismissAllowingStateLoss();
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

	public String getPassword() {
		return this.strPass;
	}
}
