package com.example.demo.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author raining_heavily
 * @Description 自定义拦截器
 * @date 2019年2月20日
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        /**
         * 添加登录拦截器
         */
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/user/login","/user/auto/login", "/user/register", "/user/logout")
                .addPathPatterns("/**");
    }


}
