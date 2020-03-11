package com.example.demo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 全局的跨域配置
 * spring的@ConditionalOnProperty注解用来根据配置文件的属性条件化添加配置
 * havingValue:当值为true时会被加载;matchIfMissing:属性缺失时的值为false
 *
 * @author raining_heavily
 */
@Configuration
@ConditionalOnProperty(
        value = "global.cors.enable",
        havingValue = "true",
        matchIfMissing = false)
public class CorsConfig {

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 允许任何域名
        corsConfiguration.addAllowedOrigin("*");
        // 允许任何头
        corsConfiguration.addAllowedHeader("*");
        // 允许任何方法
        corsConfiguration.addAllowedMethod("*");
        //复杂的跨域请求会在请求之前有一个OPTIONS的预请求，来检查服务器是否允许跨域，此项用来设置在多长时间里不需要再发送预请求来检查
        corsConfiguration.setMaxAge(60*60L);
        // 跨域时允许携带cookie
//        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// 注册
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

}
