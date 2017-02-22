package cn.youmi.framework.http.parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.gson.JsonSyntaxException;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Parser;

public class DownloadFileParser implements Parser<File,InputStream>{

	public static final String KEY_FILE_PATH = "key.filePath";
	@Override
	public File parse(AbstractRequest<File> request, InputStream is)
			throws JsonSyntaxException {
//		File file = (File) request.getTag();
		File file = (File) request.getExtras().getSerializable(KEY_FILE_PATH);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buffer = new byte[10 * 1024];
			int readen = -1;
			while((readen = is.read(buffer)) != - 1){
				fos.write(buffer, 0, readen);
			}
			fos.flush();
			fos.close();//TODO
			return file;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			file.delete();
		} catch (IOException e) {
			e.printStackTrace();
			file.delete();
		}
		return null;
	}
}
