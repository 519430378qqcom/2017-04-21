package cn.youmi.framework.http.okhttp;

/**
 * Created by youmi on 15/10/9.
 */

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import cn.youmi.framework.http.CookieDao;
import cn.youmi.framework.main.BaseApplication;

/**
 * This interceptor put all the Cookies in Preferences in the Request.
 * Your implementation on how to get the Preferences MAY VARY.
 * <p/>
 * Created by tsuharesu on 4/1/15.
 */
public class AddCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("Cookie", CookieDao.getInstance(BaseApplication.getApplication()).getAsString());
        Log.v("OkHttp", "Adding Header: " + CookieDao.getInstance(BaseApplication.getApplication()).getAsString()); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp

        return chain.proceed(builder.build());
    }
}
