package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.service.BaseService;
/**
 * BaseService的实现类，该类同样需要一个泛型
 * @Description 
 * @author raining_heavily
 * @date 2019年2月21日
 */
public class BaseServiceImpl<E> implements BaseService<E>{

	@Autowired
	BaseMapper<E> baseMapper;
	
	@Override
	public int add(E entity) throws SystemException {	
		int i = 0;
		try {
			i = baseMapper.insert(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(ExceptionEnums.DATA_INSERT_FAIL);
		}
		if(i < 1) throw new SystemException(ExceptionEnums.DATA_INSERT_FAIL);
		return i;
	}

	@Override
	public int delete(QueryWrapper<E> queryWrapper) throws SystemException {
		int i = 0;
		try {
			i = baseMapper.delete(queryWrapper);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(ExceptionEnums.DATA_DELETE_FAIL);
		}
		if(i < 1) throw new SystemException(ExceptionEnums.DATA_DELETE_FAIL);
		return i;
	}

	@Override
	public E getOne(QueryWrapper<E> queryWrapper) throws SystemException {
		E entity = null;
		try {
			entity = baseMapper.selectOne(queryWrapper);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(ExceptionEnums.DATA_SELECT_FAIL);
		}
		return entity;
	}

	@Override
	public List<E> getAll(Page<E> page, QueryWrapper<E> queryWrapper) throws SystemException {
		List<E> list = new ArrayList<>();
		try {
			list = baseMapper.selectPage(page, queryWrapper).getRecords();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(ExceptionEnums.DATA_SELECT_FAIL);
		}
		return list;
	}

	@Override
	public int update(E entity, QueryWrapper<E> queryWrapper) throws SystemException {
		int i = 0;
		try {
			i = baseMapper.update(entity, queryWrapper);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(ExceptionEnums.DATA_UPDATE_FAIL);
		}
		return i;
	}

	@Override
	public int updateById(E entity) throws SystemException {
		int i = 0;
		try {
			i= baseMapper.updateById(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(ExceptionEnums.DATA_UPDATE_FAIL);
		}
		return i;
	}

	@Override
	public int total(QueryWrapper<E> queryWrapper) throws SystemException {
		return 1;
	}
		

}
