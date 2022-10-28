package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.UserFollow;
import com.atguigu.vo.UserFollowVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFollowDao extends BaseDao<UserFollow> {
    Integer findFollowBy(@Param("userId") Long userId,@Param("houseId") Long houseId);

    List<UserFollowVo> ListPage(Long userId);
}
