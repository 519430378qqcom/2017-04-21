package com.umiwi.ui;

import java.util.Stack;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class NavigationController {

	private Stack<Fragment> stack = new Stack<Fragment>();
	private Fragment currentFragment = new Fragment();

	private FragmentActivity activity;

	public NavigationController(FragmentActivity mainActivity) {
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
		transaction.addToBackStack(f.toString());
		transaction.replace(R.id.frame_layout, currentFragment)
				.commitAllowingStateLoss();
	}

	public Fragment popFragment() {
		return popFragment(activity);
	}

	public Fragment popFragment(FragmentActivity a){
		if (!stack.isEmpty()) {
			Fragment originalRoot = currentFragment;
			Fragment last = stack.pop();
			currentFragment = last;
			a.getSupportFragmentManager()
					.beginTransaction()
					.setCustomAnimations(R.anim.popup_show_ltr,
							R.anim.popup_hide_ltr)
					.replace(R.id.frame_layout, last)
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
			activity.getSupportFragmentManager().beginTransaction()
					.replace(R.id.frame_layout, f)
					.commitAllowingStateLoss();
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
		FragmentTransaction transaction = activity.getSupportFragmentManager()
				.beginTransaction();
		if (animated) {
			transaction.setCustomAnimations(R.anim.popup_show_ltr,
					R.anim.popup_hide_ltr);
		}
		transaction.replace(R.id.frame_layout, currentFragment)
				.commitAllowingStateLoss();
	}
}
