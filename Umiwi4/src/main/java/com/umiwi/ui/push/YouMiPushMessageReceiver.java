package com.umiwi.ui.push;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.List;

import cn.youmi.framework.util.SharePreferenceUtil;

/**
 * Created by Administrator on 2017/4/7 0007.
 */

public class YouMiPushMessageReceiver extends PushMessageReceiver {

    @Override
    public void onBind(Context context, int i, String s, String s1, String s2, String s3) {
        if(i == 0) {
            Utils.setBind(context,true);
            SharePreferenceUtil util = UmiwiApplication.getInstance().getSpUtil();
            if(TextUtils.isEmpty(util.getAppId())) {
                util.setAppId(s);
                util.setUserId(s1);
                util.setChannelId(s2);
                util.setRequestId(s3);
            }
        }
        Log.e("TAG", "errorCode124=" + i);
    }

    @Override
    public void onUnbind(Context context, int i, String s) {

    }

    @Override
    public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {

    }

    @Override
    public void onMessage(Context context, String s, String s1) {

    }

    @Override
    public void onNotificationClicked(Context context, String s, String s1, String s2) {
        Log.e("TAG", "点击了状态栏的通知");
    }

    @Override
    public void onNotificationArrived(Context context, String s, String s1, String s2) {

    }
}
