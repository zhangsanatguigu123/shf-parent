package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Role;

import java.util.Map;

public interface RoleService extends BaseService<Role> {
//自定义方法
Map findAdminRoleList(Long adminId);

    void assignAdminRole(Long adminId, Long[] roleIds);
}
