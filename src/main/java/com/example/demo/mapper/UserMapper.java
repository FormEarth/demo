package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.User;


public interface UserMapper extends BaseMapper<User>{

    public int updateOwnInfo(User user);
}