package test;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.io.IoUtil;

public class HttpRequester {
	private static final String BOUNDARY = "-------45962402127348";
	private static final String FILE_ENCTYPE = "multipart/form-data";

	public static void main(String[] args) {
//		test1();
		String filepath = "E:/project/mybaby_tool/backup/video/83.mp4";
		File videoFile = new File(filepath);
		Map fileMap  =new HashMap();
		Map paramMap  =new HashMap();
		
//		http://127.0.0.1:8080/mybaby/recover/video

		
		paramMap.put("pid", "531");
		paramMap.put("name",  "name");
		paramMap.put("index",  "1");
		paramMap.put("status", "normal");
		paramMap.put("createDate", "0");
		fileMap.put("image", videoFile);
		
		String urlStr = "http://127.0.0.1:8080/upload";
		urlStr= "http://127.0.0.1:8080/mybaby/recover/video";

		

		InputStream is = post(urlStr, paramMap, fileMap);
		String html =  IoUtil.read(is,"utf-8");
		System.out.println(html);
	}

	public static void test1() {
		String filepath = "E:/project/mybaby_tool/backup/video/83.mp4";
		String urlStr = "http://127.0.0.1:8080/upload";
		Map<String, String> textMap = new HashMap<String, String>();
		textMap.put("name", "xxyyzz");
		Map<String, File> fileMap = new HashMap<String, File>();
		fileMap.put("file", new File(filepath));
		InputStream is = post(urlStr, textMap, fileMap);
		String html =  IoUtil.read(is,"utf-8");
		System.out.println(html);
	}

	/**
	 * 
	 * @param urlStr
	 *            http请求路径
	 * @param params
	 *            请求参数
	 * @param images
	 *            上传文件
	 * @return
	 */
	public static InputStream post(String urlStr, Map<String, String> params, Map<String, File> images) {
		InputStream is = null;

		try {
			URL url = new URL(urlStr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setConnectTimeout(5000);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type", FILE_ENCTYPE + "; boundary=" + BOUNDARY);

			StringBuilder sb = null;
			DataOutputStream dos = new DataOutputStream(con.getOutputStream());
			;
			if (params != null) {
				sb = new StringBuilder();
				for (String s : params.keySet()) {
					sb.append("--");
					sb.append(BOUNDARY);
					sb.append("\r\n");
					sb.append("Content-Disposition: form-data; name=\"");
					sb.append(s);
					sb.append("\"\r\n\r\n");
					sb.append(params.get(s));
					sb.append("\r\n");
				}

				dos.write(sb.toString().getBytes());
			}

			if (images != null) {
				for (String s : images.keySet()) {
					File f = images.get(s);
					sb = new StringBuilder();
					sb.append("--");
					sb.append(BOUNDARY);
					sb.append("\r\n");
					sb.append("Content-Disposition: form-data; name=\"");
					sb.append(s);
					sb.append("\"; filename=\"");
					sb.append(f.getName());
					sb.append("\"\r\n");
					sb.append("Content-Type: application/zip");// 这里注意！如果上传的不是图片，要在这里改文件格式，比如txt文件，这里应该是text/plain
					sb.append("\r\n\r\n");
					dos.write(sb.toString().getBytes());

					FileInputStream fis = new FileInputStream(f);
					byte[] buffer = new byte[1024];
					int len;
					while ((len = fis.read(buffer)) != -1) {
						dos.write(buffer, 0, len);
					}
					dos.write("\r\n".getBytes());
					fis.close();
				}

				sb = new StringBuilder();
				sb.append("--");
				sb.append(BOUNDARY);
				sb.append("--\r\n");
				dos.write(sb.toString().getBytes());
			}
			dos.flush();

			if (con.getResponseCode() == 200)
				is = con.getInputStream();

			dos.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}
}