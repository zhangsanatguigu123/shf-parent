package com.atguigu.service.impl;

import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.UserInfoDao;
import com.atguigu.entity.UserInfo;
import com.atguigu.service.UserInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfo> implements UserInfoService {
     @Autowired
     UserInfoDao userInfoDao;
    @Override
    public BaseDao<UserInfo> getBaseDao() {
        return userInfoDao;
    }

    @Override
    public UserInfo findUserByPhone(String phone) {
        return userInfoDao.findUserByPhone(phone);
    }
}
