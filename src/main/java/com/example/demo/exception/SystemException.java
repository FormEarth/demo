package com.example.demo.exception;

import java.io.Serializable;

/**
 * 
 * @Description 自定义系统异常
 * @author raining_heavily
 * @date 2019年2月17日
 */

public class SystemException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String code;
	private String message;
    
    public SystemException(ExceptionEnums exceptionEnums){
    	super(exceptionEnums.name());
        this.code = exceptionEnums.getCode(); 
        this.message = exceptionEnums.getMessage();
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
