package cn.youmi.framework.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import cn.youmi.framework.R;
import cn.youmi.framework.util.ToastU;

/**
 * 基本activity 其他base的父类或用于MainActivity
 * 
 * @author tangxiong
 * @version 2014年10月28日 上午10:56:35
 */
public class BaseActivity extends AppCompatActivity {

	protected Handler mHandler = new Handler();

	protected int theme() {
		return 0;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setTheme(theme());
		super.onCreate(savedInstanceState);
	}
	
	protected Toolbar mActionBarToolbar;
	
	protected Toolbar getActionBarToolbar(int toolbar_actionbar) {
		if (mActionBarToolbar == null) {
			mActionBarToolbar = (Toolbar) findViewById(toolbar_actionbar);
			if (mActionBarToolbar != null) {
				setSupportActionBar(mActionBarToolbar);
			}
		}
		return mActionBarToolbar;
	}
	
	protected void setAcitonbarTitle(final String title) {
		mActionBarToolbar.setNavigationIcon(R.drawable.ic_action_bar_return);
		mHandler.post(new Runnable() {
            @Override
            public void run() {
            	mActionBarToolbar.setTitle(title);
            }
        });
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		ToastU.hideToast();
	}

	protected void showMsg(String msg) {
		ToastU.showShort(this, msg);
	}

	protected void showMsg(String msg, int durationTime) {
		ToastU.show(this, msg, durationTime);
	}

}
