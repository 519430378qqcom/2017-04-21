package com.umiwi.ui.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import cn.youmi.framework.util.FileUtils;
import cn.youmi.framework.util.ImageUtillity;
import cn.youmi.framework.util.saveimage.SaveImageAsyncTask;

public class SaveImageUriToCachePathAsyncTaskFragment extends Fragment {

	private ConvertTask task;
	
	private CameraResultEvent cameraResultEvent;

	public static SaveImageUriToCachePathAsyncTaskFragment newInstance(Uri uri) {
		SaveImageUriToCachePathAsyncTaskFragment fragment = new SaveImageUriToCachePathAsyncTaskFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable("uri", uri);
		fragment.setArguments(bundle);
		fragment.setRetainInstance(true);
		return fragment;
	}
	
	public static SaveImageUriToCachePathAsyncTaskFragment newInstance(Uri uri, CameraResultEvent event) {
		SaveImageUriToCachePathAsyncTaskFragment fragment = new SaveImageUriToCachePathAsyncTaskFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable("uri", uri);
		bundle.putSerializable("event", event);
		fragment.setArguments(bundle);
		fragment.setRetainInstance(true);
		return fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (task == null) {
			task = new ConvertTask((Uri) getArguments().getParcelable("uri"));
			cameraResultEvent = (CameraResultEvent) getArguments().getSerializable("event");
			task.executeOnExecutor(SaveImageAsyncTask.THREAD_POOL_EXECUTOR);
		}
	}

	private class ConvertTask extends AsyncTask<Void, Void, String> {

		ContentResolver mContentResolver;

		Uri uri;

		public ConvertTask(Uri uri) {
			this.uri = uri;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mContentResolver = getActivity().getContentResolver();
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				InputStream inputStream = mContentResolver.openInputStream(uri);
				String path = Environment.getExternalStorageDirectory().getPath() + "/picture.png";

				if (TextUtils.isEmpty(path)) {
					return null;
				}
				File file = new File(path);
				file.getParentFile().mkdirs();
				if (file.exists() || file.length() > 0) {
					file.delete();
				}
				file.createNewFile();
				FileUtils.copyFile(inputStream, file);
				if (ImageUtillity.isThisBitmapCanRead(path)) {
					return path;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			if (getActivity() == null) {
				return;
			}
			getFragmentManager().beginTransaction()
					.remove(SaveImageUriToCachePathAsyncTaskFragment.this)
					.commitAllowingStateLoss();
			if (TextUtils.isEmpty(s)) {
				CameraManager.getInstance().setCameraResult(CameraResultEvent.ERROR);
				return;
			}
			if (cameraResultEvent == CameraResultEvent.SAVE) {
				CameraManager.getInstance().setCameraResult(CameraResultEvent.SAVE, s);
			} else {
				CameraManager.getInstance().setCameraResult(CameraResultEvent.SUCC, s);
			}
			
		}
	}
}