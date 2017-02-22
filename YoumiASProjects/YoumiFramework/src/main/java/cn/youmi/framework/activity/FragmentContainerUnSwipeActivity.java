package cn.youmi.framework.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import cn.youmi.framework.R;
import cn.youmi.framework.fragment.BaseFragment;

/**
 * @author tangxiyong
 * @version 2015-5-7 下午4:02:36
 */
public class FragmentContainerUnSwipeActivity extends BaseSwipeBackActivity {
	public static final String KEY_FRAGMENT_CLASS = "key.fragmentClass";
	
	private BaseFragment fragment;
	
	protected BaseFragment getFragment(){
		return fragment;
	}
	
	@Override
	protected void onCreate(Bundle bundle) {
		
		@SuppressWarnings("unchecked")
		Class<? extends BaseFragment> fragmentClass = (Class<? extends BaseFragment>) getIntent().getSerializableExtra(KEY_FRAGMENT_CLASS);
		try {
			fragment = fragmentClass.newInstance();
			fragment.willSetContentView(this);
			ActivityManager.getInstance().willSetContentView(this);
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		super.onCreate(bundle);
		
		setSwipeBackEnable(false);//
		fragment.onActivityCreated(this);
		ActivityManager.getInstance().onActivityCreated(this);
		setContentView(R.layout.activity_fragment_container);
		fragment.didSetContentView(this);
		ActivityManager.getInstance().didSetContentView(this);
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commitAllowingStateLoss();
	}
	
	/** action voerflow actionbar是的三点*/
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
	
	@Override
	protected void onPause() {
		super.onPause();
		fragment.onPaused(this);
		ActivityManager.getInstance().onPaused(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		fragment.onResumed(this);
		ActivityManager.getInstance().onResumed(this);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if(!fragment.onOptionsItemSelected(item)){
				slideToFinishActivity();
			}
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if( getFragment().onClickBack(this)){
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
