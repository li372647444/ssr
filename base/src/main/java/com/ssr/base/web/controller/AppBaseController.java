package com.ssr.base.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import com.github.pagehelper.PageInfo;
import com.ssr.base.util.AppAjaxSupport;
import com.ssr.base.util.AppInvokeStatusCode;
import com.ssr.base.util.SystemConfig;
import com.ssr.base.util.conversion.CustomDateTimeEditor;
import com.ssr.base.util.json.JsonToTypeReference;
import com.ssr.base.util.security.Cryption;

/**
 * 所有AppController基类,必须继承
 *
 * @author PengLian
 */
public abstract class AppBaseController {
	
	private Logger logger = Logger.getLogger(getClass());
	
	protected static long TIMEOUT = Long.parseLong(SystemConfig.getValueByKey("app.timeout", "180000"));
	protected static boolean ENCODE = Boolean.parseBoolean(SystemConfig.getValueByKey("app.encode", "true"));
	
	protected static String ERROR_MSG = "errorMsg";
    protected static String IS_AJAX = "isAjax";
    protected static String ERROR_TXT = "访问系统出错,请联系管理员!";
    protected static String ERROR_VIEW = "base/error";
    
    /**
     * 处理日期转换
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(Date.class, new CustomDateTimeEditor(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"), true));
    }
	
    /**
     * 解析APP请求参数
     * 需要解密数据
     * @param request
     * @return
     */
    public Map<String, String> getAppRequestParameterAsMap(HttpServletRequest request) throws Exception{
    	Map<String, String> reMap = new HashMap<String, String>();
    	String data = request.getParameter("data");
    	if(data == null || "".equals(data)){
    		throw new Exception(AppInvokeStatusCode.NO_DATA);
    	}
    	data = Cryption.deCrytorAppData(data);
    	reMap = JsonToTypeReference.jsonToStringMap(data);
    	String str = reMap.get("time");
    	if(str == null || "".equals(str)){
    		throw new Exception(AppInvokeStatusCode.NO_TIME_STAMP);
    	}
    	long time = Long.parseLong(str);
    	long now = System.currentTimeMillis();
    	if((now - time) > TIMEOUT){
    		throw new Exception(AppInvokeStatusCode.REQ_TIMEOUT);
    	}
    	return reMap;
    }
    
    /**
     * 解析APP请求参数
     * 需要解密数据
     * @param request
     * @return
     */
    public Map<String, Object> getAppRequestParameterAsObjectMap(HttpServletRequest request) throws Exception{
    	Map<String, Object> reMap = new HashMap<String, Object>();
    	String data = request.getParameter("data");
    	if(data == null || "".equals(data)){
    		throw new Exception(AppInvokeStatusCode.NO_DATA);
    	}
    	data = Cryption.deCrytorAppData(data);
    	reMap = JsonToTypeReference.jsonToMap(data);
    	String str = (String)reMap.get("time");
    	if(str == null || "".equals(str)){
    		throw new Exception(AppInvokeStatusCode.NO_TIME_STAMP);
    	}
    	long time = Long.parseLong(str);
    	long now = System.currentTimeMillis();
    	if((now - time) > TIMEOUT){
    		throw new Exception(AppInvokeStatusCode.REQ_TIMEOUT);
    	}
    	return reMap;
    }
    
    /**
     * 全局处理异常信息
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, HttpServletRequest request) {
        String url = request.getRequestURL().toString();
    	AppAjaxSupport ajax = new AppAjaxSupport();
        String error = e.getMessage();
        if(AppInvokeStatusCode.NOLOGING_ERROR.equals(error)){
        	ajax.setCode(error);
        	ajax.setMessage("未登录或者登陆超时!");
        }
        else if(AppInvokeStatusCode.NO_DATA.equals(error)){
        	ajax.setCode(error);
        	ajax.setMessage("接口非法参数访问!");
        }
        else if(AppInvokeStatusCode.NO_TIME_STAMP.equals(error)){
        	ajax.setCode(error);
        	ajax.setMessage("接口非法参数访问(时间戳不存在)!");
        }
        else if(AppInvokeStatusCode.REQ_TIMEOUT.equals(error)){
        	ajax.setCode(error);
        	ajax.setMessage("接口非法参数访问(时间戳过期)!");
        }
        else{
        	ajax.setCode(AppInvokeStatusCode.INNER_ERROR);
        	ajax.setMessage("接口内部错误!");
        }
        logger.error("用户请求URL:" + url + "出错!错误信息:" + error, e);
        logger.error(error);
        try {
            request.setAttribute(IS_AJAX, true);
            String msg = JsonToTypeReference.buildObjectMapper().writeValueAsString(ajax);
            if(ENCODE){
            	msg = Cryption.enCrytorAppData(msg);
            }
            request.setAttribute(ERROR_MSG, msg);
        } catch (Exception ex) {
        	logger.error("格式化json数据出错!", ex);
            ex.printStackTrace();
        }
        
        return ERROR_VIEW;
    }
    
    /**
     * 将返回对象转化为JSON字符串
     * @param o
     * @return
     * @throws Exception
     */
	public String getReturnJsonData(AppAjaxSupport o) throws Exception{
		String re = JsonToTypeReference.buildObjectMapper().writeValueAsString(o);
		if(!AppInvokeStatusCode.SUCCESS.equals(o.getCode())){
			logger.error("调用接口出错data=" + re);
		}
		re = Cryption.enCrytorAppData(re);
		return re;
	}
	
	/**
     * 将PAGE转化为EASYUI数据格式的PAGE
     * @param page
     * @return
     */
    @SuppressWarnings("rawtypes")
	public Map<String, Object> pageToJson(PageInfo page){

        Map<String, Object> reMap = new HashMap<String, Object>();
        reMap.put("rows", page.getList());
        reMap.put("pageNum", page.getPageNum());
        reMap.put("pageSize", page.getPageSize());
        reMap.put("size", page.getSize());
        //reMap.put("orderBy", page.getOrderBy());
        reMap.put("startRow", page.getStartRow());
        reMap.put("endRow", page.getEndRow());
        reMap.put("total", page.getTotal());
        reMap.put("pages", page.getPages());
        //reMap.put("firstPage", page.getFirstPage());
        reMap.put("prePage", page.getPrePage());
        reMap.put("nextPage", page.getNextPage());
        //reMap.put("lastPage", page.getLastPage());
        reMap.put("isFirstPage", page.isIsFirstPage());
        reMap.put("isLastPage", page.isIsLastPage());
        reMap.put("hasPreviousPage", page.isHasPreviousPage());
        reMap.put("hasNextPage", page.isHasNextPage());
        reMap.put("navigatePages", page.getNavigatePages());
        reMap.put("navigatepageNums", page.getNavigatepageNums());

        return reMap;
    }
    
    /**
     * 将request中的参数转化为MAP
     * @param request
     * @return
     */
    public Map<String, Object> getRequestParameterAsMap(HttpServletRequest request){
        Map<String, String[]> paraMap = request.getParameterMap();
        Map<String, Object> reMap = new HashMap<String, Object>();
        paraMap.forEach(new BiConsumer<String, String[]>() {
			@Override
			public void accept(String t, String[] u) {
				reMap.put(t, u[0]);
			}
		});
        return reMap;
    }
    
    /**
     * 将request中的参数转化为MAP
     * @param request
     * @return
     */
    public Map<String, String> getRequestParameterAsStringMap(HttpServletRequest request){
        Map<String, String[]> paraMap = request.getParameterMap();
        Map<String, String> reMap = new HashMap<String, String>();
        paraMap.forEach(new BiConsumer<String, String[]>() {
			@Override
			public void accept(String t, String[] u) {
				reMap.put(t, u[0]);
			}
		});
        return reMap;
    }

    /**
     * 写入trace日志
     * @param message
     */
    public void trace(Object message) {
        logger.trace(message);
    }

    /**
     * 写入trace日志(包含异常)
     * @param message
     * @param t
     */
    public void trace(Object message, Throwable t) {
        logger.trace(message, t);
    }

    /**
     * 写入debug日志
     * @param message
     */
    public void debug(Object message) {
        logger.debug(message);
    }
    /**
     * 写入debug日志(包含异常)
     * @param message
     * @param t
     */
    public void debug(Object message, Throwable t) {
        logger.debug(message, t);
    }
    /**
     * 写入info日志
     * @param message
     */
    public void info(Object message) {
        logger.info(message);
    }
    /**
     * 写入info日志(包含异常)
     * @param message
     * @param t
     */
    public void info(Object message, Throwable t) {
        logger.info(message, t);
    }
    /**
     * 写入warn日志
     * @param message
     */
    public void warn(Object message) {
        logger.warn(message);
    }
    /**
     * 写入warn日志(包含异常)
     * @param message
     * @param t
     */
    public void warn(Object message, Throwable t) {
        logger.warn(message, t);
    }
    /**
     * 写入error日志
     * @param message
     */
    public void error(Object message) {
        logger.error(message);
    }
    /**
     * 写入error日志(包含异常)
     * @param message
     * @param t
     */
    public void error(Object message, Throwable t) {
        logger.error(message, t);
    }
    /**
     * 写入fatal日志
     * @param message
     */
    public void fatal(Object message) {
        logger.fatal(message);
    }
    /**
     * 写入fatal日志(包含异常)
     * @param message
     * @param t
     */
    public void fatal(Object message, Throwable t) {
        logger.fatal(message, t);
    }
}
