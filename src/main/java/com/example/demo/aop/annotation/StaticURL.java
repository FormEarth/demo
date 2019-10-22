package com.example.demo.aop.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(METHOD)
/**
 * 
 * @author raining_heavily
 * @date 2019年10月17日
 * @time 下午9:30:12
 * @description 描述 该注解会给静态资源（图片、文件等）加上访问的前缀
 */
public @interface StaticURL {
	
}
