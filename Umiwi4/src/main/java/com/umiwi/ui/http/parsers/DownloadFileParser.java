package com.umiwi.ui.http.parsers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;

/**
 * @author tangxiyong
 * @version 2014年6月17日 下午6:52:21
 */
public class DownloadFileParser implements Parser<File, InputStream> {

	@Override
	public File parse(AbstractRequest<File> request, InputStream is) {

		BufferedInputStream bis = new BufferedInputStream(is);
		//获取外部sd卡状态
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			File file;
			try {
				file = new File(Environment.getExternalStorageDirectory()+"/umiwi.apk");
				//输入输出是针对程序而言的
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len ; 
				while ( (len = bis.read(buffer))!=-1){	
					fos.write(buffer, 0, len);
				}
				fos.close();
				bis.close();
				return file;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		return null;
	}

}
