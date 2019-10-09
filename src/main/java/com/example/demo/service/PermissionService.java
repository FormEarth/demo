package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Permission;
/**
 * 
 * @description 权限service
 * @author raining_heavily
 * @date 2019年3月5日
 */
public interface PermissionService extends BaseService<Permission>{
	
	/**
	 * 根据账号查询权限
	 * @param account 账号
	 * @return 所有权限信息
	 */
	List<Permission> getPermissionsByAccount(String account);
	
}
