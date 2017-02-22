package cn.youmi.framework.http;

public interface DispatcherObserver {
	void willDispatch(AbstractRequest<?> req);
}
