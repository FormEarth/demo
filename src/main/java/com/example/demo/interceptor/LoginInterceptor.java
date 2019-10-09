package com.example.demo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
/**
 * 
 * @Description 登录拦截器
 * @author raining_heavily
 * @date 2019年2月20日
 */
@Component
public class LoginInterceptor implements HandlerInterceptor{

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String user =  (String) request.getSession().getAttribute("user");
		if(StringUtils.isEmpty(user)) {
			if(request.getSession(true).isNew()) {
				request.getSession().invalidate();
				throw new SystemException(ExceptionEnums.USER_NOT_LOGIN);
			}else {
				throw new SystemException(ExceptionEnums.LOGIN_STATUS_TIME);
			}
		}else {
			return true;
		}
	}

	
	
}
