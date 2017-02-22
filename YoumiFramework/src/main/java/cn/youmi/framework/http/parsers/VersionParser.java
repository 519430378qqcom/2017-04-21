package cn.youmi.framework.http.parsers;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;
import cn.youmi.framework.model.VersionModel;

public class VersionParser implements Parser<VersionModel, InputStream> {

	@Override
	public VersionModel parse(AbstractRequest<VersionModel> request, InputStream is) {
		XmlPullParser parser =  Xml.newPullParser();
		//设置parser的数据源
		try {
			parser.setInput(is, "UTF-8");
			int type = parser.getEventType();
			VersionModel version = new VersionModel();
			while(type !=XmlPullParser.END_DOCUMENT ){
				switch (type) {
				case XmlPullParser.START_TAG:
					if("version".equals(parser.getName())){
						version.setVersion(next(parser));
					}else if("url".equals(parser.getName())){
						version.setUrl(next(parser));
					}else if("description".equals(parser.getName())){
						version.setDescription(next(parser));
					}
					break;
				}
				try {
					type = parser.next();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return version;
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
		}
		
		return null;
	}
	
	private String next(XmlPullParser parser){
		try {
			return parser.nextText();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
