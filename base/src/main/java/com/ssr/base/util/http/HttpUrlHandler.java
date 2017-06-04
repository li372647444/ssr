package com.ssr.base.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * httpUrl工具类
 * 
 * @author 
 *
 */
public class HttpUrlHandler {

	private static Logger logger = Logger.getLogger(HttpUrlHandler.class);
	
	/**
	 * map参数转化为字符串参数
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	public static String mapParamToString(Map<String, String> params) throws Exception{
		StringBuilder buffer = new StringBuilder();
		int i = 0;
		if (params != null) {
			String value = null;
            for (Map.Entry<String, String> entry : params.entrySet()) {
            	value = entry.getValue();
            	if(value == null || "".equals(value)){
            		 continue;
            	}
            	++i;
            	if(i == 1){
                    buffer.append(entry.getKey());
                    buffer.append("=");
                    buffer.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            	}
            	else{
            		buffer.append("&");
                    buffer.append(entry.getKey());
                    buffer.append("=");
                    buffer.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            	}
            }
        }
		return buffer.toString();
	}

	/**
	 * 向指定URL发送GET方法的请求
	 * @param url
	 * @param param
	 * @return
	 */
	public static String sendGet(String url, Map<String, String> params) {
		String result = "";
		BufferedReader in = null;
		String urlNameString = "";
		try {
			urlNameString = url + "?" + mapParamToString(params);
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性,根据需要自行修改
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			
			// 获取所有响应头字段
			//Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			//for (String key : map.keySet()) {
			//	System.out.println(key + "--->" + map.get(key));
			//}
			
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			
			logger.debug("发送GET请求:url=" + urlNameString + "|result=" + result);
		} catch (Exception e) {
			logger.error("发送GET请求出错:url=" + urlNameString + "|error=" + e.getMessage(), e);
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				logger.error("关闭HTTP流出错!", e);
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * @param url
	 * @param param
	 * @return 
	 */
	public static String sendPost(String url, Map<String, String> params) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			// 设置通用的请求属性,根据需要自行修改
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(mapParamToString(params));
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			logger.debug("发送POST请求:url=" + url + "|result=" + result);
		} catch (Exception e) {
			logger.error("发送POST请求出错:url=" + url + "|error=" + e.getMessage(), e);
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				logger.error("关闭HTTP流出错!", e);
			}
		}
		return result;
	}
	
}
