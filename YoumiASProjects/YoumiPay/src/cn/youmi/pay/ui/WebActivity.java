package cn.youmi.pay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import cn.youmi.framework.activity.BaseSwipeBackActivity;

public class WebActivity extends BaseSwipeBackActivity {
	public static final String WEB_URL = "WEBURL";
	private String web_Url;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getOverflowMenu();

		Intent intent = this.getIntent();
		web_Url = intent.getStringExtra(WEB_URL);

		getSupportFragmentManager().beginTransaction()
				.replace(android.R.id.content, new WebFragment(web_Url))
				.commit();

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {//this.getResources().getIdentifier("home", "id", "android.support.v7.appcompat");
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
//	/** action voerflow actionbar是的三点*/
//	private void getOverflowMenu() {//http://stackoverflow.com/questions/15492791/how-do-i-show-overflow-menu-items-to-action-bar-in-android
//        try {
//           ViewConfiguration config = ViewConfiguration.get(this);
//           Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
//           if(menuKeyField != null) {
//               menuKeyField.setAccessible(true);
//               menuKeyField.setBoolean(config, false);
//           }
//       } catch (Exception e) {
//           e.printStackTrace();
//       }
//   }

}
