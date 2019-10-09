package com.example.demo.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
/**
 * 
 * @Description 自定义拦截器
 * @author raining_heavily
 * @date 2019年2月20日
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport{

	@Autowired
	LoginInterceptor loginInterceptor;

	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		
		/**
		 * 添加登录拦截器
		 */
		//registry.addInterceptor(loginInterceptor).addPathPatterns("/api/user/*").excludePathPatterns("/api/user/login");
	}
	
	
	
	
}
