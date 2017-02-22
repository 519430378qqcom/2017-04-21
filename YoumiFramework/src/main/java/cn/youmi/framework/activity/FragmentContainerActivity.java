package cn.youmi.framework.activity;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

import cn.youmi.framework.R;
import cn.youmi.framework.fragment.BaseFragment;

public class FragmentContainerActivity extends BaseSwipeBackActivity {

    private NavigationController navigationController;

    public NavigationController getNavigationController() {
        return navigationController;
    }

    public static final String KEY_FRAGMENT_CLASS = "key.fragmentClass";

    private static final String KEY_SAVE_BUNDLE = "key.saveBundle";

    private BaseFragment fragment;

    private Bundle bundle;

    protected BaseFragment getFragment() {
        return fragment;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(KEY_SAVE_BUNDLE, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            bundle = savedInstanceState;
        } else {
            bundle = this.getIntent().getExtras();
        }

        try {
            @SuppressWarnings("unchecked")
            Class<? extends BaseFragment> fragmentClass = (Class<? extends BaseFragment>) bundle.getSerializable(KEY_FRAGMENT_CLASS);
            try {
                fragment = fragmentClass.newInstance();
                fragment.setArguments(bundle);
                fragment.willSetContentView(this);
                ActivityManager.getInstance().willSetContentView(this);

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        if (null == fragment) {
            this.finish();
            return;
        }
        navigationController = new NavigationController(this);
//		FragmentContainerActivity fca = (FragmentContainerActivity) FragmentContainerActivity.this;
        fragment.onActivityCreated(this);
        ActivityManager.getInstance().onActivityCreated(this);
        setContentView(R.layout.activity_fragment_container);
        fragment.didSetContentView(this);

        ActivityManager.getInstance().didSetContentView(this);
        this.getNavigationController().setBottomFragment(fragment);
//		this.getNavigationController().popToBottom(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        fragment.onPaused(this);
        ActivityManager.getInstance().onPaused(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fragment.onResumed(this);
        ActivityManager.getInstance().onResumed(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//			if(!fragment.onOptionsItemSelected(item)){
//				slideToFinishActivity();
//			}
                backClickKeyDown();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if(getFragment().onClickBack(this)){
//				return true;
//			}
            backClickKeyDown();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void backClickKeyDown() {
        if (getNavigationController().getStack().size() > 2) {
            getNavigationController().popFragment();
        } else if (getNavigationController().getStack().size() == 2) {
            getNavigationController().popFragment(this);
        } else {
            if (!this.isFinishing()) {
                slideToFinishActivity();
            }
        }
    }

}
