package cn.youmi.pay.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.message.BasicHeader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.youmi.account.event.UserEvent;
import cn.youmi.account.manager.UserManager;
import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.http.CookieDao;
import cn.youmi.framework.main.BaseApplication;
import cn.youmi.framework.main.ConstantProvider;
import cn.youmi.framework.util.ActivityUtils;
import cn.youmi.framework.util.AndroidSDK;
import cn.youmi.pay.R;

@SuppressWarnings("deprecation")
@SuppressLint({"ValidFragment", "SetJavaScriptEnabled"})
public class WebFragment extends BaseFragment {

    private WebView mWebView;

    private ProgressBar mProgressBar;

    private boolean mIsWebViewAvailable;

    private String mUrl = "";


    public WebFragment() {
        super();
    }

    public WebFragment(String url) {
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem refresh = menu.add("refresh");
        refresh.setTitle("刷新");
        refresh.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        refresh.setIcon(R.drawable.web_refresh);
        refresh.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        refresh.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                getWebView().reload();
                return true;
            }
        });
        MenuItem share = menu.add("分享");
        share.setTitle("分享");
        share.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        share.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (!buildShareCopyContent().contains("file:///android_asset")) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT,
                            buildShareCopyContent());
                    if (ActivityUtils.isIntentSafe(getActivity(), shareIntent)) {
                        startActivity(shareIntent);
                    }
                } else {
                    Toast.makeText(getActivity(), "链接不支持", Toast.LENGTH_SHORT)
                            .show();
                }

                return true;
            }
        });
        MenuItem copy = menu.add("复制");
        copy.setTitle("复制");
        copy.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        copy.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (!buildShareCopyContent().contains("file:///android_asset")) {
                    android.content.ClipboardManager cm = (android.content.ClipboardManager) getActivity()
                            .getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setPrimaryClip(ClipData.newPlainText("yys",
                            buildShareCopyContent()));
                    Toast.makeText(getActivity(), "复制成功", Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getActivity(), "链接不支持", Toast.LENGTH_SHORT)
                            .show();
                }

                // else {<AndroidSDK.isHONEYCOMB()
                // android.text.ClipboardManager cm =
                // (android.text.ClipboardManager)
                // getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                // cm.setText(buildShareCopyContent());
                // }

                return true;
            }
        });
        MenuItem other_browser = menu.add("在浏览器中打开");
        other_browser.setTitle("在浏览器中打开");
        other_browser.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        other_browser.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (!TextUtils.isEmpty(mUrl)
                        && !mUrl.contains("file:///android_asset")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse(mUrl));
                    getActivity().startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "链接不支持", Toast.LENGTH_SHORT)
                            .show();
                }

                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private String buildShareCopyContent() {
        String title = mWebView.getTitle();
        String url = mWebView.getUrl();
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(url)) {
            return title + " " + url;
        } else {
            return mUrl;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payweb_layout, container, false);
        this.setHasOptionsMenu(true);
        if (mWebView != null) {
            mWebView.destroy();
        }
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_action_bar_return);
        mWebView = (WebView) view.findViewById(R.id.webView);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        mWebView.setOnKeyListener(new BackKeyListener());
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
        try {
            String s = CookieDao.getInstance(getActivity()).getAsString();
            String[] array = new String[10];
            array = s.split(";");
            for (String a : array) {
                cookieManager.setCookie(urlhost, a);
            }
            CookieSyncManager.getInstance().sync();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        loadUrl(mUrl);
        return view;
    }

    private class BackKeyListener implements View.OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
            return false;
        }

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

            CookieManager cookieManager = CookieManager.getInstance();
            String cookieStr = cookieManager.getCookie(url);
            if (!TextUtils.isEmpty(cookieStr) && cookieStr.contains("username=")) {

                CookieSpec cookiespec = new BrowserCompatSpec();
                CookieOrigin origin = new CookieOrigin(ConstantProvider.getInstance().setCookieOrigin(), 80, "/", false);
                List<Cookie> cookies = new ArrayList<Cookie>();
                Header header = new BasicHeader("Set-Cookie", cookieStr);
                try {
                    cookies = cookiespec.parse(header, origin);
                    if (!cookies.isEmpty()) {
                        cookiedao.saveCookies((ArrayList<Cookie>) cookies);
                    }
                } catch (Exception e) {
                }
                UserManager.getInstance().getUserInfoSave(UserEvent.HOME_WEBVIEW);
            }
        }
    }

    private static CookieDao cookiedao = CookieDao.getInstance(BaseApplication.getApplication());

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
