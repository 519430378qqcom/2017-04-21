package com.umiwi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import cn.youmi.framework.activity.BaseActivity;
import cn.youmi.framework.util.ToastU;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.fragment.course.BigZTListFragment;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.fragment.course.JPZTDetailFragment;

/**
 * 课程详情主页
 * 
 * @author tangxiyong 2013-11-15下午6:06:40
 * @version tangxiyong 2014-06-17 此类名禁止修改
 * 
 */
public class UmiwiDetailActivity extends BaseActivity {
	public static final String KEY_DETAIL_URL = "default_detailurl";
	public static final String KEY_FRAGMENT_CLASSES = "classes";
	public static final String KEY_PUSH_CLASSES = "pushclasses";
	/** 任何页面：跳转到专辑详情页*/
	public static final int CLASS_COURSE_DETAIL = 60;
	/** 任何页面：跳转到专题详情页*/
	public static final int CLASS_ZHUANTI_DETAIL = 62;
	/** 任何页面：跳转到大专题详情页*/
	public static final int CLASS_BIG_ZHUANTI_DETAIL = 65;

	/** 课程详情页 传递过来的链接 */
//	private String schemeUrl;
	private String defaultDetailUrl;

	/** 传过来的是什么 在UmiwiConstants.CLASS_ 定义页面 */
	private int classes;
	private int pushclasses;// 百度推送来的

	private boolean isScheme;
	//百度及自己类型
	private boolean isSchemeTypeAlbum;
	private boolean isSchemeTypeZhuanti;
	private boolean isSchemeTypeApp;
	// 豌豆荚类型
	private boolean isSchemePathAlbum;
	private boolean isSchemePathZhuanti;
	private boolean isSchemePathApp;
	/**专辑*/
	private final String albumUrl = "http://i.v.youmi.cn/ClientApi/getAlbumDetail?id=%s";
	/**专题*/
	private final String zhuantiUrl = "http://i.v.youmi.cn/ClientApi/zhuantiDetail?id=%s";
	
	/** 第三方接入 或百度推送，判断返回到首页 */
	private boolean returnHome;
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_FRAGMENT_CLASSES, classes);
		outState.putInt(KEY_PUSH_CLASSES, pushclasses);
		outState.putString(KEY_DETAIL_URL, defaultDetailUrl);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent i = this.getIntent();
		classes = i.getIntExtra(KEY_FRAGMENT_CLASSES, 0);
		defaultDetailUrl = i.getStringExtra(KEY_DETAIL_URL); // 直接传链接或百度推送
		pushclasses = i.getIntExtra(KEY_PUSH_CLASSES, 0);
		if (savedInstanceState != null) {
			classes = savedInstanceState.getInt(KEY_FRAGMENT_CLASSES);
			pushclasses = savedInstanceState.getInt(KEY_PUSH_CLASSES);
			defaultDetailUrl = savedInstanceState.getString(KEY_DETAIL_URL);
		} else {
			classes = i.getIntExtra(KEY_FRAGMENT_CLASSES, 0);
			defaultDetailUrl = i.getStringExtra(KEY_DETAIL_URL); // 直接传链接或百度推送
			pushclasses = i.getIntExtra(KEY_PUSH_CLASSES, 0);
		}
		
		
		String scheme = i.getScheme(); // 第三方通过协议接入
		isScheme = null != scheme && (scheme.contains("youmi") || scheme.contains("umiwischeme"));
		
		if (isScheme) {
			returnHome = true;
			//百度及自己类型
			isSchemeTypeAlbum = "albumurl".equals(i.getData().getQueryParameter("type"));
			isSchemeTypeZhuanti = "zhuantiurl".equals(i.getData().getQueryParameter("type"));
			isSchemeTypeApp = "app".equals(i.getData().getQueryParameter("type"));
			// 豌豆荚类型
			isSchemePathAlbum = !TextUtils.isEmpty(i.getData().getPath()) && "/albumurl".equals(i.getData().getPath());
			isSchemePathZhuanti = !TextUtils.isEmpty(i.getData().getPath()) && "/zhuantiurl".equals(i.getData().getPath());
			isSchemePathApp = !TextUtils.isEmpty(i.getData().getPath()) && "/app".equals(i.getData().getPath());
			
//			AppLogger.e("scheme", "i.getPath()=============="+i.getData().getPath());//
//			AppLogger.e("scheme", "i.getQuery()=============="+i.getData().getQuery());//
//			AppLogger.e("scheme", "id=============="+i.getData().getQueryParameter("id"));
			//注意顺序
			if (isSchemePathAlbum) {
				classes = CLASS_COURSE_DETAIL;
				defaultDetailUrl = String.format(albumUrl, i.getData().getQueryParameter("id"));
			} else if (isSchemePathZhuanti) {
				classes = CLASS_ZHUANTI_DETAIL;
				defaultDetailUrl = String.format(zhuantiUrl, i.getData().getQueryParameter("id"));
			} else if (isSchemePathApp) {
				Intent intent = new Intent(UmiwiDetailActivity.this, HomeMainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				UmiwiDetailActivity.this.finish();
			} else if (isSchemeTypeAlbum) {// 专辑
				classes = CLASS_COURSE_DETAIL;
				defaultDetailUrl = i.getData().getQueryParameter("url");
			} else if (isSchemeTypeZhuanti) {// 专题
				classes = CLASS_ZHUANTI_DETAIL;
				defaultDetailUrl = i.getData().getQueryParameter("url");
			} else if (isSchemeTypeApp) {// 打开应用
				Intent intent = new Intent(UmiwiDetailActivity.this, HomeMainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				UmiwiDetailActivity.this.finish();
			} else {
				ToastU.showShort(UmiwiDetailActivity.this, "版本过低，请升级到最新版本");
				UmiwiDetailActivity.this.finish();
			}
		}
		super.onCreate(savedInstanceState);
		
		if (9 == pushclasses) {
			returnHome = true;
		}

		Intent intent = new Intent(this, UmiwiContainerActivity.class);
		switch (classes) {
		case CLASS_COURSE_DETAIL:// 专辑(课程)详情页
			intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
			intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, defaultDetailUrl);
			break;
		case CLASS_ZHUANTI_DETAIL:// 专题详情页
			intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, JPZTDetailFragment.class);
			intent.putExtra(JPZTDetailFragment.KEY_URL, defaultDetailUrl);
			break;
		case CLASS_BIG_ZHUANTI_DETAIL:// 大专题详情页
			intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, BigZTListFragment.class);
			intent.putExtra(BigZTListFragment.KEY_URL, defaultDetailUrl);
			break;
		}
		
		if (returnHome) {
			Intent intentHome = new Intent(UmiwiDetailActivity.this, HomeMainActivity.class);
			intentHome.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
		}
		startActivity(intent);
		UmiwiDetailActivity.this.finish();
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
	
}
