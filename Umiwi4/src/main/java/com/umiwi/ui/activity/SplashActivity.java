package com.umiwi.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.AdvertisementBean;
import com.umiwi.ui.fragment.splash.SplashFragment;
import com.umiwi.ui.fragment.splash.SplashNewHorizontalFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.CommonHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cn.youmi.account.event.UserEvent;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.AndroidSDK;
import cn.youmi.framework.util.PreferenceUtils;


/**
 * @author tangxiyong
 * @version 2014年6月23日 上午10:26:41
 */
public class SplashActivity extends AppCompatActivity {

	public static String photoPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/advertise.jpg";
	 public static boolean isKeyBack = false;
	/**
	 * 广告信息
	 */
	public static AdvertisementBean advertisementBean1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_frame_layout);

//		overridePendingTransition(R.anim.right_in,R.anim.left_out);
		
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		// 获得用户是否是第一次启动
		boolean isFirstStart = PreferenceUtils.getPrefBoolean(this, "isNewStart20150623", false);//注意两次的值
		if (isFirstStart) {// 4.8.0 第二次启动
			fragmentTransaction.replace(R.id.frame_layout, new SplashFragment(), SplashFragment.class.getName());

//			Intent intent = new Intent(SplashActivity.this, ColumnDetailsSplashActivity.class);
//			startActivity(intent);
//			this.finish();
		} else {// 4.8.0 新用户第一次启动
			final File file = this.getDatabasePath("user.db");//迁移数据
			file.delete();
			YoumiRoomUserManager.getInstance().getUserInfoSave(UserEvent.USER_START);

			SplashNewHorizontalFragment verticalFragment = new SplashNewHorizontalFragment();
//			verticalFragment.initData(false);
			
			fragmentTransaction.replace(R.id.frame_layout, verticalFragment, SplashNewHorizontalFragment.class.getName());
			createShortCut();//if 360
			CommonHelper.delAllFile(Environment.getDataDirectory()
					+ File.separator + "data" + File.separator
					+ getPackageName().toString() + File.separator
					+ "shared_prefs");
			PreferenceUtils.setPrefBoolean(this, "isNewStart20150623", true);
//			YoumiRoomUserManager.getInstance().logout("up.delete");
			
		}
		fragmentTransaction.commit();
		requestAdvertise();
	}

	/**
	 * 请求广告信息
	 */
	private  void requestAdvertise(){
		GetRequest<AdvertisementBean> request = new GetRequest<AdvertisementBean>(UmiwiAPI.UMIWI_ADVERTISE, GsonParser.class, AdvertisementBean.class, new AbstractRequest.Listener<AdvertisementBean>() {
			@Override
			public void onResult(AbstractRequest<AdvertisementBean> request, AdvertisementBean advertisementBean) {
				advertisementBean1 = advertisementBean;
				final ArrayList<AdvertisementBean.RAdvertBean> advertisementBeanR = advertisementBean.getR();
				final String image = advertisementBeanR.get(0).getImage();
				new Thread(){
					@Override
					public void run() {
						URL url = null;
						try {
							url = new URL(image);
							HttpURLConnection conn = (HttpURLConnection)url.openConnection();
							conn.setConnectTimeout(5000);
							conn.setRequestMethod("GET");
							if(conn.getResponseCode() == 200){
								InputStream inputStream = conn.getInputStream();
								Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
								bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(photoPath));
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}.start();
			}

			@Override
			public void onError(AbstractRequest<AdvertisementBean> requet, int statusCode, String body) {

			}
		});
		request.go();
	}
	@Override
	protected void onStart() {
		super.onStart();
		if (AndroidSDK.isKK()) {
			View decorView = this.getWindow().getDecorView();
			decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_FULLSCREEN
					| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	/*
	 * 返回
	 */
	public void doBack(View view) {
		onBackPressed();
	}
	
	public void createShortCut(){   
        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");                   
        //不允许重复创建                   
        shortcutintent.putExtra("duplicate", false);                   
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));   
        Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_launcher);   
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);   
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(getApplicationContext() , SplashActivity.class));                   
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
			isKeyBack = true;
			PreferenceUtils.setPrefBoolean(this, "isCanShowGift", true);
			startActivity(new Intent(SplashActivity.this, HomeMainActivity.class));
//			startActivity(new Intent(SplashActivity.this, ColumnDetailsSplashActivity.class));
			SplashActivity.this.finish();

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
