package cn.youmi.pay.ui;

import java.net.URL;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.http.CookieDao;
import cn.youmi.framework.util.AndroidSDK;
import cn.youmi.pay.R;
import cn.youmi.pay.event.PayResultEvent;
import cn.youmi.pay.manager.PayResultManager;

@SuppressWarnings("deprecation")
@SuppressLint({ "ValidFragment", "SetJavaScriptEnabled" })
public class PayWebFragment extends BaseFragment {

	private WebView mWebView;

	private ProgressBar mProgressBar;

	private boolean mIsWebViewAvailable;

	private String mUrl = "";

	private int classesBuyFrom = 0;


	public PayWebFragment() {
		super();
	}

	public PayWebFragment(String url) {
		super();
		mUrl = url;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("mUrl", mUrl);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
		this.setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null) {
			mUrl = savedInstanceState.getString("mUrl");
		}

	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_payweb_layout, container, false);
		if (mWebView != null) {
			mWebView.destroy();
		}
		
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		mActionBarToolbar.setNavigationIcon(R.drawable.ic_action_bar_return);
		setSupportActionBar(mActionBarToolbar);
		mWebView = (WebView) view.findViewById(R.id.webView);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);

		mWebView.setWebViewClient(new InnerWebViewClient());
		mWebView.setWebChromeClient(new InnerWebChromeClient());
		mIsWebViewAvailable = true;
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setBuiltInZoomControls(true);
		if (AndroidSDK.isICS()) {
			settings.setDisplayZoomControls(false);
		}

		CookieSyncManager.createInstance(getActivity().getApplicationContext());
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.acceptCookie();

		String urlhost = mUrl;
		try {
			URL myurl = new URL(mUrl);
			urlhost = myurl.getHost();
		} catch (Exception e) {
		}

		// String cookiestr_web = cookieManager.getCookie(urlhost);
//		String s = CookieModelManager.getInstance().getAsString();
		String s = CookieDao.getInstance(getActivity()).getAsString();
		String[] array = new String[10];
		array = s.split(";");
		for (String a : array) {
			cookieManager.setCookie(urlhost, a);
		}
		CookieSyncManager.getInstance().sync();

		loadUrl(mUrl);
		return view;
	}

	public static boolean isIntentSafe(Activity activity, Intent intent) {
		PackageManager packageManager = activity.getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(
				intent, 0);
		return activities.size() > 0;
	}

	public void loadUrl(String url) {

		if (mIsWebViewAvailable) {
			getWebView().loadUrl(mUrl = url);
		} else {
			Log.w("ImprovedWebViewFragment",
					"WebView cannot be found. Check the view and fragment have been loaded.");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		mWebView.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mWebView.onResume();
	}

	@Override
	public void onDestroyView() {
		mIsWebViewAvailable = false;
		super.onDestroyView();
		CookieSyncManager csm = CookieSyncManager.createInstance(getActivity()
				.getApplicationContext());
		CookieManager cm = CookieManager.getInstance();
		cm.removeAllCookie();
		csm.sync();
	}

	@Override
	public void onDestroy() {
		if (mWebView != null) {
			mWebView.destroy();
			mWebView = null;
		}
		super.onDestroy();
	}

	public WebView getWebView() {
		return mIsWebViewAvailable ? mWebView : null;
	}

	private class InnerWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			CookieSyncManager.createInstance(getActivity()
					.getApplicationContext());
			CookieManager cookieManager = CookieManager.getInstance();
			cookieManager.setAcceptCookie(true);
			cookieManager.acceptCookie();

			String urlhost = url;
			try {
				URL myurl = new URL(url);
				urlhost = myurl.getHost();
			} catch (Exception e) {
			}

			// String cookiestr_web = cookieManager.getCookie(urlhost);
//			String s = CookieModelManager.getInstance().getAsString();
			String s = CookieDao.getInstance(getActivity()).getAsString();
			String[] array = new String[10];
			array = s.split(";");
			for (String a : array) {
				cookieManager.setCookie(urlhost, a);
			}
			CookieSyncManager.getInstance().sync();

			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			if (getActivity() == null) {
				return;
			}
			if (!TextUtils.isEmpty(view.getTitle())) {
				mActionBarToolbar.setTitle(view.getTitle());
			}
			if (url.contains("deposit/success") || url.contains("/ipad/vipend")) {
				PayResultManager.getInstance().setPayResult(PayResultEvent.WEB_SUCC);
				Intent intent = getActivity().getIntent();
				getActivity().setResult(115, intent);
				getActivity().finish();
			}
		}
	}

	private class InnerWebChromeClient extends WebChromeClient {

		@Override
		public void onReceivedTitle(WebView view, String sTitle) {
			super.onReceivedTitle(view, sTitle);
			if (sTitle != null && sTitle.length() > 0) {
				if (getActivity() == null) {
					return;
				}
				if (!TextUtils.isEmpty(view.getTitle())) {
					mActionBarToolbar.setTitle(view.getTitle());
				}
			}
		}

		@Override
		public void onReceivedIcon(WebView view, Bitmap icon) {
			super.onReceivedIcon(view, icon);
			if (getActivity() == null) {
				return;
			}
		}

		public void onProgressChanged(WebView view, int progress) {
			if (getActivity() == null) {
				return;
			}
			if (!mProgressBar.isShown()) {
				mProgressBar.setVisibility(View.VISIBLE);
			}
			mProgressBar.setProgress(progress);
			if (progress == 100) {
				mProgressBar.setVisibility(View.INVISIBLE);
			}
		}
	}

}
