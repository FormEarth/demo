package com.example.demo.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.DemoApplicationTests;
import com.example.demo.entity.User;
import com.example.demo.exception.SystemException;
/**
 * 
 * @description 用户service测试
 * @author raining_heavily
 * @date 2019年2月23日
 */
public class UserServiceImplTest extends DemoApplicationTests{

	@Autowired
	UserService userService;
	
//	@Test
//    public void testUserLogin() throws SystemException{
//		User user = userService.userLogin("wcy", "123");
//		System.out.println(user);
//		Assert.assertNotNull(user);
//    }
//	
//	@Test
//	public void getOne() throws SystemException{
//		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//		queryWrapper.eq("user_id", 7);
//		User user = userService.getOne(queryWrapper);
//		System.out.println(user);
//		Assert.assertNotNull(user);
//    }
	
	@Test
	public void getAll() throws SystemException{
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_status", 1);
		long current = 1;
		long size = 5;
		Page<User> page = new Page<>(current,size);
		page.setSize(5);
		List<User> users = userService.getAll(page,queryWrapper);
		System.out.println(users);
		Assert.assertNotNull(users);
    }
}
