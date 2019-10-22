package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.User;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.util.Util;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	@Autowired
	UserMapper userMapper;
	
	@Override
	public User userLogin(String account, String password) throws SystemException {
		QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("account",account);
		User user = userMapper.selectOne(wrapper);
		if(user == null) {
			throw new SystemException(ExceptionEnums.USER_NOT_EXIT);
		}
		String str = Util.md5Digest(password + user.getSalt());
		if(user.getPassword().equals(str)) {
			return user;
		}else {
			throw new SystemException(ExceptionEnums.PASSWORD_ERROR);
		}
			
	}

	@Override
	public int userRegister(User user) {
		return userMapper.insert(user);
	}

	@Override
	public List<User> getUsersByParams(Page<User> page,QueryWrapper<User> queryWrapper) {
		
		IPage<User> ipage = userMapper.selectPage(page, queryWrapper);
		return ipage.getRecords();
	}

}
