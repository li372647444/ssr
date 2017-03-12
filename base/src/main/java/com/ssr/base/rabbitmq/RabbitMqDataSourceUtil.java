package com.ssr.base.rabbitmq;

import org.apache.log4j.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.ssr.base.util.SystemConfig;

/**
 * rabbitmq基础工具类
 * @author pl
 *
 */
public class RabbitMqDataSourceUtil {
	
	private static Logger logger = Logger.getLogger(RabbitMqDataSourceUtil.class);
	
	public static ConnectionFactory buildConnectionFactory(){
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(SystemConfig.getValueByKey("rabbit.host"));
		factory.setPort(Integer.parseInt(SystemConfig.getValueByKey("rabbit.port")));
		factory.setVirtualHost(SystemConfig.getValueByKey("rabbit.virtualHost"));
		factory.setUsername(SystemConfig.getValueByKey("rabbit.username"));
		factory.setPassword(SystemConfig.getValueByKey("rabbit.password"));
		return factory;
	}
	
	public static ConnectionFactory buildConnectionFactory(String virtualHost, String username, String password){
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(SystemConfig.getValueByKey("rabbit.host"));
		factory.setPort(Integer.parseInt(SystemConfig.getValueByKey("rabbit.port")));
		factory.setVirtualHost(virtualHost);
		factory.setUsername(username);
		factory.setPassword(password);
		return factory;
	}
	
	public static Connection buildConnection(){
		try {
			return buildConnectionFactory().newConnection();
		} catch (Exception e) {
			logger.error("creat rabbitmq connection failed", e);
		}
		return null;
	}
	
	public static Connection buildConnection(String virtualHost, String username, String password){
		try {
			return buildConnectionFactory(virtualHost, username, password).newConnection();
		} catch (Exception e) {
			logger.error("creat rabbitmq connection failed", e);
		}
		return null;
	}
	
	public static Channel buildChannel(){
		try {
			return buildConnection().createChannel();
		} catch (Exception e) {
			logger.error("creat rabbitmq channel failed", e);
		}
		return null;
	}
	
	public static Channel buildChannel(String virtualHost, String username, String password){
		try {
			return buildConnection(virtualHost, username, password).createChannel();
		} catch (Exception e) {
			logger.error("creat rabbitmq channel failed", e);
		}
		return null;
	}
	
	public static void releaseChannelAndConnection(Channel channel){
		if(channel == null){
			return;
		}
		Connection c = channel.getConnection();
		try {
			channel.close();
		} catch (Exception e) {
			logger.error("release channel failed", e);
		}
		try {
			if(c != null){
				c.close();
			}
		} catch (Exception e) {
			logger.error("release connection failed", e);
		}
		channel = null;
		c = null;
	}

}
