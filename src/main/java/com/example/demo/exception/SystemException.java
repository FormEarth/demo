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

    private ExceptionEnums exceptionEnums;

    public SystemException(){
        this.exceptionEnums = ExceptionEnums.DEFAULT_FAIL;
    }

    public SystemException(ExceptionEnums exceptionEnums){
        this.exceptionEnums = exceptionEnums;
    }

    public ExceptionEnums getExceptionEnums() {
        return exceptionEnums;
    }
}
