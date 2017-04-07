package cn.youmi.framework.util;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * 保存推送的id
 * @author tangxiong
 * @version 2014年10月11日 下午4:01:47
 */
public class SharePreferenceUtil {
	public static final String MESSAGE_NOTIFY_KEY = "message_notify";
	public static final String MESSAGE_SOUND_KEY = "message_sound";
	public static final String MESSAGE_DISTURB_KEY = "message_push_disturb_20160108";
	public static final String SHOW_HEAD_KEY = "show_head";
	public static final String PULLREFRESH_SOUND_KEY = "pullrefresh_sound";
	public static final String PLAY_WITH_3G = "play_with_3g";
	public static final String SHOW_3G_DIALOG = "show_3g_dialog";
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	public SharePreferenceUtil(Context context, String file) {
		sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		editor = sp.edit();
	}
	public void setDeviceId(String deviceId){
		editor.putString("deviceid",deviceId);
		editor.commit();
	}
	public String getDeviceId(){
		return sp.getString("deviceid","");
	}
	// bind succ 
	public void setBindSucc(boolean succ) {
		editor.putBoolean("is_succ", succ);
		editor.commit();
	}
	
	public boolean isBindSucc() {
		return sp.getBoolean("is_succ", false);
	}
	
	// appid
	public void setAppId(String appid) {
		editor.putString("appid", appid);
		editor.commit();
	}

	public String getAppId() {
		return sp.getString("appid", "");
	}

	// user_id
	public void setUserId(String userId) {
		editor.putString("userId", userId);
		editor.commit();
	}

	public String getUserId() {
		return sp.getString("userId", "");
	}

	// channel_id
	public void setChannelId(String channelId) {
		editor.putString("ChannelId", channelId);
		editor.commit();
	}

	public String getChannelId() {
		return sp.getString("ChannelId", "");
	}
	
	//requestId
	public void setRequestId(String requestId) {
		editor.putString("requestId", requestId);
		editor.commit();
	}
	
	public String getRequestId() {
		return sp.getString("requestId", "");
	}
	
	// 声音是否提示
	public boolean getSound() {
		return sp.getBoolean(MESSAGE_SOUND_KEY, true);
	}

	public void setSound(boolean isChecked) {
		editor.putBoolean(MESSAGE_SOUND_KEY, isChecked);
		editor.commit();
	}
	
	// 是否免打扰
	public boolean getDisturb() {
		return sp.getBoolean(MESSAGE_DISTURB_KEY, false);
	}
	
	public void setDisturb(boolean isChecked) {
		editor.putBoolean(MESSAGE_DISTURB_KEY, isChecked);
		editor.commit();
	}

	// 设置Tag (非会员，注册会员，钻石会员)
	public void setTag(String tag) {
		editor.putString("tag", tag);
		editor.commit();
	}

	public String getTag() {
		return sp.getString("tag", "");
	}

	// 设置是否在3g下观看
	public boolean getPlayWith3G() {
		return sp.getBoolean(PLAY_WITH_3G, false);
	}

	public void setPalyWith3G(boolean isChecked) {
		editor.putBoolean(PLAY_WITH_3G, isChecked);
		editor.commit();
	}

	// 是否需要弹出3g提示弹出窗
	public boolean getShow3GDialog() {
		return sp.getBoolean(SHOW_3G_DIALOG, false);
	}

	public void setShow3GDialog(boolean isShow) {
		editor.putBoolean(SHOW_3G_DIALOG, isShow);
		editor.commit();
	}
}
