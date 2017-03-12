package com.ssr.base.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {
	
	private final static String QUEUE_NAME = "data_collection";

	public static void main(String[] args) throws Exception {
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.160.225");
		factory.setPort(5672);
		factory.setVirtualHost("cmcu_data");
		factory.setUsername("cmcu_data");
		factory.setPassword("cmcu_data");
		
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		String message = null;
		for(int i = 0;i < 100000;i++){
			message = "Hello World!" + System.currentTimeMillis() + "|" + i;
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			System.out.println(" [x] Sent '" + message + "'");
		}
		
		channel.close();
		connection.close();
	}

}
