package com.ssr.base.web.interceptor;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ssr.base.model.Function;
import com.ssr.base.util.SystemConstants;
import com.ssr.base.util.annotation.Right;

/**
 * Session拦截器,默认处理所有需要登录的资源访问校验
 *
 * @author PengLian
 */
public class SessionInterceptor implements HandlerInterceptor {

    private long serviceStartTime;
    private long serviceEndTime;
    private Logger logger = Logger.getLogger(getClass());

    /**
     * 根据需要过滤请求:
     * 1.登录校验
     * 2.菜单(URL)权限
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object o) throws Exception {

    	if (logger.isDebugEnabled()){
            serviceStartTime = System.currentTimeMillis();
        }
        
        Map<String, Function> map = (Map<String, Function>)request.getSession().getAttribute(SystemConstants.SESSION_FUNCTION);
        if(map != null){
        	if(!(o instanceof HandlerMethod)){
            	return true;
        	}
        	HandlerMethod hm = (HandlerMethod)o;
            Method m = hm.getMethod();
            RequestMapping rp = m.getAnnotation(RequestMapping.class);
            String url = rp.value()[0];
            if(url.equals("/") || url.equals("/index") || url.equals("/main")){
            	return true;
            }
    		Right r = null;
            r = m.getAnnotation(Right.class);
            if(r != null && !map.containsKey(r.id())){
            	request.getRequestDispatcher("/deny").forward(request, response);
            	return false;
            }
            else{
            	return true;
            }
        }
        else{
        	response.sendRedirect(request.getContextPath() + "/login");
        	return false;
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
