package com.ssr.base.mongodb;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.ssr.base.util.Formater;
import com.ssr.base.util.SystemConfig;

public class MongodbUtil {
	
	private static Logger logger = Logger.getLogger(MongodbUtil.class);
	
	private static MongoClient mongoClient;
	
	/**
	 * 创建单例的mongodb client
	 */
	public static synchronized void buildJedisPool(){
		String ip = SystemConfig.getValueByKey("mongodb.ip", "127.0.0.1");
		int port = Integer.parseInt(SystemConfig.getValueByKey("mongodb.port", "27017"));
		mongoClient = new MongoClient(new ServerAddress(ip, port), 
				new MongoClientOptions.Builder().build());
		logger.debug("init mongodb client!");
	}
	
	/**
	 * 获取database
	 * @param databaseName
	 * @return
	 */
	public static MongoDatabase getDatabase(String databaseName){
		if(mongoClient == null){
			buildJedisPool();
		}
		logger.debug("get mongodb database with name=" + databaseName);
		return mongoClient.getDatabase(databaseName);
	}
	
	/**
	 * 创建对应的数据集合(表)
	 * @param databaseName
	 * @param collectionName
	 */
	public static synchronized void createCollection(String databaseName, String collectionName){
		MongoDatabase db = getDatabase(databaseName);
		if(db.getCollection(collectionName) == null){
			db.createCollection(collectionName);
		}
	}
	
	/**
	 * 删除对应的数据集合(表)
	 * @param databaseName
	 * @param collectionName
	 */
	public static synchronized void dropCollection(String databaseName, String collectionName){
		MongoDatabase db = getDatabase(databaseName);
		if(db.getCollection(collectionName) == null){
			db.getCollection(collectionName).drop();
		}
	}
	
	public static void main(String[] args) {
		MongoClient mongoClient = new MongoClient(new ServerAddress("192.168.160.225", 27017), 
				new MongoClientOptions.Builder().build());
		MongoDatabase db = mongoClient.getDatabase("cmcu");
		if(db.getCollection("d1") == null){
			db.createCollection("d1");
		}
		else{
			System.out.println(db.getCollection("d1").toString());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userCode", "cmcu");
		map.put("systemcode", "sys1");
		map.put("code", "d1");
		map.put("name", "电梯设备");
		map.put("value", "1");
		map.put("valueTime", System.currentTimeMillis());
		db.getCollection("d1").insertOne(new Document(map));
		
		FindIterable<Document> iterable = db.getCollection("d1").find(
				Filters.gte("valueTime", 1472180410589l)).sort(Sorts.descending("_id")).limit(10);
		iterable.forEach(new Block<Document>(){
			@Override
			public void apply(Document d) {
				System.out.println(d + Formater.timeStampToDateStr((long)d.get("valueTime")));
			}
		});
		
		mongoClient.close();
	}
}
