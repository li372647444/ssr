package com.ssr.base.util;

/**
 * app ajax请求返回结构封装
 *
 * @author PengLian
 */
public class AppAjaxSupport {
	/**
	 * 接口调用状态
	 */
	private String code;
	/**
	 * 接口调用信息
	 */
	private String message;
	/**
	 * 接口调用返回数据
	 */
	private Object data;
	/**
	 * 时间戳(自动生成)
	 */
	private long time;
	
	/**
	 * 构造方法初始化时间戳
	 */
	public AppAjaxSupport(){
		time = System.currentTimeMillis();
		code = AppInvokeStatusCode.SUCCESS;
		message = "接口访问成功!";
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
