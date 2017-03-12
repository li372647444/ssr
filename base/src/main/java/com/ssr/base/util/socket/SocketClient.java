package com.ssr.base.util.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketClient {
	
    /**
     * 服务器端地址
     */
    private InetSocketAddress serverAddress;
    
    /**
     * 连接Channel
     */
    private SocketChannel clientChannel;

    /**
     * 指令信息
     */
    private String sendMessage;

    /**
     * 返回结果
     */
    private String receiveMessage;

    /**
     * @param ip   服务端IP
     * @param port 服务端端口
     */
    public SocketClient(String ip, int port) {
    	serverAddress = new InetSocketAddress(ip, port);
    }

    /**
     * @param ip      服务端IP
     * @param port    服务端端口
     * @param message 指令
     */
    public SocketClient(String ip, int port, String message) {
    	serverAddress = new InetSocketAddress(ip, port);
        sendMessage = message;
    }

    /**
     * 设置指令
     * @param sendMessage
     */
    public void setSendMessage(String sendMessage) {
        this.sendMessage = sendMessage;
    }

    /**
     * 执行发送指令
     * 只执行一次
     *
     * @return
     */
    public String executeOnce() {
        SocketChannel client = null;
        try {
            //连接服务器
            client = SocketChannel.open();
            client.connect(serverAddress);
            //发送请求
            sendDataOnce(client);
            //接收响应
            receiveDataOnce(client);
            return receiveMessage;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }
    
    /**
     * 发送指令
     * 可多次发送
     * 
     * @param data
     * @return
     */
    public byte[] execute(byte[] data) 
    {
    	byte[] re = null;
    	try {
			if(clientChannel == null){
				//连接服务器
				clientChannel = SocketChannel.open();
				clientChannel.connect(serverAddress);
			}
			//发送数据
			sendDate(clientChannel, data);
			//接收数据
			re = receiveData(clientChannel);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
    	return re;
    }

    /**
     * 发送指令
     *
     * @param client
     * @throws IOException
     */
    private void sendDataOnce(SocketChannel client) throws IOException {
        ByteBuffer writeBuffer = ByteBuffer.wrap(sendMessage.getBytes("UTF-8"));
        client.write(writeBuffer);
    }
    
    /**
     * 发送指令
     * 
     * @param client
     * @param data
     * @throws IOException
     */
    private void sendDate(SocketChannel client, byte[] data) throws IOException {
    	ByteBuffer writeBuffer = ByteBuffer.wrap(data);
        client.write(writeBuffer);
    }

    /**
     * 返回结果
     *
     * @param client
     * @throws IOException
     */
    private void receiveDataOnce(SocketChannel client) throws IOException {
        int count;
        ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
        if ((count = client.read(buffer)) >= 0) {
        	buffer.flip();
        	byte[] bytes;
        	bytes = new byte[count];
        	buffer.get(bytes);
        	buffer.clear();
            receiveMessage = new String(bytes);
        }
    }
    
    private byte[] receiveData(SocketChannel client) throws IOException {
    	int count;
    	byte[] re = null;
    	ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
        if ((count = client.read(buffer)) >= 0) {
        	buffer.flip();
        	byte[] bytes;
        	bytes = new byte[count];
        	buffer.get(bytes);
        	buffer.clear();
        	re = bytes;
        }
        
        return re;
    }
    
    public void close(){
    	if(clientChannel != null){
    		try {
				clientChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    
}

