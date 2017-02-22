package cn.youmi.framework.http;


public class PostRequest<T> extends AbstractRequest<T>{

	

public PostRequest(String url, Class<?> parserClass,
			cn.youmi.framework.http.AbstractRequest.Listener<T> listener) {
		super(url, parserClass, listener);
	}

	public PostRequest(String url, Class<? extends ParserTarget> parserClass,
			Class<?> modelClass,
			cn.youmi.framework.http.AbstractRequest.Listener<T> listener) {
		super(url, parserClass, modelClass, listener);
	}

	@Override
	public final String getMethod() {
		return "POST";
	}
}
