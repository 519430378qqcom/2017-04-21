package cn.youmi.framework.fragment;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import cn.youmi.framework.activity.ActivityEventListener;
import cn.youmi.framework.util.ToastU;

/**
 * 所有fragment 都继承此BaseFragment
 * 包括com.umiwi.ui.main.BaseFragment，com.umiwi.ui.main.BaseConstantFragment
 * 
 * @author tangxiyong
 * @version 2014年10月30日 下午5:04:56
 * 
 */
@SuppressLint("ValidFragment")
public class BaseFragment extends AbstractFragment implements ActivityEventListener {

	protected Handler mHandler = new Handler();


	
	protected Toolbar mActionBarToolbar;
	
	protected void setSupportActionBar(Toolbar mToolbar) {
		((AppCompatActivity) this.getActivity()).setSupportActionBar(mToolbar);
	}
	/**
	 * if setSupportActionBar must use this set title
	 * @author tangxiyong
	 * @version 2015-5-14 上午11:13:36 
	 * @param mToolbar
	 * @param title
	 */
	protected void setSupportActionBarAndToolbarTitle(final Toolbar mToolbar, final String title) {
		((AppCompatActivity) this.getActivity()).setSupportActionBar(mToolbar);
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				mToolbar.setTitle(title + "");
			}
		});
	}

	@Override
	public void onStop() {
		super.onStop();
		ToastU.hideToast();
	}
	
	protected void showMsg(String msg) {
		ToastU.showShort(getActivity(), msg);
	}

	protected void showMsg(String msg, int durationTime) {
		ToastU.show(getActivity(), msg, durationTime);
	}

	private AppCompatActivity mActivity;

	public AppCompatActivity getActionActivity() {
		return mActivity;
	}

	@Override
	public void onActivityCreated(AppCompatActivity a) {
		mActivity = a;
	}

	@Override
	public void willSetContentView(AppCompatActivity a) {
	}

	@Override
	public void didSetContentView(AppCompatActivity a) {
	}

	@Override
	public void onPaused(AppCompatActivity a) {
	}

	@Override
	public void onResumed(AppCompatActivity a) {
	}

	@Override
	public void onActivityDestroyed(AppCompatActivity a) {
		mActivity = null;
	}

	@Override
	public boolean onClickBack(AppCompatActivity a) {
		return false;
	}

	@Override
	protected void onLazyLoad() {

	}
}
