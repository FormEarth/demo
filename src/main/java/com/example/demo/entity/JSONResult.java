package com.example.demo.entity;

import com.alibaba.fastjson.annotation.JSONType;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.util.Util;

/**
 * 
 * json返回数据统一格式
 * 
 * @author Nidhogg
 * @date 2019年1月27日
 */
@JSONType(orders = { "code", "mesage", "time" })
public class JSONResult {

	/** 状态码 */
	private String code;
	/** 消息 */
	private String message;
	/** 时间 */
	private String time = Util.getSystemTime();

	public JSONResult() { }
	
	public JSONResult(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public JSONResult(ExceptionEnums exceptionEnums) {
		this.code = exceptionEnums.getCode();
		this.message = exceptionEnums.getMessage();
	}

	/**
	 * 默认的成功消息
	 * @return
	 */
	public static JSONResult success() {
		return new JSONResult(ExceptionEnums.DEFAULT_SUCCESS);
	}
	
	/**
	 * 默认的失败消息
	 * @return
	 */
	public static JSONResult fail() {
		return new JSONResult(ExceptionEnums.DEFAULT_FAIL);
	}
	
	/**
	 * @deprecated
	 * @param exceptionEnums
	 * @return
	 */
	public JSONResult setEnum(ExceptionEnums exceptionEnums) {
		this.code = exceptionEnums.getCode();
		this.message = exceptionEnums.getMessage();
		return this;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public String getTime() {
		return time;
	}

}
