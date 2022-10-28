package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Permission;

import java.util.List;
import java.util.Map;

public interface PermissionService extends BaseService<Permission> {
    List<Map> findPermissionByRoleId(Long roleId);

    void assignPermissionByRoleId(Long roleId,Long[] permissionIds);

    List<Permission> findMenuByAdminId(Long adminId);
}
