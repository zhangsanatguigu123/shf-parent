package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.RolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RolePermissionDao extends BaseDao<RolePermission> {
    List<Long> findPermissionIdByRoleId(Long roleId);

    void deleteByRoleId(Long roleId);

    void insetBatch(@Param("roleId") Long roleId,@Param("permissionIds") Long[] permissionIds);
}
