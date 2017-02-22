package cn.youmi.framework.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboardUtils {
	
	public static void hideKeyboard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			View focusView = activity.getCurrentFocus();
			if (focusView != null) {
				imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
			}
		}
	}
//	public static void hideKeyboard(Activity activity) {
//		((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
//		.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
//	}
	
	public static void showKeyboard(Activity activity) {
		((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
			.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
	}
	
	public static void showKeyboard(EditText editText) {
		editText.setFocusable(true);
		editText.setFocusableInTouchMode(true);
		editText.requestFocus();
		InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
		inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}
	
	public static void hideKeyboard(EditText editText) {
		InputMethodManager inputManager= (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0); 
	}
	

}
