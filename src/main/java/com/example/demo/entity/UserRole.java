package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 
 * @description 用户_角色表
 * @author raining_heavily
 * @date 2019年2月28日
 */

@TableName(value="user_role")
public class UserRole {
	
	@TableId
	/** 用户id **/
	private long userId;
	/** 角色id **/
	private long roleId;
	
	@Override
	public String toString() {
		return "UserRole [userId=" + userId + ", roleId=" + roleId + "]";
	}
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

}
