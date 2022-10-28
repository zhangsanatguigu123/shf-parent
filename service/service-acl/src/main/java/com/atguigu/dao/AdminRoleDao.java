package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.AdminRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminRoleDao extends BaseDao<AdminRole> {
    List<Long> findRoleIdByAdminId(Long adminId);

    void deleteByAdminId(Long adminId);

    void insertBatch(@Param("adminId") Long adminId,@Param("roleIds") Long[] roleIds);
}
