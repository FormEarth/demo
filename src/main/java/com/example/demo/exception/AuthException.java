package com.example.demo.exception;

import java.io.Serializable;

import org.apache.shiro.authc.AuthenticationException;

public class AuthException extends AuthenticationException implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String msg;
	private ExceptionEnums exceptionEnums;
	
    
    public AuthException(ExceptionEnums exceptionEnums){
    	super(exceptionEnums.name());
        this.code = exceptionEnums.getCode(); 
        this.msg = exceptionEnums.getMessage();
        this.exceptionEnums = exceptionEnums;
    }

	public void setCode(String code) {
		this.code = code;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public ExceptionEnums getExceptionEnums() {
		return exceptionEnums;
	}
	
	
}
