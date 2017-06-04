package com.ssr.base.util;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * 获取SpringBeanContext中通过WebContext注入的Bean
 *
 * @author 
 */
public class SpringBeanContext {

    private static WebApplicationContext webApplicationContext;

    static {
        webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
    }

    public static WebApplicationContext getWebApplicationContext(){
        return webApplicationContext;
    }

    public static <T> T getBean(Class<T> clazz){
        return webApplicationContext.getBean(clazz);
    }

    public static Object getBean(String beanId){
        return webApplicationContext.getBean(beanId);
    }
    
    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType){
    	return webApplicationContext.getBeansWithAnnotation(annotationType);
    }

}
