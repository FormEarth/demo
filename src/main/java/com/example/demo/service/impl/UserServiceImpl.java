package com.example.demo.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.common.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.User;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.util.Util;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    @Autowired
    UserMapper userMapper;
    private String accessPref = SystemProperties.INSTANCE.getInstance().getProperty("image.access.url");

    @Override
    public User userLogin(String account, String password) throws SystemException {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("account", account);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new SystemException(ExceptionEnums.USER_NOT_EXIT);
        }
        String str = Util.md5Digest(password + user.getSalt());
        if (user.getPassword().equals(str)) {
            return user;
        } else {
            throw new SystemException(ExceptionEnums.PASSWORD_ERROR);
        }

    }

    @Override
    public int userRegister(User user) {
        return userMapper.insert(user);
    }

    @Override
    public List<User> getUsersByParams(Page<User> page, QueryWrapper<User> queryWrapper) {

        IPage<User> ipage = userMapper.selectPage(page, queryWrapper);
        return ipage.getRecords();
    }

    @Override
    public void update(User user) throws SystemException {
        int num = userMapper.updateOwnInfo(user);
        if (num < 1) {
            throw new SystemException(ExceptionEnums.DATA_UPDATE_FAIL);
        }
    }

    @Override
    public User queryCommonInfo(long id) throws SystemException {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("user_id", id).select("user_id", "user_name", "avatar", "sign","user_status","front_cover","personal_profile"));
        Util.userVerify(user);
        if (!StringUtils.isEmpty(user.getAvatar())) {
            user.setAvatar(accessPref + user.getAvatar());
        }
        if(!StringUtils.isEmpty(user.getFrontCover())){
            user.setFrontCover(accessPref + user.getFrontCover());
        }
        return user;
    }

}
