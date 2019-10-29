package com.example.demo.entity;

import java.util.HashMap;
import java.util.Map;

import com.example.demo.exception.ExceptionEnums;
/**
 * 带返回数据的json格式
 * @author Nidhogg
 * @date 2019年2月16日
 */
public class JSONDataResult extends JSONResult {
	
	/** 操作数据的map **/
	private HashMap<String, Object> data;
	
	/**
	 * 构造函数，只有请求成功，才会返回相关数据
	 */
	public JSONDataResult() {
		super(ExceptionEnums.DEFAULT_SUCCESS);
		this.data = new HashMap<>();
	}
	
	public JSONDataResult(ExceptionEnums exceptionEnums, HashMap<String, Object> data) {
		super(exceptionEnums);
		this.data = data;
	}
	
	/**
	 * 增加返回数据，可链式调用
	 * @param key key
	 * @param data 数据对象
	 * @return
	 */
	public JSONDataResult add(String key,Object data) {
		this.data.put(key, data);
		return this;
	}

	/**
	 * 批量增加返回数据，可链式调用
	 * @param map
	 * @return
	 */
	public JSONDataResult addAll(Map<String, Object> map) {
		this.data.putAll(map);
		return this;
	}

	public HashMap<String, Object> getData() {
		return data;
	}

}
