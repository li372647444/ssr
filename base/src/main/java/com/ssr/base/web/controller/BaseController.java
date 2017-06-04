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
import com.ssr.base.util.AjaxSupport;
import com.ssr.base.util.conversion.CustomDateTimeEditor;
import com.ssr.base.util.json.JsonToTypeReference;

/**
 * 所有Controller基类,必须继承
 *
 * @author 
 */
public abstract class BaseController {

    protected static String ERROR_MSG = "errorMsg";
    protected static String IS_AJAX = "isAjax";
    protected static String ERROR_TXT = "访问系统出错,请联系管理员!";
    protected static String ERROR_VIEW = "base/error";

    private Logger logger = Logger.getLogger(getClass());

    /**
     * 处理日期转换
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(Date.class, new CustomDateTimeEditor(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"), true));
    }

    /**
     * 全局处理异常信息
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, HttpServletRequest request) {
    	String txt = e.getMessage();
    	e.printStackTrace();
    	if(!txt.startsWith("auth")){
    		txt = ERROR_TXT;
    	}
        String url = request.getRequestURL().toString();
        logger.error("用户请求URL:" + url + "出错!", e);
        if((request.getHeader("accept").contains("application/json") || (request
                .getHeader("X-Requested-With")!= null && request
                .getHeader("X-Requested-With").contains("XMLHttpRequest")))) {
            AjaxSupport<Object> ajax = new AjaxSupport<Object>();
            ajax.setSuccess(false);
            ajax.setErrorMessage(txt);
            ajax.setMessage(txt);
            try {
                request.setAttribute(IS_AJAX, true);
                request.setAttribute(ERROR_MSG, JsonToTypeReference.buildObjectMapper().writeValueAsString(ajax));
            } catch (Exception ex) {
                logger.error("格式化json数据出错!", ex);
                ex.printStackTrace();
            }
        }
        else {
            request.setAttribute(ERROR_MSG, txt);
        }
        return ERROR_VIEW;
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
     * 将PAGE转化为前端翻页HTML
     * @param page
     * @param url
     * @param hasParam
     * @param mod
     * @return
     */
    @SuppressWarnings("rawtypes")
    public String pageToHtml(PageInfo page, String url, boolean hasParam, String mod){
    	return pageToHtml(page, url, hasParam, null, mod);
    }
    
    /**
     * 将PAGE转化为前端翻页HTML
     * @param page
     * @param url
     * @param hasParam
     * @param pageName
     * @param mod
     * @return
     */
    @SuppressWarnings("rawtypes")
    public String pageToHtml(PageInfo page, String url, boolean hasParam, String pageName, String mod){
    	StringBuilder sb = new StringBuilder();
    	int pages = page.getPages();
    	int pageNum = page.getPageNum();
    	boolean hasMod = true;
    	String pageStr;
    	if("".equals(mod) || null == mod){
    		hasMod = false;
    	}
    	if(null == pageName || "".equals(pageName)){
    		pageName = "page";
    	}
    	if(hasParam){
    		pageStr = "&" + pageName + "=";
    	}
    	else{
    		pageStr = "?" + pageName + "=";
    	}
    	
    	sb.append("<div class='page'>");
    	
    	if(pages <= 0){
        	sb.append("<span class='fl pr15 ml50'>");
        	sb.append("第1页");
        	sb.append("共1页");
        	sb.append("（共0条记录）");
        	sb.append("</span>");
    	}
    	else{
    		sb.append("<span class='fl pr15 ml50'>");
        	sb.append("第").append(page.getPageNum()).append("页");
        	sb.append("共").append(pages).append("页");
        	sb.append("（共").append(page.getTotal()).append("条记录）");
        	sb.append("</span>");
        	if(pageNum == 1 && pages > 1){
        		if(hasMod){
        			sb.append("<a href='javascript:void(0);'><span class='page-link prev'></span></a>");
        		}
        		else{
        			sb.append("<a href='").append(mod).append("'><span class='page-link prev'></span></a>");
        		}
        	}
        	if(pageNum > 1){
        		sb.append("<a href='").append(url).append(pageStr).append("1");
        		if(hasMod){
        			sb.append(mod);
        		}
        		sb.append("' class='page-link'>首页</a>");
        		
        		sb.append("<a href='").append(url).append(pageStr).append(pageNum - 1);
        		if(hasMod){
        			sb.append(mod);
        		}
        		sb.append("' class='page-link prev'></a>");
        	}
        	if(pages > 1){
        		int total = 1;
        		int max = 5;
        		int index = pages - pageNum;
        		if (index > 2) {
    				index = 2;
    			} 
        		else {
    				index = index <= 0 ? (max - 1) : (max - index - 1);
    			}
        		int i;
        		for (i = (pageNum - index); i <= pages && total <= max; i++) {
    				if (i <= 0) {
    					continue;
    				}
    				if (pageNum == i) {
    					sb.append("<a href='javascript:void(0);' class='page-link on'>");
    					sb.append(i);
    					sb.append("</a>");
    				} else {
    					sb.append("<a href='");
    					sb.append(url).append(pageStr).append(i);
    					if (hasMod) {
    						sb.append(mod);
    					}
    					sb.append("' class='page-link'>");
    					sb.append(i);
    					sb.append("</a>");
    				}
    				total++;
    			}
        		if (i < pages) {
        			sb.append("<span>...</span>");
    				int idx = pages - 2;
    				if (i <= idx) {
    					sb.append("<a href='").append(url).append(pageStr).append(idx);
    					if (hasMod) {
    						sb.append(mod);
    					}
    					sb.append("' class='page-link'>");
    					sb.append(idx);
    					sb.append("</a>");
    				}
    				idx++;
    				if (i <= idx) {
    					sb.append("<a href='").append(url).append(pageStr).append(idx);
    					if (hasMod) {
    						sb.append(mod);
    					}
    					sb.append("' class='page-link'>");
    					sb.append(idx);
    					sb.append("</a>");
    				}
    			}
        	}
        	if (pageNum < pages) {
    			sb.append("<a href='").append(url).append(pageStr).append(pageNum + 1);
    			if (hasMod) {
					sb.append(mod);
				}
    			sb.append("' class='page-link next'></a>");

    			sb.append("<a href='").append(url).append(pageStr).append(pages);
    			if (hasMod) {
					sb.append(mod);
				}
    			sb.append("' class='page-link'>尾页</a>");
    		}
    		if (pageNum == pages && pages > 1) {
    			sb.append("<a href='javascript:void(0);'><span class='page-link next'></span></a>");
    		}
    	}
    	
    	sb.append("</div>");
    	return sb.toString();
    }
}
