package com.ssr.base.rabbitmq;

import java.io.IOException;
import java.util.Arrays;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Receiving {
	
	private final static String QUEUE_NAME = "error_data_collection";

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
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				int s0 = body[0] & 0xFF;
				int s1 = body[1] & 0xFF;
				int s2 = body[2] & 0xFF;
				int s3 = body[3] & 0xFF;
				if(s0 == 0x5A && s1 == 0xA5 && s2 == 0x5A && s3 == 0xA5){
					System.out.println("引导符校验通过!");
				}
				byte len[] = Arrays.copyOfRange(body, 4, 8);
				int length = byteArrayToInt(len);
				System.out.println("校验长度为:" + length);
				
				int command = body[8] & 0xFF;
				System.out.println("命令为:" + command);
				
				byte commandAndDate[] = Arrays.copyOfRange(body, 8, body.length - 4);
				System.out.println("正式长度为:" + commandAndDate.length);
				if(length == commandAndDate.length){
					System.out.println("数据长度校验通过!");
				}
				
				byte checkSum[] = Arrays.copyOfRange(body, body.length - 5, body.length);
				int sum = byteArrayToInt(checkSum);
				System.out.println("校验和为:" + sum);
				
				int check = 0;
				for(int i = 0;i < commandAndDate.length;i++){
					check += commandAndDate[i] & 0xFF;
				}
				System.out.println("真实累加和为:" + check);
				
				if(sum == check){
					System.out.println("数据校验通过!");
				}
				
				byte dataLenByte[] = Arrays.copyOfRange(commandAndDate, 10, 15);
				int dataLen = byteArrayToInt(dataLenByte);
				byte data[] = Arrays.copyOfRange(commandAndDate, 15, dataLen + 15);
				
				String message = new String(data, "UTF-8");
				
				
				System.out.println(" [x] Received '" + message + "'");
			}
		};
		channel.basicConsume(QUEUE_NAME, true, consumer);
		System.out.println("create consumer success!");
	}
	
	public static int byteArrayToInt(byte[] bRefArr){
		int iOutcome = 0;
	    byte bLoop;
	    int l = bRefArr.length;
	    for (int i = 0; i < l; i++) {
	        bLoop = bRefArr[i];
	        iOutcome += (bLoop & 0xFF) << (8 * (l - 1 -i));
	    }
	    return iOutcome;
	}

}
