package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.aop.annotation.StaticURL;
import com.example.demo.common.CollectionTypeEnum;
import com.example.demo.common.Dict;
import com.example.demo.common.FileSourceEnum;
import com.example.demo.entity.Collection;
import com.example.demo.entity.JSONDataResult;
import com.example.demo.entity.JSONResult;
import com.example.demo.entity.User;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.service.ApiService;
import com.example.demo.service.CollectionService;
import com.example.demo.service.UserService;
import com.example.demo.util.Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @author raniing_heavily
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userServiceImpl;
    @Autowired
    ApiService apiService;
    @Autowired
    CollectionService collectionService;


    /**
     * 查用户普通信息
     *
     * @param userId
     * @return
     * @throws SystemException
     */
    @RequestMapping(value = "{userId}/common", method = RequestMethod.GET)
    public JSONResult queryCommonUserInfoById(@PathVariable Long userId) throws SystemException {
        User user = userServiceImpl.queryCommonInfo(userId);
        return new JSONDataResult().add("user", user);
    }

    /**
     * 注册
     *
     * @param user
     * @return
     * @throws SystemException
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public JSONResult userRegister(User user) throws SystemException {

        userServiceImpl.userRegister(user);
        return JSONResult.success();
    }

    /**
     * 用户个人信息修改
     *
     * @param user 用户对象
     * @return
     * @throws SystemException
     */
    @StaticURL
    @RequestMapping(value = "/info", method = RequestMethod.PUT)
    public JSONResult updateOwnInfo(@RequestBody User user) throws SystemException {
        //TODO 用户在修改完用户信息时，shiro即session中并没有更新
        logger.info("上传信息：" + user);
        User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
        user.setUpdater(currentLoginUser.getUserId());
        userServiceImpl.update(user);
        User newUser = userServiceImpl.getOneById(currentLoginUser.getUserId());
        return new JSONDataResult().add("user", newUser);
    }

    /**
     * 用户头像、封面修改
     *
     * @param file 图片
     * @param type 修改的是头像还是背景
     * @return 新头像的访问URL
     * @throws SystemException
     */
    @StaticURL
    @RequestMapping(value = "/avatar", method = RequestMethod.PUT)
    public JSONResult updateOwnAvatarOrFrontCover(@RequestParam("image") MultipartFile file, String type) throws SystemException {
        User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
        User user = new User();
        String relativePath = "";
        if (Dict.AVATAR.equals(type)) {
            relativePath = apiService.singleImageUploadWithoutWatermark(file, FileSourceEnum.AVATAR);
            //将session中获用户信息一并更新
            loginUser.setAvatar(relativePath);
            user.setAvatar(relativePath);
        } else if (Dict.FRONTCOVER.equals(type)) {
            relativePath = apiService.saveOriginImage(file, FileSourceEnum.FRONT_COVER);
            loginUser.setFrontCover(relativePath);
            user.setFrontCover(relativePath);
        }
        user.setUserId(loginUser.getUserId());
        user.setUpdater(loginUser.getUserId());
        userServiceImpl.update(user);
        //TODO 删除用户的上一个头像或封面
        return new JSONDataResult().add("relativePath", relativePath);
    }

    /**
     * 用户执行关注、收藏、喜欢接口
     *
     * @param collection
     * @return
     */
    @RequestMapping(value = "/collection", method = RequestMethod.POST)
    public JSONResult userAddCollection(@RequestBody Collection collection) throws SystemException {

        User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
        collection.setUserId(currentLoginUser.getUserId());
        collection.setCollectTime(new Date());
        collectionService.add(collection);

        return JSONResult.success();
    }

    /**
     * 用户删除关注、收藏、喜欢接口
     *
     * @param collectionType
     * @param collectionId
     * @return
     */
    @RequestMapping(value = "/collection", method = RequestMethod.DELETE)
    public JSONResult userRemoveCollection(@RequestParam CollectionTypeEnum collectionType, @RequestParam String collectionId) throws SystemException {
        User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
        //因为该表没有主键，因此使用查询方式删除
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("user_id", currentLoginUser.getUserId())
                .eq("collection_type", collectionType)
                .eq("collection_id", collectionId);
        collectionService.delete(queryWrapper);
        return JSONResult.success();
    }

    /**
     * 密码修改
     * @param map 因为就两个参数，这里映射为map
     * @return
     * @throws SystemException
     */
    @RequestMapping(value = "/password", method = RequestMethod.PUT)
    public JSONResult modifyOwnPassword(@RequestBody Map<String,String> map) throws SystemException {
        String newPassword = map.get("newPassword");
        String oldPassword = map.get("oldPassword");
        logger.info("new:{},old:{}",newPassword,oldPassword);
        if (newPassword.equals(oldPassword)) {
            throw new SystemException(ExceptionEnums.SAME_PASSWORD_ERROR);
        }
        User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
        String password = Util.shiroPasswordWithSalt(oldPassword, currentLoginUser.getSalt());
        //密码匹配
        if (currentLoginUser.getPassword().equals(password)) {
            String timestamp = String.valueOf(System.currentTimeMillis());
            User user = new User();
            user.setUserId(currentLoginUser.getUserId());
            user.setSalt(timestamp);
            String encodePassword = Util.shiroPasswordWithSalt(newPassword, timestamp);
            user.setPassword(encodePassword);
            userServiceImpl.updateById(user);
            //更新session中的用户信息
            currentLoginUser.setPassword(encodePassword);
            currentLoginUser.setSalt(timestamp);
        } else {
            throw new SystemException(ExceptionEnums.PASSWORD_ERROR);
        }
        return JSONResult.success();
    }


    /**
     * 登出
     *
     * @param user
     * @return
     * @throws SystemException
     */
    @PostMapping(value = "/logout")
    public JSONResult userLogout(@RequestBody User user) throws SystemException {
        Subject subject = SecurityUtils.getSubject();
        //登出
        try {
            subject.logout();
        } catch (AuthenticationException e) {
            logger.info(user.getAccount() + "登出失败！");
            e.printStackTrace();
            throw new SystemException(ExceptionEnums.LOGOUT_FAIL);
        }
        return JSONResult.success();
    }

}
