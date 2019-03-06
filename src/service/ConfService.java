
package service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.dialect.Props;
import cn.hutool.system.SystemUtil;
import gui.panel.BackupPanel;
import gui.panel.ConfigPanel;
import util.HttpRequester;
import util.LogPrintStream;
import util.Progress;

public class ConfService {
	File configFile = null;
	File backupAllInfoFile = null;
	public File backupDir = null;
	File backupConfigFile = null;
	

//	Props props = null;
	
	
	 
	public static void main(String[] args) throws Exception {
		LogPrintStream.init();
		new ConfService().backupAllInfo();
		new ConfService().backupConfig();
		new ConfService().backupPictures(null);
		new ConfService().backupVideos(null);
		

	}

	
	public ConfService() {
		String folder= SystemUtil.get(SystemUtil.USER_DIR);
		configFile = new File(folder,"config.properties");
		backupAllInfoFile = new File(folder,"backup/allinfo.json");
		backupConfigFile = new File(folder,"backup/config.json");
		
		backupDir  = new File(SystemUtil.get(SystemUtil.USER_DIR),"backup");
		
		if(!configFile.exists())
			FileUtil.touch(configFile);
		if(!backupAllInfoFile.exists())
			FileUtil.touch(backupAllInfoFile);
		if(!backupConfigFile.exists())
			FileUtil.touch(backupConfigFile);
		
		
		
	}
	
	public boolean reset() {
		String ip = getIp();
		String port = getPort();
		String context = getContext();
		String password = getPassword();
		String home = StrUtil.format("http://{}:{}/{}/", ip,port,context); ;
		String url = home+"recover/reset";
        
		System.out.println(url);
		try {
			Map<String,Object> map = new HashMap<>();
			map.put("password", password);
			String html = HttpUtil.get(url,map);

			return true;
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(ConfigPanel.instance, StrUtil.format("配置信息所指定的地址 ： {} 无法被访问到",home));
			return false;
		}		
	}

	private void store(Props props) {
		props.store(configFile.getAbsolutePath());
	}
	
	public boolean check(String password) {
		String ip = getIp();
		String port = getPort();
		String context = getContext();
		
		return check(ip,port,context,password);
	}
	
	
	
	
	public boolean check(String ip, String port, String context, String password) {
		
		String home = StrUtil.format("http://{}:{}/{}/", ip,port,context); ;
		String url = home+"backup/valid";
        
		try {
			Map<String,Object> map = new HashMap<>();
			map.put("password", password);
			String html = HttpUtil.get(url,map);
			System.out.println(html);

			if("true".equals(html)) {
				return true;
			}
			else {
				JOptionPane.showMessageDialog(ConfigPanel.instance, "后台管理密码不对");
				return false;
			}
			
	        
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(ConfigPanel.instance, StrUtil.format("配置信息所指定的地址 ： {} 无法被访问到",home));
			return false;
		}
	}
	
	
	public void update(String ip, String port, String context, String password) {
		Props props = new Props(configFile);
    	props.setProperty("ip", ip);
    	props.setProperty("port", port);
    	props.setProperty("context", context);
    	props.setProperty("password", password);
    	store(props);
	}
	
	public String getIp() {
		Props props = new Props(configFile);
		return props.getProperty("ip","127.0.0.1");
	}
	public String getPort() {
		Props props = new Props(configFile);
		return props.getProperty("port","8080");
	}
	public String getContext() {
		Props props = new Props(configFile);
		return props.getProperty("context","mybaby");
	}
	public String getPassword() {
		Props props = new Props(configFile);
		return props.getProperty("password","admin");
	}

	public boolean check() {
		String ip = getIp();
		String port = getPort();
		String context = getContext();
		String password = getPassword();
		return check(ip, port, context, password);
	}
	
	

	
	public String getServerAllInfo() {
		String ip = getIp();
		String port = getPort();
		String context = getContext();
		String password = getPassword();
	
		String home = StrUtil.format("http://{}:{}/{}/", ip,port,context); ;
		String url = home+"backup/allinfo";
		String json = null;
		
		Map<String,Object> map = new HashMap<>();
		map.put("password", password);
		json = HttpUtil.get(url,map);
		json = JSONUtil.formatJsonStr(json);
		return json;
	}
	public String getServerConfig() {
		String ip = getIp();
		String port = getPort();
		String context = getContext();
		String password = getPassword();
		
		String home = StrUtil.format("http://{}:{}/{}/", ip,port,context); ;
		String url = home+"backup/config";
		String json = null;
		
		Map<String,Object> map = new HashMap<>();
		map.put("password", password);
		json = HttpUtil.get(url,map);
		json = JSONUtil.formatJsonStr(json);
		return json;
	}
	
	
	
	
	public boolean backupAllInfo() {
		try {
			String json = getServerAllInfo();
			FileUtil.writeString(json, backupAllInfoFile, "utf-8");
			
			JSONArray array = JSONUtil.parseArray(json);
			//calc total
			for (int i = 0; i < array.size(); i++) {
	 			Object o = array.get(i);
	 			JSONObject record = (JSONObject) o;
	 			long createDate = record.getLong("createDate");
	 			String date = DateUtil.date(createDate).toDateStr();
	 			File recordFile = new File(backupDir+"/" + date, "record.txt");
	 			
	 			String title = record.getStr("title");
	 			String text = record.getStr("text");
	 			
	 			StringBuffer sb = new StringBuffer();
	 			sb.append(title);
	 			sb.append("\r\n");
	 			sb.append(text);
	 			
	 			recordFile.getParentFile().mkdirs();
	 			FileUtil.writeString(sb.toString(), recordFile, "utf-8");
			}
			
			return true;
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(BackupPanel.instance, "获取记录信息出错："+ e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	public boolean backupConfig() {
		try {
			String json = getServerConfig();
			FileUtil.writeString(json, backupConfigFile, "utf-8");
			return true;
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(BackupPanel.instance, "获取配置信息出错："+ e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	
	public String getLocalAllInfo() {
		if(!backupAllInfoFile.exists() || 0==backupAllInfoFile.length()) {
			return "[]";
		}
		String json = FileUtil.readString(backupAllInfoFile, "utf-8");			
		if(StrUtil.isEmpty(json)) {
			return "[]";
		}
		return json;
	}
	
	public String getLocalConfig() {
		if(!backupConfigFile.exists() || 0==backupConfigFile.length()) {
			return "[]";
		}
		String json = FileUtil.readString(backupConfigFile, "utf-8");			
		if(StrUtil.isEmpty(json)) {
			return "[]";
		}
		return json;
	}
	
	public int getLocalRecordSize() {
		String json =getLocalAllInfo();
		JSONArray array = JSONUtil.parseArray(json);
		return array.size();
	}
	public int getServerRecordSize() {
		String json =getServerAllInfo();
		JSONArray array = JSONUtil.parseArray(json);
		return array.size();
	}

	public void backupPictures(Progress progress) throws Exception {

		int total = 0;
		int count = 0;
		String json = FileUtil.readString(backupAllInfoFile, "utf-8");
		
		JSONArray array = JSONUtil.parseArray(json);

		//calc total
		for (int i = 0; i < array.size(); i++) {
 			Object o = array.get(i);
 			JSONObject record = (JSONObject) o;
 			JSONArray pictures= (JSONArray) record.get("pictures");
// 			System.out.println(pictures.size());
 			total+=pictures.size();
		}
		
		

		//download each
		for (int i = 0; i < array.size(); i++) {
 			Object o = array.get(i);
 			JSONObject record = (JSONObject) o;
 			long createDate = record.getLong("createDate");
 			String date = DateUtil.date(createDate).toDateStr();
 			JSONArray pictures= (JSONArray) record.get("pictures");
 			for (int j = 0; j < pictures.size(); j++) {
 				Object p = pictures.get(j);
 	 			JSONObject picture = (JSONObject) p;
 	 			
 	 			String md5= picture.getStr("md5");
 	 			long size= picture.getLong("size");
 	 			
 	 			String ip = getIp();
 	 			String port = getPort();
 	 			String context = getContext();
 	 			String password = getPassword();
 	 			
 	 			
 	 		
 	 			String url = StrUtil.format("http://{}:{}/{}/uploaded/picture/{}.jpg",ip,port,context,picture.get("id")); ;
 	 			
 	 			Map<String,Object> param = new HashMap();
 	 			param.put("password", password);
 	 			File pictureFile = new File(backupDir+"/"+date,picture.get("id")+".jpg");
 	 			System.out.println(pictureFile);
 	 			if(
 	 						pictureFile.exists()
 	 					&&	md5.equals(SecureUtil.md5(pictureFile))
 	 					) {
	 				//已经存在，并且和服务器上一样
 	 				System.out.println("已经存在，和服务器上一样" + picture.get("id"));
 	 			}
 	 			else {
 	 				System.out.println("不存在，或者和服务器上md5不匹配，进行下载" + picture.get("id"));
 	 	 			download(param,url,pictureFile);
 	 			}
 	 			count++;
 	 			int per = count*100/total;
 	 			if(null!=progress)
 	 				progress.progress(per);
// 	 			ThreadUtil.sleep(1000);
// 	 			break;
 	 			
			}
// 			break;
		}
		
		clearPictures();
		
	}

	private void clearPictures() {
		// TODO Auto-generated method stub
//		backupPictureFolder
	}


	private void download(Map<String, Object> param, String url, File file) throws Exception {
		
		try {
			file.getParentFile().mkdirs();
			HttpRequest request  = HttpRequest.get(url);
			request.form(param);
			System.out.println(url);
			HttpResponse resposne= request.execute();
			System.out.println(1);
			try(
				InputStream is = resposne.bodyStream();
//				System.out.println(3);
				
				OutputStream os = new FileOutputStream(file);
//				System.out.println(4);
			){
				IoUtil.copy(is, os);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void backupVideos(Progress progress) throws Exception {
		TimeInterval timer = DateUtil.timer();
		
		int total = 0;
		int count = 0;
		String json = FileUtil.readString(backupAllInfoFile, "utf-8");
		JSONArray array = JSONUtil.parseArray(json);
		//calc total
		for (int i = 0; i < array.size(); i++) {
 			Object o = array.get(i);
 			JSONObject record = (JSONObject) o;
 			
 			long createDate = record.getLong("createDate");
 			String date = DateUtil.date(createDate).toDateStr();
 			
 			JSONArray videos= (JSONArray) record.get("videos");
// 			System.out.println(pictures.size());
 			total+=videos.size();
		}
		
//		FileUtil.del(backupVideoFolder);

		
		//download each
		for (int i = 0; i < array.size(); i++) {
 			Object o = array.get(i);
 			JSONObject record = (JSONObject) o;
 			
 			
 			
 			long createDate = record.getLong("createDate");
 			String date = DateUtil.date(createDate).toDateStr();

 			JSONArray videos= (JSONArray) record.get("videos");
 			for (int j = 0; j < videos.size(); j++) {
 				Object v = videos.get(j);
 	 			JSONObject video = (JSONObject) v;
 	 			

 	 			
 	 			String md5= video.getStr("md5");
 	 			long size= video.getLong("size");

 	 			String ip = getIp();
 	 			String port = getPort();
 	 			String context = getContext();
 	 			String password = getPassword();
 	 		
 	 			String url = StrUtil.format("http://{}:{}/{}/uploaded/video/{}.mp4",ip,port,context,video.get("id")); ;

 	 			Map<String,Object> param = new HashMap();
 	 			param.put("password", password);
 	 			File videoFile = new File(backupDir+"/"+date,video.get("id")+".mp4");

 	 			if(
 	 					videoFile.exists()
	 					&&	md5.equals(SecureUtil.md5(videoFile))
	 					) {
	 				//已经存在，并且和服务器上一样
 	 				System.out.println("已经存在，和服务器上一样" + video.get("id"));
	 			}
	 			else {
 	 				System.out.println("不存在，或者和服务器上md5不匹配，进行下载" + video.get("id"));

	 	 			download(param,url,videoFile);
	 			}

 	 			count++;
 	 			int per = count*100/total;
 	 			if(null!=progress)
 	 				progress.progress(per);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void recover(Progress postProgress, Progress pictureProgress, Progress videoProgress) {
		String ip = getIp();
		String port = getPort();
		String context = getContext();
		String home = StrUtil.format("http://{}:{}/{}/", ip,port,context);
		
		System.out.println("home:"+home);
		
		String postUrl = home+"recover/post";
		String configUrl = home+"recover/config";
		String allPost_json = getLocalAllInfo();
		String config_json = getLocalConfig();
		
		
		
		int postTotal=0;
		int pictureTotal=0;
		int videoTotal=0;
		
		int postCount=0;
		int pictureCount=0;
		int videoCount=0;

		JSONArray posts = JSONUtil.parseArray(allPost_json);
		postTotal = posts.size();
		
		JSONArray configs = JSONUtil.parseArray(config_json);
		for (Object o : configs) {
			JSONObject config = (JSONObject) o;
			HttpUtil.post(configUrl,config.toString());
		}
		
		for (Object o: posts) {
			JSONObject post = (JSONObject) o;
			JSONArray pictures = post.getJSONArray("pictures");
			pictureTotal+=pictures.size();
			JSONArray videos = post.getJSONArray("videos");
			videoTotal+=videos.size();
		}
		
		
		for (Object o: posts) {
			JSONObject record = (JSONObject) o;
			String result = HttpUtil.post(postUrl,record.toString());


			JSONObject object = JSONUtil.parseObj(result);

			String pid = object.get("id").toString();

			postCount++;
			if(null!=postProgress)
			postProgress.progress(postCount*100/postTotal);

 			long createDate = record.getLong("createDate");
 			String date = DateUtil.date(createDate).toDateStr();

 			
			JSONArray pictures = record.getJSONArray("pictures");

			for (int i = 0; i < pictures.size(); i++) {
//				if(true)
//					continue;
				JSONObject picture = (JSONObject) pictures.get(i);
 	 			File pictureFile = new File(backupDir+"/"+date,picture.get("id")+".jpg");
 	 			if(0!=pictureFile.length()) {

 	 				String pictureUrl = home+"recover/picture";
 	 				
 	 				Map<String, Object> paramMap= new HashMap<>();
 	 				paramMap.put("image", pictureFile);
 	 				paramMap.put("pid", pid);
 	 				paramMap.put("name",  picture.getStr("name"));
 	 				paramMap.put("index", picture.getStr("index"));
 	 				paramMap.put("status", picture.getStr("status"));
 	 				paramMap.put("createDate", picture.getLong("createDate"));
 	 				paramMap.put("image", pictureFile);
 	 				
					String html= HttpUtil.post(pictureUrl, paramMap);
 	 			}
 	 			
 	 			pictureCount++;
 	 			if(null!=pictureProgress)
 				pictureProgress.progress(pictureCount*100/pictureTotal);

			}
			JSONArray videos = record.getJSONArray("videos");
			for (int i = 0; i < videos.size(); i++) {
				JSONObject video = (JSONObject) videos.get(i);

				File videoFile = new File(backupDir+"/"+date,video.get("id")+".mp4");
				if(0!=videoFile.length()) {
					
					String videoUrl = home+"recover/video";
					
					
					final Map<String, String> paramMap= new HashMap<>();
					final Map<String, File> fileMap= new HashMap<>();
					fileMap.put("image", videoFile);
					paramMap.put("pid", pid);
					paramMap.put("name",  video.getStr("name"));
					paramMap.put("index",  video.getStr("index"));
					paramMap.put("status", video.getStr("status"));
					paramMap.put("createDate", String.valueOf(video.getLong("createDate")));
					

					HttpRequester.post(videoUrl, paramMap, fileMap);
					
//					System.out.println("v1:"+videoFile);
//					if(videoFile.getName().endsWith("83.mp4")) {
//						System.out.println(videoUrl);
//						
//						String filepath = "E:/project/mybaby_tool/backup/video/83.mp4";
//						videoFile = new File(filepath);
//						final Map paramMap= new HashMap<>();
//						final Map<String, File> fileMap= new HashMap<>();
//						
////						http://127.0.0.1:8080/mybaby/recover/video
//
//						
//						paramMap.put("pid", "531");
//						paramMap.put("name",  "name");
//						paramMap.put("index",  "1");
//						paramMap.put("status", "normal");
//						paramMap.put("createDate", "0");
////						paramMap.put("image", videoFile);
//						
//						fileMap.put("image", videoFile);
//						
//						System.out.println(Thread.currentThread().getName());
//						
//						
//						HttpRequester.post(videoUrl, paramMap, fileMap);
//						
//						new Thread() {
//							public void run() {
//								System.out.println(111);
////								InputStream is =HttpRequester.post(videoUrl, paramMap, fileMap);
//							}
//						}.start();
//						
//						
////						String html= HttpUtil.post(videoUrl, paramMap);
////						System.out.println(html);
//						
//					}
//					System.out.println("v2:"+videoFile);
//
//				}
				videoCount++;
				if(null!=videoProgress)
 				videoProgress.progress(videoCount*100/videoTotal);
			}
		}
		
		}
	}
	
	
     
}