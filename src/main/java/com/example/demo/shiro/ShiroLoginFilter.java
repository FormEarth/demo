package com.example.demo.shiro;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.JSONResult;
import com.example.demo.exception.ExceptionEnums;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author raining_heavily
 */
public class ShiroLoginFilter extends FormAuthenticationFilter{
	
	@Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (isAjax(request)) {
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");
            JSONResult result = new JSONResult(ExceptionEnums.USER_NOT_LOGIN);
            httpServletResponse.getWriter().write(JSONObject.toJSON(result).toString());
        } else {
            //saveRequestAndRedirectToLogin(request, response);
            /**
             * @Mark 非ajax请求重定向为登录页面
             */
            httpServletResponse.sendRedirect("/login");
        }
        return false;
    }

    private boolean isAjax(ServletRequest request){
        String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
        if("XMLHttpRequest".equalsIgnoreCase(header)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
