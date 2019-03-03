package test;
 
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
 
public class Test {
	
	public static void main(String[] args) {
		String path = "D:/ScreenCapture/images/detail/7644.jpg";
		String url = "http://127.0.0.1:8080/upload";
	}
	
	
	 public static String doPostWithFile(String url,String savefileName, String param) {
	        String result = "";
	          try {  
	                // 换行符  
	                final String newLine = "\r\n";  
	                final String boundaryPrefix = "--";  
	                File file = new File(savefileName);
	                // 定义数据分隔线  
	                String BOUNDARY = "========7d4a6d158c9";  
	                // 服务器的域名  
	                URL realurl = new URL(url);  
	                // 发送POST请求必须设置如下两行
	                HttpURLConnection connection = (HttpURLConnection) realurl.openConnection(); 
	                connection.setDoOutput(true);
	                connection.setDoInput(true);
	                connection.setUseCaches(false);
	                connection.setRequestMethod("POST");
	                connection.setRequestProperty("Connection","Keep-Alive");
	                connection.setRequestProperty("Charset","UTF-8");
	                connection.setRequestProperty("Content-Type","multipart/form-data; boundary=" + BOUNDARY);
	                OutputStream out =connection.getOutputStream();
	                // 头
	                String boundary = BOUNDARY;
	                // 传输内容
	                StringBuffer contentBody =new StringBuffer("--" + BOUNDARY);
	                // 尾
	                String endBoundary ="\r\n--" + boundary + "--\r\n";

	                // 1. 处理普通表单域(即形如key = value对)的POST请求（这里也可以循环处理多个字段，或直接给json）
	                //这里看过其他的资料，都没有尝试成功是因为下面多给了个Content-Type
	                //form-data  这个是form上传 可以模拟任何类型
	                contentBody.append("\r\n")
	                .append("Content-Disposition: form-data; name=\"")
	                .append("param" + "\"")
	                .append("\r\n")
	                .append("\r\n")
	                .append(param)
	                .append("\r\n")
	                .append("--")
	                .append(boundary);
	                String boundaryMessage1 =contentBody.toString();
	                System.out.println(boundaryMessage1);
	                out.write(boundaryMessage1.getBytes("utf-8"));

	                // 2. 处理file文件的POST请求（多个file可以循环处理）
	                contentBody = new StringBuffer();
	                contentBody.append("\r\n")
	                .append("Content-Disposition:form-data; name=\"")
	                .append("file" +"\"; ")   // form中field的名称
	                .append("filename=\"")
	                .append(file.getName() +"\"")   //上传文件的文件名，包括目录
	                .append("\r\n")
	                .append("Content-Type:multipart/form-data")
	                .append("\r\n\r\n");
	                String boundaryMessage2 = contentBody.toString();
	                System.out.println(boundaryMessage2);
	                out.write(boundaryMessage2.getBytes("utf-8"));

	                // 开始真正向服务器写文件
	                
	                DataInputStream dis= new DataInputStream(new FileInputStream(file));
	                int bytes = 0;
	                byte[] bufferOut =new byte[(int) file.length()];
	                bytes =dis.read(bufferOut);
	                out.write(bufferOut,0, bytes);
	                dis.close();
	                byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();  
	                out.write(endData);  
	                out.flush();  
	                out.close(); 

	                // 4. 从服务器获得回答的内容
	                String strLine="";
	                String strResponse ="";
	                InputStream in =connection.getInputStream();
	                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	                while((strLine =reader.readLine()) != null)
	                {
	                        strResponse +=strLine +"\n";
	                }
	                System.out.print(strResponse);
	                return strResponse;
	            } catch (Exception e) {  
	                System.out.println("发送POST请求出现异常！" + e);  
	                e.printStackTrace();  
	            }
	          return result;
	    }
	

}