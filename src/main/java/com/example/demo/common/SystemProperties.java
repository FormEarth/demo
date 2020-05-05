package com.example.demo.common;

import com.example.demo.exception.ExceptionUtil;
import com.example.demo.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * 全局配置类
 * @author raining_heavily
 * @date 2020/4/18 22:54
 **/
public enum SystemProperties {
    /**
     *
     */
    INSTANCE;

    private final Logger logger = LoggerFactory.getLogger(SystemProperties.class);
    private Properties properties = null;

    private SystemProperties() {
        properties = new Properties();
        FileReader fr;
        ClassPathResource classPathResource = new ClassPathResource("application.properties");
        try {
            properties.load(classPathResource.getInputStream());
            //获取本机ip后加入配置
            String url = properties.getProperty("image.access.url");
            if(url == null){
                properties.put("image.access.url", "http://"+Util.getLocalRealIP()+":9090/static/thumbnail");
            }
        } catch (IOException ex) {
            logger.error(ExceptionUtil.getStackTraceString(ex));
            logger.error("system properties loading failed!");
            System.exit(-1);
        }

    }

    public Properties getInstance() {
        return this.properties;
    }

    public static void main(String[] args) {
        System.out.println(SystemProperties.INSTANCE.getInstance());
    }

}
