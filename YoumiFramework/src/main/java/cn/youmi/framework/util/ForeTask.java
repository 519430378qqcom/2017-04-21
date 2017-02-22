package cn.youmi.framework.util;

public abstract class ForeTask extends BackForeTask {

	public ForeTask() {
		exeBackTask = false;
	}

	@Override
	protected void runInBackground() {

	}

	@Override
	protected abstract void runInForeground();

}
