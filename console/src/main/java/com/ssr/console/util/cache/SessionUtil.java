package com.ssr.console.util.cache;

import javax.servlet.http.HttpServletRequest;

import com.ssr.base.util.SystemConstants;

public class SessionUtil {

	
	/**
	 * 根据SESSION值获取设备所属用户
	 * @param req
	 * @return
	 */
	public static int getCustomerId(HttpServletRequest req){
		return (int) req.getSession().getAttribute(SystemConstants.SESSION_CUSTOMER);
	}
	
}
