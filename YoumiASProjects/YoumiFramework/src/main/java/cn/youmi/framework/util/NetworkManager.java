package cn.youmi.framework.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cn.youmi.framework.debug.LogUtils;
import cn.youmi.framework.main.BaseApplication;
import cn.youmi.framework.main.ContextProvider;

public class NetworkManager {

	public interface OnNetworkChangeListener {
		void onNetworkChange();
	}

	private ConnectivityManager connectivityManager;
	private static NetworkManager _instance;
	private ArrayList<OnNetworkChangeListener> listeners = new ArrayList<NetworkManager.OnNetworkChangeListener>();
	private NetworkStatusListener statusListener = new NetworkStatusListener();

	public void addListener(OnNetworkChangeListener l) {
		listeners.add(l);
	}

	public void removeListener(OnNetworkChangeListener l) {
		listeners.remove(l);
	}

	private NetworkManager() {
		connectivityManager = (ConnectivityManager) BaseApplication
				.getApplication()
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		IntentFilter intentFilter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		BaseApplication.getApplication().registerReceiver(statusListener,
				intentFilter);
		refresh();
	}

	public static NetworkManager getInstance() {
		if (_instance == null) {
			_instance = new NetworkManager();
		}
		return _instance;
	}

	public void notifyChange() {
		for (OnNetworkChangeListener l : listeners) {

			l.onNetworkChange();
		}
	}

	public NetworkInfo getNetworkInfo() {
		return connectivityManager.getActiveNetworkInfo();
	}

	public void refresh() {
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			// String name = info.getTypeName();
		} else {
		}
	}

	public static class NetworkStatusListener extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				NetworkManager.getInstance().refresh();
				NetworkManager.getInstance().notifyChange();
			} catch (NullPointerException e){
				e.printStackTrace();
			}
		}
	}

	public boolean isWifi() {
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info == null) {
			return false;
		}
		return info.getType() == ConnectivityManager.TYPE_WIFI;
	}
	
	public boolean isWapNetwork() {
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		return info != null && info.getType() == ConnectivityManager.TYPE_MOBILE;
	}

	/** 获取当前网络名称 */
	public String getNetName() {
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info == null) {
			return "error";
		}
		return info.getTypeName();

	}

	public boolean checkNet(Context context) {
		try {
			// 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
			ConnectivityManager connecttivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connecttivity != null) {
				// 获取网络连接管理对象
				NetworkInfo info = connecttivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 当启动，登录或分享时判断是否走代理了，走了，提示用户
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public boolean isProxy() {
		return !(android.net.Proxy.getDefaultHost() == null && android.net.Proxy.getDefaultPort() == -1);
	}
	
	/**
	 * @category 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
	 * @return
	 */
	public final boolean ping() {

		String result = null;
		try {
			String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
			Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
			// 读取ping的内容，可以不加
			InputStream input = p.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			StringBuffer stringBuffer = new StringBuffer();
			String content = "";
			while ((content = in.readLine()) != null) {
				stringBuffer.append(content);
			}
			LogUtils.e("------ping-----",
					"result content : " + stringBuffer.toString());
			// ping的状态
			int status = p.waitFor();
			if (status == 0) {
				result = "success";
				return true;
			} else {
				result = "failed";
			}
		} catch (IOException e) {
			result = "IOException";
		} catch (InterruptedException e) {
			result = "InterruptedException";
		} finally {
			LogUtils.e("----result---", "result = " + result);
		}
		return false;

	}
}
