package com.example.demo.shiro;

import com.example.demo.entity.JSONResult;
import com.example.demo.exception.ExceptionEnums;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.web.filter.authc.UserFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 
 * @description 重写默认的未登录重定向为返回json数据
 * @author raining_heavily
 * @date 2019年4月21日
 */
public class LoginFilter  extends UserFilter{

	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		// 设置响应头
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		// 设置返回的数据
		JSONResult result = new JSONResult(ExceptionEnums.USER_NOT_LOGIN);
		// 写回给客户端
		PrintWriter out = response.getWriter();
		ObjectMapper mapper = new ObjectMapper();
		out.write(mapper.writeValueAsString(result));
		// 刷新和关闭输出流
		out.flush();
		out.close();
	}

}
