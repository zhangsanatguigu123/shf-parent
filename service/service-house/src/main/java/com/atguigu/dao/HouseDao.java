package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.House;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HouseDao extends BaseDao<House> {
    List<HouseVo> findListByPage(@Param("vo") HouseQueryVo houseQueryVo);
}
