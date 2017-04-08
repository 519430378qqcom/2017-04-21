package com.umiwi.ui.push;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.SplashActivity;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.activity.UmiwiDetailActivity;
import com.umiwi.ui.fragment.LecturerDetailFragment;
import com.umiwi.ui.fragment.ShakeFragment;
import com.umiwi.ui.fragment.UserTestInfoFragment;
import com.umiwi.ui.fragment.WebFragment;
import com.umiwi.ui.fragment.course.BigZTListFragment;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.fragment.course.CourseSequenceListFragment;
import com.umiwi.ui.fragment.course.JPZTDetailFragment;
import com.umiwi.ui.fragment.mine.MyCouponFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.youmi.framework.util.SharePreferenceUtil;

/**
 * Push消息处理receiver。请编写您需要的回调函数，
 * 一般来说：
 * onBind是必须的，用来处理startWork返回值；
 * onMessage用来接收透传消息；
 * onSetTags、onDelTags、onListTags是tag相关操作的回调；
 * onNotificationClicked在通知被点击时回调；
 * onUnbind是stopWork接口的返回值回调
 * <p/>
 * 返回值中的errorCode，解释如下：
 * 0 - Success
 * 10001 - Network Problem
 * 30600 - Internal Server Error
 * 30601 - Method Not Allowed
 * 30602 - Request Params Not Valid
 * 30603 - Authentication Failed
 * 30604 - Quota Use Up Payment Required
 * 30605 - Data Required Not Found
 * 30606 - Request Time Expires Timeout
 * 30607 - Channel Token Timeout
 * 30608 - Bind Relation Not Found
 * 30609 - Bind Number Too Many
 * <p/>
 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 */
public class MyPushMessageReceiver extends PushMessageReceiver {
    /**
     * TAG to Log
     */
    public static final String TAG = MyPushMessageReceiver.class
            .getSimpleName();

    /**
     * 调用PushManager.startWork后，sdk将对push
     * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
     * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
     *
     * @param context   BroadcastReceiver的执行Context
     * @param errorCode 绑定接口返回值，0 - 成功
     * @param appid     应用id。errorCode非0时为null
     * @param userId    应用user id。errorCode非0时为null
     * @param channelId 应用channel id。errorCode非0时为null
     * @param requestId 向服务端发起的请求id。在追查问题时有用；
     * @return none
     */
    @Override
    public void onBind(Context context, int errorCode, String appid,
                       String userId, String channelId, String requestId) {

        String responseString = "onBind errorCode=" + errorCode + " appid=" + appid + " userId=" + userId + " channelId=" + channelId + " requestId=" + requestId;
        Log.e("TAG", responseString);

        // 绑定成功，设置已绑定flag，可以有效的减少不必要的绑定请求
        if (errorCode == 0) {
            Utils.setBind(context, true);
            SharePreferenceUtil util = UmiwiApplication.getInstance().getSpUtil();
            if (TextUtils.isEmpty(util.getAppId())) {
                util.setAppId(appid);
                util.setChannelId(channelId);
                util.setUserId(userId);
                util.setRequestId(requestId);
            }
        }
        Log.e("TAG", "errorCode=" + errorCode);
    }

    /**
     * 接收透传消息的函数。
     *
     * @param context             上下文
     * @param message             推送的消息
     * @param customContentString 自定义内容,为空或者json字符串
     */
    @Override
    public void onMessage(Context context, String message,
                          String customContentString) {
//		String messageString = "透传消息 message=\"" + message + "\" customContentString=" + customContentString;
//        LogUtils.e(TAG, "透传消息 message=\"" + message + "\" customContentString=" + customContentString);

        try {
            JSONObject jsonContent = new JSONObject(message);
            String title = jsonContent.getString("title");
            String description = jsonContent.getString("description");
            JSONObject params = jsonContent.getJSONObject("custom_content");
            String msgtype = params.getString("type");
            String msgturl = params.getString("url");

            showNotification(title, description, msgtype, msgturl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 接收通知点击的函数。
     * @param context             上下文
     * @param title               推送的通知的标题
     * @param description         推送的通知的描述
     * @param customContentString 自定义内容，为空或者json字符串
     */
    @Override
    public void onNotificationClicked(Context context, String title,
                                      String description, String customContentString) {

//		String notifyString = "通知点击 title=\"" + title + "\" description=\"" + description + "\" customContent=" + customContentString;
//		Log.d("TAG", notifyString);
//		System.out.println("=============="+customContentString);
        // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值


        if (customContentString != null & !TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                String classes = null;
                if (!customJson.isNull("umiwidetailurl")) {
                    myvalue = customJson.getString("umiwidetailurl");
                    if (!customJson.isNull("classes")) {
                        classes = customJson.getString("classes");
                        if ("album".equals(classes)) {// 专辑
                            updateDetialContent(context, myvalue);
                        } else if ("zhuanti".equals(classes)) {// 专题
                            updateZhuanTiDetialContent(context, myvalue);
                        } else if ("lecturer".equals(classes)) {// 讲师
                            updateLecturerContent(context, myvalue);
                        } else if ("web".equals(classes)) {
                            updateWebContent(context, myvalue);
                        } else if ("bigzhuanti".equals(classes)) {
                            updateBigZhuanTiDetialContent(context, myvalue);
                        } else {
                            updateNewVersion(context, myvalue);
                        }
                    }
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    /**
     * 接收通知到达的函数。
     * @param context             上下文
     * @param title               推送的通知的标题
     * @param description         推送的通知的描述
     * @param customContentString 自定义内容，为空或者json字符串
     */

    @Override
    public void onNotificationArrived(Context context, String title,
                                      String description, String customContentString) {

//		String notifyString = "onNotificationArrived  title=\"" + title
//				+ "\" description=\"" + description + "\" customContent="
//				+ customContentString;
//		Log.d("TAG", notifyString);

//		// 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
//		if (!TextUtils.isEmpty(customContentString)) {
//			JSONObject customJson = null;
//			try {
//				customJson = new JSONObject(customContentString);
//				String myvalue = null;
//				if (!customJson.isNull("mykey")) {
//					myvalue = customJson.getString("mykey");
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//		// 你可以參考 onNotificationClicked中的提示从自定义内容获取具体值
//		updateContent(context, notifyString);
    }

    @Override
    public void onSetTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
    }

    @Override
    public void onDelTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
    }

    @Override
    public void onListTags(Context context, int errorCode, List<String> tags,
                           String requestId) {
    }

    /**
     * PushManager.stopWork() 的回调函数。
     * @param context   上下文
     * @param errorCode 错误码。0表示从云推送解绑定成功；非0表示失败。
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {

//		String responseString = "onUnbind errorCode=" + errorCode+ " requestId = " + requestId;
//		Log.d(TAG, responseString);

        // 解绑定成功，设置未绑定flag，
        if (errorCode == 0) {
            Utils.setBind(context, false);
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//		updateContent(context, responseString);
    }

    /**
     * 打开课程详情页
     */
    private void updateDetialContent(Context context, String content) {
//		Log.d(TAG, "updateDetialContent");
        String logText = "" + Utils.logStringCache;

        if (!logText.equals("")) {
            logText += "\n";
        }

        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
        logText += sDateFormat.format(new Date()) + ": ";
        logText += content;

        Utils.logStringCache = logText;

        Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(), UmiwiDetailActivity.class);
        intent.putExtra(UmiwiDetailActivity.KEY_DETAIL_URL, content);
        intent.putExtra(UmiwiDetailActivity.KEY_FRAGMENT_CLASSES, UmiwiDetailActivity.CLASS_COURSE_DETAIL);
        intent.putExtra(UmiwiDetailActivity.KEY_PUSH_CLASSES, 9);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.getApplicationContext().startActivity(intent);
    }

    /**
     * 打开专题页
     */
    @SuppressLint("SimpleDateFormat")
    private void updateZhuanTiDetialContent(Context context, String content) {
//		Log.d(TAG, "updateZhuanTiDetialContent");
        String logText = "" + Utils.logStringCache;

        if (!logText.equals("")) {
            logText += "\n";
        }

        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
        logText += sDateFormat.format(new Date()) + ": ";
        logText += content;

        Utils.logStringCache = logText;

        Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(), UmiwiDetailActivity.class);
        intent.putExtra(UmiwiDetailActivity.KEY_DETAIL_URL, content);
        intent.putExtra(UmiwiDetailActivity.KEY_FRAGMENT_CLASSES, UmiwiDetailActivity.CLASS_ZHUANTI_DETAIL);
        intent.putExtra(UmiwiDetailActivity.KEY_PUSH_CLASSES, 9);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(intent);
    }

    /**
     * 打开大专题页
     */
    @SuppressLint("SimpleDateFormat")
    private void updateBigZhuanTiDetialContent(Context context, String content) {
//		Log.d(TAG, "updateZhuanTiDetialContent");
        String logText = "" + Utils.logStringCache;

        if (!logText.equals("")) {
            logText += "\n";
        }

        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
        logText += sDateFormat.format(new Date()) + ": ";
        logText += content;

        Utils.logStringCache = logText;

        Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(), UmiwiDetailActivity.class);
        intent.putExtra(UmiwiDetailActivity.KEY_DETAIL_URL, content);
        intent.putExtra(UmiwiDetailActivity.KEY_FRAGMENT_CLASSES, UmiwiDetailActivity.CLASS_BIG_ZHUANTI_DETAIL);
        intent.putExtra(UmiwiDetailActivity.KEY_PUSH_CLASSES, 9);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(intent);
    }

    /**
     * 打开讲师页
     */
    private void updateLecturerContent(Context context, String content) {
        String logText = "" + Utils.logStringCache;

        if (!logText.equals("")) {
            logText += "\n";
        }

        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
        logText += sDateFormat.format(new Date()) + ": ";
        logText += content;

        Utils.logStringCache = logText;

        Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(), UmiwiContainerActivity.class);
        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LecturerDetailFragment.class);
        intent.putExtra(LecturerDetailFragment.KEY_DEFAULT_DETAILURL, content);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(intent);

    }

    /**
     * 打开网页
     */
    private void updateWebContent(Context context, String content) {
//		Log.d(TAG, "updateZhuanTiDetialContent");
        String logText = "" + Utils.logStringCache;

        if (!logText.equals("")) {
            logText += "\n";
        }

        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
        logText += sDateFormat.format(new Date()) + ": ";
        logText += content;

        Utils.logStringCache = logText;

        Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(), UmiwiContainerActivity.class);
        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, WebFragment.class);
        intent.putExtra(WebFragment.WEB_URL, content);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(intent);
    }

    private void updateNewVersion(Context context, String content) {
//		Log.d(TAG, "updateZhuanTiDetialContent");
        String logText = "" + Utils.logStringCache;

        if (!logText.equals("")) {
            logText += "\n";
        }

        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
        logText += sDateFormat.format(new Date()) + ": ";
        logText += content;

        Utils.logStringCache = logText;

        Toast.makeText(context, "优米创业版本过低,请先升级到最新版本", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setData(Uri.parse("http://m.youmi.cn/"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        context.startActivity(intent);

    }

    /**
     * @param title
     * @param description
     * @param type        msgtype = 1 去任意活动页，msgurl 会给出要去的活动页面
     *                    msgtype=2 去课程播放页  ，msgurl 会给出detailurl，msgid 会给出课程id
     *                    msgtype=3 去 我的-》学习周报
     *                    msgtype=4 去 我的=》优惠券
     *                    msgtype=5 去 我的=》积分
     *                    msgtype=6 去 课程分类页   msgurl 会给出 detailurl
     *                    msgtype=7  去 发现-》积分商城
     *                    msgtype=8  去 发现-》发现适合我的课程
     *                    msgtype=9  去 发现-》摇一摇
     *                    msgtype=10 打开应用即可。
     *                    msgtype=11 去大专题
     *                    msgtype=12 去专题
     * @param url
     */
    private void showNotification(String title, String description, String type, String url) {

        Context context = UmiwiApplication.getContext();
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        switch (Integer.valueOf(type)) {
            case 1:
                intent.setClass(context.getApplicationContext(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, WebFragment.class);
                intent.putExtra(WebFragment.WEB_URL, url);
                break;
            case 2:
                intent.setClass(context, UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
                intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, url);
                break;
            case 3:
                intent.setClass(context.getApplicationContext(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, WebFragment.class);
                intent.putExtra(WebFragment.WEB_URL, UmiwiAPI.WEEK_REPORT);
                break;
            case 4:
                intent.setClass(context, UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, MyCouponFragment.class);
                break;
            case 5:
                intent.setClass(context.getApplicationContext(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, WebFragment.class);
                intent.putExtra(WebFragment.WEB_URL, UmiwiAPI.INTEGRAL);
                break;
            case 6:
                intent.setClass(context, UmiwiContainerActivity.class);
                intent.putExtra(CourseSequenceListFragment.KEY_URL, url);
                intent.putExtra(CourseSequenceListFragment.KEY_ACTION_TITLE, title);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseSequenceListFragment.class);
                break;
            case 7:
                intent.setClass(context.getApplicationContext(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, WebFragment.class);
                intent.putExtra(WebFragment.WEB_URL, UmiwiAPI.INTEGRAL_SHOP);
                break;
            case 8:
                intent.setClass(context, UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, UserTestInfoFragment.class);
                intent.putExtra(UserTestInfoFragment.URL_CATEGORY, UmiwiAPI.USER_TEST_COURSE);
                break;
            case 9:
                intent.setClass(context, UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ShakeFragment.class);
                break;
            case 10:
//                if (!ActivityUtils.isActivityExist(context, HomeMainActivity.class)) {
//                    intent.setClass(context, SplashActivity.class);
//                } else {
//                    intent.setClass(context, SplashActivity.class);
//                }
                //TODO
                intent.setClass(context, SplashActivity.class);
                break;
            case 11:
                intent.setClass(context, UmiwiContainerActivity.class);
                intent.putExtra(BigZTListFragment.KEY_URL, url);
                intent.putExtra(BigZTListFragment.KEY_ACTION_TITLE, title);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, BigZTListFragment.class);
                break;
            case 12:
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, JPZTDetailFragment.class);
                intent.putExtra(JPZTDetailFragment.KEY_URL, url);
                break;
        }
        startNotification(intent, title, description);
    }

    /**
     * 启动通知
     */
    private void startNotification(Intent intent, String title, String description) {
        Bitmap largeIcon = BitmapFactory.decodeResource(UmiwiApplication.getInstance().getResources(), R.drawable.ic_launcher);
        PendingIntent pendingIntent = PendingIntent.getActivity(UmiwiApplication.getContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new NotificationCompat.Builder(
                UmiwiApplication.getContext()).setAutoCancel(true)
                .setContentTitle(title).setContentText(description)
                .setDefaults(Notification.DEFAULT_ALL)
                        // 设置使用所有默认值（声音、震动、闪屏等）
                .setLargeIcon(largeIcon).setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher)// 删除就显示不了了
                .setWhen(System.currentTimeMillis())
                        // .setNumber(mNewNum)
                .build();

        NotificationManager mNotificationManager = (NotificationManager) UmiwiApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, notification);
    }


}
