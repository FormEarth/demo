package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Dict;
import com.example.demo.entity.JSONDataResult;
import com.example.demo.entity.JSONResult;
import com.example.demo.entity.User;
import com.example.demo.exception.AuthException;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.service.UserService;

@Validated
@RestController
@RequestMapping(value="/demo/api/user")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserService  userServiceImpl;
	
	@CrossOrigin
	@RequestMapping(value="/login",method = RequestMethod.POST)
	public JSONResult userLogin(@RequestBody User  user) throws SystemException {
		Assert.notNull(user.getAccount(),"账号不能为空！");
		Assert.notNull(user.getPassword(),"密码不能为空！");
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getAccount(), user.getPassword());
        Subject subject = SecurityUtils.getSubject();
        //登录
        try {
			subject.login(usernamePasswordToken);
		} catch (Exception e) {
			logger.error(e.getMessage());
			if(e instanceof AuthException) {
				throw new SystemException(((AuthException) e).getExceptionEnums());
			}else {
				throw new SystemException(ExceptionEnums.PASSWORD_ERROR);
			}
			
		}
		User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        logger.info(Dict.CURRENT_USER_DATA + currentUser);
		return new JSONDataResult().add(Dict.CURRENT_USER_DATA,currentUser);
	}
	
	@RequestMapping(value="/register",method = RequestMethod.POST)
	public JSONResult userRegister(User user) throws SystemException {
		
		userServiceImpl.userRegister(user);
		return JSONResult.success();
	}
	
	@RequestMapping(value="/users/{pageSize}",method = RequestMethod.GET)
	public JSONResult getUsers(@PathVariable String pageSize) throws SystemException {
		
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_status", 1);
		
		long size = pageSize==null ? 1 : Long.valueOf(pageSize);
		
		Page<User> page = new Page<>(size,5);
		page.setSize(5);
		List<User> list = userServiceImpl.getAll(page, queryWrapper);
		return  new JSONDataResult().add("user", list);
	}
	
	//@RequestBody必须使用post请求方式
	@RequestMapping(value="/add",method = RequestMethod.POST)
	public JSONResult userAdd(
			@RequestBody(required=false)  
			@Valid
			User user, BindingResult bingdingResult) throws SystemException {
		System.out.println("user"+user);
		System.out.println("mail"+user.getMail());
		if(bingdingResult.hasErrors()) {
			return new JSONResult("1111",bingdingResult.getObjectName());
		}else {
			return JSONResult.success();
		}
		//userServiceImpl.add(user);
		
	}
	
	@RequestMapping(value="/logout",method = RequestMethod.POST)
	public JSONResult userLogout(HttpServletRequest request,@RequestBody User user) throws SystemException {
		Subject subject = SecurityUtils.getSubject();
        //登出
        try {
			subject.logout();
		} catch (AuthenticationException e) {
			logger.info(user.getAccount()+"登出失败！");
			e.printStackTrace();
			throw new SystemException(ExceptionEnums.LOGOUT_FAIL);
		}
		return JSONResult.success();
	}

}
