package com.example.demo.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.User;
import com.example.demo.exception.AuthException;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.service.PermissionService;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import com.example.demo.util.Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author raining_heavily
 * @description 自定义Realm实现
 * @date 2019年4月7日
 */
public class ShiroRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(ShiroRealm.class);

    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    PermissionService permissionService;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

//		// 添加角色和权限
//		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//
//		User user = (User) principalCollection.getPrimaryPrincipal();
//		// 保存所有角色
//		Set<String> allRoles = new HashSet<>();
//		// 保存所有权限
//		Set<String> allPermissions = new HashSet<>();
//		// 查询对应用户-角色
//		List<Role> roles = roleService.getRolesByAccount(user.getAccount());
//		for (Role role : roles) {
//			allRoles.add(role.getRoleName());
//			// 查询所有权限
//			// List<Permission> permissions = new ArrayList<>();
//			// 查询对应角色-权限
//			List<Permission> permissions = permissionService.getPermissionsByAccount(user.getAccount());
//			for (Permission permission : permissions) {
//				allPermissions.add(permission.getPermissionName());
//			}
//		}
//		// 添加角色
//		simpleAuthorizationInfo.addRoles(allRoles);
//		// 添加权限
//		simpleAuthorizationInfo.addStringPermissions(allPermissions);
//
//		logger.info(user.toString());
//
//		return simpleAuthorizationInfo;
        return null;
    }

    /**
     * 认证(登录)
     * Token 用户提交的信息；Info shiro持有的用户信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 1、登录认证的方法需要先执行，需要用他来判断登录的用户信息是否合法
        // 取得用户名
        String username = (String) token.getPrincipal();
        // 需要通过用户名取得用户的完整信息，利用业务层操作
        User user = new User();
        try {
            user = userService.getOne(new QueryWrapper<User>().eq("account", username));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new AuthException(ExceptionEnums.LOGIN_FAIL);
        }

        try {
            Util.userVerify(user);
        } catch (SystemException e) {
            throw new AuthException(e.getExceptionEnums());
        }

        // 若存在，将此用户存放到登录认证info中，无需自己做密码对比，Shiro会为我们进行密码对比校验
        return new SimpleAuthenticationInfo(
                user,
                user.getPassword(),
                ByteSource.Util.bytes(user.getSalt()),
                getName()
        );
    }

    /**
     * 清理缓存权限
     */
    public void clearCachedAuthorizationInfo() {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }

}
