package com.umiwi.ui.fragment.setting;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.dialog.ChangeLogDialog;
import com.umiwi.ui.dialog.ShareDialog;
import com.umiwi.ui.fragment.down.SelectDownloadPathFragment;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.push.Utils;
import com.umiwi.ui.util.CommonHelper;

import java.util.List;

import cn.youmi.framework.dialog.MsgDialog;
import cn.youmi.framework.dialog.UpdateDialog;
import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.manager.ModelManager.ModelStatusListener;
import cn.youmi.framework.manager.VersionManager;
import cn.youmi.framework.model.VersionModel;
import cn.youmi.framework.util.AndroidSDK;
import cn.youmi.framework.util.DimensionUtil;
import cn.youmi.framework.util.SharePreferenceUtil;
import cn.youmi.framework.util.UpdateUtils;

/**
 * 
 * @author tangxiong
 * @version 2014年7月18日 下午3:10:29 
 */
public class SettingFragment extends BaseFragment implements OnClickListener{
	private TextView right_share;
	private TextView right_update;
	private TextView right_version;
	private TextView right_downpath;
	private ToggleButton right_ck_push;
	private ToggleButton right_show3g;
	
	private ProgressDialog mProgressDialog;
	
	private SharePreferenceUtil mSpUtil;

	@SuppressLint("InflateParams") 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, null);
		
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, "设置");
		
		mSpUtil = UmiwiApplication.getInstance().getSpUtil();
		
		right_share = (TextView) view.findViewById(R.id.right_bt_share);
		right_update = (TextView) view.findViewById(R.id.right_bt_update);
		right_version = (TextView) view.findViewById(R.id.right_bt_version);
		right_downpath = (TextView) view.findViewById(R.id.right_bt_downpath);
		right_ck_push = (ToggleButton) view.findViewById(R.id.right_ck_push);
		right_show3g = (ToggleButton) view.findViewById(R.id.right_show3g);
		RelativeLayout.LayoutParams layoutParams = (LayoutParams) right_ck_push.getLayoutParams();
		layoutParams.width = DimensionUtil.dip2px(45);
		layoutParams.height = DimensionUtil.dip2px(25);
		right_ck_push.setLayoutParams(layoutParams);
		RelativeLayout.LayoutParams layoutParams2 = (LayoutParams) right_show3g.getLayoutParams();
		layoutParams2.width = DimensionUtil.dip2px(45);
		layoutParams2.height = DimensionUtil.dip2px(25);
		right_show3g.setLayoutParams(layoutParams2);
		
		right_share.setOnClickListener(this);
		right_update.setOnClickListener(this);
		right_version.setOnClickListener(this);
		right_downpath.setOnClickListener(this);

		right_show3g.setChecked(!mSpUtil.getPlayWith3G());

		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setCanceledOnTouchOutside(false);

		VersionManager.getInstance().registerListener(versionListener);

		right_ck_push.setChecked(!mSpUtil.getDisturb());
		right_ck_push.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mSpUtil.setDisturb(!isChecked);
			}
		});
		right_show3g.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mSpUtil.setPalyWith3G(!isChecked);
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
	public void onClick(View v) {
		UmiwiContainerActivity ca = (UmiwiContainerActivity) getActivity();
		switch (v.getId()) {
		case R.id.right_bt_share:
			ShareDialog.getInstance().showDialog(getActivity(),
					getString(R.string.share_app_title),
					getString(R.string.share_app_content),
					getString(R.string.share_app_weburl),
					getString(R.string.share_app_image));
			break;
		case R.id.right_bt_update:
			if (CommonHelper.checkNetWifi(getActivity())) {
				progressShow("检查中...");
				String path = null;
				if (AndroidSDK.isICS()) {
					path = UmiwiApplication.getInstance().getResources().getString(R.string.serverurlics);
				} else {
					path = UmiwiApplication.getInstance().getResources().getString(R.string.serverurlgingerbread);
				}
				VersionManager.getInstance().checkNewVersion("setting", path);
			} else {
				showMsg("网络异常,请检查网络");
			}
			
			break;
		case R.id.right_bt_version:
			VersionFragment versionFragment = new VersionFragment();
			ca.getNavigationController().pushFragment(versionFragment);
			
			break;
		case R.id.right_bt_downpath:
			SelectDownloadPathFragment downloadPathFragment = new SelectDownloadPathFragment();
			ca.getNavigationController().pushFragment(downloadPathFragment);
			break;
		
		default:
			break;
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if (mSpUtil.getDisturb()) {
			PushManager.stopWork(getActivity());
		} else {
			PushManager.startWork(UmiwiApplication.getContext(), PushConstants.LOGIN_TYPE_API_KEY, Utils.getMetaValue(getActivity(), "api_key"));
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		VersionManager.getInstance().unregisterListener(versionListener);
	}


	private ModelStatusListener<String, VersionModel> versionListener = new ModelStatusListener<String, VersionModel>() {

		@Override
		public void onModelGet(String key, final VersionModel models) {
			if (key.contains("settingupdate")) {
				progressDissmiss();
				final MsgDialog dialog = new MsgDialog();
				dialog.setTitle(R.string.new_version_detected);
				dialog.setMessage(models.getDescription(), true);
				dialog.setPositiveButtonText(R.string.update);
				dialog.setPositiveButtonListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismissAllowingStateLoss();
						
						if (UpdateUtils.getAppVersion(UpdateUtils.updateAppFilePath, getActivity()).equals(models.getVersion())) {
							UpdateUtils.installApk(UpdateUtils.updateFile);
						} else {
							UpdateDialog updatedialog = new UpdateDialog();
							updatedialog.setDownUrl(models.getUrl());
							updatedialog.show(getActivity().getSupportFragmentManager(), "updatedialog");
						}
					}
				});
				dialog.show(getChildFragmentManager(), "dialog");
			} else if (key.contains("showlog")) {
				progressDissmiss();
				Toast.makeText(getActivity(), "已经是最新版了", Toast.LENGTH_SHORT).show();
//				showVersionLog(getActivity());
			} else if (key.contains("down")) {
				progressDissmiss();
			} else if (key.contains("downerror")) {
				showMsg("下载出错,请重试");
				progressDissmiss();
			} else if (key.contains("settingerror")){
				showMsg("网络异常");
				progressDissmiss();
			}
		}

		@Override
		public void onModelUpdate(String key, VersionModel model) {
			
		}

		@Override
		public void onModelsGet(String key, List<VersionModel> models) {
			
		}
	};
	
	public void showVersionLog(Context context){
		ChangeLogDialog.getInstance().showDialog(context);
//		final ChangeLogDialog changeLogDialog = new ChangeLogDialog();
//		changeLogDialog.show(getChildFragmentManager(), "ChangeLogDialog");
	}
	
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
	
}