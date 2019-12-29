package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 解决put请求参数无法接收问题
 *
 * @author raining_heavily
 * @date 2019/12/24 21:34
 **/
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Bean
    public FormContentFilter formContentFilter() {
        return new FormContentFilter();
    }
}
