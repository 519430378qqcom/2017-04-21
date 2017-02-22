package cn.youmi.framework.fragment;

import java.lang.reflect.Field;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * @author tangxiyong
 * @version 2015-5-7 下午3:41:23
 */
public abstract class AbstractFragment extends Fragment {

	protected boolean isVisible;

	protected String fragmentName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentName = this.getClass().getName();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInVisible();
		}
	}

	protected void onVisible() {
		onLazyLoad();
	}

	protected void onInVisible() {
	}

	/**
	 * 延迟加载 如果需要Fragment在前台时才加载数据时
	 */
	protected abstract void onLazyLoad();


	// a hack refer to
	// http://stackoverflow.com/questions/15207305/getting-the-error-java-lang-illegalstateexception-activity-has-been-destroyed
	private static final Field sChildFragmentManagerField;

	static {
		Field f = null;
		try {
			f = Fragment.class.getDeclaredField("mChildFragmentManager");
			f.setAccessible(true);
		} catch (NoSuchFieldException e) {
		}
		sChildFragmentManagerField = f;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (sChildFragmentManagerField != null) {
			try {
				sChildFragmentManagerField.set(this, null);
			} catch (Exception e) {
			}
		}
	}
}
