package cn.youmi.framework.activity;

import java.util.Stack;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import cn.youmi.framework.R;
import cn.youmi.framework.debug.LogUtils;

public class NavigationController {

	private Stack<Fragment> stack = new Stack<Fragment>();
	private Fragment currentFragment = new Fragment();

	private AppCompatActivity activity;

	public NavigationController(AppCompatActivity mainActivity) {
		activity = mainActivity;
	}

	public Fragment getCurrentFragment() {
		return currentFragment;
	}

	public Stack<Fragment> getStack() {
		return stack;
	}

	public void pushFragment(Fragment f) {
		this.pushFragment(f, true);
	}

	public void pushFragment(Fragment f, boolean animated) {
		if (currentFragment != null) {
			stack.push(currentFragment);
		}
		currentFragment = f;
		android.support.v4.app.FragmentTransaction transaction = activity
				.getSupportFragmentManager().beginTransaction();
		if (animated) {
			transaction.setCustomAnimations(R.anim.popup_show_rtl,
					R.anim.popup_hide_rtl);
		}
		transaction.addToBackStack(f.getClass().getName().toString());
		transaction.replace(R.id.fragment_container, currentFragment)
				.commitAllowingStateLoss();
	}

	public Fragment popFragment() {
		return popFragment(activity);
	}

	public Fragment popFragment(AppCompatActivity a){
		if (!stack.isEmpty()) {
			Fragment originalRoot = currentFragment;
			Fragment last = stack.pop();
			currentFragment = last;
			a.getSupportFragmentManager()
					.beginTransaction()
					.setCustomAnimations(R.anim.popup_show_ltr,
							R.anim.popup_hide_ltr)
					.replace(R.id.fragment_container, last)
					.commitAllowingStateLoss();
			return originalRoot;
		}
		return null;
	}
	
	public void clearStack() {
		stack.clear();
	}

	public void setBottomFragment(Fragment f) {
		if (stack.size() < 2) {
			LogUtils.e("navigation", "===setBottomFragment");
			FragmentManager fragmentManager = activity.getSupportFragmentManager();
			fragmentManager.popBackStack();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.addToBackStack(f.getClass().getName().toString())
						.replace(R.id.fragment_container, f)
						.commit();
		}
		if (!stack.isEmpty()) {
			stack.remove(0);
		}
		stack.add(0, f);
		currentFragment = f;
	}

	public void popToBottom(boolean animated) {
		if(stack.size() < 2){
			return;
		}
		Fragment f = stack.get(0);
		stack.clear();
		currentFragment = f;
		LogUtils.e("navigation", "===popToBottom");
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		fragmentManager.popBackStack();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (animated) {
			transaction.setCustomAnimations(R.anim.popup_show_ltr,
					R.anim.popup_hide_ltr);
		}
		transaction.replace(R.id.fragment_container, currentFragment)
				.commitAllowingStateLoss();
	}
}
