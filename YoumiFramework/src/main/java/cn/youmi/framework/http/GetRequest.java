package cn.youmi.framework.http;


public class GetRequest<T> extends AbstractRequest<T> {

	public GetRequest(String url, Class<?> parserClass,
			cn.youmi.framework.http.AbstractRequest.Listener<T> listener) {
		super(url, parserClass, listener);
	}

	public GetRequest(String url,Class<? extends ParserTarget> parserClass,Class<?> modelClass,Listener<T> listener){
		super(url, parserClass, modelClass,listener);
	}
	
	@Override
	public final String getMethod() {
		return "GET";
	}
}
