package com.example.demo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.common.Dict;
import com.example.demo.entity.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;

/**
 * @author raining_heavily
 * @Description 登录拦截器
 * @date 2019年2月20日
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("执行了拦截preHandle," + request.getRequestURI());
        //GET方法的请求不校验是否登录
        if (Dict.GET.equals(request.getMethod())) {
            return true;
        }
        User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
        System.out.println("当前用户：" + currentLoginUser);
        if (currentLoginUser == null) {
            throw new SystemException(ExceptionEnums.USER_NOT_LOGIN);
        } else {
            return true;
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        System.out.println("执行了afterCompletion");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        System.out.println("执行了拦截afterCompletion");
    }


}
