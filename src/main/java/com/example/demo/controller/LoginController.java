package com.example.demo.controller;

import com.example.demo.aop.annotation.StaticURL;
import com.example.demo.common.Dict;
import com.example.demo.entity.Collection;
import com.example.demo.entity.JSONDataResult;
import com.example.demo.entity.JSONResult;
import com.example.demo.entity.User;
import com.example.demo.exception.AuthException;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.service.CollectionService;
import com.example.demo.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 登录接口<br>
 *
 * @author raining_heavily
 * @date 2019/11/20 13:26
 **/
@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userServiceImpl;
    @Autowired
    CollectionService collectionService;

    /**
     * 用户账号密码登录
     * @param user
     * @param rememberMe
     * @return
     * @throws SystemException
     */
    @StaticURL
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public JSONResult userLogin(@RequestBody User user, boolean rememberMe) throws SystemException, CloneNotSupportedException {

        Assert.notNull(user.getAccount(), "账号不能为空！");
        Assert.notNull(user.getPassword(), "密码不能为空！");
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getAccount(), user.getPassword());
        if (rememberMe) {
            usernamePasswordToken.setRememberMe(true);
        }
        Subject subject = SecurityUtils.getSubject();
        //账号密码登录
        try {
            subject.login(usernamePasswordToken);
        } catch (Exception e) {
            if (e instanceof AuthException) {
                throw new SystemException(((AuthException) e).getExceptionEnums());
            } else {
                throw new SystemException(ExceptionEnums.PASSWORD_ERROR);
            }
        }
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        JSONDataResult result = new JSONDataResult();
        User resultUser = currentUser.clone();
        //置空密码信息
        resultUser.setPassword(null);
        resultUser.setSalt(null);
        //初始化用户关注、喜欢、收藏列表
        initCollectionList(currentUser.getUserId(), result);
        return result.add(Dict.CURRENT_USER_DATA, resultUser)
                .add("AuthorizationSessionId", subject.getSession().getId());

    }

    /**
     * 记住密码下次自动登录
     * @param request
     * @return
     * @throws SystemException
     */
    @StaticURL
    @RequestMapping(value = "/user/auto/login", method = RequestMethod.POST)
    public JSONResult userLogin(HttpServletRequest request) throws SystemException, CloneNotSupportedException {
        String sessionId = request.getHeader(Dict.AUTHORIZATION);
        User user;
        if (StringUtils.isEmpty(sessionId)) {
            throw new SystemException(ExceptionEnums.USER_NOT_LOGIN);
        } else {
            if (SecurityUtils.getSubject().getPrincipal() == null) {
                throw new SystemException(ExceptionEnums.LOGIN_STATUS_TIMEOUT);
            } else {
                user = (User) SecurityUtils.getSubject().getPrincipal();
            }
        }
        User resultUser = user.clone();
        //置空密码信息
        resultUser.setPassword(null);
        resultUser.setSalt(null);
        JSONDataResult result = new JSONDataResult().add(Dict.CURRENT_USER_DATA, resultUser);
        initCollectionList(user.getUserId(), result);
        return result;
    }


    /**
     * 初始化用户关注、喜欢、收藏列表
     *
     * @param userId
     * @param result
     */
    private void initCollectionList(Long userId, JSONDataResult result) throws SystemException {
        List<Collection> allCollections = collectionService.queryAllCollectionsByUserId(userId);
        //用户关注列表
        Set<Long> userWatchSet = new HashSet<>();
        //用户喜欢列表
        Set<String> userLikeSet = new HashSet<>();
        //用户收藏列表
        Set<String> userKeepSet = new HashSet<>();

        for (Collection collection : allCollections) {
            String collectionId = collection.getCollectionId();
            switch (collection.getCollectionType()) {
                case Dict.USER_WATCH:
                    userWatchSet.add(Long.valueOf(collectionId));
                    break;
                case Dict.WRITING_LIKE:
                    userLikeSet.add(collectionId);
                    break;
                case Dict.WRITING_KEEP:
                    userKeepSet.add(collectionId);
                    break;
                default:
                    throw new SystemException(ExceptionEnums.DATA_HANDLE_ERROR);
            }
        }
        result.add("user_watch_list", userWatchSet)
                .add("user_like_list", userLikeSet)
                .add("user_keep_list", userKeepSet);
    }
}
