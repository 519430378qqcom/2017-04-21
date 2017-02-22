package cn.youmi.framework.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.youmi.framework.main.BaseApplication;

public class UpdateUtils {

	private UpdateCallback callback;
	private int progress;
	private Boolean canceled;
	public static String UPDATE_DOWNURL = "";
	public static final String UPDATE_SAVENAME = "update.apk";
	
	private static final int UPDATE_CHECKCOMPLETED = 1;
	private static final int UPDATE_DOWNLOADING = 2;
	private static final int UPDATE_DOWNLOAD_ERROR = 3;
	private static final int UPDATE_DOWNLOAD_COMPLETED = 4;
	private static final int UPDATE_DOWNLOAD_CANCELED = 5;

	public static final File updateFile = new File(StorageUtils.getApkUpdatePath(), UpdateUtils.UPDATE_SAVENAME);
	public static final String updateAppFilePath = new File(StorageUtils.getApkUpdatePath(), UPDATE_SAVENAME).getPath();
	
	public UpdateUtils(Context context, UpdateCallback updateCallback) {
		callback = updateCallback;
		canceled = false;
	}

	public void isUpdate(final String downUrl) {
		UPDATE_DOWNURL = downUrl;
		updateHandler.sendEmptyMessage(UPDATE_CHECKCOMPLETED);
	}

	public static void installApk(File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		BaseApplication.getApplication().startActivity(intent);
	}
	
	public void downloadPackage() {

		new Thread() {
			

			@Override
			public void run() {
				try {
					URL url = new URL(UPDATE_DOWNURL);

					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();

					File ApkFile = new File(StorageUtils.getApkUpdatePath(), UPDATE_SAVENAME);

					if (ApkFile.exists()) {
						ApkFile.delete();
					}

					FileOutputStream fos = new FileOutputStream(ApkFile);

					int count = 0;
					byte buf[] = new byte[512];

					do {
						int numread = is.read(buf);
						count += numread;
						progress = (int) (((float) count / length) * 100);

						updateHandler.sendMessage(updateHandler.obtainMessage(UPDATE_DOWNLOADING));
						if (numread <= 0) {

							updateHandler.sendEmptyMessage(UPDATE_DOWNLOAD_COMPLETED);
							break;
						}
						fos.write(buf, 0, numread);
					} while (!canceled);
					if (canceled) {
						updateHandler.sendEmptyMessage(UPDATE_DOWNLOAD_CANCELED);
					}
					fos.close();
					is.close();
				} catch (MalformedURLException e) {
					e.printStackTrace();

					updateHandler.sendMessage(updateHandler.obtainMessage(
							UPDATE_DOWNLOAD_ERROR, e.getMessage()));
				} catch (IOException e) {
					e.printStackTrace();

					updateHandler.sendMessage(updateHandler.obtainMessage(
							UPDATE_DOWNLOAD_ERROR, e.getMessage()));
				}

			}
		}.start();
	}

	public void cancelDownload() {
		canceled = true;
	}

	Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case UPDATE_CHECKCOMPLETED:

				callback.downloading();
				break;
			case UPDATE_DOWNLOADING:

				callback.downloadProgressChanged(progress);
				break;
			case UPDATE_DOWNLOAD_ERROR:

				callback.downloadCompleted(false, msg.obj.toString());
				break;
			case UPDATE_DOWNLOAD_COMPLETED:

				callback.downloadCompleted(true, "");
				break;
			case UPDATE_DOWNLOAD_CANCELED:

				callback.downloadCanceled();
			default:
				break;
			}
		}
	};

	public interface UpdateCallback {
		void downloading();

		void downloadProgressChanged(int progress);

		void downloadCanceled();

		void downloadCompleted(Boolean sucess, CharSequence errorMsg);
	}

	
	/**
	* 获取apk包的信息：版本号
	*/
	public static String getAppVersion(String absPath, Context context) {
		PackageManager pm = context.getPackageManager();
		PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES);
		if (pkgInfo != null) {
			return pkgInfo.versionName;
		}
		return "1";
	}
	
	public static String getNativeVersion(){
		String versionName = null;
		try {
			versionName = BaseApplication.getApplication().getPackageManager()
				    .getPackageInfo(BaseApplication.getApplication().getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

}
