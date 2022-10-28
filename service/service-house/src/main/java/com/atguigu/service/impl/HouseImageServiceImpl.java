package com.atguigu.service.impl;

import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.HouseImageDao;
import com.atguigu.entity.HouseImage;
import com.atguigu.service.HouseImageService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class HouseImageServiceImpl extends BaseServiceImpl<HouseImage> implements HouseImageService {
     @Autowired
    HouseImageDao houseImageDao;
    @Override
    public BaseDao<HouseImage> getBaseDao() {
        return houseImageDao;
    }

    @Override
    public List<HouseImage> findListByHouseId(Long houseId, int i) {
        return houseImageDao.findListByHouseId(houseId,i);
    }
}
