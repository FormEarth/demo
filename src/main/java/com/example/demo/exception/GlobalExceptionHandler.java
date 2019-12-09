package com.example.demo.exception;

import javax.validation.ConstraintViolationException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.example.demo.entity.JSONResult;

/**
 * 
 * @Description 全局异常处理
 * @author raining_heavily
 * @date 2019年2月17日
 */
@RestControllerAdvice
/*
@RestControllerAdvice注解相当于ControllerAdvice和@ResponseBody的组合
@ControllerAdvice是controller的一个辅助类，最常用的就是作为全局异常处理的切面类
@ControllerAdvice可以指定扫描范围，使用方式：@ControllerAdvice(basePackages ="com.example.demo.controller")
@ControllerAdvice约定了几种可行的返回值，如果是直接返回model类的话，需要使用@ResponseBody进行json转换
	返回String，表示跳到某个view
	返回modelAndView
	返回model + @ResponseBody
*/
public class GlobalExceptionHandler {
	
	/**
	 * 处理自定义的SystemException
	 * @param ex
	 * @return
	 */
    @ExceptionHandler(value = SystemException.class)
	public JSONResult systemExceptionHandler(SystemException ex){
		ex.printStackTrace();
        return new JSONResult(ex.getExceptionEnums().getCode(),ex.getExceptionEnums().getMessage());
     }
	
	/**
	 * 处理404
	 * @param ex
	 * @return
	 */
    @ExceptionHandler(value = NoHandlerFoundException.class)
	public JSONResult noHandlerFoundExceptionHandler(NoHandlerFoundException ex){
		ex.printStackTrace();
        return new JSONResult(ExceptionEnums.API_NOT_FOUND);
     }
    
	/**
	 * 处理由Hibernate-Validator抛出的字段验证不通过的ConstraintViolationException、MethodArgumentNotValidException
	 * @param ex
	 * @return
	 */
    @ExceptionHandler(value = {ConstraintViolationException.class,MethodArgumentNotValidException.class})
	public JSONResult constraintViolationExceptionHandler(ConstraintViolationException ex){
		ex.printStackTrace();
        return new JSONResult("4444", ex.getMessage());
     }
	
	/**
	 * 处理未定义的未知RuntimeException
	 * @param ex
	 * @return
	 */
    @ExceptionHandler(value = RuntimeException.class)
	public JSONResult exceptionHandler(RuntimeException ex){
		ex.printStackTrace();
//		JSONResult result = new JSONResult();
//		if(ex instanceof NoHandlerFoundException) {
//			return result.setEnum(ExceptionEnums.API_NOT_FOUND);
//		}else if(ex instanceof SystemException) {
//			return new JSONResult(ex.getCode(), ex.getMsg());
//		}else if(ex instanceof ConstraintViolationException||ex instanceof MethodArgumentNotValidException) {
//			return result.setEnum(ExceptionEnums.UNKNOWN_ERROR);
//		}else {
			//发生异常进行日志记录，写入数据库或者其他处理，此处省略
	        return new JSONResult(ExceptionEnums.UNKNOWN_ERROR);
//		}
        
     }

}
