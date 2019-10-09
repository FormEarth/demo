package com.example.demo.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.User;
import com.example.demo.exception.SystemException;
/**
 * 
 * @Description 用户相关service
 * @author raining_heavily
 * @date 2019年2月17日
 */
public interface UserService extends BaseService<User>{

	/**
	 * 用户登录
	 * @param account 账号
	 * @param password 密码
	 * @return 登录用户信息
	 * @throws SystemException 系统异常
	 */
	public User userLogin(String account,String password) throws SystemException;
	
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	public int userRegister(User user);
	
	/**
	 * 根据查询条件查询用户
	 * @param page 分页对象
	 * @param queryWrapper 查询对象
	 * @return 用户对象
	 */
	public List<User> getUsersByParams(Page<User> page, QueryWrapper<User> queryWrapper);
	
	
}
