package cn.youmi.framework.http.parsers;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;

/**
@Author buhe
2014-6-12下午1:49:52
 */
public class StringParser implements Parser<String, String> {

	@Override
	public String parse(AbstractRequest<String> request, String is) {
		return is;
	}

}
