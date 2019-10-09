package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Role;

public interface RoleService extends BaseService<Role>{

	/**
	 * 根据账号查询角色
	 * @param account 账号
	 * @return 所有角色信息
	 */
	List<Role> getRolesByAccount(String account);
}
