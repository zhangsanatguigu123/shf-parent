package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.Admin;

import java.util.List;

public interface AdminDao extends BaseDao<Admin> {
    Admin findAdminByUserName(String username);

    List<String> findPermissionCodeByAdminId(Long id);
}
