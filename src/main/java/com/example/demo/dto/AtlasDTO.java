package com.example.demo.dto;

import com.example.demo.entity.Atlas;
import com.example.demo.entity.User;

/**
 * @author raining_heavily
 * @date 2019/11/5 12:27
 **/
public class AtlasDTO extends Atlas {
    private User user;

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}
