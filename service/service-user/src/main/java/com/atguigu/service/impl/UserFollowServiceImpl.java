package com.atguigu.service.impl;

import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.UserFollowDao;
import com.atguigu.entity.UserFollow;
import com.atguigu.service.DictService;
import com.atguigu.service.UserFollowService;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class UserFollowServiceImpl extends BaseServiceImpl<UserFollow> implements UserFollowService {
    @Autowired
    UserFollowDao userFollowDao;
    @Override
    public BaseDao<UserFollow> getBaseDao() {
        return userFollowDao;
    }

    @Override
    public boolean findFollowBy(Long userId, Long houseId) {
       Integer count= userFollowDao.findFollowBy(userId,houseId);
       if(count>0){
           return true;
       }
        return false;
    }
     @DubboReference
    DictService dictService;
    @Override
    public PageInfo<UserFollowVo> ListPage(Long userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<UserFollowVo> pageList=userFollowDao.ListPage(userId);
        for (UserFollowVo userFollowVo : pageList) {
            userFollowVo.setHouseTypeName(dictService.getNameById(userFollowVo.getHouseTypeId()));
            userFollowVo.setDirectionName(dictService.getNameById(userFollowVo.getDirectionId()));
            userFollowVo.setFloorName(dictService.getNameById(userFollowVo.getFloorId()));
        }
        return new PageInfo<>(pageList,5);
    }
}
