package cn.youmi.framework.activity;

import android.support.v7.app.AppCompatActivity;

public interface ActivityEventListener {
	void onActivityCreated(AppCompatActivity a);
	void willSetContentView(AppCompatActivity a);
	void didSetContentView(AppCompatActivity a);
	
	void onPaused(AppCompatActivity a);
	void onResumed(AppCompatActivity a);
	
	void onActivityDestroyed(AppCompatActivity a);
	
	boolean onClickBack(AppCompatActivity a);
}
