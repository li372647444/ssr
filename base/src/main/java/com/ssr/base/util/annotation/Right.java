package com.ssr.base.util.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.Mapping;

@Documented
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Mapping
public @interface Right {

	String id();

	String name();
	
	String moduleId();
	
	String moduleName();

	String desc() default "";
	
	String moduleDesc() default "";
	
	int order() default 0;
	
	int moduleOrder() default 0;

	boolean isMenu() default false;
}
