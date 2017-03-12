package com.ssr.base.util.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * httpclient工具类
 * @author PengLian
 *
 */
public class HttpClientHandler {
	
	private static Logger logger = Logger.getLogger(HttpClientHandler.class);
	
	/**
	 * 将MAP填充到POST参数中
	 * @param params
	 * @return
	 */
	public static List<NameValuePair> buildNameValuePair(Map<String, String> params) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>(params.size());
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        return nvps;
    }
	
	/**
	 * 发送POST请求
	 * @param params
	 * @return
	 */
	public static String doPost(String url, Map<String, String> params){
		String result = null;
        List<NameValuePair> nvps = buildNameValuePair(params);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        EntityBuilder builder = EntityBuilder.create();
        CloseableHttpResponse response = null;
		try {
			builder.setParameters(nvps);
			httpPost.setEntity(builder.build());
			response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getReasonPhrase().equals("OK")
                    && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
            EntityUtils.consume(entity);
            logger.info("发送POST请求:url=" + url + "|result=" + result);
		} catch (Exception e) {
			result = "error:" + e.getMessage();
			logger.error("发送POST请求出错:url=" + url + "|error=" + e.getMessage(), e);
		} finally {
			releaseHttpClient(httpclient, response);
		}
		return result;
	}
	
	/**
	 * 发送GET请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doGet(String url, Map<String, String> params){
		String result = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String urlWithParams = concatUrl(url, params);
        HttpGet httpGet = new HttpGet(urlWithParams);
        CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getReasonPhrase().equals("OK")
                    && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
            EntityUtils.consume(entity);
            logger.info("发送GET请求:url=" + url + "|result=" + result);
		} catch (Exception e) {
			result = "error:" + e.getMessage();
			logger.error("发送GET请求出错:url=" + url + "|error=" + e.getMessage(), e);
		} finally {
			releaseHttpClient(httpclient, response);
		}
		return result;
	}
	
	/**
	 * 清理HTTP请求资源
	 * @param httpclient
	 * @param response
	 */
	public static void releaseHttpClient(CloseableHttpClient httpclient, CloseableHttpResponse response){
		try {
			if(response != null){
				response.close();
			}
			if(httpclient != null){
				httpclient.close();
			}
		} catch (Exception e) {
			logger.error("释放HTTP资源出错:error=" + e.getMessage());
		}
	}
	
	/**
	 * 拼装HTTP参数(自动过滤掉为空的参数)
	 * @param url
	 * @param params
	 * @return
	 */
	public static String concatUrl(String url, Map<String, String> params) {
		StringBuilder buffer = new StringBuilder(url);
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
            		buffer.append("?");
                    buffer.append(entry.getKey());
                    buffer.append("=");
                    buffer.append(entry.getValue());
            	}
            	else{
            		buffer.append("&");
                    buffer.append(entry.getKey());
                    buffer.append("=");
                    buffer.append(entry.getValue());
            	}
            }
        }
		return buffer.toString();
	}
	
	/**
	 * 从文件中读取需要返回的字符串
	 * @param path
	 * @return
	 */
	public static String readStringFromFile(String path){
		StringBuilder sb = new StringBuilder();
		File file = new File(path);
        BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				sb.append(tempString);
			}
		} catch (Exception e) {
			logger.error("读取文件出错:file=" + path + "|error=" + e.getMessage());
		} finally {
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					logger.error("关闭文件流出错:file=" + path + "|error=" + e.getMessage());
				}
			}
		}
		return sb.toString();
	}
}
