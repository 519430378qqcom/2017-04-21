package cn.youmi.framework.manager;

import java.io.File;

import android.content.Context;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.parsers.DownloadFileParser;
import cn.youmi.framework.http.parsers.VersionParser;
import cn.youmi.framework.model.VersionModel;
import cn.youmi.framework.util.SingletonFactory;
import cn.youmi.framework.util.UpdateUtils;

public class VersionManager extends ModelManager<String, VersionModel> {

	public static VersionManager getInstance() {
		return SingletonFactory.getInstance(VersionManager.class);
	}

	private Listener<VersionModel> listener = new Listener<VersionModel>() {

		@Override
		public void onResult(AbstractRequest<VersionModel> request,
				VersionModel t) {
			if (t != null) {

				String tag = (String) request.getTag();

				if (t.shouldUpdate()) {
					if ("home".equals(tag)) {
						for (ModelStatusListener<String, VersionModel> l : listeners) {
							l.onModelGet("homeupdate", t);
						}
					} else if ("setting".equals(tag)) {
						for (ModelStatusListener<String, VersionModel> l : listeners) {
							l.onModelGet("settingupdate", t);
						}
					}

				} else {
					if ("home".equals(tag)) {
						for (ModelStatusListener<String, VersionModel> l : listeners) {
							l.onModelGet("togohome", t);
						}
					} else if ("setting".equals(tag)) {
						for (ModelStatusListener<String, VersionModel> l : listeners) {
							l.onModelGet("showlog", t);
						}
					}
				}

			
			}
		}

		@Override
		public void onError(AbstractRequest<VersionModel> requet,
				int statusCode, String body) {
			String tag = (String) requet.getTag();
			if ("home".equals(tag)) {
				for (ModelStatusListener<String, VersionModel> l : listeners) {
					l.onModelGet("homeerror", null);
				}
			} else if ("setting".equals(tag)) {
				for (ModelStatusListener<String, VersionModel> l : listeners) {
					l.onModelGet("settingerror", null);
				}
			}
		}
	};

	public void checkNewVersion(String where, String upateUrl) {
		GetRequest<VersionModel> request = new GetRequest<VersionModel>(
				upateUrl, VersionParser.class, listener);
		request.setTag(where);
		HttpDispatcher.getInstance().go(request); 
	}

	private Listener<File> downloadFileListener = new Listener<File>() {

		@Override
		public void onResult(AbstractRequest<File> request, File t) {

			for (ModelStatusListener<String, VersionModel> l : listeners) {
				l.onModelGet("down", null);
			}
			UpdateUtils.installApk(t);
		}

		@Override
		public void onError(AbstractRequest<File> requet, int statusCode,
				String body) {
			for (ModelStatusListener<String, VersionModel> l : listeners) {
				l.onModelGet("downerror", null);
			}
		}
	};

	public void downloadAndInstall(VersionModel vm, Context context) {
		GetRequest<File> request = new GetRequest<File>(vm.getUrl(),
				DownloadFileParser.class, downloadFileListener);
		request.go();
	}

}
