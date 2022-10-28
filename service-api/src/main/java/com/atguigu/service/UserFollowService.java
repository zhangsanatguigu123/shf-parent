package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.UserFollow;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;

public interface UserFollowService extends BaseService<UserFollow> {
    boolean findFollowBy(Long userId, Long houseId);

    PageInfo<UserFollowVo> ListPage(Long userId, Integer pageNum, Integer pageSize);
}
