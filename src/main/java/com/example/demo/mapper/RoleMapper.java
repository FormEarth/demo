package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.Role;

@Mapper
public interface RoleMapper extends BaseMapper<Role>{

	/**
	 * 根据账号查询角色
	 * @param account 账号
	 * @return 所有角色信息
	 */
	List<Role> getRolesByAccount(@Param("account") String account);
}
