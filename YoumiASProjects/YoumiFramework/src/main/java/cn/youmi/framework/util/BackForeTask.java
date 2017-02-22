package cn.youmi.framework.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Looper;

public abstract class BackForeTask {
	private static ExecutorService _threadPool = Executors
			.newFixedThreadPool(4);
	private static Handler _handler = new Handler(Looper.getMainLooper());

	protected boolean exeBackTask = true;

	public BackForeTask() {
		if (!exeBackTask) {
			_handler.post(new Runnable() {
				@Override
				public void run() {
					runInForeground();
				}
			});
			return;
		}
		_threadPool.execute(new Runnable() {
			@Override
			public void run() {
				runInBackground();
				_handler.post(new Runnable() {
					@Override
					public void run() {
						runInForeground();
					}
				});
			}
		});
	}

	protected abstract void runInBackground();

	protected abstract void runInForeground();
}
