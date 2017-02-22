package cn.youmi.framework.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.support.v7.app.AppCompatActivity;
import cn.youmi.framework.util.SingletonFactory;

public class ActivityManager implements ActivityEventListener {

	public static ActivityManager getInstance() {
		return SingletonFactory.getInstance(ActivityManager.class);
	}

	ArrayList<WeakReference<ActivityEventListener>> activityEventListeners = new ArrayList<WeakReference<ActivityEventListener>>();

	public void regitenerActivityEventListener(ActivityEventListener l) {
		activityEventListeners.add(new WeakReference<ActivityEventListener>(l));
	}

	@Override
	public void onActivityCreated(AppCompatActivity a) {
		for (WeakReference<ActivityEventListener> l : activityEventListeners) {
			if (l.get() != null) {
				l.get().onActivityCreated(a);
			}
		}
	}

	@Override
	public void willSetContentView(AppCompatActivity a) {
		for (WeakReference<ActivityEventListener> l : activityEventListeners) {
			if (l.get() != null) {
				l.get().willSetContentView(a);
			}
		}
	}

	@Override
	public void didSetContentView(AppCompatActivity a) {
		for (WeakReference<ActivityEventListener> l : activityEventListeners) {
			if (l.get() != null) {
				l.get().didSetContentView(a);
			}
		}
	}

	@Override
	public void onPaused(AppCompatActivity a) {
		for (WeakReference<ActivityEventListener> l : activityEventListeners) {
			if (l.get() != null) {
				l.get().onPaused(a);
			}
		}
	}

	@Override
	public void onResumed(AppCompatActivity a) {
		for (WeakReference<ActivityEventListener> l : activityEventListeners) {
			if (l.get() != null) {
				l.get().onResumed(a);
			}
		}
	}

	@Override
	public void onActivityDestroyed(AppCompatActivity a) {
		for (WeakReference<ActivityEventListener> l : activityEventListeners) {
			if (l.get() != null) {
				l.get().onActivityDestroyed(a);
			}
		}
	}

	@Override
	public boolean onClickBack(AppCompatActivity a) {
		return false;
	}

}
