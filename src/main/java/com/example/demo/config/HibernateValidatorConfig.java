package com.example.demo.config;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @description 配置Hibernate-Validator，该模块集成于spring-boot-starter-web
 * @author raining_heavily
 * @date 2019年2月23日
 */
@Configuration
public class HibernateValidatorConfig {
	
	 @Bean
	 public Validator validator() {
		 ValidatorFactory factory = Validation.byProvider(HibernateValidator.class)
				 .configure()
	             .failFast(true) //开启快速失败模式
	             .buildValidatorFactory();
	     return factory.getValidator();
	   }
	
//	@Bean
//    public ResourceBundleMessageSource getMessageSource() {
//        ResourceBundleMessageSource rbms = new ResourceBundleMessageSource();
//        rbms.setDefaultEncoding("UTF-8");
////        rbms.setBasenames("i18n/errors/ErrorMessages", "i18n/prompt/PromptMessages",
////                "i18n/validation/ValidationMessages");
//        return rbms;
//    }
// 
//    @Bean
//    public Validator validator() {
//    	ResourceBundleMessageSource rbms = new ResourceBundleMessageSource();
//        rbms.setDefaultEncoding("UTF-8");
//        //rbms.setBasename("classpath:i18n/");
//        
//        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
//        validator.setValidationMessageSource(rbms);
//        
//        return validator;
//    }
	 
}
