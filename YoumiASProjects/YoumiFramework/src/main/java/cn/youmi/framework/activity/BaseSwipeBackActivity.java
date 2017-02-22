package cn.youmi.framework.activity;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;
import android.os.Bundle;
import android.view.View;

/**
 * 滑动返回的Activity(全功能)
 * 
 * @author D_tangxiyong
 * @version 实现滑动返回接口：SwipeBackActivityBase
 */
public class BaseSwipeBackActivity extends BaseActivity implements
		SwipeBackActivityBase {
	private SwipeBackActivityHelper mSwipeBackHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSwipeBackHelper = new SwipeBackActivityHelper(this);
		mSwipeBackHelper.onActivityCreate();
		setSwipeBackEnable(true);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mSwipeBackHelper.onPostCreate();
	}

	@Override
	public View findViewById(int id) {
		View v = super.findViewById(id);
		if (v != null)
			return v;
		return mSwipeBackHelper.findViewById(id);
	}

	@Override
	public SwipeBackLayout getSwipeBackLayout() {
		return mSwipeBackHelper.getSwipeBackLayout();
	}

	/**
	 * 是否手势滑动
	 */
	@Override
	public void setSwipeBackEnable(boolean enable) {
		getSwipeBackLayout().setEnableGesture(enable);
	}

	@Override
	public void slideToFinishActivity() {
		getSwipeBackLayout().scrollToFinishActivity();
	}
	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {//this.getResources().getIdentifier("home", "id", "android.support.v7.appcompat");
//		case android.R.id.home:
//			if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
//				scrollToFinishActivity();
//				return true;
//			}
//			getSupportFragmentManager().popBackStack(null, 0);
//			return true;
//
//		}
//		return super.onOptionsItemSelected(item);
//	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
//				slideToFinishActivity();
//				return true;
//			}
//			getSupportFragmentManager().popBackStack(null, 0);
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}

}
