package cn.youmi.framework.http.parsers;

public class HttpParserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8478304415218424687L;
	
	private String msg;
	public String getMsg(){
		return msg;
	}
	public HttpParserException(String msg){
		this.msg = msg;
	}
}
