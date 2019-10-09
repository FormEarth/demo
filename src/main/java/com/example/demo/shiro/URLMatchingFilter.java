package com.example.demo.shiro;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Permission;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.service.PermissionService;

/**
 * 
 * @description 基于URL判断用户是否有权限进行操作
 * @author raining_heavily
 * @date 2019年3月5日
 */
@Component
public class URLMatchingFilter extends PathMatchingFilter{

	private static final Logger logger = LoggerFactory.getLogger(URLMatchingFilter.class);
	
	@Autowired
	PermissionService permissionService;
//	private static URLMatchingFilter urlMatchingFilter;
//	@PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
//    public void init() {
//		urlMatchingFilter = this;
//		urlMatchingFilter.permissionService = this.permissionService;
//        // 初使化时将已静态化的testService实例化
//    }
	
	@Override
	protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		
		String requestURL = getPathWithinApplication(request);
        logger.info("请求的url :"+requestURL);
        Subject subject = SecurityUtils.getSubject();
        
        if (!subject.isAuthenticated()){
            // 如果没有登录, 直接返回true 进入登录流程
            return  true;
        }
        //User user = (User) subject.getPreviousPrincipals().getPrimaryPrincipal();
        
        
        String account = (String) subject.getPrincipal();
        logger.info("---------"+account);
        
        /** 查询账号下的所有权限 **/
        List<Permission> permissions = new ArrayList<>();
       logger.info("----------"+String.valueOf(permissionService.hashCode()));
        permissions = permissionService.getPermissionsByAccount(account);
        System.out.println(permissions.size());
 
        boolean hasPermission = false;
        for (Permission url : permissions) {
              if (url.getUrl().equals(requestURL)){
                  hasPermission = true;
                  break;
              }
        }
        if (hasPermission){
            return true;
        }else {
//            UnauthorizedException ex = new UnauthorizedException("当前用户没有访问路径" + requestURL + "的权限");
//            subject.getSession().setAttribute("ex",ex);
//            WebUtils.issueRedirect(request, response, "/unauthorized");
//            return false;
        	throw new SystemException(ExceptionEnums.UNAUTH_ERROR);
        }
 
    }


	
}
