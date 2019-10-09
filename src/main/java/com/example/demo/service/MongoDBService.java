package com.example.demo.service;

/**
 * 
 * @author raining_heavily
 * @param <T>
 * @date 2019年9月14日
 * @time 下午1:43:59
 * @description MongoDB相关操作
 */
public interface MongoDBService<T> {

	/**
	 * 创建对象
	 */
	public void saveTest(T t);

	/**
	 * 根据用户名查询对象
	 * 
	 * @return
	 */
	public T findTestById(String id);

	/**
	 * 更新对象
	 */
	public void updateTest(T t);

	/**
	 * 删除对象
	 * 
	 * @param id
	 */
	public void deleteTestById(String id);

}
