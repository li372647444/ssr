package com.ssr.base.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ssr.base.util.AppInvokeStatusCode;
import com.ssr.base.util.SystemConstants;

public class AppSessionInterceptor implements HandlerInterceptor {
	private long serviceStartTime;
    private long serviceEndTime;
    private Logger logger = Logger.getLogger(getClass());

    /**
     * 根据需要过滤请求:
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     * @throws Exception
     */
	public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object o) throws Exception {

    	if (logger.isDebugEnabled()){
            serviceStartTime = System.currentTimeMillis();
        }
        
        Object user  = request.getSession().getAttribute(SystemConstants.SESSION_USER);
        if(user != null){
        	return true;
        }
        else{
        	throw new Exception(AppInvokeStatusCode.NOLOGING_ERROR);
        }
    }

    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object o, Exception e) throws Exception {

        if (logger.isDebugEnabled()){
            StringBuffer sb = new StringBuffer("调用资源:");
            sb.append(request.getRequestURI());
            sb.append(";方法:" + request.getMethod());
            serviceEndTime = System.currentTimeMillis();
            sb.append(";耗费毫秒数:" + (serviceEndTime - serviceStartTime));
            logger.debug(sb.toString());
        }
    }
}
