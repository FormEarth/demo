package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Permission;
import com.example.demo.mapper.PermissionMapper;
import com.example.demo.service.PermissionService;
@Service
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService{

	@Autowired
	PermissionMapper permissionMapper;
	
	@Override
	public List<Permission> getPermissionsByAccount(String account) {
		
		return permissionMapper.getPermissionsByAccount(account);
	}

}
