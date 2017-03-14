package com.umiwi.ui.activity;

import java.util.List;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import cn.youmi.account.event.UserEvent;
import cn.youmi.framework.activity.FragmentContainerActivity;
import cn.youmi.framework.dialog.MsgDialog;
import cn.youmi.framework.manager.ModelManager.ModelStatusListener;
import cn.youmi.framework.manager.UserKillLogoutManager;
import cn.youmi.framework.manager.UserKillLogoutManager.UserKillEvent;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.LoginUtil;

public class UmiwiContainerActivity extends FragmentContainerActivity {
	
	@Override
	protected int theme() {
		return R.style.Theme_Umiwi_base;
	}

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		UserKillLogoutManager.getInstance().registerListener(userKillListener);
	}

	@Override
	protected void onPause() {
		super.onPause();
//		if (getSwipeBackLayout().isSlideFinish()) {
//			LogUtils.e("detailactivity", "===onPause");
//		}
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		UserKillLogoutManager.getInstance().unregisterListener(userKillListener);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return keyCode == KeyEvent.KEYCODE_MENU || super.onKeyDown(keyCode, event);
	}

	private ModelStatusListener<UserKillEvent, String> userKillListener = new ModelStatusListener<UserKillEvent, String>() {

		@Override
		public void onModelGet(UserKillEvent key, String models) {
		}

		@Override
		public void onModelUpdate(UserKillEvent key, String model) {
			switch (key) {
			case KILL_LOGOUT:
				YoumiRoomUserManager.getInstance().logout();
				YoumiRoomUserManager.getInstance().getUserInfoSave(UserEvent.LOGGED_KICK_OUT);
				MsgDialog dialog = new MsgDialog();
				dialog.setTitle("退出提示");
				dialog.setMessage(model);

				dialog.setNegativeButtonText("确定");
				dialog.setNegativeButtonListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						UmiwiContainerActivity.this.finish();
					}
				});

				dialog.setPositiveButtonText("重新登录");
				dialog.setPositiveButtonListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						LoginUtil.getInstance().showLoginView(UmiwiContainerActivity.this);
						UmiwiContainerActivity.this.finish();
					}
				});
				dialog.show(getSupportFragmentManager(), "logoutdialog");
				break;

			default:
				break;
			}
		}

		@Override
		public void onModelsGet(UserKillEvent key, List<String> models) {
		}
	};

}
