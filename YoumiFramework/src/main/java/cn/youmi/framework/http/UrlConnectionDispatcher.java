package cn.youmi.framework.http;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.message.BasicHeader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.youmi.framework.http.AbstractRequest.Parser;
import cn.youmi.framework.main.BaseApplication;
import cn.youmi.framework.main.ConstantProvider;
import cn.youmi.framework.util.SingletonFactory;

public class UrlConnectionDispatcher extends HttpDispatcher {
	private static CookieDao cookiedao = CookieDao.getInstance(BaseApplication.getApplication());
	
	public static UrlConnectionDispatcher getInstance(){
		return SingletonFactory.getInstance(UrlConnectionDispatcher.class);
	}

	@Override
	public <T> void go(final AbstractRequest<T> request) {
		getTheradPool().submit(new Runnable() {
			@Override
			public void run() {
				String fileName = null;
				String filePath = null;
				for (String key : request.getFilesMap().keySet()) {
					fileName = key;
					filePath = request.getFilesMap().get(key);
//					Log.e("xx","key" + key  + " = " + filePath);
				}

				MultipartUtility multipart = null;
				try {
					multipart = new MultipartUtility(request.getURL(), "UTF-8");
				} catch (IOException e) {
					e.printStackTrace();
					getHandler().post(new Runnable(){
						@Override
						public void run() {
							request.getListener().onError(request, -1, "");
						}
						
					});
//					Log.e("xx","new Exccc");
					return;
				}

				multipart.addHeaderField("Connection", "Keep-Alive");
//				Log.e("xx","afeter header connection");
				
				String userAgent = request.getHeader("User-Agent");
				if(userAgent == null){
					userAgent = "Mozilla/5.0 (Linux; U; Android 0.5; en-us) AppleWebKit/522+ (KHTML, like Gecko) Safari/419.3";
				}
				
				
				// userAgent =
				// "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36";
				multipart.addHeaderField("User-Agent", userAgent);// TODO
				multipart.addHeaderField("COOKIE", getCoookieFromDB());

//				Log.e("xx","afeter header cookie");
				HashMap<String, String> params = request.getParams();
				
				
				for (String key : params.keySet()) {
					String val = params.get(key);
					multipart.addFormField(key, val);
//					Log.e("xx","key ==" + key + " :" + val);
				}

				try {

					long total = 0;
					for(String key:request.getFilesMap().keySet()){
						File file = new File(request.getFilesMap().get(key));
						total += file.length();
//						Log.e("xx","total=" + total);
					}
					request.setTogal(total);
					
					for (String key : request.getFilesMap().keySet()) {
						
						fileName = key;
						filePath = request.getFilesMap().get(key);
//						Log.e("xx","fileName" + fileName + "@" + filePath);
						multipart.addFilePart(request,fileName, new File(filePath));// TODO
					}
					
					if(request.isCanceled()){
						//TODO
						
						getHandler().post(new Runnable() {
							@Override
							public void run() {
								//TODO
								request.getListener().onError(request, -1, "canceled");
							}
						});
//						Log.e("xx","canceld");
						return;
					}
					
					List<String> response = null;
					response = multipart.finish();

//					Log.e("xx","response=" + response);
//					System.out.println("SERVER REPLIED:");
					final StringBuilder sb = new StringBuilder();
					for (String line : response) {
						sb.append(line);
					}
					Parser<T, String> parser = getParserForRequest(request,
							String.class);
//					Log.e("xx","server response:" + sb.toString());
					try {
						T result = null;
						try {
							result = parser.parse(request, sb.toString());
//							Log.e("xx","result=" + result);
						} catch (Exception e) {
							e.printStackTrace();
//							Log.e("xx","response exxx=" + e);
						}
						final T resultCopy = result;
						getHandler().post(new Runnable() {
							@Override
							public void run() {
								request.getListener().onResult(request, resultCopy);
							}
						});
					} catch (Exception je) {
						request.setException(je);
						getHandler().post(new Runnable() {
							@Override
							public void run() {
								//TODO
								request.getListener().onError(request, 404, sb.toString());
							}
						});
//						Log.e("xx","exception je=" + je);
					}

				} catch (final IOException e) {
					e.printStackTrace();
//					Log.e("xx","ioe je=" + e);
					getHandler().post(new Runnable() {
						@Override
						public void run() {
							//TODO
							request.getListener().onError(request, 404, e.toString());
						}
					});
				}
			}
		});
	}


	/** 从数据库里取cookie */
	public static String getCoookieFromDB() {
		String cookiestr = "";

		List<CookieModel> cookiemodels = cookiedao.listAvailable();
		for (CookieModel cookiemodel : cookiemodels) {
			String name = cookiemodel.getName();
			String value = cookiemodel.getValue();
			cookiestr += name + "=" + value + ";";
		}
		return cookiestr;
	}

	/**
	 * This utility class provides an abstraction layer for sending multipart
	 * HTTP POST requests to a web server.
	 * 
	 * @author www.codejava.net
	 *
	 */
	public class MultipartUtility {
		private final String boundary;
		private static final String LINE_FEED = "\r\n";
		private HttpURLConnection httpConn;
		private String charset;
		private OutputStream outputStream;
		private PrintWriter writer;

		/**
		 * This constructor initializes a new HTTP POST request with content
		 * type is set to multipart/form-data
		 * 
		 * @param requestURL
		 * @param charset
		 * @throws IOException
		 */
		public MultipartUtility(String requestURL, String charset)
				throws IOException {
			this.charset = charset;

			// creates a unique boundary based on time stamp
			boundary = "===" + System.currentTimeMillis() + "===";

			URL url = new URL(requestURL);
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setUseCaches(false);
			httpConn.setDoOutput(true); // indicates POST method
			httpConn.setDoInput(true);
			httpConn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + boundary);
			httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
			httpConn.setRequestProperty("Cookie", getCoookieFromDB());
			System.out.println("=============urlhttp");
			outputStream = httpConn.getOutputStream();
			writer = new PrintWriter(new OutputStreamWriter(outputStream,
					charset), true);
		}

		/**
		 * Adds a form field to the request
		 * 
		 * @param name
		 *            field name
		 * @param value
		 *            field value
		 */
		public void addFormField(String name, String value) {
			writer.append("--" + boundary).append(LINE_FEED);
			writer.append(
					"Content-Disposition: form-data; name=\"" + name + "\"")
					.append(LINE_FEED);
			writer.append("Content-Type: text/plain; charset=" + charset)
					.append(LINE_FEED);
			writer.append(LINE_FEED);
			writer.append(value).append(LINE_FEED);
			writer.flush();
		}

		/**
		 * Adds a upload file section to the request
		 * 
		 * @param fieldName
		 *            name attribute in <input type="file" name="..." />
		 * @param uploadFile
		 *            a File to be uploaded
		 * @throws IOException
		 */
		public void addFilePart(AbstractRequest<?> req,String fieldName, File uploadFile)
				throws IOException {
			String fileName = null;
//			if(fieldName.equals("image") || fieldName.equals("avatar")){
//				fileName = "ms.jpg";// TODO
//			}else{
//				fileName = "ms.mp3";
//			}
			String path = uploadFile.getAbsolutePath();
			
			String end = path.substring(path.lastIndexOf(".") + 1,
					path.length()).toLowerCase(Locale.ENGLISH);
			fileName = "ms." + end;
			
//			Log.e("xx","before line feed");
			writer.append("--" + boundary).append(LINE_FEED);
			writer.append(
					"Content-Disposition: form-data; name=\"" + fieldName
							+ "\"; filename=\"" + fileName + "\"").append(
					LINE_FEED);
			writer.append("Content-Type: " + "application/octet-stream")
					.append(LINE_FEED);

			writer.append("Content-Transfer-Encoding: binary")
					.append(LINE_FEED);
			writer.append(LINE_FEED);
			writer.flush();
//			Log.e("xx","after flush");

			FileInputStream inputStream = new FileInputStream(uploadFile);
			byte[] buffer = new byte[1024];
			int bytesRead = -1;
//			Log.e("xx","before read");
			while (!req.isCanceled() && (bytesRead = inputStream.read(buffer)) >= 0) {
				outputStream.write(buffer, 0, bytesRead);
				req.notifyProgress(bytesRead);
//				Log.e("xx","written" + bytesRead);
//				if(DebugI)
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}//TODO
			}
			outputStream.flush();
			inputStream.close();

			writer.append(LINE_FEED);
			writer.flush();
		}

		/**
		 * Adds a header field to the request.
		 * 
		 * @param name
		 *            - name of the header field
		 * @param value
		 *            - value of the header field
		 */
		public void addHeaderField(String name, String value) {
			writer.append(name + ": " + value).append(LINE_FEED);
			writer.flush();
		}

		public void writeHeader() {
			writer.append(getCoookieFromDB()).append(LINE_FEED);
			writer.flush();
		}

		/**
		 * Completes the request and receives response from the server.
		 * 
		 * @return a list of Strings as response in case the server returned
		 *         status OK, otherwise an exception is thrown.
		 * @throws IOException
		 */
		public List<String> finish() throws IOException {
			List<String> response = new ArrayList<String>();

			writer.append(LINE_FEED).flush();
			writer.append("--" + boundary + "--").append(LINE_FEED);
			writer.close();

			// checks server's status code first
			int status = httpConn.getResponseCode();
			if (status == HttpURLConnection.HTTP_OK) {
				
				CookieSpec cookiespec = new BrowserCompatSpec();
				CookieOrigin origin = new CookieOrigin(ConstantProvider.getInstance().setCookieOrigin(), 80, "/", false);
				List<Cookie> cookies = new ArrayList<Cookie>();
				Header header = new BasicHeader("Set-Cookie", httpConn.getHeaderField("Set-Cookie"));
				try {
					cookies = cookiespec.parse(header, origin);
					if (!cookies.isEmpty()) {
						 cookiedao.saveCookies((ArrayList<Cookie>) cookies);
					}
				} catch (Exception e) {
					// TODO
				}
				
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(httpConn.getInputStream()));
				String line = null;
				while ((line = reader.readLine()) != null) {
					response.add(line);
				}
				reader.close();
				httpConn.disconnect();
			} else {
				throw new IOException("Server returned non-OK status: "
						+ status);
			}

			return response;
		}
	}

}
