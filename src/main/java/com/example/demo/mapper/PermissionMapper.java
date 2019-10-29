package com.example.demo.mapper;

import java.util.List;

import com.example.demo.entity.Permission;

public interface PermissionMapper extends BaseMapper<Permission>{

	/**
	 * 根据账号查询权限
	 * @param account 账号
	 * @return 所有权限信息
	 */
	List<Permission> getPermissionsByAccount(String account);
}
