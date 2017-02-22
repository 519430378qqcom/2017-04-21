package cn.youmi.pay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import cn.youmi.framework.activity.BaseSwipeBackActivity;

public class PayWebActivity extends BaseSwipeBackActivity {
	public static final String IF_BUY_IS_TRUE = "IFBUYISTRUE";
	public static final String WEB_URL = "WEBURL";
	private String web_Url;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSwipeBackEnable(false);//
		Intent intent = this.getIntent();
		web_Url = intent.getStringExtra(WEB_URL);
//		setResult(RESULT_OK);

		getSupportFragmentManager().beginTransaction()
				.replace(android.R.id.content, new PayWebFragment(web_Url))
				.commit();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
				slideToFinishActivity();
				return true;
			}
			getSupportFragmentManager().popBackStack(null, 0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
//
//	/* 重点要写返回键后的getVipIdentity();操作，未登录并且购买未成功时的操作 */
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			Intent intent = getIntent();
//			setResult(116, intent);
//			getSwipeBackLayout().scrollToFinishActivity();
//
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}

}
