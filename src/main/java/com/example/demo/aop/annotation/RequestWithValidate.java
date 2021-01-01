package com.example.demo.aop.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import java.lang.annotation.RetentionPolicy;


/**
 * @author raining_heavily
 * @date 2020/7/14 21:52
 **/

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD})
@RequestMapping
public @interface RequestWithValidate {

    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default "";

    @AliasFor(annotation = RequestMapping.class, attribute = "method")
    RequestMethod[] method() default RequestMethod.GET;

    /**
     * 校验的json字符串，适用较少的校验规则
     * @return
     */
    String validate() default "";

    /**
     * 校验的json文件，适用较多较复杂的校验规则，默认为path替换/为_的文件
     * @return
     */
    String file() default "";

}
