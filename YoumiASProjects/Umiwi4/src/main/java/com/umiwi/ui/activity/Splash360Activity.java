package com.umiwi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.umiwi.ui.R;

import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.util.PreferenceUtils;

/**
 * @author tangxiyong
 * @version 2014年6月23日 上午10:26:41
 */
public class Splash360Activity extends AppCompatActivity {

	Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.splash_layout);
		ImageView splash_image = (ImageView) findViewById(R.id.splash_image);
		ImageLoader mImageLoader = new ImageLoader(this);
		mImageLoader.loadImage(R.drawable.guide_splash, splash_image);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// 获得用户是否是第一次启动
				boolean isFirstStart = PreferenceUtils.getPrefBoolean(Splash360Activity.this, "isNewStart360", false);
				if (isFirstStart) {
					startActivity(new Intent(Splash360Activity.this, HomeMainActivity.class));
					Splash360Activity.this.finish();
				} else {
					startActivity(new Intent(Splash360Activity.this, SplashActivity.class));
					PreferenceUtils.setPrefBoolean(Splash360Activity.this, "isNewStart360", true);
					createShortCut();
					Splash360Activity.this.finish();
				}

			}
		}, 3000);
	}

	public void createShortCut(){
		Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		//不允许重复创建
		shortcutintent.putExtra("duplicate", false);
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
		Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_launcher);
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(getApplicationContext() , Splash360Activity.class));
		sendBroadcast(shortcutintent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ( keyCode == KeyEvent.KEYCODE_MENU ) {//http://stackoverflow.com/questions/19275447/pressing-menu-button-causes-crash-in-activity-with-no-actionbar/19320065#19320065
	       //https://code.google.com/p/android/issues/detail?id=61394
			// do nothing
	        return true;
	    }
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			PreferenceUtils.setPrefBoolean(this, "isCanShowGift", true);
			startActivity(new Intent(Splash360Activity.this, HomeMainActivity.class));
			Splash360Activity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
