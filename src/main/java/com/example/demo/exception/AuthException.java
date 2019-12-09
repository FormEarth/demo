package com.example.demo.exception;

import java.io.Serializable;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @author chunyangwang
 */
public class AuthException extends AuthenticationException implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ExceptionEnums exceptionEnums;

    public AuthException(ExceptionEnums exceptionEnums) {
        this.exceptionEnums = exceptionEnums;
    }

    public ExceptionEnums getExceptionEnums() {
        return exceptionEnums;
    }

}
