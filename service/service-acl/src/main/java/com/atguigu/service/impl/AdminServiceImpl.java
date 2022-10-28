package com.atguigu.service.impl;

import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseService;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.AdminDao;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@DubboService
public class AdminServiceImpl extends BaseServiceImpl<Admin> implements AdminService {
    //@Autowired
    @Resource
    AdminDao adminDao;
    @Override
    public BaseDao<Admin> getBaseDao() {
        return adminDao;
    }

    @Override
    public Admin findAdminByUserName(String username) {
        return adminDao.findAdminByUserName(username);
    }

    @Override
    public List<String> findPermissionCodeByAdminId(Long id) {
        return adminDao.findPermissionCodeByAdminId(id);
    }
}
