package com.example.demo.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.exception.SystemException;
/**
 * 常见的增删查改方法，所有Service的父类
 * @Description 
 * @author raining_heavily
 * @date 2019年2月21日
 */
public interface BaseService<E> {

	/**
	 * 添加一条数据
	 * @param entity 实体
	 * @return
	 * @throws SystemException
	 */
	public int add(E entity) throws SystemException;
	
	/**
	 * 删除数据
	 * @param queryWrapper 条件
	 * @return
	 */
	public int delete(QueryWrapper<E> queryWrapper) throws SystemException;
	
	/**
	 * 获取一条数据
	 * @param queryWrapper 条件
	 * @return
	 */
	public E getOne(QueryWrapper<E> queryWrapper) throws SystemException;

	/**
	 * 根据主键获取一条数据
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	public E getOneById(long id) throws SystemException;
	
	/**
	 * 获取查询数据总条数
	 * @param queryWrapper 条件
	 * @return
	 */
	public int total(QueryWrapper<E> queryWrapper) throws SystemException;

	
	/**
	 * 获取多条数据
	 * @param queryWrapper 条件
	 * @return
	 */
	public List<E> getAll(Page<E> page, QueryWrapper<E> queryWrapper) throws SystemException;
	
	/**
	 * 根据查询条件，更新数据
	 * @param queryWrapper 条件
	 * @return
	 */
	public int update(E entity,QueryWrapper<E> queryWrapper) throws SystemException;
	
	/**
	 * 根据主键，更新数据
	 * @param entity 更新的实体
	 * @return
	 */
	public int updateById(E entity) throws SystemException;
	
}
