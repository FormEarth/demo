package com.example.demo.shiro;

import com.example.demo.common.Dict;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 重写sessionId的获取方法（默认是从cookie中获取的）
 *
 * @author raining_heavily
 * @date 2019/11/19 12:20
 **/
public class SessionManager extends DefaultWebSessionManager {

    private static final String REFERENCED_SESSION_ID_SOURCE = "cookie";

    public SessionManager() {
        super();
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String sessionId = ((HttpServletRequest) request).getHeader(Dict.AUTHORIZATION);
        if (StringUtils.isEmpty(sessionId)) {
            //如果没有携带id参数则按照父类的方式在cookie进行获取
            return super.getSessionId(request, response);
        } else {
            //如果请求头中有 token 则其值为sessionId
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sessionId);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return sessionId;
        }
    }
}
