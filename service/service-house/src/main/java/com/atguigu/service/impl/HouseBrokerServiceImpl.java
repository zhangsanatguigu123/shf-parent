package com.atguigu.service.impl;

import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.HouseBrokerDao;
import com.atguigu.entity.HouseBroker;
import com.atguigu.service.HouseBrokerService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class HouseBrokerServiceImpl extends BaseServiceImpl<HouseBroker> implements HouseBrokerService {
     @Autowired
    HouseBrokerDao houseBrokerDao;
    @Override
    public BaseDao<HouseBroker> getBaseDao() {
        return houseBrokerDao;
    }

    @Override
    public List<HouseBroker> findListByHouseId(Long houseId) {
        return houseBrokerDao.findListByHouseId(houseId);
    }

    @Override
    public HouseBroker findBrokerByHouseIdAndBrokerId(HouseBroker houseBroker) {
        return houseBrokerDao.findBrokerByHouseIdAndBrokerId(houseBroker);
    }
}
